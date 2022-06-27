package project.cyberproton.atom.state.listener;

import project.cyberproton.atom.state.Change;
import project.cyberproton.atom.state.MutableStore;
import project.cyberproton.atom.state.TypedKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class DefaultConcreteStateListener<T> implements ConcreteStateListener<T> {
    private final UUID uniqueId;
    private final TypedKey<T> key;
    private final Consumer<Map<TypedKey<?>, Change<?>>> handler;

    protected DefaultConcreteStateListener(@NotNull UUID uniqueId, @NotNull TypedKey<T> key, @Nullable Consumer<Map<TypedKey<?>, Change<?>>> handler) {
        Objects.requireNonNull(uniqueId, "uniqueId");
        Objects.requireNonNull(key, "key");
        this.uniqueId = uniqueId;
        this.key = key;
        this.handler = handler;
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @NotNull
    @Override
    public TypedKey<T> getKey() {
        return key;
    }

    @Override
    public void onStateChange(@NotNull Map<TypedKey<?>, Change<?>> changes) {
        if (handler == null) {
            return;
        }
        handler.accept(changes);
    }

    public static <T> Builder<T> builder(TypedKey<T> key) {
        return new Builder<>(key);
    }


    public static final class Builder<T> implements ConcreteStateListener.Builder<T> {
        private final TypedKey<T> key;
        private Consumer<Map<TypedKey<?>, Change<?>>> handler;

        private Builder(TypedKey<T> key) {
            this.key = key;
        }

        @NotNull
        @Override
        public Builder<T> handler(Consumer<Map<TypedKey<?>, Change<?>>> handler) {
            this.handler = handler;
            return this;
        }

        @NotNull
        @Override
        public DefaultConcreteStateListener<T> build() {
            return new DefaultConcreteStateListener<T>(UUID.randomUUID(), key, handler);
        }
    }
}
