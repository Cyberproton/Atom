package project.cyberproton.atom.bukkit.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.gui.ActiveGui;

public class BukkitGuiHolder implements InventoryHolder {
    private final ActiveGui<?> gui;

    public BukkitGuiHolder(ActiveGui<?> gui) {
        this.gui = gui;
    }

    @Override
    public @NotNull Inventory getInventory() {
        throw new UnsupportedOperationException();
    }

    public ActiveGui<?> getGui() {
        return gui;
    }

    public static BukkitGuiHolder of(ActiveGui<?> gui) {
        return new BukkitGuiHolder(gui);
    }
}
