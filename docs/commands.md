**English** | [中文](commands_cn.md)

\>\>\> [Back to index](readme.md)

# Commands

## raid

### list

`/raid list [<full>]`

List information of all current raids

### tracking

`/raid tracking [<start|stop|restart|realtime>]`

Start a raid tracking to gather statistics from ongoing raids


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
- Portal pigman spawning
- Trans-dimension from portal
- Spawned by item (spawn eggs etc.)
- Slime division (for slime and magma cube)
- Zombie Reinforce
- Spawner
- `/summon` command
- Mob drop (item and xp orb only)
- Block drop (item only)

Note that only entities that have been tracked spawning will be counted to the statistic

Tracked entity removal reasons
- Despawn, including immediately despawn, random despawn, difficulty despawn and timeout despawn
- Damaged to death
- Becomes persistent. Note that the entity is still not removed from the world
- Rides on a vehicle (1.16+). Note that the entity is still not removed from the world
- Trans-dimension through portal
- Entity merged (item and xp orb only)
- Picked up by player (item and xp orb only)
- Collected up by hopper or hopper minecart (item only)
- Entering void
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


## tick

### warp status

`/tick warp status`

Display the current status of tick warping, including starter, estimated remaining time etc.

See [tickWarp logger](#tickWarp) for easier access


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


## manipulate

Manipulate the world

### container

`/manipulate container`

Manipulate data structure containers of the world, including:

| Container name | Command prefix | Support operations |
| --- | --- | --- |
| Entity list | `entity` | reverting, shuffling |
| Tickable tile entity list | `tileentity` | reverting, shuffling |
| Tile tick queue | `tiletick` | add item, remove items at position |
| Block event queue | `blockevent` | add item, remove items at position |

Command lists:

```
/manipulate container entity [revert|shuffle]
/manipulate container tileentity [revert|shuffle]
/manipulate container tiletick add <pos> <block> <delay> [<priority>]
/manipulate container tiletick remove <pos>
/manipulate container blockevent add <pos> <block> <type> <data>
/manipulate container blockevent remove <pos>
```
