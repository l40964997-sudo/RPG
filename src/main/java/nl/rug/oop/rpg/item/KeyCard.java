package nl.rug.oop.rpg.item;

import java.io.Serializable;

/**
 * KeyCard: A passive item used to open LockedDoors.
 */
public class KeyCard extends Item implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Construct a new keycard.
     *
     * @param name        display name, used by LockedDoor
     *                    for matching.
     * @param description descriptive text shown when inspected.
     */
    public KeyCard(String name, String description) {
        super(name, description);
    }
}