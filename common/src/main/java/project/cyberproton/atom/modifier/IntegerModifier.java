package project.cyberproton.atom.modifier;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IntegerModifier extends NumericModifier<Integer> {
    protected IntegerModifier(@NotNull UUID uniqueId, @NotNull String name, @NotNull Integer value, @NotNull NumericOperation operation) {
        super(uniqueId, name, value, operation);
    }

    @NotNull
    public static IntegerModifier of(@NotNull UUID uniqueId, @NotNull String name, int value, @NotNull NumericOperation operation) {
        return new IntegerModifier(uniqueId, name, value, operation);
    }
}
