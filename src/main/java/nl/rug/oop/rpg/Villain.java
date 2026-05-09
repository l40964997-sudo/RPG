package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;
import java.util.Scanner;

@Getter
public abstract class Villain extends NPC implements Attackable, Serializable {

    private static final long serialVersionUID=1L;

    private final String name;
    private int health;
    private final int maxHealth;
    private final int damage;
    private final Item drop;

    public Villain(String name, String description, int baseHealth, int baseDamage,
                   Difficulty difficulty, Item drop) {
        super(description);
        this.name = name;
        this.maxHealth = (int) Math.round(baseHealth * difficulty.getVillainMultiplier());
        this.health = this.maxHealth;
        this.damage = (int) Math.round(baseDamage * difficulty.getVillainMultiplier());
        this.drop = drop;
    }

    @Override
    public boolean isDead(){
        return health<=0;
    }

    @Override
    public void takeDamage(int amount){
        if(amount<0) amount=0;
        health-=amount;
        if(health<0) health=0;
    }

    protected void heal(int amount){
        health+=amount;
        if(health>maxHealth) health=maxHealth;
    }



    /**
     * Reaction hook fired immediately after the player damages
     * this villain. Override to implement counter-attacks,
     * partial absorption, miss-chances, etc. Default: no-op.
     *
     * @param player the attacking player.
     * @param dealt  the amount of damage that was just applied.
     */
    protected void onPlayerAttack(Player player, int dealt) { }

    /**
     * Per-round passive hook fired at the start of every round
     * before the player picks an action. Override to implement
     * regeneration, summoning, ramping damage, etc. Default: no-op.
     */
    protected void onTurnStart() { }

    /**
     * The villain's offensive turn. Default implementation deals
     * {@link #getDamage()} damage to the player. Override for
     * variable attacks (bombs, multi-hits, status effects).
     *
     * @param player the player taking the hit.
     */
    protected void performAttack(Player player) {
        System.out.println(name + " strikes you for " + damage + " damage.");
        player.takeDamage(damage);
    }

    // --- Combat loop -------------------------------------------------------

    /**
     * Run the turn-based combat loop until either combatant dies
     * or the player flees. On the player's death the outer game
     * loop ends. On the villain's death this villain is removed
     * from its room, the quest log is notified, and any drop is
     * granted to the player.
     *
     * @param player the player engaging this villain.
     */
    @Override
    public void interact(Player player) {
        Scanner scanner = Game.getScannerInstance();
        Difficulty difficulty = Game.getDifficultyInstance();
        double playerMult = difficulty.getPlayerMultiplier();


        System.out.println();
        System.out.println("=== " + name + " stands in your way! ===");

        while (!isDead() && !player.isDead()) {
            onTurnStart();
            if (isDead()) break;

            System.out.println();
            System.out.println("Miles HP: " + player.getHealth() + "/" + player.getMaxHealth()
                    + "  |  Web: " + player.getWebCharges()
                    + "  |  Venom: " + player.getVenomCharges());
            System.out.println(name + " HP: " + health + "/" + maxHealth);
            System.out.println("What do you do?");
            System.out.println("  (0) Punch");
            System.out.println("  (1) Web Shot       [1 web]");
            System.out.println("  (2) Venom Strike   [1 venom]");
            System.out.println("  (3) Camouflage     [1 venom]");
            System.out.println("  (4) Use an item");
            System.out.println("  (5) Swing away (flee)");

            int choice = readInt(scanner);
            boolean villainGetsTurn = true;

            switch (choice) {
                case 0: {
                    int dealt = scaleDamage(player.getDamage(), playerMult);
                    System.out.println("You land a clean punch for " + dealt + " damage.");
                    takeDamage(dealt);
                    onPlayerAttack(player, dealt);
                    break;
                }
                case 1: {
                    if (!player.spendWebCharges(1)) {
                        System.out.println("Click. Out of web fluid!");
                        break;
                    }
                    int dealt = scaleDamage(player.getDamage() + 3, playerMult);
                    System.out.println("Thwip! You web-blast " + name + " for " + dealt + " damage.");
                    takeDamage(dealt);
                    onPlayerAttack(player, dealt);
                    break;
                }
                case 2: {
                    if (!player.spendVenomCharges(1)) {
                        System.out.println("You can't muster a venom blast right now.");
                        break;
                    }
                    int dealt = scaleDamage(player.getDamage() * 2, playerMult);
                    System.out.println("ZZZZAP! Venom strike hits for " + dealt + " damage.");
                    takeDamage(dealt);
                    onPlayerAttack(player, dealt);
                    break;
                }
                case 3: {
                    if (!player.spendVenomCharges(1)) {
                        System.out.println("Not enough venom energy to vanish.");
                        break;
                    }
                    System.out.println("You go invisible. The next attack will miss.");
                    player.setCamouflaged(true);
                    break;
                }
                case 4: {
                    if (player.getInventory().isEmpty()) {
                        System.out.println("You've got nothing on you. " + name + " grins.");
                        break;
                    }
                    showInventory(player);
                    System.out.println("Use which? (-1 to cancel)");
                    int idx = readInt(scanner);
                    if (idx >= 0 && idx < player.getInventory().size()) {
                        player.useItem(idx);
                    } else {
                        System.out.println("You hesitate.");
                        villainGetsTurn = false;
                    }
                    break;
                }
                case 5:
                    System.out.println("You shoot a web and swing out of the fight.");
                    return;
                default:
                    System.out.println("You fumble. " + name + " doesn't.");
            }

            if (villainGetsTurn && !isDead() && !player.isDead()) {
                performAttack(player);
            }
        }

        if (isDead()) {
            System.out.println();
            System.out.println("You took down " + name + "!");
            if (player.getCurrentRoom() != null) {
                player.getCurrentRoom().removeNpc(this);
            }
            player.getQuestLog().notifyVillainDefeated(name);
            if (drop != null) {
                System.out.println(name + " left something behind.");
                player.addItem(drop);
            }
        }
    }

    // --- Helpers -----------------------------------------------------------

    /**
     * Apply the player damage multiplier and clamp to a minimum
     * of 1, so on Ultimate the player still scratches the boss.
     *
     * @param base raw damage before multiplier.
     * @param mult player damage multiplier from current difficulty.
     * @return scaled damage value, never below 1.
     */
    private int scaleDamage(int base, double mult) {
        int dealt = (int) Math.round(base * mult);
        return dealt < 1 ? 1 : dealt;
    }

    /**
     * Print the player's inventory as a numbered list during combat.
     */
    private void showInventory(Player player) {
        System.out.println("Your stuff:");
        for (int i = 0; i < player.getInventory().size(); i++) {
            System.out.println("  (" + i + ") " + player.getInventory().get(i).getName());
        }
    }

    /**
     * Read an integer from the shared scanner, re-prompting on
     * non-numeric input. Treats EOF as a flee command so the
     * combat exits cleanly when stdin closes.
     */
    private int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            } catch (java.util.NoSuchElementException e) {
                return 5;
            }
        }
    }
}

