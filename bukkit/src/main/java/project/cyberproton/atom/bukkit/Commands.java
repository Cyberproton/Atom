package project.cyberproton.atom.bukkit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.Platform;
import project.cyberproton.atom.bukkit.adapter.BukkitAdapter;
import project.cyberproton.atom.bukkit.plugin.BukkitPlugin;
import project.cyberproton.atom.entity.player.IPlayer;
import project.cyberproton.atom.gui.chest.TestGui;
import project.cyberproton.atom.modifier.NumericOperation;
import project.cyberproton.atom.potion.PotionEffectType;
import project.cyberproton.atom.stat.Stats;
import project.cyberproton.atom.modifier.DoubleModifier;

@CommandAlias("atom|at|a")
public class Commands extends BaseCommand {
    @HelpCommand
    @CatchUnknown
    public void help(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("gui")
    public class Gui extends BaseCommand {
        @Subcommand("test")
        public void test(Player player) {
            TestGui gui = new TestGui(Atom.getInstance().getGuiManager());
            Atom.getInstance().getGuiManager().show(gui, player.getUniqueId());
        }
    }

    @Subcommand("reload")
    public void reload(CommandSender sender) {
        sender.sendMessage("Reloading Atom...");
        ((BukkitAtom) Atom.getInstance()).reload();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAtom has been reloaded"));
    }

    @Subcommand("unsafe")
    public class Unsafe extends BaseCommand {
        @Subcommand("reload")
        public void reload(CommandSender sender) {
            sender.sendMessage("Start unsafe reloading...");
            ((BukkitPlugin) Platform.getBootstrap()).onDisable();
            ((BukkitPlugin) Platform.getBootstrap()).onEnable();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUnsafe reloading completed"));
        }
    }

    @Subcommand("stat")
    public class Stat extends BaseCommand {
        @Subcommand("show")
        public void show(CommandSender sender) {
            sender.sendMessage("------- Stats -------");
            for (project.cyberproton.atom.stat.Stat<?, ?> stat : Atom.getInstance().getStatManager().getAllStats()) {
                sender.sendMessage("+ Stat: " + stat.getKey() + ", type: " + stat.getClass().getSimpleName());
            }
            sender.sendMessage("---------------------");
        }

        @Subcommand("add")
        @CommandCompletion("@players")
        public void add(CommandSender sender, IPlayer target) {
            target.getStatModule().getContainer(Stats.ATTACK_DAMAGE).addModifier(DoubleModifier.of("physical.test", 10, NumericOperation.ADDITION));
            sender.sendMessage("Adding stat to player");
        }

        @Subcommand("remove")
        @CommandCompletion("@players")
        public void remove(CommandSender sender, IPlayer target) {
            target.getStatModule().getContainer(Stats.ATTACK_DAMAGE).removeModifiersIf(m -> m.getName().startsWith("physical.test"));
            sender.sendMessage("Remove stat from player");
        }
    }

    @Subcommand("attribute")
    public class Attribute extends BaseCommand {
        @Subcommand("vanilla")
        public void vanilla(Player player, @Optional org.bukkit.attribute.Attribute attribute) {
            player.sendMessage("--- Player " + player.getName() + " attributes ---");
            org.bukkit.attribute.Attribute[] attributes;
            if (attribute == null) {
                attributes = org.bukkit.attribute.Attribute.values();
            } else {
                attributes = new org.bukkit.attribute.Attribute[] { attribute };
            }
            for (org.bukkit.attribute.Attribute attr : attributes) {
                AttributeInstance instance = player.getAttribute(attr);
                if (instance == null) {
                    continue;
                }
                player.sendMessage("* Attribute: " + attr.name() + ", Value: " + instance.getValue());
                for (AttributeModifier modifier : instance.getModifiers()) {
                    player.sendMessage("Modifier: " + modifier.getName() + ", Operation: " + modifier.getOperation() + ", Value: " + modifier.getAmount());
                }
                player.sendMessage("**********************************************");
            }
            player.sendMessage("--------------------------------------------------");
        }
    }

    @Subcommand("effect")
    public class Effect extends BaseCommand {
        @Subcommand("apply")
        @CommandPermission("atom.effect.apply")
        public void apply(CommandSender sender, Player player, PotionEffectType effect, @Default("20") Integer duration, @Default("0") Integer amplifier) {
            player.addPotionEffect(new PotionEffect(BukkitAdapter.toBukkitPotionEffectType(effect), duration, amplifier));
            sender.sendMessage("Potion effect applied");
        }

        @Subcommand("remove")
        @CommandPermission("atom.effect.remove")
        public void remove(CommandSender sender, Player player, PotionEffectType effect) {
            player.removePotionEffect(BukkitAdapter.toBukkitPotionEffectType(effect));
            sender.sendMessage("Potion effect removed");
        }
    }
}
