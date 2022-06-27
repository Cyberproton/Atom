package project.cyberproton.atom.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.Keys;
import project.cyberproton.atom.enchant.Enchant;
import project.cyberproton.atom.enchant.EnchantType;
import project.cyberproton.atom.state.KeyValue;
import project.cyberproton.atom.state.StoreConfiguration;
import project.cyberproton.atom.state.TypedKey;
import project.cyberproton.atom.state.Value;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultItemStackBuilder implements ItemStack.Builder {
    private ItemType type;
    private Component name;
    private String texture;
    private List<Component> lore;
    private List<Enchant> enchants;
    private int amount = 1;
    private boolean glow;
    private boolean hideEnchants;
    private boolean hideAttributes;
    private boolean hideCanDestroy;
    private boolean hideCanPlace;
    private boolean hideMiscellaneous;
    private boolean hideUnbreakable;
    private double damage = 0;
    private final Map<TypedKey<?>, Object> data = new HashMap<>();

    @NotNull
    @Override
    public ItemStack.Builder type(@NotNull ItemType type) {
        Objects.requireNonNull(type, "type");
        this.type = type;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder withDefaultType() {
        this.type = ItemType.DIAMOND;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder lore(@NotNull List<Component> lore) {
        this.lore = lore;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder enchants(@NotNull List<Enchant> enchants) {
        this.enchants = enchants;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder glow(boolean glow) {
        this.glow = glow;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder texture(String texture) {
        this.texture = texture;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder damage(double damage) {
        this.damage = damage;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder amount(int amount) {
        this.amount = amount;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder customName(@Nullable Component name) {
        this.name = name;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder hideEnchants(boolean hideEnchants) {
        this.hideEnchants = hideEnchants;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder hideAttributes(boolean hideAttributes) {
        this.hideAttributes = hideAttributes;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder hideCanDestroy(boolean hideCanDestroy) {
        this.hideCanDestroy = hideCanDestroy;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder hideCanPlace(boolean hideCanPlace) {
        this.hideCanPlace = hideCanPlace;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder hideMiscellaneous(boolean hideMiscellaneous) {
        this.hideMiscellaneous = hideMiscellaneous;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder hideUnbreakable(boolean hideUnbreakable) {
        this.hideUnbreakable = hideUnbreakable;
        return this;
    }

    @NotNull
    @Override
    public ItemStack.Builder hideAll() {
        hideEnchants(true);
        hideAttributes(true);
        hideCanDestroy(true);
        hideCanPlace(true);
        hideMiscellaneous(true);
        hideUnbreakable(true);
        return this;
    }

    @NotNull
    @Override
    public <V> ItemStack.Builder data(TypedKey<V> key, V value) {
        data.put(key, value);
        return this;
    }

    @NotNull
    @Override
    public ItemStack build() {
        Objects.requireNonNull(type, "type");
        validateAmount(amount);
        Integer alternativeAmount = ((Integer) data.get(Keys.AMOUNT));
        if (alternativeAmount != null) {
            validateAmount(alternativeAmount);
        }
        Component appliedName = this.name == null ? null : Component.empty().append(this.name).color(NamedTextColor.WHITE);
        List<Component> appliedLore = this.lore == null ? new ArrayList<>() : lore.stream().map(s -> Component.empty().append(s).color(NamedTextColor.GRAY)).collect(Collectors.toList());
        List<Enchant> appliedEnchants = this.enchants == null ? new ArrayList<>() : new ArrayList<>(this.enchants);
        boolean appliedHideEnchants = this.hideEnchants;
        if (glow && appliedEnchants.isEmpty()) {
            appliedEnchants.add(Enchant.of(EnchantType.DURABILITY, 1));
            appliedHideEnchants = true;
        }
        StoreConfiguration.Builder builder = StoreConfiguration
            .builder()
            .keyValues(Keys.ITEM_TYPE, Value.of(type))
            .keyValues(Keys.CUSTOM_NAME, Value.of(appliedName))
            .keyValues(Keys.LORE, Value.of(appliedLore))
            .keyValues(Keys.AMOUNT, Value.of(amount))
            .keyValues(Keys.TEXTURE, Value.of(texture))
            .keyValues(Keys.DAMAGE, Value.of(damage))
            .keyValues(Keys.ENCHANTS, Value.of(appliedEnchants))
            .keyValues(Keys.HIDE_ENCHANTS, Value.of(appliedHideEnchants))
            .keyValues(Keys.HIDE_ATTRIBUTES, Value.of(hideAttributes))
            .keyValues(Keys.HIDE_CAN_DESTROY, Value.of(hideCanDestroy))
            .keyValues(Keys.HIDE_CAN_PLACE, Value.of(hideCanPlace))
            .keyValues(Keys.HIDE_MISCELLANEOUS, Value.of(hideMiscellaneous))
            .keyValues(Keys.HIDE_UNBREAKABLE, Value.of(hideUnbreakable));
        for (Map.Entry<TypedKey<?>, Object> entry : data.entrySet()) {
            KeyValue<Object> keyValue = KeyValue.of((TypedKey<Object>) entry.getKey(), Value.of(entry.getValue()));
            builder.keyValues(keyValue);
        }
        return new DefaultItemStack(builder.build());
    }

    void validateAmount(int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must not lower than 1");
        }
    }
}
