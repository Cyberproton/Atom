package project.cyberproton.atom.gui.pane;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.state.*;
import project.cyberproton.atom.state.listener.StateListener;

import java.util.Map;

public abstract class AbstractPane implements Pane {
    private final MutableStore store = Store.expandable();
    private final StateListener listener = StateListener.all(this::onStateChange);

    protected <S> State<S> useState(TypeToken<S> type, S initialValue) {
        return store.stateOf(type, initialValue);
    }

    protected <S> State<S> useState(Class<S> cls, S initialValue) {
        try {
            return useState(TypeToken.of(cls), initialValue);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void onStateChange(Map<TypedKey<?>, Change<?>> changes) {

    }
}
