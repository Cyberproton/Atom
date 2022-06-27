package project.cyberproton.atom.module;

import com.google.common.reflect.TypeToken;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.LifeCycle;
import project.cyberproton.atom.Log;
import project.cyberproton.atom.Platform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public class SimpleModuleManager<T extends Module> implements ModuleManager<T> {
    private final Map<UUID, ModuleContainer<T>> modules = new LinkedHashMap<>();
    private List<ModuleContainer<T>> sorted = null;
    private final List<UUID> defaultModuleIds;
    private LifeCycle lifeCycle = LifeCycle.DISABLED;

    protected SimpleModuleManager(@NotNull List<UUID> defaultModuleIds) {
        Objects.requireNonNull(defaultModuleIds, "defaultModuleIds");
        this.defaultModuleIds = defaultModuleIds;
    }

    protected SimpleModuleManager() {
        this(new ArrayList<>());
    }

    @NotNull
    @Override
    public List<UUID> getDefaultModuleIds() {
        return defaultModuleIds;
    }

    @NotNull
    @Override
    public List<T> getModules() {
        List<T> res = new ArrayList<>();
        for (ModuleContainer<T> moduleContainer : modules.values()) {
            res.add(moduleContainer.getModule());
        }
        return res;
    }

    @NotNull
    @Override
    public List<T> getActiveModules() {
        List<T> res = new ArrayList<>();
        for (ModuleContainer<T> moduleContainer : modules.values()) {
            if (moduleContainer.getLifecycle() == Module.Lifecycle.ACTIVE) {
                continue;
            }
            res.add(moduleContainer.getModule());
        }
        return res;
    }

    @Nullable
    @Override
    public T getModule(@NotNull UUID id) {
        Objects.requireNonNull(id, "id");
        ModuleContainer<T> moduleContainer = modules.get(id);
        if (moduleContainer == null) {
            return null;
        }
        if (moduleContainer.getLifecycle() == Module.Lifecycle.DISABLED &&
            moduleContainer.getModule().getOptions().contains(Module.Option.LAZY_ENABLING))
        {
            try {
                moduleContainer.getModule().onEnable();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            moduleContainer.setLifecycle(Module.Lifecycle.ACTIVE);
        }
        return moduleContainer.getModule();
    }

    @Nullable
    @Override
    public <M extends T> M getModule(@NotNull TypeToken<M> type) {
        Objects.requireNonNull(type, "type");
        for (ModuleContainer<T> moduleContainer : modules.values()) {
            T module = moduleContainer.getModule();
            if (type.getRawType().isAssignableFrom(module.getClass())) {
                return (M) type.getRawType().cast(module);
            }
        }
        return null;
    }

    @Override
    public void enableModules() {
        if (lifeCycle != LifeCycle.DISABLED) {
            return;
        }
        lifeCycle = LifeCycle.ENABLING;
        for (ModuleContainer<T> moduleContainer : getSortedModuleDatumByDependencies(false)) {
            if (moduleContainer.getModule().getOptions().contains(Module.Option.LAZY_ENABLING)) {
                continue;
            }
            try {
                moduleContainer.getModule().onEnable();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            moduleContainer.setLifecycle(Module.Lifecycle.ACTIVE);
        }
        lifeCycle = LifeCycle.ACTIVE;
    }

    @Override
    public void reloadModules() {
        Log.d(getClass().getSimpleName(), "Reloading modules");
        if (lifeCycle != LifeCycle.ACTIVE) {
            return;
        }
        Log.d(getClass().getSimpleName(), "Life pass");
        lifeCycle = LifeCycle.RELOADING;
        for (ModuleContainer<T> moduleDatumByDependency : getSortedModuleDatumByDependencies(false)) {
            Log.d(getClass().getSimpleName(), moduleDatumByDependency.getModule().getUUID());
        }
        for (ModuleContainer<T> moduleContainer : getSortedModuleDatumByDependencies(false)) {
            try {
                moduleContainer.getModule().onReload();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            moduleContainer.setLifecycle(Module.Lifecycle.ACTIVE);
        }
        lifeCycle = LifeCycle.ACTIVE;
    }

    @Override
    public void disableModules() {
        if (lifeCycle != LifeCycle.ACTIVE) {
            return;
        }
        lifeCycle = LifeCycle.DISABLING;
        for (ModuleContainer<T> moduleContainer : getSortedModuleDatumByDependencies(true)) {
            try {
                moduleContainer.getModule().onDisable();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            moduleContainer.setLifecycle(Module.Lifecycle.DISABLED);
        }
        lifeCycle = LifeCycle.DISABLED;
    }

    public void tickModules() {
        long current = Platform.getCurrentTick();
        for (ModuleContainer<T> moduleContainer : modules.values()) {
            T module = moduleContainer.getModule();
            if (moduleContainer.getLifecycle() != Module.Lifecycle.ACTIVE || module.getTickRate() <= 0) {
                continue;
            }
            if (current - moduleContainer.getLastTicked() < module.getTickRate()) {
                continue;
            }
            moduleContainer.setLastTicked(current);
            try {
                module.onTick();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean registerModule(@NotNull T module) {
        Objects.requireNonNull(module, "module");
        ModuleContainer<T> moduleContainer = modules.get(module.getUUID());
        if (moduleContainer != null) {
            throw new IllegalArgumentException("Module with id " + module.getUUID() + " has already been registered");
        }
        try {
            modules.put(module.getUUID(), new ModuleContainer<>(module));
            if (lifeCycle == LifeCycle.ACTIVE) {
                module.onEnable();
            }
            sorted = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean unregisterModule(@NotNull UUID uuid) {
        Objects.requireNonNull(uuid, "uuid");
        if (defaultModuleIds.contains(uuid)) {
            throw new IllegalArgumentException("Cannot unregister default module");
        }
        ModuleContainer<T> m = modules.remove(uuid);
        try {
            m.getModule().onDisable();
            sorted = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public List<ModuleContainer<T>> getSortedModuleDatumByDependencies(boolean reverse) {
        if (sorted != null) {
            if (reverse) {
                List<ModuleContainer<T>> res = new ArrayList<>();
                for (int i = sorted.size() - 1; i >= 0; i--) {
                    res.add(sorted.get(i));
                }
                return res;
            }
            return new ArrayList<>(sorted);
        }

        Graph<UUID, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (ModuleContainer<T> moduleContainer : modules.values()) {
            graph.addVertex(moduleContainer.getModule().getUUID());
        }

        for (ModuleContainer<T> moduleContainer : modules.values()) {
            for (Class<?> dependency : moduleContainer.getModule().getDependencies()) {
                for (ModuleContainer<T> md : modules.values()) {
                    if (md.getModule().getUUID().equals(moduleContainer.getModule().getUUID())) {
                        continue;
                    }
                    if (dependency.isAssignableFrom(md.getModule().getClass())) {
                        System.out.println(dependency.getSimpleName() + " of " + moduleContainer.getModule().getClass().getSimpleName() + " -> " + md.getModule().getClass().getSimpleName());
                        graph.addEdge(md.getModule().getUUID(), moduleContainer.getModule().getUUID());
                        break;
                    }
                }
            }
        }

        TopologicalOrderIterator<UUID, DefaultEdge> topo = new TopologicalOrderIterator<>(graph);

        sorted = new ArrayList<>();
        topo.forEachRemaining(uuid -> sorted.add(modules.get(uuid)));

        if (reverse) {
            List<ModuleContainer<T>> reversed = new ArrayList<>();
            topo.forEachRemaining(uuid -> reversed.add(0, modules.get(uuid)));
            return reversed;
        }

        return new ArrayList<>(sorted);
    }

    @NotNull
    public static <T extends Module> SimpleModuleManager<T> create(@NotNull List<UUID> defaultModuleIds) {
        return new SimpleModuleManager<>(defaultModuleIds);
    }

    @NotNull
    public static <T extends Module> SimpleModuleManager<T> create() {
        return new SimpleModuleManager<>();
    }
}
