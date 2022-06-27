package project.cyberproton.atom.gui.element;

import project.cyberproton.atom.gui.context.ClickContext;
import project.cyberproton.atom.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

public class Button extends Node implements Clickable {
    private final Consumer<ClickContext> anyClickHandler;
    private final Consumer<ClickContext> leftClickHandler;
    private final Consumer<ClickContext> shiftLeftClickHandler;
    private final Consumer<ClickContext> rightClickHandler;
    private final Consumer<ClickContext> shiftRightClickHandler;

    Button(ItemStack item, Consumer<ClickContext> anyClickHandler, Consumer<ClickContext> leftClickHandler, Consumer<ClickContext> shiftLeftClickHandler, Consumer<ClickContext> rightClickHandler, Consumer<ClickContext> shiftRightClickHandler) {
        super(item);
        this.anyClickHandler = anyClickHandler;
        this.leftClickHandler = leftClickHandler;
        this.shiftLeftClickHandler = shiftLeftClickHandler;
        this.rightClickHandler = rightClickHandler;
        this.shiftRightClickHandler = shiftRightClickHandler;
    }

    @Nullable
    @Override
    public Consumer<ClickContext> onAnyClickHandler() {
        return anyClickHandler;
    }

    @Nullable
    @Override
    public Consumer<ClickContext> onLeftClickHandler() {
        return leftClickHandler;
    }

    @Nullable
    @Override
    public Consumer<ClickContext> onRightClickHandler() {
        return rightClickHandler;
    }

    @Nullable
    @Override
    public Consumer<ClickContext> onShiftLeftClickHandler() {
        return shiftLeftClickHandler;
    }

    @Nullable
    @Override
    public Consumer<ClickContext> onShiftRightClickHandler() {
        return shiftRightClickHandler;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Consumer<ClickContext> anyClickHandler;
        private Consumer<ClickContext> leftClickHandler;
        private Consumer<ClickContext> shiftLeftClickHandler;
        private Consumer<ClickContext> rightClickHandler;
        private Consumer<ClickContext> shiftRightClickHandler;
        private ItemStack item;

        private Builder() {
        }

        @NotNull
        public static Builder create() {
            return new Builder();
        }

        @NotNull
        public Builder anyClickHandler(@Nullable Consumer<ClickContext> anyClickHandler) {
            this.anyClickHandler = anyClickHandler;
            return this;
        }

        @NotNull
        public Builder leftClickHandler(@Nullable Consumer<ClickContext> leftClickHandler) {
            this.leftClickHandler = leftClickHandler;
            return this;
        }

        @NotNull
        public Builder shiftLeftClickHandler(@Nullable Consumer<ClickContext> shiftLeftClickHandler) {
            this.shiftLeftClickHandler = shiftLeftClickHandler;
            return this;
        }

        @NotNull
        public Builder rightClickHandler(@Nullable Consumer<ClickContext> rightClickHandler) {
            this.rightClickHandler = rightClickHandler;
            return this;
        }

        @NotNull
        public Builder shiftRightClickHandler(@Nullable Consumer<ClickContext> shiftRightClickHandler) {
            this.shiftRightClickHandler = shiftRightClickHandler;
            return this;
        }

        @NotNull
        public Builder item(@Nullable ItemStack item) {
            this.item = item;
            return this;
        }

        @NotNull
        public Button build() {
            return new Button(item, anyClickHandler, leftClickHandler, shiftLeftClickHandler, rightClickHandler, shiftRightClickHandler);
        }
    }
}
