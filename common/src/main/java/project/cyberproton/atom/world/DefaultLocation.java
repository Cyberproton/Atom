package project.cyberproton.atom.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultLocation implements Location {
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    DefaultLocation(@Nullable World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @NotNull
    @Override
    public World getWorld() {
        if (world == null) {
            throw new IllegalStateException("World has not been loaded");
        }
        return world;
    }

    @Nullable
    @Override
    public World getWorldIfAvailable() {
        return null;
    }

    @Override
    public boolean isWorldAvailable() {
        return false;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }
}
