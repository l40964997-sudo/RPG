package nl.rug.oop.rpg;

import java.io.Serializable;

/**
 * Green Goblin: deals bonus damage every third turn by tossing
 * a pumpkin bomb. Otherwise rams the player with the glider for
 * normal damage. Overrides only {@link Villain#performAttack(Player)}.
 */
public class GreenGoblin extends Villain implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Base health pool before difficulty scaling. */
    private static final int BASE_HEALTH = 46;
    /** Base per-turn damage before difficulty scaling. */
    private static final int BASE_DAMAGE = 7;
    /** Extra damage applied when a bomb attack fires. */
    private static final int BOMB_DAMAGE_BONUS = 6;
    /** Every Nth turn triggers a bomb attack. */
    private static final int BOMB_FREQUENCY = 3;

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
                BASE_HEALTH,BASE_DAMAGE,difficulty,drop);
    }

    /**
     * Most turns: ordinary attack. Every {@value #BOMB_FREQUENCY}th turn: bomb for
     * +{@value #BOMB_DAMAGE_BONUS} extra damage.
     *
     * @param player the player taking the hit.
     */
    @Override
    public void performAttack(Player player) {
        turnCount++;
        int finalDamage = this.getDamage();

        if (turnCount % BOMB_FREQUENCY == 0) {
            finalDamage += BOMB_DAMAGE_BONUS;
            System.out.println(getName() + " Throw a pumpkin bomb！");
        }
        System.out.println(getName() + " hits you for " + finalDamage + " damage.");
        player.takeDamage(finalDamage);
    }
}