package project.cyberproton.atom.gui;

import project.cyberproton.atom.gui.element.Element;
import project.cyberproton.atom.gui.pane.Pane;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleSlotBuilder implements SlotBuilder {
    private final Position position;
    private Pane pane;

    public SimpleSlotBuilder(Position position) {
        this.position = position;
    }

    @NotNull
    @Override
    public Position position() {
        return position;
    }

    @Override
    public void component(@Nullable Pane pane) {
        this.pane = pane;
    }

    @Override
    @Nullable
    public Pane component() {
        return pane;
    }

    @Override
    public void element(@Nullable Element element) {
        this.pane = Pane.single(element);
    }
}
