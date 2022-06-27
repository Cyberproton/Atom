package project.cyberproton.atom.module;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.UUID;

public interface ModuleManager<T extends Module> {

    static <T extends Module> ModuleManager<T> createSimple(@NotNull List<UUID> defaultModuleIds) {
        return SimpleModuleManager.create(defaultModuleIds);
    }

    static <T extends Module> ModuleManager<T> createSimple() {
        return SimpleModuleManager.create();
    }

    @NotNull
    List<UUID> getDefaultModuleIds();

    @NotNull
    List<T> getModules();

    @NotNull
    List<T> getActiveModules();

    @Nullable
    T getModule(@NotNull UUID id);

    @Nullable
    <M extends T> M getModule(@NotNull TypeToken<M> type);

    @Nullable
    default <M extends T> M getModule(@NotNull Class<M> cls) {
        return getModule(TypeToken.of(cls));
    }

    boolean registerModule(@NotNull T module);

    boolean unregisterModule(@NotNull UUID uuid);

    default boolean unregisterModule(@NotNull T module) {
        return unregisterModule(module.getUUID());
    }

    default void enableModules() {}

    default void reloadModules() {}

    default void disableModules() {}

}
