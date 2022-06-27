package project.cyberproton.atom.mechanics.type;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.mechanics.GenericMechanicsType;
import project.cyberproton.atom.mechanics.modifier.EquipmentDamageModifier;
import project.cyberproton.atom.mechanics.modifier.NoEquipmentDamageModifier;
import project.cyberproton.atom.modifier.combiner.ModifierCombiner;
import project.cyberproton.atom.slot.EquipmentSlot;
import project.cyberproton.atom.state.Key;

import java.util.*;

public class NoEquipmentDamageMechanicsType extends GenericMechanicsType<NoEquipmentDamageModifier, Boolean, Map<EquipmentSlot, Boolean>> {
    private NoEquipmentDamageMechanicsType(@NotNull Key key) {
        super(key, TypeToken.of(NoEquipmentDamageModifier.class), TypeToken.of(Boolean.class), new TypeToken<Map<EquipmentSlot, Boolean>>() {}, (modifiers, context) -> {
            Map<EquipmentSlot, Boolean> res = new HashMap<>();
            EnumMap<EquipmentSlot, Collection<NoEquipmentDamageModifier>> categorized = new EnumMap<>(EquipmentSlot.class);
            for (NoEquipmentDamageModifier modifier : modifiers) {
                categorized.computeIfAbsent(modifier.getSlot(), k -> new ArrayList<>()).add(modifier);
            }
            for (Map.Entry<EquipmentSlot, Collection<NoEquipmentDamageModifier>> entry : categorized.entrySet()) {
                res.put(entry.getKey(), ModifierCombiner.BOOLEAN.combine(entry.getValue()));
            }
            return res;
        });
    }

    public static NoEquipmentDamageMechanicsType noEquipmentDamage(@NotNull Key key) {
        return new NoEquipmentDamageMechanicsType(key);
    }
}
