package project.cyberproton.atom.modifier;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.UUID;

public class GenericModifier<T> implements Modifier<T> {
    private final String name;
    private final T value;
    private final UUID uniqueId;

    protected GenericModifier(@NotNull UUID uniqueId, @NotNull String name, @NotNull T value) {
        Objects.requireNonNull(uniqueId, "uniqueId");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");
        this.name = name;
        this.value = value;
        this.uniqueId = uniqueId;
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public T getValue() {
        return value;
    }
}
