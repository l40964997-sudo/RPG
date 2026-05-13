package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a distinct location or zone within the game world (NYC).
 * <p>
 * This class acts as a central node in the game's map. It manages the state
 * of a single area by containing the environmental description, a dynamic list
 * of available exits ({@link Door}s), and a list of characters currently
 * present ({@link NPC}s).
 * <p>
 * Beyond just holding data, the Room actively manages its contents. It provides
 * methods to dynamically update the area (such as removing defeated enemies) and
 * can evaluate its own state, like checking if all hostile entities have been
 * defeated ({@link #villainCleared()}) to allow passage through guarded doors.
 */
@Getter
public class Room implements Inspectable, Serializable {

    private static final long serialVersionUID= 1L;
    /** Text shown when the player looks around. */
    private final String description;
    /** Live list of doors leading out of this room. */
    private final List<Door> doors;
    /** Live list of NPCs currently in this room. */
    private final List<NPC> npcs;

    /**
     * Construct a new Room.
     *
     * @param description text shown when the player looks around.
     */
    public Room(String description) {
        this.description = description;
        this.doors = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    /**
     * Get an unmodifiable view of the doors in this room.
     * Mutations must go through {@link #addDoor(Door)}.
     *
     * @return read-only list of doors.
     */
    public List<Door> getDoors() {
        return Collections.unmodifiableList(doors);
    }

    /**
     * Get an unmodifiable view of the NPCs in this room.
     * Mutations must go through {@link #addNpc(NPC)} or
     * {@link #removeNpc(NPC)}.
     *
     * @return read-only list of NPCs.
     */
    public List<NPC> getNpcs() {
        return Collections.unmodifiableList(npcs);
    }

    /**
     * Add an exit to this room.
     *
     * @param door the door to add.
     */
    public void addDoor(Door door) {
        doors.add(door);
    }

    /**
     * Add an NPC (ally, trainer, villain) to this room.
     *
     * @param npc the NPC to add.
     */
    public void addNpc(NPC npc) {
        npcs.add(npc);
    }

    /**
     * Remove an NPC from this room. Called by villains on their
     * own death; can also be used if an ally leaves.
     *
     * @param npc the NPC to remove.
     */
    public void removeNpc(NPC npc) {
        npcs.remove(npc);
    }

    /**
     * Check whether all hostile NPCs in this room are defeated.
     * Used by {@link GuardedDoor} to decide whether the player
     * may pass.
     *
     * @return {@code true} if there are no live {@link Villain}s
     *         in this room.
     */
    public boolean villainCleared(){
        for(NPC npc: npcs){
            if(npc instanceof Villain && !((Villain)npc).isDead()){
                return false;
            }
        }
        return true;
    }

    /**
     * Print the room description and a count of available exits.
     */
    @Override
    public void inspect(){
        String verb = doors.size() == 1 ? "is " : "are ";
        String plural = doors.size() == 1 ? "" : "s";
        System.out.println("You see: " + description
                + " There " + verb + doors.size() + " way" + plural + " out.");
    }
}
