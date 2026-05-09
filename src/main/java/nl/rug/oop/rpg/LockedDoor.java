package nl.rug.oop.rpg;

import java.util.List;

/**
 * Locked. Requires a specific item by name in the player's inventory;
 * the item is consumed when the door is used.
 */
public class LockedDoor extends Door {

    private static final long serialVersionUID = 1L;

    private final String requiredItemName;

    public LockedDoor(String description, Room destination, String requiredItemName) {
        super(description, destination);
        this.requiredItemName = requiredItemName;
    }

    public LockedDoor(String description, Room destination,
                      String requiredItemName, Item reward) {
        super(description, destination, reward);
        this.requiredItemName = requiredItemName;
    }

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
