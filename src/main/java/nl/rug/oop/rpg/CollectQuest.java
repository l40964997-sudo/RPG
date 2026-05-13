package nl.rug.oop.rpg;

/**
 * Listens for picked-up items. Completes once the player has collected
 * the required number of items whose name matches (case-insensitive).
 */
public class CollectQuest extends Quest {

    private static final long serialVersionUID = 1L;

    /** Target item name. */
    private final String targetItemName;
    /** Target. */
    private final int targetCount;
    /** Progress. */
    private int collected;

    /**
     * Construct a new collection quest.
     *
     * @param name           short title shown in the quest log.
     * @param description    longer text explaining the goal.
     * @param targetItemName item display name to count (case-insensitive).
     * @param targetCount    number of items to collect.
     */
    public CollectQuest(String name, String description,
                        String targetItemName, int targetCount) {
        super(name, description);
        if (targetItemName == null || targetItemName.isBlank()) {
            throw new IllegalArgumentException("targetItemName must be non-blank");
        }
        if (targetCount <= 0) {
            throw new IllegalArgumentException(
                    "targetCount must be positive, got: " + targetCount);
        }
        this.targetItemName = targetItemName;
        this.targetCount = targetCount;
        this.collected = 0;
    }

    /**
     * Update progress when a matching item is picked up.
     * Already-completed quests ignore further events.
     *
     * @param itemName display name of the just-collected item.
     */
    @Override
    public void onItemCollected(String itemName) {
        if (isCompleted()) {
            return;
        }
        if (targetItemName.equalsIgnoreCase(itemName)) {
            collected++;
            if (collected >= targetCount) {
                complete();
            }
        }
    }

    /**
     * Operation.
     *
     * @return {@code "Complete"} if done, otherwise
     *         {@code "X / N <item>s"} (e.g. {@code "1 / 2 Web Cartridges"}).
     */
    @Override
    public String getProgressSummary() {
        if (isCompleted()) {
            return "Complete";
        }
        return collected + "/" + targetCount + " " + targetItemName + " collected";
    }
}
