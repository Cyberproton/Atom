package project.cyberproton.atom.stat;

import project.cyberproton.atom.modifier.Modifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.modifier.TemporaryModifier;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

public interface StatContainer<S extends Stat<M, V>, M extends Modifier<V>, V> {
    @NotNull
    S getStat();

    @Nullable
    V getCombinedValue();

    @NotNull
    Collection<M> getModifiers();

    void addModifier(@NotNull M modifier);

    void addTemporaryModifier(@NotNull TemporaryModifier<M> temporaryModifier);

    void removeModifier(@NotNull UUID id);

    default void removeModifier(@NotNull M modifier) {
        removeModifier(modifier.getUniqueId());
    }

    void removeModifiersIf(@NotNull Predicate<M> predicate);

    void clean();
}
