package nl.rug.oop.rpg;

/**
 * WebCartridge: Refills web shooter charges.
 */
public class WebCartridge extends Item {
    private static final long serialVersionUID = 1L;

    /** Charges. */
    private final int charges;

    /**
     * Construct a new WebCartridge.
     *
     * @param charges number of web charges added to the player on use.
     */
    public WebCartridge(int charges) {
        super("Web Cartridge",
                "A cartridge of Miles' homemade web fluid. +" + charges + " web charges.");
        this.charges = charges;
    }

    /**
     * Add {@code charges} web charges to the player. Web charges
     * have no upper cap.
     *
     * @param player the player reloading.
     */
    @Override
    public void use(Player player) {
        player.addWebCharges(charges);
        System.out.println("Web shooters reloaded with " + charges + " new charges!");
    }
}