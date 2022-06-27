package project.cyberproton.atom.resource;

import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.plugin.PluginModule;
import project.cyberproton.atom.state.Key;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class ResourceManager<T extends AtomPlugin> implements PluginModule<T> {
    private final Map<Key, Resource> resources = new HashMap<>();
    private final Map<Key, ResourceLoader<?>> loaders = new HashMap<>();
    private final Map<Class<?>, ResourceLoader<?>> loadersByClasses = new HashMap<>();
    private final Path path;

    public ResourceManager(Path path) {
        this.path = path;
    }

    @Override
    public void onEnable() {
        onReload();
    }

    @Override
    public void onReload() {

    }

    public void registerResourceIdentifier() {

    }

    public <R extends Resource> R getResource(Key key, Class<R> resourceCls) {
        return null;
    }

    public Message getMessage(Key key) {
        return null;
    }

    public TitleResource getTitle(Key key) {
        return null;
    }



    public <R extends Resource> void registerLoader(R resource, ResourceLoader<R> loader) {
        loaders.put(resource.key(), loader);
    }

    public <R extends Resource> void registerLoader(Class<R> resourceClass, ResourceLoader<R> loader) {
        loadersByClasses.put(resourceClass, loader);
    }
}
