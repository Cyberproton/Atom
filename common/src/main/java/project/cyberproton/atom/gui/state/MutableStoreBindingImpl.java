package project.cyberproton.atom.gui.state;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.gui.Gui;
import project.cyberproton.atom.state.MutableStore;
import project.cyberproton.atom.state.State;
import project.cyberproton.atom.state.TypedKey;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashSet;
import java.util.Set;

public class MutableStoreBindingImpl implements MutableStoreBinding {
    private final Gui<?, ?> boundGui;
    private final MutableStore boundStore;
    private StateListener listener;
    private final Set<TypedKey<?>> keys = new HashSet<>();
    private boolean isBound = true;

    public MutableStoreBindingImpl(Gui<?, ?> boundGui, MutableStore boundStore) {
        this.boundGui = boundGui;
        this.boundStore = boundStore;
    }

    @NotNull
    @Override
    public Gui<?, ?> boundGui() {
        return boundGui;
    }

    @NotNull
    @Override
    public MutableStore boundStore() {
        return boundStore;
    }

    @Override
    public void unbind() {
        if (listener == null || !isBound) {
            return;
        }
        boundStore().unsubscribe(listener);
        isBound = false;
    }

    private void unsubscribeListener() {
        if (listener == null || !isBound) {
            return;
        }
        boundStore().unsubscribe(listener);
        listener = null;
    }

    private void subscribeListener() {
        if (listener != null || !isBound) {
            return;
        }
        listener = StateListener.multiple(keys, listener::onStateChange);
        boundStore().subscribe(listener);
    }

    @NotNull
    @Override
    public <T> State<T> stateOf(@NotNull TypeToken<T> type, @Nullable T initialState) {
        if (!isBound) {
            throw new IllegalStateException("Binding has been unbound");
        }
        unsubscribeListener();
        State<T> state = boundStore.stateOf(type, initialState);
        keys.add(state.getKey());
        subscribeListener();
        return state;
    }

    @NotNull
    @Override
    public <T> State<T> stateOf(TypedKey<T> key, @Nullable T initialState) {
        if (!isBound) {
            throw new IllegalStateException("Binding has been unbound");
        }
        unsubscribeListener();
        State<T> state = boundStore.stateOf(key, initialState);
        keys.add(state.getKey());
        subscribeListener();
        return state;
    }
}
