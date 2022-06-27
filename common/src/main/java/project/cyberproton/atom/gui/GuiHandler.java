package project.cyberproton.atom.gui;

import project.cyberproton.atom.gui.context.ClickContext;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public interface GuiHandler<G extends Gui<? extends FrameBuilder, ? extends FrameBuilder>> {
    @NotNull
    Class<G> guiType();

    void show(ActiveGui<G> gui, Collection<UUID> viewers);

    void update(ActiveGui<G> gui);

    void click(ActiveGui<G> gui, UUID clicker, ClickContext context);

    void close(ActiveGui<G> gui, Collection<UUID> viewers);

    void close(ActiveGui<G> gui);

    default void close(ActiveGui<G> gui, UUID viewer) {
        close(gui, Collections.singletonList(viewer));
    }

    @NotNull
    Collection<UUID> viewersOf(ActiveGui<G> gui);
}
