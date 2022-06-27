package project.cyberproton.atom.stat;

import project.cyberproton.atom.modifier.Modifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public interface CompositeStat {
    @NotNull
    static CompositeStat of(@NotNull List<StatInstance<?, ?, ?>> instances) {
        Objects.requireNonNull(instances, "instances");
        return new DefaultCompositeStat(Collections.unmodifiableList(instances));
    }

    @NotNull
    static CompositeStat of(@NotNull StatInstance<?, ?, ?>... instances) {
        return of(Arrays.asList(instances));
    }

    @NotNull
    static CompositeStat empty() {
        return of(new ArrayList<>());
    }

    @NotNull
    List<StatInstance<?, ?, ?>> all();

    @Nullable
    <S extends Stat<M, V>, M extends Modifier<V>, V> List<StatInstance<S, M, V>> instanceOf(Stat<M, V> stat);
}
