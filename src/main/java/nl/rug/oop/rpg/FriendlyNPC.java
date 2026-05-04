package nl.rug.oop.rpg;

public class FriendlyNPC extends NPC{
    private static final long serialVersionUID=1L;

    private final String dialogue;
    private Item gift;
    private Quest quest;

    public FriendlyNPC(String description, String dialogue){
        super(description);
        this.dialogue= dialogue;
    }

    public FriendlyNPC(String description, String dialogue,Item gift){
        this(description,dialogue);
        this.gift=gift;
    }

    public FriendlyNPC(String description, String dialogue, Item gift, Quest quest){
        this(description, dialogue, gift);
        this.quest=quest;
    }

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
