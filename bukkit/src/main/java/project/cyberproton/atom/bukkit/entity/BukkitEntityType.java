package project.cyberproton.atom.bukkit.entity;

import project.cyberproton.atom.entity.EntityType;
import project.cyberproton.atom.state.Key;

import org.jetbrains.annotations.NotNull;

public abstract class BukkitEntityType implements EntityType<BukkitEntity> {
    private final Key key;

    public BukkitEntityType(@NotNull org.bukkit.entity.EntityType type) {
        this.key = Key.of("bukkit", type.getName());
    }

    @NotNull
    @Override
    public Key getKey() {
        return key;
    }

    @NotNull
    @Override
    public String getName() {
        return key.getValue();
    }
}
