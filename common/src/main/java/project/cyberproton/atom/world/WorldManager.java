package project.cyberproton.atom.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface WorldManager {

    @NotNull
    List<World> getWorlds();

    @Nullable
    World getWorld(@NotNull String name);

    World getWorld(@NotNull UUID uuid);

    void linkWorlds(@NotNull Collection<World> worlds);

}
