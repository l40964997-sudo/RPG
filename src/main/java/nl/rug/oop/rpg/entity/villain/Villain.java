package nl.rug.oop.rpg.entity.villain;

import lombok.Getter;
import nl.rug.oop.rpg.Difficulty;
import nl.rug.oop.rpg.behavior.Attackable;
import nl.rug.oop.rpg.controller.CombatController;
import nl.rug.oop.rpg.entity.NPC;
import nl.rug.oop.rpg.entity.Player;
import nl.rug.oop.rpg.item.Item;

/**
 * Base class for all named villains. A Villain owns its own stats
 * and exposes hooks that {@link CombatController} calls during a
 * fight. It does <strong>not</strong> drive the combat loop —
 * that responsibility belongs to {@link CombatController}.
 * <p>
 * Subclasses override these hooks to give each villain a unique
 * combat personality:
 * <ul>
 *   <li>{@link #performAttack(Player)} — the villain's offensive
 *       turn. Default: a plain attack for {@link #getDamage()} damage.</li>
 *   <li>{@link #modifyIncomingDamage(int)} — adjust incoming damage
 *       <em>before</em> it lands (e.g. Sandman absorbs half).
 *       Default: pass-through.</li>
 *   <li>{@link #onPlayerAttack(Player, int)} — react <em>after</em>
 *       the (modified) damage has been applied (e.g. Doc Ock's
 *       counter on heavy hits). Default: no-op.</li>
 *   <li>{@link #onTurnStart()} — passive hook fired at the start
 *       of every round (e.g. Venom regenerates). Default: no-op.</li>
 * </ul>
 * Stats are scaled by the current {@link Difficulty} once at
 * construction; changing difficulty later does not retroactively
 * rescale already-spawned villains.
 * <p>
 * Villains are always hostile and may be killed, so they override
 * {@link NPC#isHostile()} and {@link NPC#isAlive()}.
 */
@Getter
public abstract class Villain extends NPC implements Attackable {

    private static final long serialVersionUID = 1L;

    /** Display name shown in combat banners and quest events. */
    private final String name;
    /** Current hit points; reaches zero on defeat. */
    private int health;
    /** Upper bound for {@link #health}; set once after difficulty scaling. */
    private final int maxHealth;
    /** Per-attack damage dealt by the default {@link #performAttack(Player)}. */
    private final int damage;
    /** Optional item awarded on defeat; {@code null} means no drop. */
    private final Item drop;
    /** Difficulty captured at spawn time; available to subclasses for scaled effects. */
    private final Difficulty difficulty;

    /**
     * Construct a new Villain.
     *
     * @param name        short display name (e.g. {@code "Goblin"}).
     * @param description longer text shown when inspecting this villain.
     * @param baseHealth  pre-difficulty HP value.
     * @param baseDamage  pre-difficulty per-attack damage.
     * @param difficulty  current game difficulty; scales the stats above.
     * @param drop        optional item awarded on defeat; may be {@code null}.
     */
    public Villain(String name, String description, int baseHealth, int baseDamage,
                   Difficulty difficulty, Item drop) {
        super(description);
        this.name = name;
        this.maxHealth = (int) Math.round(baseHealth * difficulty.getVillainMultiplier());
        this.health = this.maxHealth;
        this.damage = (int) Math.round(baseDamage * difficulty.getVillainMultiplier());
        this.drop = drop;
        this.difficulty = difficulty;
    }

    /** Villains are always hostile. */
    @Override
    public boolean isHostile() {
        return true;
    }

    /** Villains are alive until their HP hits zero. */
    @Override
    public boolean isAlive() {
        return !isDead();
    }

    /**
     * Checks if the villain is dead.
     *
     * @return {@code true} once {@link #health} is zero or below.
     */
    @Override
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Apply damage to this villain. Negative inputs are clamped
     * to zero, and {@link #health} is floored at zero so display
     * values never go negative.
     *
     * @param amount raw incoming damage; values below zero count as zero.
     */
    @Override
    public void takeDamage(int amount) {
        if (amount < 0) {
            amount = 0;
        }
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Restore HP, clamped to {@link #maxHealth}. Negatives are ignored.
     *
     * @param amount HP to restore.
     */
    protected void heal(int amount) {
        if (amount < 0) {
            return;
        }
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    /**
     * Damage-modifier hook fired <em>before</em> the player's
     * incoming damage is applied. Override for absorption, partial
     * resistance, dodge chances, etc. Default returns the damage
     * unchanged.
     * <p>
     * Called by {@link Player}'s combat actions before
     * {@link #takeDamage(int)}.
     *
     * @param incoming the raw damage about to be applied.
     * @return the (possibly modified) damage actually applied.
     */
    public int modifyIncomingDamage(int incoming) {
        return incoming;
    }

    /**
     * Reaction hook fired immediately <em>after</em> the player
     * damages this villain. Override for counter-attacks, taunts,
     * etc. Default: no-op.
     *
     * @param player the attacking player.
     * @param dealt  the amount of damage that was actually applied
     *               (already passed through {@link #modifyIncomingDamage(int)}).
     */
    public void onPlayerAttack(Player player, int dealt) { }

    /**
     * Per-round passive hook fired at the start of every round
     * before the player picks an action. Override for regeneration,
     * summoning, ramping damage, etc. Default: no-op.
     */
    public void onTurnStart() { }

    /**
     * The villain's offensive turn. Default deals {@link #getDamage()}
     * damage to the player. Override for variable attacks.
     *
     * @param player the player taking the hit.
     */
    public void performAttack(Player player) {
        System.out.println(name + " strikes you for " + damage + " damage.");
        player.takeDamage(damage);
    }

    /**
     * Entry point for combat. Delegates the entire fight to
     * {@link CombatController}; the villain itself stays out of
     * input/output and loop control.
     *
     * @param player the player engaging this villain.
     */
    @Override
    public void interact(Player player) {
        new CombatController(player, this).run();
    }

    /**
     * Post-victory cleanup: remove this villain from its room,
     * notify the quest log, and award the drop if any. Called by
     * {@link CombatController} once the fight ends in the
     * player's favor.
     *
     * @param player the victorious player.
     */
    public void handleDefeat(Player player) {
        System.out.println();
        System.out.println("You took down " + name + "!");
        if (player.getCurrentRoom() != null) {
            player.getCurrentRoom().removeNpc(this);
        }
        player.getQuestlog().notifyVillainDefeated(name);
        if (drop != null) {
            System.out.println(name + " left something behind.");
            player.addItem(drop);
        }
    }
}