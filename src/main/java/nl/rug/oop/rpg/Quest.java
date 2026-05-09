package nl.rug.oop.rpg;

import lombok.Getter;

@Getter
public abstract class Quest implements QuestListener,Inspectable {

    private static final long serialVersionUID= 1L;

    private final String name;
    private final String description;
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
