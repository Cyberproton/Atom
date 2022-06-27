package project.cyberproton.atom.modifier;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LongModifier extends GenericModifier<Long> {
    protected LongModifier(@NotNull UUID uniqueId, @NotNull String name, @NotNull Long value) {
        super(uniqueId, name, value);
    }

    @NotNull
    public static LongModifier of(@NotNull UUID uniqueId, @NotNull String name, long value) {
        return new LongModifier(uniqueId, name, value);
    }
}
