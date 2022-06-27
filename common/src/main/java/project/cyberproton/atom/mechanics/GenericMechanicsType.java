package project.cyberproton.atom.mechanics;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.state.Key;

import java.util.Objects;

public class GenericMechanicsType<M extends Modifier<V>, V, O> implements MechanicsType<M, V, O> {
    private final Key key;
    private final TypeToken<M> modifierType;
    private final TypeToken<V> dataType;
    private final TypeToken<O> combinedOutputType;
    private final MechanicsModifierExtractor<M, O> extractor;

    protected GenericMechanicsType(@NotNull Key key, @NotNull TypeToken<M> modifierType, @NotNull TypeToken<V> dataType, @NotNull TypeToken<O> combinedOutputType, @Nullable MechanicsModifierExtractor<M, O> extractor) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(modifierType, "modifierType");
        Objects.requireNonNull(dataType, "dataType");
        this.key = key;
        this.modifierType = modifierType;
        this.dataType = dataType;
        this.combinedOutputType = combinedOutputType;
        this.extractor = extractor;
    }

    @NotNull
    @Override
    public Key getKey() {
        return key;
    }

    @NotNull
    @Override
    public TypeToken<M> getModifierType() {
        return modifierType;
    }

    @Override
    public @NotNull TypeToken<V> getDataType() {
        return dataType;
    }

    @NotNull
    @Override
    public TypeToken<O> getCombinedOutputType() {
        return combinedOutputType;
    }

    @Nullable
    @Override
    public MechanicsModifierExtractor<M, O> getExtractor() {
        return extractor;
    }
}
