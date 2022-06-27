package project.cyberproton.atom.bukkit.world;

import project.cyberproton.atom.Proxy;
import project.cyberproton.atom.world.WorldManager;
import project.cyberproton.atom.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.UUID;

public class BukkitWorld implements World, Proxy<org.bukkit.World> {
    private final WorldManager worldManager;
    private final WeakReference<org.bukkit.World> wrapped;

    public BukkitWorld(@NotNull WorldManager worldManager, @NotNull org.bukkit.World wrapped) {
        Objects.requireNonNull(worldManager, "universe");
        Objects.requireNonNull(wrapped, "wrapped");
        this.worldManager = worldManager;
        this.wrapped = new WeakReference<>(wrapped);
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return unwrap().getUID();
    }

    @NotNull
    @Override
    public WorldManager getWorldManager() {
        return worldManager;
    }

    @NotNull
    @Override
    public String getName() {
        return unwrap().getName();
    }

    @Nullable
    @Override
    public org.bukkit.World unwrapOrNull() {
        return null;
    }
}
