package project.cyberproton.atom.ability;

import project.cyberproton.atom.ability.castable.Castable;
import project.cyberproton.atom.state.Keyed;

import java.util.List;

public interface Ability extends Keyed {
    String getName();

    List<Castable> getCastables();


}
