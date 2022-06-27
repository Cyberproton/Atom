package project.cyberproton.atom;

import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;

public class Platform {
    // This will be injected by Atom implementations
    private static PlatformProvider provider;

    private Platform() {}

    public static PlatformProvider getProvider() {
        if (provider == null) throw new IllegalStateException("Atom has not been initialized");
        return provider;
    }

    @NotNull
    public static Type getType() {
        return getProvider().getType();
    }

    public static boolean isOnMainThread() {
        return getProvider().isOnMainThread();
    }

    public static long getCurrentTick() {
        return getProvider().getCurrentTick();
    }

    @NotNull
    public static Version getServerVersion() {
        return getProvider().getServerVersion();
    }

    public static boolean isStopping() {
        return getProvider().isStopping();
    }

    @NotNull
    public static Atom getAtom() {
        return getProvider().getAtom();
    }

    @NotNull
    public static AtomPlugin getBootstrap() {
        return getProvider().getBootstrap();
    }

    public enum Type {
        BUKKIT,
    }

    public static class Injector {

    }
}
