package nl.rug.oop.rpg;

/**
 * GreenGoblin: 每3回合造成额外伤害
 */
public class GreenGoblin extends Villain {
    private static final long serialVersionUID = 1L;
    private int turnCount = 0;

    public GreenGoblin(String name, String description, int baseHealth, int baseDamage, Difficulty difficulty, Item drop) {
        super(name, description, baseHealth, baseDamage, difficulty, drop);
    }

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