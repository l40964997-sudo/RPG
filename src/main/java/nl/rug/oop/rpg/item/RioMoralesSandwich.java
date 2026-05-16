package nl.rug.oop.rpg.item;

import nl.rug.oop.rpg.entity.Player;

import java.io.Serializable;

/**
 * A consumable healing item representing a sandwich made by Miles' mother, Rio Morales.
 * When used from the inventory, it restores a specific amount of Health Points (HP)
 * to the player, capped at their maximum health. As a consumable, it is removed
 * from the inventory after one use.
 */
public class RioMoralesSandwich extends Item implements Usable, Serializable {
    private static final long serialVersionUID = 1L;

    /** Heal amount. */
    private final int healAmount;

    /**
     * Construct a new RioMoralesSandwich.
     *
     * @param healAmount HP restored when the sandwich is eaten.
     */
    public RioMoralesSandwich(int healAmount) {
        super("Rio's sandwich","Pernil with mayo and pickles, " +
                "wrapped in foil. Restore "+healAmount+" HP");
        if (healAmount <= 0) {
            throw new IllegalArgumentException(
                    "healAmount must be positive, got: " + healAmount);
        }
        this.healAmount = healAmount;
    }

    /**
     * Heal the player by the configured amount (capped at max HP).
     *
     * @param player the player eating the sandwich.
     */
    @Override
    public boolean use(Player player) {
        player.heal(healAmount);
        System.out.println("You ate the sandwich. Tastes like home! Recovered " + healAmount + " HP.");
        return true;
    }
}