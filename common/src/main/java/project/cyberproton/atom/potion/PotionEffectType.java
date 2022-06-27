package project.cyberproton.atom.potion;

public enum PotionEffectType {
    /**
     * Increases movement speed.
     */

    SPEED,
    /**
     * Decreases movement speed.
     */
    SLOW,

    /**
     * Increases dig speed.
     */
    FAST_DIGGING,

    /**
     * Decreases dig speed.
     */
    SLOW_DIGGING,

    /**
     * Increases damage dealt.
     */
    INCREASE_DAMAGE,

    /**
     * Heals an entity.
     */
    HEAL,

    /**
     * Hurts an entity.
     */
    HARM,

    /**
     * Increases jump height.
     */
    JUMP,

    /**
     * Warps vision on the client.
     */
    CONFUSION,

    /**
     * Regenerates health.
     */
    REGENERATION,

    /**
     * Decreases damage dealt to an entity.
     */
    DAMAGE_RESISTANCE,

    /**
     * Stops fire damage.
     */
    FIRE_RESISTANCE,

    /**
     * Allows breathing underwater.
     */
    WATER_BREATHING,

    /**
     * Grants invisibility.
     */
    INVISIBILITY,

    /**
     * Blinds an entity.
     */
    BLINDNESS,

    /**
     * Allows an entity to see in the dark.
     */
    NIGHT_VISION,

    /**
     * Increases hunger.
     */
    HUNGER,

    /**
     * Decreases damage dealt by an entity.
     */
    WEAKNESS,

    /**
     * Deals damage to an entity over time.
     */
    POISON,

    /**
     * Deals damage to an entity over time and gives the health to the
     * shooter.
     */
    WITHER,

    /**
     * Increases the maximum health of an entity.
     */
    HEALTH_BOOST,

    /**
     * Increases the maximum health of an entity with health that cannot be
     * regenerated, but is refilled every 30 seconds.
     */
    ABSORPTION,

    /**
     * Increases the food level of an entity each tick.
     */
    SATURATION,

    /**
     * Outlines the entity so that it can be seen from afar.
     */
    GLOWING,

    /**
     * Causes the entity to float into the air.
     */
    LEVITATION,

    /**
     * Loot table luck.
     */
    LUCK,

    /**
     * Loot table unluck.
     */
    UNLUCK,

    /**
     * Slows entity fall rate.
     */
    SLOW_FALLING,

    /**
     * Effects granted by a nearby conduit. Includes enhanced underwater abilities.
     */
    CONDUIT_POWER,

    /**
     * Squee'ek uh'k kk'kkkk squeek eee'eek.
     */
    DOLPHINS_GRACE,

    /**
     * oof.
     */
    BAD_OMEN,

    /**
     * \o/.
     */
    HERO_OF_THE_VILLAGE,
}
