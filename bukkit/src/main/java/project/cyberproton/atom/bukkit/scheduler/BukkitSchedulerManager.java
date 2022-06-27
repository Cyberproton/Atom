package project.cyberproton.atom.bukkit.scheduler;

import project.cyberproton.atom.bukkit.plugin.BukkitPlugin;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.promise.PromiseBuilder;
import project.cyberproton.atom.scheduler.Scheduler;
import project.cyberproton.atom.scheduler.SchedulerManager;

public class BukkitSchedulerManager implements SchedulerManager {
    private final AtomPlugin plugin;
    private final Scheduler sync;
    private final Scheduler async;

    public BukkitSchedulerManager(BukkitPlugin plugin) {
        this.plugin = plugin;
        this.sync = new BukkitSyncScheduler(plugin);
        this.async = new BukkitAsyncScheduler(plugin);
    }

    @Override
    public Scheduler sync() {
        return sync;
    }

    @Override
    public Scheduler async() {
        return async;
    }

    @Override
    public PromiseBuilder promise() {
        return PromiseBuilder.of(plugin);
    }
}
