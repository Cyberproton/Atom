package project.cyberproton.atom.command;

import org.jetbrains.annotations.Nullable;

public class Conditions {
    public static final String LIMIT = "limit";
    public static final String PLAYER_INVENTORY_HAS_SLOTS_EMPTY = "inventory-slots-empty";

    public static String limit(@Nullable Integer min, @Nullable Integer max) {
        return LIMIT + ":" + (min == null ? "" : "min=" + min) + "," + (max == null ? "" : "max=" + max);
    }

    public static String playerInventoryHasSlotsEmpty(@Nullable Integer amount) {
        return PLAYER_INVENTORY_HAS_SLOTS_EMPTY + ":" + (amount == null ? "" : amount);
    }
}
