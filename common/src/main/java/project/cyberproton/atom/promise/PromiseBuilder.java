package project.cyberproton.atom.promise;

import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface PromiseBuilder {
    static PromiseBuilder of(@NotNull AtomPlugin plugin) {
        return new DefaultPromise.Builder(plugin);
    }

    /**
     * Returns a new empty Promise
     *
     * <p>An empty promise can be 'completed' via the supply methods.</p>
     *
     * @param <U> the result type
     * @return a new empty promise
     */
    @NotNull
    <U> Promise<U> empty();

    /**
     * Returns a new base promise to be built on top of.
     *
     * @return a new promise
     */
    @NotNull
    Promise<Void> start();

    /**
     * Returns a Promise which is already completed with the given value.
     *
     * @param value the value
     * @param <U> the result type
     * @return a new completed promise
     */
    @NotNull
    <U> Promise<U> completed(@Nullable U value);

    /**
     * Returns a Promise which is already completed with the given exception.
     *
     * @param exception the exception
     * @param <U> the result type
     * @return the new completed promise
     */
    @NotNull
    <U> Promise<U> exceptionally(@NotNull Throwable exception);

    /**
     * Returns a Promise which represents the given future.
     *
     * <p>The implementation will make an attempt to wrap the future without creating a new process
     * to await the result (by casting to {@link java.util.concurrent.CompletionStage} or
     * {@link com.google.common.util.concurrent.ListenableFuture}).</p>
     *
     * <p>Calls to {@link Promise#cancel() cancel} the returned promise will not affected the wrapped
     * future.</p>
     *
     * @param future the future to wrap
     * @param <U> the result type
     * @return the new promise
     */
    @NotNull
    <U> Promise<U> wrapFuture(@NotNull Future<U> future);

    /**
     * Returns a new Promise, and schedules it's population via the given supplier.
     *
     * @param context the type of executor to use to supply the promise
     * @param supplier the value supplier
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplying(@NotNull ThreadContext context, @NotNull Supplier<U> supplier) {
        Promise<U> p = empty();
        return p.supply(context, supplier);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given supplier.
     *
     * @param supplier the value supplier
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingSync(@NotNull Supplier<U> supplier) {
        Promise<U> p = empty();
        return p.supplySync(supplier);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given supplier.
     *
     * @param supplier the value supplier
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingAsync(@NotNull Supplier<U> supplier) {
        Promise<U> p = empty();
        return p.supplyAsync(supplier);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given supplier,
     * after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param supplier the value supplier
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingDelayed(@NotNull ThreadContext context, @NotNull Supplier<U> supplier, long delayTicks) {
        Promise<U> p = empty();
        return p.supplyDelayed(context, supplier, delayTicks);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given supplier,
     * after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param supplier the value supplier
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingDelayed(@NotNull ThreadContext context, @NotNull Supplier<U> supplier, long delay, @NotNull TimeUnit unit) {
        Promise<U> p = empty();
        return p.supplyDelayed(context, supplier, delay, unit);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given supplier,
     * after the delay has elapsed.
     *
     * @param supplier the value supplier
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingDelayedSync(@NotNull Supplier<U> supplier, long delayTicks) {
        Promise<U> p = empty();
        return p.supplyDelayedSync(supplier, delayTicks);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given supplier,
     * after the delay has elapsed.
     *
     * @param supplier the value supplier
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingDelayedSync(@NotNull Supplier<U> supplier, long delay, @NotNull TimeUnit unit) {
        Promise<U> p = empty();
        return p.supplyDelayedSync(supplier, delay, unit);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given supplier,
     * after the delay has elapsed.
     *
     * @param supplier the value supplier
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingDelayedAsync(@NotNull Supplier<U> supplier, long delayTicks) {
        Promise<U> p = empty();
        return p.supplyDelayedAsync(supplier, delayTicks);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given supplier,
     * after the delay has elapsed.
     *
     * @param supplier the value supplier
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingDelayedAsync(@NotNull Supplier<U> supplier, long delay, @NotNull TimeUnit unit) {
        Promise<U> p = empty();
        return p.supplyDelayedAsync(supplier, delay, unit);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given callable.
     *
     * @param context the type of executor to use to supply the promise
     * @param callable the value callable
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingExceptionally(@NotNull ThreadContext context, @NotNull Callable<U> callable) {
        Promise<U> p = empty();
        return p.supplyExceptionally(context, callable);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given callable.
     *
     * @param callable the value callable
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingExceptionallySync(@NotNull Callable<U> callable) {
        Promise<U> p = empty();
        return p.supplyExceptionallySync(callable);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given callable.
     *
     * @param callable the value callable
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingExceptionallyAsync(@NotNull Callable<U> callable) {
        Promise<U> p = empty();
        return p.supplyExceptionallyAsync(callable);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given callable,
     * after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param callable the value callable
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingExceptionallyDelayed(@NotNull ThreadContext context, @NotNull Callable<U> callable, long delayTicks) {
        Promise<U> p = empty();
        return p.supplyExceptionallyDelayed(context, callable, delayTicks);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given callable,
     * after the delay has elapsed.
     *
     * @param context the type of executor to use to supply the promise
     * @param callable the value callable
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingExceptionallyDelayed(@NotNull ThreadContext context, @NotNull Callable<U> callable, long delay, @NotNull TimeUnit unit) {
        Promise<U> p = empty();
        return p.supplyExceptionallyDelayed(context, callable, delay, unit);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given callable,
     * after the delay has elapsed.
     *
     * @param callable the value callable
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingExceptionallyDelayedSync(@NotNull Callable<U> callable, long delayTicks) {
        Promise<U> p = empty();
        return p.supplyExceptionallyDelayedSync(callable, delayTicks);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given callable,
     * after the delay has elapsed.
     *
     * @param callable the value callable
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingExceptionallyDelayedSync(@NotNull Callable<U> callable, long delay, @NotNull TimeUnit unit) {
        Promise<U> p = empty();
        return p.supplyExceptionallyDelayedSync(callable, delay, unit);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given callable,
     * after the delay has elapsed.
     *
     * @param callable the value callable
     * @param delayTicks the delay in ticks
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingExceptionallyDelayedAsync(@NotNull Callable<U> callable, long delayTicks) {
        Promise<U> p = empty();
        return p.supplyExceptionallyDelayedAsync(callable, delayTicks);
    }

    /**
     * Returns a new Promise, and schedules it's population via the given callable,
     * after the delay has elapsed.
     *
     * @param callable the value callable
     * @param delay the delay
     * @param unit the unit of delay
     * @param <U> the result type
     * @return the promise
     */
    @NotNull
    default <U> Promise<U> supplyingExceptionallyDelayedAsync(@NotNull Callable<U> callable, long delay, @NotNull TimeUnit unit) {
        Promise<U> p = empty();
        return p.supplyExceptionallyDelayedAsync(callable, delay, unit);
    }
}
