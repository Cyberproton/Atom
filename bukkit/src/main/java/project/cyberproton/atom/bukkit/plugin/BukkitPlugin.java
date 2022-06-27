package project.cyberproton.atom.bukkit.plugin;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import project.cyberproton.atom.bukkit.logging.BukkitLogger;
import project.cyberproton.atom.bukkit.scheduler.BukkitSchedulerManager;
import project.cyberproton.atom.logging.Logger;
import project.cyberproton.atom.module.ModuleManager;
import project.cyberproton.atom.plugin.*;
import project.cyberproton.atom.scheduler.SchedulerManager;
import project.cyberproton.atom.scheduler.Task;
import project.cyberproton.atom.terminable.TerminableContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public abstract class BukkitPlugin extends JavaPlugin implements AtomPlugin {
    private final BaseAtomPlugin delegate = new BaseAtomPlugin();
    private final PluginMetadata metadata = new PluginMetadata(getFile().toPath(), getDataFolder().toPath(), getDataFolder().toPath().resolve("data"));
    private final SchedulerManager schedulerManager = new BukkitSchedulerManager(this);
    private final BukkitLogger logger = new BukkitLogger(this);
    private CommentedConfigurationNode atomConfig;
    private Task task;

    @Override
    public final void onLoad() {
        onPluginLoad();
    }

    @Override
    public final void onEnable() {
        delegate.enable();
        onPluginEnable();
        task = schedulerManager.sync().runRepeating(delegate::tick, 0L, 1L);
    }

    public final void reload() {
        delegate.reload();
        onPluginReload();
        atomConfig = null;
    }

    @Override
    public void dispatchAction(@NotNull PluginAction action) {
        switch (action) {
            case RELOAD:
                reload();
            case ENABLE:
                Bukkit.getPluginManager().enablePlugin(this);
            case DISABLE:
                Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public final void onDisable() {
        onPluginDisable();
        delegate.disable();
        task.stop();
    }

    @NotNull
    @Override
    public String getId() {
        return getName();
    }

    @NotNull
    @Override
    public PluginMetadata getMetadata() {
        return metadata;
    }

    @NotNull
    @Override
    public Logger getLog() {
        return logger;
    }

    @NotNull
    @Override
    public CommentedConfigurationNode getAtomConfig() {
        if (atomConfig != null) return atomConfig;
        Path dataPath = getMetadata().getDataPath();
        getLog().d("data path: " + dataPath.toAbsolutePath());
        File atomDir = getMetadata().getDataPath().resolve("atom").toFile();
        if (!atomDir.exists()) {
            atomDir.mkdirs();
        }
        File configFile = atomDir.toPath().resolve("config.yml").toFile();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            atomConfig = YamlConfigurationLoader
                    .builder()
                    .file(configFile)
                    .build()
                    .load();
            return atomConfig;
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public SchedulerManager getScheduler() {
        return schedulerManager;
    }

    @NotNull
    @Override
    public ModuleManager<PluginModule<?>> getModuleManager() {
        return delegate.getModuleManager();
    }

    @NotNull
    @Override
    public <T extends AutoCloseable> T bind(@NotNull T terminable) {
        return delegate.bind(terminable);
    }

    @NotNull
    @Override
    public <T extends TerminableContainer> T bindContainer(@NotNull T module) {
        return delegate.bindContainer(module);
    }
}
