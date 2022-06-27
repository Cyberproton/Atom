package project.cyberproton.atom.mechanics.type;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.mechanics.GenericMechanicsType;
import project.cyberproton.atom.mechanics.modifier.EquipmentDamageModifier;
import project.cyberproton.atom.modifier.combiner.ModifierCombiner;
import project.cyberproton.atom.slot.EquipmentSlot;
import project.cyberproton.atom.state.Key;

import java.util.*;

public class EquipmentDamageMechanicsType extends GenericMechanicsType<EquipmentDamageModifier, Double, Map<EquipmentSlot, Double>> {
    protected EquipmentDamageMechanicsType(@NotNull Key key) {
        super(key, TypeToken.of(EquipmentDamageModifier.class), TypeToken.of(Double.class), new TypeToken<Map<EquipmentSlot, Double>>() {}, (modifiers, context) -> {
            Map<EquipmentSlot, Double> res = new HashMap<>();
            EnumMap<EquipmentSlot, Collection<EquipmentDamageModifier>> categorized = new EnumMap<>(EquipmentSlot.class);
            for (EquipmentDamageModifier modifier : modifiers) {
                categorized.computeIfAbsent(modifier.getSlot(), k -> new ArrayList<>()).add(modifier);
            }
            for (Map.Entry<EquipmentSlot, Collection<EquipmentDamageModifier>> entry : categorized.entrySet()) {
                res.put(entry.getKey(), ModifierCombiner.DOUBLE.combine(entry.getValue()));
            }
            return res;
        });
    }

    public static EquipmentDamageMechanicsType equipmentDamage(@NotNull Key key) {
        return new EquipmentDamageMechanicsType(key);
    }
}
