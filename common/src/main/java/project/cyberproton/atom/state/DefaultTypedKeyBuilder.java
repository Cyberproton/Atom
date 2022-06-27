package project.cyberproton.atom.state;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

public class DefaultTypedKeyBuilder<V> implements TypedKey.Builder<V> {
    private Key key;
    private TypeToken<V> valueType;

    @Override
    public TypedKey.Builder<V> key(@NotNull Key key) {
        this.key = key;
        return this;
    }

    @Override
    public TypedKey.Builder<V> valueType(@NotNull TypeToken<V> type) {
        this.valueType = type;
        return this;
    }

    @NotNull
    @Override
    public TypedKey<V> build() {
        if (key == null) {
            throw new IllegalStateException("Key must not null");
        }
        if (valueType == null) {
            throw new IllegalStateException("Value type must not null");
        }
        return new DefaultTypedKey<>(key, valueType);
    }
}
