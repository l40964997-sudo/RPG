package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;
import java.util.Scanner;

/**
 * Base class for all named villains in the game. Provides the
 * shared turn-based combat loop in {@link #interact(Player)} and
 * exposes three protected hooks that subclasses override to add
 * unique combat behavior:
 * <ul>
 *   <li>{@link #performAttack(Player)} — the villain's turn.
 *       Default: a plain attack for {@link #getDamage()} damage.</li>
 *   <li>{@link #onPlayerAttack(Player, int)} — called <em>after</em>
 *       the player damages this villain. Default: no-op.</li>
 *   <li>{@link #onTurnStart()} — called at the start of every
 *       round. Default: no-op.</li>
 * </ul>
 * Stats are scaled by the current {@link Difficulty} once at
 * construction; changing difficulty later does not retroactively
 * rescale already-spawned villains.
 * <p>
 * On death the villain notifies the player's {@link Questlog} so
 * defeat-based quests can update, and grants an optional drop.
 */
@Getter
public abstract class Villain extends NPC implements Attackable, Serializable {

    private static final long serialVersionUID = 1L;

    /** Display name shown in combat banners and quest events. */
    private final String name;
    /** Current hit points; reaches zero on defeat. */
    private int health;
    /** Upper bound for {@link #health}; set once after difficulty scaling. */
    private final int maxHealth;
    /** Per-attack damage dealt by the default {@link #performAttack(Player)}. */
    private final int damage;
    /** Optional item awarded on defeat; {@code null} means no drop. */
    private final Item drop;

    /**
     * Construct a new Villain.
     *
     * @param name        short display name (e.g. {@code "Goblin"}).
     * @param description longer text shown when inspecting this villain.
     * @param baseHealth  pre-difficulty HP value.
     * @param baseDamage  pre-difficulty per-attack damage.
     * @param difficulty  current game difficulty; scales the stats above.
     * @param drop        optional item awarded on defeat; may be {@code null}.
     */
    public Villain(String name, String description, int baseHealth, int baseDamage,
                   Difficulty difficulty, Item drop) {
        super(description);
        this.name = name;
        this.maxHealth = (int) Math.round(baseHealth * difficulty.getVillainMultiplier());
        this.health = this.maxHealth;
        this.damage = (int) Math.round(baseDamage * difficulty.getVillainMultiplier());
        this.drop = drop;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true} once {@link #health} is zero or below.
     */
    @Override
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Negative inputs are clamped to zero, and {@link #health} is
     * floored at zero so display values never go negative.
     *
     * @param amount raw incoming damage; values below zero count as zero.
     */
    @Override
    public void takeDamage(int amount) {
        if (amount < 0) {
            amount = 0;
        }
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Restore HP, clamped to {@link #maxHealth}.
     *
     * @param amount HP to restore; negative values reduce health.
     */
    protected void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    /**
     * Reaction hook fired immediately after the player damages
     * this villain. Override for counter-attacks, partial
     * absorption, miss-chances, etc. Default: no-op.
     *
     * @param player the attacking player.
     * @param dealt  the amount of damage that was just applied.
     */
    protected void onPlayerAttack(Player player, int dealt) { }

    /**
     * Per-round passive hook fired at the start of every round
     * before the player picks an action. Override for regeneration,
     * summoning, ramping damage, etc. Default: no-op.
     */
    protected void onTurnStart() { }

    /**
     * Damage-modifier hook fired before the player's incoming
     * damage is applied. Default returns the damage unchanged.
     *
     * @param damage the incoming damage value.
     * @return the (possibly modified) damage actually applied.
     */
    protected int onPlayerAttack(int damage) {
        return damage;
    }

    /**
     * The villain's offensive turn. Default deals {@link #getDamage()}
     * damage to the player. Override for variable attacks.
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
     * or the player flees. Delegates to {@link #runCombatLoop(Player)}
     * for the round-by-round logic and {@link #handleDefeat(Player)}
     * for post-victory cleanup.
     *
     * @param player the player engaging this villain.
     */
    @Override
    public void interact(Player player) {
        announceFight();
        runCombatLoop(player);
        if (isDead()) {
            handleDefeat(player);
        }
    }

    /**
     * Print the combat-start banner.
     */
    private void announceFight() {
        System.out.println();
        System.out.println("=== " + name + " stands in your way! ===");
    }

    /**
     * Drive the round-by-round loop. Exits on death of either
     * combatant or when the player picks "flee".
     *
     * @param player the player engaging this villain.
     */
    private void runCombatLoop(Player player) {
        Scanner scanner = Game.getScannerInstance();
        double playerMult = Game.getDifficultyInstance().getPlayerMultiplier();
        while (!isDead() && !player.isDead()) {
            onTurnStart();
            if (isDead()) {
                break;
            }
            printRoundStatus(player);
            printMenu();
            int choice = readInt(scanner);
            if (choice == 5) {
                System.out.println("You shoot a web and swing out of the fight.");
                return;
            }
            boolean villainGetsTurn = handleChoice(player, scanner, choice, playerMult);
            if (villainGetsTurn && !isDead() && !player.isDead()) {
                performAttack(player);
            }
        }
    }

    /**
     * Print the per-round HP / resource summary.
     *
     * @param player the player whose stats are shown.
     */
    private void printRoundStatus(Player player) {
        System.out.println();
        System.out.println("Miles HP: " + player.getHealth() + "/" + player.getMaxHealth()
                + "  |  Web: " + player.getWebCharges()
                + "  |  Venom: " + player.getVenomCharges());
        System.out.println(name + " HP: " + health + "/" + maxHealth);
    }

    /**
     * Print the action menu.
     */
    private void printMenu() {
        System.out.println("What do you do?");
        System.out.println("  (0) Punch");
        System.out.println("  (1) Web Shot       [1 web]");
        System.out.println("  (2) Venom Strike   [1 venom]");
        System.out.println("  (3) Camouflage     [1 venom]");
        System.out.println("  (4) Use an item");
        System.out.println("  (5) Swing away (flee)");
    }

    /**
     * Dispatch the player's chosen action.
     *
     * @param player     the acting player.
     * @param scanner    shared input scanner (needed by item-use).
     * @param choice     the menu option the player selected.
     * @param playerMult cached player damage multiplier.
     * @return {@code true} if the villain should retaliate this round.
     */
    private boolean handleChoice(Player player, Scanner scanner, int choice, double playerMult) {
        switch (choice) {
            case 0:
                doPunch(player, playerMult);
                return true;
            case 1:
                doWebShot(player, playerMult);
                return true;
            case 2:
                doVenomStrike(player, playerMult);
                return true;
            case 3:
                doCamouflage(player);
                return true;
            case 4:
                return doUseItem(player, scanner);
            default:
                System.out.println("You fumble. " + name + " doesn't.");
                return true;
        }
    }

    /**
     * Plain melee attack.
     *
     * @param player     the attacking player.
     * @param playerMult cached player damage multiplier.
     */
    private void doPunch(Player player, double playerMult) {
        int dealt = scaleDamage(player.getDamage(), playerMult);
        System.out.println("You land a clean punch for " + dealt + " damage.");
        takeDamage(dealt);
        onPlayerAttack(player, dealt);
    }

    /**
     * Web-shot attack: costs 1 web charge, +3 base damage.
     *
     * @param player     the attacking player.
     * @param playerMult cached player damage multiplier.
     */
    private void doWebShot(Player player, double playerMult) {
        if (!player.spendWebCharges(1)) {
            System.out.println("Click. Out of web fluid!");
            return;
        }
        int dealt = scaleDamage(player.getDamage() + 3, playerMult);
        System.out.println("Thwip! You web-blast " + name + " for " + dealt + " damage.");
        takeDamage(dealt);
        onPlayerAttack(player, dealt);
    }

    /**
     * Venom strike: costs 1 venom charge, doubles base damage.
     *
     * @param player     the attacking player.
     * @param playerMult cached player damage multiplier.
     */
    private void doVenomStrike(Player player, double playerMult) {
        if (!player.spendVenomCharges(1)) {
            System.out.println("You can't muster a venom blast right now.");
            return;
        }
        int dealt = scaleDamage(player.getDamage() * 2, playerMult);
        System.out.println("ZZZZAP! Venom strike hits for " + dealt + " damage.");
        takeDamage(dealt);
        onPlayerAttack(player, dealt);
    }

    /**
     * Camouflage: costs 1 venom charge, makes the next incoming
     * attack miss.
     *
     * @param player the player going invisible.
     */
    private void doCamouflage(Player player) {
        if (!player.spendVenomCharges(1)) {
            System.out.println("Not enough venom energy to vanish.");
            return;
        }
        System.out.println("You go invisible. The next attack will miss.");
        player.setCamouflaged(true);
    }

    /**
     * Inventory sub-menu. Lets the player pick an item to use mid-combat
     * or back out without spending a turn.
     *
     * @param player  the player whose inventory is consulted.
     * @param scanner shared input scanner.
     * @return {@code true} if a turn was spent (item used or empty bag),
     *         {@code false} if the player cancelled.
     */
    private boolean doUseItem(Player player, Scanner scanner) {
        if (player.getInventory().isEmpty()) {
            System.out.println("You've got nothing on you. " + name + " grins.");
            return true;
        }
        showInventory(player);
        System.out.println("Use which? (-1 to cancel)");
        int idx = readInt(scanner);
        if (idx >= 0 && idx < player.getInventory().size()) {
            player.useItem(idx);
            return true;
        }
        System.out.println("You hesitate.");
        return false;
    }

    /**
     * Post-victory cleanup: remove this villain from its room,
     * notify the quest log, and award the drop if any.
     *
     * @param player the victorious player.
     */
    private void handleDefeat(Player player) {
        System.out.println();
        System.out.println("You took down " + name + "!");
        if (player.getCurrentRoom() != null) {
            player.getCurrentRoom().removeNpc(this);
        }
        player.getQuestlog().notifyVillainDefeated(name);
        if (drop != null) {
            System.out.println(name + " left something behind.");
            player.addItem(drop);
        }
    }

    // --- Helpers -----------------------------------------------------------

    /**
     * Apply the player damage multiplier and clamp to a minimum of 1,
     * so on Ultimate the player still scratches the boss.
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
     *
     * @param player the player whose inventory is being shown.
     */
    private void showInventory(Player player) {
        System.out.println("Your stuff:");
        for (int i = 0; i < player.getInventory().size(); i++) {
            System.out.println("  (" + i + ") " + player.getInventory().get(i).getName());
        }
    }

    /**
     * Read an integer from the shared scanner, re-prompting on
     * non-numeric input. Treats EOF as a flee command so combat
     * exits cleanly when stdin closes.
     *
     * @param scanner the shared input scanner.
     * @return the parsed integer, or {@code 5} (flee) if stdin closed.
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