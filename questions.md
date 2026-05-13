# Question 1

In step 1, you were asked to create a `Room` class with a description, which will be printed if inspected. Two software developers proposed two different implementations for the `Room` class.

The first developer proposed one implementation:

```java
public class Room {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 
```

The second developer proposed another implementation:

```java
public class Room {
    public String description;
}
```

Both developers are discussing which implementation is better and why. Please answer the following question:

Which of these two implementations would you select? And why?

Justify your answer in at least 250 words. Please explain the consequences, benefits and drawbacks of each implementation and support it with an example.

___

Answer: The first implementation is the better choice, because  it follows encapsulation.
By making description private and exposing it through method, the class keep full control over how internal state is read and modified.
Implementation 1: Encapsulation 
consequence:
The approach hides the internal state of the Room object and requires external classes to interact with it through a defined contract (the methods).
benefits: 
1 Access control. Can make a field read only by providing only a getter, or write once by guarding setter
2 Control and Validation.Maintain complete control over what happens when a property is read or changed.
Can add validation logic, logging, or trigger other events whenever the description is updated.
3 can change the internal representation of the data without breaking the public API.if you later decide to store the description in a database or split it into an array of strings, you only change the inside of the getter/setter methods. The calling code remains completely untouched.
4 debugging.
Drawbacks:Verbosity.
It requires writing boilerplate code, which makes the class longer.

Implementation 2: Public Fields
consequence:
This approach exposes the internal data directly to the outside world, allowing any external class to read or write to the field directly.
Benefits:
Brevity. The code is extremely short and easy to read.
Drawbacks:
1 Loss of Control.Any class can change the description at any time, to anything (including null), without the Room class knowing it happened.
2 if there's a need to validate the description, cannot simply change the field  to private without breaking compilation for every single external class that accesses it directly.

Supporting example
imagine that six months into development, a new business requirement is introduced: "A room's description must never be null and must automatically be formatted so the first letter is capitalized."
If you use first implementation:(simply update setDescription method in the Room class)

public void setDescription(String description) {
if (description == null) {
throw new IllegalArgumentException("Description cannot be null");
}
this.description = description.substring(0, 1).toUpperCase() + description.substring(1);
}
You make the change in exactly one place. The rest of your application instantly benefits from this new logic, and no other files need to be modified

If you used the second implementation:
You have a massive problem. Because hundreds of other classes might be writing myRoom.description = "some text". 
You have to hunt down every single assignment across your entire codebase.
you must manually add if checks and string-manipulation logic to every single place the variable is modified.
___

# Question 2

In step 2, you are asked to create two interfaces: `Inspectable` and `Interactable`.
Interfaces by definition do not have implementations. They consist of method signatures:

```java
interface Inspectable {
    void inspect();
}
```

A software developer claims that interfaces are not useful, because they do not contain implementations. Thus, we should just use classes, and we do not need to define empty interfaces.

What do you think about this opinion? Do you agree or disagree with this opinion?

Please justify your answer in at least 250 words, and support your justifications with an example.

___

Answer: Disagree.
Interfaces are extremely useful because they contain no implementation.
They exist to define a contract.
If relying only on classes , a developer would tightly couple their code, restrict inheritance, and severely limits the flexibility of the software.
Advantage of interface:
1 A contract decoupled from implementation. An interface defines what an object can do, not how. Code written against Inspectable works with any current or future class that implements it, without knowing or caring about the concrete type. This is the Dependency Inversion Principle: depend on abstractions, not on concretions.
2 Multiple inheritance of type. Java forbids extending more than one class. But a Room can be both Inspectable and Interactable, and a Chest can be Inspectable, Interactable, and Openable. Classes alone cannot express this.
3 Polymorphism across unrelated hierarchies. Consider a game where rooms, items, and NPCs are all inspectable but otherwise share nothing:

List<Inspectable> things = new ArrayList<>();
things.add(new Room());
things.add(new Sword());
things.add(new Goblin());
for (Inspectable t : things) t.inspect();
Without the interface, this loop is impossible

4 Testability and substitution. Interfaces let us swap real implementations for fakes or mocks during testing. A class hard-coded to depend on FileSaveStorage is hard to test; one that depends on a Storage interface can be tested with an in-memory implementation.
5 Enabling design patterns. Strategy, Observer, Command, and many others rely on interfaces to plug in different behaviors at runtime.

Unlike class, the interface lets us write code that works with anything fulfilling the contract, present or future.
___

# Question 3

To save your game state, you were asked to use Java classes `FileOutputStream` and `ObjectOutputStream`.
These two classes are part of the Java libraries, and they are designed based on a specific design pattern.

Which design pattern do `FileOutputStream` and `ObjectOutputStream` implement?

Explain the roles of this design pattern and how `FileOutputStream` and `ObjectOutputStream` implement it. Also explain the benefit of implementing this design pattern.

___

Answer: 
The Design Pattern: Decorator
The FileOutputStream and ObjectOutputStream classes in Java implement the Decorator design pattern.

Roles of the Decorator pattern
Component
— an abstract type defining the common interface. In java.io, this is OutputStream.
Concrete Component
— a basic implementation that does the actual underlying work. FileOutputStream is one: it writes raw bytes to a file.
Decorator 
— an abstract class that wraps a Component and forwards calls to it, while conforming to the Component interface itself. In java.io, this is FilterOutputStream.
Concrete Decorator 
— extends the decorator to add new behavior on top of the wrapped Component. ObjectOutputStream is a concrete decorator that adds the ability to serialize whole Java objects (not just bytes).

How FileOutputStream and ObjectOutputStream Implement It
FileOutputStream acts as the Concrete Component. Its sole responsibility is to write raw bytes directly to a file on the file system. It does not know what an object, a string, or a boolean is; it only understands raw byte data.

ObjectOutputStream acts as the Concrete Decorator. It wraps around an existing OutputStream (in this case, my FileOutputStream). It adds the complex behavior of serialization—translating high-level Java objects (like a Room or Player) into a stream of bytes.

In code:
OutputStream fileStream = new FileOutputStream("savegame.dat");
ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

When call objectStream.writeObject(myGameState), the ObjectOutputStream does the heavy lifting of translating the object into bytes, and then it hands those bytes off to the FileOutputStream to physically write them to the disk.

Benefits:
Composition over inheritance. Instead of needing classes like ObjectFileOutputStream, CompressedObjectFileOutputStream, BufferedCompressedObjectFileOutputStream, etc. (an explosion of subclasses), you stack decorators at runtime: new ObjectOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(...)))).
Open/Closed Principle. New behaviors are added as new decorators without changing existing classes.
Flexibility. The decoration order can be tuned to the use case, and each layer has a single responsibility.
___

# Question 4

Suppose you are using ObjectOutputStream to save the Room objects. One day you add a new field `private int difficulty;` to the Room class. When you try to load an old saved file, the program crashes.

Why does this happen?
How could you prevent this kind of crash when you change your classes in the future?

Answer in at least 150 words and explain with an example if you can.

___

Answer:The crash is caused by a mismatch in a hidden identifier that Java uses behind the scenes.
When serialize an object using ObjectOutputStream, Java automatically calculates a unique identifier for that class called the serialVersionUID. 
If we do not explicitly define this ID ourselves, the JVM generates one at runtime based on the class's structural blueprint, 
including its fields, methods, and access modifiers.
When we added the private int difficulty, field to the Room class,  changed its structural blueprint. 
As a result, the JVM generated a completely new serialVersionUID for the updated class.
When use ObjectInputStream to load an old saved file, Java compares the serialVersionUID of the saved object against the ID of the current code. 
Because we added a field, the IDs no longer match. 
Java assumes the older saved data is dangerously incompatible with the new code and abruptly throws an InvalidClassException, crashing your program.

To prevent this from happening, 
I must take control of the versioning process by explicitly declaring a fixed serialVersionUID within the class.

example:
@Setters
@Getters
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private int difficulty;
   ....//fields

}
Whenever I create a class that implements Serializable, 
add a serialVersionUID immediately.
Doing so ensures saved data remains resilient as game evolves.
___



# Question 5

You want to add items to your game, like swords, potions, and keys.
Two developers proposed two different designs.

The first developer suggested using one single class with a type field:

```java
public class Item {
    public String type;
}
```

He argues that this is a good design because it is simple and easy to understand. He also argues that it is easy to add new types of items in the future! Additionally, serializing and deserializing the items is easy because they are all in one class.

The second developer suggested using a class hierarchy with an abstract class `Item` and subclasses for each type of item:

```java
public abstract class Item {
    public String name;
}

public class Sword extends Item {
    public int damage;
}
public class Potion extends Item {
    public int healing;
}
public class Key extends Item {
    public String keyType;
}
```

He argues that this is a better design because it is more flexible and extensible. 

__Which design do you believe is more appropriate in the given context? Justify your answer in at most 150 words.__

___

Answer: the second developer's method(class hierarchy method) is more appropriate.
The design utilizing an abstract class and inheritance.
While the first approach seems simple initially, relying on a string type field is a well-known anti-pattern.
To represent everything, that single Item class would eventually need fields for damage, healing, and keyType. 
Consequently, every item would carry memory-wasting, irrelevant data (e.g., a potion does not need a damage value), 
resulting in a bloated class. 
Furthermore, the game logic would require massive, error-prone if/else or switch statements to handle behavior.

The second approach correctly leverages polymorphism and the Open/Closed Principle.
Encapsulation: Each subclass strictly contains only its relevant data and behavior.
Extensibility: To add a Shield later, you simply create a new subclass without altering existing, tested code.
This design guarantees a robust, type-safe, and highly maintainable architecture
___