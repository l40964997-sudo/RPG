package nl.rug.oop.rpg;

/**
 * Plain door. Always lets the player through.
 */
public class NormalDoor extends Door {

    private static final long serialVersionUID = 1L;

    public NormalDoor(String description, Room destination) {
        super(description, destination);
    }

    public NormalDoor(String description, Room destination, Item reward) {
        super(description, destination, reward);
    }

    @Override
    public void interact(Player player) {
        System.out.println("You take that route.");
        passThrough(player);
    }
}
