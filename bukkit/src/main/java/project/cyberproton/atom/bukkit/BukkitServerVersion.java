package project.cyberproton.atom.bukkit;

import org.bukkit.Bukkit;
import project.cyberproton.atom.Version;

public class BukkitServerVersion implements Version {
    @Override
    public String getRaw() {
        return Bukkit.getMinecraftVersion();
    }

    @Override
    public int getMajor() {
        return Integer.parseInt(getRaw().split("\\.")[0]);
    }

    @Override
    public int getMinor() {
        return Integer.parseInt(getRaw().split("\\.")[1]);
    }
}
