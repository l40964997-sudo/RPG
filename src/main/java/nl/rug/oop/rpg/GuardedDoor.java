package nl.rug.oop.rpg;

/**
 * Refuses passage while any villain in the player's current room is
 * still standing.
 */
public class GuardedDoor extends Door {

    private static final long serialVersionUID = 1L;

    public GuardedDoor(String description, Room destination) {
        super(description, destination);
    }

    public GuardedDoor(String description, Room destination, Item reward) {
        super(description, destination, reward);
    }

    @Override
    public void interact(Player player) {
        if (!player.getCurrentRoom().villainCleared()) {
            System.out.println("The door won't budge. There are still villains around.");
            return;
        }
        passThrough(player);
    }
}
