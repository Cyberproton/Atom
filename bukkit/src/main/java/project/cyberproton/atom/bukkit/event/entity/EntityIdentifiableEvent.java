package project.cyberproton.atom.bukkit.event.entity;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.UUID;

public class EntityIdentifiableEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final UUID entityId;

    public EntityIdentifiableEvent(@NotNull UUID entityId) {
        Objects.requireNonNull(entityId, "entityId");
        this.entityId = entityId;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    @NotNull
    public UUID getEntityId() {
        return entityId;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
