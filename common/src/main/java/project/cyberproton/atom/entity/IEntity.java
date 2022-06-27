package project.cyberproton.atom.entity;

import org.spongepowered.math.vector.Vector3d;
import project.cyberproton.atom.Identifiable;
import project.cyberproton.atom.state.NonExpandableStore;
import project.cyberproton.atom.world.Location;
import project.cyberproton.atom.world.World;

import org.jetbrains.annotations.NotNull;

public interface IEntity extends Identifiable, NonExpandableStore {
    @NotNull
    EntityType<? extends IEntity> getEntityType();

    @NotNull
    EntityModuleManager getModuleManager();

    @NotNull
    IStatModule<? extends IEntity> getStatModule();

    @NotNull
    IStateModule<? extends IEntity> getStateModule();

    @NotNull
    IDamageMechanicsModule<? extends IEntity> getDamageMechanicsModule();

    boolean teleport(@NotNull Location location);

    default boolean teleport(@NotNull World world, @NotNull Vector3d position) {
        return teleport(Location.of(world, position));
    }

    boolean isRemoved();

    default void onTick() {}

    default void enable() {
        getModuleManager().enableModules();
    }

    default void reload() {
        getModuleManager().reloadModules();
    }

    default void disable() {
        getModuleManager().disableModules();
    }
}
