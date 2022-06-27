package project.cyberproton.atom.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.entity.player.IPlayer;

import java.util.UUID;

public class Players {
    @Nullable
    public static IPlayer getPlayer(@NotNull UUID uuid) {
        return Atom.getInstance().getEntityManager().getPlayer(uuid);
    }

    @Nullable
    public static IPlayer getPlayer(@NotNull String name) {
        return Atom.getInstance().getEntityManager().getPlayer(name);
    }
}
