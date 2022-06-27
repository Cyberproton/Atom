package project.cyberproton.atom.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;

public interface Loader<T> {
    @Nullable
    T load(@NotNull ConfigurationNode node);
}
