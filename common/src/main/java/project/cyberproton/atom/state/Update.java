package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public class Update<T> {
    private final TypedKey<T> key;
    private final Value<T> next;

    private Update(@NotNull TypedKey<T> key, @NotNull Value<T> next) {
        Objects.requireNonNull(key, "Key must not be null");
        Objects.requireNonNull(next, "next");
        this.key = key;
        this.next = next;
    }

    @NotNull
    public TypedKey<T> getKey() {
        return key;
    }

    @NotNull
    public Value<T> getNext() {
        return next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Update<?> update = (Update<?>) o;
        return key.equals(update.key) && next.equals(update.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, next);
    }

    @Override
    public String toString() {
        return "Update{" +
               "key=" + key +
               ", next=" + next +
               '}';
    }

    public static <T> Update<T> of(@NotNull TypedKey<T> key, @NotNull Value<T> next) {
        return new Update<>(key, next);
    }

    public static <T> Update<T> of(@NotNull TypedKey<T> key, @Nullable T next) {
        return new Update<>(key, Value.of(next));
    }
}
