package project.cyberproton.atom.bukkit.stat;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import project.cyberproton.atom.Platform;
import project.cyberproton.atom.attribute.AttributeType;
import project.cyberproton.atom.bukkit.adapter.BukkitAdapter;
import project.cyberproton.atom.bukkit.entity.BukkitEntity;
import project.cyberproton.atom.bukkit.event.entity.EntityStatModifierAddEvent;
import project.cyberproton.atom.bukkit.event.entity.EntityStatRemoveModifierEvent;
import project.cyberproton.atom.modifier.NumericOperation;
import project.cyberproton.atom.stat.AbstractStatContainer;
import project.cyberproton.atom.stat.VanillaStat;
import project.cyberproton.atom.modifier.DoubleModifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.modifier.TemporaryModifier;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BukkitVanillaStatContainer extends AbstractStatContainer<VanillaStat, DoubleModifier, Double> {
    private final BukkitEntity<?> entity;
    private final VanillaStat stat;
    private final AttributeType attributeType;
    private final Attribute bukkitAttribute;
    private final Map<UUID, Long> temporaryModifiersExpiredAt = new HashMap<>();

    public BukkitVanillaStatContainer(@NotNull BukkitEntity<?> entity, @NotNull VanillaStat stat) {
        super(entity, stat);
        this.entity = entity;
        this.stat = stat;
        this.attributeType = stat.getAttributeType();
        this.bukkitAttribute = Attribute.valueOf(attributeType.name());
    }

    @NotNull
    @Override
    public VanillaStat getStat() {
        return stat;
    }

    @NotNull
    public AttributeType getAttributeType() {
        return attributeType;
    }

    @NotNull
    public Attribute getBukkitAttribute() {
        return bukkitAttribute;
    }

    @NotNull
    @Override
    public Collection<DoubleModifier> getModifiers() {
        List<DoubleModifier> modifiers = new ArrayList<>();
        for (AttributeModifier modifier : entity.unwrap().getAttribute(bukkitAttribute).getModifiers()) {
            modifiers.add(BukkitAdapter.toAtomModifier(modifier));
        }
        return modifiers;
    }

    @Nullable
    @Override
    public Double getCombinedValue() {
        double x = getValue(NumericOperation.ADDITION);
        double total = x;
        total += x * getValue(NumericOperation.MULTIPLY_BASE);
        total *= (1 + getValue(NumericOperation.MULTIPLY_TOTAL));
        return total;
    }

    public double getValue(@NotNull NumericOperation operation) {
        Objects.requireNonNull(operation, "operation");
        AttributeModifier.Operation bukkitOperation = BukkitAdapter.toBukkitModifierOperation(operation);
        double value = 0.0;
        for (AttributeModifier modifier : entity.unwrap().getAttribute(bukkitAttribute).getModifiers()) {
            if (modifier.getOperation() != bukkitOperation) {
                continue;
            }
            value += modifier.getAmount();
        }
        return value;
    }

    @Override
    public void addModifier(@NotNull DoubleModifier modifier) {
        Objects.requireNonNull(modifier, "modifier");
        EntityStatModifierAddEvent event = new EntityStatModifierAddEvent(entity, stat, modifier);
        event.callEvent();
        if (!event.isCancelled()) {
            entity.unwrap().getAttribute(bukkitAttribute).addModifier(BukkitAdapter.toBukkitModifier(modifier));
        }
    }

    @Override
    public void addTemporaryModifier(@NotNull TemporaryModifier<DoubleModifier> temporaryModifier) {
        DoubleModifier modifier = temporaryModifier.getModifier();
        EntityStatModifierAddEvent event = new EntityStatModifierAddEvent(entity, stat, modifier);
        event.callEvent();
        if (!event.isCancelled()) {
            entity.unwrap().getAttribute(bukkitAttribute).addModifier(BukkitAdapter.toBukkitModifier(modifier));
            temporaryModifiersExpiredAt.put(modifier.getUniqueId(), Platform.getCurrentTick() + temporaryModifier.getTicks());
        }
    }

    @Override
    public void removeModifier(@NotNull UUID id) {
        Objects.requireNonNull(id, "id");
        for (AttributeModifier modifier : entity.unwrap().getAttribute(bukkitAttribute).getModifiers()) {
            if (!modifier.getUniqueId().equals(id)) {
                continue;
            }
            EntityStatRemoveModifierEvent event = new EntityStatRemoveModifierEvent(entity, stat, BukkitAdapter.toAtomModifier(modifier));
            event.callEvent();
            if (!event.isCancelled()) {
                entity.unwrap().getAttribute(bukkitAttribute).removeModifier(modifier);
                temporaryModifiersExpiredAt.remove(id);
            }
            break;
        }
    }

    @Override
    public void removeModifiersIf(@NotNull Predicate<DoubleModifier> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        for (AttributeModifier modifier : entity.unwrap().getAttribute(bukkitAttribute).getModifiers()) {
            DoubleModifier mod = BukkitAdapter.toAtomModifier(modifier);
            if (!predicate.test(mod)) {
                continue;
            }
            EntityStatRemoveModifierEvent event = new EntityStatRemoveModifierEvent(entity, stat, BukkitAdapter.toAtomModifier(modifier));
            event.callEvent();
            if (!event.isCancelled()) {
                entity.unwrap().getAttribute(bukkitAttribute).removeModifier(modifier);
                temporaryModifiersExpiredAt.remove(modifier.getUniqueId());
            }
        }
    }

    @Override
    public void clean() {
        long currentTick = Platform.getCurrentTick();
        temporaryModifiersExpiredAt.entrySet()
            .stream()
            .filter(e -> e.getValue() > currentTick).collect(Collectors.toList())
            .forEach(e -> removeModifier(e.getKey()));
    }
}
