package project.cyberproton.atom.modifier;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public interface ExpiringModifier<M extends Modifier<?>> {
    @NotNull
    M getModifier();

    boolean hasExpired();

    @NotNull
    static <T extends Modifier<?>> ExpiringModifier<T> expiring(@NotNull T modifier, long duration, @NotNull TimeUnit unit) {
        Objects.requireNonNull(modifier, "modifier");
        return new SimpleExpiringModifier<>(modifier, unit.toMillis(duration));
    }

    @NotNull
    static <T extends Modifier<?>> ExpiringModifier<T> expiring(@NotNull T modifier, long duration) {
        Objects.requireNonNull(modifier, "modifier");
        return new SimpleExpiringModifier<>(modifier, duration);
    }
}
