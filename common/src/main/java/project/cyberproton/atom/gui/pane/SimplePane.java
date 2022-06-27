package project.cyberproton.atom.gui.pane;

import project.cyberproton.atom.gui.element.Element;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.UUID;

public class SimplePane implements Pane {
    private final UUID uuid = UUID.randomUUID();

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Nullable
    @Override
    public Map<Position, Element> onBuild() {
        return null;
    }
}
