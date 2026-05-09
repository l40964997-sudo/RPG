package nl.rug.oop.rpg;

import lombok.AllArgsConstructor;
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

    private final String description;
    private final List<Door> doors;
    private final List<NPC> npcs;

    public Room(String description) {
        this.description = description;
        this.doors = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    public void addDoor(Door door) {
        doors.add(door);
    }

    public void addNpc(NPC npc) {
        npcs.add(npc);
    }

    public void removeNpc(NPC npc) {
        npcs.remove(npc);
    }

    /**
     * @return true if every villain in this room has been defeated
     */
    public boolean villainCleared(){
        for(NPC npc: npcs){
            if(npc instanceof Villain && !((Villain)npc).isDead()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void inspect(){
        System.out.println("You see: "+ description+"There"+(doors.size()==1?"is":"are")+  + doors.size() + " way" + (doors.size() == 1 ? "" : "s") + " out.");
    }
}
