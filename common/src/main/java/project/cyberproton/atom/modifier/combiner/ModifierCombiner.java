package project.cyberproton.atom.modifier.combiner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.modifier.Modifier;

import java.util.Collection;

public interface ModifierCombiner<M extends Modifier<?>, CO> {
    BooleanModifierCombiner BOOLEAN = new BooleanModifierCombiner();
    DoubleModifierCombiner DOUBLE = new DoubleModifierCombiner();
    IntegerModifierCombiner INTEGER = new IntegerModifierCombiner();

    @Nullable
    CO combine(@NotNull Collection<? extends @NotNull M> modifiers);
}
