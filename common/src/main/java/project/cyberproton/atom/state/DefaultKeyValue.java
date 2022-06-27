package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;

public class DefaultKeyValue<T> implements KeyValue<T> {
    private final TypedKey<T> key;
    private final Value<T> value;

    public DefaultKeyValue(@NotNull TypedKey<T> key, @NotNull Value<T> value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    @Override
    public TypedKey<T> getKey() {
        return key;
    }

    @NotNull
    @Override
    public Value<T> getValue() {
        return value;
    }
}
