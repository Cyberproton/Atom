package project.cyberproton.atom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Proxy<T> {
    @Nullable
    T unwrapOrNull();

    @NotNull
    default T unwrap() {
        T wrapped = unwrapOrNull();
        if (wrapped == null) {
            throw new IllegalStateException("Target has been destroy or null");
        }
        return wrapped;
    }
}
