package nl.rug.oop.rpg;

import java.io.Serializable;

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
