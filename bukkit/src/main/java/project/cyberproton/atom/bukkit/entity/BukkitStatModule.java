package project.cyberproton.atom.bukkit.entity;

import project.cyberproton.atom.bukkit.stat.BukkitAtomStatContainer;
import project.cyberproton.atom.entity.AbstractStatModule;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.stat.*;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.bukkit.stat.BukkitVanillaStatContainer;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.Set;

public class BukkitStatModule extends AbstractStatModule<BukkitEntity<?>> {
    public BukkitStatModule(@NotNull AtomPlugin plugin, @NotNull BukkitEntity<?> owner, @NotNull Set<Option> options) {
        super(plugin, owner, options);
        // Initialize vanilla stats
        for (VanillaStat vanilla : Stats.vanillas()) {
            getContainer(vanilla);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends Stat<M, V>, M extends Modifier<V>, V> @NotNull StatContainer<S, M, V> getContainer(@NotNull S stat) {
        Objects.requireNonNull(stat, "stat");
        if (stat instanceof VanillaStat) {
            return (StatContainer<S, M, V>) containers.computeIfAbsent(stat, s -> new BukkitVanillaStatContainer(getOwner(), ((VanillaStat) stat)));
        } else {
            return (StatContainer<S, M, V>) containers.computeIfAbsent(stat, s -> new BukkitAtomStatContainer<>(getOwner(), stat));
        }
    }
}
