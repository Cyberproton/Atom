package project.cyberproton.atom.state.listener;

import project.cyberproton.atom.state.Change;
import project.cyberproton.atom.state.TypedKey;

import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class MultipleStateListener implements StateListener {
    private final Set<TypedKey<?>> keys;
    private final UUID uniqueId;
    private final Consumer<Map<TypedKey<?>, Change<?>>> handler;

    MultipleStateListener(UUID uniqueId, Set<TypedKey<?>> keys, Consumer<Map<TypedKey<?>, Change<?>>> handler) {
        this.keys = keys;
        this.uniqueId = uniqueId;
        this.handler = handler;
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    public Set<TypedKey<?>> getKeys() {
        return keys;
    }

    @Override
    public void onStateChange(@NotNull Map<TypedKey<?>, Change<?>> changes) {
        if (handler == null) {
            return;
        }
        handler.accept(changes);
    }
}
