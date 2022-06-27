package project.cyberproton.atom.bukkit.event.entity;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class EntityPreCreateEvent extends EntityIdentifiableEvent {
    public EntityPreCreateEvent(@NotNull UUID entityId) {
        super(entityId);
    }
}
