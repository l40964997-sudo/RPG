package nl.rug.oop.rpg;

public class SuitUpgrade extends Item{

    private static final long serialVersionUID = 1L;

    private final int damageBonus;

    /**
     * Construct a new SuitUpgrade.
     *
     * @param upgradeName short name of the specific upgrade
     *                    (used as part of the item display name).
     * @param damageBonus permanent damage added on use.
     */
    public SuitUpgrade(String upgradeName,int damageBonus){
        super("Suit Upgrade: " + upgradeName,
                "Permanent suit modification. +" + damageBonus + " damage on every hit.");
        this.damageBonus=damageBonus;
    }

    /**
     * Permanently increase the player's base damage. The increase
     * affects every subsequent attack, including damage scaled by
     * web shot, venom strike, and difficulty multipliers.
     *
     * @param player the player installing the upgrade.
     */
    @Override
    public void use(Player player){
        player.increaseDamage(damageBonus);
        System.out.println("Nice, new suit! +(extra " + damageBonus + " damage)");
    }
}
