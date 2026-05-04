package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class Door implements Inspectable,Interactable, Serializable {

    private static final long serialVersionUID=1L;

    private final String description;
    private final Room destination;
    private Item reward;

    public Door(String description,Room destination){
        this.description=description;
        this.destination=destination;
    }

    public Door(String description, Room destination, Item reward) {
        this(description, destination);
        this.reward = reward;
    }


    /**
     * Subclasses call this
     * once they decide the player is allowed to pass through.
     * Awards the reward
     * and moves the player to the destination room.
     */
    protected void passThrough(Player player){
        player.setCurrentRoom(destination);
        grandReward(player);
    }

    protected void grandReward(Player player){
        if(reward!=null){
            System.out.println("Something useful catches your eye on the way.");
            player.addItem(reward);
            reward=null;
        }
    }

    @Override
    public void inspect(){
        System.out.println(description);
    }
}
