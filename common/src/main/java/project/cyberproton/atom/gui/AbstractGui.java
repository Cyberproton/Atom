package project.cyberproton.atom.gui;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.state.*;
import project.cyberproton.atom.state.listener.StateListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractGui<T extends FrameBuilder, B extends FrameBuilder> implements Gui<T, B> {
    private final UUID uniqueId = UUID.randomUUID();
    protected final GuiManager<?> guiManager;
    protected final ExpandableStore store;
    private final StateListener listener = StateListener.all(this::onStateChange);

    protected AbstractGui(@NotNull GuiManager<?> guiManager) {
        Objects.requireNonNull(guiManager, "guiManager");
        this.guiManager = guiManager;
        store = Store.expandable();
        store.subscribe(listener);
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public GuiManager<?> guiManager() {
        return guiManager;
    }

    private void onStateChange(Map<TypedKey<?>, Change<?>> changes) {
        requestUpdate();
    }

    @Override
    public void requestUpdate() {
        guiManager.update(getUniqueId());
    }

    @Override
    public void requestRefresh() {

    }

    public GuiBuilder<T, B> gui(@Nullable T topFrameBuilder, @Nullable B bottomFrameBuilder) {
        return GuiBuilder.<T, B>create().topFrame(topFrameBuilder).bottomFrame(bottomFrameBuilder);
    }

    public <S> State<S> useState(TypeToken<S> type, S initialValue) {
        return store.stateOf(type, initialValue);
    }

    public <S> State<S> useState(Class<S> cls, S initialValue) {
        try {
            return useState(TypeToken.of(cls), initialValue);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
