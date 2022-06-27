package project.cyberproton.atom.state;


import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.promise.Promise;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ReferenceStore implements MutableStore {
    protected final Map<TypedKey<?>, KeyListener<?>> listeners = new HashMap<>();
    protected final Map<TypedKey<?>, List<BiConsumer<TypedKey<?>, Value<?>>>> validators = new HashMap<>();
    private boolean isAcceptingConfiguration = true;

    protected ReferenceStore() {
        configure();
        isAcceptingConfiguration = false;
    }

    @Override
    public <T> boolean has(@NotNull TypedKey<T> key) {
        return listeners.containsKey(key);
    }

    @Nullable
    @Override
    public <T> T getUnboxedOrNull(@NotNull TypedKey<T> key) {
        KeyListener<T> keyListener = ((KeyListener<T>) listeners.get(key));
        if (keyListener == null) {
            return null;
        }
        return keyListener.onGet.apply(key).getOrNull();
    }

    @NotNull
    @Override
    public <T> Value<T> get(@NotNull TypedKey<T> key) {
        KeyListener<T> keyListener = ((KeyListener<T>) listeners.get(key));
        if (keyListener == null) {
            return Value.ofNull();
        }
        return Value.of(keyListener.onGet.apply(key).get());
    }

    @NotNull
    @Override
    public Set<TypedKey<?>> getKeys() {
        return null;
    }

    @NotNull
    @Override
    public Collection<?> getValues() {
        List<Object> res = new ArrayList<>();
        for (KeyListener<?> value : listeners.values()) {
            Object v = value.onGet.apply(value.key);
            res.add(v);
        }
        return res;
    }

    @Override
    public void update(Collection<Update<?>> updates) {

    }

    @Override
    public void updateSync(@NotNull Collection<Update<?>> updates) {
        for (Update<?> update : updates) {

        }
    }

    @Override
    public Promise<Void> updateAsync(@NotNull Collection<Update<?>> updates) {
        return null;
    }

    @Override
    public void unsubscribe(@NotNull StateListener listener) {

    }

    protected abstract void configure();

    protected final <T> KeyListener<T> listen(TypedKey<T> key) {
        KeyListener<T> listener = new KeyListener<>(key);
        listeners.put(key, listener);
        return listener;
    }

    protected void noSupport() {
        throw new UnsupportedOperationException("This operation is not support");
    }

    protected static class KeyListener<T> {
        private final TypedKey<T> key;
        private Consumer<Value<T>> onSet;
        private List<Function<Value<T>, Object>> setValidators;
        private Function<TypedKey<?>, Value<T>> onGet;
        private boolean allowNullValue;

        private KeyListener(TypedKey<T> key, Consumer<Value<T>> onSet, List<Function<Value<T>, Object>> setValidators, Function<TypedKey<?>, Value<T>> onGet) {
            Objects.requireNonNull(key, "key");
            this.key = key;
            this.onSet = onSet;
            this.setValidators = setValidators;
            this.onGet = onGet;
        }

        private KeyListener(@NotNull TypedKey<T> key) {
            this(key, null, null, null);
        }

        public KeyListener<T> onSet(@Nullable Consumer<Value<T>> onSet) {
            this.onSet = onSet;
            return this;
        }

        public KeyListener<T> setValidators(@Nullable List<Function<Value<T>, Object>> setValidators) {
            this.setValidators = setValidators;
            return this;
        }

        public KeyListener<T> onGet(@Nullable Function<TypedKey<?>, Value<T>> onGet) {
            this.onGet = onGet;
            return this;
        }

        public KeyListener<T> allowNullValue(boolean allowNullValue) {
            this.allowNullValue = allowNullValue;
            return this;
        }
    }
}
