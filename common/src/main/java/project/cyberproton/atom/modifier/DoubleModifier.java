package project.cyberproton.atom.modifier;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class DoubleModifier extends NumericModifier<Double> {
    protected DoubleModifier(@NotNull UUID uniqueId, @NotNull String name, @NotNull Double value, @NotNull NumericOperation operation) {
        super(uniqueId, name, value, operation);
    }

    @NotNull
    public static DoubleModifier of(@NotNull UUID uniqueId, @NotNull String name, double value, @NotNull NumericOperation operation) {
        Objects.requireNonNull(uniqueId, "uniqueId");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(operation, "operation");
        return new DoubleModifier(uniqueId, name, value, operation);
    }

    @NotNull
    public static DoubleModifier of(@NotNull UUID uniqueId, @NotNull String name, double value) {
        return of(uniqueId, name, value, NumericOperation.ADDITION);
    }

    @NotNull
    public static DoubleModifier of(@NotNull String name, double value, @NotNull NumericOperation operation) {
        return of(UUID.randomUUID(), name, value, operation);
    }

    @NotNull
    public static DoubleModifier of(@NotNull String name, double value) {
        return of(UUID.randomUUID(), name, value, NumericOperation.ADDITION);
    }

    @NotNull
    public static ExpiringModifier<DoubleModifier> expiring(@NotNull UUID uniqueId, @NotNull String name, double value, @NotNull NumericOperation operation, long duration) {
        return ExpiringModifier.expiring(of(uniqueId, name, value, operation), duration);
    }

    @NotNull
    public static ExpiringModifier<DoubleModifier> expiring(@NotNull String name, double value, @NotNull NumericOperation operation, long duration) {
        return expiring(UUID.randomUUID(), name, value, duration);
    }

    @NotNull
    public static ExpiringModifier<DoubleModifier> expiring(@NotNull UUID uniqueId, @NotNull String name, double value, long duration) {
        return expiring(uniqueId, name, value, NumericOperation.ADDITION, duration);
    }

    @NotNull
    public static ExpiringModifier<DoubleModifier> expiring(@NotNull String name, double value, long duration) {
        return expiring(name, value, NumericOperation.ADDITION, duration);
    }
}
