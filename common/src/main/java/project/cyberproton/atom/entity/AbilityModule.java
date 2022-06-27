package project.cyberproton.atom.entity;

import project.cyberproton.atom.ability.Ability;
import project.cyberproton.atom.ability.trigger.Trigger;
import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class AbilityModule<T extends IEntity> implements IAbilityModule<T> {
    private final T owner;
    private final AtomPlugin plugin;
    private final Set<Option> options;

    public AbilityModule(@NotNull AtomPlugin plugin, @NotNull T owner, @NotNull Set<Option> options) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(options, "options");
        this.owner = owner;
        this.plugin = plugin;
        this.options = options;
    }

    @NotNull
    @Override
    public AtomPlugin getPlugin() {
        return null;
    }

    @NotNull
    @Override
    public T getOwner() {
        return owner;
    }

    @NotNull
    @Override
    public UUID getUUID() {
        return EntityModules.ModuleId.ABILITY;
    }

    @NotNull
    @Override
    public Set<Option> getOptions() {
        return options;
    }

    @Override
    public void castAbilities(Trigger trigger) {

    }

    @Override
    public void addAbility(@NotNull Ability ability) {

    }

    @Nullable
    @Override
    public Ability removeAbility(@NotNull Ability ability) {
        return null;
    }

    @Nullable
    @Override
    public Ability removeAbility(@NotNull String id) {
        return null;
    }

    @NotNull
    @Override
    public List<Ability> removeAbilitiesIf(Predicate<Ability> predicate) {
        return null;
    }

    @NotNull
    @Override
    public List<Ability> clearAbilities() {
        return null;
    }


}
