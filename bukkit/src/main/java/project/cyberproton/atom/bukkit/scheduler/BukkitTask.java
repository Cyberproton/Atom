package project.cyberproton.atom.bukkit.scheduler;

import org.bukkit.scheduler.BukkitRunnable;
import project.cyberproton.atom.exception.Exceptions;
import project.cyberproton.atom.scheduler.Task;
import project.cyberproton.atom.util.Delegate;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class BukkitTask extends BukkitRunnable implements Task, Delegate<Consumer<Task>> {
    private final Consumer<Task> backingTask;

    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    public BukkitTask(Consumer<Task> backingTask) {
        this.backingTask = backingTask;
    }

    @Override
    public void run() {
        if (this.cancelled.get()) {
            cancel();
            return;
        }

        try {
            this.backingTask.accept(this);
            this.counter.incrementAndGet();
        } catch (Throwable e) {
            Exceptions.reportScheduler(e);
        }

        if (this.cancelled.get()) {
            cancel();
        }
    }

    @Override
    public int getTimesRan() {
        return this.counter.get();
    }

    @Override
    public boolean stop() {
        return !this.cancelled.getAndSet(true);
    }

    @Override
    public int getId() {
        return getTaskId();
    }

    @Override
    public boolean isClosed() {
        return this.cancelled.get();
    }

    @Override
    public Consumer<Task> getDelegate() {
        return this.backingTask;
    }
}
