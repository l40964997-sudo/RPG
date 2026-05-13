package nl.rug.oop.rpg.item;

import nl.rug.oop.rpg.entity.Player;

/**
 * Active items that can be invoked from the inventory menu.
 * Passive items (e.g. {@link KeyCard}) deliberately do not
 * implement this — the type system filters them out of the
 * "use which item?" path.
 */
public interface Usable {
    /**
     * Apply this item's effect.
     *
     * @param player the user; never {@code null}.
     * @return {@code true} if the item should be consumed (removed
     *         from the inventory), {@code false} if it should stay
     *         (reusable, like a map).
     */
    boolean use(Player player);
}
