package project.cyberproton.atom.state;

import org.jetbrains.annotations.Nullable;

public class DefaultValue<T> implements Value<T> {
    protected final T value;

    public DefaultValue(@Nullable T value) {
        this.value = value;
    }

    @Nullable
    @Override
    public T getOrNull() {
        return value;
    }
}
