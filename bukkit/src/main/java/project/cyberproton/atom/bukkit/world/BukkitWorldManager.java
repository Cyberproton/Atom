package project.cyberproton.atom.bukkit.world;

import org.bukkit.Bukkit;
import project.cyberproton.atom.bukkit.entity.BukkitEntityManager;
import project.cyberproton.atom.bukkit.plugin.BukkitPlugin;
import project.cyberproton.atom.entity.player.IPlayerRegistry;
import project.cyberproton.atom.world.AbstractWorldManager;
import project.cyberproton.atom.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitWorldManager extends AbstractWorldManager<BukkitPlugin> {
    public BukkitWorldManager(@NotNull BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
    }

    @NotNull
    @Override
    public List<World> getWorlds() {
        List<World> worlds = new ArrayList<>();
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            worlds.add(new BukkitWorld(this, world));
        }
        return worlds;
    }

    @Nullable
    @Override
    public World getWorld(@NotNull String name) {
        for (World world : getWorlds()) {
            if (world.getName().equals(name)) {
                return world;
            }
        }
        return null;
    }

    @Override
    public World getWorld(@NotNull UUID uuid) {
        org.bukkit.World bukkitWorld = Bukkit.getWorld(uuid);
        if (bukkitWorld == null) return null;
        return new BukkitWorld(this, bukkitWorld);
    }
}
