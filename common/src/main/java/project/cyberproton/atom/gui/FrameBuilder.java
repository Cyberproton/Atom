package project.cyberproton.atom.gui;

import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.function.Consumer;

public interface FrameBuilder {
    @NotNull
    SlotBuilder slot(@NotNull Position position);

    @NotNull
    default SlotBuilder slot(int x, int y) {
        return slot(Position.of(x, y));
    }

    @NotNull
    List<SlotBuilder> slots();

    FrameBuilder slots(Consumer<FrameBuilder> context);
}
