package project.cyberproton.atom.bukkit.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import project.cyberproton.atom.bukkit.event.Events;
import project.cyberproton.atom.gui.FrameBuilder;
import project.cyberproton.atom.gui.Gui;
import project.cyberproton.atom.gui.GuiHandler;

public abstract class BukkitGuiHandler<G extends Gui<? extends FrameBuilder, ? extends FrameBuilder>> implements GuiHandler<G> {
    private final Plugin plugin;

    public BukkitGuiHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
