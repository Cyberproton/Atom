package project.cyberproton.atom.inject;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import java.util.function.Supplier;

public interface Provider<T> {
    static <T> Provider<T> of(@NotNull TypeToken<T> cls, @NotNull Supplier<T> instanceSupplier) {
        return new DefaultProvider<>(cls, instanceSupplier);
    }

    @NotNull
    TypeToken<T> getType();

    @NotNull
    Supplier<T> getInstanceSupplier();
}
