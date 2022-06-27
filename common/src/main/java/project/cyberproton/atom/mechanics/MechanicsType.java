package project.cyberproton.atom.mechanics;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.state.Key;
import project.cyberproton.atom.state.Keyed;

public interface MechanicsType<M extends Modifier<V>, V, O> extends Keyed {

    @NotNull
    static <M extends Modifier<V>, V, O> GenericMechanicsType<M, V, O> create(
        @NotNull Key key,
        @NotNull TypeToken<M> modifierType,
        @NotNull TypeToken<V> dataType,
        @NotNull TypeToken<O> combinedOutputType,
        @Nullable MechanicsModifierExtractor<M, O> extractor
    ) {
        return new GenericMechanicsType<>(key, modifierType, dataType, combinedOutputType, extractor);
    }

    @NotNull
    static <M extends Modifier<V>, V, O> GenericMechanicsType<M, V, O> create(
        @NotNull Key key,
        @NotNull Class<M> modifierClass,
        @NotNull Class<V> dataClass,
        @NotNull Class<O> combinedOutputType,
        @Nullable MechanicsModifierExtractor<M, O> extractor
    ) {
        return create(key, TypeToken.of(modifierClass), TypeToken.of(dataClass), TypeToken.of(combinedOutputType), extractor);
    }

    @NotNull
    static <M extends Modifier<V>, V, O> DoubleMechanicsType createDouble(@NotNull Key key) {
        return new DoubleMechanicsType(key);
    }

    @NotNull
    TypeToken<M> getModifierType();

    @NotNull
    TypeToken<V> getDataType();

    @NotNull
    TypeToken<O> getCombinedOutputType();

    @Nullable
    MechanicsModifierExtractor<M, O> getExtractor();

}
