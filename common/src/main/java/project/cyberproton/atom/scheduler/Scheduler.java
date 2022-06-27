package project.cyberproton.atom.scheduler;

import project.cyberproton.atom.promise.ThreadContext;
import project.cyberproton.atom.util.Delegates;
import project.cyberproton.atom.util.Ticks;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Utility for scheduling tasks
 */
public interface Scheduler extends Executor {

    /**
     * Gets the context this scheduler operates in.
     *
     * @return the context
     */
    @NotNull
    ThreadContext getContext();

    @Override
    void execute(@NotNull Runnable runnable);

    /**
     * Execute the passed runnable
     *
     * @param runnable the runnable
     * @return a Promise which will return when the runnable is complete
     */
    @NotNull
    Task run(@NotNull Runnable runnable);

    /**
     * Execute the passed runnable at some point in the future
     *
     * @param runnable the runnable
     * @param delayTicks    the delay before the task begins
     * @return a Promise which will return when the runnable is complete
     */
    @NotNull
    Task runLater(@NotNull Runnable runnable, long delayTicks);

    /**
     * Execute the passed runnable at some point in the future
     *
     * @param runnable the runnable
     * @param delay    the delay to wait before calling the supplier
     * @param unit     the unit of delay
     * @return a Promise which will return when the runnable is complete
     */
    @NotNull
    default Task runLater(@NotNull Runnable runnable, long delay, @NotNull TimeUnit unit) {
        Objects.requireNonNull(runnable, "runnable");
        return runLater(runnable, Ticks.from(delay, unit));
    }

    /**
     * Schedule a repeating task to run
     *
     * @param consumer the task to run
     * @param delayTicks the delay before the task begins
     * @param intervalTicks the interval at which the task will repeat
     * @return a task instance
     */
    @NotNull
    Task runRepeating(@NotNull Consumer<Task> consumer, long delayTicks, long intervalTicks);

    /**
     * Schedule a repeating task to run
     *
     * @param consumer the task to run
     * @param delay the delay before the task begins
     * @param delayUnit the unit of delay
     * @param interval the interval at which the task will repeat
     * @param intervalUnit the
     * @return a task instance
     */
    @NotNull
    default Task runRepeating(@NotNull Consumer<Task> consumer, long delay, @NotNull TimeUnit delayUnit, long interval, @NotNull TimeUnit intervalUnit) {
        return runRepeating(consumer, Ticks.from(delay, delayUnit), Ticks.from(interval, intervalUnit));
    }

    /**
     * Schedule a repeating task to run
     *
     * @param runnable the task to run
     * @param delayTicks the delay before the task begins
     * @param intervalTicks the interval at which the task will repeat
     * @return a task instance
     */
    @NotNull
    default Task runRepeating(@NotNull Runnable runnable, long delayTicks, long intervalTicks) {
        return runRepeating(Delegates.runnableToConsumer(runnable), delayTicks, intervalTicks);
    }

    /**
     * Schedule a repeating task to run
     *
     * @param runnable the task to run
     * @param delay the delay before the task begins
     * @param delayUnit the unit of delay
     * @param interval the interval at which the task will repeat
     * @param intervalUnit the
     * @return a task instance
     */
    @NotNull
    default Task runRepeating(@NotNull Runnable runnable, long delay, @NotNull TimeUnit delayUnit, long interval, @NotNull TimeUnit intervalUnit) {
        return runRepeating(Delegates.runnableToConsumer(runnable), delay, delayUnit, interval, intervalUnit);
    }
}
