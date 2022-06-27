package project.cyberproton.atom.mechanics.modifier;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.modifier.DoubleModifier;
import project.cyberproton.atom.modifier.NumericOperation;
import project.cyberproton.atom.slot.EquipmentSlot;

import java.util.Objects;
import java.util.UUID;

public class EquipmentDamageModifier extends DoubleModifier {
    private final EquipmentSlot slot;

    private EquipmentDamageModifier(UUID uuid, String name, Double value, NumericOperation operation, EquipmentSlot slot) {
        super(uuid, name, value, operation);
        this.slot = slot;
    }

    @NotNull
    public EquipmentSlot getSlot() {
        return slot;
    }

    public static EquipmentDamageModifier equipmentDamage(@NotNull UUID uuid, @NotNull String name, double value, @NotNull EquipmentSlot slot, @NotNull NumericOperation operation) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(operation, "operation");
        Objects.requireNonNull(slot, "slot");
        return new EquipmentDamageModifier(uuid, name, value, operation, slot);
    }

    public static EquipmentDamageModifier equipmentDamage(@NotNull String name, double value, @NotNull EquipmentSlot slot, @NotNull NumericOperation operation) {
        return equipmentDamage(UUID.randomUUID(), name, value, slot, operation);
    }
}
