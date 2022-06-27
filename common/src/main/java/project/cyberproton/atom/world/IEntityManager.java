package project.cyberproton.atom.world;

import org.spongepowered.math.vector.Vector3d;
import project.cyberproton.atom.entity.EntityType;
import project.cyberproton.atom.entity.IEntity;
import project.cyberproton.atom.entity.MinecraftEntityType;
import project.cyberproton.atom.entity.player.IPlayer;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.plugin.PluginModule;
import project.cyberproton.atom.state.Store;
import project.cyberproton.atom.terminable.Terminable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.UUID;

public interface IEntityManager<P extends AtomPlugin> extends PluginModule<P>, Terminable {
    @NotNull
    Collection<IEntity> getAllEntities();

    @NotNull
    Collection<IPlayer> getAllPlayers();

    @Nullable
    IEntity spawnMinecraftEntity(@NotNull MinecraftEntityType type, @NotNull World world, @NotNull Vector3d position, @NotNull Store data);

    @Nullable
    default IEntity spawnMinecraftEntity(@NotNull MinecraftEntityType type, @NotNull World world, @NotNull Vector3d position) {
        return spawnMinecraftEntity(type, world, position, Store.empty());
    }

    @Nullable
    <T extends IEntity> T spawnEntity(@NotNull EntityType<T> type, @NotNull World world, @NotNull Vector3d position, @NotNull Store data);

    @Nullable
    default <T extends IEntity> T spawnEntity(@NotNull EntityType<T> type, @NotNull World world, @NotNull Vector3d position) {
        return spawnEntity(type, world, position, Store.empty());
    }

    @Nullable
    IEntity create(@NotNull UUID uniqueId);

    @Nullable
    IEntity create(@NotNull IEntity entity);

    @Nullable
    IEntity getEntity(@NotNull UUID uniqueId);

    @Nullable
    IPlayer getPlayer(@NotNull UUID uniqueId);

    @Nullable
    IPlayer getPlayer(@NotNull String name);

    @Nullable
    IEntity removeEntity(@NotNull UUID uniqueId);

    @Nullable
    IEntity removeEntity(@NotNull IEntity entity, @NotNull Store data);

    @Nullable
    default IEntity removeEntity(@NotNull IEntity entity) {
        return removeEntity(entity, Store.empty());
    }

    @Override
    default void close() {
        unregister();
    }
}
