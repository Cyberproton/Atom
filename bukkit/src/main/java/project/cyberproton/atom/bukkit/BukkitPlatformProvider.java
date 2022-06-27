package project.cyberproton.atom.bukkit;

import org.bukkit.Bukkit;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.Platform;
import project.cyberproton.atom.PlatformProvider;
import project.cyberproton.atom.Version;
import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;

public class BukkitPlatformProvider implements PlatformProvider {
    private final BukkitAtom atom;
    private final BukkitServerVersion serverVersion = new BukkitServerVersion();

    public BukkitPlatformProvider(BukkitAtom atom) {
        this.atom = atom;
    }

    @NotNull
    @Override
    public Platform.Type getType() {
        return Platform.Type.BUKKIT;
    }

    @Override
    public boolean isOnMainThread() {
        return Bukkit.isPrimaryThread();
    }

    @Override
    public long getCurrentTick() {
        return Bukkit.getCurrentTick();
    }

    @Override
    public boolean isStopping() {
        return Bukkit.isStopping();
    }

    @NotNull
    @Override
    public Version getServerVersion() {
        return serverVersion;
    }

    @NotNull
    @Override
    public AtomPlugin getBootstrap() {
        return atom;
    }

    @NotNull
    @Override
    public Atom getAtom() {
        return atom;
    }
}
