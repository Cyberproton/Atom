package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class Change<T> {
    private final TypedKey<T> key;
    private final Value<T> previous;
    private final Value<T> next;

    private Change(@NotNull TypedKey<T> key, @NotNull Value<T> previous, @NotNull Value<T> next) {
        Objects.requireNonNull(key, "Key must not be null");
        Objects.requireNonNull(previous, "current");
        Objects.requireNonNull(next, "next");
        this.key = key;
        this.previous = previous;
        this.next = next;
    }

    @NotNull
    public TypedKey<T> getKey() {
        return key;
    }

    @NotNull
    public Value<T> getPrevious() {
        return previous;
    }

    @NotNull
    public Value<T> getNext() {
        return next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Change<?> change = (Change<?>) o;
        return key.equals(change.key) && previous.equals(change.previous) && next.equals(change.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, previous, next);
    }

    public static <T> Change<T> of(@NotNull TypedKey<T> key, @NotNull Value<T> current, @NotNull Value<T> next) {
        return new Change<>(key, current, next);
    }

    public static <T> Change<T> of(@NotNull TypedKey<T> key, @NotNull T current, @NotNull T next) {
        return new Change<>(key, Value.of(current), Value.of(next));
    }
}
