package project.cyberproton.atom.gui;

import project.cyberproton.atom.Identifiable;

import java.util.UUID;

public interface ActiveGui<G extends Gui<? extends FrameBuilder, ? extends FrameBuilder>> extends Identifiable {
    G gui();

    void show(UUID viewerUUID);

    void close();
}
