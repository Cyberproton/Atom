package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public interface KeyValue<T> {
    static <T> KeyValue<T> of(@NotNull TypedKey<T> key, @NotNull Value<T> value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        return new DefaultKeyValue<>(key, value);
    }

    @NotNull
    TypedKey<T> getKey();

    @NotNull
    Value<T> getValue();
}
