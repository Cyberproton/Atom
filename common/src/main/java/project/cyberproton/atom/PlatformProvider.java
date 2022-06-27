package project.cyberproton.atom;

import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;

public interface PlatformProvider {
    @NotNull
    Platform.Type getType();

    boolean isOnMainThread();

    long getCurrentTick();

    @NotNull
    Version getServerVersion();

    @NotNull
    AtomPlugin getBootstrap();

    @NotNull
    Atom getAtom();

    boolean isStopping();
}
