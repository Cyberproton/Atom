package project.cyberproton.atom.bukkit.event.entity;

import project.cyberproton.atom.entity.IEntity;

import org.jetbrains.annotations.NotNull;

public class EntityPreRemoveEvent extends EntityEvent {
    public EntityPreRemoveEvent(@NotNull IEntity entity) {
        super(entity);
    }
}
