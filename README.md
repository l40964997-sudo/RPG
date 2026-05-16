# Spider-Man RPG

A turn-based text RPG written in Java, themed around Miles Morales and built as an exercise in object-oriented design. The focus is on a clean, extensible architecture: small interfaces, deep polymorphism, and a quest system built on the observer pattern.

## Overview

You play Miles Morales, navigating eight locations across New York to defeat the Green Goblin's five-villain crew. The game is entirely text-driven — every interaction happens through a numbered menu in the terminal. Combat is turn-based, with a small toolkit of attacks (punch, web shot, venom strike) and one defensive option (camouflage). Save/load supports named saves and a quicksave slot. Three difficulty levels scale villain stats and player damage.

Mechanically it's deliberately compact; structurally it's built to grow. New door types, villains, items, and quests are all single-file additions — no changes to the engine.

## Running

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="nl.rug.oop.rpg.Main"
```

Requires Java 17 or higher. Lombok is used for getter and constructor generation.

## Gameplay at a glance

```
Miles Morales | HP: 60/60 | DMG: 8 | Web: 4 | Venom: 3 | Difficulty: Crime Wave
What do you want to do?
(0) Look around       (5) Check Quests       (10) Quit
(1) Look for a way out (6) QuickSave
(2) Look for company   (7) QuickLoad
(3) Change difficulty  (8) Save
(4) Check Inventory    (9) Load
```

Progression flows through option `(2) Look for company` — NPCs hand out quests and items, villains start combat. Doors gate progression: some require defeating the villains in the room first, some require a keycard, some deal damage on use.

### Combat

Combat opens when the player engages a villain:

```
(0) Punch                  — base damage
(1) Web Shot     [1 web]   — base damage + 3
(2) Venom Strike [1 venom] — base damage × 2
(3) Camouflage   [1 venom] — next incoming attack misses
(4) Use an item
(5) Swing away (flee)
```

Each villain has a unique combat hook:

| Villain | Mechanic |
|---|---|
| Sandman | Absorbs half of every incoming hit |
| Mysterio | 33% chance to dodge any attack (hologram) |
| Doctor Octopus | Deflects 3 damage off heavy hits (10+) |
| Green Goblin | Throws a bomb every third turn for +6 damage |
| Venom | Regenerates 3 HP at the start of each round |

## Architecture

The project is organized by responsibility, not by feature:

```
src/main/java/nl/rug/oop/rpg/
├── Main, Game, GameState, SaveManager, Difficulty   — game loop & state
│
├── behavior/      — small interfaces (Inspectable, Interactable, Attackable)
├── controller/    — combat and trade orchestration extracted from entities
├── entity/        — the people: Player, NPC, Trainer, Trader, FriendlyNPC
│   └── villain/   — the five villains, each subclassing Villain
├── item/          — items, item interfaces, item subclasses
├── quest/         — quest system: Quest, Questlog, listeners, subclasses
└── world/         — rooms, doors, and the world graph
```

### Key design decisions

**Interfaces over class hierarchies.** The `behavior/` package defines three narrow contracts:

- `Inspectable` — anything that can be examined (`void inspect()`).
- `Interactable` — anything the player can act on (`void interact(Player)`).
- `Attackable` — anything that has HP and can be fought.

A `FriendlyNPC` is `Inspectable` and `Interactable` but not `Attackable`, so the compiler prevents the player from accidentally attacking a quest-giver. A `Villain` is all three. This is cheaper than building separate class hierarchies for combatants and non-combatants.

**Chain-of-responsibility damage hooks.** Villain combat variations are expressed by overriding a single hook:

```java
protected int onPlayerAttack(int damage) {
    return damage;   // default: damage passes through unchanged
}
```

Subclasses transform the incoming damage before it lands. Sandman's absorb is `return damage / 2`; Mysterio's dodge is `return 0`; Doc Ock's deflect is `return damage - 3`. This is far cleaner than the alternative of letting damage apply and then "undoing" it in a post-hook.

**Observer pattern for quests.** When the player picks up an item or defeats a villain, the gameplay code calls `questlog.notifyItemCollected(name)` or `questlog.notifyVillainDefeated(name)`. The `Questlog` fans the event out to every active quest. Each quest decides for itself whether the event is relevant by overriding the appropriate `QuestListener` method.

Adding a new quest type means:

1. Subclass `Quest`.
2. Override the listener methods you care about.
3. Construct the new quest in `Main` and hand it to an NPC.

No gameplay-side code changes. No engine changes.

**Save/load isolation.** Only `GameState` (player + difficulty) implements `Serializable`. The `Game` class — which owns the `Scanner` — does not. This is deliberate: it prevents accidentally serializing runtime state that has no business being persisted, and keeps the save format minimal and forward-compatible.

**Controllers for cross-cutting orchestration.** Combat and trading involve multiple entities and multi-turn loops. Rather than putting that logic inside `Villain.interact()` or `Trader.interact()`, it lives in `CombatController` and `TradeController` in the `controller/` package. Entities expose state; controllers run flows.

## Extension points

The project is built to grow. To add:

- **A new door type** → subclass `Door`, override `interact(Player)`.
- **A new villain** → subclass `Villain`, override one of `performAttack`, `onPlayerAttack`, or `onTurnStart`.
- **A new item** → subclass `Item`, override `use(Player)`.
- **A new quest type** → subclass `Quest`, override the relevant `QuestListener` methods.

The engine doesn't need to change for any of these — Main constructs them and passes them around as their abstract types.

## The map

All connections are one-way:

```
Apartment ──▶ Rooftop ──┬──▶ Subway ──────▶ Oscorp Lobby ──▶ Oscorp Top ──▶ Glider ──▶ Pier
                        └──▶ Times Square ─▶
```

- **Apartment → Rooftop**: simple door.
- **Rooftop → Subway**: simple door, drops a reward.
- **Subway → Oscorp Lobby**: trap door, deals damage on use.
- **Times Square → Oscorp Lobby**: simple door, drops a reward.
- **Oscorp Lobby → Oscorp Top**: locked, requires Oscorp Keycard.
- **Oscorp Top → Glider**: guarded, requires all villains in the room defeated.
- **Glider → Pier**: simple door to the final boss.

## Things I'd improve

- **Combat is deterministic** apart from Mysterio's dodge. Adding a crit chance and a few status effects would give the combat loop more texture.
- **The map is linear.** Branching paths and revisitable rooms would create real exploration. The current graph is technically a tree.
- **The menu is context-free.** It always shows the same eleven options, regardless of whether the room is empty or there's a quest update. A context-sensitive menu would help guide the player.
- **The singleton bridge** in `Game.getScannerInstance()` and `Game.getDifficultyInstance()` is a known wart. A proper fix is to pass a `CombatContext` through `Interactable.interact()`, but that pollutes every implementer.
- **No tests.** The project compiles and runs end-to-end, but there's no test suite. Combat hooks and quest listeners would be the highest-value places to add unit tests.

## License

MIT.
