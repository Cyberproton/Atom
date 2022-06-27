package project.cyberproton.atom.stat;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.stat.combiner.StatCombiner;
import project.cyberproton.atom.stat.loader.StatLoader;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.state.Key;
import project.cyberproton.atom.state.Keyed;

import java.util.Objects;

public class AbstractStat<M extends Modifier<V>, V> implements Stat<M, V>, Keyed {
    private final String id;
    private final String namespace;
    private final TypeToken<M> modifierType;
    private final TypeToken<V> dataType;
    private final Key key;

    public AbstractStat(@NotNull String namespace, @NotNull String id, TypeToken<M> modifierType, TypeToken<V> dataType) {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(id, "id");
        this.id = id;
        this.namespace = namespace;
        this.modifierType = modifierType;
        this.dataType = dataType;
        this.key = Key.of(namespace, id + "-" + this.dataType.toString());
    }

    public AbstractStat(@NotNull Key key, TypeToken<M> modifierType, TypeToken<V> valueType) {
        this(key.getNamespace(), key.getValue(), modifierType, valueType);
    }

    @NotNull
    @Override
    public final Key getKey() {
        return key;
    }

    @Override
    @NotNull
    public TypeToken<V> getDataType() {
        return dataType;
    }

    @Override
    @NotNull
    public TypeToken<M> getModifierType() {
        return modifierType;
    }

    @Override
    @NotNull
    public String getId() {
        return id;
    }

    @Override
    @NotNull
    public String getNamespace() {
        return namespace;
    }

    @Override
    @Nullable
    public StatCombiner<M, V> getCombiner() {
        return null;
    }

    @Override
    @Nullable
    public StatLoader<V> getLoader() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stat<?, ?> stat = (Stat<?, ?>) o;
        return id.equals(stat.getId()) && namespace.equals(stat.getNamespace()) && modifierType.equals(stat.getModifierType()) && dataType.equals(stat.getDataType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, namespace, dataType);
    }
}
