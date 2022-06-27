package project.cyberproton.atom.bukkit.event.entity;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class EntityRemoveEvent extends EntityIdentifiableEvent {
    public EntityRemoveEvent(@NotNull UUID entityId) {
        super(entityId);
    }
}
