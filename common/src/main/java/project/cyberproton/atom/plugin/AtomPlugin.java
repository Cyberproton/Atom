package project.cyberproton.atom.plugin;

import org.spongepowered.configurate.CommentedConfigurationNode;
import project.cyberproton.atom.module.ModuleManager;
import project.cyberproton.atom.logging.Logger;
import project.cyberproton.atom.scheduler.SchedulerManager;
import project.cyberproton.atom.terminable.TerminableConsumer;

import org.jetbrains.annotations.NotNull;

public interface AtomPlugin extends TerminableConsumer {

    @NotNull
    String getId();

    @NotNull
    PluginMetadata getMetadata();

    @NotNull
    Logger getLog();

    @NotNull
    SchedulerManager getScheduler();

    @NotNull
    ModuleManager<PluginModule<?>> getModuleManager();

    @NotNull
    CommentedConfigurationNode getAtomConfig();

    void dispatchAction(@NotNull PluginAction action);

    default void onPluginLoad() {}

    default void onPluginEnable() {}

    default void onPluginReload() {}

    default void onPluginDisable() {}

}
