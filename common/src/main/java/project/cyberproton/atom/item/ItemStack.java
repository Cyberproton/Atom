package project.cyberproton.atom.item;

import net.kyori.adventure.text.Component;
import project.cyberproton.atom.Keys;
import project.cyberproton.atom.enchant.Enchant;
import project.cyberproton.atom.state.Store;
import project.cyberproton.atom.state.TypedKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface ItemStack extends Store {
    @NotNull
    static ItemStack ofDefault() {
        return builder().type(ItemType.DIAMOND).build();
    }

    @NotNull
    static ItemStack ofEmpty(@NotNull ItemType type) {
        Objects.requireNonNull(type, "type");
        return builder().type(type).customName(Component.empty()).build();
    }

    @NotNull
    static Builder builder() {
        return new DefaultItemStackBuilder();
    }

    @NotNull
    static Builder builder(@NotNull ItemStack item) {
        Objects.requireNonNull(item, "item");
        Builder builder = builder();
        builder
            .type(item.type())
            .amount(item.amount())
            .lore(item.getUnboxedOrNull(Keys.LORE))
            .customName(item.getUnboxedOrNull(Keys.CUSTOM_NAME))
            .enchants(item.getUnboxedOrSupply(Keys.ENCHANTS, ArrayList::new))
            .texture(item.getUnboxedOrNull(Keys.TEXTURE))
            .hideEnchants(item.getUnboxedOrElse(Keys.HIDE_ENCHANTS, false))
            .hideAttributes(item.getUnboxedOrElse(Keys.HIDE_ATTRIBUTES, false))
            .hideUnbreakable(item.getUnboxedOrElse(Keys.HIDE_UNBREAKABLE, false))
            .hideCanDestroy(item.getUnboxedOrElse(Keys.HIDE_CAN_DESTROY, false))
            .hideCanPlace(item.getUnboxedOrElse(Keys.HIDE_CAN_PLACE, false))
            .hideMiscellaneous(item.getUnboxedOrElse(Keys.HIDE_MISCELLANEOUS, false))
            .build();
        return builder;
    }

    @NotNull
    static ItemStack of(@NotNull ItemType type, int amount) {
        return new DefaultItemStack(type, amount);
    }

    @NotNull
    static ItemStack of(@NotNull ItemType type) {
        return new DefaultItemStack(type, 1);
    }

    @NotNull
    ItemType type();

    int amount();

    @NotNull
    default ItemStack withAmount(int amount) {
        return ItemStack.of(type(), amount);
    }

    @NotNull
    <V> ItemStack withData(@NotNull TypedKey<V> key, @Nullable V data);

    interface Builder {
        @NotNull
        Builder type(@NotNull ItemType type);

        @NotNull
        Builder withDefaultType();

        @NotNull
        Builder lore(@Nullable List<Component> lore);

        @NotNull
        default Builder lore(@NotNull Component... lore) {
            return lore(Arrays.asList(lore));
        }

        @NotNull
        Builder enchants(@NotNull List<Enchant> enchants);

        @NotNull
        default Builder enchants(@NotNull Enchant... enchants) {
            return enchants(Arrays.asList(enchants));
        }

        @NotNull
        Builder glow(boolean glow);

        @NotNull
        Builder texture(String texture);

        @NotNull
        Builder damage(double damage);

        @NotNull
        Builder amount(int amount);

        @NotNull
        Builder customName(@Nullable Component name);

        @NotNull
        Builder hideEnchants(boolean hideEnchants);

        @NotNull
        Builder hideAttributes(boolean hideAttributes);

        @NotNull
        Builder hideCanDestroy(boolean hideCanDestroy);

        @NotNull
        Builder hideCanPlace(boolean hideCanPlace);

        @NotNull
        Builder hideMiscellaneous(boolean hideMiscellaneous);

        @NotNull
        Builder hideUnbreakable(boolean hideUnbreakable);

        @NotNull
        Builder hideAll();

        @NotNull
        <V> Builder data(TypedKey<V> key, V value);

        @NotNull
        ItemStack build();
    }
}
