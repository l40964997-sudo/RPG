package nl.rug.oop.rpg;

/**
 * KeyCard: A passive item used to open LockedDoors.
 */
public class KeyCard extends Item {
    private static final long serialVersionUID = 1L;

    /**
     * Construct a new keycard.
     *
     * @param name        display name, used by {@link LockedDoor}
     *                    for matching.
     * @param description descriptive text shown when inspected.
     */
    public KeyCard(String name, String description) {
        super(name, description);
    }

    /**
     * No-op effect. Prints a hint and puts the keycard back into
     * the player's inventory (since the framework already removed
     * it before calling this method).
     *
     * @param player the player who tried to use the keycard.
     */
    @Override
    public void use(Player player) {
        // KeyCards are usually passive, but we provide a message if used directly
        System.out.println("This is a " + getName()
                + ". It should be used to unlock specific doors, not used directly.");
        player.getInventory().add(this);
    }
} 