package nl.rug.oop.rpg;

import lombok.Getter;

/**
 * Base class for all quests in the game. Every quest has an
 * immutable name and description plus a mutable completion flag.
 * <p>
 * Quests track player progress by listening to gameplay events
 * via the {@link QuestListener} interface. The {@link Questlog}
 * fans out events to every active quest; each subclass overrides
 * only the listener methods it cares about and updates its own
 * progress. When a quest's goal is reached, the subclass calls
 * the protected {@link #complete()} method.
 * <p>
 * {@code Quest} is abstract because the meaning of "completed"
 * depends entirely on the quest type — there is no universal
 * default. {@link DefeatQuest} completes on villain kills,
 * {@link CollectQuest} completes on item pickups.
 * <p>
 * Subclasses should also override {@link #getProgressSummary()}
 * to return a short, informative status string.
 */
@Getter
public abstract class Quest implements QuestListener,Inspectable {

    private static final long serialVersionUID= 1L;

    /** Short title shown in the quest log. */
    private final String name;
    /** Longer text explaining the goal. */
    private final String description;
    /** True once the quest's goal has been reached. */
    private boolean completed;

    /**
     * Construct a new Quest.
     *
     * @param name        short title shown in the quest log.
     * @param description longer text explaining the goal.
     */
    public Quest(String name, String description) {
        this.name = name;
        this.description = description;
        this.completed = false;
    }

    /**
     * Mark this quest as complete and announce it to the player.
     * Subclasses call this from their listener overrides once the
     * quest's specific goal is reached. Safe to call more than once
     * — only the first call announces.
     */
    protected void complete(){
        if(!completed){
            completed= true;
            System.out.println("Congrats! >>Quest complete: "+ name + "!");
        }
    }

    /**
     * Get a short progress summary shown next to the quest in the
     *  quest log. Default returns {@code "Complete"} or
     * {@code "In progress"}; subclasses should override with
     * something more informative such as {@code "2 / 5"}.
     *
     * @return short progress summary, Default just
     *         shows complete/incomplete. Subclasses override for
     *         more detail.
     */
    public String getProgressSummary(){
        return completed? "Complete":"In progress";
    }

    /**
     * Print the quest with a status tag, name, description, and
     * progress summary.
     */
    @Override
    public void inspect(){
        String tag= completed?"[DONE]":"[    ]";
        System.out.println(tag+name+"-"+description+"("+getProgressSummary()+")");
    }
}
