package nl.rug.oop.rpg;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Miles Morales, the player character. Tracks his current location,
 * combat stats, inventory, special-resource charges, and quest log.
 * <p>
 * Miles has two unique combat resources beyond simple HP and damage:
 * <ul>
 *   <li><strong>Web charges</strong> — spent on web shots in combat;
 *       refilled by {@link WebCartridge} items.</li>
 *   <li><strong>Venom charges</strong> — spent on venom strikes
 *       and camouflage; refilled by {@link VenomBattery} items.</li>
 * </ul>
 * The {@code camouflaged} flag is a one-shot defensive buff: when
 * set, the next call to {@link #takeDamage(int)} ignores the damage
 * and clears the flag.
 * <p>
 * Combat actions ({@link #punch}, {@link #webShot}, {@link #venomStrike},
 * {@link #camouflage}) live on the player rather than on Villain
 * because they describe what the player <em>does</em>, not what the
 * villain <em>is</em>. Each action runs the raw damage through
 * {@link Villain#modifyIncomingDamage(int)} first (so e.g. Sandman
 * can halve it), applies the result, then fires
 * {@link Villain#onPlayerAttack(Player, int)} (so e.g. Doc Ock can
 * counter-attack).
 * <p>
 * Item pickups are routed through {@link #addItem(Item)} so the
 * {@link Questlog} can react to collection-based quests
 * automatically. Code that grants items should always call
 * {@code addItem} rather than mutating the inventory list directly.
 */
@Getter
@Setter
public class Player implements Attackable, Serializable {

    private static final long serialVersionUID = 1L;

    /** Player's display name. */
    private final String name;
    /** Room the player is currently in. */
    private Room currentRoom;
    /** Current health (always &ge; 0). */
    private int health;
    /** Maximum health; healing is capped at this. */
    private final int maxHealth;
    /** Base damage dealt per attack before any buffs. */
    private int damage;
    /** Web shooter charges available for web-shot attacks. */
    private int webCharges;
    /** Venom charges available for venom strikes and camouflage. */
    private int venomCharges;
    /** When true, the next incoming attack will miss. */
    private boolean camouflaged;
    /** Internal mutable inventory. Read access goes through {@link #getInventory()}. */
    private final List<Item> inventory;
    /** The player's quest log. */
    private final Questlog questlog;

    /** Shared input scanner; not serialized, re-injected after load. */
    private transient Scanner scanner;
    /** Active difficulty; not serialized, re-injected after load. */
    private transient Difficulty difficulty;

    /**
     * Construct a new player at full health.
     *
     * @param name         display name.
     * @param maxHealth    starting and maximum HP.
     * @param damage       base damage dealt per attack.
     * @param webCharges   starting web shooter charges.
     * @param venomCharges starting venom strike charges.
     */
    public Player(String name, int maxHealth, int damage,
                  int webCharges, int venomCharges) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.damage = damage;
        this.webCharges = webCharges;
        this.venomCharges = venomCharges;
        this.inventory = new ArrayList<>();
        this.questlog = new Questlog();
    }

    /**
     * Get an unmodifiable view of the inventory. Mutations must go
     * through {@link #addItem(Item)}, {@link #useItem(int)}, or
     * {@link #takeItemByName(String)} so quest hooks and consume
     * logic stay correct.
     *
     * @return read-only list of held items.
     */
    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    /**
     * Inject the non-serializable runtime dependencies. Called by
     * {@link Game} at startup and after every successful load.
     *
     * @param scanner    shared input scanner.
     * @param difficulty current difficulty for combat scaling.
     */
    public void setRuntime(Scanner scanner, Difficulty difficulty) {
        this.scanner = scanner;
        this.difficulty = difficulty;
    }

    /**
     * Checks if the player has been defeated.
     *
     * @return {@code true} if the player's health has reached zero.
     */
    @Override
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Apply damage to the player. If {@link #isCamouflaged()} is
     * set, the damage is ignored and the camouflage flag is cleared.
     *
     * @param amount damage; values below zero are treated as zero.
     */
    @Override
    public void takeDamage(int amount) {
        if (camouflaged) {
            System.out.println("You're invisible! The attack hits empty air.");
            camouflaged = false;
            return;
        }
        if (amount < 0) {
            amount = 0;
        }
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Restore HP, capped at {@link #getMaxHealth()}.
     *
     * @param amount HP to restore; negatives are ignored.
     */
    public void heal(int amount) {
        if (amount < 0) {
            return;
        }
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    /**
     * Permanently increase the player's base damage. Negatives are ignored.
     *
     * @param amount damage to add.
     */
    public void increaseDamage(int amount) {
        if (amount < 0) {
            return;
        }
        damage += amount;
    }

    /**
     * Add web shooter charges. Negatives are ignored.
     *
     * @param amount charges to add.
     */
    public void addWebCharges(int amount) {
        if (amount < 0) {
            return;
        }
        webCharges += amount;
    }

    /**
     * Add venom strike charges. Negatives are ignored.
     *
     * @param amount charges to add.
     */
    public void addVenomCharges(int amount) {
        if (amount < 0) {
            return;
        }
        venomCharges += amount;
    }

    /**
     * Try to spend N web charges.
     *
     * @param amount number of charges to spend.
     * @return {@code true} if the player had enough; {@code false}
     *         if not (in which case nothing is spent).
     */
    public boolean spendWebCharges(int amount) {
        if (webCharges < amount) {
            return false;
        }
        webCharges -= amount;
        return true;
    }

    /**
     * Try to spend N venom charges.
     *
     * @param amount number of charges to spend.
     * @return {@code true} if the player had enough; {@code false}
     *         if not (in which case nothing is spent).
     */
    public boolean spendVenomCharges(int amount) {
        if (venomCharges < amount) {
            return false;
        }
        venomCharges -= amount;
        return true;
    }

    /**
     * Add an item to the inventory and notify the quest log.
     * <strong>All</strong> item grants should flow through this
     * method so collection quests update automatically.
     *
     * @param item the item to add; never {@code null}.
     */
    public void addItem(Item item) {
        inventory.add(item);
        System.out.println("You picked up: " + item.getName() + ".");
        questlog.notifyItemCollected(item.getName());
    }

    /**
     * Try to use the item at the given inventory index. Consumable
     * items (those whose {@link Usable#use(Player)} returns
     * {@code true}) are removed; reusable items (like a map) stay
     * in the inventory. Passive items that aren't {@link Usable}
     * print an explanation and are left alone.
     *
     * @param index zero-based index into {@link #getInventory()}.
     */
    public void useItem(int index) {
        if (index < 0 || index >= inventory.size()) {
            System.out.println("Invalid item.");
            return;
        }
        Item item = inventory.get(index);
        if (!(item instanceof Usable usable)) {
            System.out.println("You can't use the " + item.getName()
                    + " directly. It must be used by something else (a door, a trader, ...).");
            return;
        }
        if (usable.use(this)) {
            inventory.remove(index);
        }
    }

    /**
     * Remove and return the first item whose name matches the given
     * string (case-insensitive). Used by traders and locked doors.
     *
     * @param name item name to look for; case-insensitive.
     * @return the removed item, or {@code null} if no match was found.
     */
    public Item takeItemByName(String name) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getName().equalsIgnoreCase(name)) {
                return inventory.remove(i);
            }
        }
        return null;
    }

    /**
     * Print the inventory as a numbered list during combat. Called
     * by {@link CombatController} when the player opens the in-fight
     * item menu.
     */
    public void showInventoryForCombat() {
        System.out.println("Your stuff:");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println("  (" + i + ") " + inventory.get(i).getName());
        }
    }

    // --- Combat actions ----------------------------------------------------

    /**
     * Plain melee attack.
     *
     * @param target     the villain on the receiving end.
     * @param playerMult the difficulty player multiplier.
     */
    public void punch(Villain target, double playerMult) {
        int raw = scaleDamage(damage, playerMult);
        int dealt = applyAttack(target, raw);
        System.out.println("You land a clean punch for " + dealt + " damage.");
        target.onPlayerAttack(this, dealt);
    }

    /**
     * Web-shot attack: costs 1 web charge, adds +3 base damage.
     * If the player is out of web fluid, nothing happens.
     *
     * @param target     the villain on the receiving end.
     * @param playerMult the difficulty player multiplier.
     */
    public void webShot(Villain target, double playerMult) {
        if (!spendWebCharges(1)) {
            System.out.println("Click. Out of web fluid!");
            return;
        }
        int raw = scaleDamage(damage + 3, playerMult);
        int dealt = applyAttack(target, raw);
        System.out.println("Thwip! You web-blast " + target.getName()
                + " for " + dealt + " damage.");
        target.onPlayerAttack(this, dealt);
    }

    /**
     * Venom strike: costs 1 venom charge, doubles base damage.
     * If the player is out of venom, nothing happens.
     *
     * @param target     the villain on the receiving end.
     * @param playerMult the difficulty player multiplier.
     */
    public void venomStrike(Villain target, double playerMult) {
        if (!spendVenomCharges(1)) {
            System.out.println("You can't muster a venom blast right now.");
            return;
        }
        int raw = scaleDamage(damage * 2, playerMult);
        int dealt = applyAttack(target, raw);
        System.out.println("ZZZZAP! Venom strike hits for " + dealt + " damage.");
        target.onPlayerAttack(this, dealt);
    }

    /**
     * Camouflage: costs 1 venom charge, makes the next incoming
     * attack miss. If the player is out of venom, nothing happens.
     */
    public void camouflage() {
        if (!spendVenomCharges(1)) {
            System.out.println("Not enough venom energy to vanish.");
            return;
        }
        System.out.println("You go invisible. The next attack will miss.");
        this.camouflaged = true;
    }

    /**
     * Run the raw damage through the villain's damage-modifier
     * hook, clamp to at least 1, apply it, and return the actual
     * damage dealt.
     *
     * @param target the villain being hit.
     * @param raw    the raw damage before modification.
     * @return the actual damage that was applied.
     */
    private int applyAttack(Villain target, int raw) {
        int modified = target.modifyIncomingDamage(raw);
        if (modified < 1) {
            modified = 1;
        }
        target.takeDamage(modified);
        return modified;
    }

    /**
     * Apply the player damage multiplier and clamp to a minimum of 1,
     * so on Ultimate the player still scratches the boss.
     *
     * @param base raw damage before multiplier.
     * @param mult player damage multiplier from current difficulty.
     * @return scaled damage value, never below 1.
     */
    private int scaleDamage(int base, double mult) {
        int dealt = (int) Math.round(base * mult);
        return dealt < 1 ? 1 : dealt;
    }
}