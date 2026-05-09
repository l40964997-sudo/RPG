package nl.rug.oop.rpg;

/**
 * Mysterio: Uses illusions to occasionally dodge attacks.
 */
public class Mysterio extends Villain {
    private static final long serialVersionUID = 1L;

    public Mysterio(String name, String description, int baseHealth, int baseDamage, Difficulty difficulty, Item drop) {
        super(name, description, baseHealth, baseDamage, difficulty, drop);
    }

    @Override
    public int onPlayerAttack(int damage) {
        if (Math.random() < 0.33) {
            System.out.println("You hit " + getName() + "... But it was just a hologram! Zero damage!");
            return 0;
        }
        return damage;
    }
}