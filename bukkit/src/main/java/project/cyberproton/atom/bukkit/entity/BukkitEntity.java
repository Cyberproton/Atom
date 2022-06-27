package project.cyberproton.atom.bukkit.entity;

import org.bukkit.entity.LivingEntity;
import project.cyberproton.atom.Keys;
import project.cyberproton.atom.Proxy;
import project.cyberproton.atom.bukkit.adapter.BukkitAdapter;
import project.cyberproton.atom.entity.AbstractEntity;
import project.cyberproton.atom.entity.EntityType;
import project.cyberproton.atom.entity.IEntity;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.state.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BukkitEntity<T extends LivingEntity> extends AbstractEntity implements Proxy<T> {
    protected final WeakReference<T> ref;
    private final EntityType<? extends IEntity> type;

    public BukkitEntity(@NotNull AtomPlugin plugin, @NotNull List<UUID> defaultModuleIds, @NotNull T entity) {
        super(plugin, defaultModuleIds);
        Objects.requireNonNull(entity, "entity");
        this.ref = new WeakReference<>(entity);
        this.type = new EntityType<IEntity>() {
            private final Key key = Key.minecraft(entity.getType().getName() == null ? "UNKNOWN" : entity.getType().getName());

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

    @Nullable
    @Override
    protected StoreConfiguration configure() {
        return StoreConfiguration.builder()
            .interceptors(
                Interceptor.builder(Keys.HEALTH)
                    .onGet(() -> Value.of(unwrap().getHealth()))
                    .onSet(c -> {
                        c.reflectChangesOnStore(false);
                        return c.next();
                    })
                    .build(),
                Interceptor.builder(Keys.LOCATION)
                    .onGet(() -> Value.of(BukkitAdapter.toAtomLocation(unwrap().getLocation())))
                    .onSet(c -> {
                        c.reflectChangesOnStore(false);
                        unwrap().teleport(BukkitAdapter.toBukkitLocation(c.next().get()));
                        return c.next();
                    })
                    .build()
            )
            .build();
    }

    @Nullable
    @Override
    public T unwrapOrNull() {
        return ref.get();
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return unwrap().getUniqueId();
    }

    @NotNull
    @Override
    public EntityType<? extends IEntity> getEntityType() {
        return type;
    }

    @Override
    public boolean teleport(@NotNull project.cyberproton.atom.world.Location location) {
        return unwrap().teleport(BukkitAdapter.toBukkitLocation(location));
    }

    @Override
    public boolean isRemoved() {
        LivingEntity target = ref.get();
        return target == null || target.isDead();
    }
}
