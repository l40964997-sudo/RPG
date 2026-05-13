package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;

/**
 * The save snapshot. Holds everything that needs to survive across a
 * save/load cycle: the player (which transitively owns the world via
 * currentRoom) and the active difficulty.
 * Game-loop state like the Scanner does NOT belong here.
 */

@Getter
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The saved player and (transitively) the world graph. */
    private final Player player;

    /** Difficulty in effect at save time. */
    private final Difficulty difficulty;

    /** Every room in the game plus the starting position. */
    private final World world;

    /**
     * Construct a new persistent state.
     *
     * @param player     the player character.
     * @param difficulty the active difficulty.
     * @param world      the assembled world.
     */
    public GameState(Player player, Difficulty difficulty, World world) {
        this.player = player;
        this.difficulty = difficulty;
        this.world = world;
    }

}
