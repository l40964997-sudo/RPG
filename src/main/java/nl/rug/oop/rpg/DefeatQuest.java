package nl.rug.oop.rpg;

import java.io.Serializable;

/**
 * Listens for villain defeats. Two flavours:
 *   1) target a specific villain by name, or
 *   2) kill any N villains.
 * Pick the constructor that matches the quest you want.
 */
public abstract class DefeatQuest extends Quest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Create a quest to defeat one specific villain by name.
     *
     * @param name        short title shown in the quest log.
     * @param description longer text explaining the goal.
     */
    public DefeatQuest(String name, String description) {
        super(name, description);
    }
}
