package project.cyberproton.atom.bukkit.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.spongepowered.math.vector.Vector3d;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.bukkit.adapter.BukkitAdapter;
import project.cyberproton.atom.bukkit.event.Events;
import project.cyberproton.atom.bukkit.event.entity.EntityCreateEvent;
import project.cyberproton.atom.bukkit.event.entity.EntityPreCreateEvent;
import project.cyberproton.atom.bukkit.event.entity.EntityPreRemoveEvent;
import project.cyberproton.atom.bukkit.event.entity.EntityRemoveEvent;
import project.cyberproton.atom.bukkit.plugin.BukkitPlugin;
import project.cyberproton.atom.entity.*;
import project.cyberproton.atom.entity.player.IPlayer;
import project.cyberproton.atom.event.Subscription;
import project.cyberproton.atom.state.Store;
import project.cyberproton.atom.world.AbstractEntityManager;
import project.cyberproton.atom.world.Location;
import project.cyberproton.atom.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public class BukkitEntityManager extends AbstractEntityManager<BukkitPlugin> {
    private final List<Subscription> subs = new ArrayList<>();

    public BukkitEntityManager(@NotNull BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        getLog().dt(getClass().getSimpleName(), "Enable");
        subs.add(Events.subscribe(PlayerJoinEvent.class).handler(event -> {
            getLog().dt(getClass().getSimpleName(), "Player join " + event.getPlayer().getUniqueId());
            create(event.getPlayer().getUniqueId());
        }).submit(getPlugin()));
        subs.add(Events.subscribe(PlayerQuitEvent.class).handler(event -> {
            getLog().dt(getClass().getSimpleName(), "Player quit " + event.getPlayer().getUniqueId());
            removeEntity(event.getPlayer().getUniqueId());
        }).submit(getPlugin()));
        subs.add(Events.subscribe(EntitySpawnEvent.class).handler((event) -> create(event.getEntity().getUniqueId())).submit(getPlugin()));
        subs.add(Events.subscribe(EntityDeathEvent.class).filter(e -> !(e.getEntity() instanceof Player)).handler(event -> {
            removeEntity(event.getEntity().getUniqueId());
        }).submit(getPlugin()));
        subs.add(Events.subscribe(ChunkLoadEvent.class).handler((event) -> {
            for (Entity entity : event.getChunk().getEntities()) {
                create(entity.getUniqueId());
            }
        }).submit(getPlugin()));
        subs.add(Events.subscribe(ChunkUnloadEvent.class).handler(event -> {
            for (Entity entity : event.getChunk().getEntities()) {
                removeEntity(entity.getUniqueId());
            }
        }).submit(getPlugin()));
    }

    @Override
    public void onDisable() {
        for (Subscription sub : subs) {
            sub.unregister();
        }
        super.onDisable();
    }

    @Nullable
    @Override
    protected AbstractEntity platformCreate(@NotNull UUID uniqueId) {
        Entity bukkit = Bukkit.getEntity(uniqueId);
        if (!(bukkit instanceof LivingEntity)) return null;
        Events.call(new EntityPreCreateEvent(uniqueId));
        BukkitEntity<? extends LivingEntity> entity = (bukkit instanceof Player)
                ? new BukkitPlayer(getPlugin(), EntityModules.ModuleId.all(), ((Player) bukkit))
                : new BukkitEntity<>(getPlugin(), EntityModules.ModuleId.all(), (LivingEntity) bukkit);
        Set<Option> options = new HashSet<>();
        if (!(bukkit instanceof Player)) {
            options.add(Option.LAZY_ENABLING);
        }
        entity.getModuleManager().registerModule(new BukkitStatModule(getPlugin(), entity, options));
        //entity.getModuleManager().registerModule(new AbilityModule<>(getPlugin(), entity, options));
        //entity.getModuleManager().registerModule(new BukkitStateModule(getPlugin(), entity, emptyOptions));
        Events.call(new EntityCreateEvent(entity));
        return entity;
    }

    @Override
    protected void platformRemove(@NotNull IEntity entity) {
        if (!(entity instanceof BukkitEntity)) {
            throw new IllegalArgumentException("Entity is not BukkitEntity. Is this platform Bukkit?");
        }
        if (entity instanceof IPlayer) {
            Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Removing player: " + entity.getUniqueId());
        }
        Events.call(new EntityPreRemoveEvent(entity));
        entities.remove(entity.getUniqueId());
        entity.disable();
        if (!(entity instanceof IPlayer)) {
            ((BukkitEntity<?>) entity).unwrap().remove();
        }
        Events.call(new EntityRemoveEvent(entity.getUniqueId()));
    }

    @Nullable
    @Override
    public IEntity removeEntity(@NotNull IEntity entity, @NotNull Store data) {
        if (entity instanceof BukkitEntity) {
            return removeEntity(entity.getUniqueId());
        }
        IEntity e = entities.remove(entity.getUniqueId());
        if (e == null) {
            return null;
        }
        e.disable();
        EntityType.Remover remover = e.getEntityType().getRemover();
        if (remover != null) {
            remover.remove(e, data);
        }
        return e;
    }

    @Nullable
    @Override
    public IEntity spawnMinecraftEntity(@NotNull MinecraftEntityType type, @NotNull World world, @NotNull Vector3d position, @NotNull Store data) {
        org.bukkit.entity.EntityType bukkitEntityType = org.bukkit.entity.EntityType.valueOf(type.name());
        org.bukkit.World bukkitWorld = BukkitAdapter.toBukkitWorld(world);
        if (bukkitWorld == null) {
            return null;
        }
        Entity e = bukkitWorld.spawnEntity(new org.bukkit.Location(bukkitWorld, position.x(), position.y(), position.z()), bukkitEntityType);
        return create(e.getUniqueId());
    }

    @Nullable
    @Override
    public <T extends IEntity> T spawnEntity(@NotNull EntityType<T> type, @NotNull World world, @NotNull Vector3d position, @NotNull Store data) {
        if (type.getCreator() == null) {
            return null;
        }
        T e = type.getCreator().create(Location.of(world, position), data);
        if (e == null) {
            return null;
        }
        create(e);
        return e;
    }

    @Nullable
    @Override
    public IPlayer getPlayer(@NotNull UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uniqueId");
        IEntity entity = entities.get(uniqueId);
        if (!(entity instanceof IPlayer)) {
            return null;
        }
        return ((IPlayer) entity);
    }

    @Nullable
    @Override
    public IPlayer getPlayer(@NotNull String name) {
        Objects.requireNonNull(name, "name");
        UUID uuid = Bukkit.getPlayerUniqueId(name);
        if (uuid == null) {
            return null;
        }
        return getPlayer(uuid);
    }
}
