package project.cyberproton.atom;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class GlobalConfig {
    private Damage damage = new Damage();
    private boolean test;

    @NotNull
    public Damage damageSection() {
        return damage;
    }

    public boolean test() {
        return test;
    }

    @ConfigSerializable
    public static class Damage {
        private boolean useCustomDamageSystem = true;
        private double protectionEnchantCoefficient = 0.01;
        private double resistancePotionCoefficient = 0.1;
        private double minDamage = 0;

        public boolean useCustomDamageSystem() {
            return useCustomDamageSystem;
        }

        public double protectionEnchantCoefficient() {
            return protectionEnchantCoefficient;
        }

        public double resistancePotionCoefficient() {
            return resistancePotionCoefficient;
        }

        public double minDamage() {
            return minDamage;
        }
    }

    public static GlobalConfig instance() {
        return Atom.getInstance().globalConfig();
    }
}
