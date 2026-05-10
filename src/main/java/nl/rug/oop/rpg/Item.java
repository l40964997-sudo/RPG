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

    /** Short display name, e.g. "Health Potion". */
    private final String name;
    /** Longer text shown when the item is inspected. */
    private final String description;

    /**
     * Print the item's name and description.
     */
    @Override
    public void inspect(){
        System.out.println(name+":" +description);
    }

    /**
     * Apply this item's effect. Called by
     * {@link Player#useItem(int)} <em>after</em> the item has
     * already been removed from the inventory.
     *
     * @param player the player using the item; never {@code null}.
     */
    public abstract void use(Player player);

}
