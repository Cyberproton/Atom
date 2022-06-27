package project.cyberproton.atom.inject;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import java.util.function.Supplier;

public interface MutableInjector extends Injector {

    <T> void registerProvider(@NotNull Provider<T> provider);

    default <T> void registerProvider(@NotNull Class<T> cls, @NotNull Supplier<T> provider) {
        registerProvider(Provider.of(TypeToken.of(cls), provider));
    }

    default <T> void registerProvider(@NotNull TypeToken<T> type, @NotNull Supplier<T> provider) {
        registerProvider(Provider.of(type, provider));
    }

    <T> void unregisterProvider(@NotNull Provider<T> provider);

    <T> void unregisterProvider(@NotNull TypeToken<T> type);

    default <T> void unregisterProvider(@NotNull Class<T> cls) {
        unregisterProvider(TypeToken.of(cls));
    }

}
