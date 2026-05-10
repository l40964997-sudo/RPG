package nl.rug.oop.rpg;

/**
 * Mysterio: Uses illusions to occasionally dodge attacks.
 */
public class Mysterio extends Villain {
    private static final long serialVersionUID = 1L;

    /**
     * Construct a new Mysterio.
     * @param difficulty current game difficulty; scales the villain's stats.
     * @param drop       optional item awarded to the player on defeat.
     */
    public Mysterio(Difficulty difficulty, Item drop) {
        super("Mysterio",
                "Quentin Beck in the fishbowl helmet, surrounded by shimmering doubles.",
                35, 6, difficulty, drop);
    }

    /**
     * 33% chance the hit lands on a hologram and is wasted.
     * @param damage incoming damage.
     * @return 0 if dodged, otherwise the damage unchanged.
     */
    @Override
    public int onPlayerAttack(int damage) {
        if (Math.random() < 0.33) {
            System.out.println("You hit " + getName() + "... But it was just a hologram! Zero damage!");
            return 0;
        }
        return damage;
    }
}