package nl.rug.oop.rpg;

import java.io.Serializable;

/**
 * Doctor Octopus: when hit hard (10+ damage in one blow), one
 * of his tentacles snaps back and absorbs some of the impact
 * <em>and</em> the next turn's normal attack still hits. Light
 * hits don't trigger the counter, so players have a real choice
 * between fast small hits and slow big hits.
 */
public class DoctorOctopus extends Villain implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Base heavy-hit threshold before difficulty scaling. */
    private static final int BASE_HEAVY_HIT_THRESHOLD = 10;
    /** Base counter damage before difficulty scaling. */
    private static final int BASE_COUNTER_DAMAGE = 5;

    /** Scaled threshold: hits at or above this trigger the counter. */
    private final int heavyHitThreshold;
    /** Scaled counter damage. */
    private final int counterDamage;

    /**
     * Construct Doctor Octopus with difficulty-scaled stats.
     *
     * @param difficulty current game difficulty.
     * @param drop       optional item awarded on defeat.
     */
    public DoctorOctopus( Difficulty difficulty, Item drop) {
        super("Doctor Octopus", "Otto Octavius, four steel tentacles whirring around him",
                60, 8, difficulty, drop);
        double mult = difficulty.getVillainMultiplier();
        this.heavyHitThreshold = (int) Math.round(BASE_HEAVY_HIT_THRESHOLD * mult);
        int scaled = (int) Math.round(BASE_COUNTER_DAMAGE * mult);
        this.counterDamage = scaled < 1 ? 1 : scaled;
    }

    /**
     * If the incoming hit was heavy, snap a tentacle back forcounter-attack
     * Light hits do nothing.
     *
     * @param player the player who just attacked.
     * @param dealt  damage just dealt to this villain.
     */
    @Override
    public void onPlayerAttack(Player player, int dealt) {
        if (dealt >= heavyHitThreshold) {
            System.out.println("A tentacle snaps back and slams you for "
                    + counterDamage + " counter damage!");
            player.takeDamage(counterDamage);
        }
    }
}