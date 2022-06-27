package project.cyberproton.atom.config.serializer;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import project.cyberproton.atom.item.ItemStack;
import project.cyberproton.atom.item.ItemUtils;

import java.lang.reflect.Type;

public class ItemStackSerializer implements TypeSerializer<ItemStack> {
    @Override
    public ItemStack deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return ItemUtils.loadFromNode(node);
    }

    @Override
    public void serialize(Type type, @Nullable ItemStack obj, ConfigurationNode node) throws SerializationException {

    }
}
