package project.cyberproton.atom.modifier;

import project.cyberproton.atom.Identifiable;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public interface Modifier<T> extends Identifiable {
    static <T> Modifier<T> of(@NotNull UUID uuid, @NotNull String name, @NotNull T value) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");
        return new GenericModifier<>(UUID.randomUUID(), name, value);
    }

    static <T> Modifier<T> of(@NotNull String name, @NotNull T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");
        return new GenericModifier<>(UUID.randomUUID(), name, value);
    }

    @NotNull
    String getName();

    @NotNull
    T getValue();
}
