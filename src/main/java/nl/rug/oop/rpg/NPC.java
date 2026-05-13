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
     * Whether this NPC is hostile to the player. Used by gameplay
     * code (e.g. {@link Room#villainCleared()}) to decide whether
     * the room contains threats without having to know the concrete
     * NPC type. Default: {@code false} (friendly NPCs).
     *
     * @return {@code true} if this NPC is hostile.
     */
    public boolean isHostile() {
        return false;
    }

    /**
     * Whether this NPC is still alive. Only meaningful for hostile
     * NPCs that can be killed; friendly NPCs are always alive.
     * Default: {@code true}. {@link Villain} overrides this.
     *
     * @return {@code true} if this NPC is alive.
     */
    public boolean isAlive() {
        return true;
    }

    /**
     * Default inspection prints the description. Subclasses may
     * override to add extra detail.
     */
    @Override
    public void inspect() {
        System.out.println(description);
    }

}



