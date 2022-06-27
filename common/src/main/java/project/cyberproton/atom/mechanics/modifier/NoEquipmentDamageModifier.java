package project.cyberproton.atom.mechanics.modifier;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.modifier.BooleanModifier;
import project.cyberproton.atom.slot.EquipmentSlot;

import java.util.Objects;
import java.util.UUID;

public class NoEquipmentDamageModifier extends BooleanModifier {
    private final EquipmentSlot slot;

    private NoEquipmentDamageModifier(@NotNull UUID uuid, @NotNull String name, @NotNull Boolean value, @NotNull EquipmentSlot slot) {
        super(uuid, name, value);
        this.slot = slot;
    }

    @NotNull
    public EquipmentSlot getSlot() {
        return slot;
    }

    public static NoEquipmentDamageModifier noEquipmentDamage(@NotNull UUID uuid, @NotNull String name, boolean value, @NotNull EquipmentSlot slot) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(slot, "slot");
        return new NoEquipmentDamageModifier(uuid, name, value, slot);
    }

    public static NoEquipmentDamageModifier noEquipmentDamage(@NotNull String name, boolean value, @NotNull EquipmentSlot slot) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(slot, "slot");
        return new NoEquipmentDamageModifier(UUID.randomUUID(), name, value, slot);
    }
}
