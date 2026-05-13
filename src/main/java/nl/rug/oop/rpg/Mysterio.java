package nl.rug.oop.rpg;

import java.io.Serializable;

/**
 * Mysterio: Uses illusions to occasionally dodge attacks.
 */
public class Mysterio extends Villain implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Probability of a hit being a hologram and dealing zero damage. */
    private static final double DODGE_CHANCE = 0.33;

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
     * With {@value #DODGE_CHANCE} probability the hit is wasted on
     * a hologram and deals zero damage. {@link Player} will clamp
     * the displayed value to at least 1, so the player still sees
     * some impact text, but Mysterio takes no real damage.
     *
     * @param incoming raw damage about to be applied.
     * @return 0 if dodged, otherwise the damage unchanged.
     */
    @Override
    public int modifyIncomingDamage(int incoming) {
        if (Math.random() < DODGE_CHANCE) {
            System.out.println("You hit " + getName() + "... But it was just a hologram! Zero damage!");
            return 0;
        }
        return incoming;
    }
}