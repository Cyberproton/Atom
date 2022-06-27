package project.cyberproton.atom.gui.element;

import project.cyberproton.atom.gui.context.ClickContext;

import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

public interface Clickable {
    @Nullable
    Consumer<ClickContext> onAnyClickHandler();

    @Nullable
    Consumer<ClickContext> onLeftClickHandler();

    @Nullable
    Consumer<ClickContext> onRightClickHandler();

    @Nullable
    Consumer<ClickContext> onShiftLeftClickHandler();

    @Nullable
    Consumer<ClickContext> onShiftRightClickHandler();
}
