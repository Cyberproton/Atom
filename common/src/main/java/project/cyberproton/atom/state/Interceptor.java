package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Interceptor<T> {
    private final TypedKey<T> key;
    private final Function<SetContext<T>, Value<T>> onSet;
    private final Consumer<SetContext<T>> onBeforeSet;
    private final Consumer<SetContext<T>> onAfterSet;
    private final Supplier<Value<T>> onGet;
    private final Runnable onBeforeGet;
    private final Consumer<Value<T>> onAfterGet;
    private final boolean allowNullValue;

    public Interceptor(@NotNull TypedKey<T> key, @Nullable Function<SetContext<T>, Value<T>> onSet, @Nullable Consumer<SetContext<T>> onBeforeSet, @Nullable Consumer<SetContext<T>> onAfterSet, Supplier<Value<T>> onGet, @Nullable Runnable onBeforeGet, @Nullable Consumer<Value<T>> onAfterGet, boolean allowNullValue) {
        this.key = key;
        this.onSet = onSet;
        this.onBeforeSet = onBeforeSet;
        this.onAfterSet = onAfterSet;
        this.onGet = onGet;
        this.onBeforeGet = onBeforeGet;
        this.onAfterGet = onAfterGet;
        this.allowNullValue = allowNullValue;
    }

    @NotNull
    public TypedKey<T> getKey() {
        return key;
    }

    @Nullable
    public Function<SetContext<T>, Value<T>> getOnSet() {
        return onSet;
    }

    @Nullable
    public Consumer<SetContext<T>> getOnBeforeSet() {
        return onBeforeSet;
    }

    @Nullable
    public Consumer<SetContext<T>> getOnAfterSet() {
        return onAfterSet;
    }

    @Nullable
    public Supplier<Value<T>> getOnGet() {
        return onGet;
    }

    @Nullable
    public Runnable getOnBeforeGet() {
        return onBeforeGet;
    }

    @Nullable
    public Consumer<Value<T>> getOnAfterGet() {
        return onAfterGet;
    }

    public boolean allowNullValue() {
        return allowNullValue;
    }

    @Override
    public String toString() {
        return "Interceptor{" +
               "key=" + key +
               ", onSet=" + onSet +
               ", onBeforeSet=" + onBeforeSet +
               ", onAfterSet=" + onAfterSet +
               ", onGet=" + onGet +
               ", onBeforeGet=" + onBeforeGet +
               ", onAfterGet=" + onAfterGet +
               ", allowNullValue=" + allowNullValue +
               '}';
    }

    public static <T> Builder<T> builder(TypedKey<T> key) {
        return Builder.of(key);
    }

    public static final class SetContext<T> {
        private final TypedKey<T> key;
        private final Value<T> previous;
        private final Value<T> next;
        private boolean shouldNotifyListener = true;
        private boolean reflectChangesOnStore = true;

        public SetContext(@NotNull TypedKey<T> key, @NotNull Value<T> previous, @NotNull Value<T> next) {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(previous, "previous");
            Objects.requireNonNull(next, "next");
            this.key = key;
            this.previous = previous;
            this.next = next;
        }

        @NotNull
        public TypedKey<T> key() {
            return key;
        }

        @NotNull
        public Value<T> previous() {
            return previous;
        }

        @NotNull
        public Value<T> next() {
            return next;
        }

        public boolean shouldNotifyListener() {
            return shouldNotifyListener;
        }

        public void shouldNotifyListener(boolean shouldNotifyListener) {
            this.shouldNotifyListener = shouldNotifyListener;
        }

        public boolean reflectChangesOnStore() {
            return reflectChangesOnStore;
        }

        public void reflectChangesOnStore(boolean reflectChangesOnStore) {
            this.reflectChangesOnStore = reflectChangesOnStore;
        }
    }

    public static final class Builder<T> {
        private final TypedKey<T> key;
        private Function<SetContext<T>, Value<T>> onSet;
        private Consumer<SetContext<T>> onBeforeSet;
        private Consumer<SetContext<T>> onAfterSet;
        private Supplier<Value<T>> onGet;
        private Runnable onBeforeGet;
        private Consumer<Value<T>> onAfterGet;
        private boolean allowNullValue;

        private Builder(@NotNull TypedKey<T> key) {
            Objects.requireNonNull(key, "key");
            this.key = key;
        }

        public static <T> Builder<T> of(@NotNull TypedKey<T> key) {
            return new Builder<>(key);
        }

        public Builder<T> onSet(Function<SetContext<T>, Value<T>> onSet) {
            this.onSet = onSet;
            return this;
        }

        public Builder<T> onBeforeSet(Consumer<SetContext<T>> onBeforeSet) {
            this.onBeforeSet = onBeforeSet;
            return this;
        }

        public Builder<T> onAfterSet(Consumer<SetContext<T>> onAfterSet) {
            this.onAfterSet = onAfterSet;
            return this;
        }

        public Builder<T> onGet(Supplier<Value<T>> onGet) {
            this.onGet = onGet;
            return this;
        }

        public Builder<T> onBeforeGet(Runnable onBeforeGet) {
            this.onBeforeGet = onBeforeGet;
            return this;
        }

        public Builder<T> onAfterGet(Consumer<Value<T>> onAfterGet) {
            this.onAfterGet = onAfterGet;
            return this;
        }

        public Builder<T> allowNullValue(boolean allowNullValue) {
            this.allowNullValue = allowNullValue;
            return this;
        }

        public Interceptor<T> build() {
            return new Interceptor<>(key, onSet, onBeforeSet, onAfterSet, onGet, onBeforeGet, onAfterGet, allowNullValue);
        }
    }
}