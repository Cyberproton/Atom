package project.cyberproton.atom.bukkit.stat;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.bukkit.event.entity.EntityStatModifierAddEvent;
import project.cyberproton.atom.bukkit.event.entity.EntityStatRemoveModifierEvent;
import project.cyberproton.atom.entity.IEntity;
import project.cyberproton.atom.stat.AtomStatContainer;
import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.modifier.Modifier;

public class BukkitAtomStatContainer<S extends Stat<M, V>, M extends Modifier<V>, V> extends AtomStatContainer<S, M, V> {
    public BukkitAtomStatContainer(@NotNull IEntity owner, @NotNull S stat) {
        super(owner, stat);
    }

    @Override
    public boolean callStatAddEvent(@NotNull M modifier) {
        EntityStatModifierAddEvent event = new EntityStatModifierAddEvent(getOwner(), getStat(), modifier);
        return !event.isCancelled();
    }

    @Override
    public boolean callStatRemoveEvent(@NotNull M modifier) {
        return !new EntityStatRemoveModifierEvent(getOwner(), getStat(), modifier).isCancelled();
    }
}
