package project.cyberproton.atom.state;

import org.jetbrains.annotations.Nullable;

public class ExpiringValue<T> extends DefaultValue<T> implements TransientValue<T> {
    private final long expireAt;

    public ExpiringValue(@Nullable T value, long durationMillis) {
        super(value);
        this.expireAt = System.currentTimeMillis() + durationMillis;
    }

    @Nullable
    @Override
    public T getOrNull() {
        if (hasExpired()) {
            return null;
        }
        return value;
    }

    @Override
    public boolean hasExpired() {
        return System.currentTimeMillis() >= expireAt;
    }
}
