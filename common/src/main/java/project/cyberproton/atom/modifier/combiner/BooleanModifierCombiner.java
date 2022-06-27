package project.cyberproton.atom.modifier.combiner;

import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.modifier.BooleanModifier;

import java.util.Collection;

public class BooleanModifierCombiner implements ModifierCombiner<BooleanModifier, Boolean> {
    @Override
    public @Nullable Boolean combine(@NotNull Collection<? extends @NotNull BooleanModifier> modifiers) {
        if (modifiers.isEmpty()) return false;
        return Iterables.getLast(modifiers).getValue();
    }
}
