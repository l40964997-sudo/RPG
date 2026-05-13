package nl.rug.oop.rpg.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Owns the player's active quests and broadcasts gameplay events
 * (villain defeated, item collected) to each one.
 * <p>
 * This is the central piece of the quest system's decoupling:
 * gameplay code (combat, item pickup) calls {@code notify*}
 * methods on this log without knowing or caring which quests
 * are active. The log fans the event out to every quest, and
 * each quest decides for itself whether the event is relevant.
 * <p>
 * Adding a new event type is a one-line change here plus a new
 * default method on {@link QuestListener}; existing quests
 * automatically ignore unknown events thanks to the default
 * empty implementations.
 */
public class Questlog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The list of active quests. */
    private final List<Quest> quests = new ArrayList<>();

    /**
     * Construct an empty quest log.
     */
    public Questlog() {
        // intentionally empty
    }

    /**
     * Get the list of active quests.
     *
     * @return the live quest list. Mutating this directly bypasses
     *         the duplicate-name check in {@link #addQuest(Quest)},
     *         so prefer {@code addQuest} for new entries.
     */
    public List<Quest> getQuests() {
        return quests;
    }

    /**
     * Add a quest to the log. Duplicates (by name, case-insensitive)
     * are silently ignored — a player who talks to the same NPC
     * twice does not get the same quest twice.
     *
     * @param quest the quest to add; never {@code null}.
     */
    public void addQuest(Quest quest) {
        for (Quest q : quests) {
            if (q.getName().equalsIgnoreCase(quest.getName())) {
                return;
            }
        }
        quests.add(quest);
        System.out.println(">> New quest: " + quest.getName());
        System.out.println("   " + quest.getDescription());
    }

    /**
     * Notify every active quest that a villain has been defeated.
     *
     * @param villainName display name of the defeated villain.
     */
    public void notifyVillainDefeated(String villainName) {
        for (Quest q : quests) {
            q.onVillainDefeated(villainName);
        }
    }

    /**
     * Notify every active quest that the player has picked up an item.
     *
     * @param itemName display name of the picked-up item.
     */
    public void notifyItemCollected(String itemName) {
        for (Quest q : quests) {
            q.onItemCollected(itemName);
        }
    }

    /**
     * Check whether the quest log is empty.
     *
     * @return {@code true} if the log contains no quests.
     */
    public boolean isEmpty() {
        return quests.isEmpty();
    }
}
