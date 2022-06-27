package project.cyberproton.atom.plugin;

import com.google.common.reflect.TypeToken;
import org.spongepowered.configurate.CommentedConfigurationNode;
import project.cyberproton.atom.module.ModuleManager;
import project.cyberproton.atom.module.SimpleModuleManager;
import project.cyberproton.atom.terminable.TerminableConsumer;
import project.cyberproton.atom.terminable.TerminableContainer;
import project.cyberproton.atom.terminable.composite.CompositeTerminable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public class BaseAtomPlugin implements ModuleManager<PluginModule<?>>, TerminableConsumer {
    private final CompositeTerminable compositeTerminable = CompositeTerminable.create();
    private final SimpleModuleManager<PluginModule<?>> moduleManager = SimpleModuleManager.create();

    @NotNull
    @Override
    public List<UUID> getDefaultModuleIds() {
        return moduleManager.getDefaultModuleIds();
    }

    @NotNull
    @Override
    public List<PluginModule<?>> getModules() {
        return moduleManager.getModules();
    }

    @NotNull
    @Override
    public List<PluginModule<?>> getActiveModules() {
        return moduleManager.getActiveModules();
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <M extends PluginModule<?>> M getModule(@NotNull Class<M> cls) {
        return moduleManager.getModule(cls);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <M extends PluginModule<?>> M getModule(@NotNull TypeToken<M> type) {
        return moduleManager.getModule(type);
    }

    @Nullable
    @Override
    public PluginModule<?> getModule(@NotNull UUID id) {
        return moduleManager.getModule(id);
    }

    @Override
    public boolean registerModule(@NotNull PluginModule<?> module) {
        return moduleManager.registerModule(module);
    }

    @Override
    public boolean unregisterModule(@NotNull PluginModule<?> module) {
        return moduleManager.unregisterModule(module);
    }

    @Override
    public boolean unregisterModule(@NotNull UUID uuid) {
        return moduleManager.unregisterModule(uuid);
    }

    public SimpleModuleManager<PluginModule<?>> getModuleManager() {
        return moduleManager;
    }

    @NotNull
    @Override
    public <T extends AutoCloseable> T bind(@NotNull T terminable) {
        return compositeTerminable.bind(terminable);
    }

    @NotNull
    @Override
    public <T extends TerminableContainer> T bindContainer(@NotNull T module) {
        return compositeTerminable.bindContainer(module);
    }

    public void enable() {
        moduleManager.enableModules();
    }

    public void reload() {
        moduleManager.reloadModules();
    }

    public void disable() {
        compositeTerminable.closeAndReportException();
        moduleManager.disableModules();
    }

    public void tick() {
        moduleManager.tickModules();
    }
}
