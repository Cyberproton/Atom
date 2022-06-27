package project.cyberproton.atom.bukkit.event.entity;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import project.cyberproton.atom.entity.IEntity;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public abstract class EntityEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final IEntity entity;

    public EntityEvent(@NotNull IEntity entity) {
        Objects.requireNonNull(entity, "entity");
        this.entity = entity;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    @NotNull
    public IEntity getEntity() {
        return entity;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
