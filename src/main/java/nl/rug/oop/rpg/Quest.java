package nl.rug.oop.rpg;

import lombok.Getter;

@Getter
public abstract class Quest implements QuestListener,Inspectable {

    private static final long serialVersionUID= 1L;

    private final String name;
    private final String description;
    private boolean completed;

    public Quest(String name, String description) {
        this.name = name;
        this.description = description;
        this.completed = false;
    }

    protected void compete(){
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

    @Override
    public void inspect(){
        String tag= completed?"[DONE]":"[    ]";
        System.out.println(tag+name+"-"+description+"("+getProgressSummary()+")");
    }


}
