package nl.rug.oop.rpg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Owns the player's active quests and fans out gameplay events
 * (villain defeated, item collected) to each one. Quests update
 * their own progress.
 *
 * This decouples gameplay code from quest-tracking — Villain.die()
 * just calls QuestLog.notifyVillainDefeated() and doesn't need to
 * know which (or how many) quests care.
 */
public class Questlog implements Serializable {

    private static final long serialVersionUID= 1L;

    private final List<Quest> quests= new ArrayList<>();

    public List<Quest> getQuests(){
        return quests;
    }


    public void addQuest(Quest quest){
        for (Quest q: quests) {
            if (q.getName().equalsIgnoreCase(quest.getName())) {
                return;
            }
        }
            quests.add(quest);
            System.out.println(">>New quest:"+quest.getName());
            System.out.println(quest.getDescription());
        }

        public void notifyVillainDefeated(String villainName) {
            for (Quest q : quests) {
                q.onVillainDefeated(villainName);
            }
        }

        public void notifyItemCollected(String itemName){
            for(Quest q:quests){
                q.onItemCollected(itemName);
            }
        }

        public boolean isEmpty(){
            return quests.isEmpty();
        }
    }

