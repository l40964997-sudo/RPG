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
    ULTIMATE("untimate",1.5,0.75);

    private final String label;
    private final double villainMultiplier;
    private final double playerMultiplier;

    Difficulty(String label, double villainMultiplier, double playerMultiplier) {
        this.label = label;
        this.villainMultiplier = villainMultiplier;
        this.playerMultiplier = playerMultiplier;
    }



}
