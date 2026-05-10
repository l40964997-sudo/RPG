package nl.rug.oop.rpg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;

/**
 * Base class for any non-player character — friendly or hostile.
 * <p>
 * Concrete subclasses choose what {@link #interact(Player)} means:
 * {@link FriendlyNPC} talks and may give items, {@link Trainer}
 * permanently improves Miles' stats, {@link Trader} swaps items,
 * and the various {@link Villain} subclasses start combat.
 * <p>
 * {@code NPC} is abstract because there is no sensible default
 * implementation of {@code interact()} that fits every NPC type.
 */
@Getter
@AllArgsConstructor
public abstract class NPC implements Inspectable, Interactable,Serializable{

    private static final long serialVersionUID = 1L;

    /** Text shown when the player inspects this NPC. */
    private final String description;

    /**
     * Default inspection prints the description. Subclasses may
     * override to add extra detail.
     */
    @Override
    public void inspect() {
        System.out.println(description);
    }

}



