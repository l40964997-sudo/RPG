package nl.rug.oop.rpg;

/**
 * Sandman: Sand-like body reduces incoming damage.
 */
public class Sandman extends Villain {
    private static final long serialVersionUID = 1L;

    public Sandman(String name, String description, int baseHealth, int baseDamage, Difficulty difficulty, Item drop) {
        super(name, description, baseHealth, baseDamage, difficulty, drop);
    }

    @Override
    public int onPlayerAttack(int damage) {
        System.out.println(getName() + "'s sand body absorbs half of the physical impact.");
        return damage / 2;
    }
}