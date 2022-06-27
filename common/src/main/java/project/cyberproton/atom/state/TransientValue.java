package project.cyberproton.atom.state;

import project.cyberproton.atom.exception.ElementHasExpiredException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TransientValue<T> extends Value<T> {
    @Override
    @Nullable
    T getOrNull();

    @NotNull
    @Override
    default T get() {
        if (hasExpired()) {
            throw new ElementHasExpiredException("This value has expired");
        }
        return Value.super.get();
    }

    boolean hasExpired();
}
