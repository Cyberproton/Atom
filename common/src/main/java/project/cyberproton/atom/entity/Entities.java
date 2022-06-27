package project.cyberproton.atom.entity;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.Atom;

import java.util.UUID;

public class Entities {
    public static IEntity getEntity(@NotNull UUID uuid) {
        return Atom.getInstance().getEntityManager().getEntity(uuid);
    }
}
