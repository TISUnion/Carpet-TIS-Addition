**English** | [中文](commands_cn.md)

\>\>\> [Back to index](readme.md)

# Commands

## info

### world ticking_order

`/info world ticking_order`

Show the ticking order of current dimensions in the game


## lifetime

A tracker to track lifetime and spawn / removal reasons from all newly spawned and dead entities

This tracker is mostly used to debug mobfarms. It aims to track the process from mob starting affecting the mobcap to mob being removed from the mobcap. The spawning tracking part of it doesn't cover every kind of mob spawning reasons

Other than being removed from the world, if a mob becomes persistent for the first time like nametagged or item pickup, it will be marked as removal too. If a mob doesn't count towards the mobcap when it spawns, it will not be tracked

This tracker also tracks lifetime of items and xp orbs from mob and block drops as an additional functionality. Note that it doesn't track all item / xp orb spawning, so you'd better have a test before actually using it

Adding a `realtime` suffix to the command will turn the rate result from in-game time based to realtime based

Check rule [lifeTimeTrackerConsidersMobcap](rules.md#lifetimetrackerconsidersmobcap) for switching the strategy with mobs that don't count towards mobcap

### tracking

`/lifetime tracking [<start|stop|restart>]`

Control the lifetime tracker

Tracked entity types:
- All kinds of mob (MobEntity)
- Item Entity
- Experience Orb Entity

Tracked entity spawning reasons
- Natural spawning
- Nether portal pigman spawning
- `/summon` command
- Spawned by item
- Block drop (item only)
- Slime division
- Zombie Reinforce
- Spawned by spawner
- Spawned in raid as raider
- Be summoned by entity or block
- Breeding
- Dispensed by block
- Mob drop (item and xp orb only)
- Mob throw (item only)
- Mob conversion
- Trans-dimension from portal

Note that only entities that have been tracked spawning will be counted to the statistic

Tracked entity removal reasons
- Despawn, including immediately despawn, random despawn, difficulty despawn and timeout despawn
- Damaged to death
- Becomes persistent. Note that the entity is still not removed from the world
- Rides on a vehicle (1.16+). Note that the entity is still not removed from the world
- Enderman picked up a block (1.16+). Note that the entity is still not removed from the world
- Entity merged (item and xp orb only)
- Collected up by hopper or hopper minecart (item only)
- Entering void
- Picked up by player or mob (item and xp orb only)
- Mob conversion
- Trans-dimension through portal
- Other (anything else not in the list)

The definition of lifetime is: **The amount of spawning stage passing between entity spawning and entity removal**, in other words, how many gameticks does the entity counts towards mobcap. Technically the injection point for the passing spawning stage counter increment is right before the world recalculating the mobcap

Statistics are sorted by the proportion of the amount

### <entity_type>

`/lifetime <entity_type> [<life_time|removal|spawning>]`

Show the detail statistic of specific entity type. You can specify which part of the statistic will be output

For example, `/lifetime creeper` shows all statistic of creeper in detail, and `/lifetime creeper removal` only shows removal statistic of creeper in detail

### filter

```
/lifetime filter <entity_type> set <entity_selector>
/lifetime filter <entity_type> clear
```

Set an entity filter for given entity type. Use `global` as the `<entity_type>` to set filter globally

Entities need to be accepted by the related filter to be record by the lifetime tracker

Filter is input as an `@e` style Minecraft entity selector. e.g. `@e[distance=..100,nbt={Item:{id:"minecraft:oak_sapling"}}]`

Use `/lifetime filter` to display current activated filters


## manipulate

Manipulate the world

### container

`/manipulate container`

Manipulate data structure containers of the world, including:

| Container name | Command prefix | Support operations                                                   |
| --- | --- |----------------------------------------------------------------------|
| Entity list | `entity` | reverting, shuffling                                                 |
| Tickable tile entity list | `tileentity` | reverting, shuffling, query overall / specified position information |
| Tile tick queue | `tiletick` | add item, remove items at position                                   |
| Block event queue | `blockevent` | add item, remove items at position                                   |

Command lists:

```
/manipulate container entity [revert|shuffle]
/manipulate container tileentity [query|revert|shuffle|statistic]
/manipulate container tiletick add <pos> <block> <delay> [<priority>]
/manipulate container tiletick remove <pos>
/manipulate container blockevent add <pos> <block> <type> <data>
/manipulate container blockevent remove <pos>
```

### entity

Manipulate target entities

`/manipulate entity <entity_selector> [actions]`

Command lists:

Set / Clear target entities custom name

```
/manipulate entity <target> rename <name_text>
/manipulate entity <target> rename clear
```

Query / Set target entities persistent tag state

```
/manipulate entity <target> persistent
/manipulate entity <target> persistent set <state>
```

Target entities vehicle logic control

```
/manipulate entity <target> mount <vehicle>
/manipulate entity <target> dismount
```

Target entities velocity logic control

```
/manipulate entity <target> velocity [add|set] <x> <y> <z>
```


## raid

### list

`/raid list [<full>]`

List information of all current raids

### tracking

`/raid tracking [<start|stop|restart|realtime>]`

Start a raid tracking to gather statistics from ongoing raids


## refresh

### inventory

`/refresh inventory`: Refresh your inventory

`/refresh inventory <players>`: Refresh selected players' inventory. Requires permission level 2

### chunk

`/refresh chunk`: The same as `/refresh chunk current`

`/refresh chunk current`: Refresh the current chunk you are in

`/refresh chunk all`: Refresh all chunks within your view distance

`/refresh chunk inrange <chebyshevDistance>`: Refresh all chunks within the given chebyshev distance

`/refresh chunk at <chunkX> <chunkZ>`: Refresh the chunk at given position

All chunk refresh operations only affect chunks within your view distance

Multiple chunk refreshing creates lags on server's network thread due to packet compression, so there is an input thresholder for the command to prevent packet over-accumulation


## removeentity

`/removeentity <target>`: Removed target entities from the world directly. Does not affect players


## scounter

Similar to carpet's `/counter` command for its hopper counter, this command is used for counting items output from infinity item supplier hoppers created by rule [hopperNoItemCost](rules.md#hoppernoitemcost)

`/scounter`: View statistics for all supplier counters

`/scounter reset`: Reset all supplier counters

`/scounter <color> [realtime]`: View statistics for specified supplier counter. Append suffix ` realtime` to display the rate using real time

`/scounter <color> reset`: Reset specified supplier counter


## sleep

Immediately Block the current thread for given duration, can be used to create lag

Use this command in command block with rule [instantCommandBlock](rules.md#instantcommandblock) to create lag at any time you want

`/sleep`: Show help

`/sleep <duration> (s|ms|us)`: Sleep for given time duration in given time unit

Available time units:

- `s`: Second, 1 * 10 ^ 0s
- `ms`: Milli-second, 1 * 10 ^ -3s
- `us`: Micro-second, 1 * 10 ^ -6s


## spawn

### mobcapsLocal

**Available in Minecraft 1.18.2+**

`/spawn mobcapsLocal [<player>]`

Display the local mobcap of the specified player in format like carpet's `/spawn mobcaps` command

If no player is specified, it will display the local mobcap of the command source

See also: [tickWarp logger](loggers.md#mobcapslocal)


## tick

### warp status

`/tick warp status`

Display the current status of tick warping, including starter, estimated remaining time etc.

See [tickWarp logger](loggers.md#tickwarp) for easier access
