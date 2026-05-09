package nl.rug.oop.rpg;

/**
 * Listens for villain defeats. Two flavours:
 *   1) target a specific villain by name, or
 *   2) kill any N villains.
 * Pick the constructor that matches the quest you want.
 */
public class DefeatQuest extends Quest {

    private static final long serialVersionUID = 1L;

    private final String targetName;
    private final int targetCount;
    private int defeated;

    /**
     * Quest: defeat one specific villain.
     */
    public DefeatQuest(String name, String description, String targetName) {
        super(name, description);
        this.targetName = targetName;
        this.targetCount = 1;
        this.defeated = 0;
    }

    /**
     * Quest: defeat any N villains.
     */
    public DefeatQuest(String name, String description, int targetCount) {
        super(name, description);
        this.targetName = null;
        this.targetCount = targetCount;
        this.defeated = 0;
    }

    @Override
    public void onVillainDefeated(String villainName) {
        if (isCompleted()) {
            return;
        }
        if (targetName != null) {
            if (targetName.equalsIgnoreCase(villainName)) {
                defeated = 1;
                compete();
            }
        } else {
            defeated++;
            if (defeated >= targetCount) {
                compete();
            }
        }
    }

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
