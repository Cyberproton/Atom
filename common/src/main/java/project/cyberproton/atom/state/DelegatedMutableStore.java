package project.cyberproton.atom.state;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.promise.Promise;
import project.cyberproton.atom.state.listener.BoundStateListener;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class DelegatedMutableStore implements MutableStore {
    private final MutableStore delegated;

    public DelegatedMutableStore(@NotNull MutableStore delegated) {
        Objects.requireNonNull(delegated, "delegated");
        this.delegated = delegated;
    }

    @Override
    public void update(Collection<Update<?>> updates) {
        delegated.update(updates);
    }

    @Override
    public void updateSync(@NotNull Collection<Update<?>> updates) {
        delegated.updateSync(updates);
    }

    @Override
    public Promise<Void> updateAsync(@NotNull Collection<Update<?>> updates) {
        return delegated.updateAsync(updates);
    }

    @Override
    @NotNull
    public <T> State<T> stateOf(@NotNull TypeToken<T> type, @Nullable T initialState) {
        return delegated.stateOf(type, initialState);
    }

    @Override
    @NotNull
    public <T> State<T> stateOf(TypedKey<T> key, @Nullable T initialState) {
        return delegated.stateOf(key, initialState);
    }

    @Override
    @NotNull
    public BoundStateListener subscribe(@NotNull StateListener listener) {
        return delegated.subscribe(listener);
    }

    @Override
    public void unsubscribe(@NotNull StateListener listener) {
        delegated.unsubscribe(listener);
    }

    @Override
    public void unsubscribe(@NotNull UUID listenerUUID) {
        delegated.unsubscribe(listenerUUID);
    }

    @Override
    public <T> boolean has(@NotNull TypedKey<T> key) {
        return delegated.has(key);
    }

    @Override
    @NotNull
    public <T> Value<T> get(@NotNull TypedKey<T> key) {
        return delegated.get(key);
    }

    @Override
    @Nullable
    public <T> T getUnboxedOrNull(@NotNull TypedKey<T> key) {
        return delegated.getUnboxedOrNull(key);
    }

    @Override
    @NotNull
    public Set<TypedKey<?>> getKeys() {
        return delegated.getKeys();
    }

    @Override
    @NotNull
    public Collection<?> getValues() {
        return delegated.getValues();
    }
}
