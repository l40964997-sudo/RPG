package nl.rug.oop.rpg;

/**
 * DoctorOctopus: 受到重击时反击
 */
public class DoctorOctopus extends Villain {
    private static final long serialVersionUID = 1L;

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