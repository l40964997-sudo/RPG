package nl.rug.oop.rpg;

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

    private final Scanner scanner;
    private GameState state;
    private boolean running;

    public Game(GameState state) {
        this.state = state;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

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
        System.out.println("(2) Look for company");
        System.out.println("(3) Change difficulty");
        System.out.println("(4) Check Inventory");
        System.out.println("(5) Check Quests");
        System.out.println("(6) QuickSave");
        System.out.println("(7) QuickLoad");
        System.out.println("(8) Save");
        System.out.println("(9) Load");
        System.out.println("(10) Quit");
    }

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
                    System.out.println("Quicksave successful.");
                }
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
            default:
                System.out.println("Unknown option.");
        }
    }

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

    private void applyLoaded(GameState loaded, String label) {
        if (loaded != null) {
            this.state = loaded;
            System.out.println(label + " successful.");
        }
    }

    private int readInt() {
        try {
            return scanner.nextInt();
        } catch (NoSuchElementException eof) {
            running = false;
            return -1;
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Please enter a number.");
            return -1;
        }
    }

    private String readWord() {
        try {
            return scanner.next();
        } catch (NoSuchElementException eof) {
            running = false;
            return "";
        }
    }
}
