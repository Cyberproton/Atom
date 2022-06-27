package project.cyberproton.atom.entity.player;

import net.kyori.adventure.audience.Audience;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.entity.EntityType;
import project.cyberproton.atom.entity.IEntity;

import org.jetbrains.annotations.NotNull;

public interface IPlayer extends IEntity {
    @NotNull
    @Override
    EntityType<? extends IPlayer> getEntityType();

    @NotNull
    default Audience audience() {
        return Atom.getInstance().audiences().player(getUniqueId());
    }
}
