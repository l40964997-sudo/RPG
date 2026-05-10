package nl.rug.oop.rpg;

/**
 * Green Goblin: deals bonus damage every third turn by tossing
 * a pumpkin bomb. Otherwise rams the player with the glider for
 * normal damage. Overrides only {@link Villain#performAttack(Player)}.
 */
public class GreenGoblin extends Villain {
    private static final long serialVersionUID = 1L;

    /** Counter used to fire the bomb attack on every third turn. */
    private int turnCount = 0;

    /**
     * Construct the Green Goblin with difficulty-scaled stats.
     *
     * @param difficulty current game difficulty.
     * @param drop       optional item awarded on defeat.
     */
    public GreenGoblin(Difficulty difficulty, Item drop) {
        super("Green Goblin","Norman Osborn cackles from atop his glider, a pumpkin bomb in each hand.",
                46,7,difficulty,drop);
    }

    /**
     * Most turns: ordinary attack. Every third turn: bomb for
     * +6 extra damage.
     *
     * @param player the player taking the hit.
     */
    @Override
    public void performAttack(Player player) {
        turnCount++;
        int finalDamage = this.getDamage();

        if (turnCount % 3 == 0) {
            finalDamage += 6;
            System.out.println(getName() + " Throw a pumpkin bomb！Extra Damage！");
        }

        player.takeDamage(finalDamage);
    }
}