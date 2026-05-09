package nl.rug.oop.rpg;

/**
 * GreenGoblin: 每3回合造成额外伤害
 */
public class GreenGoblin extends Villain {
    private static final long serialVersionUID = 1L;
    private int turnCount = 0;

    /**
     * Construct the Green Goblin with difficulty-scaled stats.
     *
     * @param difficulty current game difficulty.
     * @param drop       optional item awarded on defeat.
     */
    public GreenGoblin(Difficulty difficulty, Item drop) {
        super("Green Goblin","Norman Osborn cackles from atop his glider, a pumpkin bomb in each hand",46,7,difficulty,drop);
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