package project.cyberproton.atom;

import com.google.common.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import project.cyberproton.atom.enchant.Enchant;
import project.cyberproton.atom.entity.EntityType;
import project.cyberproton.atom.item.ItemType;
import project.cyberproton.atom.state.Key;
import project.cyberproton.atom.state.TypedKey;
import project.cyberproton.atom.world.Location;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class Keys {
    public static final TypedKey<Double> HEALTH = TypedKey.of(Key.minecraft("health"), Double.class);
    public static final TypedKey<Location> LOCATION = TypedKey.of(Key.minecraft("location"), Location.class);
    public static final TypedKey<String> DISPLAY_NAME = TypedKey.of(Key.minecraft("display-name"), String.class);

    public static final TypedKey<ItemType> ITEM_TYPE = TypedKey.of(Key.minecraft("item-type"), ItemType.class);
    public static final TypedKey<Integer> AMOUNT = TypedKey.of(Key.minecraft("amount"), Integer.class);
    public static final TypedKey<String> TEXTURE = TypedKey.of(Key.atom("texture"), String.class);
    public static final TypedKey<Double> DAMAGE = TypedKey.of(Key.minecraft("damage"), Double.class);
    public static final TypedKey<Component> CUSTOM_NAME = TypedKey.of(Key.minecraft("custom-name"), Component.class);
    public static final TypedKey<List<Component>> LORE = TypedKey.of(Key.minecraft("lore"), new TypeToken<List<Component>>() {});
    public static final TypedKey<List<Enchant>> ENCHANTS = TypedKey.of(Key.minecraft("enchants"), new TypeToken<List<Enchant>>() {});
    public static final TypedKey<Boolean> HIDE_ENCHANTS = TypedKey.of(Key.minecraft("hide-enchants"), Boolean.class);
    public static final TypedKey<Boolean> HIDE_ATTRIBUTES = TypedKey.of(Key.minecraft("hide-attributes"), Boolean.class);
    public static final TypedKey<Boolean> HIDE_CAN_DESTROY = TypedKey.of(Key.minecraft("hide-can-destroy"), Boolean.class);
    public static final TypedKey<Boolean> HIDE_CAN_PLACE = TypedKey.of(Key.minecraft("hide-can-place"), Boolean.class);
    public static final TypedKey<Boolean> HIDE_MISCELLANEOUS = TypedKey.of(Key.minecraft("hide-miscellaneous"), Boolean.class);
    public static final TypedKey<Boolean> HIDE_UNBREAKABLE = TypedKey.of(Key.minecraft("hide-unbreakable"), Boolean.class);

    public static final TypedKey<EntityType<?>> ENTITY_TYPE = TypedKey.of(Key.minecraft("entity-type"), new TypeToken<EntityType<?>>() {});
}
