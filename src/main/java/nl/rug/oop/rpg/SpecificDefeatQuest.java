package nl.rug.oop.rpg;

import java.io.Serializable;

/**
 * Quest that completes when one specific villain is defeated.
 * Matching is case-insensitive on the villain's display name.
 */
public class SpecificDefeatQuest extends DefeatQuest implements Serializable{
    private static final long serialVersionUID = 1L;

    /** Display name of the villain that completes this quest. */
    private final String targetName;

    /**
     * Construct a quest targeting one specific villain.
     *
     * @param name        short title shown in the quest log.
     * @param description longer text explaining the goal.
     * @param targetName  display name of the villain to defeat;
     *                    must be non-null and non-blank.
     * @throws IllegalArgumentException if {@code targetName} is null or blank.
     */
    public SpecificDefeatQuest(String name, String description, String targetName) {
        super(name, description);
        if (targetName == null || targetName.isBlank()) {
            throw new IllegalArgumentException("targetName must be non-blank");
        }
        this.targetName = targetName;
    }

    /**
     * Complete the quest the first time the matching villain dies.
     * All other defeats are ignored.
     *
     * @param villainName name of the just-defeated villain.
     */
    @Override
    public void onVillainDefeated(String villainName) {
        if (isCompleted()) {
            return;
        }
        if (targetName.equalsIgnoreCase(villainName)) {
            complete();
        }
    }

    /**
     * Progress summary: either "Complete" or "Target: &lt;name&gt;".
     *
     * @return human-readable progress string.
     */
    @Override
    public String getProgressSummary() {
        return isCompleted() ? "Complete" : "Target: " + targetName;
    }

}
