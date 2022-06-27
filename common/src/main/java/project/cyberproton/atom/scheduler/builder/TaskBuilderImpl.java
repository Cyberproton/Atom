package project.cyberproton.atom.scheduler.builder;

import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.promise.Promise;
import project.cyberproton.atom.promise.ThreadContext;
import project.cyberproton.atom.scheduler.SchedulerManager;
import project.cyberproton.atom.scheduler.Task;
import project.cyberproton.atom.util.Delegates;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

class TaskBuilderImpl implements TaskBuilder {
    public static TaskBuilder of(AtomPlugin plugin) {
        return new TaskBuilderImpl(plugin.getScheduler());
    }

    public static TaskBuilder of(SchedulerManager schedulerManager) {
        return new TaskBuilderImpl(schedulerManager);
    }

    private final SchedulerManager schedulerManager;
    private final ThreadContextual sync;
    private final ThreadContextual async;

    private TaskBuilderImpl(@NotNull SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
        this.sync = new ThreadContextualBuilder(schedulerManager, ThreadContext.SYNC);
        this.async = new ThreadContextualBuilder(schedulerManager, ThreadContext.ASYNC);
    }

    @NotNull
    @Override
    public ThreadContextual sync() {
        return this.sync;
    }

    @NotNull
    @Override
    public ThreadContextual async() {
        return this.async;
    }

    private static final class ThreadContextualBuilder implements ThreadContextual {
        private final ThreadContext context;
        private final ContextualPromiseBuilder instant;
        private final SchedulerManager schedulerManager;

        ThreadContextualBuilder(SchedulerManager schedulerManager, ThreadContext context) {
            this.schedulerManager = schedulerManager;
            this.context = context;
            this.instant = new ContextualPromiseBuilderImpl(schedulerManager, context);
        }

        @NotNull
        @Override
        public ContextualPromiseBuilder now() {
            return this.instant;
        }

        @NotNull
        @Override
        public DelayedTick after(long ticks) {
            return new DelayedTickBuilder(schedulerManager, this.context, ticks);
        }

        @NotNull
        @Override
        public DelayedTime after(long duration, @NotNull TimeUnit unit) {
            return new DelayedTimeBuilder(schedulerManager, this.context, duration, unit);
        }

        @NotNull
        @Override
        public ContextualTaskBuilder afterAndEvery(long ticks) {
            return new ContextualTaskBuilderTickImpl(schedulerManager, this.context, ticks, ticks);
        }

        @NotNull
        @Override
        public ContextualTaskBuilder afterAndEvery(long duration, @NotNull TimeUnit unit) {
            return new ContextualTaskBuilderTimeImpl(schedulerManager, this.context, duration, unit, duration, unit);
        }

        @NotNull
        @Override
        public ContextualTaskBuilder every(long ticks) {
            return new ContextualTaskBuilderTickImpl(schedulerManager, this.context, 0, ticks);
        }

        @NotNull
        @Override
        public ContextualTaskBuilder every(long duration, @NotNull TimeUnit unit) {
            return new ContextualTaskBuilderTimeImpl(schedulerManager, this.context, 0, TimeUnit.NANOSECONDS, duration, unit);
        }
    }

    private static final class DelayedTickBuilder implements DelayedTick {
        private final ThreadContext context;
        private final long delay;
        private final SchedulerManager schedulerManager;

        DelayedTickBuilder(SchedulerManager schedulerManager, ThreadContext context, long delay) {
            this.schedulerManager = schedulerManager;
            this.context = context;
            this.delay = delay;
        }

        @NotNull
        @Override
        public <T> Promise<T> supply(@NotNull Supplier<T> supplier) {
            return schedulerManager
                    .promise()
                    .supplyingDelayed(context, supplier, delay);
        }

        @NotNull
        @Override
        public <T> Promise<T> call(@NotNull Callable<T> callable) {
            return schedulerManager
                    .promise()
                    .supplyingDelayed(context, Delegates.callableToSupplier(callable), delay);
        }

        @NotNull
        @Override
        public Promise<Void> run(@NotNull Runnable runnable) {
            return schedulerManager
                    .promise()
                    .supplyingDelayed(context, Delegates.runnableToSupplier(runnable), delay);
        }

        @NotNull
        @Override
        public ContextualTaskBuilder every(long ticks) {
            return new ContextualTaskBuilderTickImpl(schedulerManager, this.context, this.delay, ticks);
        }
    }

    private static final class DelayedTimeBuilder implements DelayedTime {
        private final ThreadContext context;
        private final long delay;
        private final TimeUnit delayUnit;
        private final SchedulerManager schedulerManager;

        DelayedTimeBuilder(SchedulerManager schedulerManager, ThreadContext context, long delay, TimeUnit delayUnit) {
            this.schedulerManager = schedulerManager;
            this.context = context;
            this.delay = delay;
            this.delayUnit = delayUnit;
        }

        @NotNull
        @Override
        public <T> Promise<T> supply(@NotNull Supplier<T> supplier) {
            return schedulerManager.promise().supplyingDelayed(context, supplier, this.delay, this.delayUnit);
        }

        @NotNull
        @Override
        public <T> Promise<T> call(@NotNull Callable<T> callable) {
            return schedulerManager.promise().supplyingDelayed(context, Delegates.callableToSupplier(callable), this.delay, this.delayUnit);
        }

        @NotNull
        @Override
        public Promise<Void> run(@NotNull Runnable runnable) {
            return schedulerManager.promise().supplyingDelayed(context, Delegates.runnableToSupplier(runnable), this.delay, this.delayUnit);
        }

        @NotNull
        @Override
        public ContextualTaskBuilder every(long duration, TimeUnit unit) {
            return new ContextualTaskBuilderTimeImpl(schedulerManager, this.context, this.delay, this.delayUnit, duration, unit);
        }
    }

    private static class ContextualPromiseBuilderImpl implements ContextualPromiseBuilder {
        private final ThreadContext context;
        private final SchedulerManager schedulerManager;

        ContextualPromiseBuilderImpl(SchedulerManager schedulerManager, ThreadContext context) {
            this.schedulerManager = schedulerManager;
            this.context = context;
        }

        @NotNull
        @Override
        public <T> Promise<T> supply(@NotNull Supplier<T> supplier) {
            return schedulerManager.promise().supplying(context, supplier);
        }

        @NotNull
        @Override
        public <T> Promise<T> call(@NotNull Callable<T> callable) {
            return schedulerManager.promise().supplying(context, Delegates.callableToSupplier(callable));
        }

        @NotNull
        @Override
        public Promise<Void> run(@NotNull Runnable runnable) {
            return schedulerManager.promise().supplying(context, Delegates.runnableToSupplier(runnable));
        }
    }

    private static class ContextualTaskBuilderTickImpl implements ContextualTaskBuilder {
        private final ThreadContext context;
        private final long delay;
        private final long interval;
        private final SchedulerManager schedulerManager;

        ContextualTaskBuilderTickImpl(SchedulerManager schedulerManager, ThreadContext context, long delay, long interval) {
            this.schedulerManager = schedulerManager;
            this.context = context;
            this.delay = delay;
            this.interval = interval;
        }

        @NotNull
        @Override
        public Task consume(@NotNull Consumer<Task> consumer) {
            return schedulerManager.get(this.context).runRepeating(consumer, this.delay, this.interval);
        }

        @NotNull
        @Override
        public Task run(@NotNull Runnable runnable) {
            return schedulerManager.get(this.context).runRepeating(runnable, this.delay, this.interval);
        }
    }

    private static class ContextualTaskBuilderTimeImpl implements ContextualTaskBuilder {
        private final ThreadContext context;
        private final long delay;
        private final TimeUnit delayUnit;
        private final long interval;
        private final TimeUnit intervalUnit;
        private final SchedulerManager schedulerManager;

        ContextualTaskBuilderTimeImpl(SchedulerManager schedulerManager, ThreadContext context, long delay, TimeUnit delayUnit, long interval, TimeUnit intervalUnit) {
            this.schedulerManager = schedulerManager;
            this.context = context;
            this.delay = delay;
            this.delayUnit = delayUnit;
            this.interval = interval;
            this.intervalUnit = intervalUnit;
        }

        @NotNull
        @Override
        public Task consume(@NotNull Consumer<Task> consumer) {
            return schedulerManager.get(this.context).runRepeating(consumer, this.delay, this.delayUnit, this.interval, this.intervalUnit);
        }

        @NotNull
        @Override
        public Task run(@NotNull Runnable runnable) {
            return schedulerManager.get(this.context).runRepeating(runnable, this.delay, this.delayUnit, this.interval, this.intervalUnit);
        }
    }
}