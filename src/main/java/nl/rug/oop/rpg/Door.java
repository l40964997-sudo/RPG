package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;


/**
 * Base class for all doors — anything that connects one
 * {@link Room} to another. Doors are one-way: they have a single
 * destination, and going through removes the player from the
 * current room and places them in the destination.
 * <p>
 * Concrete subclasses define <em>when</em> the player is allowed
 * through, and what side-effects passing through has, by
 * overriding {@link #interact(Player)}. Examples:
 * <ul>
 *   <li>{@link NormalDoor}  — always lets the player through.</li>
 *   <li>{@link TrapDoor}    — lets them through but deals damage.</li>
 *   <li>{@link GuardedDoor} — blocks until the current room's
 *       villains are defeated.</li>
 *   <li>{@link LockedDoor}  — requires a specific item.</li>
 * </ul>
 * The base class provides the shared behavior of moving the
 * player and granting an optional reward via the helper
 * {@link #passThrough(Player)} — subclasses call this once they
 * have decided the player may pass.
 */
@Getter
public abstract class Door implements Inspectable,Interactable, Serializable {

    private static final long serialVersionUID=1L;

    /** Text shown when the door is inspected. */
    private final String description;

    /** Room the player enters on successful passage. */
    private final Room destination;

    /** One-time reward granted on first passage; nulled after. */
    private Item reward;

    /**
     * Create a door with no reward.
     *
     * @param description text shown when the door is inspected.
     * @param destination room the player enters on successful passage.
     */
    public Door(String description,Room destination){
        this.description=description;
        this.destination=destination;
    }

    /**
     * Create a door that grants an item the first time the player
     * passes through.
     *
     * @param description text shown when the door is inspected.
     * @param destination room the player enters on successful passage.
     * @param reward      item granted on first passage; nulled out
     *                    afterwards so it can only be claimed once.
     */
    public Door(String description, Room destination, Item reward) {
        this(description, destination);
        this.reward = reward;
    }

    /**
     * Helper called by subclasses once they have decided the
     * player may go through. Moves the player to {@link #destination}
     * and grants the one-time reward (if any).
     *
     * @param player the player passing through; never {@code null}.
     */
    protected void passThrough(Player player){
        player.setCurrentRoom(destination);
        grantReward(player);
    }

    /**
     * Grant the door's reward to the player and clear it so it
     * cannot be claimed twice. No-op if there is no reward set.
     *
     * @param player the player to grant the reward to.
     */
    protected void grantReward(Player player){
        if(reward!=null){
            System.out.println("Something useful catches your eye on the way.");
            player.addItem(reward);
            reward=null;
        }
    }

    /**
     * Default inspection prints the door's description. Subclasses
     * may override to hint at conditions (e.g. "Locked").
     */
    @Override
    public void inspect(){
        System.out.println(description);
    }
}
