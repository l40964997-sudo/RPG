package nl.rug.oop.rpg.world;

import nl.rug.oop.rpg.entity.Player;
import nl.rug.oop.rpg.item.Item;
import nl.rug.oop.rpg.item.KeyCard;

import java.io.Serializable;

/**
 * Locked. Requires a specific item by name in the player's inventory;
 * the item is consumed when the door is used.
 */
public class LockedDoor extends Door implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The keycard that opens this door. Compared by name on use. */
    private final KeyCard requiredKey;

    /**
     * Create a locked door with no reward.
     *
     * @param description text shown when the door is inspected.
     * @param destination room behind the door.
     * @param requiredKey the {@link KeyCard} that opens this door;
     *                    must not be {@code null}.
     * @throws IllegalArgumentException if {@code requiredKey} is null.
     */
    public LockedDoor(String description, Room destination, KeyCard requiredKey) {
        super(description, destination);
        if (requiredKey == null) {
            throw new IllegalArgumentException("requiredKey must not be null");
        }
        this.requiredKey = requiredKey;
    }

    /**
     * Constructs a locked door that grants a reward upon first passage.
     *
     * @param description text shown when the door is inspected.
     * @param destination the room this door leads to.
     * @param requiredKey the {@link KeyCard} needed to unlock this door; must not be null.
     * @param reward      the item granted to the player upon successful entry.
     * @throws IllegalArgumentException if {@code requiredKey} is null.
     */
    public LockedDoor(String description, Room destination,
                      KeyCard requiredKey, Item reward) {
        super(description, destination, reward);
        if (requiredKey == null ) {
            throw new IllegalArgumentException("requiredItemName must be non-blank");
        }
        this.requiredKey = requiredKey;
    }

    /**
     * Get the keycard required to open this door.
     *
     * @return the required {@link KeyCard}.
     */
    public KeyCard getRequiredKey() {
        return requiredKey;
    }

    /**
     * Search the inventory for an item whose name matches the
     * required item. If found, consume it and pass through; if
     * not, print a hint and refuse.
     *
     * @param player the player using the door.
     */
    @Override
    public void interact(Player player) {
        Item used = player.takeItemByName(requiredKey.getName());
        if (used == null) {
            System.out.println("Locked. You'd need a " + requiredKey.getName() + " to get through.");
            return;
        }
        System.out.println(".You use the \" + used.getName() + \". The door clicks open");
        passThrough(player);
    }
}
