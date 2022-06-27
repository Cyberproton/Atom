package project.cyberproton.atom.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.stat.StatContainer;
import project.cyberproton.atom.modifier.Modifier;

import java.util.Collection;

public interface IStatModule<T extends IEntity> extends IEntityModule<T> {
    @NotNull
    Collection<StatContainer<?, ?, ?>> getContainers();

    @NotNull
    <S extends Stat<M, V>, M extends Modifier<V>, V> StatContainer<S, M, V> getContainer(@NotNull S stat);

    @Nullable
    <S extends Stat<M, V>, M extends Modifier<V>, V> V getValue(@NotNull S stat);
}
