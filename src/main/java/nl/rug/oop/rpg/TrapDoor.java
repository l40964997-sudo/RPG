package nl.rug.oop.rpg;

import java.io.Serializable;

/**
 * Damages the player on use. If the damage would kill, spider-sense
 * kicks in and the door refuses passage.
 */
public class TrapDoor extends Door implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The amount of health points deducted from the player when they trigger the trap. */
    private final int damage;
    /** The narrative text displayed to the player when the trap is activated.*/
    private final String trapDescription;

    /**
     * Create a trap door without a reward.
     *
     * @param description     text shown when the door is inspected.
     * @param destination     room the player enters if they survive the trap.
     * @param damage          HP cost for using this door.
     * @param trapDescription text printed when the trap fires
     *                        (e.g. {@code "You crash through a railing."}).
     */
    public TrapDoor(String description, Room destination,
                    int damage, String trapDescription) {
        super(description, destination);
        this.damage = damage;
        this.trapDescription = trapDescription;
    }

    /**
     * Create a trap door that grants a one-time reward.
     *
     * @param description     text shown when the door is inspected.
     * @param destination     room the player enters if they survive the trap.
     * @param reward          item granted on first successful passage.
     * @param damage          HP cost for using this door.
     * @param trapDescription text printed when the trap fires.
     */
    public TrapDoor(String description, Room destination,
                    Item reward, int damage, String trapDescription) {
        super(description, destination, reward);
        this.damage = damage;
        this.trapDescription = trapDescription;
    }


    /**
     * Refuse passage if the trap would kill the player, otherwise
     * deal the damage and pass them through.
     *
     * @param player the player using the door.
     */
    @Override
    public void interact(Player player) {
        if (player.getHealth() - damage <= 0) {
            System.out.println("Spider-sense tingles! That door would be the end of you. You back off.");
            return;
        }
        System.out.println("It's a trap! You take " + damage + " damage going through.");
        player.takeDamage(damage);
        passThrough(player);
    }
}
