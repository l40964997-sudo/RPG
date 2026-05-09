package nl.rug.oop.rpg;

public class FriendlyNPC extends NPC{
    private static final long serialVersionUID=1L;

    private final String dialogue;
    private Item gift;
    private Quest quest;

    /**
     * Create an NPC who only talks.
     *
     * @param description text shown when the NPC is inspected.
     * @param dialogue    line of speech printed on interaction.
     */
    public FriendlyNPC(String description, String dialogue){
        super(description);
        this.dialogue= dialogue;
    }


    /**
     * Create an NPC who hands over a gift on first interaction.
     *
     * @param description text shown when the NPC is inspected.
     * @param dialogue    line of speech printed on interaction.
     * @param gift        item given on first interaction.
     */
    public FriendlyNPC(String description, String dialogue,Item gift){
        this(description,dialogue);
        this.gift=gift;
    }

    /**
     * Create an NPC who assigns a quest and hands over a gift on
     * first interaction.
     *
     * @param description text shown when the NPC is inspected.
     * @param dialogue    line of speech printed on interaction.
     * @param gift        item given on first interaction.
     * @param quest       quest added to the player's log on first interaction.
     */
    public FriendlyNPC(String description, String dialogue, Item gift, Quest quest){
        this(description, dialogue, gift);
        this.quest=quest;
    }


    /**
     * Speak to the player, then (in this order):
     * @param player the player initiating the interaction.
     */
    @Override
    public void interact(Player player){
        System.out.println("They say: \"" + dialogue + "\"");
        //assign the quest before giving the gift,
        //so the gift item counts towards the quest
        //if it's a CollectQuest
        if(quest!=null){
            player.getQuestlog().addQuest(quest);
            quest=null;
        }
        if(gift!=null){
            System.out.println("They hand you something");
            player.addItem(gift);
            gift=null;
        }

    }
}
