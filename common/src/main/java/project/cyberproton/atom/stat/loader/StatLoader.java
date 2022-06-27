package project.cyberproton.atom.stat.loader;

import org.spongepowered.configurate.ConfigurationNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StatLoader<V> {
    @Nullable
    V load(@NotNull ConfigurationNode node);
}
