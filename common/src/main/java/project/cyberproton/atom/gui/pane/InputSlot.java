package project.cyberproton.atom.gui.pane;

import com.google.common.collect.ImmutableMap;
import project.cyberproton.atom.gui.context.InputContext;
import project.cyberproton.atom.gui.element.Element;
import project.cyberproton.atom.gui.element.Inputtable;
import project.cyberproton.atom.gui.element.Node;
import project.cyberproton.atom.item.ItemStack;
import project.cyberproton.atom.state.State;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class InputSlot extends AbstractPane implements Inputtable {
    private final State<ItemStack> holdingItem;
    private final ItemStack background;

    public InputSlot(@NotNull State<ItemStack> holdingItem, @Nullable ItemStack background) {
        Objects.requireNonNull(holdingItem, "holdingItem");
        this.holdingItem = holdingItem;
        this.background = background;
    }

    @Override
    public void onInput(InputContext context) {

    }

    @Nullable
    @Override
    public Map<Position, Element> onBuild() {
        ItemStack item = holdingItem.getUnboxedOrNull();
        return ImmutableMap.of(Position.zero(), Node.of(item == null ? background : item));
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return null;
    }
}
