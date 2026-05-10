package nl.rug.oop.rpg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;


@Getter
@AllArgsConstructor
public abstract class NPC implements Inspectable, Interactable,Serializable{

    private static final long serialVersionUID = 1L;

    private final String description;

    @Override
    public void inspect() {
        System.out.println(description);
    }

}



