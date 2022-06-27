package project.cyberproton.atom.stat;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.Platform;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.state.Key;

import java.util.*;

public class AtomStatManager implements StatManager {
    private final UUID uuid = UUID.randomUUID();
    private final Map<Key, Stat<?, ?>> stats = new HashMap<>();
    private final List<DoubleStat> doubles = new ArrayList<>();

    public AtomStatManager() {

    }

    @Override
    public void onEnable() {
        onReload();
    }

    @Override
    public void onReload() {
        stats.clear();
        doubles.clear();
        for (Stat<?, ?> stat : Stats.all()) {
            stats.put(stat.getKey(), stat);
            if (stat instanceof DoubleStat) {
                doubles.add(((DoubleStat) stat));
            }
        }
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public @NotNull AtomPlugin getPlugin() {
        return Platform.getBootstrap();
    }

    @Override
    public @Nullable Stat<?, ?> getStat(@NotNull String name) {
        for (Stat<?, ?> stat : stats.values()) {
            if (stat.getId().equals(name)) {
                return stat;
            }
        }
        return null;
    }

    @Override
    public @Nullable Stat<?, ?> getStat(@NotNull Key key) {
        return stats.get(key);
    }

    @Override
    public @Nullable <M extends Modifier<V>, V> Stat<M, V> getStatOfType(@NotNull Key key, @NotNull TypeToken<Stat<M, V>> statCls) {
        Stat<?, ?> stat = stats.get(key);
        try {
            if (!statCls.getRawType().equals(stat.getClass()) || !statCls.resolveType(Stat.class.getTypeParameters()[0]).equals(stat.getModifierType()) || !statCls.resolveType(Stat.class.getTypeParameters()[1]).equals(stat.getDataType())) {
                return null;
            }
            return ((Stat<M, V>) statCls.getType());
        } catch (ClassCastException ex) {
            return null;
        }
    }

    @Override
    public @Nullable DoubleStat getDoubleStat(@NotNull Key key) {
        Stat<?, ?> stat = stats.get(key);
        if (!(stat instanceof DoubleStat)) {
            return null;
        }
        return ((DoubleStat) stat);
    }

    @NotNull
    @Override
    public Collection<Stat<?, ?>> getAllStats() {
        return new ArrayList<>(stats.values());
    }

    @NotNull
    @Override
    public Collection<DoubleStat> getDoubleStats() {
        return new ArrayList<>(doubles);
    }

    @Override
    public <M extends Modifier<V>, V> void registerStat(@NotNull Stat<M, V> stat) {
        if (stats.containsKey(stat.getKey())) {
            throw new IllegalStateException("Stat " + stat.getId() + " of type " + stat.getDataType() + " has already been registered");
        }
        stats.put(stat.getKey(), stat);
        if (stat instanceof DoubleStat) {
            doubles.add(((DoubleStat) stat));
        }
    }

    @Override
    public <M extends Modifier<V>, V> void unregisterStat(@NotNull Stat<M, V> stat) {
        stats.remove(stat.getKey());
        if (stat instanceof DoubleStat) {
            doubles.remove(((DoubleStat) stat));
        }
    }
}
