package project.cyberproton.atom.entity;

import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.module.ModuleManager;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;

public interface EntityModuleManager extends ModuleManager<IEntityModule<?>> {

    static EntityModuleManager createSimple() {
        return SimpleEntityModuleManager.create();
    }

    static EntityModuleManager createSimple(@NotNull AtomPlugin plugin) {
        return SimpleEntityModuleManager.create(plugin);
    }

    static EntityModuleManager createSimple(@NotNull AtomPlugin plugin, @NotNull List<UUID> defaultModuleIds) {
        return SimpleEntityModuleManager.create(plugin, defaultModuleIds);
    }

    @NotNull
    List<IEntityModule<?>> getModulesByPlugin(@NotNull AtomPlugin plugin);

}
