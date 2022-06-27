package project.cyberproton.atom.gui;

import project.cyberproton.atom.gui.element.Element;
import project.cyberproton.atom.item.ItemStack;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utils {
    @NotNull
    public static Map<Position, ItemStack> fill(@NotNull ItemStack item, @NotNull Position from, @NotNull Position to) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Map<Position, ItemStack> items = new HashMap<>();
        int fromX = Math.min(from.x(), to.x());
        int toX = Math.max(from.x(), to.x());
        int fromY = Math.min(from.y(), to.y());
        int toY = Math.max(from.y(), to.y());
        for (int i = fromY; i <= toY; i++) {
            for (int j = fromX; j <= toX; j++) {
                items.put(Position.of(j, i), item);
            }
        }
        return items;
    }

    @NotNull
    public static Map<Position, Element> fill(@NotNull Element element, @NotNull Position from, @NotNull Position to) {
        Objects.requireNonNull(element, "element");
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Map<Position, Element> res = new HashMap<>();
        int fromX = Math.min(from.x(), to.x());
        int toX = Math.max(from.x(), to.x());
        int fromY = Math.min(from.y(), to.y());
        int toY = Math.max(from.y(), to.y());
        for (int i = fromY; i <= toY; i++) {
            for (int j = fromX; j <= toX; j++) {
                res.put(Position.of(j, i), element);
            }
        }
        return res;
    }
}
