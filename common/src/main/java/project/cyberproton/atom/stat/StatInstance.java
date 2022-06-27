package project.cyberproton.atom.stat;

import project.cyberproton.atom.modifier.Modifier;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class StatInstance<S extends Stat<M, V>, M extends Modifier<V>, V> {
    private final S stat;
    private final V value;

    public StatInstance(@NotNull S stat, @NotNull V value) {
        this.stat = stat;
        this.value = value;
    }

    @NotNull
    public S getStat() {
        return stat;
    }

    @NotNull
    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatInstance<?, ?, ?> that = (StatInstance<?, ?, ?>) o;
        return stat.equals(that.stat) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stat, value);
    }

    @Override
    public String toString() {
        return "StatInstance{" +
               "stat=" + stat.getId() +
               ", value=" + value +
               '}';
    }
}
