package project.cyberproton.atom.bukkit.entity;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import project.cyberproton.atom.entity.EntityType;
import project.cyberproton.atom.entity.player.IPlayer;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.state.Key;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;

public class BukkitPlayer extends BukkitEntity<Player> implements IPlayer {
    public BukkitPlayer(@NotNull AtomPlugin plugin, @NotNull List<UUID> defaultModuleIds, @NotNull Player player) {
        super(plugin, defaultModuleIds, player);
    }

    @NotNull
    @Override
    public EntityType<? extends IPlayer> getEntityType() {
        return new EntityType<IPlayer>() {
            private final Key key = Key.minecraft("PLAYER");

            @NotNull
            @Override
            public Key getKey() {
                return key;
            }

            @NotNull
            @Override
            public String getName() {
                return getKey().getValue();
            }
        };
    }
}
