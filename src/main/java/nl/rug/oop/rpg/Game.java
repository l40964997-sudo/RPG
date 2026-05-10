package nl.rug.oop.rpg;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Top-level menu loop. Owns the Scanner and the current GameState,
 * delegates persistence to SaveManager, and routes user input to the
 * right handler. The Scanner stays here (and only here) so it never
 * ends up inside something Serializable.
 */
public class Game {

    /** The running Game instance, set by the constructor. */
    private static Game instance;     // <-- ADD THIS LINE
    /** Shared input scanner; re-used by combat code. */
    private final Scanner scanner;
    /** Current persistable game state (player + difficulty). */
    private GameState state;
    /** Main loop continues while this is true. */
    private boolean running;

    /**
     * Construct a new Game wrapping the given initial state.
     *
     * @param state the starting state; must contain a player whose
     *              {@code currentRoom} is set.
     */
    public Game(GameState state) {
        this.state = state;
        this.scanner = new Scanner(System.in);
        this.running = true;
        instance = this;
    }

    /**
     * Get the shared scanner of the running Game.
     *
     * @return the running Game's scanner; combat code reads from
     *         this instead of constructing its own Scanner.
     */
    public static Scanner getScannerInstance() {
        return instance.scanner;
    }

    /**
     * Get the active difficulty of the running Game.
     *
     * @return the running Game's current difficulty.
     */
    public static Difficulty getDifficultyInstance() {
        return instance.state.getDifficulty();
    }

    /**
     * Run the main menu loop until the player dies or quits.
     */
    public void run() {
        Player player = state.getPlayer();
        System.out.println("Welcome, " + player.getName() + "!");
        while (running && !player.isDead()) {
            printMainMenu();
            int choice = readInt();
            handleMainChoice(choice);
            player = state.getPlayer();
        }
        if (player.isDead()) {
            System.out.println("Game Over!");
        }
    }

    /** Print the top-level menu. */
    private void printMainMenu() {
        Player player = state.getPlayer();
        System.out.println();
        System.out.println(player.getName()
                + " | HP: " + player.getHealth() + "/" + player.getMaxHealth()
                + " | DMG: " + player.getDamage()
                + " | Web: " + player.getWebCharges()
                + " | Venom: " + player.getVenomCharges()
                + " | Difficulty: " + state.getDifficulty().getLabel());
        System.out.println("What do you want to do?");
        System.out.println("(0) Look around");
        System.out.println("(1) Look for a way out");
        System.out.println("(2) Check who's there");
        System.out.println("(3) Change difficulty");
        System.out.println("(4) Check Inventory");
        System.out.println("(5) Check Quests");
        System.out.println("(6) QuickSave");
        System.out.println("(7) QuickLoad");
        System.out.println("(8) Save");
        System.out.println("(9) Load");
        System.out.println("(10) Quit");
    }

    /**
     * Dispatch the user's top-level menu choice.
     *
     * @param choice the menu option selected by the user.
     */
    private void handleMainChoice(int choice) {
        switch (choice) {
            case 0:
                state.getPlayer().getCurrentRoom().inspect();
                break;
            case 1:
                handleDoors();
                break;
            case 2:
                handleNpcs();
                break;
            case 3:
                changeDifficulty();
                break;
            case 4:
                handleInventory();
                break;
            case 5:
                showQuests();
                break;
            case 6:
                if (SaveManager.quickSave(state)) {
                    System.out.println("Quicksave successful.");}
                break;
            case 7:
                applyLoaded(SaveManager.quickLoad(), "Quickload");
                break;
            case 8:
                handleNamedSave();
                break;
            case 9:
                handleNamedLoad();
                break;
            case 10:
                running = false;
                System.out.println("Farewell.");
                break;
            default: System.out.println("Unknown option.");
        }
    }

    /** Handle the quicksave operation. */
    private void handleQuickSave() {
        if (SaveManager.quickSave(state)) {
            System.out.println("Quicksave successful.");
        }
    }

    /** Show available doors and let the player choose one. */
    private void handleDoors() {
        List<Door> doors = state.getPlayer().getCurrentRoom().getDoors();
        if (doors.isEmpty()) {
            System.out.println("There are no doors here.");
            return;
        }
        System.out.println("You look around for doors. You see:");
        for (int i = 0; i < doors.size(); i++) {
            System.out.print("(" + i + ") ");
            doors.get(i).inspect();
        }
        System.out.println("Which door do you take? (-1: stay here)");
        int choice = readInt();
        if (choice == -1) {
            return;
        }
        if (choice < 0 || choice >= doors.size()) {
            System.out.println("There is no such door.");
            return;
        }
        doors.get(choice).interact(state.getPlayer());
    }

    /** Show NPCs in the current room and let the player interact. */
    private void handleNpcs() {
        List<NPC> npcs = state.getPlayer().getCurrentRoom().getNpcs();
        if (npcs.isEmpty()) {
            System.out.println("You are alone here.");
            return;
        }
        System.out.println("You look if there's someone here. You see:");
        for (int i = 0; i < npcs.size(); i++) {
            System.out.print("(" + i + ") ");
            npcs.get(i).inspect();
        }
        System.out.println("Interact? (-1: do nothing)");
        int choice = readInt();
        if (choice == -1) {
            return;
        }
        if (choice < 0 || choice >= npcs.size()) {
            System.out.println("Nobody by that number.");
            return;
        }
        npcs.get(choice).interact(state.getPlayer());
    }

    /** Prompt for a new difficulty and apply it. */
    private void changeDifficulty() {
        Difficulty[] all = Difficulty.values();
        System.out.println("Choose difficulty:");
        for (int i = 0; i < all.length; i++) {
            System.out.println("(" + i + ") " + all[i].getLabel());
        }
        int choice = readInt();
        if (choice < 0 || choice >= all.length) {
            System.out.println("Invalid difficulty.");
            return;
        }
        state = new GameState(state.getPlayer(), all[choice]);
        System.out.println("Difficulty set to " + all[choice].getLabel() + ".");
    }

    /** Show inventory and let the player use one item. */
    private void handleInventory() {
        List<Item> inventory = state.getPlayer().getInventory();
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
            return;
        }
        System.out.println("Inventory:");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println("(" + i + ") " + inventory.get(i).getName()
                    + " - " + inventory.get(i).getDescription());
        }
        System.out.println("Use which item? (-1: cancel)");
        int choice = readInt();
        if (choice == -1) {
            return;
        }
        state.getPlayer().useItem(choice);
    }

    /** Print every quest, completed or not, with full description. */
    private void showQuests() {
        Questlog log = state.getPlayer().getQuestlog();
        if (log.isEmpty()) {
            System.out.println("You have no active quests.");
            return;
        }
        System.out.println("Quests:");
        for (Quest q : log.getQuests()) {
            q.inspect();
        }
    }

    /** Prompt for a filename and save under that name. */
    private void handleNamedSave() {
        System.out.println("Filename? (no extension)");
        String name = readWord();
        if (!SaveManager.isValidName(name)) {
            System.err.println("Invalid filename. Use 1-32 letters, digits, '_' or '-'.");
            return;
        }
        if (SaveManager.save(state, name + SaveManager.EXTENSION)) {
            System.out.println("Save successful.");
        }
    }

    /** Show the list of save files and load the chosen one. */
    private void handleNamedLoad() {
        List<String> saves = SaveManager.listSaves();
        if (saves.isEmpty()) {
            System.out.println("No save files found.");
            return;
        }
        System.out.println("Which file? (-1: none)");
        for (int i = 0; i < saves.size(); i++) {
            System.out.println("(" + i + ") " + saves.get(i));
        }
        int choice = readInt();
        if (choice == -1) {
            return;
        }
        if (choice < 0 || choice >= saves.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        applyLoaded(SaveManager.load(saves.get(choice)), "Load");
    }

    /**
     * Replace the running state with a loaded snapshot. After a
     * successful load, recalculates the completed-quest baseline
     * so we don't accidentally re-announce already-completed quests.
     *
     * @param loaded the deserialized state, or {@code null} on failure.
     * @param label  human-readable verb ("Load" or "Quickload") for the message.
     */
    private void applyLoaded(GameState loaded, String label) {
        if (loaded != null) {
            this.state = loaded;
            System.out.println(label + " successful.");
        }
    }

    /**
     * Read one integer from input, re-prompting on bad input.
     * Reads a full line and parses it so the trailing newline is
     * consumed cleanly — no leftover bytes for the next call.
     *
     * @return parsed integer, or {@code -1} if the input stream
     *         closes (in which case the running flag is also cleared).
     */
    private int readInt() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Please enter a number.");
            return -1;
        } catch (NoSuchElementException eof) {
            running = false;
            return -1;
        }
    }

    /**
     * Read one trimmed line of input.
     *
     * @return trimmed line, or empty string if the stream closes.
     */
    private String readWord() {
        try {
            return scanner.next();
        } catch (NoSuchElementException eof) {
            running = false;
            return "";
        }
    }
}
