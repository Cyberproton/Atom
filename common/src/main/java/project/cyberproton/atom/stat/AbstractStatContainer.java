package project.cyberproton.atom.stat;

import project.cyberproton.atom.entity.IEntity;
import project.cyberproton.atom.modifier.Modifier;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractStatContainer<S extends Stat<M, V>, M extends Modifier<V>, V> implements StatContainer<S, M, V> {
    private final IEntity owner;
    private final S stat;

    public AbstractStatContainer(@NotNull IEntity owner, @NotNull S stat) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(stat, "stat");
        this.owner = owner;
        this.stat = stat;
    }

    @NotNull
    public IEntity getOwner() {
        return owner;
    }

    @NotNull
    @Override
    public S getStat() {
        return stat;
    }
}
