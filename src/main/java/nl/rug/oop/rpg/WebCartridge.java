package nl.rug.oop.rpg;

/**
 * WebCartridge: Refills web shooter charges.
 */
public class WebCartridge extends Item {
    private static final long serialVersionUID = 1L;
    private int charges;

    public WebCartridge(String name, String description, int charges) {
        super(name, description);
        this.charges = charges;
    }

    @Override
    public void use(Player player) {
        player.addWebCharges(charges);
        System.out.println("Web shooters reloaded with " + charges + " new charges!");
    }
}