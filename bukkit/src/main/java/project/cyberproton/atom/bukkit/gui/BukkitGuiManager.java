package project.cyberproton.atom.bukkit.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import project.cyberproton.atom.bukkit.event.Events;
import project.cyberproton.atom.bukkit.plugin.BukkitPlugin;
import project.cyberproton.atom.gui.AbstractGuiManager;
import project.cyberproton.atom.gui.ActiveGui;
import project.cyberproton.atom.gui.Gui;
import project.cyberproton.atom.gui.GuiHandler;
import project.cyberproton.atom.gui.chest.ChestGui;
import project.cyberproton.atom.gui.context.ClickContext;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class BukkitGuiManager extends AbstractGuiManager<BukkitPlugin> {
    private final UUID uuid = UUID.fromString("eeaed939-d474-45b7-a275-81c725c5ce56");

    public BukkitGuiManager(BukkitPlugin plugin) {
        super(plugin);
        registerHandler(ChestGui.class, new BukkitGuiChestHandler(plugin));
    }

    @NotNull
    @Override
    public UUID getUUID() {
        return uuid;
    }
}
