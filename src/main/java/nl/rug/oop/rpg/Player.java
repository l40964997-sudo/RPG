package nl.rug.oop.rpg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Player implements Attackable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private Room currentRoom;

    private int health;
    private final int maxHealth;
    private int damage;

    private int webCharges;
    private int venomCharges;
    private boolean camouflaged;

    private final List<Item> inventory;
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

    @Override
    public boolean isDead(){
        return health<=0;
    }

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

    public void heal(int amount){
        if(amount<0) amount=0;
        health+=amount;
        if(health>maxHealth) {
            health=maxHealth;
        }
    }

    /**
     * @param amount damage to add.
     */
    public void increaseDamage(int amount){
        damage+=amount;
    }

    /**
     * @param amount charges to add.
     */
    public void addWebCharges(int amount) {
        webCharges += amount;
    }

    /**
     * @param amount charges to add.
     */
    public void addVenomCharges(int amount){
        venomCharges+=amount;
    }


    /**
     * @param amount number of charges to spend.
     * @return {@code true} if the player had enough; {@code false}
     *         if not (in which case nothing is spent).
     */
    public boolean spendWebCharges(int amount){
        if(webCharges< amount) return false;
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
        if(venomCharges<amount) return false;
        venomCharges-=amount;
        return true;
    }

    /**
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
