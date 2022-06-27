package project.cyberproton.atom.gui.state;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.gui.Gui;
import project.cyberproton.atom.promise.Promise;
import project.cyberproton.atom.state.MutableStore;
import project.cyberproton.atom.state.State;
import project.cyberproton.atom.state.TypedKey;
import project.cyberproton.atom.state.Update;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;

public interface MutableStoreBinding {
    @NotNull
    Gui<?, ?> boundGui();

    @NotNull
    MutableStore boundStore();

    void unbind();

    default void update(Collection<Update<?>> updates) {
        boundStore().update(updates);
    }

    default void updateSync(@NotNull Collection<Update<?>> updates) {
        boundStore().updateSync(updates);
    }

    default Promise<Void> updateAsync(@NotNull Collection<Update<?>> updates) {
        return boundStore().updateAsync(updates);
    }

    @NotNull
    <T> State<T> stateOf(@NotNull TypeToken<T> type, @Nullable T initialState);

    @NotNull
    <T> State<T> stateOf(TypedKey<T> key, @Nullable T initialState);
}
