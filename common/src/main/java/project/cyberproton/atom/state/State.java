package project.cyberproton.atom.state;

import project.cyberproton.atom.promise.Promise;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public interface State<T> {
    @NotNull
    TypedKey<T> getKey();

    @Nullable
    T getUnboxedOrNull();

    @NotNull
    default T getUnboxed() {
        T value = getUnboxedOrNull();
        if (value == null) {
            throw new IllegalStateException("State is null");
        }
        return value;
    }

    @NotNull
    Value<T> get();

    @NotNull
    default T getUnboxedOrElse(@NotNull T defaultValue) {
        Objects.requireNonNull(defaultValue, "defaultValue");
        T state = getUnboxedOrNull();
        if (state == null) return defaultValue;
        return state;
    }

    void updateSync(@NotNull Value<T> state);

    default void updateSync(@Nullable T state) {
        updateSync(Value.of(state));
    }

    Promise<Void> updateAsync(@NotNull Value<T> state);

    default Promise<Void> updateAsync(@Nullable T state) {
        return updateAsync(Value.of(state));
    }

    void subscribe(@NotNull StateListener listener);

    void unsubscribe(@NotNull StateListener listener);
}
