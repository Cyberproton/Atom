package project.cyberproton.atom.mechanics;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.modifier.DoubleModifier;
import project.cyberproton.atom.state.Key;

public class DoubleMechanicsType extends GenericMechanicsType<DoubleModifier, Double, Double> {
    public DoubleMechanicsType(@NotNull Key key) {
        super(key, TypeToken.of(DoubleModifier.class), TypeToken.of(Double.class), TypeToken.of(Double.class), MechanicsModifierExtractor.DOUBLE);
    }

    public static DoubleMechanicsType of(Key key) {
        return new DoubleMechanicsType(key);
    }
}
