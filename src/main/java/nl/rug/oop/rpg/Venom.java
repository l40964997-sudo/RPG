package nl.rug.oop.rpg;

/**
 * Venom: Regenerates health at the start of each turn.
 */
public class Venom extends Villain {
    private static final long serialVersionUID = 1L;

    public Venom(String name, String description, int baseHealth, int baseDamage, Difficulty difficulty, Item drop) {
        super(name, description, baseHealth, baseDamage, difficulty, drop);
    }

    @Override
    public void onTurnStart() {
        int healAmount = 3;
        int newHealth = Math.min(getMaxHealth(), getHealth() + healAmount);
        setHealth(newHealth);
        System.out.println(getName() + "'s symbiote is regenerating... Recovered " + healAmount + " HP.");
    }
}