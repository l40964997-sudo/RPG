package nl.rug.oop.rpg;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * The save snapshot. Holds everything that needs to survive across a
 * save/load cycle: the player (which transitively owns the world via
 * currentRoom) and the active difficulty.
 *
 * Game-loop state like the Scanner does NOT belong here.
 */
@Getter
@AllArgsConstructor
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Player player;
    private final Difficulty difficulty;
}
