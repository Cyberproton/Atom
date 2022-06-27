package project.cyberproton.atom.mechanics;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.modifier.BooleanModifier;
import project.cyberproton.atom.modifier.DoubleModifier;
import project.cyberproton.atom.modifier.IntegerModifier;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.modifier.combiner.ModifierCombiner;

import java.util.Collection;

@FunctionalInterface
public interface MechanicsModifierExtractor<M extends Modifier<?>, O> {
    MechanicsModifierExtractor<BooleanModifier, Boolean> BOOLEAN = (modifiers, context) -> ModifierCombiner.BOOLEAN.combine(modifiers);
    MechanicsModifierExtractor<DoubleModifier, Double> DOUBLE = (modifiers, context) -> ModifierCombiner.DOUBLE.combine(modifiers);
    MechanicsModifierExtractor<IntegerModifier, Integer> INTEGER = (modifiers, context) -> ModifierCombiner.INTEGER.combine(modifiers);

    O extract(@NotNull Collection<M> modifiers, @NotNull MechanicsExtractContext context);
}
