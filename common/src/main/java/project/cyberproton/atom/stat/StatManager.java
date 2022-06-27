package project.cyberproton.atom.stat;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.plugin.AtomPluginModule;
import project.cyberproton.atom.modifier.Modifier;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.state.Key;

import java.util.Collection;

public interface StatManager extends AtomPluginModule {
    @NotNull
    Collection<Stat<?, ?>> getAllStats();

    @NotNull
    Collection<DoubleStat> getDoubleStats();

    @Nullable
    Stat<?, ?> getStat(@NotNull String name);

    @Nullable
    Stat<?, ?> getStat(@NotNull Key key);

    @Nullable
    DoubleStat getDoubleStat(@NotNull Key key);

    @Nullable
    <M extends Modifier<V>, V> Stat<M, V> getStatOfType(@NotNull Key key, @NotNull TypeToken<Stat<M, V>> statCls);

    @Nullable
    default <M extends Modifier<V>, V> Stat<M, V> getStatOfType(@NotNull Key key, @NotNull Class<Stat<M, V>> statCls) {
        return getStatOfType(key, TypeToken.of(statCls));
    }

    <M extends Modifier<V>, V> void registerStat(@NotNull Stat<M, V> stat);

    <M extends Modifier<V>, V> void unregisterStat(@NotNull Stat<M, V> stat);
}
