package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;

public interface ExpandableStore extends MutableStore {
    <T> boolean remove(@NotNull TypedKey<T> key);

    void clear();
}
