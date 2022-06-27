package project.cyberproton.atom.plugin;

import org.spongepowered.configurate.CommentedConfigurationNode;
import project.cyberproton.atom.logging.Logger;
import project.cyberproton.atom.module.Module;
import project.cyberproton.atom.module.ModuleManager;
import project.cyberproton.atom.scheduler.SchedulerManager;

import org.jetbrains.annotations.NotNull;

public interface PluginModule<T extends AtomPlugin> extends Module {

    @NotNull
    T getPlugin();

    @NotNull
    default PluginMetadata getMetadata() {
        return getPlugin().getMetadata();
    }

    @NotNull
    default SchedulerManager getScheduler() {
        return getPlugin().getScheduler();
    }

    @NotNull
    default ModuleManager<PluginModule<?>> getModuleManager() {
        return getPlugin().getModuleManager();
    }

    @NotNull
    default Logger getLog() {
        return getPlugin().getLog();
    }

    @NotNull
    default CommentedConfigurationNode getAtomConfig() {
        return getPlugin().getAtomConfig();
    }

    default boolean register() {
        return getPlugin().getModuleManager().registerModule(this);
    }

    default boolean unregister() {
        return getPlugin().getModuleManager().unregisterModule(this);
    }

}
