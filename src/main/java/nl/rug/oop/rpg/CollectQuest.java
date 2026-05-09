package nl.rug.oop.rpg;

/**
 * Listens for picked-up items. Completes once the player has collected
 * the required number of items whose name matches (case-insensitive).
 */
public class CollectQuest extends Quest {

    private static final long serialVersionUID = 1L;

    private final String targetItemName;
    private final int targetCount;
    private int collected;

    public CollectQuest(String name, String description,
                        String targetItemName, int targetCount) {
        super(name, description);
        this.targetItemName = targetItemName;
        this.targetCount = targetCount;
        this.collected = 0;
    }

    @Override
    public void onItemCollected(String itemName) {
        if (isCompleted()) {
            return;
        }
        if (targetItemName.equalsIgnoreCase(itemName)) {
            collected++;
            if (collected >= targetCount) {
                compete();
            }
        }
    }

    @Override
    public String getProgressSummary() {
        if (isCompleted()) {
            return "Complete";
        }
        return collected + "/" + targetCount + " " + targetItemName + " collected";
    }
}
