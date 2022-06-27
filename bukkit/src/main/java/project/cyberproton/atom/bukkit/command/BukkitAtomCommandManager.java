package project.cyberproton.atom.bukkit.command;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.Log;
import project.cyberproton.atom.command.Conditions;
import project.cyberproton.atom.entity.player.IPlayer;

public class BukkitAtomCommandManager extends PaperCommandManager {
    public BukkitAtomCommandManager(@NotNull Plugin plugin) {
        super(plugin);
        enableUnstableAPI("help");
        getCommandContexts().registerContext(IPlayer.class, (it) -> {
            IPlayer player = Atom.getInstance().getEntityManager().getPlayer(it.popFirstArg());
            if (player == null) {
                throw new InvalidCommandArgument("&eInvalid player");
            }
            return player;
        });
        getCommandConditions().addCondition(Integer.class, Conditions.LIMIT, (c, exec, value) -> {
            if (value == null) {
                return;
            }
            Log.d(getClass().getSimpleName(), "Config: " + c.getConfig());
            Log.d(getClass().getSimpleName(), "Has min: " + c.hasConfig("min"));
            Log.d(getClass().getSimpleName(), "Has max: " + c.hasConfig("max"));
            if (c.hasConfig("min") && c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("Min value must be " + c.getConfigValue("min", 0));
            }
            if (c.hasConfig("max") && c.getConfigValue("max", 0) < value) {
                throw new ConditionFailedException("Max value must be " + c.getConfigValue("max", 0));
            }
        });
        getCommandConditions().addCondition(Double.class, Conditions.LIMIT, (c, exec, value) -> {
            if (value == null) {
                return;
            }
            Log.d(getClass().getSimpleName(), "Config: " + c.getConfig());
            Log.d(getClass().getSimpleName(), "Has min: " + c.hasConfig("min"));
            Log.d(getClass().getSimpleName(), "Has max: " + c.hasConfig("max"));
            if (c.hasConfig("min") && c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("Min value must be " + c.getConfigValue("min", 0));
            }
            if (c.hasConfig("max") && c.getConfigValue("max", 0) < value) {
                throw new ConditionFailedException("Max value must be " + c.getConfigValue("max", 0));
            }
        });
        getCommandConditions().addCondition(Player.class, "inventory-slots-empty", (c, exec, value) -> {
            if (value == null) {
                return;
            }
            int amount = 1;
            if (c.hasConfig("amount")) {
                amount = c.getConfigValue("amount", 1);
            }
            if (!hasEmptySlots(value.getInventory(), amount)) {
                throw new ConditionFailedException("You must have at least " + amount + "slots to perform this command");
            }
        });
        getCommandConditions().addCondition(Player.class, "has-item-in-main-hand", (c, exec, value) -> {
            if (value == null) {
                return;
            }
            if (!value.getInventory().getItemInMainHand().getType().isAir()) {
                return;
            }
            throw new ConditionFailedException("You must hold the item in main hand");
        });
    }

    public static int getEmptySlots(Inventory inventory) {
        int count = 0;
        if (inventory instanceof PlayerInventory) {
            for (ItemStack item : inventory.getStorageContents()) {
                if (item == null || item.getType().isEmpty()) {
                    count++;
                }
            }
        } else {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item == null || item.getType().isEmpty()) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean hasEmptySlots(Inventory inventory, int slots) {
        if (slots == 0) {
            return true;
        }
        int count = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType().isEmpty()) {
                count++;
                if (count >= slots) {
                    return true;
                }
            }
        }
        return false;
    }
}
