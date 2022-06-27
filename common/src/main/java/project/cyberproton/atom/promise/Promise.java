package project.cyberproton.atom.promise;

import project.cyberproton.atom.Platform;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.terminable.Terminable;
import project.cyberproton.atom.util.Delegates;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An object that acts as a proxy for a result that is initially unknown,
 * usually because the computation of its value is yet incomplete.
 *
 * <p>This interface carries similar method signatures to those of
 * {@link CompletionStage} and {@link CompletableFuture}.</p>
 *
 * <p>However, a distinction is made between actions which are executed on
 * the main server thread vs asynchronously.</p>
 *
 * @param <V> the result type
 */
public interface Promise<V> extends Future<V>, Terminable {
    /**
     * Create a promise with plugin's scheduler.
     *
     * @param plugin the plugin of which the scheduler will be used
     * @return the promise
     */
    static PromiseBuilder of(AtomPlugin plugin) {
        return PromiseBuilder.of(plugin);
    }

    /**
     * Create a promise with bootstrap plugin's scheduler.
     *
     * @return the promise
     */
    static PromiseBuilder of() {
        return of(Platform.getBootstrap());
    }

    /**
     * Attempts to cancel execution of this task.
     *
     * @return {@code false} if the task could not be cancelled, typically
     * because it has already completed normally;
     * {@code true} otherwise
     */
    default boolean cancel() {
        return cancel(true);
    }

    /**
     * Returns the result value when complete, or throws an
     * (unchecked) exception if completed exceptionally.
     *
     * <p>To better conform with the use of common functional forms, if a
     * computation involved in the completion of this
     * Promise threw an exception, this method throws an
     * (unchecked) {@link CompletionException} with the underlying
     * exception as its cause.</p>
     *
     * @return the result value
     * @throws CancellationException if the computation was cancelled
     * @throws CompletionException if this future completed
     * exceptionally or a completion computation threw an exception
     */
    V join();

    /**
     * Returns the result value (or throws any encountered exception)
     * if completed, else returns the given valueIfAbsent.
     *
     * @param valueIfAbsent the value to return if not completed
     * @return the result value, if completed, else the given valueIfAbsent
     * @throws CancellationException if the computation was cancelled
     * @throws CompletionException if this future completed
     * exceptionally or a completion computation threw an exception
     */
    V getNow(V valueIfAbsent);

    /**
     * Supplies the Promise's result.
     *
     * @param value the object to pass to the promise
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supply(@Nullable V value);

    /**
     * Supplies an exceptional result to the Promise.
     *
     * @param exception the exception to supply
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyException(@NotNull Throwable exception);

    /**
     * Schedules the supply of the Promise's result, via the given supplier.
     *
     * @param context the type of executor to use to supply the promise
     * @param supplier the supplier
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    default Promise<V> supply(@NotNull ThreadContext context, @NotNull Supplier<V> supplier) {
        switch (context) {
            case SYNC:
                return supplySync(supplier);
            case ASYNC:
                return supplyAsync(supplier);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Schedules the supply of the Promise's result, via the given supplier.
     *
     * @param supplier the supplier
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplySync(@NotNull Supplier<V> supplier);

    /**
     * Schedules the supply of the Promise's result, via the given supplier.
     *
     * @param supplier the supplier
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyAsync(@NotNull Supplier<V> supplier);

    /**
     * Schedules the supply of the Promise's result, via the given supplier,
     * after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param supplier the supplier
     * @param delayTicks the delay in ticks
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    default Promise<V> supplyDelayed(@NotNull ThreadContext context, @NotNull Supplier<V> supplier, long delayTicks) {
        switch (context) {
            case SYNC:
                return supplyDelayedSync(supplier, delayTicks);
            case ASYNC:
                return supplyDelayedAsync(supplier, delayTicks);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Schedules the supply of the Promise's result, via the given supplier,
     * after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param supplier the supplier
     * @param delay the delay
     * @param unit the unit of delay
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    default Promise<V> supplyDelayed(@NotNull ThreadContext context, @NotNull Supplier<V> supplier, long delay, @NotNull TimeUnit unit) {
        switch (context) {
            case SYNC:
                return supplyDelayedSync(supplier, delay, unit);
            case ASYNC:
                return supplyDelayedAsync(supplier, delay, unit);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Schedules the supply of the Promise's result, via the given supplier,
     * after the delay has elapsed.
     *
     * @param supplier the supplier
     * @param delayTicks the delay in ticks
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyDelayedSync(@NotNull Supplier<V> supplier, long delayTicks);

    /**
     * Schedules the supply of the Promise's result, via the given supplier,
     * after the delay has elapsed.
     *
     * @param supplier the supplier
     * @param delay the delay
     * @param unit the unit of delay
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyDelayedSync(@NotNull Supplier<V> supplier, long delay, @NotNull TimeUnit unit);

    /**
     * Schedules the supply of the Promise's result, via the given supplier,
     * after the delay has elapsed.
     *
     * @param supplier the supplier
     * @param delayTicks the delay in ticks
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyDelayedAsync(@NotNull Supplier<V> supplier, long delayTicks);

    /**
     * Schedules the supply of the Promise's result, via the given supplier,
     * after the delay has elapsed.
     *
     * @param supplier the supplier
     * @param delay the delay
     * @param unit the unit of delay
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyDelayedAsync(@NotNull Supplier<V> supplier, long delay, @NotNull TimeUnit unit);

    /**
     * Schedules the supply of the Promise's result, via the given callable.
     *
     * @param context the type of executor to use to supply the promise
     * @param callable the callable
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    default Promise<V> supplyExceptionally(@NotNull ThreadContext context, @NotNull Callable<V> callable) {
        switch (context) {
            case SYNC:
                return supplyExceptionallySync(callable);
            case ASYNC:
                return supplyExceptionallyAsync(callable);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Schedules the supply of the Promise's result, via the given callable.
     *
     * @param callable the callable
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyExceptionallySync(@NotNull Callable<V> callable);

    /**
     * Schedules the supply of the Promise's result, via the given callable.
     *
     * @param callable the callable
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyExceptionallyAsync(@NotNull Callable<V> callable);

    /**
     * Schedules the supply of the Promise's result, via the given callable,
     * after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param callable the callable
     * @param delayTicks the delay in ticks
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    default Promise<V> supplyExceptionallyDelayed(@NotNull ThreadContext context, @NotNull Callable<V> callable, long delayTicks) {
        switch (context) {
            case SYNC:
                return supplyExceptionallyDelayedSync(callable, delayTicks);
            case ASYNC:
                return supplyExceptionallyDelayedAsync(callable, delayTicks);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Schedules the supply of the Promise's result, via the given callable,
     * after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param callable the callable
     * @param delay the delay
     * @param unit the unit of delay
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    default Promise<V> supplyExceptionallyDelayed(@NotNull ThreadContext context, @NotNull Callable<V> callable, long delay, @NotNull TimeUnit unit) {
        switch (context) {
            case SYNC:
                return supplyExceptionallyDelayedSync(callable, delay, unit);
            case ASYNC:
                return supplyExceptionallyDelayedAsync(callable, delay, unit);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Schedules the supply of the Promise's result, via the given callable,
     * after the delay has elapsed.
     *
     * @param callable the callable
     * @param delayTicks the delay in ticks
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyExceptionallyDelayedSync(@NotNull Callable<V> callable, long delayTicks);

    /**
     * Schedules the supply of the Promise's result, via the given callable,
     * after the delay has elapsed.
     *
     * @param callable the callable
     * @param delay the delay
     * @param unit the unit of delay
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyExceptionallyDelayedSync(@NotNull Callable<V> callable, long delay, @NotNull TimeUnit unit);

    /**
     * Schedules the supply of the Promise's result, via the given callable,
     * after the delay has elapsed.
     *
     * @param callable the callable
     * @param delayTicks the delay in ticks
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyExceptionallyDelayedAsync(@NotNull Callable<V> callable, long delayTicks);

    /**
     * Schedules the supply of the Promise's result, via the given callable,
     * after the delay has elapsed.
     *
     * @param callable the callable
     * @param delay the delay
     * @param unit the unit of delay
     * @return the same promise
     * @throws IllegalStateException if the promise is already being supplied, or has already been completed.
     */
    @NotNull
    Promise<V> supplyExceptionallyDelayedAsync(@NotNull Callable<V> callable, long delay, @NotNull TimeUnit unit);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function.
     *
     * @param context the type of executor to use to supply the promise
     * @param fn the function to use to compute the value
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    default <U> Promise<U> thenApply(@NotNull ThreadContext context, @NotNull Function<? super V, ? extends U> fn) {
        switch (context) {
            case SYNC:
                return thenApplySync(fn);
            case ASYNC:
                return thenApplyAsync(fn);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function.
     *
     * @param fn the function to use to compute the value
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenApplySync(@NotNull Function<? super V, ? extends U> fn);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function.
     *
     * @param fn the function to use to compute the value
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenApplyAsync(@NotNull Function<? super V, ? extends U> fn);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param fn the function to use to compute the value
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    default <U> Promise<U> thenApplyDelayed(@NotNull ThreadContext context, @NotNull Function<? super V, ? extends U> fn, long delayTicks) {
        switch (context) {
            case SYNC:
                return thenApplyDelayedSync(fn, delayTicks);
            case ASYNC:
                return thenApplyDelayedAsync(fn, delayTicks);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param fn the function to use to compute the value
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    default <U> Promise<U> thenApplyDelayed(@NotNull ThreadContext context, @NotNull Function<? super V, ? extends U> fn, long delay, @NotNull TimeUnit unit) {
        switch (context) {
            case SYNC:
                return thenApplyDelayedSync(fn, delay, unit);
            case ASYNC:
                return thenApplyDelayedAsync(fn, delay, unit);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param fn the function to use to compute the value
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenApplyDelayedSync(@NotNull Function<? super V, ? extends U> fn, long delayTicks);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param fn the function to use to compute the value
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenApplyDelayedSync(@NotNull Function<? super V, ? extends U> fn, long delay, @NotNull TimeUnit unit);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param fn the function to use to compute the value
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenApplyDelayedAsync(@NotNull Function<? super V, ? extends U> fn, long delayTicks);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param fn the function to use to compute the value
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenApplyDelayedAsync(@NotNull Function<? super V, ? extends U> fn, long delay, @NotNull TimeUnit unit);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * action.
     *
     * @param context the type of executor to use to supply the promise
     * @param action the action to perform before completing the returned future
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenAccept(@NotNull ThreadContext context, @NotNull Consumer<? super V> action) {
        switch (context) {
            case SYNC:
                return thenAcceptSync(action);
            case ASYNC:
                return thenAcceptAsync(action);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * action.
     *
     * @param action the action to perform before completing the returned future
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenAcceptSync(@NotNull Consumer<? super V> action) {
        return thenApplySync(Delegates.consumerToFunction(action));
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * action.
     *
     * @param action the action to perform before completing the returned future
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenAcceptAsync(@NotNull Consumer<? super V> action) {
        return thenApplyAsync(Delegates.consumerToFunction(action));
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * action, after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param action the action to perform before completing the returned future
     * @param delayTicks the delay in ticks
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenAcceptDelayed(@NotNull ThreadContext context, @NotNull Consumer<? super V> action, long delayTicks) {
        switch (context) {
            case SYNC:
                return thenAcceptDelayedSync(action, delayTicks);
            case ASYNC:
                return thenAcceptDelayedAsync(action, delayTicks);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * action, after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param action the action to perform before completing the returned future
     * @param delay the delay
     * @param unit the unit of delay
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenAcceptDelayed(@NotNull ThreadContext context, @NotNull Consumer<? super V> action, long delay, @NotNull TimeUnit unit) {
        switch (context) {
            case SYNC:
                return thenAcceptDelayedSync(action, delay, unit);
            case ASYNC:
                return thenAcceptDelayedAsync(action, delay, unit);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * action, after the delay has elapsed.
     *
     * @param action the action to perform before completing the returned future
     * @param delayTicks the delay in ticks
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenAcceptDelayedSync(@NotNull Consumer<? super V> action, long delayTicks) {
        return thenApplyDelayedSync(Delegates.consumerToFunction(action), delayTicks);
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * action, after the delay has elapsed.
     *
     * @param action the action to perform before completing the returned future
     * @param delay the delay
     * @param unit the unit of delay
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenAcceptDelayedSync(@NotNull Consumer<? super V> action, long delay, @NotNull TimeUnit unit) {
        return thenApplyDelayedSync(Delegates.consumerToFunction(action), delay, unit);
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * action, after the delay has elapsed.
     *
     * @param action the action to perform before completing the returned future
     * @param delayTicks the delay in ticks
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenAcceptDelayedAsync(@NotNull Consumer<? super V> action, long delayTicks) {
        return thenApplyDelayedAsync(Delegates.consumerToFunction(action), delayTicks);
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * action, after the delay has elapsed.
     *
     * @param action the action to perform before completing the returned future
     * @param delay the delay
     * @param unit the unit of delay
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenAcceptDelayedAsync(@NotNull Consumer<? super V> action, long delay, @NotNull TimeUnit unit) {
        return thenApplyDelayedAsync(Delegates.consumerToFunction(action), delay, unit);
    }

    /**
     * Returns a new Promise that, when this promise completes normally, executes
     * the given task.
     *
     * @param context the type of executor to use to supply the promise
     * @param action the action to run before completing the returned future
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenRun(@NotNull ThreadContext context, @NotNull Runnable action) {
        switch (context) {
            case SYNC:
                return thenRunSync(action);
            case ASYNC:
                return thenRunAsync(action);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, executes
     * the given task.
     *
     * @param action the action to run before completing the returned future
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenRunSync(@NotNull Runnable action) {
        return thenApplySync(Delegates.runnableToFunction(action));
    }

    /**
     * Returns a new Promise that, when this promise completes normally, executes
     * the given task.
     *
     * @param action the action to run before completing the returned future
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenRunAsync(@NotNull Runnable action) {
        return thenApplyAsync(Delegates.runnableToFunction(action));
    }

    /**
     * Returns a new Promise that, when this promise completes normally, executes
     * the given task, after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param action the action to run before completing the returned future
     * @param delayTicks the delay in ticks
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenRunDelayed(@NotNull ThreadContext context, @NotNull Runnable action, long delayTicks) {
        switch (context) {
            case SYNC:
                return thenRunDelayedSync(action, delayTicks);
            case ASYNC:
                return thenRunDelayedAsync(action, delayTicks);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, executes
     * the given task, after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param action the action to run before completing the returned future
     * @param delay the delay
     * @param unit the unit of delay
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenRunDelayed(@NotNull ThreadContext context, @NotNull Runnable action, long delay, @NotNull TimeUnit unit) {
        switch (context) {
            case SYNC:
                return thenRunDelayedSync(action, delay, unit);
            case ASYNC:
                return thenRunDelayedAsync(action, delay, unit);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, executes
     * the given task, after the delay has elapsed.
     *
     * @param action the action to run before completing the returned future
     * @param delayTicks the delay in ticks
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenRunDelayedSync(@NotNull Runnable action, long delayTicks) {
        return thenApplyDelayedSync(Delegates.runnableToFunction(action), delayTicks);
    }

    /**
     * Returns a new Promise that, when this promise completes normally, executes
     * the given task, after the delay has elapsed.
     *
     * @param action the action to run before completing the returned future
     * @param delay the delay
     * @param unit the unit of delay
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenRunDelayedSync(@NotNull Runnable action, long delay, @NotNull TimeUnit unit) {
        return thenApplyDelayedSync(Delegates.runnableToFunction(action), delay, unit);
    }

    /**
     * Returns a new Promise that, when this promise completes normally, executes
     * the given task, after the delay has elapsed.
     *
     * @param action the action to run before completing the returned future
     * @param delayTicks the delay in ticks
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenRunDelayedAsync(@NotNull Runnable action, long delayTicks) {
        return thenApplyDelayedAsync(Delegates.runnableToFunction(action), delayTicks);
    }

    /**
     * Returns a new Promise that, when this promise completes normally, executes
     * the given task, after the delay has elapsed.
     *
     * @param action the action to run before completing the returned future
     * @param delay the delay
     * @param unit the unit of delay
     * @return the new promise
     */
    @NotNull
    default Promise<Void> thenRunDelayedAsync(@NotNull Runnable action, long delay, @NotNull TimeUnit unit) {
        return thenApplyDelayedAsync(Delegates.runnableToFunction(action), delay, unit);
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function.
     *
     * @param context the type of executor to use to supply the promise
     * @param fn the function to use to compute the value
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    default <U> Promise<U> thenCompose(@NotNull ThreadContext context, @NotNull Function<? super V, ? extends Promise<U>> fn) {
        switch (context) {
            case SYNC:
                return thenComposeSync(fn);
            case ASYNC:
                return thenComposeAsync(fn);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function.
     *
     * @param fn the function to use to compute the value
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenComposeSync(@NotNull Function<? super V, ? extends Promise<U>> fn);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function.
     *
     * @param fn the function to use to compute the value
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenComposeAsync(@NotNull Function<? super V, ? extends Promise<U>> fn);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param fn the function to use to compute the value
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    default <U> Promise<U> thenComposeDelayedSync(@NotNull ThreadContext context, @NotNull Function<? super V, ? extends Promise<U>> fn, long delayTicks) {
        switch (context) {
            case SYNC:
                return thenComposeDelayedSync(fn, delayTicks);
            case ASYNC:
                return thenComposeDelayedAsync(fn, delayTicks);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param fn the function to use to compute the value
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    default <U> Promise<U> thenComposeDelayedSync(@NotNull ThreadContext context, @NotNull Function<? super V, ? extends Promise<U>> fn, long delay, @NotNull TimeUnit unit) {
        switch (context) {
            case SYNC:
                return thenComposeDelayedSync(fn, delay, unit);
            case ASYNC:
                return thenComposeDelayedAsync(fn, delay, unit);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param fn the function to use to compute the value
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenComposeDelayedSync(@NotNull Function<? super V, ? extends Promise<U>> fn, long delayTicks);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param fn the function to use to compute the value
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenComposeDelayedSync(@NotNull Function<? super V, ? extends Promise<U>> fn, long delay, @NotNull TimeUnit unit);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param fn the function to use to compute the value
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenComposeDelayedAsync(@NotNull Function<? super V, ? extends Promise<U>> fn, long delayTicks);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's result as the argument to the given
     * function, after the delay has elapsed.
     *
     * @param fn the function to use to compute the value
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> thenComposeDelayedAsync(@NotNull Function<? super V, ? extends Promise<U>> fn, long delay, @NotNull TimeUnit unit);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's exception as the argument to the given
     * function. Otherwise, if this promise completes normally, then the
     * returned promise also completes normally with the same value.
     *
     * @param context the type of executor to use to supply the promise
     * @param fn the function to use to compute the value of the returned
     *           Promise, if this promise completed exceptionally
     * @return the new promise
     */
    @NotNull
    default Promise<V> exceptionally(@NotNull ThreadContext context, @NotNull Function<Throwable, ? extends V> fn) {
        switch (context) {
            case SYNC:
                return exceptionallySync(fn);
            case ASYNC:
                return exceptionallyAsync(fn);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's exception as the argument to the given
     * function. Otherwise, if this promise completes normally, then the
     * returned promise also completes normally with the same value.
     *
     * @param fn the function to use to compute the value of the returned
     *           Promise, if this promise completed exceptionally
     * @return the new promise
     */
    @NotNull
    Promise<V> exceptionallySync(@NotNull Function<Throwable, ? extends V> fn);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's exception as the argument to the given
     * function. Otherwise, if this promise completes normally, then the
     * returned promise also completes normally with the same value.
     *
     * @param fn the function to use to compute the value of the returned
     *           Promise, if this promise completed exceptionally
     * @return the new promise
     */
    @NotNull
    Promise<V> exceptionallyAsync(@NotNull Function<Throwable, ? extends V> fn);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's exception as the argument to the given
     * function, after the delay has elapsed. Otherwise, if this promise
     * completes normally, then the returned promise also completes normally
     * with the same value.
     *
     * @param context the type of executor to use to supply the promise
     * @param fn the function to use to compute the value of the returned
     *           Promise, if this promise completed exceptionally
     * @param delayTicks the delay in ticks
     * @return the new promise
     */
    @NotNull
    default Promise<V> exceptionallyDelayed(@NotNull ThreadContext context, @NotNull Function<Throwable, ? extends V> fn, long delayTicks) {
        switch (context) {
            case SYNC:
                return exceptionallyDelayedSync(fn, delayTicks);
            case ASYNC:
                return exceptionallyDelayedAsync(fn, delayTicks);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's exception as the argument to the given
     * function, after the delay has elapsed. Otherwise, if this promise
     * completes normally, then the returned promise also completes normally
     * with the same value.
     *
     * @param context the type of executor to use to supply the promise
     * @param fn the function to use to compute the value of the returned
     *           Promise, if this promise completed exceptionally
     * @param delay the delay
     * @param unit the unit of delay
     * @return the new promise
     */
    @NotNull
    default Promise<V> exceptionallyDelayed(@NotNull ThreadContext context, @NotNull Function<Throwable, ? extends V> fn, long delay, @NotNull TimeUnit unit) {
        switch (context) {
            case SYNC:
                return exceptionallyDelayedSync(fn, delay, unit);
            case ASYNC:
                return exceptionallyDelayedAsync(fn, delay, unit);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's exception as the argument to the given
     * function, after the delay has elapsed. Otherwise, if this promise
     * completes normally, then the returned promise also completes normally
     * with the same value.
     *
     * @param fn the function to use to compute the value of the returned
     *           Promise, if this promise completed exceptionally
     * @param delayTicks the delay in ticks
     * @return the new promise
     */
    @NotNull
    Promise<V> exceptionallyDelayedSync(@NotNull Function<Throwable, ? extends V> fn, long delayTicks);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's exception as the argument to the given
     * function, after the delay has elapsed. Otherwise, if this promise
     * completes normally, then the returned promise also completes normally
     * with the same value.
     *
     * @param fn the function to use to compute the value of the returned
     *           Promise, if this promise completed exceptionally
     * @param delay the delay
     * @param unit the unit of delay
     * @return the new promise
     */
    @NotNull
    Promise<V> exceptionallyDelayedSync(@NotNull Function<Throwable, ? extends V> fn, long delay, @NotNull TimeUnit unit);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's exception as the argument to the given
     * function, after the delay has elapsed. Otherwise, if this promise
     * completes normally, then the returned promise also completes normally
     * with the same value.
     *
     * @param fn the function to use to compute the value of the returned
     *           Promise, if this promise completed exceptionally
     * @param delayTicks the delay in ticks
     * @return the new promise
     */
    @NotNull
    Promise<V> exceptionallyDelayedAsync(@NotNull Function<Throwable, ? extends V> fn, long delayTicks);

    /**
     * Returns a new Promise that, when this promise completes normally, is
     * executed with this promise's exception as the argument to the given
     * function, after the delay has elapsed. Otherwise, if this promise
     * completes normally, then the returned promise also completes normally
     * with the same value.
     *
     * @param fn the function to use to compute the value of the returned
     *           Promise, if this promise completed exceptionally
     * @param delay the delay
     * @param unit the unit of delay
     * @return the new promise
     */
    @NotNull
    Promise<V> exceptionallyDelayedAsync(@NotNull Function<Throwable, ? extends V> fn, long delay, @NotNull TimeUnit unit);


    /**
     * Returns a {@link CompletableFuture} maintaining the same
     * completion properties as this Promise.
     *
     * A Promise implementation that does not choose to interoperate
     * with CompletableFutures may throw {@code UnsupportedOperationException}.
     *
     * @return the CompletableFuture
     * @throws UnsupportedOperationException if this implementation
     * does not interoperate with CompletableFuture
     */
    CompletableFuture<V> toCompletableFuture();

}
