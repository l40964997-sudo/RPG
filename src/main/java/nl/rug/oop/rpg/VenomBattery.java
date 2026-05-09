package nl.rug.oop.rpg;

/**
 * VenomBattery: Increases Venom Strike charges.
 */
public class VenomBattery extends Item {
    private static final long serialVersionUID = 1L;
    private int charges;

    public VenomBattery(String name, String description, int charges) {
        super(name, description);
        this.charges = charges;
    }

    @Override
    public void use(Player player) {
        player.addVenomCharges(charges);
        System.out.println("Venom Battery absorbed. You feel the bio-electricity surging!");
    }
}