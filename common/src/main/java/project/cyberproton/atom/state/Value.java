package project.cyberproton.atom.state;

import project.cyberproton.atom.util.Ticks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface Value<T> {
    static <T> Value<T> of(@Nullable T value) {
        return new DefaultValue<>(value);
    }

    static <T> Value<T> ofOrDefault(@Nullable T value, @NotNull Supplier<T> def) {
        if (value == null) {
            return new DefaultValue<>(Objects.requireNonNull(def.get()));
        }
        return new DefaultValue<>(value);
    }

    static <T> Value<T> ofNull() {
        return new DefaultValue<>(null);
    }

    static <T> TransientValue<T> expiring(T value, long ticks) {
        return new ExpiringValue<>(value, Ticks.to(ticks, TimeUnit.MILLISECONDS));
    }

    static <T> TransientValue<T> expiring(T value, long duration, TimeUnit unit) {
        return new ExpiringValue<>(value, unit.toMillis(duration));
    }

    @NotNull
    default T get() {
        T value = getOrNull();
        if (value == null) {
            throw new NoSuchElementException("Value is null");
        }
        return value;
    }

    @Nullable
    T getOrNull();
}
