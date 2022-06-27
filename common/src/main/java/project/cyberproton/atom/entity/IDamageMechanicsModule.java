package project.cyberproton.atom.entity;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.mechanics.MechanicsApplyContext;

public interface IDamageMechanicsModule<T extends IEntity> extends IEntityModule<T> {
    void applyMechanics(@NotNull MechanicsApplyContext context);

    void applyMechanics();
}
