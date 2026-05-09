package nl.rug.oop.rpg;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Base class for everything Miles can carry. Each item defines its
 * own use() effect. Items must be Serializable so they survive
 * save/load with the player's inventory.
 */
@AllArgsConstructor
@Getter
public abstract class Item implements Inspectable {

    private static final long serialVersionUID= 1L;

    private final String name;
    private final String description;

    @Override
    public void inspect(){
        System.out.println(name+":" +description);
    }

    /**
     * @param player the player using the item; never {@code null}.
     */
    public abstract void use(Player player);


}
