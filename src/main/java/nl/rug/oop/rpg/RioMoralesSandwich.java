package nl.rug.oop.rpg;

/**
 * RioMoralesSandwich: Heals the player.
 */
public class RioMoralesSandwich extends Item {
    private static final long serialVersionUID = 1L;

    /** Heal amount. */
    private final int healAmount;

    /**
     * Construct a new RioMoralesSandwich.
     *
     * @param healAmount HP restored when the sandwich is eaten.
     */
    public RioMoralesSandwich(int healAmount) {
        super("Rio's sandwich","Pernil with mayo and pickles, wrapped in foil. Restore "+healAmount+" HP");
        this.healAmount = healAmount;
    }


    /**
     * Heal the player by the configured amount (capped at max HP).
     *
     * @param player the player eating the sandwich.
     */
    @Override
    public void use(Player player) {
        player.heal(healAmount);
        System.out.println("You ate the sandwich. Tastes like home! Recovered " + healAmount + " HP.");
    }
}