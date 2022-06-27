package project.cyberproton.atom.world;

import org.spongepowered.math.vector.Vector3d;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public interface Location {
    static Location of(@NotNull World world, double x, double y, double z, float yaw, float pitch) {
        return new DefaultLocation(world, x, y, z, yaw, pitch);
    }

    static Location of(@NotNull World world, double x, double y, double z) {
        return of(world, x, y, z);
    }

    static Location of(@NotNull World world, @NotNull Vector3d position, float yaw, float pitch) {
        return of(world, position.x(), position.y(), position.z(), yaw, pitch);
    }

    static Location of(@NotNull World world, @NotNull Vector3d position) {
        return of(world, position.x(), position.y(), position.z());
    }

    @NotNull
    World getWorld();

    @Nullable
    World getWorldIfAvailable();

    boolean isWorldAvailable();

    double getX();

    double getY();

    double getZ();

    float getYaw();

    float getPitch();

    default Vector3d getPosition() {
        return new Vector3d(getX(), getY(), getZ());
    }

    default Location add(double x, double y, double z) {
        return of(getWorld(), getX() + x, getY() + y, getZ() + z, getYaw(), getPitch());
    }

    default Location add(@NotNull Vector3d v3d) {
        Objects.requireNonNull(v3d, "v3d");
        return of(getWorld(), getX() + v3d.x(), getY() + v3d.y(), getZ() + v3d.z(), getYaw(), getPitch());
    }

    default Location sub(double x, double y, double z) {
        return of(getWorld(), getX() - x, getY() - y, getZ() - z, getYaw(), getPitch());
    }

    default Location sub(@NotNull Vector3d v3d) {
        Objects.requireNonNull(v3d, "v3d");
        return of(getWorld(), getX() - v3d.x(), getY() - v3d.y(), getZ() - v3d.z(), getYaw(), getPitch());
    }

    default Location mul(double x, double y, double z) {
        return of(getWorld(), getX() * x, getY() * y, getZ() * z, getYaw(), getPitch());
    }

    default Location mul(@NotNull Vector3d v3d) {
        Objects.requireNonNull(v3d, "v3d");
        return of(getWorld(), getX() * v3d.x(), getY() * v3d.y(), getZ() * v3d.z(), getYaw(), getPitch());
    }

    default Location div(double x, double y, double z) {
        return of(getWorld(), getX() / x, getY() / y, getZ() / z, getYaw(), getPitch());
    }

    default Location div(@NotNull Vector3d v3d) {
        Objects.requireNonNull(v3d, "v3d");
        return of(getWorld(), getX() / v3d.x(), getY() / v3d.y(), getZ() / v3d.z(), getYaw(), getPitch());
    }
}
