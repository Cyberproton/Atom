package project.cyberproton.atom.entity;

import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.stat.StatContainer;
import project.cyberproton.atom.modifier.Modifier;

import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractStatModule<T extends IEntity> implements IStatModule<T> {
    protected final Map<Stat<?, ?>, StatContainer<?, ?, ?>> containers = new ConcurrentHashMap<>();
    private final T owner;
    private final AtomPlugin plugin;
    private final Set<Option> options;

    public AbstractStatModule(@NotNull AtomPlugin plugin, @NotNull T owner, @NotNull Set<Option> options) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(options, "options");
        this.owner = owner;
        this.plugin = plugin;
        this.options = options;
    }

    @Override
    public @NotNull Collection<StatContainer<?, ?, ?>> getContainers() {
        return new ArrayList<>(containers.values());
    }

    @Override
    public abstract <S extends Stat<M, V>, M extends Modifier<V>, V> @NotNull StatContainer<S, M, V> getContainer(@NotNull S stat);

    @Override
    public <S extends Stat<M, V>, M extends Modifier<V>, V> V getValue(@NotNull S stat) {
        Objects.requireNonNull(stat, "stat");
        return getContainer(stat).getCombinedValue();
    }

    @NotNull
    @Override
    public UUID getUUID() {
        return EntityModules.ModuleId.STAT;
    }

    @NotNull
    @Override
    public AtomPlugin getPlugin() {
        return plugin;
    }

    @NotNull
    @Override
    public T getOwner() {
        return owner;
    }

    @NotNull
    @Override
    public Set<Option> getOptions() {
        return options;
    }
}
