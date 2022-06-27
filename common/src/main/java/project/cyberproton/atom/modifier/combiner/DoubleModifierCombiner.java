package project.cyberproton.atom.modifier.combiner;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.modifier.DoubleModifier;

import java.util.Collection;

public class DoubleModifierCombiner implements ModifierCombiner<DoubleModifier, Double> {
    @Override
    public @NotNull Double combine(@NotNull Collection<? extends @NotNull DoubleModifier> values) {
        double x = 0;
        double y = 0;
        double z = 0;
        for (DoubleModifier modifier : values) {
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
        double combined = x;
        combined += x * y;
        combined *= 1 + z;
        return combined;
    }
}
