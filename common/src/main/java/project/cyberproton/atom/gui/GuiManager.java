package project.cyberproton.atom.gui;

import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.plugin.PluginModule;
import project.cyberproton.atom.state.MutableStore;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public interface GuiManager<P extends AtomPlugin> extends PluginModule<P> {
    <G extends Gui<?, ?>> void show(@NotNull G gui, @NotNull Collection<UUID> viewers);

    default <G extends Gui<?, ?>> void show(@NotNull G activeGui, @NotNull UUID viewer) {
        Objects.requireNonNull(viewer, "viewer");
        show(activeGui, Collections.singletonList(viewer));
    }

    <G extends Gui<?, ?>> void close(@NotNull G gui);

    <G extends Gui<?, ?>> void close(@NotNull G gui, @NotNull Collection<UUID> viewers);

    default <G extends Gui<?, ?>> void close(@NotNull G gui, @NotNull UUID viewer) {
        Objects.requireNonNull(viewer, "viewer");
        close(gui, Collections.singletonList(viewer));
    }

    default <G extends Gui<?, ?>> void update(@NotNull G gui) {
        update(gui.getUniqueId());
    }

    void update(@NotNull UUID guiUUID);

    <G extends Gui<? extends FrameBuilder, ? extends FrameBuilder>> void registerHandler(Class<? extends G> cls, GuiHandler<G> handler);

    <G extends Gui<? extends FrameBuilder, ? extends FrameBuilder>> void unregisterHandler(Class<? extends G> cls);
}
