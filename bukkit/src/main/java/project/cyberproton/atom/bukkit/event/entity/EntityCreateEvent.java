package project.cyberproton.atom.bukkit.event.entity;

import project.cyberproton.atom.entity.IEntity;

import org.jetbrains.annotations.NotNull;

public class EntityCreateEvent extends EntityEvent {
    public EntityCreateEvent(@NotNull IEntity entity) {
        super(entity);
    }
}
