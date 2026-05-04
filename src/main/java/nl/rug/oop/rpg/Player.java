package nl.rug.oop.rpg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
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
        if(amount<0) amount=0;
        health-=amount;
        if(health<0) health=0;
    }

    public void heal(int amount){
        if(amount<0) amount=0;
        health+=amount;
        if(health>maxHealth) health=maxHealth;
    }

    public void increaseDamage(int amount){
        webCharges+=amount;
    }

    public void addVenomCharges(int amount){
        venomCharges+=amount;
    }

    public boolean spendWebCharges(int amount){
        if(webCharges< amount) return false;
        webCharges -=amount;
        return true;
    }

    public boolean spendVenomCharges(int amount){
        if(venomCharges<amount) return false;
        venomCharges-=amount;
        return true;
    }

    public void addItem(Item item){
        inventory.add(item);
        System.out.println("You picked up: " + item.getName() + ".");
        questlog.notifyItemCollected(item.getName());
    }

    public void useItem(int index){
        if(index<0|| index>=inventory.size()){
            System.out.println("Invalid item.");
            return;
        }
        Item item=inventory.remove(index);
        item.use(this);
    }
}
