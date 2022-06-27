package project.cyberproton.atom.state;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.promise.Promise;
import project.cyberproton.atom.state.listener.BoundStateListener;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public interface MutableStore extends Store {
    void update(Collection<Update<?>> updates);

    default void update(@NotNull Update<?>... updates) {
        List<Update<?>> updateList = new ArrayList<>();
        for (Update<?> update : updates) {
            Objects.requireNonNull(update, "update");
            updateList.add(update);
        }
        updateSync(updateList);
    }

    default <T> void update(@NotNull TypedKey<T> key, @NotNull Value<T> value) {
        updateSync(Update.of(key, value));
    }

    default <T> void update(@NotNull TypedKey<T> key, @Nullable T value) {
        updateSync(key, Value.of(value));
    }

    void updateSync(@NotNull Collection<Update<?>> updates);

    default void updateSync(@NotNull Update<?>... updates) {
        List<Update<?>> updateList = new ArrayList<>();
        for (Update<?> update : updates) {
            Objects.requireNonNull(update, "update");
            updateList.add(update);
        }
        updateSync(updateList);
    }

    default <T> void updateSync(@NotNull TypedKey<T> key, @NotNull Value<T> value) {
        updateSync(Update.of(key, value));
    }

    default <T> void updateSync(@NotNull TypedKey<T> key, @Nullable T value) {
        updateSync(key, Value.of(value));
    }

    Promise<Void> updateAsync(@NotNull Collection<Update<?>> updates);

    default Promise<Void> updateAsync(@NotNull Update<?>... updates) {
        List<Update<?>> updateList = new ArrayList<>();
        for (Update<?> update : updates) {
            Objects.requireNonNull(update, "update");
            updateList.add(update);
        }
        return updateAsync(updateList);
    }

    default <T> Promise<Void> updateAsync(@NotNull TypedKey<T> key, @NotNull Value<T> value) {
        return updateAsync(Update.of(key, value));
    }

    default <T> Promise<Void> updateAsync(@NotNull TypedKey<T> key, @Nullable T value) {
        return updateAsync(key, Value.of(value));
    }

    @NotNull
    BoundStateListener subscribe(@NotNull StateListener listener);

    void unsubscribe(@NotNull StateListener listener);

    void unsubscribe(@NotNull UUID listenerUUID);

    @NotNull
    <T> State<T> stateOf(@NotNull TypeToken<T> type, @Nullable T initialState);

    @NotNull
    default <T> State<T> stateOf(@NotNull TypeToken<T> type) {
        return stateOf(type, null);
    }

    @NotNull
    default <T> State<T> stateOf(@NotNull Class<T> type, @Nullable T initialState) {
        return stateOf(TypeToken.of(type), initialState);
    }

    @NotNull
    default <T> State<T> stateOf(@NotNull Class<T> type) {
        return stateOf(type, null);
    }

    @NotNull
    <T> State<T> stateOf(TypedKey<T> key, @Nullable T initialState);
}
