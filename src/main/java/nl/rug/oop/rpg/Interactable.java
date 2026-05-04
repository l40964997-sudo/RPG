package nl.rug.oop.rpg;

/**
 * An interface for objects Miles can interact with — webbing through
 * a door, talking to an ally, or fighting a villain.
 */
public interface Interactable {

    /**
     * Interact with this object.
     *
     * @param player the player performing the interaction
     */
    void interact(Player player);
}
