package project.cyberproton.atom.gui.element;

import com.google.common.collect.ImmutableMap;
import project.cyberproton.atom.item.ItemStack;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.Nullable;
import java.util.Map;

public class Node implements Element {
    private final ItemStack item;

    protected Node(@Nullable ItemStack item) {
        this.item = item == null ? ItemStack.ofDefault() : item;
    }

    @Nullable
    @Override
    public Map<Position, ItemStack> onBuild() {
        return ImmutableMap.of(Position.zero(), item);
    }

    public static Node of(ItemStack item) {
        return new Node(item);
    }
}
