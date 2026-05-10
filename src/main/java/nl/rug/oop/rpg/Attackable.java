package nl.rug.oop.rpg;

/**
 * Interface for entities that can participate in combat — they have
 * health, deal damage, and can die.
 * <p>
 * Only {@link Player} and {@link Villain} subclasses implement this.
 * Friendly NPCs deliberately do <strong>not</strong> implement
 * {@code Attackable}: separating "things you can talk to" from
 * "things you can fight" through the type system means the
 * compiler prevents accidentally attacking a quest-giver.
 * <p>
 * This is the third interface in the game (alongside
 * {@link Inspectable} and {@link Interactable}) and exists so
 * combat code can treat Player and Villain uniformly.
 */
public interface Attackable {

    /**
     * Apply damage to this entity. Implementations should clamp
     * health at zero rather than allowing negative health.
     *
     * @param damage amount of damage to apply; values below zero
     *               should be treated as zero.
     */
    void takeDamage(int damage);

    /**
     * Get the current health.
     *
     * @return current health (always &ge; 0).
     */
    int getHealth();

    /**
     * Get the base damage this entity deals on a normal attack.
     *
     * @return base damage value.
     */
    int getDamage();

    /**
     * Check whether this entity is dead.
     *
     * @return {@code true} once health has reached zero or below.
     */
    boolean isDead();

    /**
     * Get the display name shown in combat messages.
     *
     * @return short display name.
     */
    String getName();
}
