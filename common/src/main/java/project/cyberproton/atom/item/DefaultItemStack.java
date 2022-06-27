package project.cyberproton.atom.item;

import project.cyberproton.atom.Keys;
import project.cyberproton.atom.enchant.Enchant;
import project.cyberproton.atom.state.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultItemStack extends ImmutableStore implements ItemStack {
    DefaultItemStack(@NotNull ItemType type, int amount) {
        super(getStoreConfiguration(type, amount));
    }

    DefaultItemStack(@NotNull StoreConfiguration configuration) {
        super(configuration);
    }

    @NotNull
    @Override
    public ItemType type() {
        return getUnboxed(Keys.ITEM_TYPE);
    }

    @Override
    public int amount() {
        return getUnboxed(Keys.AMOUNT);
    }

    @NotNull
    @Override
    public <V> ItemStack withData(@NotNull TypedKey<V> key, @Nullable V data) {
        Objects.requireNonNull(key, "key");
        List<Enchant> enchants = getUnboxedOrNull(Keys.ENCHANTS);
        if (enchants == null) enchants = new ArrayList<>();
        return ItemStack.builder()
            .type(type())
            .amount(amount())
            .lore(getUnboxedOrNull(Keys.LORE))
            .customName(getUnboxedOrNull(Keys.CUSTOM_NAME))
            .enchants(enchants)
            .texture(getUnboxedOrNull(Keys.TEXTURE))
            .hideEnchants(getUnboxedOrElse(Keys.HIDE_ENCHANTS, false))
            .hideAttributes(getUnboxedOrElse(Keys.HIDE_ATTRIBUTES, false))
            .hideUnbreakable(getUnboxedOrElse(Keys.HIDE_UNBREAKABLE, false))
            .hideCanDestroy(getUnboxedOrElse(Keys.HIDE_CAN_DESTROY, false))
            .hideCanPlace(getUnboxedOrElse(Keys.HIDE_CAN_PLACE, false))
            .hideMiscellaneous(getUnboxedOrElse(Keys.HIDE_MISCELLANEOUS, false))
            .data(key, data)
            .build();
    }

    private static StoreConfiguration getStoreConfiguration(ItemType type, int amount) {
        return StoreConfiguration.builder().keyValues(
                KeyValue.of(Keys.ITEM_TYPE, Value.of(type)),
                KeyValue.of(Keys.AMOUNT, Value.of(amount))
        ).build();
    }
}
