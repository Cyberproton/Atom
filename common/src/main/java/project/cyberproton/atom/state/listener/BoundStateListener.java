package project.cyberproton.atom.state.listener;

import project.cyberproton.atom.state.MutableStore;
import project.cyberproton.atom.terminable.Terminable;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public interface BoundStateListener extends Terminable {
    @NotNull
    static BoundStateListener bind(@NotNull StateListener listener, @NotNull Collection<MutableStore> stores) {
        return new DefaultBoundStateListener(listener, stores);
    }

    @NotNull
    static BoundStateListener bind(@NotNull StateListener listener, @NotNull MutableStore store, @NotNull MutableStore... stores) {
        List<MutableStore> lst = new ArrayList<>();
        lst.add(store);
        lst.addAll(Arrays.asList(stores));
        return bind(listener, lst);
    }

    @NotNull
    static BoundStateListener bindAndSubscribe(@NotNull StateListener listener, @NotNull Collection<MutableStore> stores) {
        DefaultBoundStateListener bounded = new DefaultBoundStateListener(listener, stores);
        bounded.subscribe();
        return bounded;
    }

    @NotNull
    static BoundStateListener bindAndSubscribe(@NotNull StateListener listener, @NotNull MutableStore store, @NotNull MutableStore... stores) {
        List<MutableStore> lst = new ArrayList<>();
        lst.add(store);
        lst.addAll(Arrays.asList(stores));
        return bindAndSubscribe(listener, lst);
    }

    @NotNull
    Collection<MutableStore> boundStores();

    @NotNull
    StateListener boundListener();

    default void subscribe() {
        for (MutableStore boundStore : boundStores()) {
            boundStore.subscribe(boundListener());
        }
    }

    default void unsubscribe() {
        for (MutableStore boundStore : boundStores()) {
            boundStore.unsubscribe(boundListener());
        }
    }

    @Override
    default void close() {
        unsubscribe();
    }
}
