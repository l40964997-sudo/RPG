package nl.rug.oop.rpg;

/** Orchestrates a single trade between a Player and a Trader. */
public class TradeController {

    /**
     * Attempt a trade. Prints the outcome and mutates state via
     * the model's own methods — never by reaching into collections.
     *
     *  @param player the player initiating the trade; must not be null.
     *  @param trader the trader being interacted with; must not be null.
     */
    public void attempt(Player player, Trader trader) {
        if (!trader.hasOffer()) {
            System.out.println("They say: \"Nothing left to trade, spidey.\"");
            return;
        }
        System.out.println("They say: \"Got a " + trader.getWantedItemName()
                + "? I'll trade you a " + trader.peekOffered().getName() + " for it.\"");

        Item given = player.takeItemByName(trader.getWantedItemName());
        if (given == null) {
            System.out.println("You don't have one. They shrug.");
            return;
        }
        System.out.println("You hand over the " + given.getName() + ".");
        player.addItem(trader.takeOffered());
    }

}
