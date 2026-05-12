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

Answer: first implementation. 

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

___

# Question 3

To save your game state, you were asked to use Java classes `FileOutputStream` and `ObjectOutputStream`.
These two classes are part of the Java libraries, and they are designed based on a specific design pattern.

Which design pattern do `FileOutputStream` and `ObjectOutputStream` implement?

Explain the roles of this design pattern and how `FileOutputStream` and `ObjectOutputStream` implement it. Also explain the benefit of implementing this design pattern.

___

Answer:

___

# Question 4

Suppose you are using ObjectOutputStream to save the Room objects. One day you add a new field `private int difficulty;` to the Room class. When you try to load an old saved file, the program crashes.

Why does this happen?
How could you prevent this kind of crash when you change your classes in the future?

Answer in at least 150 words and explain with an example if you can.

___

Answer:

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

Which design do you believe is more appropriate in the given context? Justify your answer in at most 150 words.

___

Answer: the second developer's method.
design would defy polymorphism
Using class hierarcht with an abst

___