package project.cyberproton.atom.gui.chest;

import project.cyberproton.atom.gui.*;

import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public class ChestGui extends AbstractGui<TopFrameChestBuilder, PlayerInventoryFrameBuilder> {
    public ChestGui(GuiManager<?> guiManager) {
        super(guiManager);
    }

    @Override
    public GuiBuilder<TopFrameChestBuilder, PlayerInventoryFrameBuilder> onBuild() {
        return gui(null, null);
    }

    @Override
    public TopFrameChestBuilder topFrame() {
        return TopFrameChestBuilder.create();
    }

    @NotNull
    @Override
    public TopFrameChestBuilder topFrame(Consumer<TopFrameChestBuilder> context) {
        TopFrameChestBuilder builder = TopFrameChestBuilder.create();
        context.accept(builder);
        return builder;
    }

    @NotNull
    @Override
    public PlayerInventoryFrameBuilder bottomFrame(Consumer<PlayerInventoryFrameBuilder> context) {
        PlayerInventoryFrameBuilder builder = PlayerInventoryFrameBuilder.create();
        context.accept(builder);
        return builder;
    }

    @Override
    public PlayerInventoryFrameBuilder bottomFrame() {
        return PlayerInventoryFrameBuilder.create();
    }
}
