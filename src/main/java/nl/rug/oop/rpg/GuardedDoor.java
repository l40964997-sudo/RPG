package nl.rug.oop.rpg;

import java.io.Serializable;

/**
 * Refuses passage while any villain in the player's current room is
 * still standing.
 */
public class GuardedDoor extends Door implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Create a guarded door with no reward.
     *
     * @param description text shown when the door is inspected.
     * @param destination room behind the door.
     */
    public GuardedDoor(String description, Room destination) {
        super(description, destination);
    }

    /**
     * Create a guarded door that grants a reward on first passage.
     *
     * @param description text shown when the door is inspected.
     * @param destination room behind the door.
     * @param reward      item granted on first successful passage.
     */
    public GuardedDoor(String description, Room destination, Item reward) {
        super(description, destination, reward);
    }

    /**
     * Block the player if any villain in the current room is alive,
     * otherwise let them through.
     *
     * @param player the player using the door.
     */
    @Override
    public void interact(Player player) {
        if (!player.getCurrentRoom().villainCleared()) {
            System.out.println("The door won't budge. There are still villains around.");
            return;
        }
        passThrough(player);
    }
}
