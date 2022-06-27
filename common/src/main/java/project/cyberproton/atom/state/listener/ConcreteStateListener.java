package project.cyberproton.atom.state.listener;

import project.cyberproton.atom.state.Change;
import project.cyberproton.atom.state.MutableStore;
import project.cyberproton.atom.state.TypedKey;

import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public interface ConcreteStateListener<T> extends StateListener {
    @NotNull
    TypedKey<T> getKey();

    interface Builder<T> {
        @NotNull
        ConcreteStateListener.Builder<T> handler(Consumer<Map<TypedKey<?>, Change<?>>> handler);

        @NotNull
        ConcreteStateListener<T> build();
    }
}
