package nl.rug.oop.rpg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Stateless file IO service for save/load. Owns no game state;
 * every method is given everything it needs as a parameter.
 * Manages the {@value #SAVE_DIR} directory, validates filenames,
 * and (de)serializes {@link GameState} objects.
 * <p>
 * All methods print human-readable error messages on failure
 * (using {@link System#err} for IO errors) and never throw —
 * the calling code can simply check the boolean/null return
 * value and move on.
 */
public final class SaveManager {

    /** Directory where save files are stored. */
    public static final String SAVE_DIR = "savedgames";
    /** Reserved filename used by the QuickSave feature. */
    public static final String QUICKSAVE_FILE = "quicksave.ser";
    /** Required extension for save files. */
    public static final String EXTENSION = ".ser";

    /** Letters, digits, dash, underscore. 1 to 32 characters. */
    private static final Pattern VALID_NAME = Pattern.compile("[A-Za-z0-9_-]{1,32}");

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should only be accessed statically.
     */
    private SaveManager() {
    }

    /**
     * Make sure {@value #SAVE_DIR} exists. Called before any IO.
     *
     * @return a {@link File} pointing at the save directory.
     */
    private static File ensureDirectory() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists() && !dir.mkdir()) {
            System.err.println("Could not create save directory: " + SAVE_DIR);
        }
        return dir;
    }

    /**
     * Validate a user-supplied save name.
     *
     * @param name candidate name without extension.
     * @return {@code true} if the name is acceptable.
     */
    public static boolean isValidName(String name) {
        return name != null && VALID_NAME.matcher(name).matches();
    }

    /**
     * Save a snapshot to the given filename inside savedgames/.
     *
     * @param state    the snapshot to save
     * @param filename file name including .ser extension
     * @return true on success
     */
    public static boolean save(GameState state, String filename) {
        File dir = ensureDirectory();
        File target = new File(dir, filename);
        try (FileOutputStream fos = new FileOutputStream(target);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(state);
            return true;
        } catch (IOException e) {
            System.err.println("Could not save game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load a snapshot from the given filename inside savedgames/.
     *
     * @param filename file name including .ser extension
     * @return the loaded state, or null on failure
     */
    public static GameState load(String filename) {
        File target = new File(SAVE_DIR, filename);
        if (!target.exists()) {
            System.err.println("Save file does not exist: " + filename);
            return null;
        }
        try (FileInputStream fis = new FileInputStream(target);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            if (obj instanceof GameState) {
                return (GameState) obj;
            }
            System.err.println("Save file is corrupted: not a GameState.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Could not load game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Save to the canonical quicksave file.
     *
     * @param state the snapshot to save.
     * @return {@code true} on success.
     */
    public static boolean quickSave(GameState state) {
        return save(state, QUICKSAVE_FILE);
    }

    /**
     * Load from the canonical quicksave file.
     *
     * @return the loaded state, or {@code null} on failure.
     */
    public static GameState quickLoad() {
        return load(QUICKSAVE_FILE);
    }

    /**
     * List all save files (with extension) in alphabetical order.
     *
     * @return sorted list of {@value #EXTENSION} files in
     *         {@value #SAVE_DIR}, or an empty list if there are none.
     */
    public static List<String> listSaves() {
        File dir = ensureDirectory();
        File[] files = dir.listFiles((d, name) -> name.endsWith(EXTENSION));
        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }
        List<String> names = new ArrayList<>(files.length);
        for (File f : files) {
            names.add(f.getName());
        }
        Collections.sort(names);
        return names;
    }
}
