package project.cyberproton.atom.world;

import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.PersistentEntityStore;
import jetbrains.exodus.entitystore.PersistentEntityStores;
import project.cyberproton.atom.entity.AbstractEntity;
import project.cyberproton.atom.entity.IEntity;
import project.cyberproton.atom.entity.player.IPlayer;
import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractEntityManager<P extends AtomPlugin> implements IEntityManager<P> {
    private final UUID uuid = UUID.fromString("c930b52d-944e-46d2-916d-fdc64213474a");
    private final P plugin;
    protected final Map<UUID, IEntity> entities = new ConcurrentHashMap<>();
    protected PersistentEntityStore store;

    public AbstractEntityManager(@NotNull P plugin) {
        Objects.requireNonNull(plugin, "plugin");
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        getLog().it("EntityManager", "Testing entity store...");
        store = PersistentEntityStores.newInstance(
                getPlugin().getMetadata().getDatabasePath().resolve("entities").toString()
        );
        store.close();
        getLog().it("EntityManager", "Testing completed.");
        store = PersistentEntityStores.newInstance(
            getPlugin().getMetadata().getDatabasePath().resolve("entities").toString()
        );
    }

    @Override
    public void onDisable() {
        if (store != null) {
            store.close();
        }
    }

    @NotNull
    @Override
    public UUID getUUID() {
        return uuid;
    }

    @NotNull
    @Override
    public P getPlugin() {
        return plugin;
    }

    @NotNull
    @Override
    public Collection<IEntity> getAllEntities() {
        return new ArrayList<>(entities.values());
    }

    @NotNull
    @Override
    public Collection<IPlayer> getAllPlayers() {
        List<IPlayer> players = new ArrayList<>();
        for (IEntity entity : entities.values()) {
            if (entity instanceof IPlayer) {
                players.add(((IPlayer) entity));
            }
        }
        return players;
    }

    @Nullable
    @Override
    public IEntity getEntity(@NotNull UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uniqueId");
        return entities.get(uniqueId);
    }

    @Nullable
    @Override
    public IEntity create(@NotNull IEntity entity) {
        Objects.requireNonNull(entity, "entity");
        UUID uniqueId = entity.getUniqueId();
        if (entities.containsKey(uniqueId)) {
            return entities.get(uniqueId);
        }
        store.executeInExclusiveTransaction((txn) -> {
            Entity e = txn.find("Entity", "uniqueId", uniqueId.toString()).getFirst();
            if (e == null) {
                e = txn.newEntity("Entity");
                e.setProperty("uniqueId", UUID.randomUUID().toString());
            }
        });
        entities.put(entity.getUniqueId(), entity);
        entity.enable();
        return entity;
    }

    @Nullable
    @Override
    public IEntity create(@NotNull UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uniqueId");
        if (entities.containsKey(uniqueId)) {
            return entities.get(uniqueId);
        }
        AbstractEntity entity = platformCreate(uniqueId);
        if (entity == null) {
            return null;
        }
        store.executeInExclusiveTransaction((txn) -> {
            Entity e = txn.find("Entity", "uniqueId", uniqueId.toString()).getFirst();
            if (e == null) {
                e = txn.newEntity("Entity");
                e.setProperty("uniqueId", UUID.randomUUID().toString());
            }
        });
        entities.put(entity.getUniqueId(), entity);
        entity.enable();
        //entity.getModuleManager().enableModules();
        return entity;
    }

    @Nullable
    protected abstract AbstractEntity platformCreate(@NotNull UUID uniqueId);

    @Nullable
    @Override
    public IEntity removeEntity(@NotNull UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uniqueId");
        IEntity entity = entities.get(uniqueId);
        if (entity == null) return null;
        platformRemove(entity);
        store.executeInExclusiveTransaction(txn -> {
            Entity e = txn.find("Entity", "uniqueId", uniqueId.toString()).getFirst();
            if (e == null) {
                return;
            }
            e.delete();
        });
        return entity;
    }

    protected abstract void platformRemove(@NotNull IEntity entity);
}
