package project.cyberproton.atom.gui;

import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractFrameBuilder implements FrameBuilder {
    private final List<SlotBuilder> slotBuilders = new ArrayList<>();

    @NotNull
    @Override
    public SlotBuilder slot(@NotNull Position position) {
        SimpleSlotBuilder slotBuilder = new SimpleSlotBuilder(position);
        slotBuilders.add(slotBuilder);
        return slotBuilder;
    }

    @Override
    @NotNull
    public List<SlotBuilder> slots() {
        return slotBuilders;
    }

    @Override
    public FrameBuilder slots(Consumer<FrameBuilder> context) {
        context.accept(this);
        return this;
    }
}
