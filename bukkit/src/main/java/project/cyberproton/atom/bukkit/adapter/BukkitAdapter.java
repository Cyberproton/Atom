package project.cyberproton.atom.bukkit.adapter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.Keys;
import project.cyberproton.atom.enchant.Enchant;
import project.cyberproton.atom.enchant.EnchantType;
import project.cyberproton.atom.item.ItemStack;
import project.cyberproton.atom.item.ItemType;
import project.cyberproton.atom.modifier.NumericOperation;
import project.cyberproton.atom.potion.PotionEffectType;
import project.cyberproton.atom.modifier.DoubleModifier;
import project.cyberproton.atom.slot.EquipmentSlot;
import project.cyberproton.atom.util.Enums;
import project.cyberproton.atom.world.Location;
import project.cyberproton.atom.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BukkitAdapter {
    @NotNull
    public static AttributeModifier.Operation toBukkitModifierOperation(@NotNull NumericOperation operation) {
        Objects.requireNonNull(operation, "operation");
        switch (operation) {
            case ADDITION:
                return AttributeModifier.Operation.ADD_NUMBER;
            case MULTIPLY_BASE:
                return AttributeModifier.Operation.ADD_SCALAR;
            case MULTIPLY_TOTAL:
                return AttributeModifier.Operation.MULTIPLY_SCALAR_1;
            default:
                throw new IllegalStateException("Cannot convert atom operation " + operation.name() + " to bukkit counterpart");
        }
    }

    @NotNull
    public static NumericOperation toAtomModifierOperation(@NotNull AttributeModifier.Operation operation) {
        Objects.requireNonNull(operation, "operation");
        switch (operation) {
            case ADD_NUMBER:
                return NumericOperation.ADDITION;
            case ADD_SCALAR:
                return NumericOperation.MULTIPLY_BASE;
            case MULTIPLY_SCALAR_1:
                return NumericOperation.MULTIPLY_TOTAL;
            default:
                throw new IllegalStateException("Cannot convert bukkit operation " + operation.name() + " to atom counterpart");
        }
    }

    @NotNull
    public static AttributeModifier toBukkitModifier(@NotNull DoubleModifier modifier) {
        Objects.requireNonNull(modifier, "modifier");
        return new AttributeModifier(
                modifier.getUniqueId(),
                modifier.getName(),
                modifier.getValue(),
                toBukkitModifierOperation(modifier.getOperation())
        );
    }

    @NotNull
    public static DoubleModifier toAtomModifier(@NotNull AttributeModifier modifier) {
        Objects.requireNonNull(modifier, "modifier");
        return DoubleModifier.of(
                modifier.getUniqueId(),
                modifier.getName(),
                modifier.getAmount(),
                toAtomModifierOperation(modifier.getOperation())
        );
    }

    @NotNull
    public static org.bukkit.Location toBukkitLocation(@NotNull Location location) {
        Objects.requireNonNull(location, "location");
        return new org.bukkit.Location(toBukkitWorld(location.getWorld()), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @NotNull
    public static Location toAtomLocation(@NotNull org.bukkit.Location location) {
        Objects.requireNonNull(location, "location");
        return Location.of(toAtomWorld(location.getWorld()), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Nullable
    public static org.bukkit.World toBukkitWorld(@NotNull World world) {
        Objects.requireNonNull(world, "world");
        return Bukkit.getWorld(world.getUniqueId());
    }

    @Nullable
    public static World toAtomWorld(@NotNull org.bukkit.World world) {
        Objects.requireNonNull(world, "world");
        return Atom.getInstance().getWorldManager().getWorld(world.getUID());
    }

    @Nullable
    public static Material toBukkitMaterial(@NotNull ItemType type) {
        return Material.getMaterial(type.name(), false);
    }

    @Nullable
    public static ItemType toAtomItemType(@NotNull Material material) {
        return Enums.valueOfOrNull(ItemType.class, material.name());
    }

    @Nullable
    public static EquipmentSlot toAtomEquipmentSlot(@NotNull org.bukkit.inventory.EquipmentSlot bukkitEquipmentSlot) {
        return Enums.valueOfOrNull(EquipmentSlot.class, bukkitEquipmentSlot.name());
    }

    @Nullable
    public static org.bukkit.inventory.EquipmentSlot toBukkitEquipmentSlot(@NotNull EquipmentSlot equipmentSlot) {
        return Enums.valueOfOrNull(org.bukkit.inventory.EquipmentSlot.class, equipmentSlot.name());
    }

    @Nullable
    public static org.bukkit.potion.PotionEffectType toBukkitPotionEffectType(PotionEffectType type) {
        return org.bukkit.potion.PotionEffectType.getByName(type.name());
    }

    @Nullable
    public static org.bukkit.inventory.ItemStack toBukkitItemStack(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "itemStack");
        Material material = toBukkitMaterial(itemStack.type());
        if (material == null) return null;
        org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(material, itemStack.amount());
        ItemMeta meta = item.getItemMeta();
        Component customName = itemStack.getUnboxedOrNull(Keys.CUSTOM_NAME);
        if (customName != null) {
            meta.setDisplayName(LegacyComponentSerializer.legacySection().serialize(customName));
        }
        List<Component> lore = itemStack.getUnboxedOrNull(Keys.LORE);
        if (lore != null) {
            List<String> legacyLore = new ArrayList<>();
            for (Component component : lore) {
                legacyLore.add(LegacyComponentSerializer.legacySection().serialize(component));
            }
            meta.setLore(legacyLore);
        }
        List<Enchant> enchants = itemStack.getUnboxedOrNull(Keys.ENCHANTS);
        if (enchants != null) {
            for (Enchant enchant : enchants) {
                Enchantment bukkitEnchant = toBukkitEnchantType(enchant.type());
                if (bukkitEnchant == null) {
                    continue;
                }
                meta.addEnchant(bukkitEnchant, enchant.level(), true);
            }
        }
        if (Boolean.TRUE.equals(itemStack.getUnboxedOrNull(Keys.HIDE_ENCHANTS))) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (Boolean.TRUE.equals(itemStack.getUnboxedOrNull(Keys.HIDE_UNBREAKABLE))) {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        item.setItemMeta(meta);
        return item;
    }

    @Nullable
    public static ItemStack toAtomItemStack(@NotNull org.bukkit.inventory.ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "itemStack");
        ItemType itemType = toAtomItemType(itemStack.getType());
        if (itemType == null) return null;
        ItemMeta meta = itemStack.getItemMeta();
        List<Enchant> enchants = new ArrayList<>();
        if (meta.hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> bukkitEnchant : meta.getEnchants().entrySet()) {
                enchants.add(Enchant.of(toAtomEnchantType(bukkitEnchant.getKey()), bukkitEnchant.getValue()));
            }
        }
        return ItemStack.builder()
            .type(itemType)
            .customName(meta.hasDisplayName() ? LegacyComponentSerializer.legacySection().deserialize(meta.getDisplayName()) : null)
            .lore(meta.hasLore() ? meta.getLore().stream().map(line -> LegacyComponentSerializer.legacySection().deserialize(line)).collect(Collectors.toList()) : null)
            .hideAttributes(meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES))
            .hideEnchants(meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
            .hideUnbreakable(meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE))
            .hideCanDestroy(meta.hasItemFlag(ItemFlag.HIDE_DESTROYS))
            .hideCanPlace(meta.hasItemFlag(ItemFlag.HIDE_PLACED_ON))
            .hideMiscellaneous(meta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS))
            .enchants(enchants)
            .build();
    }

    @Nullable
    public static Enchantment toBukkitEnchantType(@NotNull EnchantType enchantType) {
        Objects.requireNonNull(enchantType, "enchantType");
        return Enchantment.getByName(enchantType.name());
    }

    @Nullable
    public static EnchantType toAtomEnchantType(@NotNull Enchantment enchantType) {
        Objects.requireNonNull(enchantType, "enchantType");
        return Enums.valueOfOrNull(EnchantType.class, enchantType.getName());
    }
}
