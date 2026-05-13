package nl.rug.oop.rpg.controller;

import nl.rug.oop.rpg.entity.Player;
import nl.rug.oop.rpg.entity.villain.Villain;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Orchestrates a single fight between a {@link Player} and a
 * {@link Villain}. Owns the round loop, the action menu, and
 * the input parsing. Reads the scanner and difficulty from the
 * player's injected runtime context, so it depends on no static
 * global.
 * <p>
 * Created fresh by {@link Villain#interact(Player)} every time a
 * fight starts. It is intentionally short-lived: no state needs
 * to survive between fights.
 */
public class CombatController {
    /** Sentinel menu choice that means "flee the fight". */
    private static final int FLEE_CHOICE = 5;

    /** The player engaged in this fight. */
    private final Player player;
    /** The villain on the other side of this fight. */
    private final Villain villain;
    /** Shared input scanner pulled from the player's runtime context. */
    private final Scanner scanner;
    /** Cached player damage multiplier for this fight. */
    private final double playerMult;

    /**
     * Construct a controller for a single fight.
     *
     * @param player  the engaging player; must have its runtime
     *                dependencies (scanner, difficulty) already injected.
     * @param villain the villain being fought.
     */
    public CombatController(Player player, Villain villain) {
        this.player = player;
        this.villain = villain;
        this.scanner = player.getScanner();
        this.playerMult = player.getDifficulty().getPlayerMultiplier();
    }

    /**
     * Run the fight from announcement through defeat cleanup.
     * Exits when the player flees or either combatant dies.
     */
    public void run() {
        announce();
        loop();
        if (villain.isDead()) {
            villain.handleDefeat(player);
        }
    }

    /** Print the combat-start banner. */
    private void announce() {
        System.out.println();
        System.out.println("=== " + villain.getName() + " stands in your way! ===");
    }

    /** Drive the round-by-round loop until death or flee. */
    private void loop() {
        while (!villain.isDead() && !player.isDead()) {
            villain.onTurnStart();
            if (villain.isDead()) {
                break;
            }
            printRoundStatus();
            printMenu();
            int choice = readInt();
            if (choice == FLEE_CHOICE) {
                System.out.println("You shoot a web and swing out of the fight.");
                return;
            }
            boolean villainGetsTurn = dispatch(choice);
            if (villainGetsTurn && !villain.isDead() && !player.isDead()) {
                villain.performAttack(player);
            }
        }
    }

    /** Print the per-round HP / resource summary. */
    private void printRoundStatus() {
        System.out.println();
        System.out.println("Miles HP: " + player.getHealth() + "/" + player.getMaxHealth()
                + "  |  Web: " + player.getWebCharges()
                + "  |  Venom: " + player.getVenomCharges());
        System.out.println(villain.getName()
                + " HP: " + villain.getHealth() + "/" + villain.getMaxHealth());
    }

    /** Print the action menu. */
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
     * @param choice the menu option the player selected.
     * @return {@code true} if the villain should retaliate this round.
     */
    private boolean dispatch(int choice) {
        switch (choice) {
            case 0:
                player.punch(villain, playerMult);
                return true;
            case 1:
                player.webShot(villain, playerMult);
                return true;
            case 2:
                player.venomStrike(villain, playerMult);
                return true;
            case 3:
                player.camouflage();
                return true;
            case 4:
                return useItem();
            default:
                System.out.println("You fumble. " + villain.getName() + " doesn't.");
                return true;
        }
    }

    /**
     * Inventory sub-menu. Lets the player pick an item to use mid-combat
     * or back out without spending a turn.
     *
     * @return {@code true} if a turn was spent, {@code false} if cancelled.
     */
    private boolean useItem() {
        if (player.getInventory().isEmpty()) {
            System.out.println("You've got nothing on you. " + villain.getName() + " grins.");
            return true;
        }
        player.showInventoryForCombat();
        System.out.println("Use which? (-1 to cancel)");
        int idx = readInt();
        if (idx >= 0 && idx < player.getInventory().size()) {
            player.useItem(idx);
            return true;
        }
        System.out.println("You hesitate.");
        return false;
    }

    /**
     * Read an integer from the shared scanner, re-prompting on
     * non-numeric input. Treats EOF as a flee command.
     *
     * @return the parsed integer, or {@link #FLEE_CHOICE} on EOF.
     */
    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            } catch (NoSuchElementException e) {
                return FLEE_CHOICE;
            }
        }
    }
}
