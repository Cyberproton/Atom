package project.cyberproton.atom.inject;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class AbstractMutableInjector extends AbstractInjector implements MutableInjector {
    @Override
    public <T> void registerProvider(@NotNull Provider<T> provider) {
        Objects.requireNonNull(provider, "provider");
        if (providers.containsKey(provider.getType())) {
            throw new IllegalStateException("Provider of type " + provider.getType() + " has already been registered");
        }
        providers.put(provider.getType(), provider);
    }

    @Override
    public <T> void unregisterProvider(@NotNull TypeToken<T> type) {
        Objects.requireNonNull(type, "type");
        providers.remove(type);
    }

    @Override
    public <T> void unregisterProvider(@NotNull Provider<T> provider) {
        Objects.requireNonNull(provider, "provider");
        Provider<?> exist = providers.get(provider.getType());
        if (exist == null) {
            throw new NoSuchElementException("Provider does not exist");
        }
        providers.remove(provider.getType());
    }

    @Override
    public void inject(@NotNull Object obj, @NotNull Predicate<Field> predicate) {

    }
}
