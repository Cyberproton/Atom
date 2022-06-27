package project.cyberproton.atom.entity;

import org.jetbrains.annotations.NotNull;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.state.DefaultNonExpandableStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unchecked")
public abstract class AbstractEntity extends DefaultNonExpandableStore implements IEntity {
    private final AtomPlugin plugin;
    private final EntityModuleManager moduleManager;

    public AbstractEntity(@NotNull AtomPlugin plugin, @NotNull List<UUID> defaultModuleIds) {
        super();
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(defaultModuleIds, "defaultModuleIds");
        this.plugin = plugin;
        this.moduleManager = EntityModuleManager.createSimple(plugin, defaultModuleIds);
    }

    public AbstractEntity(@NotNull AtomPlugin plugin) {
        this(plugin, new ArrayList<>());
    }

    @NotNull
    @Override
    public EntityModuleManager getModuleManager() {
        return moduleManager;
    }

    @NotNull
    @Override
    public IStatModule<IEntity> getStatModule() {
        return ((IStatModule<IEntity>) moduleManager.getModule(EntityModules.ModuleId.STAT));
    }

    @NotNull
    @Override
    public IStateModule<? extends IEntity> getStateModule() {
        return ((IStateModule<IEntity>) moduleManager.getModule(EntityModules.ModuleId.MECHANICS));
    }

    @Override
    public @NotNull IDamageMechanicsModule<? extends IEntity> getDamageMechanicsModule() {
        return null;
    }

    @Override
    public void enable() {
        moduleManager.enableModules();
    }

    @Override
    public void reload() {
        moduleManager.reloadModules();
    }

    @Override
    public void disable() {
        moduleManager.disableModules();
    }

    public void tick() { }
}
