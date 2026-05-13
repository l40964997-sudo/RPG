package nl.rug.oop.rpg.item;

import nl.rug.oop.rpg.entity.Player;

import java.io.Serializable;

/**
 * A permanent suit modification — reinforced gauntlets, refined
 * web formula, etc. Permanently increases the player's base
 * damage on use. Unlike most items, the effect is not reversible.
 */
public class SuitUpgrade extends Item implements Usable, Serializable {

    private static final long serialVersionUID = 1L;

    /** Permanent damage added on use. */
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
        if (damageBonus < 0) {
            throw new IllegalArgumentException(
                    "damageBonus must be non-negative, got: " + damageBonus);
        }
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
    public boolean use(Player player){
        player.increaseDamage(damageBonus);
        System.out.println("Nice, new suit! +(extra " + damageBonus + " damage)");
        return true;
    }
}
