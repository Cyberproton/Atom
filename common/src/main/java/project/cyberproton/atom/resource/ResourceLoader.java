package project.cyberproton.atom.resource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;

public interface ResourceLoader<R extends Resource> {
    @Nullable
    R load(@NotNull ConfigurationNode node);
}
