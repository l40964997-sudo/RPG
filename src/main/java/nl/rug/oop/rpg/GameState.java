package nl.rug.oop.rpg;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The save snapshot. Holds everything that needs to survive across a
 * save/load cycle: the player (which transitively owns the world via
 * currentRoom) and the active difficulty.
 * Game-loop state like the Scanner does NOT belong here.
 */

@Getter
@AllArgsConstructor
public class GameState {

    private static final long serialVersionUID = 1L;

    /** The saved player and (transitively) the world graph. */
    private final Player player;

    /** Difficulty in effect at save time. */
    private final Difficulty difficulty;

}
