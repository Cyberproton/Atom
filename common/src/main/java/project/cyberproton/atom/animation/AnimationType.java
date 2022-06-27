package project.cyberproton.atom.animation;

public enum AnimationType {

    /**
     * Colored particles from a tipped arrow.
     */
    ARROW_PARTICLES,
    /**
     * Rabbit jumping.
     */
    RABBIT_JUMP,
    /**
     * When mobs get hurt.
     */
    HURT,
    /**
     * When a mob dies.
     * <p>
     * <b>This will cause client-glitches!</b>
     *
     * @deprecated although this effect may trigger other events on non-living
     * entities, it's only supported usage is on living ones.
     */
    @Deprecated
    DEATH,
    // PAIL - SPIGOT-3641 duplicate
    // GOLEM_ATTACK,
    // 5 - unused
    /**
     * The smoke when taming a wolf fails.
     */
    WOLF_SMOKE,
    /**
     * The hearts when taming a wolf succeeds.
     */
    WOLF_HEARTS,
    /**
     * When a wolf shakes (after being wet).
     */
    WOLF_SHAKE,
    // 9 - unused
    /**
     * When an entity eats a LONG_GRASS block.
     *
     * @deprecated although this effect may trigger other events on non-living
     * entities, it's only supported usage is on living ones.
     */
    @Deprecated
    SHEEP_EAT,
    /**
     * When an Iron Golem gives a rose.
     */
    IRON_GOLEM_ROSE,
    /**
     * Hearts from a villager.
     */
    VILLAGER_HEART,
    /**
     * When a villager is angry.
     */
    VILLAGER_ANGRY,
    /**
     * Happy particles from a villager.
     */
    VILLAGER_HAPPY,
    /**
     * Magic particles from a witch.
     */
    WITCH_MAGIC,
    /**
     * When a zombie transforms into a villager by shaking violently.
     */
    ZOMBIE_TRANSFORM,
    /**
     * When a firework explodes.
     */
    FIREWORK_EXPLODE,
    /**
     * Hearts from a breeding entity.
     */
    LOVE_HEARTS,
    /**
     * Resets squid rotation.
     */
    SQUID_ROTATE,
    /**
     * Silverfish entering block, spawner spawning.
     */
    ENTITY_POOF,
    /**
     * Guardian sets laser target.
     */
    GUARDIAN_TARGET,
    // 22-28 player internal flags
    /**
     * Shield blocks attack.
     */
    SHIELD_BLOCK,
    /**
     * Shield breaks.
     */
    SHIELD_BREAK,
    // 31 - unused
    /**
     * Armor stand is hit.
     */
    ARMOR_STAND_HIT,
    /**
     * Entity hurt by thorns attack.
     */
    THORNS_HURT,
    /**
     * Iron golem puts away rose.
     */
    IRON_GOLEM_SHEATH,
    /**
     * Totem prevents entity death.
     */
    TOTEM_RESURRECT,
    /**
     * Entity hurt due to drowning damage.
     */
    HURT_DROWN,
    /**
     * Entity hurt due to explosion damage.
     */
    HURT_EXPLOSION,

}
