package nl.rug.oop.rpg;

/**
 * Application entry point. Composes the game world via
 * {@link WorldBuilder} and hands it to {@link Game}.
 * <p>
 * The world description is intentionally declarative: each helper
 * method does one piece of setup (rooms, doors, allies, villains)
 * by mutating the builder rather than by manually threading local
 * variables. Adding a new area is a matter of registering rooms
 * and connecting them — no rewrites of helper signatures.
 */
public final class Main {

    // --- Room keys: declared once, referenced everywhere ------------------

    /** Identifier for the player's starting apartment room. */
    private static final String APARTMENT = "apartment";
    /** Identifier for the city rooftop location. */
    private static final String ROOFTOP = "rooftop";
    /** Identifier for the underground subway location. */
    private static final String SUBWAY = "subway";
    /** Identifier for the Times Square location. */
    private static final String TIMES_SQUARE = "timesSquare";
    /** Identifier for the lobby of the Oscorp building. */
    private static final String OSCORP_LOBBY = "oscorpLobby";
    /** Identifier for the top floor or roof of the Oscorp building. */
    private static final String OSCORP_TOP = "oscorpTop";
    /** Identifier for the glider location, typically used for aerial encounters. */
    private static final String GLIDER = "glider";
    /** Identifier for the city pier location. */
    private static final String PIER = "pier";

    /**
     * Utility class. Not meant to be instantiated.
     */
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

        // The single keycard object is referenced by both the
        // door that requires it and the NPC that gives it out,
        // so there's no string-name duplication that can drift.
        KeyCard oscorpKeycard = new KeyCard(
                "Oscorp Keycard",
                "Plastic badge with a chip embedded. Unlocks Oscorp doors.");

        WorldBuilder builder = new WorldBuilder();
        registerRooms(builder);
        wireDoors(builder, oscorpKeycard);
        addAllies(builder, oscorpKeycard);
        addVillains(builder, difficulty);
        builder.startAt(APARTMENT);

        World world = builder.build();

        Player miles = new Player("Miles Morales",
                /* maxHealth */ 60,
                /* damage    */ 8,
                /* web       */ 4,
                /* venom     */ 3);
        miles.setCurrentRoom(world.getStartingRoom());

        Game game = new Game(new GameState(miles, difficulty, world));
        game.run();
    }

    /**
     * Register every room in the game under a stable string key.
     *
     * @param b the builder to mutate.
     */
    private static void registerRooms(WorldBuilder b) {
        b.addRoom(APARTMENT,
                "Mami's apartment in Brooklyn. Smell of pernil, suit hidden under the bed.");
        b.addRoom(ROOFTOP,
                "Brooklyn rooftop at sunset, water towers casting long shadows over the East River.");
        b.addRoom(SUBWAY,
                "Abandoned subway tunnel under Manhattan, water dripping from old pipes.");
        b.addRoom(TIMES_SQUARE,
                "Times Square, neon billboards and tourists everywhere - perfect chaos.");
        b.addRoom(OSCORP_LOBBY,
                "Oscorp Tower lobby, polished marble and a row of security turnstiles.");
        b.addRoom(OSCORP_TOP,
                "Top floor of Oscorp Tower, glass walls and humming server racks.");
        b.addRoom(GLIDER,
                "Mid-air over the Hudson, glider engines screaming overhead.");
        b.addRoom(PIER,
                "The pier at midnight, fog rolling in off the river. The final showdown.");
    }

    /**
     * Wire all the doors between rooms. Resolves rooms by key via
     * {@link WorldBuilder#room(String)}.
     *
     * @param b             the builder to mutate.
     * @param oscorpKeycard the shared keycard reference used by
     *                      the locked Oscorp elevator.
     */
    private static void wireDoors(WorldBuilder b, KeyCard oscorpKeycard) {
        b.addDoor(APARTMENT, new NormalDoor(
                "The fire escape - climb up to the rooftop.", b.room(ROOFTOP)));
        b.addDoor(ROOFTOP, new NormalDoor(
                "Drop down a manhole into the subway tunnels.", b.room(SUBWAY),
                new WebCartridge(2)));
        b.addDoor(ROOFTOP, new NormalDoor(
                "Web-swing across the Brooklyn Bridge to Times Square.", b.room(TIMES_SQUARE)));
        b.addDoor(SUBWAY, new TrapDoor(
                "Climb a precarious maintenance shaft up to Oscorp Lobby.",
                b.room(OSCORP_LOBBY),
                new RioMoralesSandwich(20),
                /* damage */ 6,
                "The shaft gives way halfway up - you slam into a girder."));
        b.addDoor(TIMES_SQUARE, new NormalDoor(
                "Take the rooftops to Oscorp Tower's lobby.", b.room(OSCORP_LOBBY),
                new VenomBattery(2)));
        b.addDoor(OSCORP_LOBBY, new LockedDoor(
                "The executive elevator up to the top floor - keycard reader.",
                b.room(OSCORP_TOP),
                oscorpKeycard,
                new SuitUpgrade("Reinforced Gauntlets", 3)));
        b.addDoor(OSCORP_TOP, new GuardedDoor(
                "The skylight overlooking the Goblin's glider - needs a clear shot.",
                b.room(GLIDER)));
        b.addDoor(GLIDER, new NormalDoor(
                "Free-fall down to the pier.", b.room(PIER)));
    }

    /**
     * Place friendly NPCs and assign their associated quests.
     *
     * @param b             the builder to mutate.
     * @param oscorpKeycard the keycard handed out by the Oscorp scientist.
     */
    private static void addAllies(WorldBuilder b, KeyCard oscorpKeycard) {
        Quest mainQuest = new Countdefeatquest("Save the City",
                "Defeat the Goblin's entire crew of five villains. Start by talking to Peter.",
                /* count */ 5);
        Quest goblinQuest = new SpecificDefeatQuest("End the Glider",
                "Take down the Green Goblin himself.",
                "Green Goblin");
        Quest souvenirQuest = new CollectQuest("Souvenirs",
                "Pick up two Web Cartridges scattered around the city.",
                "Web Cartridge", 2);

        b.addNpc(APARTMENT, new FriendlyNPC("Rio Morales - your mom, packing a sandwich.",
                "Be careful out there, mijo. Take this for the road.",
                new RioMoralesSandwich(25),
                mainQuest));
        b.addNpc(ROOFTOP, new FriendlyNPC("Peter Parker, perched on the water tower in his red-and-blue.",
                "Five villains tonight, kid. Goblin's the loud one. Watch the symbiote - he heals.",
                new WebCartridge(3),
                goblinQuest));
        b.addNpc(ROOFTOP, new Trainer(
                "Aaron Davis - your uncle, leaning against an HVAC unit.",
                "Alright kid, let me show you a few things. Don't tell your mom.",
                /* heal */ 10, /* damage */ 2, /* web */ 1, /* venom */ 1));
        b.addNpc(TIMES_SQUARE, new FriendlyNPC("Ganke Lee, on FaceTime from your dorm.",
                "I rerouted Mysterio's hologram drones - heavy hits should cut through. Take a cartridge.",
                new WebCartridge(2),
                souvenirQuest));
        b.addNpc(TIMES_SQUARE, new Trader("A street vendor with a glowing tech cart.",
                "Rio's Sandwich",
                new VenomBattery(3)));
        b.addNpc(OSCORP_LOBBY, new FriendlyNPC("An Oscorp scientist hiding behind a desk.",
                "Take this keycard! Just please, get me out of here.",
                oscorpKeycard));
        b.addNpc(OSCORP_TOP, new FriendlyNPC("An intern still at his computer, terrified.",
                "I scraped some files. This might help with the Goblin.",
                new SuitUpgrade("Bio-Electric Amplifier", 4)));
    }

    /**
     * Place all five villains in their rooms with their drops.
     *
     * @param b          the builder to mutate.
     * @param difficulty scales villain stats.
     */
    private static void addVillains(WorldBuilder b, Difficulty difficulty) {
        b.addNpc(SUBWAY, new Sandman(difficulty, new VenomBattery(2)));
        b.addNpc(TIMES_SQUARE, new Mysterio(difficulty, new WebCartridge(3)));
        b.addNpc(OSCORP_TOP, new DoctorOctopus(difficulty,
                new SuitUpgrade("Tentacle-Tested Plating", 3)));
        b.addNpc(GLIDER, new GreenGoblin(difficulty, new RioMoralesSandwich(30)));
        b.addNpc(PIER, new Venom(difficulty, new SuitUpgrade("Symbiote Resistance", 5)));

    }
}