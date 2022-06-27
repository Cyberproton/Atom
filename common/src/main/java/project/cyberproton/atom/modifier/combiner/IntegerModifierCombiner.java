package project.cyberproton.atom.modifier.combiner;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.modifier.IntegerModifier;

import java.util.Collection;

public class IntegerModifierCombiner implements ModifierCombiner<IntegerModifier, Integer> {
    @Override
    public @NotNull Integer combine(@NotNull Collection<? extends @NotNull IntegerModifier> values) {
        int x = 0;
        int y = 0;
        int z = 0;
        for (IntegerModifier modifier : values) {
            switch (modifier.getOperation()) {
                case ADDITION:
                    x += modifier.getValue();
                    break;
                case MULTIPLY_BASE:
                    y += modifier.getValue();
                    break;
                case MULTIPLY_TOTAL:
                    z += modifier.getValue();
                    break;
            }
        }
        int combined = x;
        combined += x * y;
        combined *= 1 + z;
        return combined;
    }
}
