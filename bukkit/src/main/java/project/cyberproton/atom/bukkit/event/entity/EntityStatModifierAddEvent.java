package project.cyberproton.atom.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.entity.IEntity;
import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.modifier.Modifier;

import java.util.Objects;

public class EntityStatModifierAddEvent extends EntityEvent implements Cancellable {
    private final Stat<?, ?> stat;
    private final Modifier<?> modifier;
    private boolean cancelled;

    public EntityStatModifierAddEvent(@NotNull IEntity entity, @NotNull Stat<?, ?> stat, @NotNull Modifier<?> modifier) {
        super(entity);
        Objects.requireNonNull(stat, "stat");
        Objects.requireNonNull(modifier, "modifier");
        this.stat = stat;
        this.modifier = modifier;
    }

    @NotNull
    public Stat<?, ?> getStat() {
        return stat;
    }

    @NotNull
    public Modifier<?> getModifier() {
        return modifier;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
