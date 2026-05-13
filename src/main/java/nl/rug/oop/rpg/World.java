package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Aggregate view of every room in the game plus the player's
 * starting room. Built once by {@link WorldBuilder} and held by
 * {@link GameState} so later code can look up rooms by name
 * without having to walk the door graph from the starting room.
 */
@Getter
public class World implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Insertion-ordered map of room key to room instance. */
    private final Map<String, Room> rooms;
    /** The starting room (where the player begins). */
    private final Room startingRoom;

    /**
     * Construct a world. Normally only called by {@link WorldBuilder}.
     *
     * @param rooms        insertion-ordered map of key to room;
     *                     copied defensively.
     * @param startingRoom the room the player begins in;
     *                     must be a value in {@code rooms}.
     */
    public World(Map<String, Room> rooms, Room startingRoom) {
        this.rooms = new LinkedHashMap<>(rooms);
        this.startingRoom = startingRoom;
    }

    /**
     * Look up a room by its builder key.
     *
     * @param key the key used when the room was added to the builder.
     * @return the room, or {@code null} if no such key exists.
     */
    public Room getRoom(String key) {
        return rooms.get(key);
    }

    /**
     * Read-only view of every room in the world.
     *
     * @return unmodifiable map of room key to room instance.
     */
    public Map<String, Room> getAllRooms() {
        return Collections.unmodifiableMap(rooms);
    }
}
