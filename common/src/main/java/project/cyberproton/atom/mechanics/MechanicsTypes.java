package project.cyberproton.atom.mechanics;

import project.cyberproton.atom.mechanics.type.EquipmentDamageMechanicsType;
import project.cyberproton.atom.mechanics.type.NoEquipmentDamageMechanicsType;
import project.cyberproton.atom.state.Key;

public class MechanicsTypes {
    public static EquipmentDamageMechanicsType EQUIPMENT_DAMAGE = EquipmentDamageMechanicsType.equipmentDamage(Key.of("atom", "equipment-damage"));
    public static NoEquipmentDamageMechanicsType NO_EQUIPMENT_DAMAGE = NoEquipmentDamageMechanicsType.noEquipmentDamage(Key.of("atom", "no-equipment-damage"));
    public static DoubleMechanicsType PHYSICAL_DAMAGE = DoubleMechanicsType.of(Key.of("atom", "physical-damage"));
    public static DoubleMechanicsType PHYSICAL_CRITICAL_STRIKE_POWER = DoubleMechanicsType.of(Key.of("atom", "physical-critical-strike-power"));
    public static DoubleMechanicsType PHYSICAL_CRITICAL_STRIKE_CHANCE = DoubleMechanicsType.of(Key.of("atom", "physical-critical-strike-chance"));
    public static DoubleMechanicsType TRUE_DAMAGE = DoubleMechanicsType.of(Key.of("atom", "true-damage"));
    public static DoubleMechanicsType HEAL = DoubleMechanicsType.of(Key.of("atom", "heal"));
    public static DoubleMechanicsType LIFE_STEAL = DoubleMechanicsType.of(Key.of("atom", "life-steal"));
}
