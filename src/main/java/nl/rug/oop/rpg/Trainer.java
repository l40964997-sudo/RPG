package nl.rug.oop.rpg;

public class Trainer extends NPC{

    private static final long serialVersionUID= 1L;

    private final String dialogue;
    private final int healBonus;
    private final int damageBonus;
    private final int webBonus;
    private final int venomBonus;
    private boolean trained;


    public Trainer(String description, String dialogue, int healBonus, int damageBonus,int webBonus,int venomBonus){
        super(description);
        this.dialogue=dialogue;
        this.healBonus=healBonus;
        this.damageBonus=damageBonus;
        this.webBonus=webBonus;
        this.venomBonus=venomBonus;
    }

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
            player.heal(healBonus);
            System.out.println("  +" + webBonus + " web charges");
        }
        if(venomBonus>0){
            player.addVenomCharges(venomBonus);
            System.out.println("  +" + venomBonus + " venom charges");
        }
        trained=true;
    }

}
