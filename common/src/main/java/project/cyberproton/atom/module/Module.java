package project.cyberproton.atom.module;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public interface Module {

    @NotNull
    UUID getUUID();

    @NotNull
    default Set<Option> getOptions() {
        return new HashSet<>();
    }

    default void onEnable() {}

    default void onReload() {}

    default void onDisable() {}

    default void onTick() {}

    default int getTickRate() {
        return -1;
    }

    @NotNull
    default List<Class<?>> getDependencies() {
        return new ArrayList<>();
    }

    enum Lifecycle {
        DISABLED,
        ENABLING,
        RELOADING,
        ACTIVE,
        DISABLING,
    }

    enum Option {
        LAZY_ENABLING,
    }

}
