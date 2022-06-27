package project.cyberproton.atom.entity;

import project.cyberproton.atom.state.Key;
import project.cyberproton.atom.state.Store;
import project.cyberproton.atom.world.Location;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityType<E extends IEntity> {

    @NotNull
    Key getKey();

    @NotNull
    String getName();

    @Nullable
    default Creator<E> getCreator() {
        return null;
    }

    @Nullable
    default Remover getRemover() {
        return null;
    }

    @FunctionalInterface
    interface Creator<E extends IEntity> {
        @Nullable
        E create(@NotNull Location location, @NotNull Store data);
    }


    @FunctionalInterface
    interface Remover {
        boolean remove(IEntity entity, @NotNull Store data);
    }

}
