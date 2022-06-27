package project.cyberproton.atom.promise;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.exception.Exceptions;
import project.cyberproton.atom.util.Delegate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Implementation of {@link Promise} using the server scheduler.
 *
 * @param <V> the result type
 */
@SuppressWarnings("UnstableApiUsage")
class DefaultPromise<V> implements Promise<V> {

    private final AtomPlugin plugin;

    /**
     * If the promise is currently being supplied
     */
    private final AtomicBoolean supplied = new AtomicBoolean(false);

    /**
     * If the execution of the promise is cancelled
     */
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    /**
     * The completable future backing this promise
     */
    @NotNull
    private final CompletableFuture<V> fut;

    DefaultPromise(@NotNull AtomPlugin plugin) {
        this.plugin = plugin;
        this.fut = new CompletableFuture<>();
    }

    private DefaultPromise(@NotNull AtomPlugin plugin, @Nullable V v) {
        this.plugin = plugin;
        this.fut = CompletableFuture.completedFuture(v);
        this.supplied.set(true);
    }

    private DefaultPromise(@NotNull AtomPlugin plugin, @NotNull Throwable t) {
        this.plugin = plugin;
        (this.fut = new CompletableFuture<>()).completeExceptionally(t);
        this.supplied.set(true);
    }

    private DefaultPromise(@NotNull AtomPlugin plugin, @NotNull CompletableFuture<V> fut) {
        this.plugin = plugin;
        this.fut = Objects.requireNonNull(fut, "future");
        this.supplied.set(true);
        this.cancelled.set(fut.isCancelled());
    }

    /* utility methods */

    private void executeSync(@NotNull Runnable runnable) {
        /*
        if (ThreadContext.forCurrentThread() == ThreadContext.SYNC) {
            HelperExceptions.wrapSchedulerTask(runnable).run();
        } else {
            plugin.getScheduler().sync().execute(runnable);
        }

         */
        plugin.getScheduler().sync().execute(runnable);
    }

    private void executeAsync(@NotNull Runnable runnable) {
        plugin.getScheduler().async().execute(runnable);
    }

    private void executeDelayedSync(@NotNull Runnable runnable, long delayTicks) {
        if (delayTicks <= 0) {
            executeSync(runnable);
        } else {
            plugin.getScheduler().sync().runLater(runnable, delayTicks);
        }
    }

    private void executeDelayedAsync(@NotNull Runnable runnable, long delayTicks) {
        if (delayTicks <= 0) {
            executeAsync(runnable);
        } else {
            plugin.getScheduler().async().runLater(runnable, delayTicks);
        }
    }

    private void executeDelayedSync(@NotNull Runnable runnable, long delay, TimeUnit unit) {
        if (delay <= 0) {
            executeSync(runnable);
        } else {
            plugin.getScheduler().sync().runLater(runnable, delay, unit);
        }
    }

    private void executeDelayedAsync(@NotNull Runnable runnable, long delay, TimeUnit unit) {
        if (delay <= 0) {
            executeAsync(runnable);
        } else {
            plugin.getScheduler().async().runLater(runnable, delay, unit);
        }
    }

    private boolean complete(V value) {
        return !this.cancelled.get() && this.fut.complete(value);
    }

    private boolean completeExceptionally(@NotNull Throwable t) {
        return !this.cancelled.get() && this.fut.completeExceptionally(t);
    }

    private void markAsSupplied() {
        if (!this.supplied.compareAndSet(false, true)) {
            throw new IllegalStateException("Promise is already being supplied.");
        }
    }

    /* future methods */

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        this.cancelled.set(true);
        return this.fut.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return this.fut.isCancelled();
    }

    @Override
    public boolean isDone() {
        return this.fut.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return this.fut.get();
    }

    @Override
    public V get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.fut.get(timeout, unit);
    }

    @Override
    public V join() {
        return this.fut.join();
    }

    @Override
    public V getNow(V valueIfAbsent) {
        return this.fut.getNow(valueIfAbsent);
    }

    @Override
    public CompletableFuture<V> toCompletableFuture() {
        return this.fut.thenApply(Function.identity());
    }

    @Override
    public void close() {
        cancel();
    }

    @Override
    public boolean isClosed() {
        return isCancelled();
    }

    /* implementation */

    @NotNull
    @Override
    public Promise<V> supply(@Nullable V value) {
        markAsSupplied();
        complete(value);
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyException(@NotNull Throwable exception) {
        markAsSupplied();
        completeExceptionally(exception);
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplySync(@NotNull Supplier<V> supplier) {
        markAsSupplied();
        executeSync(new SupplyRunnable(supplier));
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyAsync(@NotNull Supplier<V> supplier) {
        markAsSupplied();
        executeAsync(new SupplyRunnable(supplier));
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyDelayedSync(@NotNull Supplier<V> supplier, long delayTicks) {
        markAsSupplied();
        executeDelayedSync(new SupplyRunnable(supplier), delayTicks);
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyDelayedSync(@NotNull Supplier<V> supplier, long delay, @NotNull TimeUnit unit) {
        markAsSupplied();
        executeDelayedSync(new SupplyRunnable(supplier), delay, unit);
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyDelayedAsync(@NotNull Supplier<V> supplier, long delayTicks) {
        markAsSupplied();
        executeDelayedAsync(new SupplyRunnable(supplier), delayTicks);
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyDelayedAsync(@NotNull Supplier<V> supplier, long delay, @NotNull TimeUnit unit) {
        markAsSupplied();
        executeDelayedAsync(new SupplyRunnable(supplier), delay, unit);
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyExceptionallySync(@NotNull Callable<V> callable) {
        markAsSupplied();
        executeSync(new ThrowingSupplyRunnable(callable));
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyExceptionallyAsync(@NotNull Callable<V> callable) {
        markAsSupplied();
        executeAsync(new ThrowingSupplyRunnable(callable));
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyExceptionallyDelayedSync(@NotNull Callable<V> callable, long delayTicks) {
        markAsSupplied();
        executeDelayedSync(new ThrowingSupplyRunnable(callable), delayTicks);
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyExceptionallyDelayedSync(@NotNull Callable<V> callable, long delay, @NotNull TimeUnit unit) {
        markAsSupplied();
        executeDelayedSync(new ThrowingSupplyRunnable(callable), delay, unit);
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyExceptionallyDelayedAsync(@NotNull Callable<V> callable, long delayTicks) {
        markAsSupplied();
        executeDelayedAsync(new ThrowingSupplyRunnable(callable), delayTicks);
        return this;
    }

    @NotNull
    @Override
    public Promise<V> supplyExceptionallyDelayedAsync(@NotNull Callable<V> callable, long delay, @NotNull TimeUnit unit) {
        markAsSupplied();
        executeDelayedAsync(new ThrowingSupplyRunnable(callable), delay, unit);
        return this;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenApplySync(@NotNull Function<? super V, ? extends U> fn) {
        DefaultPromise<U> promise = new DefaultPromise<>(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeSync(new ApplyRunnable<>(promise, fn, value));
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenApplyAsync(@NotNull Function<? super V, ? extends U> fn) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeAsync(new ApplyRunnable<>(promise, fn, value));
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenApplyDelayedSync(@NotNull Function<? super V, ? extends U> fn, long delayTicks) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeDelayedSync(new ApplyRunnable<>(promise, fn, value), delayTicks);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenApplyDelayedSync(@NotNull Function<? super V, ? extends U> fn, long delay, @NotNull TimeUnit unit) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeDelayedSync(new ApplyRunnable<>(promise, fn, value), delay, unit);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenApplyDelayedAsync(@NotNull Function<? super V, ? extends U> fn, long delayTicks) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeDelayedAsync(new ApplyRunnable<>(promise, fn, value), delayTicks);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenApplyDelayedAsync(@NotNull Function<? super V, ? extends U> fn, long delay, @NotNull TimeUnit unit) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeDelayedAsync(new ApplyRunnable<>(promise, fn, value), delay, unit);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenComposeSync(@NotNull Function<? super V, ? extends Promise<U>> fn) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeSync(new ComposeRunnable<>(promise, fn, value, true));
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenComposeAsync(@NotNull Function<? super V, ? extends Promise<U>> fn) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeAsync(new ComposeRunnable<>(promise, fn, value, false));
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenComposeDelayedSync(@NotNull Function<? super V, ? extends Promise<U>> fn, long delayTicks) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeDelayedSync(new ComposeRunnable<>(promise, fn, value, true), delayTicks);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenComposeDelayedSync(@NotNull Function<? super V, ? extends Promise<U>> fn, long delay, @NotNull TimeUnit unit) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeDelayedSync(new ComposeRunnable<>(promise, fn, value, true), delay, unit);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenComposeDelayedAsync(@NotNull Function<? super V, ? extends Promise<U>> fn, long delayTicks) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeDelayedAsync(new ComposeRunnable<>(promise, fn, value, false), delayTicks);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public <U> Promise<U> thenComposeDelayedAsync(@NotNull Function<? super V, ? extends Promise<U>> fn, long delay, @NotNull TimeUnit unit) {
        DefaultPromise<U> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t != null) {
                promise.completeExceptionally(t);
            } else {
                executeDelayedAsync(new ComposeRunnable<>(promise, fn, value, false), delay, unit);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public Promise<V> exceptionallySync(@NotNull Function<Throwable, ? extends V> fn) {
        DefaultPromise<V> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t == null) {
                promise.complete(value);
            } else {
                executeSync(new ExceptionallyRunnable<>(promise, fn, t));
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public Promise<V> exceptionallyAsync(@NotNull Function<Throwable, ? extends V> fn) {
        DefaultPromise<V> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t == null) {
                promise.complete(value);
            } else {
                executeAsync(new ExceptionallyRunnable<>(promise, fn, t));
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public Promise<V> exceptionallyDelayedSync(@NotNull Function<Throwable, ? extends V> fn, long delayTicks) {
        DefaultPromise<V> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t == null) {
                promise.complete(value);
            } else {
                executeDelayedSync(new ExceptionallyRunnable<>(promise, fn, t), delayTicks);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public Promise<V> exceptionallyDelayedSync(@NotNull Function<Throwable, ? extends V> fn, long delay, @NotNull TimeUnit unit) {
        DefaultPromise<V> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t == null) {
                promise.complete(value);
            } else {
                executeDelayedSync(new ExceptionallyRunnable<>(promise, fn, t), delay, unit);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public Promise<V> exceptionallyDelayedAsync(@NotNull Function<Throwable, ? extends V> fn, long delayTicks) {
        DefaultPromise<V> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t == null) {
                promise.complete(value);
            } else {
                executeDelayedAsync(new ExceptionallyRunnable<>(promise, fn, t), delayTicks);
            }
        });
        return promise;
    }

    @NotNull
    @Override
    public Promise<V> exceptionallyDelayedAsync(@NotNull Function<Throwable, ? extends V> fn, long delay, @NotNull TimeUnit unit) {
        DefaultPromise<V> promise = new DefaultPromise(plugin);
        this.fut.whenComplete((value, t) -> {
            if (t == null) {
                promise.complete(value);
            } else {
                executeDelayedAsync(new ExceptionallyRunnable<>(promise, fn, t), delay, unit);
            }
        });
        return promise;
    }

    /* delegating behaviour runnables */

    private final class ThrowingSupplyRunnable implements Runnable, Delegate<Callable<V>> {
        private final Callable<V> supplier;
        private ThrowingSupplyRunnable(Callable<V> supplier) {
            this.supplier = supplier;
        }
        @Override public Callable<V> getDelegate() { return this.supplier; }

        @Override
        public void run() {
            if (DefaultPromise.this.cancelled.get()) {
                return;
            }
            try {
                DefaultPromise.this.fut.complete(this.supplier.call());
            } catch (Throwable t) {
                Exceptions.reportPromise(t);
                DefaultPromise.this.fut.completeExceptionally(t);
            }
        }
    }

    private final class SupplyRunnable implements Runnable, Delegate<Supplier<V>> {
        private final Supplier<V> supplier;
        private SupplyRunnable(Supplier<V> supplier) {
            this.supplier = supplier;
        }
        @Override public Supplier<V> getDelegate() { return this.supplier; }

        @Override
        public void run() {
            if (DefaultPromise.this.cancelled.get()) {
                return;
            }
            try {
                DefaultPromise.this.fut.complete(this.supplier.get());
            } catch (Throwable t) {
                Exceptions.reportPromise(t);
                DefaultPromise.this.fut.completeExceptionally(t);
            }
        }
    }

    private final class ApplyRunnable<U> implements Runnable, Delegate<Function> {
        private final DefaultPromise<U> promise;
        private final Function<? super V, ? extends U> function;
        private final V value;
        private ApplyRunnable(DefaultPromise<U> promise, Function<? super V, ? extends U> function, V value) {
            this.promise = promise;
            this.function = function;
            this.value = value;
        }
        @Override public Function getDelegate() { return this.function; }

        @Override
        public void run() {
            if (DefaultPromise.this.cancelled.get()) {
                return;
            }
            try {
                this.promise.complete(this.function.apply(this.value));
            } catch (Throwable t) {
                Exceptions.reportPromise(t);
                this.promise.completeExceptionally(t);
            }
        }
    }

    private final class ComposeRunnable<U> implements Runnable, Delegate<Function> {
        private final DefaultPromise<U> promise;
        private final Function<? super V, ? extends Promise<U>> function;
        private final V value;
        private final boolean sync;
        private ComposeRunnable(DefaultPromise<U> promise, Function<? super V, ? extends Promise<U>> function, V value, boolean sync) {
            this.promise = promise;
            this.function = function;
            this.value = value;
            this.sync = sync;
        }
        @Override public Function getDelegate() { return this.function; }

        @Override
        public void run() {
            if (DefaultPromise.this.cancelled.get()) {
                return;
            }
            try {
                Promise<U> p = this.function.apply(this.value);
                if (p == null) {
                    this.promise.complete(null);
                } else {
                    if (this.sync) {
                        p.thenAcceptSync(this.promise::complete);
                    } else {
                        p.thenAcceptAsync(this.promise::complete);
                    }
                }
            } catch (Throwable t) {
                Exceptions.reportPromise(t);
                this.promise.completeExceptionally(t);
            }
        }
    }

    private final class ExceptionallyRunnable<U> implements Runnable, Delegate<Function> {
        private final DefaultPromise<U> promise;
        private final Function<Throwable, ? extends U> function;
        private final Throwable t;
        private ExceptionallyRunnable(DefaultPromise<U> promise, Function<Throwable, ? extends U> function, Throwable t) {
            this.promise = promise;
            this.function = function;
            this.t = t;
        }
        @Override public Function getDelegate() { return this.function; }

        @Override
        public void run() {
            if (DefaultPromise.this.cancelled.get()) {
                return;
            }
            try {
                this.promise.complete(this.function.apply(this.t));
            } catch (Throwable t) {
                Exceptions.reportPromise(t);
                this.promise.completeExceptionally(t);
            }
        }
    }


    public static class Builder implements PromiseBuilder {
        private final AtomPlugin plugin;

        Builder(AtomPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        @NotNull
        public <U> Promise<U> empty() {
            return new DefaultPromise<>(plugin);
        }

        @Override
        @NotNull
        public Promise<Void> start() {
            return completed(null);
        }

        @Override
        @NotNull
        public <U> Promise<U> completed(@Nullable U value) {
            return new DefaultPromise<>(plugin, value);
        }

        @Override
        @NotNull
        public <U> Promise<U> exceptionally(@NotNull Throwable exception) {
            return new DefaultPromise<>(plugin, exception);
        }

        @Override
        @NotNull
        public <U> Promise<U> wrapFuture(@NotNull Future<U> future) {
            if (future instanceof CompletableFuture<?>) {
                return new DefaultPromise<>(plugin, ((CompletableFuture<U>) future).thenApply(Function.identity()));

            } else if (future instanceof CompletionStage<?>) {
                //noinspection unchecked
                CompletionStage<U> fut = (CompletionStage<U>) future;
                return new DefaultPromise<>(plugin, fut.toCompletableFuture().thenApply(Function.identity()));

            } else if (future instanceof ListenableFuture<?>) {
                ListenableFuture<U> fut = (ListenableFuture<U>) future;
                DefaultPromise<U> promise = new DefaultPromise<>(plugin);
                promise.supplied.set(true);

                Futures.addCallback(fut, new FutureCallback<U>() {
                    @Override
                    public void onSuccess(@Nullable U result) {
                        promise.complete(result);
                    }

                    @Override
                    public void onFailure(@NotNull Throwable t) {
                        promise.completeExceptionally(t);
                    }
                }, MoreExecutors.directExecutor());

                return promise;

            } else {
                if (future.isDone()) {
                    try {
                        return completed(future.get());
                    } catch (ExecutionException e) {
                        return exceptionally(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return supplyingExceptionallyAsync(future::get);
                }
            }
        }

        @Override
        @NotNull
        public <U> Promise<U> supplying(@NotNull ThreadContext context, @NotNull Supplier<U> supplier) {
            Promise<U> p = empty();
            return p.supply(context, supplier);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingSync(@NotNull Supplier<U> supplier) {
            Promise<U> p = empty();
            return p.supplySync(supplier);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingAsync(@NotNull Supplier<U> supplier) {
            Promise<U> p = empty();
            return p.supplyAsync(supplier);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingDelayed(@NotNull ThreadContext context, @NotNull Supplier<U> supplier, long delayTicks) {
            Promise<U> p = empty();
            return p.supplyDelayed(context, supplier, delayTicks);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingDelayed(@NotNull ThreadContext context, @NotNull Supplier<U> supplier, long delay, @NotNull TimeUnit unit) {
            Promise<U> p = empty();
            return p.supplyDelayed(context, supplier, delay, unit);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingDelayedSync(@NotNull Supplier<U> supplier, long delayTicks) {
            Promise<U> p = empty();
            return p.supplyDelayedSync(supplier, delayTicks);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingDelayedAsync(@NotNull Supplier<U> supplier, long delayTicks) {
            Promise<U> p = empty();
            return p.supplyDelayedAsync(supplier, delayTicks);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingDelayedAsync(@NotNull Supplier<U> supplier, long delay, @NotNull TimeUnit unit) {
            Promise<U> p = empty();
            return p.supplyDelayedAsync(supplier, delay, unit);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingExceptionally(@NotNull ThreadContext context, @NotNull Callable<U> callable) {
            Promise<U> p = empty();
            return p.supplyExceptionally(context, callable);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingExceptionallySync(@NotNull Callable<U> callable) {
            Promise<U> p = empty();
            return p.supplyExceptionallySync(callable);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingExceptionallyAsync(@NotNull Callable<U> callable) {
            Promise<U> p = empty();
            return p.supplyExceptionallyAsync(callable);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingExceptionallyDelayed(@NotNull ThreadContext context, @NotNull Callable<U> callable, long delayTicks) {
            Promise<U> p = empty();
            return p.supplyExceptionallyDelayed(context, callable, delayTicks);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingExceptionallyDelayed(@NotNull ThreadContext context, @NotNull Callable<U> callable, long delay, @NotNull TimeUnit unit) {
            Promise<U> p = empty();
            return p.supplyExceptionallyDelayed(context, callable, delay, unit);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingExceptionallyDelayedSync(@NotNull Callable<U> callable, long delayTicks) {
            Promise<U> p = empty();
            return p.supplyExceptionallyDelayedSync(callable, delayTicks);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingExceptionallyDelayedSync(@NotNull Callable<U> callable, long delay, @NotNull TimeUnit unit) {
            Promise<U> p = empty();
            return p.supplyExceptionallyDelayedSync(callable, delay, unit);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingExceptionallyDelayedAsync(@NotNull Callable<U> callable, long delayTicks) {
            Promise<U> p = empty();
            return p.supplyExceptionallyDelayedAsync(callable, delayTicks);
        }

        @Override
        @NotNull
        public <U> Promise<U> supplyingExceptionallyDelayedAsync(@NotNull Callable<U> callable, long delay, @NotNull TimeUnit unit) {
            Promise<U> p = empty();
            return p.supplyExceptionallyDelayedAsync(callable, delay, unit);
        }
    }
}
