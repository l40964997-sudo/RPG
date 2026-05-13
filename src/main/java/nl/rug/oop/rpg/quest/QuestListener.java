package nl.rug.oop.rpg.quest;

import java.io.Serializable;

/**
 * Observer interface implemented by quests so they can listen to
 * gameplay events (villain defeated, item collected, ...) and
 * update their own progress automatically.
 * <p>
 * The {@link Questlog} broadcasts each event to every active quest
 * by calling the corresponding method on this interface. Each
 * concrete {@link Quest} subclass overrides only the events it
 * actually cares about — the others fall through to the empty
 * {@code default} implementations defined here.
 * <p>
 * Extends {@link Serializable} because every quest is saved with
 * the player, and Java serialization is recursive.
 */
public interface QuestListener extends Serializable {

    /**
     * Called by the QuestLog whenever a villain is defeated.
     *
     * @param villainName the name of the defeated villain
     */
    default void onVillainDefeated(String villainName){}

    /**
     * Called whenever the player picks up an item.
     *
     * @param itemName the display name of the picked-up item
     */
    default void onItemCollected(String itemName){}
}
