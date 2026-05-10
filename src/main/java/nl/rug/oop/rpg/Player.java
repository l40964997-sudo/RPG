package nl.rug.oop.rpg;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    /** The player's inventory. */
    private final List<Item> inventory;
    /** The player's quest log. */
    private final Questlog questlog;

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
     * Check whether the player has been defeated.
     *
     * @return {@code true} if the player's health has reached zero.
     */
    @Override
    public boolean isDead(){
        return health<=0;
    }

    /**
     * Apply damage to the player. If {@link #isCamouflaged()} is
     * set, the damage is ignored and the camouflage flag is cleared.
     *
     * @param amount damage; values below zero are treated as zero.
     */
    @Override
    public void takeDamage(int amount){
        if(camouflaged){
            System.out.println("You're invisible! The attack hits empty air.");
            camouflaged=false;
            return;
        }
        if(amount<0) {
            amount = 0;
        }
        health-=amount;
        if(health<0) {
            health=0;
        }
    }

    /**
     * Restore HP, capped at {@link #getMaxHealth()}.
     *
     * @param amount HP to restore; values below zero are treated as zero.
     */
    public void heal(int amount){
        if(amount<0) {
            amount = 0;
        }
        health+=amount;
        if(health>maxHealth) {
            health=maxHealth;
        }
    }

    /**
     * Permanently increase the player's base damage.
     *
     * @param amount damage to add.
     */
    public void increaseDamage(int amount){
        damage+=amount;
    }

    /**
     * Add web shooter charges (no upper cap).
     *
     * @param amount charges to add.
     */
    public void addWebCharges(int amount) {
        webCharges += amount;
    }

    /**
     * Add venom strike charges (no upper cap).
     *
     * @param amount charges to add.
     */
    public void addVenomCharges(int amount){
        venomCharges+=amount;
    }

    /**
     * Try to spend N web charges.
     *
     * @param amount number of charges to spend.
     * @return {@code true} if the player had enough; {@code false}
     *         if not (in which case nothing is spent).
     */
    public boolean spendWebCharges(int amount){
        if(webCharges< amount) {
            return false;
        }
        webCharges -=amount;
        return true;
    }

    /**
     * Try to spend N venom charges.
     *
     * @param amount number of charges to spend.
     * @return {@code true} if the player had enough; {@code false}
     *         if not (in which case nothing is spent).
     */
    public boolean spendVenomCharges(int amount){
        if(venomCharges<amount) {
            return false;
        }
        venomCharges-=amount;
        return true;
    }

    /**
     * Add an item to the inventory and notify the quest log.
     * <strong>All</strong> item grants should flow through this
     * method so collection quests update automatically.
     *
     * @param item the item to add; never {@code null}.
     */
    public void addItem(Item item){
        inventory.add(item);
        System.out.println("You picked up: " + item.getName() + ".");
        questlog.notifyItemCollected(item.getName());
    }

    /**
     * Remove the item at the given inventory index and apply its
     * effect. If the index is out of range, prints a message and
     * does nothing.
     *
     * @param index zero-based index into {@link #getInventory()}.
     */
    public void useItem(int index){
        if(index<0|| index>=inventory.size()){
            System.out.println("Invalid item.");
            return;
        }
        Item item=inventory.remove(index);
        item.use(this);
    }
}
