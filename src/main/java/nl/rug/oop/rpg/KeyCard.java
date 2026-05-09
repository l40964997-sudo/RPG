package nl.rug.oop.rpg;

/**
 * KeyCard: A passive item used to open LockedDoors.
 */
public class KeyCard extends Item {
    private static final long serialVersionUID = 1L;

    public KeyCard(String name, String description) {
        super(name, description);
    }

    @Override
    public void use(Player player) {
        // KeyCards are usually passive, but we provide a message if used directly
        System.out.println("This is a " + getName() + ". It should be used to unlock specific doors, not used directly.");
    }
} 