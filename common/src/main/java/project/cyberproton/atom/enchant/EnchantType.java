package project.cyberproton.atom.enchant;

import project.cyberproton.atom.state.Key;
import project.cyberproton.atom.state.Keyed;

import org.jetbrains.annotations.NotNull;

public enum EnchantType implements Keyed {
    /**
     * Provides protection against environmental damage
     */
    PROTECTION_ENVIRONMENTAL("protection"),

    /**
     * Provides protection against fire damage
     */
    PROTECTION_FIRE("fire_protection"),

    /**
     * Provides protection against fall damage
     */
    PROTECTION_FALL("feather_falling"),

    /**
     * Provides protection against explosive damage
     */
    PROTECTION_EXPLOSIONS("blast_protection"),

    /**
     * Provides protection against projectile damage
     */
    PROTECTION_PROJECTILE("projectile_protection"),

    /**
     * Decreases the rate of air loss whilst underwater
     */
    OXYGEN("respiration"),

    /**
     * Increases the speed at which a player may mine underwater
     */
    WATER_WORKER("aqua_affinity"),

    /**
     * Damages the attacker
     */
    THORNS("thorns"),

    /**
     * Increases walking speed while in water
     */
    DEPTH_STRIDER("depth_strider"),

    /**
     * Freezes any still water adjacent to ice / frost which player is walking on
     */
    FROST_WALKER("frost_walker"),

    /**
     * Item cannot be removed
     */
    BINDING_CURSE("binding_curse"),

    /**
     * Increases damage against all targets
     */
    DAMAGE_ALL("sharpness"),

    /**
     * Increases damage against undead targets
     */
    DAMAGE_UNDEAD("smite"),

    /**
     * Increases damage against arthropod targets
     */
    DAMAGE_ARTHROPODS("bane_of_arthropods"),

    /**
     * All damage to other targets will knock them back when hit
     */
    KNOCKBACK("knockback"),

    /**
     * When attacking a target, has a chance to set them on fire
     */
    FIRE_ASPECT("fire_aspect"),

    /**
     * Provides a chance of gaining extra loot when killing monsters
     */
    LOOT_BONUS_MOBS("looting"),

    /**
     * Increases damage against targets when using a sweep attack
     */
    SWEEPING_EDGE("sweeping"),

    /**
     * Increases the rate at which you mine/dig
     */
    DIG_SPEED("efficiency"),

    /**
     * Allows blocks to drop themselves instead of fragments (for example,
     * stone instead of cobblestone)
     */
    SILK_TOUCH("silk_touch"),

    /**
     * Decreases the rate at which a tool looses durability
     */
    DURABILITY("unbreaking"),

    /**
     * Provides a chance of gaining extra loot when destroying blocks
     */
    LOOT_BONUS_BLOCKS("fortune"),

    /**
     * Provides extra damage when shooting arrows from bows
     */
    ARROW_DAMAGE("power"),

    /**
     * Provides a knockback when an entity is hit by an arrow from a bow
     */
    ARROW_KNOCKBACK("punch"),

    /**
     * Sets entities on fire when hit by arrows shot from a bow
     */
    ARROW_FIRE("flame"),

    /**
     * Provides infinite arrows when shooting a bow
     */
    ARROW_INFINITE("infinity"),

    /**
     * Decreases odds of catching worthless junk
     */
    LUCK("luck_of_the_sea"),

    /**
     * Increases rate of fish biting your hook
     */
    LURE("lure"),

    /**
     * Causes a thrown trident to return to the player who threw it
     */
    LOYALTY("loyalty"),

    /**
     * Deals more damage to mobs that live in the ocean
     */
    IMPALING("impaling"),

    /**
     * When it is rainy, launches the player in the direction their trident is thrown
     */
    RIPTIDE("riptide"),

    /**
     * Strikes lightning when a mob is hit with a trident if conditions are
     * stormy
     */
    CHANNELING("channeling"),

    /**
     * Shoot multiple arrows from crossbows
     */
    MULTISHOT("multishot"),

    /**
     * Charges crossbows quickly
     */
    QUICK_CHARGE("quick_charge"),

    /**
     * Crossbow projectiles pierce entities
     */
    PIERCING("piercing"),

    /**
     * Allows mending the item using experience orbs
     */
    MENDING("mending"),
    ;

    private final Key key;

    EnchantType(String key) {
        this.key = Key.minecraft(key);
    }

    @NotNull
    @Override
    public Key getKey() {
        return key;
    }
}
