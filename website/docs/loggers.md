---
sidebar_position: 2
---

# Loggers

## commandBlock

`/log commandBlock <option>`

Inform when a command block or command block minecart executes command

It's useful to find out where the annoying hidden running command block is

With default `throttled` option every command block will log at the highest frequency once every 3 seconds

Attributes:
- Default option: `throttled`
- Suggested options: `throttled`, `all`


## damage

`/log damage <selector>`

Inform when a living entity gets damage and display how the damage gets calculated

The `<selector>` option consist of 1 or 2 object declaration strings
and an optional `->` or `<->` direction indicator

Example structures for the `<selector>` where `A` and `B` are 2 object declarations:

- `A`: damage source is `A` or damage target is `A`
- `->A`: damage target is `A`
- `A->`: damage source is `A`
- `A->B`: damage source is `A`, and damage target is `B` (`A` just dealt some damages to `B`)
- `A<->B`: damage source is `A` and damage target is `B`, or damage source is `B` and damage target is `A`

To declare an object, you can choose any of the following syntaxes:

- Hardcoded:
  - *empty*, `*` or `all`: Matches anything
  - `me`: Matches the subscriber itself
  - `players`: Matches player
- Entity type (matches given type of entities):
  - `cat`: Matches cat
  - `minecraft:cat`: same as above
  - `entity_type/cat`: same as above
- Damage name (matches source only):
  - `hotFloor`: Matches if damage msg ID is hotFloor (i.e. damage type `minecraft:hot_floor`)
  - `damage_name/hotFloor`: same as above
- Damage type (available in mc1.19.4+, matches source only):
  - `hot_floor`: Matches if damage type is `minecraft:hot_floor`
  - `minecraft:hot_floor`: same as above
  - `damage_type/hot_floor`: same as above
- Entity selector:
  - `@e[distance=..20]`: this works too, but requires permission level 2 like vanilla
  - `Steve`: works if player Steve is online
  - `some-uuid-string`: just like an entity selector in command

`<selector>` examples:

- `->me`: Damage dealt to the subscriber itself
- `->creeper`: Damage dealt to creeper
- `vex->`: Damage dealt from vex
- `zombie`: Damage from / to zombies
- `minecraft:zombie`: The same as `zombie`
- `me->zombie`: Damage from the subscriber to zombies
- `me<->zombie`: Damage between the subscriber and zombies
- `hotFloor->zombie`: Damage from magma block to zombies
- `->@e[distance=..10]`: Damage dealt to entities within 10m of the subscriber

Attributes:
- Default option: `all`
- Suggested options: `all`, `players`, `me`, `->creeper`, `vex->`, `me->zombie`, `hotFloor->zombie`, `Steve`, `@e[distance=..10]`


## item

`/log item <events>`

Inform when something happens to an item entity, for example item despawned after 5min

Available events:
- `create`: An item entity is created in the world for any kinds of reason. Stack trace is included in the message
- `die`: An item entity died
- `despawn`: An item entity despawned

Use csv format, like `despawn,die` for logging multiple events

Available option separators: `,`, `.` and ` ` (`.` is the only choice in 1.14.4 version)

Attributes:
- Default option: `despawn`
- Suggested options: `despawn`, `die`, `despawn,die`


## lifetime

`/log lifetime <entity_type>`

A HUD Logger

Displays the current lifetime statistic of specific entity type from the [LifeTime Tracker](commands.md#lifetime) in the dimension the player is in

The logging options is required to be an available entity type

Attributes:
- Default option: N/A
- Suggested options: All available entity types in current's lifetime tracking


## lightQueue

`/log lightQueue`

A HUD logger for debugging light suppression. It displays the following information of the lighting task queue:

- Average task accumulation speed
- Current light queue size. Indicating with symbol `S`
- Estimated duration of light suppression if the light suppressor is switched off now. Indicating with symbol `T`
- Average task enqueuing speed
- Average task executing speed

The sampling duration can be specified with rule [lightQueueLoggerSamplingDuration](rules.md#lightqueueloggersamplingduration), default 60gt

Specify the logging option to select the world you want log its light queue, like `/log mobcaps`

Attributes:
- Default option: `dynamic`
- Suggested options: `dynamic`, `overworld`, `the_nether`, `the_end`


## memory

`/log memory`

Display current memory usage of the server in HUD

Format: `Used memory` / `Allocated memory` | `Max memory`

Attributes:
- Default option: N/A
- Suggested options: N/A


## microTiming

`/log microTiming <type>`

Log micro timings of redstone components. The ticket of the chunk the component is in needs to be at least lazy-processing (ticket level 32)

Check rule [microTiming](rules.md#microtiming) for detail. Remember to use `/carpet microTiming true` to enable logger functionality

Available options:
- `all`: Default value, log all events
- `merged`: Log all events and merged continuous same events
- `unique`: Log the first unique event in every gametick

Attributes:
- Default option: `merged`
- Suggested options: `all`, `merged`, `unique`


## mobcapsLocal

**Available in Minecraft 1.18.2+**

`/log mobcapsLocal [<player>]`

A HUD Logger

Like carpet's mobcaps logger, but what it displays is the local mobcap of the specified player

If no player is specified, it will display the local mobcap of the subscriber

Attributes:
- Default option: N/A
- Suggested options: Names of all online players


## movement

`/log movement <target>`

Switch: rule [loggerMovement](rules.md#loggermovement)

Inform when a living entity tries to move and display how the actual movement gets calculated

`<target>` is an entity selector. Make sure you have selector the necessary targets or expect log spam (`@` selector requires permission level 2)

Additionally, appending `non_zero:` as the prefix in the `<target>` string will filter out those logs whose final movement vector is 0

Attributes:
- Default option: `non_zero:@a[distance=..10]`
- Suggested options: `non_zero:@a[distance=..10]`, `@s`, `non_zero:@e[type=creeper,distance=..5]`, `Steve`


## phantom

`/log phantom <options>`

With option `spawning`, it informs when someone spawns a wave of phantoms

With option `reminder`, it reminds you when you haven't slept for 45min or 60min

Attributes:
- Default option: `spawning`
- Suggested options: `spawning`, `reminder`, `spawning,reminder`


## portalCreation

`/log portalCreation <options>`

Inform when an entity creates a new nether portal

The logging option can be an entity selector, restricting what the portal creator can be

Attributes:
- Default option: `all`
- Suggested options: `all`, `players`, `me`, `@e[type=creeper]`, `@e[type=!player]`


## raid

`/log raid`

Inform when these raid related events happen:

- A raid has been created
- A raid has been invalidated
- The bad omen level of a raid has been increased
- The center Position of a raid has been moved

Attributes:
- Default option: N/A
- Suggested options: N/A


## scounter

`/log scounter <color>`

It's a HUD logger

Similar to carpet's `counter` logger for its hopper counter, this logger is used for showing items output from infinity item supplier hoppers created by rule [hopperNoItemCost](rules.md#hoppernoitemcost)

Attributes:
- Default option: N/A
- Suggested options: All dye color names


## ticket

`/log ticket <types>`

Inform when a ticket is created or removed

Use csv format, like `portal,dragon` for logging multiple types of ticket

Available option separators: `,`, `.` and ` ` (`.` is the only choice in 1.14.4 version)

**Warning:** Logging `unknown` ticket may make you get spammed

Attributes:
- Default option: `portal`
- Suggested options: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## tickWarp

`/log tickWarp <option>`

A HUD logger to display to progress of current tick warping

It only shows up when the server is tick warping

See [/tick warp status](commands.md#warp-status) command for displaying more details of tick warp

Attributes:
- Default option: `bar`
- Suggested options: `bar`, `value`


## turtleEgg

`/log turtleEgg`

Logs when a turtle egg is trampled to broken

Attributes:
- Default option: N/A
- Suggested options: N/A


## wanderingTrader

`/log wanderingTrader`

Logs when someone summon (actually, special natural spawning based on randomly chosen player) a wandering trader


## xcounter

`/log xcounter <color>`

It's a HUD logger

Similar to carpet's `counter` logger for its hopper counter, this logger is used for showing xp statistic xp counters created by rule [hopperXpCounters](rules.md#hopperxpcounters)

Attributes:
- Default option: N/A
- Suggested options: All dye color names


## xporb

`/log xporb <events>`

Basically the same as [item logger](#item) but logs experience orb entities
