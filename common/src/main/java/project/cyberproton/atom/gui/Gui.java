package project.cyberproton.atom.gui;

import project.cyberproton.atom.Identifiable;

import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public interface Gui<T extends FrameBuilder, B extends FrameBuilder> extends Identifiable {

    GuiManager<?> guiManager();

    default void onCreate() { }

    default void onDispose() { }

    GuiBuilder<T, B> onBuild();

    default void onRefresh() {
        onBuild();
    }

    void requestUpdate();

    void requestRefresh();

    T topFrame();

    B bottomFrame();

    @NotNull
    T topFrame(Consumer<T> context);

    @NotNull
    B bottomFrame(Consumer<B> context);

}
