package nl.rug.oop.rpg;

/**
 * RioMoralesSandwich: Heals the player.
 */
public class RioMoralesSandwich extends Item {
    private static final long serialVersionUID = 1L;
    private int healAmount;

    public RioMoralesSandwich(String name, String description, int healAmount) {
        super(name, description);
        this.healAmount = healAmount;
    }

    @Override
    public void use(Player player) {
        player.heal(healAmount);
        System.out.println("You ate the sandwich. Tastes like home! Recovered " + healAmount + " HP.");
    }
}