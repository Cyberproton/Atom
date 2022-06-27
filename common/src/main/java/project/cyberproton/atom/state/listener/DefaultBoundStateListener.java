package project.cyberproton.atom.state.listener;

import project.cyberproton.atom.state.MutableStore;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class DefaultBoundStateListener implements BoundStateListener {
    private final Collection<MutableStore> boundStores;
    private final StateListener boundListener;

    DefaultBoundStateListener(StateListener boundListener, Collection<MutableStore> stores) {
        Objects.requireNonNull(boundListener, "boundListener");
        Objects.requireNonNull(boundListener, "boundListener");
        for (MutableStore store : stores) {
            Objects.requireNonNull(store, "Stores contains null");
        }
        this.boundStores = Collections.unmodifiableCollection(stores);
        this.boundListener = boundListener;
    }

    @NotNull
    @Override
    public Collection<MutableStore> boundStores() {
        return boundStores;
    }

    @NotNull
    @Override
    public StateListener boundListener() {
        return boundListener;
    }
}
