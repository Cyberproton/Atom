package project.cyberproton.atom.ability;

import project.cyberproton.atom.ability.castable.Castable;
import project.cyberproton.atom.state.Key;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class DefaultAbility implements Ability {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<Castable> getCastables() {
        return null;
    }

    @NotNull
    @Override
    public Key getKey() {
        return null;
    }
}
