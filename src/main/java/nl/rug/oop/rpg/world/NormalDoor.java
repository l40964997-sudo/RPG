package nl.rug.oop.rpg.world;

import nl.rug.oop.rpg.entity.Player;
import nl.rug.oop.rpg.item.Item;

import java.io.Serializable;

/**
 * Plain door. Always lets the player through.
 */
public class NormalDoor extends Door implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Create a normal door with no reward.
     *
     * @param description text shown when the door is inspected.
     * @param destination room the player enters on use.
     */
    public NormalDoor(String description, Room destination) {
        super(description, destination);
    }

    /**
     * Create a normal door that grants a one-time reward.
     *
     * @param description text shown when the door is inspected.
     * @param destination room the player enters on use.
     * @param reward      item granted the first time the player passes through.
     */
    public NormalDoor(String description, Room destination, Item reward) {
        super(description, destination, reward);
    }

    /**
     * Move the player through, no questions asked.
     *
     * @param player the player using the door.
     */
    @Override
    public void interact(Player player) {
        System.out.println("You take that route.");
        passThrough(player);
    }
}
