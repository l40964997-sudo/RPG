package nl.rug.oop.rpg;

public interface Attackable {

    void takeDamage(int damage);

    int getHealth();

    int getDamage();

    boolean isDead();

    String getName();
}
