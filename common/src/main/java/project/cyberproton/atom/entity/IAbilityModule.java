package project.cyberproton.atom.entity;

import project.cyberproton.atom.ability.Ability;
import project.cyberproton.atom.ability.trigger.Trigger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.function.Predicate;

public interface IAbilityModule<T extends IEntity> extends IEntityModule<T> {

    void castAbilities(Trigger trigger);

    void addAbility(@NotNull Ability ability);

    @Nullable
    Ability removeAbility(@NotNull Ability ability);

    @Nullable
    Ability removeAbility(@NotNull String id);

    @NotNull
    List<Ability> removeAbilitiesIf(Predicate<Ability> predicate);

    @NotNull
    List<Ability> clearAbilities();

}
