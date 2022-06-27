package project.cyberproton.atom.entity;

import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;

public abstract class AtomEntity extends AbstractEntity {
    private final UUID uniqueId = UUID.randomUUID();
    private boolean isRemoved = false;

    public AtomEntity(@NotNull AtomPlugin plugin, @NotNull List<UUID> defaultModuleIds) {
        super(plugin, defaultModuleIds);
    }

    public AtomEntity(@NotNull AtomPlugin plugin) {
        super(plugin);
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean isRemoved() {
        return isRemoved;
    }


    public void markAsRemoved() {
        isRemoved = true;
    }

}
