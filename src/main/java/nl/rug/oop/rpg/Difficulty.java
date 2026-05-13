package nl.rug.oop.rpg;

import lombok.Getter;

/**
 * Difficulty setting. Each level has multipliers that scale villain
 * stats up and player damage down for harder modes.
 *
 * Enums are Serializable by default — saving works automatically.
 */
@Getter
public enum Difficulty {

    FRIENDLY("Friendly Neighborhood",0.75,1.25),
    CRIME_WAVE("Crime Wave",1.0,1.0),
    ULTIMATE("ultimate",1.5,0.75);

    /** Human-readable label shown in menus. */
    private final String label;
    /** Multiplier applied to villain HP and damage at construction. */
    private final double villainMultiplier;
    /** Multiplier applied to the player's outgoing damage in combat. */
    private final double playerMultiplier;

    /**
     * * Constructs a new Difficulty level with specific balance modifiers.
     *
     * @param label             human-readable name shown in menus.
     * @param villainMultiplier scales villain HP and damage at construction.
     * @param playerMultiplier  scales the player's outgoing damage in combat.
     */
    Difficulty(String label, double villainMultiplier, double playerMultiplier) {
        this.label = label;
        this.villainMultiplier = villainMultiplier;
        this.playerMultiplier = playerMultiplier;
    }
}
