package project.cyberproton.atom.stat.combiner;

import project.cyberproton.atom.modifier.combiner.ModifierCombiner;
import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.modifier.Modifier;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public abstract class StatCombiner<M extends Modifier<V>, V> implements ModifierCombiner<M, V> {
    private final Stat<M, V> stat;

    public StatCombiner(@NotNull Stat<M, V> stat) {
        this.stat = stat;
    }

    @NotNull
    public Stat<M, V> getStat() {
        return stat;
    }
}
