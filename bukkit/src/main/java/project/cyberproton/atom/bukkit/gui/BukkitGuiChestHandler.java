package project.cyberproton.atom.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import project.cyberproton.atom.bukkit.adapter.BukkitAdapter;
import project.cyberproton.atom.bukkit.event.Events;
import project.cyberproton.atom.gui.*;
import project.cyberproton.atom.gui.chest.ChestGui;
import project.cyberproton.atom.gui.chest.TopFrameChestBuilder;
import project.cyberproton.atom.gui.context.ClickContext;
import project.cyberproton.atom.gui.element.Clickable;
import project.cyberproton.atom.gui.element.Element;
import project.cyberproton.atom.gui.element.Inputtable;
import project.cyberproton.atom.gui.pane.Pane;
import project.cyberproton.atom.item.ItemStack;
import project.cyberproton.atom.util.Enums;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class BukkitGuiChestHandler implements GuiHandler<ChestGui>, Listener {
    private final Plugin plugin;
    private final Map<UUID, PlatformGui> inventories = new HashMap<>();

    public BukkitGuiChestHandler(Plugin plugin) {
        this.plugin = plugin;
        Events.subscribe(InventoryClickEvent.class).handler(event -> {
            plugin.getLogger().info("sfsdfdsfdfsf Click");
            if (!(event.getInventory().getHolder() instanceof BukkitGuiHolder)) {
                return;
            }
            plugin.getLogger().info("holder pass");
            BukkitGuiHolder holder = ((BukkitGuiHolder) event.getInventory().getHolder());
            if (!inventories.containsKey(holder.getGui().getUniqueId())) {
                return;
            }
            plugin.getLogger().info("Inventories pass");
            Inventory inventory = inventories.get(holder.getGui().getUniqueId()).getInventory();
            ClickContext.FramePosition framePosition = event.getClickedInventory() != inventory ? ClickContext.FramePosition.BOTTOM : ClickContext.FramePosition.TOP;

            ClickContext.ClickType clickType = Enums.valueOfOrNull(ClickContext.ClickType.class, event.getClick().name());
            if (clickType == null) {
                return;
            }
            ClickContext context = new ClickContext(event.getWhoClicked().getUniqueId(), framePosition, fromRawPosition(event.getSlot()), event, clickType);
            click((ActiveGui<ChestGui>) holder.getGui(), event.getWhoClicked().getUniqueId(), context);
            event.setCancelled(true);
        }).submit(plugin);
        Events.subscribe(InventoryDragEvent.class).handler(event -> {
            if (!(event.getInventory().getHolder() instanceof BukkitGuiHolder)) {
                return;
            }
            event.setCancelled(true);
        });
        Events.subscribe(InventoryCloseEvent.class).handler(event -> {
            if (!(event.getInventory().getHolder() instanceof BukkitGuiHolder)) {
                return;
            }
            BukkitGuiHolder holder = ((BukkitGuiHolder) event.getInventory().getHolder());
            if (!inventories.containsKey(holder.getGui().getUniqueId())) {
                close((ActiveGui<ChestGui>) holder.getGui());
            }
        }).submit(plugin);
    }

    @NotNull
    @Override
    public Class<ChestGui> guiType() {
        return ChestGui.class;
    }

    @Override
    public void show(ActiveGui<ChestGui> gui, Collection<UUID> viewers) {
        GuiBuilder<TopFrameChestBuilder, PlayerInventoryFrameBuilder> builder = gui.gui().onBuild();
        int row = builder.topFrame().row();
        if (row < 0 || row > 6) {
            return;
        }
        if (!inventories.containsKey(gui.getUniqueId())) {
            update(gui);
        }
        Inventory inventory = inventories.get(gui.getUniqueId()).getInventory();
        for (UUID viewer : viewers) {
            Player player = Bukkit.getPlayer(viewer);
            if (player == null) {
                continue;
            }
            player.openInventory(inventory);
        }
    }

    @Override
    public void update(ActiveGui<ChestGui> gui) {
        PlatformGui platformGui = inventories.get(gui.getUniqueId());
        GuiBuilder<TopFrameChestBuilder, PlayerInventoryFrameBuilder> builder = gui.gui().onBuild();
        int row = builder.topFrame().row();
        if (row < 0 || row > 6) {
            return;
        }
        String title = builder.topFrame().title();
        if (title == null) title = "";
        Inventory inventory = platformGui == null ? Bukkit.createInventory(BukkitGuiHolder.of(gui), row * 9, title) : platformGui.getInventory();
        inventory.clear();
        Map<Position, Element> elements = new HashMap<>();
        TopFrameChestBuilder top = builder.topFrame();
        for (SlotBuilder high : top.slots()) {
            Pane highComp = high.component();
            if (highComp == null) continue;
            Map<Position, Element> highElems = highComp.onBuild();
            if (highElems == null) continue;
            for (Map.Entry<Position, Element> mid : highElems.entrySet()) {
                Map<Position, ItemStack> midElems = mid.getValue().onBuild();
                if (midElems == null) continue;
                for (Map.Entry<Position, ItemStack> low : midElems.entrySet()) {
                    Position position = high.position().add(mid.getKey().add(low.getKey()));
                    if (isNotInBound(position, inventory.getSize())) {
                        continue;
                    }
                    ItemStack itemStack = low.getValue();
                    inventory.setItem(rawPositionOf(position), BukkitAdapter.toBukkitItemStack(itemStack));
                    elements.put(position, mid.getValue());
                }
            }
        }
        inventories.put(gui.getUniqueId(), new PlatformGui(inventory, elements));
    }

    private int rawPositionOf(Position position) {
        return position.y() * 9 + position.x();
    }

    private boolean isNotInBound(Position position, int size) {
        int slot = position.y() * 9 + position.x();
        return slot < 0 || slot >= size;
    }

    private Position fromRawPosition(int raw) {
        int y = raw / 9;
        int x = raw - (raw / 9 * 9);
        return Position.of(x, y);
    }

    @Override
    public void click(ActiveGui<ChestGui> gui, UUID clicker, ClickContext context) {
        plugin.getLogger().info("Click at " + context.getClickedPosition());
        PlatformGui platformGui = inventories.get(gui.getUniqueId());
        if (platformGui == null) return;
        Element element = platformGui.getElements().get(context.getClickedPosition());
        if (element instanceof Clickable) {
            Consumer<ClickContext> anyClickHandler = ((Clickable) element).onAnyClickHandler();
            Consumer<ClickContext> leftClickHandler = ((Clickable) element).onLeftClickHandler();
            Consumer<ClickContext> rightClickHandler = ((Clickable) element).onRightClickHandler();
            Consumer<ClickContext> shiftLeftClickHandler = ((Clickable) element).onShiftLeftClickHandler();
            Consumer<ClickContext> shiftRightClickHandler = ((Clickable) element).onShiftRightClickHandler();
            if (anyClickHandler != null) {
                anyClickHandler.accept(context);
            }
            if (context.getClickType() == ClickContext.ClickType.LEFT && leftClickHandler != null) {
                leftClickHandler.accept(context);
            }
            if (context.getClickType() == ClickContext.ClickType.RIGHT && rightClickHandler != null) {
                rightClickHandler.accept(context);
            }
            if (context.getClickType() == ClickContext.ClickType.SHIFT_LEFT && shiftLeftClickHandler != null) {
                shiftLeftClickHandler.accept(context);
            }
            if (context.getClickType() == ClickContext.ClickType.SHIFT_RIGHT && shiftRightClickHandler != null) {
                shiftRightClickHandler.accept(context);
            }
        }
        if (element instanceof Inputtable) {

        }
    }

    @Override
    public void close(ActiveGui<ChestGui> gui, Collection<UUID> viewers) {
        Inventory inventory = inventories.get(gui.getUniqueId()).getInventory();
        if (inventory == null) {
            return;
        }
        for (UUID viewer : viewers) {
            Player player = Bukkit.getPlayer(viewer);
            if (player == null || player.getOpenInventory().getTopInventory() != inventory) {
                continue;
            }
            player.closeInventory();
        }
        if (inventory.getViewers().isEmpty()) {
            inventories.remove(gui.getUniqueId());
            gui.gui().onDispose();
        }
    }

    @Override
    public void close(ActiveGui<ChestGui> gui) {
        Inventory inventory = inventories.get(gui.getUniqueId()).getInventory();
        if (inventory == null) {
            return;
        }
        for (HumanEntity viewer : inventory.getViewers()) {
            viewer.closeInventory();
        }
        inventories.remove(gui.getUniqueId());
        gui.gui().onDispose();
    }

    @NotNull
    @Override
    public Collection<UUID> viewersOf(ActiveGui<ChestGui> gui) {
        Inventory inventory = inventories.get(gui.getUniqueId()).getInventory();
        if (inventory == null) {
            return new ArrayList<>();
        }
        List<UUID> viewers = new ArrayList<>();
        for (HumanEntity viewer : inventory.getViewers()) {
            viewers.add(viewer.getUniqueId());
        }
        return viewers;
    }

    private static class PlatformGui {
        private final Inventory inventory;
        private final Map<Position, Element> elements;

        public PlatformGui(Inventory inventory, Map<Position, Element> elements) {
            this.inventory = inventory;
            this.elements = elements;
        }

        public Inventory getInventory() {
            return inventory;
        }

        public Map<Position, Element> getElements() {
            return elements;
        }
    }
}
