package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A location in NYC.
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
