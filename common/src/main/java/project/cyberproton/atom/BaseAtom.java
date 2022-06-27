package project.cyberproton.atom;

import project.cyberproton.atom.ability.AbilityManager;
import project.cyberproton.atom.ability.AtomAbilityManager;
import project.cyberproton.atom.stat.AtomStatManager;
import project.cyberproton.atom.stat.StatManager;

public class BaseAtom {
    private AbilityManager abilityManager;
    private StatManager statManager;

    public void load() {

    }

    public void enable() {
        statManager = new AtomStatManager();
        statManager.register();
        abilityManager = new AtomAbilityManager();
    }

    public void reload() {

    }

    public void disable() {

    }

    public StatManager getStatManager() {
        return statManager;
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }
}
