package project.cyberproton.atom.gui.context;

import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class ClickContext {
    private final UUID clicker;
    private final Position position;
    private final Object event;
    private final FramePosition framePosition;
    private final ClickType clickType;

    public ClickContext(UUID clicker, FramePosition framePosition, Position position, Object event, ClickType clickType) {
        this.clicker = clicker;
        this.framePosition = framePosition;
        this.position = position;
        this.event = event;
        this.clickType = clickType;
    }

    @NotNull
    public UUID getClicker() {
        return clicker;
    }

    @NotNull
    public FramePosition getFramePosition() {
        return framePosition;
    }

    @NotNull
    public ClickType getClickType() {
        return clickType;
    }

    @NotNull
    public Position getClickedPosition() {
        return position;
    }

    @Nullable
    public Object getEvent() {
        return event;
    }

    public enum FramePosition {
        TOP,
        BOTTOM,
    }

    public enum ClickType {
        LEFT,
        RIGHT,
        SHIFT_LEFT,
        SHIFT_RIGHT,
    }
}
