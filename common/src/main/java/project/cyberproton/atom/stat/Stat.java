package project.cyberproton.atom.stat;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.stat.combiner.StatCombiner;
import project.cyberproton.atom.stat.loader.StatLoader;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.state.Keyed;

@SuppressWarnings("UnstableApiUsage")
public interface Stat<M extends Modifier<V>, V> extends Keyed {
    @NotNull
    TypeToken<V> getDataType();

    @NotNull
    TypeToken<M> getModifierType();

    @NotNull
    String getId();

    @NotNull
    String getNamespace();

    @Nullable
    default StatCombiner<M, V> getCombiner() {
        return null;
    }

    @Nullable
    default StatLoader<V> getLoader() {
        return null;
    }
}


