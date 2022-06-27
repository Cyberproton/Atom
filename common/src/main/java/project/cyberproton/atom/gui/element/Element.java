package project.cyberproton.atom.gui.element;

import project.cyberproton.atom.item.ItemStack;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.Nullable;
import java.util.Map;

public interface Element {
    @Nullable
    Map<Position, ItemStack> onBuild();
}
