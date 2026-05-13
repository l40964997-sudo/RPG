package nl.rug.oop.rpg.entity;

import java.io.Serializable;

/**
 * An NPC who permanently improves the player's stats. Distinct
 * from {@link FriendlyNPC} because the effect is mechanical, not
 * just a delivered item — stats change directly and the result
 * persists regardless of inventory state.
 * <p>
 * Each trainer can only train the player once. Subsequent
 * interactions print a polite refusal.
 */
public class Trainer extends NPC implements Serializable {

    private static final long serialVersionUID= 1L;

    /** Line spoken when training begins. */
    private final String dialogue;
    /** HP restored after training (capped at max HP). */
    private final int healBonus;
    /** Permanent damage increase. */
    private final int damageBonus;
    /** Web charges added after training. */
    private final int webBonus;
    /** Venom charges added after training. */
    private final int venomBonus;
    /** True once the player has been trained by this trainer. */
    private boolean trained;

    /**
     * Construct a new Trainer.
     *
     * @param description text shown when the trainer is inspected.
     * @param dialogue    line spoken when training begins.
     * @param healBonus   HP restored after training (capped at max HP).
     * @param damageBonus permanent damage increase.
     * @param webBonus    web charges added after training.
     * @param venomBonus  venom charges added after training.
     */
    public Trainer(String description, String dialogue, int healBonus, int damageBonus,int webBonus,int venomBonus){
        super(description);
        this.dialogue=dialogue;
        this.healBonus=healBonus;
        this.damageBonus=damageBonus;
        this.webBonus=webBonus;
        this.venomBonus=venomBonus;
    }

    /**
     * If this trainer has not yet trained the player, apply all
     * stat bonuses and mark this trainer as used. Otherwise print
     * a refusal message and do nothing.
     *
     * @param player the player being trained.
     */
    @Override
    public void interact(Player player){
        if(trained){
            System.out.println("They say: \"This is all you ever needed, son.\"");
            return;
        }
        System.out.println("They say: \"" + dialogue + "\"");
        System.out.println("You spend some time training with them...");
        if(damageBonus>0){
            player.increaseDamage(damageBonus);
            System.out.println("  +" + damageBonus + " damage");
        }
        if(healBonus>0){
            player.heal(healBonus);
            System.out.println("  +" + healBonus + " HP restored");
        }
        if(webBonus>0){
            player.heal(webBonus);
            System.out.println("  +" + webBonus + " web charges");
        }
        if(venomBonus>0){
            player.addVenomCharges(venomBonus);
            System.out.println("  +" + venomBonus + " venom charges");
        }
        trained=true;
    }

}
