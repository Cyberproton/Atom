package project.cyberproton.atom.entity;

import project.cyberproton.atom.Atom;
import project.cyberproton.atom.Platform;
import project.cyberproton.atom.module.SimpleModuleManager;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.scheduler.Task;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SimpleEntityModuleManager extends SimpleModuleManager<IEntityModule<?>> implements EntityModuleManager {
    private final AtomPlugin plugin;
    private Task task;

    private SimpleEntityModuleManager(@NotNull AtomPlugin plugin, @NotNull List<UUID> defaultModuleIds) {
        super(defaultModuleIds);
        Objects.requireNonNull(plugin, "plugin");
        this.plugin = plugin;
    }

    private SimpleEntityModuleManager(@NotNull AtomPlugin plugin) {
        this(plugin, new ArrayList<>());
    }

    private SimpleEntityModuleManager() {
        this(Platform.getBootstrap());
    }

    @Override
    public void enableModules() {
        if (task != null) {
            task.stop();
        }
        super.enableModules();
        task = plugin.getScheduler().sync().runRepeating(this::tickModules, 0L, 1L);
        tickModules();
    }

    @Override
    public void disableModules() {
        super.disableModules();
        task.closeAndReportException();
        task = null;
    }

    @NotNull
    @Override
    public List<IEntityModule<?>> getModulesByPlugin(@NotNull AtomPlugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        List<IEntityModule<?>> modules = getModules();
        modules.removeIf(m -> m.getPlugin() != plugin);
        return modules;
    }

    @NotNull
    public static SimpleEntityModuleManager create(@NotNull AtomPlugin plugin, @NotNull List<UUID> defaultModuleIds) {
        return new SimpleEntityModuleManager(plugin, defaultModuleIds);
    }

    @NotNull
    public static SimpleEntityModuleManager create(@NotNull AtomPlugin plugin) {
        return new SimpleEntityModuleManager(plugin);
    }

    @NotNull
    public static SimpleEntityModuleManager create() {
        return new SimpleEntityModuleManager();
    }
}
