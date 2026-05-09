package nl.rug.oop.rpg;

/**
 * DoctorOctopus: 受到重击时反击
 */
public class DoctorOctopus extends Villain {
    private static final long serialVersionUID = 1L;

    public DoctorOctopus(String name, String description, int baseHealth, int baseDamage, Difficulty difficulty, Item drop) {
        super(name, description, baseHealth, baseDamage, difficulty, drop);
    }

    @Override
    public int onPlayerAttack(int damage) {
        if (damage >= 10) {
            System.out.println(getName() + " Using machine arms to avoid some attack and attack back！");
            // 假设这里直接扣玩家一点血
            return damage - 3; // 减少受到的伤害
        }
        return damage;
    }
}