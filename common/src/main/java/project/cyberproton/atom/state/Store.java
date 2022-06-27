package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.function.Supplier;

public interface Store {

    static Store empty() {
        return new ImmutableStore(StoreConfiguration.empty());
    }

    static ImmutableStore immutable(@NotNull StoreConfiguration configuration) {
        return new ImmutableStore(configuration);
    }

    static ExpandableStore expandable() {
        return new DefaultExpandableStore();
    }

    static ExpandableStore expandable(List<KeyValue<?>> keyValues) {
        return new DefaultExpandableStore(StoreConfiguration.empty());
    }

    static ExpandableStore expandable(KeyValue<?>... keyValues) {
        return expandable(Arrays.asList(keyValues));
    }

    static MutableStore nonExpandable(List<KeyValue<?>> keyValues) {
        return new DefaultNonExpandableStore(StoreConfiguration.empty());
    }

    static MutableStore nonExpandable(KeyValue<?>... keyValues) {
        return nonExpandable(Arrays.asList(keyValues));
    }

    <T> boolean has(@NotNull TypedKey<T> key);

    @NotNull
    <T> Value<T> get(@NotNull TypedKey<T> key);

    @Nullable
    <T> T getUnboxedOrNull(@NotNull TypedKey<T> key);

    @NotNull
    default <T> T getUnboxed(@NotNull TypedKey<T> key) {
        T value = getUnboxedOrNull(key);
        if (value == null) {
            throw new NoSuchElementException("No value was found for key " + key);
        }
        return value;
    }

    @NotNull
    default <T> T getUnboxedOrElse(@NotNull TypedKey<@NotNull T> key, @NotNull T def) {
        Objects.requireNonNull(def, "def");
        T value = getUnboxedOrNull(key);
        if (value == null) {
            return def;
        }
        return value;
    }

    @NotNull
    default <T> T getUnboxedOrSupply(@NotNull TypedKey<@NotNull T> key, @NotNull Supplier<@NotNull T> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        T value = getUnboxedOrNull(key);
        if (value == null) {
            return Objects.requireNonNull(supplier.get());
        }
        return value;
    }

    @NotNull
    Set<TypedKey<?>> getKeys();

    @NotNull
    Collection<?> getValues();

}
