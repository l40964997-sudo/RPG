package nl.rug.oop.rpg;

/**
 * The main class. This is the starting point.
 */
public final class Main {

    /** Utility class. Not meant to be instantiated. */
    private Main() {
        // utility class — no instances
    }

    /**
     * Build the world and start the game.
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        Difficulty difficulty = Difficulty.CRIME_WAVE;

        Room apartment   = new Room("Mami's apartment in Brooklyn. Smell of pernil, suit hidden under the bed.");
        Room rooftop     = new Room("Brooklyn rooftop at sunset, " +
                "water towers casting long shadows over the East River.");
        Room subway      = new Room("Abandoned subway tunnel under Manhattan, water dripping from old pipes.");
        Room timesSquare = new Room("Times Square, neon billboards and tourists everywhere - perfect chaos.");
        Room oscorpLobby = new Room("Oscorp Tower lobby, polished marble and a row of security turnstiles.");
        Room oscorpTop   = new Room("Top floor of Oscorp Tower, glass walls and humming server racks.");
        Room glider      = new Room("Mid-air over the Hudson, glider engines screaming overhead.");
        Room pier        = new Room("The pier at midnight, fog rolling in off the river. The final showdown.");

        wireBoroughDoors(apartment, rooftop, subway, timesSquare);
        wireManhattanDoors(subway, timesSquare, oscorpLobby, oscorpTop, glider, pier);
        addBoroughAllies(apartment, rooftop);
        addManhattanAllies(timesSquare, oscorpLobby, oscorpTop);
        addVillains(difficulty, subway, timesSquare, oscorpTop, glider, pier);

        Player miles = new Player("Miles Morales",
                /* maxHealth */ 60,
                /* damage    */ 8,
                /* web       */ 4,
                /* venom     */ 3);
        miles.setCurrentRoom(apartment);

        Game game = new Game(new GameState(miles, difficulty));
        game.run();
    }

    /**
     * Wire up the Brooklyn-side doors: apartment to rooftop, and rooftop's
     * two exits down into Manhattan.
     *
     * @param apartment   starting room.
     * @param rooftop     reached from the apartment.
     * @param subway      reached from the rooftop.
     * @param timesSquare reached from the rooftop.
     */
    private static void wireBoroughDoors(Room apartment, Room rooftop,
                                         Room subway, Room timesSquare) {
        apartment.addDoor(new NormalDoor(
                "The fire escape - climb up to the rooftop.", rooftop));
        rooftop.addDoor(new NormalDoor(
                "Drop down a manhole into the subway tunnels.", subway,
                new WebCartridge(2)));
        rooftop.addDoor(new NormalDoor(
                "Web-swing across the Brooklyn Bridge to Times Square.", timesSquare));
    }

    /**
     * Wire up the Manhattan-side doors: subway/Times Square into Oscorp,
     * up Oscorp Tower, across to the glider, and finally to the pier.
     *
     * @param subway      one route into Oscorp lobby.
     * @param timesSquare other route into Oscorp lobby.
     * @param oscorpLobby contains the locked elevator to the top floor.
     * @param oscorpTop   contains the guarded skylight to the glider.
     * @param glider      contains the door down to the pier.
     * @param pier        final destination.
     */
    private static void wireManhattanDoors(Room subway, Room timesSquare, Room oscorpLobby, Room oscorpTop,
                                           Room glider, Room pier) {
        subway.addDoor(new TrapDoor(
                "Climb a precarious maintenance shaft up to Oscorp Lobby.",
                oscorpLobby,
                new RioMoralesSandwich(20),
                /* damage */ 6,
                "The shaft gives way halfway up - you slam into a girder."));
        timesSquare.addDoor(new NormalDoor(
                "Take the rooftops to Oscorp Tower's lobby.", oscorpLobby,
                new VenomBattery(2)));
        oscorpLobby.addDoor(new LockedDoor(
                "The executive elevator up to the top floor - keycard reader.",
                oscorpTop,
                "Oscorp Keycard",
                new SuitUpgrade("Reinforced Gauntlets", 3)));
        oscorpTop.addDoor(new GuardedDoor(
                "The skylight overlooking the Goblin's glider - needs a clear shot.",
                glider));
        glider.addDoor(new NormalDoor(
                "Free-fall down to the pier.", pier));
    }

    /**
     * Place Brooklyn-side allies (Rio, Peter, Aaron) and assign their quests.
     *
     * @param apartment Rio's room.
     * @param rooftop   rooftop where Peter and Aaron wait.
     */
    private static void addBoroughAllies(Room apartment, Room rooftop) {
        Quest mainQuest = new DefeatQuest(
                "Save the City",
                "Defeat the Goblin's entire crew of five villains. Start by talking to Peter on the rooftop.",
                /* count */ 5);
        Quest goblinQuest = new DefeatQuest(
                "End the Glider",
                "Take down the Green Goblin himself.",
                /* targetName */ "Green Goblin");

        apartment.addNpc(new FriendlyNPC(
                "Rio Morales - your mom, packing a sandwich.",
                "Be careful out there, mijo. Take this for the road.",
                new RioMoralesSandwich(25),
                mainQuest));
        rooftop.addNpc(new FriendlyNPC(
                "Peter Parker, perched on the water tower in his red-and-blue.",
                "Five villains tonight, kid. Goblin's the loud one. Watch the symbiote - he heals.",
                new WebCartridge(3),
                goblinQuest));
        rooftop.addNpc(new Trainer(
                "Aaron Davis - your uncle, leaning against an HVAC unit.",
                "Alright kid, let me show you a few things. Don't tell your mom.",
                /* heal */ 10, /* damage */ 2, /* web */ 1, /* venom */ 1));
    }

    /**
     * Place Manhattan-side allies (Ganke, the trader, and the Oscorp folks).
     *
     * @param timesSquare where Ganke and the trader hang out.
     * @param oscorpLobby where the scientist with the keycard is hiding.
     * @param oscorpTop   where the intern is.
     */
    private static void addManhattanAllies(Room timesSquare, Room oscorpLobby, Room oscorpTop) {
        Quest souvenirQuest = new CollectQuest(
                "Souvenirs",
                "Pick up two Web Cartridges scattered around the city.",
                "Web Cartridge", 2);

        timesSquare.addNpc(new FriendlyNPC(
                "Ganke Lee, on FaceTime from your dorm.",
                "I rerouted Mysterio's hologram drones - heavy hits should cut through. Take a cartridge.",
                new WebCartridge(2),
                souvenirQuest));
        timesSquare.addNpc(new Trader(
                "A street vendor with a glowing tech cart.",
                "Rio's Sandwich",
                new VenomBattery(3)));
        oscorpLobby.addNpc(new FriendlyNPC(
                "An Oscorp scientist hiding behind a desk.",
                "Take this keycard! Just please, get me out of here.",
                new KeyCard("Oscorp Keycard",
                        "Plastic badge with a chip embedded. Unlocks Oscorp doors.")));
        oscorpTop.addNpc(new FriendlyNPC(
                "An intern still at his computer, terrified.",
                "I scraped some files. This might help with the Goblin.",
                new SuitUpgrade("Bio-Electric Amplifier", 4)));
    }

    /**
     * Place all five villains in their rooms with their drops.
     *
     * @param difficulty  scales villain stats.
     * @param subway      Sandman's territory.
     * @param timesSquare Mysterio's territory.
     * @param oscorpTop   Doc Ock's territory.
     * @param glider      Green Goblin's territory.
     * @param pier        Venom's territory.
     */
    private static void addVillains(Difficulty difficulty, Room subway, Room timesSquare,
                                    Room oscorpTop, Room glider, Room pier) {
        subway.addNpc(new Sandman(difficulty, new VenomBattery(2)));
        timesSquare.addNpc(new Mysterio(difficulty, new WebCartridge(3)));
        oscorpTop.addNpc(new DoctorOctopus(difficulty, new SuitUpgrade("Tentacle-Tested Plating", 3)));
        glider.addNpc(new GreenGoblin(difficulty, new RioMoralesSandwich(30)));
        pier.addNpc(new Venom(difficulty, new SuitUpgrade("Symbiote Resistance", 5)));
    }
}