package project.cyberproton.atom.stat;

import project.cyberproton.atom.Atom;
import project.cyberproton.atom.attribute.AttributeType;
import project.cyberproton.atom.state.Key;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stats {
    public static final VanillaStat ATTACK_SPEED = new VanillaStat(Key.of("atom", "attack-speed"), AttributeType.GENERIC_ATTACK_SPEED);
    public static final VanillaStat ATTACK_DAMAGE = new VanillaStat(Key.of("atom", "attack-damage"), AttributeType.GENERIC_ATTACK_DAMAGE);
    public static final VanillaStat ARMOR = new VanillaStat(Key.of("atom", "armor"), AttributeType.GENERIC_ARMOR);
    public static final VanillaStat ARMOR_TOUGHNESS = new VanillaStat(Key.of("atom", "armor-toughness"), AttributeType.GENERIC_ARMOR_TOUGHNESS);
    public static final DoubleStat ARROW_PHYSICAL_DAMAGE = new DoubleStat(Key.of("atom", "arrow-physical-damage"));
    public static final DoubleStat ARROW_CRITICAL_STRIKE_CHANCE = new DoubleStat(Key.of("atom", "arrow-critical-strike-chance"));
    public static final DoubleStat ARROW_CRITICAL_STRIKE_POWER = new DoubleStat(Key.of("atom", "arrow-critical-strike-power"));
    public static final DoubleStat CRITICAL_STRIKE_CHANCE = new DoubleStat(Key.of("atom", "critical-strike-chance"));
    public static final DoubleStat CRITICAL_STRIKE_POWER = new DoubleStat(Key.of("atom", "critical-strike-power"));
    public static final DoubleStat LIFE_STEAL = new DoubleStat(Key.of("atom", "life-steal"));
    public static final VanillaStat LUCK = new VanillaStat(Key.of("atom", "luck"), AttributeType.GENERIC_LUCK);
    public static final DoubleStat HEALING_RATE = new DoubleStat(Key.of("atom", "healing-rate"));
    public static final VanillaStat MAX_HEALTH = new VanillaStat(Key.of("atom", "max-health"), AttributeType.GENERIC_MAX_HEALTH);
    public static final VanillaStat MOVEMENT_SPEED = new VanillaStat(Key.of("atom", "movement-speed"), AttributeType.GENERIC_MOVEMENT_SPEED);


    private static final List<VanillaStat> vanillas;
    private static final List<Stat<?, ?>> all;

    static {
        List<Stat<?, ?>> a = new ArrayList<>();
        List<VanillaStat> v = new ArrayList<>();
        for (Field field : Stats.class.getFields()) {
            try {
                field.setAccessible(true);
                Object o = field.get(null);
                if (!(o instanceof Stat<?, ?>)) {
                    continue;
                }
                a.add(((Stat<?, ?>) o));
                if (o instanceof VanillaStat) {
                    v.add(((VanillaStat) o));
                }
            } catch (IllegalAccessException e) {
                Atom.getInstance().getLog().d("Unable to load some stats");
            }
        }
        vanillas = Collections.unmodifiableList(v);
        all = Collections.unmodifiableList(a);
    }

    public static List<VanillaStat> vanillas() {
        return vanillas;
    }

    public static List<Stat<?, ?>> all() {
        return all;
    }
}
