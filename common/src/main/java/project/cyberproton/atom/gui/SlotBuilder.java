package project.cyberproton.atom.gui;

import project.cyberproton.atom.gui.element.Element;
import project.cyberproton.atom.gui.pane.Pane;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SlotBuilder {
    @NotNull
    Position position();

    void component(@Nullable Pane pane);

    void element(@Nullable Element element);

    @Nullable
    Pane component();
}
