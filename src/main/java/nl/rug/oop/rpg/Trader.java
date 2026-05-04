package nl.rug.oop.rpg;

import java.util.List;

public class Trader extends NPC {
    private static final long serialVersionUID=1L;

    private final String wantedItemName;
    private Item offeredItem;

    public Trader(String description, String wantedItemName, Item offeredItem){
        super(description);
        this.wantedItemName=wantedItemName;
        this.offeredItem=offeredItem;
    }

    @Override
    public void interact(Player player){
        if(offeredItem==null){
            System.out.println("They say: \"Nothing left to trade, spidey.\"");
            return;
        }
        System.out.println("They say: \"Got a " + wantedItemName
                + "? I'll trade you a " + offeredItem.getName() + " for it.\"");

        List<Item> inv=player.getInventory();
        for(int i=0;i<inv.size();i++){
            if(inv.get(i).getName().equalsIgnoreCase(wantedItemName)){
                Item given=inv.remove(i);
                System.out.println("You hand over the "+given.getName()+".");
                player.addItem(offeredItem);
                offeredItem=null;
                return;
            }
        }
        System.out.println("you don't have one. They shrug.");


    }
}
