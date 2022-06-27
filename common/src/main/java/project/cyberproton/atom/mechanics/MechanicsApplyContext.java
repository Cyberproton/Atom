package project.cyberproton.atom.mechanics;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.entity.IEntity;

import java.util.Objects;

public class MechanicsApplyContext {
    private final IEntity target;
    private final TargetSide targetSide;
    private final Object event;

    public MechanicsApplyContext(@NotNull IEntity target, @NotNull TargetSide targetSide, @NotNull Object event) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(event, "event");
        this.target = target;
        this.targetSide = targetSide;
        this.event = event;
    }

    @NotNull
    public IEntity getTarget() {
        return target;
    }

    @NotNull
    public TargetSide getTargetSide() {
        return targetSide;
    }

    @NotNull
    public Object getEvent() {
        return event;
    }
}
