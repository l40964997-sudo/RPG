package nl.rug.oop.rpg;

import java.io.Serializable;

/**
 * Sandman: Sand-like body reduces incoming damage.
 */
public class Sandman extends Villain implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Construct a new Sandman.
     *
     * @param difficulty current game difficulty; scales the villain's stats.
     * @param drop       optional item awarded to the player on defeat.
     */
    public Sandman(Difficulty difficulty, Item drop) {
        super("Sandman",
                "Flint Marko, a churning column of grit and broken concrete.",
                80, 7, difficulty, drop);
    }

    @Override
    public int modifyIncomingDamage(int incoming) {
        System.out.println(getName() + "'s sand body absorbs half of the physical impact.");
        return incoming / 2;
    }
}