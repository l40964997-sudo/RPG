package nl.rug.oop.rpg;

/**
 * A quest that requires the player to defeat a specific number of villains.
 * <p>
 * This quest tracks the total number of defeated villains, regardless of their
 * specific names, and completes once the target count is reached.
 */
public class Countdefeatquest extends DefeatQuest{

    private static final long serialVersionUID = 1L;

    /** Total kills required to complete the quest; positive. */
    private final int targetCount;
    /** Current progress; capped at {@link #targetCount}. */
    private int defeated;

    /**
     * Construct a count-based defeat quest.
     *
     * @param name        short title shown in the quest log.
     * @param description longer text explaining the goal.
     * @param targetCount number of villains to defeat; must be positive.
     * @throws IllegalArgumentException if {@code targetCount} is not positive.
     */
    public Countdefeatquest(String name, String description, int targetCount) {
        super(name, description);
        if (targetCount <= 0) {
            throw new IllegalArgumentException(
                    "targetCount must be positive, got: " + targetCount);
        }
        this.targetCount = targetCount;
        this.defeated = 0;
    }

    /**
     * Increment progress on every villain defeat. Completes once
     * progress reaches {@link #targetCount}.
     *
     * @param villainName name of the just-defeated villain (unused).
     */
    @Override
    public void onVillainDefeated(String villainName) {
        if (isCompleted()) {
            return;
        }
        defeated++;
        if (defeated >= targetCount) {
            complete();
        }
    }

    /**
     * Progress summary: either "Complete" or "X/N defeated".
     *
     * @return human-readable progress string.
     */
    @Override
    public String getProgressSummary() {
        return isCompleted() ? "Complete" : defeated + "/" + targetCount + " defeated";
    }
}
