package project.cyberproton.atom.world;

import project.cyberproton.atom.Identifiable;

import org.jetbrains.annotations.NotNull;

public interface World extends Identifiable {
    @NotNull
    WorldManager getWorldManager();

    @NotNull
    String getName();
}
