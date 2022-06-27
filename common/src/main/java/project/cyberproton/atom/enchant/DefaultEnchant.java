package project.cyberproton.atom.enchant;

public class DefaultEnchant implements Enchant {
    private final EnchantType type;
    private final int level;

    DefaultEnchant(EnchantType type, int level) {
        this.type = type;
        this.level = level;
    }

    @Override
    public EnchantType type() {
        return type;
    }

    @Override
    public int level() {
        return level;
    }
}
