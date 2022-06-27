package project.cyberproton.atom.gui;

import project.cyberproton.atom.Identifiable;
import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.UUID;

public abstract class AbstractGuiHandler<G extends Gui<? extends FrameBuilder, ? extends FrameBuilder>> implements Identifiable {
    private final UUID uuid = UUID.randomUUID();
    protected final AtomPlugin plugin;
    protected final G gui;

    public AbstractGuiHandler(AtomPlugin plugin, G gui) {
        this.plugin = plugin;
        this.gui = gui;
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    public abstract void update();

    public abstract void refresh();

    public abstract void show(UUID viewer);

    public abstract void close(UUID viewer);

    @NotNull
    public abstract Collection<UUID> viewers();
}
