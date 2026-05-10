package nl.rug.oop.rpg;

import java.util.List;

/**
 * Locked. Requires a specific item by name in the player's inventory;
 * the item is consumed when the door is used.
 */
public class LockedDoor extends Door {

    private static final long serialVersionUID = 1L;

    /** Display name of the item that opens this door. */
    private final String requiredItemName;

    /**
     * Create a locked door with no reward.
     *
     * @param description      text shown when the door is inspected.
     * @param destination      room behind the door.
     * @param requiredItemName name of the item the player must hold;
     *                         match is case-insensitive.
     */
    public LockedDoor(String description, Room destination, String requiredItemName) {
        super(description, destination);
        this.requiredItemName = requiredItemName;
    }

    /**
     * Create a locked door that grants a reward on first passage.
     *
     * @param description      text shown when the door is inspected.
     * @param destination      room behind the door.
     * @param reward           item granted on first successful passage.
     * @param requiredItemName name of the item the player must hold.
     */
    public LockedDoor(String description, Room destination,
                      String requiredItemName, Item reward) {
        super(description, destination, reward);
        this.requiredItemName = requiredItemName;
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
        List<Item> inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getName().equalsIgnoreCase(requiredItemName)) {
                Item used = inventory.remove(i);
                System.out.println("You use the " + used.getName() + ". The door clicks open.");
                passThrough(player);
                return;
            }
        }
        System.out.println("Locked. You'd need a " + requiredItemName + " to get through.");
    }
}
