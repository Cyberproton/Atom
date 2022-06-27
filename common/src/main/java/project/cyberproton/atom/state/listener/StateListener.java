package project.cyberproton.atom.state.listener;

import project.cyberproton.atom.Identifiable;
import project.cyberproton.atom.state.Change;
import project.cyberproton.atom.state.TypedKey;
import project.cyberproton.atom.terminable.Terminable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public interface StateListener extends Identifiable {

    static <V> ConcreteStateListener<V> single(@NotNull TypedKey<V> key, @Nullable Consumer<Map<TypedKey<?>, Change<?>>> handler) {
        return new DefaultConcreteStateListener<>(UUID.randomUUID(), key, handler);
    }

    static MultipleStateListener multiple(@NotNull Set<TypedKey<?>> keys, @Nullable Consumer<Map<TypedKey<?>, Change<?>>> handler) {
        return new MultipleStateListener(UUID.randomUUID(), keys, handler);
    }

    static AllStateListener all(@Nullable Consumer<Map<TypedKey<?>, Change<?>>> handler) {
        return new AllStateListener(UUID.randomUUID(), handler);
    }

    void onStateChange(@NotNull Map<TypedKey<?>, Change<?>> changes);

}
