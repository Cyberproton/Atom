package project.cyberproton.atom.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import project.cyberproton.atom.config.ConfigUtils;
import project.cyberproton.atom.enchant.Enchant;
import project.cyberproton.atom.enchant.EnchantType;
import project.cyberproton.atom.util.Enums;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    @Nullable
    public static ItemStack loadFromNode(@NotNull ConfigurationNode node) {
        String material = node.node("material").getString("");
        ItemType type = Enums.valueOfOrNull(ItemType.class, material);
        if (type == null) {
            return null;
        }
        String nameRaw = node.node("name").getString();
        Component name = nameRaw == null ? null : LegacyComponentSerializer.legacyAmpersand().deserialize(nameRaw);
        List<String> loreRaw = ConfigUtils.getListOrElse(node.node("lore"), String.class, ConfigUtils.emptyListHandler());
        List<Component> lore = new ArrayList<>();
        for (String line : loreRaw) {
            lore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(line));
        }
        String texture = node.node("texture").getString();
        double damage = node.node("damage").getDouble();
        boolean glow = node.node("glow").getBoolean();
        List<String> enchantsRaw = ConfigUtils.getListOrEmpty(node.node("enchants"), String.class);
        List<Enchant> enchants = new ArrayList<>();
        for (String enchantRaw : enchantsRaw) {
            EnchantType enchantType = Enums.valueOfOrNull(EnchantType.class, enchantRaw);
            if (enchantType == null) {
                continue;
            }
            enchants.add(Enchant.of(enchantType, 1));
        }
        return ItemStack
                .builder()
                .type(type)
                .customName(name)
                .lore(lore)
                .enchants(enchants)
                .texture(texture)
                .damage(damage)
                .glow(glow)
                .build();
    }
}
