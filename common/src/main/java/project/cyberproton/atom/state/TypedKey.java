package project.cyberproton.atom.state;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

public interface TypedKey<V> extends Keyed {
    static <V> TypedKey<V> of(@NotNull Key key, @NotNull TypeToken<V> type) {
        return new DefaultTypedKey<>(key, type);
    }

    static <V> TypedKey<V> of(@NotNull Key key, Class<V> type) {
        return of(key, TypeToken.of(type));
    }

    static TypedKey.Builder<?> builder() {
        return new DefaultTypedKeyBuilder<>();
    }

    @NotNull
    TypeToken<V> getValueType();

    interface Builder<V> {
        Builder<V> key(@NotNull Key key);

        Builder<V> valueType(@NotNull TypeToken<V> type);

        default Builder<V> valueType(@NotNull Class<V> type) {
            return valueType(TypeToken.of(type));
        }

        @NotNull
        TypedKey<V> build();
    }
}
