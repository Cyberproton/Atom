package project.cyberproton.atom.ability;

import project.cyberproton.atom.state.Key;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AbilityManager {
    @Nullable
    Ability getAbility(@NotNull Key key);

    @Nullable
    Ability getAbility(@NotNull String name);
}
