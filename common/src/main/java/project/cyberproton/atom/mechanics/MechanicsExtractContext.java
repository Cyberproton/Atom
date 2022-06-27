package project.cyberproton.atom.mechanics;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.entity.IDamageMechanicsModule;
import project.cyberproton.atom.entity.IEntity;

import java.util.Objects;

public class MechanicsExtractContext {
    private final IEntity target;

    public MechanicsExtractContext(@NotNull IEntity target) {
        Objects.requireNonNull(target, "target");
        this.target = target;
    }

    @NotNull
    public IEntity getTarget() {
        return target;
    }

    @NotNull
    public IDamageMechanicsModule<?> getDamageMechanicsModule() {
        return target.getDamageMechanicsModule();
    }
}
