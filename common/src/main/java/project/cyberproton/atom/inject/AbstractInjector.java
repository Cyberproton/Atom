package project.cyberproton.atom.inject;

import com.google.common.reflect.TypeToken;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.util.PackageFilter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class AbstractInjector implements Injector {
    private final AtomPlugin plugin;
    protected final AbstractInjector parent;
    protected final Config config;
    protected final Map<TypeToken<?>, Provider<?>> providers = new HashMap<>();
    protected final Map<TypeToken<?>, Object> instances = new HashMap<>();
    protected final Set<ClassInfo<?>> injected = new HashSet<>();

    public AbstractInjector(@Nullable AbstractInjector parent, @NotNull Injector.Config config, @Nullable AtomPlugin plugin, @NotNull List<Provider<?>> initialProviders) {
        this.parent = parent;
        this.config = config;
        this.plugin = plugin;
        for (Provider<?> provider : initialProviders) {
            providers.put(provider.getType(), provider);
        }
        setup();
    }

    public AbstractInjector() {
        this(null, Config.DEFAULT, null, new ArrayList<>());
    }

    private void setup() {
        for (Provider<?> provider : providers.values()) {
            instances.put(provider.getType(), provider.getInstanceSupplier().get());
        }
        if (config.getPackageFilters().contains(PackageFilter.NONE)) {
            return;
        }
        URL url;
        if (plugin == null) {
            url = getClass().getProtectionDomain().getCodeSource().getLocation();
        } else {
            url = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
        }
        File file;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }

        List<Class<?>> classes = new ArrayList<>();

        try {
            try (JarFile jarFile = new JarFile(file)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                if (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        String className = entry.getName()
                                .replace("/", ".")
                                .replace(".class", "");
                        if (plugin != null) {
                            classes.add(Class.forName(className, false, plugin.getClass().getClassLoader()));
                        } else {
                            classes.add(Class.forName(className, false, getClass().getClassLoader()));
                        }
                    }
                }
            } catch (ClassNotFoundException e) {}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Graph<Class<?>, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        Map<Class<?>, ClassInfo<?>> temporaryInfo = new HashMap<>();
        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(Auto.class)) {
                continue;
            }
            Class<?> current = clazz;
            do {
                for (Constructor<?> constructor : current.getConstructors()) {
                    if (!constructor.isAnnotationPresent(Auto.class)) {
                        continue;
                    }
                    for (Parameter parameter : constructor.getParameters()) {
                        graph.addEdge(current, parameter.getType());
                    }
                }
                for (Field field : current.getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Auto.class)) {
                        continue;
                    }
                    graph.addEdge(current, field.getType());
                }
                current = current.getSuperclass();
            } while (current != null);
        }
        TopologicalOrderIterator<Class<?>, DefaultEdge> topo = new TopologicalOrderIterator<>(graph);
        //topo.forEachRemaining(injected::add);
        //for (Class<?> cls : injected) {
//
        //}
    }

    @Override
    public void inject(@NotNull Object obj) {
        Class<?> current = obj.getClass();
        do {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Auto.class)) {

                }
            }
            current = current.getSuperclass();
        } while (current != null);
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Auto.class)) {

            }
        }
        for (Constructor<?> constructor : obj.getClass().getConstructors()) {
            if (!constructor.isAnnotationPresent(Auto.class)) {
                continue;
            }
            for (Parameter parameter : constructor.getParameters()) {
                providers.get(TypeToken.of(parameter.getType()));
            }

        }
    }

    @Override
    public void inject(@NotNull Object obj, @NotNull Predicate<Field> predicate) {

    }

    private static class ClassInfo<T> {
        private final TypeToken<T> clazz;
        private final Constructor<T> constructor;
        private final List<Field> fields;

        public ClassInfo(TypeToken<T> clazz, Constructor<T> constructor, List<Field> fields) {
            this.clazz = clazz;
            this.constructor = constructor;
            this.fields = fields;
        }

        public TypeToken<T> getClazz() {
            return clazz;
        }

        public Constructor<T> getConstructor() {
            return constructor;
        }

        public List<Field> getFields() {
            return fields;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClassInfo<?> classInfo = (ClassInfo<?>) o;
            return clazz.equals(classInfo.clazz) && constructor.equals(classInfo.constructor) && fields.equals(classInfo.fields);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz, constructor, fields);
        }
    }
}
