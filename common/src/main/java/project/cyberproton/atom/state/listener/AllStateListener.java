package project.cyberproton.atom.state.listener;

import project.cyberproton.atom.state.Change;
import project.cyberproton.atom.state.TypedKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class AllStateListener implements StateListener {
    private final UUID uniqueId;
    private final Consumer<Map<TypedKey<?>, Change<?>>> handler;

    protected AllStateListener(@NotNull UUID uniqueId, @Nullable Consumer<Map<TypedKey<?>, Change<?>>> handler) {
        this.uniqueId = uniqueId;
        this.handler = handler;
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void onStateChange(@NotNull Map<TypedKey<?>, Change<?>> changes) {
        if (handler == null) {
            return;
        }
        handler.accept(changes);
    }
}
