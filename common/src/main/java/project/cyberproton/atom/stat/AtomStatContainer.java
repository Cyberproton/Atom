package project.cyberproton.atom.stat;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.Platform;
import project.cyberproton.atom.entity.IEntity;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.modifier.TemporaryModifier;
import project.cyberproton.atom.stat.combiner.StatCombiner;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AtomStatContainer<S extends Stat<M, V>, M extends Modifier<V>, V> extends AbstractStatContainer<S, M, V> {
    private final Map<UUID, M> modifiers = new HashMap<>();
    private final Map<UUID, Long> temporaryModifiersExpiredAt = new HashMap<>();

    public AtomStatContainer(@NotNull IEntity owner, @NotNull S stat) {
        super(owner, stat);
    }

    @NotNull
    @Override
    public Collection<M> getModifiers() {
        return new ArrayList<>(modifiers.values());
    }

    @Override
    public V getCombinedValue() {
        StatCombiner<M, V> merger = getStat().getCombiner();
        if (merger == null) {
            return null;
        }
        return merger.combine(modifiers.values());
    }

    @Override
    public void addModifier(@NotNull M modifier) {
        Objects.requireNonNull(modifier, "modifier");
        if (callStatAddEvent(modifier)) {
            modifiers.put(modifier.getUniqueId(), modifier);
        }
    }

    @Override
    public void addTemporaryModifier(@NotNull TemporaryModifier<M> temporaryModifier) {
        M modifier = temporaryModifier.getModifier();
        if (callStatAddEvent(modifier)) {
            temporaryModifiersExpiredAt.put(modifier.getUniqueId(), Platform.getCurrentTick() + temporaryModifier.getTicks());
        }
    }

    @Override
    public void removeModifier(@NotNull UUID id) {
        Objects.requireNonNull(id, "id");
        M m = modifiers.get(id);
        if (m == null) return;
        if (callStatRemoveEvent(m)) {
            modifiers.remove(id);
            temporaryModifiersExpiredAt.remove(id);
        }
    }

    @Override
    public void removeModifiersIf(@NotNull Predicate<M> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        Iterator<M> iterator = modifiers.values().iterator();
        while (iterator.hasNext()) {
            M modifier = iterator.next();
            if (!predicate.test(modifier)) {
                continue;
            }
            if (!callStatRemoveEvent(modifier)) {
                continue;
            }
            iterator.remove();
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

    public abstract boolean callStatAddEvent(@NotNull M modifier);

    public abstract boolean callStatRemoveEvent(@NotNull M modifier);
}
