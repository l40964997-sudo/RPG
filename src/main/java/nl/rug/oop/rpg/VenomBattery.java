package nl.rug.oop.rpg;

/**
 * VenomBattery: Increases Venom Strike charges.
 */
public class VenomBattery extends Item {

    private static final long serialVersionUID = 1L;

    /** Charges. */
    private final int charges;

    /**
     * Construct a new VenomBattery.
     *
     * @param charges number of venom charges added to the player on use.
     */
    public VenomBattery(int charges) {
        super("Venom Battery",
                "Stored bio-electric energy. +" + charges + " venom strike charges.");
        this.charges = charges;
    }


    /**
     * Add {@code charges} venom charges to the player. Venom
     * charges have no upper cap.
     *
     * @param player the player recharging.
     */
    @Override
    public void use(Player player) {
        player.addVenomCharges(charges);
        System.out.println("Venom Battery absorbed. You feel the bio-electricity surging!");
    }
}