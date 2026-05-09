package nl.rug.oop.rpg;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class Villain extends NPC implements Attackable, Serializable {

    private static final long serialVersionUID=1L;

    private final String name;
    private int health;
    private final int maxHealth;
    private final int damage;
    private final Item drop;

    public Villain(String name, String description, int baseHealth, int baseDamage,
                   Difficulty difficulty, Item drop) {
        super(description);
        this.name = name;
        this.maxHealth = (int) Math.round(baseHealth * difficulty.getVillainMultiplier());
        this.health = this.maxHealth;
        this.damage = (int) Math.round(baseDamage * difficulty.getVillainMultiplier());
        this.drop = drop;
    }

    @Override
    public boolean isDead(){
        return health<=0;
    }

    @Override
    public void takeDamage(int amount){
        if(amount<0) amount=0;
        health-=amount;
        if(health<0) health=0;
    }

    protected void heal(int amount){
        health+=amount;
        if(health>maxHealth) health=maxHealth;
    }

    protected void onPlayerAttack(Player player,int dealt){}
    protected void onTurnStart(){}

    protected void performAttack(Player player){
        System.out.println(name + " strikes you for " + damage + " damage.");
        player.takeDamage(damage);
    }





}
