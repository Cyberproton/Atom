package project.cyberproton.atom.mechanics;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.modifier.BooleanModifier;
import project.cyberproton.atom.modifier.combiner.ModifierCombiner;
import project.cyberproton.atom.state.Key;

public class BooleanMechanicsType extends GenericMechanicsType<BooleanModifier, Boolean, Boolean> {
    public BooleanMechanicsType(@NotNull Key key) {
        super(key, new TypeToken<BooleanModifier>() {}, TypeToken.of(Boolean.class), TypeToken.of(Boolean.class), (m, c) -> ModifierCombiner.BOOLEAN.combine(m));
    }

    public static BooleanMechanicsType of(@NotNull Key key) {
        return new BooleanMechanicsType(key);
    }
}
