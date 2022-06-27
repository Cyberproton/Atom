package project.cyberproton.atom.gui;

public class GuiBuilder<T extends FrameBuilder, B extends FrameBuilder> {
    private T top;
    private B bottom;

    private GuiBuilder() {}

    public GuiBuilder<T, B> topFrame(T topFrame) {
        this.top = topFrame;
        return this;
    }

    public GuiBuilder<T, B> bottomFrame(B bottomFrame) {
        this.bottom = bottomFrame;
        return this;
    }

    public T topFrame() {
        return top;
    }

    public B bottomFrame() {
        return bottom;
    }

    public static <T extends FrameBuilder, B extends FrameBuilder> GuiBuilder<T, B> create() {
        return new GuiBuilder<>();
    }
}
