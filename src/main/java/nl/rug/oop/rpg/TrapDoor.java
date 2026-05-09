package nl.rug.oop.rpg;

/**
 * Damages the player on use. If the damage would kill, spider-sense
 * kicks in and the door refuses passage.
 */
public class TrapDoor extends Door {

    private static final long serialVersionUID = 1L;

    private final int trapDamage;

    public TrapDoor(String description, Room destination, int trapDamage) {
        super(description, destination);
        this.trapDamage = trapDamage;
    }

    public TrapDoor(String description, Room destination, int trapDamage, Item reward) {
        super(description, destination, reward);
        this.trapDamage = trapDamage;
    }

    @Override
    public void interact(Player player) {
        if (player.getHealth() - trapDamage <= 0) {
            System.out.println("Spider-sense tingles! That door would be the end of you. You back off.");
            return;
        }
        System.out.println("It's a trap! You take " + trapDamage + " damage going through.");
        player.takeDamage(trapDamage);
        passThrough(player);
    }
}
