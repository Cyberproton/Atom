package project.cyberproton.atom.bukkit.entity;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.GlobalConfig;
import project.cyberproton.atom.bukkit.adapter.BukkitAdapter;
import project.cyberproton.atom.bukkit.plugin.BukkitPlugin;
import project.cyberproton.atom.entity.AbstractDamageMechanicsModule;
import project.cyberproton.atom.entity.IEntity;
import project.cyberproton.atom.mechanics.MechanicsApplyContext;
import project.cyberproton.atom.mechanics.MechanicsTypes;
import project.cyberproton.atom.mechanics.modifier.EquipmentDamageModifier;
import project.cyberproton.atom.slot.EquipmentSlot;
import project.cyberproton.atom.stat.Stats;

import java.util.HashMap;
import java.util.Map;

public class BukkitDamageMechanicsModule extends AbstractDamageMechanicsModule<BukkitEntity<?>> {
    private EntityDamageEvent handlingEvent = null;

    public BukkitDamageMechanicsModule(@NotNull BukkitPlugin plugin, @NotNull BukkitEntity<?> owner) {
        super(plugin, owner);
    }

    @Override
    public void applyMechanics(@NotNull MechanicsApplyContext context) {
        if (!(context.getEvent() instanceof EntityDamageEvent)) {
            throw new IllegalArgumentException("Not a bukkit EntityDamageEvent");
        }
        EntityDamageEvent event = (EntityDamageEvent) context.getEvent();
        apply(event);
        typedModifiers.clear();
    }

    @Override
    public void applyMechanics() {

    }

    private void apply(EntityDamageEvent event) {

        double base = event.getDamage();
        Double value = getCombinedModifierValue(MechanicsTypes.PHYSICAL_DAMAGE);
        Map<EquipmentSlot, Boolean> noEquipmentDamages = getCombinedModifierValue(MechanicsTypes.NO_EQUIPMENT_DAMAGE);

        base += value;
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "");
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Damage before: " + event.getFinalDamage());
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Base: " + event.getDamage());

        IEntity player = Atom.getInstance().getEntityManager().getEntity(event.getEntity().getUniqueId());
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity entity = ((LivingEntity) event.getEntity());
        if (player == null) {
            return;
        }

        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Attack damage: " + player.getStatModule().getValue(Stats.ATTACK_DAMAGE));
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Reduction before: " + event.getDamage(EntityDamageEvent.DamageModifier.ARMOR));
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Reduction protection before: " + event.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE));
        event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0);
        event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, 0);
        event.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, 0);

        double armor = player.getStatModule().getValue(Stats.ARMOR);
        double armorToughness = player.getStatModule().getValue(Stats.ARMOR_TOUGHNESS);
        //double expectingDamage = reductionDamageByArmor(base, armor, armorToughness);

        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Armor: " + armor);
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Toughness: " + armorToughness);
        //Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Reduction after: " + damage);
        //Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Expecting damage: " + expectingDamage);

        double reductionByEnchants = base * epf(entity) * GlobalConfig.instance().damageSection().protectionEnchantCoefficient();
        double reductionByArmor = base - base * reductionDamageByArmor(base, armor, armorToughness);
        double reductionByResistance = base * reductionByResistance(entity, GlobalConfig.instance().damageSection().resistancePotionCoefficient());

        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Reduction by armor: " + reductionByArmor);
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Reduction by enchants: " + reductionByEnchants);
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Reduction by resistance: " + reductionByResistance);

        event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, -reductionByEnchants);
        event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, -reductionByArmor);
        event.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, -reductionByResistance);

        event.setDamage(EntityDamageEvent.DamageModifier.BASE, Math.max(GlobalConfig.instance().damageSection().minDamage(), event.getFinalDamage()));

        for (EntityDamageEvent.DamageModifier damageModifier : EntityDamageEvent.DamageModifier.values()) {
            if (damageModifier == EntityDamageEvent.DamageModifier.BASE) {
                continue;
            }
            try {
                event.setDamage(damageModifier, 0);
            } catch (UnsupportedOperationException ignored) {}
        }

        Map<EquipmentSlot, Double> equipmentDamages = getCombinedModifierValue(MechanicsTypes.EQUIPMENT_DAMAGE);
        Map<EquipmentSlot, Boolean> noEquipmentDamage = getCombinedModifierValue(MechanicsTypes.NO_EQUIPMENT_DAMAGE);
        for (Map.Entry<EquipmentSlot, Double> equipmentDamage : equipmentDamages.entrySet()) {
            if (noEquipmentDamage.getOrDefault(equipmentDamage.getKey(), false)) {
                continue;
            }
            ItemStack item = getOwner().unwrap().getEquipment().getItem(BukkitAdapter.toBukkitEquipmentSlot(equipmentDamage.getKey()));
            if (item.getType().isAir()) {
                continue;
            }
            ItemMeta meta = item.getItemMeta();
            if (!(meta instanceof Damageable)) {
                continue;
            }
            ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + equipmentDamage.getValue().intValue());
        }

        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "After: " + event.getFinalDamage());
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "");
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
