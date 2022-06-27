package project.cyberproton.atom.bukkit.scheduler;

import org.bukkit.Bukkit;
import project.cyberproton.atom.bukkit.plugin.BukkitPlugin;
import project.cyberproton.atom.promise.ThreadContext;
import project.cyberproton.atom.scheduler.Scheduler;
import project.cyberproton.atom.scheduler.Task;
import project.cyberproton.atom.util.Delegates;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.function.Consumer;

public class BukkitAsyncScheduler implements Scheduler {
    private final BukkitPlugin plugin;

    public BukkitAsyncScheduler(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public ThreadContext getContext() {
        return ThreadContext.ASYNC;
    }

    @Override
    public void execute(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, runnable);
    }

    @NotNull
    @Override
    public Task run(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");
        BukkitTask task = new BukkitTask(Delegates.runnableToConsumer(runnable));
        task.runTaskAsynchronously(plugin);
        return task;
    }

    @NotNull
    @Override
    public Task runLater(@NotNull Runnable runnable, long delayTicks) {
        Objects.requireNonNull(runnable, "runnable");
        BukkitTask task = new BukkitTask(Delegates.runnableToConsumer(runnable));
        task.runTaskLaterAsynchronously(plugin, delayTicks);
        return task;
    }

    @NotNull
    @Override
    public Task runRepeating(@NotNull Consumer<Task> consumer, long delayTicks, long intervalTicks) {
        Objects.requireNonNull(consumer, "consumer");
        BukkitTask task = new BukkitTask(consumer);
        task.runTaskTimerAsynchronously(plugin, delayTicks, intervalTicks);
        return task;
    }
}
