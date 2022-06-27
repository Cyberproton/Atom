package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("unchecked")
public abstract class AbstractStore implements Store {
    protected final Map<TypedKey<?>, Value<?>> states = new HashMap<>();
    protected final Map<TypedKey<?>, Interceptor<?>> interceptors = new HashMap<>();
    protected final ReentrantLock lock = new ReentrantLock();

    protected AbstractStore(@NotNull StoreConfiguration configuration) {
        exactConfiguration(configuration);
        StoreConfiguration alternative = configure();
        if (alternative != null) {
            exactConfiguration(alternative);
        }
    }

    protected AbstractStore() {
        StoreConfiguration alternative = configure();
        if (alternative != null) {
            exactConfiguration(alternative);
        }
    }

    @Nullable
    protected StoreConfiguration configure() {
        return null;
    }

    protected Collection<Interceptor<?>> interceptors() {
        return interceptors.values();
    }

    @Override
    public <T> boolean has(@NotNull TypedKey<T> key) {
        try {
            lock.lock();
            return states.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Nullable
    @Override
    public <T> T getUnboxedOrNull(@NotNull TypedKey<T> key) {
        return get(key).getOrNull();
    }

    @NotNull
    @Override
    public <T> Value<T> get(@NotNull TypedKey<T> key) {
        try {
            lock.lock();
            Interceptor<?> interceptor = interceptors.get(key);
            if (interceptor != null && interceptor.getOnGet() != null) {
                return (Value<T>) interceptor.getOnGet().get();
            }
            Value<T> value = (Value<T>) states.get(key);
            if (value == null) return Value.of(null);
            if (value instanceof TransientValue<?> && ((TransientValue<?>) value).hasExpired()) {
                states.remove(key);
                return Value.of(null);
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    @NotNull
    @Override
    public Set<TypedKey<?>> getKeys() {
        try {
            lock.lock();
            return new HashSet<>(states.keySet());
        } finally {
            lock.unlock();
        }
    }

    @NotNull
    @Override
    public Collection<?> getValues() {
        try {
            lock.lock();
            return new ArrayList<>(states.values());
        } finally {
            lock.unlock();
        }
    }

    protected void withLock(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");
        try {
            lock.lock();
            runnable.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    protected void withLockOrThrow(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");
        try {
            lock.lock();
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    @Nullable
    protected <T> T withLockAndReturn(@NotNull Callable<T> callable) {
        Objects.requireNonNull(callable, "callable");
        try {
            lock.lock();
            return callable.call();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Nullable
    protected <T> T withLockAndReturnOrThrow(@NotNull Callable<T> callable) {
        try {
            lock.lock();
            return callable.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            lock.unlock();
        }
    }

    private void exactConfiguration(StoreConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration");
        for (KeyValue<?> keyValue : configuration.getInitialKeyValues()) {
            states.put(keyValue.getKey(), keyValue.getValue());
        }
        for (Interceptor<?> interceptor : configuration.getInterceptors()) {
            interceptors.put(interceptor.getKey(), interceptor);
        }
    }
}
