package project.cyberproton.atom.mechanics;

import org.jetbrains.annotations.NotNull;

public interface MechanicsHandler {
    void applyMechanics(@NotNull MechanicsApplyContext context);

    void applyMechanics();
}
