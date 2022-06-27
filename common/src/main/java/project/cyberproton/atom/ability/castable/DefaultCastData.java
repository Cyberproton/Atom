package project.cyberproton.atom.ability.castable;

import project.cyberproton.atom.ability.Ability;
import project.cyberproton.atom.ability.target.Targeter;
import project.cyberproton.atom.ability.trigger.Trigger;
import project.cyberproton.atom.condition.Condition;
import project.cyberproton.atom.entity.IEntity;

import java.util.List;

public class DefaultCastData {
    private final Ability ability;
    private final Trigger trigger;
    private final IEntity caster;
    private final List<Targeter> targeters;
    private final List<Condition> conditions;

    public DefaultCastData(Ability ability, Trigger trigger, IEntity caster, List<Targeter> targeters, List<Condition> conditions) {
        this.ability = ability;
        this.trigger = trigger;
        this.caster = caster;
        this.targeters = targeters;
        this.conditions = conditions;
    }

    public Ability getAbility() {
        return ability;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public IEntity getCaster() {
        return caster;
    }

    public List<Targeter> getTargeters() {
        return targeters;
    }

    public List<Condition> getConditions() {
        return conditions;
    }
}
