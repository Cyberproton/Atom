package project.cyberproton.atom.enchant;

import java.util.Objects;

public interface Enchant {
    static Enchant of(EnchantType type, int level){
        Objects.requireNonNull(type, "type");
        return new DefaultEnchant(type, level);
    }

    EnchantType type();

    int level();
}
