package nl.rug.oop.rpg;

/**
 * Listens for villain defeats. Two flavours:
 *   1) target a specific villain by name, or
 *   2) kill any N villains.
 * Pick the constructor that matches the quest you want.
 */
public class DefeatQuest extends Quest {

    private static final long serialVersionUID = 1L;

    /** Specific target name, or {@code null} for count-based mode. */
    private final String targetName;

    /** Total kills required to complete the quest. */
    private final int targetCount;

    /** Current progress toward the target. */
    private int defeated;

    /**
     * Create a quest to defeat one specific villain by name.
     *
     * @param name        short title shown in the quest log.
     * @param description longer text explaining the goal.
     * @param targetName  display name of the villain to defeat
     *                    (case-insensitive match).
     */
    public DefeatQuest(String name, String description, String targetName) {
        super(name, description);
        this.targetName = targetName;
        this.targetCount = 1;
        this.defeated = 0;
    }

    /**
     * Create a quest to defeat any N villains, regardless of which.
     *
     * @param name        short title shown in the quest log.
     * @param description longer text explaining the goal.
     * @param targetCount number of villains to defeat (any type).
     */
    public DefeatQuest(String name, String description, int targetCount) {
        super(name, description);
        this.targetName = null;
        this.targetCount = targetCount;
        this.defeated = 0;
    }

    /**
     * Update progress when a villain is defeated. Specific quests
     * only count their named target; count-based quests count any
     * kill. Already-completed quests ignore further events.
     *
     * @param villainName name of the just-defeated villain.
     */
    @Override
    public void onVillainDefeated(String villainName) {
        if (isCompleted()) {
            return;
        }
        if (targetName != null) {
            if (targetName.equalsIgnoreCase(villainName)) {
                defeated = 1;
                complete();
            }
        } else {
            defeated++;
            if (defeated >= targetCount) {
                complete();
            }
        }
    }

    /**
     * Gets a short text summary of the current quest progress.
     *
     * @return {@code "Complete"} if done, otherwise either the
     * specific target's name or {@code "X/N defeated"}.
     */
    @Override
    public String getProgressSummary() {
        if (isCompleted()) {
            return "Complete";
        }
        if (targetName != null) {
            return "Target: " + targetName;
        }
        return defeated + "/" + targetCount + " defeated";
    }
}
