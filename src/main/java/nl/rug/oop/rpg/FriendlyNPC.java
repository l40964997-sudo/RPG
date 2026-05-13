package nl.rug.oop.rpg;

import java.io.Serializable;

/**
 * A non-hostile NPC: an ally Miles can talk to. May give the
 * player an item, assign a quest, or both. Each gift and each
 * quest can only be received once — after the first interaction,
 * the corresponding field is nulled out.
 * <p>
 * <strong>Order of operations:</strong> when an NPC both assigns
 * a quest and gives a gift, the quest is added to the quest log
 * <em>before</em> the gift is granted. This ensures that if the
 * gift item is what the new quest wants the player to collect,
 * the gift counts toward progress. Reversing this order is a
 * bug we caught during testing.
 */
public class FriendlyNPC extends NPC implements Serializable {
    private static final long serialVersionUID=1L;

    /** Line of speech printed on interaction. */
    private final String dialogue;
    /** Gift. */
    private Item gift;
    /** Quest added on first interaction. */
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
     * Speak to the player, then execute action order.
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
