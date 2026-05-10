package nl.rug.oop.rpg;

/**
 * Doctor Octopus: when hit hard (10+ damage in one blow), one
 * of his tentacles snaps back and absorbs some of the impact
 * <em>and</em> the next turn's normal attack still hits. Light
 * hits don't trigger the counter, so players have a real choice
 * between fast small hits and slow big hits.
 * <p>
 * Overrides {@link Villain#onPlayerAttack(int)} only.
 */
public class DoctorOctopus extends Villain {
    private static final long serialVersionUID = 1L;

    /**
     * Construct Doctor Octopus with difficulty-scaled stats.
     *
     * @param difficulty current game difficulty.
     * @param drop       optional item awarded on defeat.
     */
    public DoctorOctopus( Difficulty difficulty, Item drop) {
        super("Doctor Octopus", "Otto Octavius, four steel tentacles whirring around him", 60, 8, difficulty, drop);
    }

    /**
     * If the incoming hit was heavy (10+ damage), counter-attack
     * for 5 damage. Otherwise do nothing.
     *
     * @param player the player who just attacked.
     * @param dealt  damage just dealt to this villain.
     */
    @Override
    public void onPlayerAttack(Player player, int dealt) {
        if (dealt >= 10) {
            int counter = 5;
            System.out.println("A tentacle snaps back and slams you for " + counter + " counter damage!");
            player.takeDamage(counter);
        }
    }
}