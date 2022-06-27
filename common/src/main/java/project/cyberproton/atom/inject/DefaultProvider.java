package project.cyberproton.atom.inject;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.function.Supplier;

public class DefaultProvider<T> implements Provider<T> {
    private final TypeToken<T> type;
    private final Supplier<T> instanceSupplier;

    public DefaultProvider(@NotNull TypeToken<T> type, @NotNull Supplier<T> instanceSupplier) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(instanceSupplier, "instanceSupplier");
        this.type = type;
        this.instanceSupplier = instanceSupplier;
    }

    @NotNull
    @Override
    public TypeToken<T> getType() {
        return type;
    }

    @NotNull
    @Override
    public Supplier<T> getInstanceSupplier() {
        return instanceSupplier;
    }
}
