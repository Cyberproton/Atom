package project.cyberproton.atom.stat.loader;

import org.spongepowered.configurate.ConfigurationNode;
import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.modifier.Modifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GenericStatLoader<M extends Modifier<V>, V> implements StatLoader<V> {
    private final Stat<M, V> stat;

    public GenericStatLoader(@NotNull Stat<M, V> stat) {
        this.stat = stat;
    }

    @Override
    @Nullable
    public abstract V load(@NotNull ConfigurationNode node);

    @NotNull
    public Stat<M, V> getStat() {
        return stat;
    }
}
