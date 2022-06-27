package project.cyberproton.atom.modifier;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BooleanModifier extends GenericModifier<Boolean> {
    protected BooleanModifier(@NotNull UUID uniqueId, @NotNull String name, @NotNull Boolean value) {
        super(uniqueId, name, value);
    }

    @NotNull
    public static BooleanModifier of(@NotNull UUID uniqueId, @NotNull String name, boolean value) {
        return new BooleanModifier(uniqueId, name, value);
    }
}
