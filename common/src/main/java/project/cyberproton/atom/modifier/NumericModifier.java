package project.cyberproton.atom.modifier;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public abstract class NumericModifier<T extends Number> extends GenericModifier<T> {
    private final NumericOperation operation;

    protected NumericModifier(@NotNull UUID uniqueId, @NotNull String name, @NotNull T value, @NotNull NumericOperation operation) {
        super(uniqueId, name, value);
        Objects.requireNonNull(operation, "operation");
        this.operation = operation;
    }

    @NotNull
    public NumericOperation getOperation() {
        return operation;
    }
}
