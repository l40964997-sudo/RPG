package nl.rug.oop.rpg;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fluent builder for {@link World}. Lets {@link Main} (or any other
 * caller) describe the game map without hardcoding it into a string
 * of static helper methods. Each room is registered under a string
 * key so doors, NPCs, and the starting position can be wired by
 * reference rather than by smuggling local variables through helper
 * signatures.
 * <p>
 * Methods throw {@link IllegalStateException} for missing keys and
 * on {@link #build()} if no starting room was set, so configuration
 * mistakes fail loudly at startup rather than as null pointers
 * mid-game.
 */
public class WorldBuilder {
    /** Insertion-ordered registry of rooms by builder key. */
    private final Map<String, Room> rooms = new LinkedHashMap<>();
    /** Key of the room the player should start in; null until set. */
    private String startKey;

    /**
     * Register a new room under the given key.
     *
     * @param key         unique identifier for later lookup.
     * @param description text shown when the room is inspected.
     * @return {@code this} for chaining.
     * @throws IllegalArgumentException if the key is already in use.
     */
    public WorldBuilder addRoom(String key, String description) {
        if (rooms.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate room key: " + key);
        }
        rooms.put(key, new Room(description));
        return this;
    }

    /**
     * Add a pre-built door to the named source room.
     *
     * @param fromKey key of the room the door lives in.
     * @param door    the door to add.
     * @return {@code this} for chaining.
     * @throws IllegalStateException if {@code fromKey} is unknown.
     */
    public WorldBuilder addDoor(String fromKey, Door door) {
        require(fromKey).addDoor(door);
        return this;
    }

    /**
     * Add a pre-built NPC to the named room.
     *
     * @param roomKey key of the room the NPC lives in.
     * @param npc     the NPC to add.
     * @return {@code this} for chaining.
     * @throws IllegalStateException if {@code roomKey} is unknown.
     */
    public WorldBuilder addNpc(String roomKey, NPC npc) {
        require(roomKey).addNpc(npc);
        return this;
    }

    /**
     * Designate the room the player will start in.
     *
     * @param key key of the starting room.
     * @return {@code this} for chaining.
     * @throws IllegalStateException if {@code key} is unknown.
     */
    public WorldBuilder startAt(String key) {
        require(key);
        this.startKey = key;
        return this;
    }

    /**
     * Look up a registered room by key, for callers that need to
     * pass it directly into a constructor.
     *
     * @param key the builder key.
     * @return the room registered under that key.
     * @throws IllegalStateException if {@code key} is unknown.
     */
    public Room room(String key) {
        return require(key);
    }

    /**
     * Finish construction and produce a {@link World}.
     *
     * @return the assembled world.
     * @throws IllegalStateException if {@link #startAt(String)} was
     *                               never called.
     */
    public World build() {
        if (startKey == null) {
            throw new IllegalStateException("startAt(...) was not called");
        }
        return new World(rooms, rooms.get(startKey));
    }

    /**
     * Look up a room and throw a clear error if the key is unknown.
     *
     * @param key the room key to resolve.
     * @return the registered room.
     * @throws IllegalStateException if no room is registered under {@code key}.
     */
    private Room require(String key) {
        Room r = rooms.get(key);
        if (r == null) {
            throw new IllegalStateException("Unknown room key: " + key);
        }
        return r;
    }
}
