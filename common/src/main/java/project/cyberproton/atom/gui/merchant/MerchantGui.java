package project.cyberproton.atom.gui.merchant;

import project.cyberproton.atom.gui.AbstractGui;
import project.cyberproton.atom.gui.GuiBuilder;
import project.cyberproton.atom.gui.GuiManager;
import project.cyberproton.atom.gui.PlayerInventoryFrameBuilder;
import project.cyberproton.atom.state.ExpandableStore;

import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public abstract class MerchantGui extends AbstractGui<TopFrameMerchantBuilder, PlayerInventoryFrameBuilder> {
    protected MerchantGui(GuiManager<?> guiManager) {
        super(guiManager);
    }

    @Override
    public GuiBuilder<TopFrameMerchantBuilder, PlayerInventoryFrameBuilder> onBuild() {
        return null;
    }

    @NotNull
    @Override
    public TopFrameMerchantBuilder topFrame(Consumer<TopFrameMerchantBuilder> context) {
        return null;
    }

    @NotNull
    @Override
    public PlayerInventoryFrameBuilder bottomFrame(Consumer<PlayerInventoryFrameBuilder> context) {
        return null;
    }

    @Override
    public TopFrameMerchantBuilder topFrame() {
        return null;
    }

    @Override
    public PlayerInventoryFrameBuilder bottomFrame() {
        return null;
    }
}
