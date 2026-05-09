package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class Door implements Inspectable,Interactable, Serializable {

    private static final long serialVersionUID=1L;

    private final String description;
    private final Room destination;
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
        grandReward(player);
    }

    /**
     * Grant the door's reward to the player and clear it so it
     * cannot be claimed twice. No-op if there is no reward set.
     *
     * @param player the player to grant the reward to.
     */
    protected void grandReward(Player player){
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
