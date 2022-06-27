package project.cyberproton.atom.state;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.promise.Promise;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class DefaultState<T> implements State<T> {
    private final TypedKey<T> key;
    private final MutableStore store;

    public DefaultState(TypeToken<T> type, MutableStore store) {
        this.key = TypedKey.of(Key.of("user-defined", UUID.randomUUID().toString()), type);
        this.store = store;
    }

    public DefaultState(TypedKey<T> key, MutableStore store) {
        this.key = key;
        this.store = store;
    }

    @NotNull
    @Override
    public TypedKey<T> getKey() {
        return key;
    }

    @Override
    @Nullable
    public T getUnboxedOrNull() {
        return store.getUnboxedOrNull(key);
    }

    @NotNull
    @Override
    public Value<T> get() {
        return store.get(key);
    }

    @Override
    public void updateSync(@NotNull Value<T> value) {
        Objects.requireNonNull(value, "value");
        store.updateSync(key, value);
    }

    @Override
    public Promise<Void> updateAsync(@NotNull Value<T> value) {
        Objects.requireNonNull(value, "value");
        return store.updateAsync(key, value);
    }

    @Override
    public void subscribe(@NotNull StateListener listener) {
        store.subscribe(listener);
    }

    @Override
    public void unsubscribe(@NotNull StateListener listener) {
        store.unsubscribe(listener);
    }
}
