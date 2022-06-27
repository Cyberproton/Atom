package project.cyberproton.atom.bukkit.listener;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.GlobalConfig;
import project.cyberproton.atom.bukkit.entity.BukkitDamageMechanicsModule;
import project.cyberproton.atom.entity.Entities;
import project.cyberproton.atom.entity.IDamageMechanicsModule;
import project.cyberproton.atom.entity.IEntity;
import project.cyberproton.atom.entity.Players;
import project.cyberproton.atom.entity.player.IPlayer;
import project.cyberproton.atom.mechanics.MechanicsApplyContext;
import project.cyberproton.atom.mechanics.TargetSide;
import project.cyberproton.atom.stat.Stats;

import java.lang.reflect.Field;

public class DamageSystemListener implements Listener {
    private final Field modifierFunctions;
    private final Field modifiers;

    public DamageSystemListener() {
        try {
            this.modifierFunctions = EntityDamageEvent.class.getDeclaredField("modifierFunctions");
            this.modifierFunctions.setAccessible(true);
            this.modifiers = EntityDamageEvent.class.getDeclaredField("modifiers");
            this.modifiers.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamageFire(EntityDamageByEntityEvent event) {

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!Atom.getInstance().globalConfig().damageSection().useCustomDamageSystem()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        // Defender side
        IPlayer player = Players.getPlayer(event.getEntity().getUniqueId());
        if (player == null) {
            return;
        }
        IDamageMechanicsModule<?> module = player.getDamageMechanicsModule();
        module.applyMechanics(new MechanicsApplyContext(player, TargetSide.DEFENDER, event));

        // Attack side
        if (!(event instanceof EntityDamageByEntityEvent) || !(((EntityDamageByEntityEvent) event).getDamager() instanceof LivingEntity)) {
            return;
        }
        IEntity damager = Entities.getEntity(((EntityDamageByEntityEvent) event).getDamager().getUniqueId());
        if (damager == null) {
            return;
        }
        damager.getDamageMechanicsModule().applyMechanics(new MechanicsApplyContext(damager, TargetSide.ATTACKER, event));
    }

    private static double reductionDamageByArmor(double damage, double armor, double armorToughness) {
        return (1 - Math.min(20, Math.max(armor / 5, armor - (4 * damage) / (armorToughness + 8))) / 25);
    }

    private static double reductionByResistance(LivingEntity target, double coefficient) {
        PotionEffect potionEffect = target.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        if (potionEffect == null) {
            return 0.0;
        }
        return potionEffect.getAmplifier() * coefficient;
    }

    public static int epf(LivingEntity target) {
        EntityEquipment inv = target.getEquipment();
        if (inv == null) {
            return 0;
        }
        ItemStack helm = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack legs = inv.getLeggings();
        ItemStack boot = inv.getBoots();
        return (helm != null ? helm.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) : 0) +
               (chest != null ? chest.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) : 0) +
               (legs != null ? legs.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) : 0) +
               (boot != null ? boot.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) : 0);
    }

}
