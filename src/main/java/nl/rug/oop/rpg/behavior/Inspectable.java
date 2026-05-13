package nl.rug.oop.rpg.behavior;

/**
 * An interface for objects Miles can examine. Implementing classes
 * provide their own inspect logic, typically printing some
 * descriptive output to the console.
 */
public interface Inspectable {

    /**
     * Inspect this object. Should produce some descriptive output.
     */
    void inspect();
}
