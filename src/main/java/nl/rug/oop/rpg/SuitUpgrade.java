package nl.rug.oop.rpg;

public class SuitUpgrade extends Item{

    private static final long serialVersionUID = 1L;

    private final int damageBonus;

    public SuitUpgrade(String upgradeName,int damageBonus){
        super("Suit Upgrade: " + upgradeName,
                "Permanent suit modification. +" + damageBonus + " damage on every hit.");
        this.damageBonus=damageBonus;
    }
    @Override
    public void use(Player player){
        player.increaseDamage(damageBonus);
        System.out.println("Nice, new suit! +(extra " + damageBonus + " damage)");
    }
}
