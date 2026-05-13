package nl.rug.oop.rpg;

import lombok.Getter;

/**
 * An NPC who exchanges one item for another. Looks for an item
 * by name in the player's inventory; if found, removes it and
 * grants the offered item in return.
 * <p>
 * Each trader has only a single offered item. Once the trade is
 * completed, the trader has nothing more to offer and subsequent
 * interactions print a refusal.
 */
@Getter
public class Trader extends NPC {
    private static final long serialVersionUID=1L;
    /** Name of the item the trader wants in exchange. */
    private final String wantedItemName;
    /** Item handed over once the trade is made; nulled after. */
    private Item offeredItem;

    /**
     * Construct a new Trader.
     *
     * @param description    text shown when the trader is inspected.
     * @param wantedItemName name of the item the trader wants in
     *                       exchange (case-insensitive match).
     * @param offeredItem    item handed over once the trade is made.
     * @throws IllegalArgumentException if wantedItemName is null/blank
     *                                    or offeredItem is null.
     */
    public Trader(String description, String wantedItemName, Item offeredItem){
        super(description);
        if (wantedItemName == null || wantedItemName.isBlank()) {
            throw new IllegalArgumentException("wantedItemName must be non-blank");
        }
        if (offeredItem == null) {
            throw new IllegalArgumentException("offeredItem must not be null");
        }
        this.wantedItemName=wantedItemName;
        this.offeredItem=offeredItem;
    }

    /**
     * Checks if the trader still has an item to offer.
     *
     * @return true if the trader still has something to offer.
     */
    public boolean hasOffer() {
        return offeredItem != null;
    }

    /**
     * Looks at the offered item without removing it from the trader.
     *
     * @return the offered item without consuming it, or null if none.
     */
    public Item peekOffered() {
        return offeredItem;
    }

    /**
     * Consume and return the offered item. After this call,
     * {@link #hasOffer()} returns false.
     *
     * @return the item being offered by the trader.
     * @throws IllegalStateException if there is nothing to offer.
     */
    public Item takeOffered() {
        if (offeredItem == null) {
            throw new IllegalStateException("Trader has nothing left to offer");
        }
        Item out = offeredItem;
        offeredItem = null;
        return out;
    }

    /**
     * Try to perform the trade. If the player has the wanted
     * item, consume it and grant the offered item; otherwise
     * print that nothing was exchanged.
     *
     * @param player the player initiating the trade.
     */
    @Override
    public void interact(Player player){
        new TradeController().attempt(player, this);
    }
}
