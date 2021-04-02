# Carpet TIS Addition

[![License](https://img.shields.io/github/license/TISUnion/Carpet-TIS-Addition.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![Issues](https://img.shields.io/github/issues/TISUnion/Carpet-TIS-Addition.svg)](https://github.com/TISUnion/Carpet-TIS-Addition/issues)
[![MC Versions](http://cf.way2muchnoise.eu/versions/For%20MC_carpet-tis-addition_all.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-tis-addition)
[![CurseForge](http://cf.way2muchnoise.eu/full_carpet-tis-addition_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-tis-addition)

[>>> 中文 <<<](https://github.com/TISUnion/Carpet-TIS-Addition/blob/master/README_CN.md)

A [Carpet mod](https://github.com/gnembon/fabric-carpet) (fabric-carpet) extension, a collection of carpet mod style useful tools and interesting features

Use with carpet mod in the same Minecraft version. Use newer carpet mod versions whenever possible

# Index

## [Rules](#rule-list)

- [antiSpamDisabled](#antiSpamDisabled)
- [blockEventPacketRange](#blockEventPacketRange)
- [blockPlacementIgnoreEntity](#blockPlacementIgnoreEntity)
- [chunkTickSpeed](#chunkTickSpeed)
- [commandLifeTime](#commandLifeTime)
- [commandRaid](#commandRaid)
- [creativeOpenShulkerBoxForcely](#creativeOpenShulkerBoxForcely)
- [dispenserNoItemCost](#dispenserNoItemCost)
- [dispensersFireDragonBreath](#dispensersFireDragonBreath)
- [entityMomentumLoss](#entityMomentumLoss)
- [fakePlayerNamePrefix](#fakePlayerNamePrefix)
- [fakePlayerNameSuffix](#fakePlayerNameSuffix)
- [fluidDestructionDisabled](#fluidDestructionDisabled)
- [hopperCountersUnlimitedSpeed](#hopperCountersUnlimitedSpeed)
- [HUDLoggerUpdateInterval](#HUDLoggerUpdateInterval)
- [instantCommandBlock](#instantCommandBlock)
- [keepMobInLazyChunks](#keepMobInLazyChunks)
- [lightQueueLoggerSamplingDuration](#lightQueueLoggerSamplingDuration)
- [lightUpdates](#lightUpdates)
- [microTiming](#microTiming)
- [microTimingTarget](#microTimingTarget)
- [opPlayerNoCheat](#opPlayerNoCheat)
- [optimizedFastEntityMovement](#optimizedFastEntityMovement)
- [optimizedHardHitBoxEntityCollision](#optimizedHardHitBoxEntityCollision)
- [optimizedTNTHighPriority](#optimizedTNTHighPriority)
- [poiUpdates](#poiUpdates)
- [railDupingFix](#railDupingFix)
- [redstoneDustRandomUpdateOrder](#redstoneDustRandomUpdateOrder)
- [renewableDragonEgg](#renewableDragonEgg)
- [renewableDragonHead](#renewableDragonHead)
- [renewableElytra](#renewableElytra)
- [repeaterHalfDelay](#repeaterHalfDelay)
- [sandDupingFix](#sandDupingFix)
- [structureBlockLimit](#structureBlockLimit)
- [synchronizedLightThread](#synchronizedLightThread)
- [tileTickLimit](#tileTickLimit)
- [tntDupingFix](#tntDupingFix)
- [tntFuseDuration](#tntFuseDuration)
- [tooledTNT](#tooledTNT)
- [totallyNoBlockUpdate](#totallyNoBlockUpdate)
- [turtleEggTrampledDisabled](#turtleEggTrampledDisabled)
- [xpTrackingDistance](#xpTrackingDistance)

## [Ported-rules](#ported-rules-list)

- [lightEngineMaxBatchSize](#lightEngineMaxBatchSize)
- [structureBlockOutlineDistance](#structureBlockOutlineDistance)

## [Loggers](#logger-list)

- [ticket](#ticket)
- [memory](#memory)
- [item](#item)
- [xporb](#xporb)
- [raid](#raid)
- [microTiming](#microTiming-1)
- [damage](#damage)
- [commandBlock](#commandBlock)
- [lightQueue](#lightQueue)
- [tickWarp](#tickWarp)
- [turtleEgg](#turtleEgg)
- [lifeTime](#lifeTime)

## [Commands](#command-list)

- [raid](#raid-1)
- [info](#info)
- [lifetime](#lifetime-1)
- [tick](#tick)

## [Scarpet](#scarpet-1)

### [Functions](#functions-1)

- [`register_block(pos)`](#register_blockpos)
- [`unregister_block(pos)`](#unregister_blockpos)
- [`registered_blocks()`](#registered_blocks)
- [`is_registered(pos)`](#is_registeredpos)
  
### [Events](#events)

- [`__on_microtiming_event(type, pos, dimension)`](#__on_microtiming_eventtype-pos-dimension)


## Others

- [other stuffs](#other-stuffs)
- [development](#Development)


# Rule List

## antiSpamDisabled

Disable spamming checks on players, including: chat message cooldown, creative item drop cooldown

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `SURVIVAL`


## blockEventPacketRange

Set the range where player will receive a block event packet after a block event fires successfully

For piston the packet is used to render the piston movement animation. Decrease it to reduce client's lag

- Type: `double`
- Default value: `64`
- Suggested options: `0`, `16`, `64`, `128`
- Categories: `TIS`, `OPTIMIZATION`


## blockPlacementIgnoreEntity

Disable entity collision check before block placement, aka you can place blocks inside entities

Works with creative mode players only

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## chunkTickSpeed

Modify how often the chunk tick occurs per chunk per game tick

The default value is `1`. Set it to `0` to disables chunk ticks

Affected game phases:
- thunder
- ice and snow
- randomtick

With a value of `n`, in every chunk every game tick, climate things will tick `n` times, and randomtick will tick `n` * `randomTickSpeed` times per chunk section

- Type: `int`
- Default value: `1`
- Suggested options: `0`, `1`, `10`, `100`, `1000`
- Categories: `TIS`, `CREATIVE`


## commandLifeTime

Enables `/lifetime` command to track entity lifetime and so on

Useful for mob farm debugging etc.

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `COMMAND`


## commandRaid

Enables `/raid` command for raid listing and tracking

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `COMMAND`


## creativeOpenContainerForcibly

Allow creative players to open a container even if the container is blocked. e.g. for shulker box

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## dispenserNoItemCost

Dispensers and droppers execute without having the itemstack inside decreased

Either dropping and using items do not cost, but dropper transferring item still costs

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `DISPENSER`, `CREATIVE`


## dispensersFireDragonBreath

Dispenser can fire dragon breath bottle to create a dragon breath effect cloud

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `DISPENSER`


## entityMomentumLoss

Set it to `false` to disable entity axis momentum cancellation if it's above 10m/gt when being loaded from disk

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `EXPERIMENTAL`


## fakePlayerNamePrefix

Add a name prefix for fake players spawned with `/player` command

Which can prevent summoning fake player with illegal names and make player list look nicer

Set it to `#none` to stop adding a prefix

- Type: `String`
- Default value: `#none`
- Suggested options: `#none`, `bot_`
- Categories: `TIS`, `CARPET_MOD`


## fakePlayerNameSuffix

Add a name suffix for fake players spawned with `/player` command

Set it to `#none` to stop adding a suffix

- Type: `String`
- Default value: `#none`
- Suggested options: `#none`, `_fake`
- Categories: `TIS`, `CARPET_MOD`


## fluidDestructionDisabled

Disable block destruction by liquid flowing

Fluid will just simple stopped at the state before destroying the block

It's useful to prevent liquid from accidentally flooding your redstone wiring in creative

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## hopperCountersUnlimitedSpeed

Make hopper pointing towards wool has infinity speed to suck in or transfer items

Only works when hopperCounters option in Carpet Mod is on

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `CARPET_MOD`


## HUDLoggerUpdateInterval

Overwrite HUD loggers update interval (gametick)

- Type: `int`
- Default value: `20`
- Suggested options: `1`, `5`, `20`, `100`
- Categories: `TIS`, `CARPET_MOD`


## instantCommandBlock

Make command blocks on redstone ores execute command instantly instead of scheduling a 1gt delay TileTick event for execution

Only affects normal command blocks

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## keepMobInLazyChunks

The mobs in lazy chunks will not despawn, like the behavior before 1.15

This option has no effect in versions before 1.15

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `EXPERIMENTAL`


## lightQueueLoggerSamplingDuration

The sampling duration of light queue logger in game tick

Affects all data except the queue size displayed in the logger

- Type: `int`
- Default value: `60`
- Suggested options: `1`, `20`, `60`, `100`, `6000`
- Categories: `TIS`


## lightUpdates

Pause or disable light updates

If set to suppressed, no light update can be executed which simulates light suppressor

If set to ignored, no light update can be scheduled. It's useful for creating light errors

If set to off, no light update can be scheduled or executed

**\[WARNING\]** If set to suppressed or off, new chunks cannot be loaded. Then if the server tries to load chunk for player movement or whatever reason the server will be stuck forever

- Type: `enum`
- Default value: `on`
- Suggested options: `on`, `suppressed`, `ignored`, `off`
- Categories: `TIS`, `CREATIVE`, `EXPERIMENTAL`


## microTiming

Enable the function of [MicroTiming logger](#microTiming-1)

Display redstone components actions, block updates and stacktrace with a wool block

Use `/log microTiming` to start logging

Might impact the server performance when it's on

EndRods will detect block updates and redstone components will show their actions

| Block Type                                                   | How to log            |
| ------------------------------------------------------------ | --------------------- |
| Observer, Piston, EndRod                                     | pointing towards wool |
| Repeater, Comparator, RedstoneTorch, RedstoneDust, Rail, Button, Lever, PressurePlate, TripwireHook | placed on wool        |

Beside that, a universal block actions logging method is using EndRod on wool block to point on the block you want to log

Check rule [microTimingTarget](#microTimingTarget) to see how to switch logging method

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## microTimingTarget

Modify the way to specify events to be logged in microTiming logger

`labelled`: Logs events labelled with wool

`in_range`: Logs events within 32m of any player

`all`: Logs every event. **Use with caution**

- Type: `enum`
- Default value: `labelled`
- Suggested options: `labelled`, `in_range`, `all`
- Categories: `TIS`, `CREATIVE`


## opPlayerNoCheat

Disable some command to prevent accidentally cheating

Affects command list: `/gamemode`, `/tp`, `/teleport`, `/give`, `/setblock`, `/summon`

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `SURVIVAL`


## optimizedFastEntityMovement

Optimize fast entity movement by only checking block collisions on current moving axis

Inspired by the `fastMovingEntityOptimization` rule in [carpetmod112](https://github.com/gnembon/carpetmod112)

Use with rule `optimizedTNT` to greatly improve performance in cannons

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `OPTIMIZATION`, `EXPERIMENTAL`


## optimizedHardHitBoxEntityCollision

Optimize entity colliding with entities with hard hit box

It uses an extra separate list to store entities, that have a hard hit box including boat and shulker, in a chunk

It reduces quite a lot of unnecessary iterating when an entity is moving and trying to search entities with hard hit box on the way, since the world is always not filled with boats and shulkers

Enable it before loading the chunk to make it work. ~20% performance boost in portal mob farms

Might not work with other mods that add new entities

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `OPTIMIZATION`, `EXPERIMENTAL`


## optimizedTNTHighPriority

Use a Mixin injection with higher priority for carpet rule `optimizedTNT`

So the rule `optimizedTNT` can overwrite lithium's explosion optimization

Of course rule optimizedTNT needs to be on for it to work

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `OPTIMIZATION`, `EXPERIMENTAL`


## poiUpdates

Whether block changes will cause POI to updates or not

Set it to `false` to disable POI updates

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## railDupingFix

Disable rail duping using old school pushing lit powered or activator rail method

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `BUGFIX`


## redstoneDustRandomUpdateOrder

Randomize the order for redstone dust to emit block updates

It's useful to test if your contraption is locational or not

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## renewableDragonEgg

Make dragon egg renewable

When a dragon egg is in dragon breath effect cloud it has a possibility to absorb the effect cloud and `summon` a new dragon egg

Use with rule [dispensersFireDragonBreath](#dispensersfiredragonbreath) for more ease

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`


## renewableDragonHead

Ender dragon killed by charged creeper will drop dragon head

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`


## renewableElytra

Phathom killed by shulker will drops an elytra with given possibility

Set it to 0 to disable

- Type: `double`
- Default value: `0`
- Suggested options: `0`, `0.2`, `1`
- Categories: `TIS`, `FEATURE`


## repeaterHalfDelay

Halve the delay of redstone repeaters upon a redstone ore

The delay will change from 2, 4, 6 or 8 game tick instead of 1, 2, 3 or 4 game tick

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## sandDupingFix

Disable sand and other gravity block duping using end portal

Gravity block includes sand, anvil, dragon egg and so on

In sand dupers sand will only get teleported to the other dimension

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `BUGFIX`


## structureBlockLimit

Overwrite the size limit of structure block

Relative position might display wrongly on client side if it's larger than 32

- Type: `int`
- Default value: `32`
- Suggested options: `32`, `64`, `96`, `127`
- Categories: `TIS`, `CREATIVE`


## synchronizedLightThread

Synchronize lighting thread with the server thread, so the light thread will not lag behind the main thread and get desynchronized

The server will wait until all lighting tasks to be done at the beginning of each world ticking

With this rule you can safely `/tick warp` without potential light suppression or lighting desynchronization

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `EXPERIMENTAL`


## tileTickLimit

Modify the limit of executed tile tick events per game tick

- Type: `int`
- Default value: `65536`
- Suggested options: `1024`, `65536`, `2147483647`
- Categories: `TIS`, `CREATIVE`


## tntDupingFix

Disable TNT, carpet and part of rail dupers

Attachment block update based dupers will do nothing and redstone component update based dupers can no longer keep their duped block

~~Dupe bad dig good~~

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `BUGFIX`, `EXPERIMENTAL`


## tntFuseDuration

Overwrite the default fuse duration of TNT

This might also affects the fuse duration of TNT ignited in explosion

- Type: `int`
- Default value: `80`
- Suggested options: `0`, `80`, `32767`
- Categories: `TIS`, `CREATIVE`


## tooledTNT

Tools on the player's main hand is applied to item dropping during the explosion caused by the player

So you can ignite TNT to harvest blocks that require specific tool or enchantment as long as you are holding the right tool

For example, you can harvest ice with silk touch pickaxe, or harvest grass with shears

It also works for any other living entities beside player

Technically this rule applies the main hand item of the causing entity onto the loot table builder during the explosion

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`


## totallyNoBlockUpdate

Disable all block updates and state updates

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## xpTrackingDistance

Overwrite the tracking distance of xp orb

Change it to 0 to disable tracking

- Type: `double`
- Default value: `8`
- Suggested options: `0`, `1`, `8`, `32`
- Categories: `TIS`, `CREATIVE`


-----------

# Ported rules list

## lightEngineMaxBatchSize

- Source: fabric carpet [1.4.23](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.23)
- Target branches: 1.14.4, 1.15.2

## structureBlockOutlineDistance

- Source: fabric carpet [1.4.25](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.25)
- Target branches: 1.14.4, 1.15.2


-----------

# Logger List

## ticket

`/log ticket <types>`

Info when a ticket is created or removed

Use csv format, like `portal,dragon` for logging multiple types of ticket

Available option separators: `,`, `.` and ` ` (`.` is the only choice in 1.14.4 version)

**Warning:** Logging `unknown` ticket may make you get spammed

Attributes:
- Default option: `portal`
- Suggested options: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## memory

`/log memory`

Display current consumed and total memory of the server in HUD

Attributes:
- Default option: N/A
- Suggested options: N/A


## item

`/log item <events>`

Info when something happens to an item entity, for example item despawned after 5min

Available events:
- `create`: An item entity is created in the world for any kinds of reason. Stack trace is included in the message
- `die`: An item entity died
- `despawn`: An item entity despawned

Use csv format, like `despawn,die` for logging multiple events

Available option separators: `,`, `.` and ` ` (`.` is the only choice in 1.14.4 version)

Attributes:
- Default option: `despawn`
- Suggested options: `despawn`, `die`, `despawn,die`


## xporb

`/log xporb <events>`

Basically the same as [item logger](#item) but logs experience orb entities


## raid

`/log raid`

Info when these raid related events happen:

- A raid has been created
- A raid has been invalidated
- The bad omen level of a raid has been increased
- The center Position of a raid has been moved

Attributes:
- Default option: N/A
- Suggested options: N/A


## microTiming

`/log microTiming <type>`

Log micro timings of redstone components. The ticket of the chunk the component is in needs to be at least lazy-processing (ticket level 32)

Check rule [microTiming](#microTiming) for detail. Remember to use `/carpet microTiming true` to enable logger functionality

Available options: 
- `all`: Default value, log all events
- `merged`: Log all events and merged continuous same events
- `unique`: Log the first unique event in every gametick

Attributes:
- Default option: `merged`
- Suggested options: `all`, `merged`, `unique`


## damage

`/log damage <target>`

Info when a living entity gets damage and display how the damage gets calculated

Available logging targets:
- `all`: Log damage from all living entities
- `players`: Log damage with player participation
- `me`: Log damage related to yourself

Attributes:
- Default option: `all`
- Suggested options: `all`, `players`, `me`


## commandBlock

`/log commandBlock <option>`

Info when a command block or command block minecart executes command

It's useful to find out where the annoying hidden running command block is

With default `throttled` option every command block will log at the highest frequency once every 3 seconds

Attributes:
- Default option: `throttled`
- Suggested options: `throttled`, `all`


## lightQueue

`/log lightQueue`

A HUD logger for debugging light suppression. It displays the following information of the lighting task queue:

- Average task accumulation speed
- Current light queue size. Indicating with symbol `S`
- Estimated duration of light suppression if the light suppressor is switched off now. Indicating with symbol `T`
- Average task enqueuing speed
- Average task executing speed

The sampling duration can be specified with rule [lightQueueLoggerSamplingDuration](#lightQueueLoggerSamplingDuration), default 60gt

Specify the logging option to select the world you want log its light queue, like `/log mobcaps`

Attributes:
- Default option: `dynamic`
- Suggested options: `dynamic`, `overworld`, `the_nether`, `the_end`


## tickWarp

`/log tickWarp <option>`

A HUD logger to display to progress of current tick warping

It only shows up when the server is tick warping

See [/tick warp status](#warp-status) command for displaying more details of tick warp

Attributes:
- Default option: `bar`
- Suggested options: `bar`, `value`


## turtleEgg

`/log turtleEgg`

Logs when a turtle egg is trampled to broken

Attributes:
- Default option: N/A
- Suggested options: N/A


## lifeTime

`/log lifeTime <entity_type>`

A HUD Logger

Displays the current lifetime statistic of specific entity type from the [LifeTime Tracker](#lifetime-1) in the dimension the player is in

The logging options is required to be an available entity type

Attributes:
- Default option: N/A
- Suggested options: All available entity types in current's lifetime tracking


# Command List

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
- Other (anything else not in the list)

The definition of lifetime is: **The amount of spawning stage passing between entity spawning and entity removal**, in other words, how many gameticks does the entity counts towards mobcap. Technically the injection point for the passing spawning stage counter increment is right before the world recalculating the mobcap

Statistics are sorted by the proportion of the amount 

### <entity_type>

`/lifetime <entity_type> [<life_time|removal|spawning>]`

Show the detail statistic of specific entity type. You can specify which part of the statistic will be output

For example, `/lifetime creeper` shows all statistic of creeper in detail, and `/lifetime creeper removal` only shows removal statistic of creeper in detail 


## tick

### warp status

`/tick warp status`

Display the current status of tick warping, including starter, estimated remaining time etc.

See [tickWarp logger](#tickWarp) for easier access

-----------

# Scarpet

Available in Carpet TIS Addition 1.16.4+

## Functions

### `register_block(pos)`

Registers a block position to be tracked with events by scarpet. It is added to a global tracker (which you can view with `is_registered()`). 

Blocks in this list will trigger events in `__on_microtiming_event` if they occur, independently of the loggers.

Returns `true` if block was not previously in list, and `false` if it was.

### `unregister_block(pos)`

Removes a block position from the list of block positions to be tracked

Returns `true` if block was previously in the list, and `false` if not.

### `registered_blocks()`

Returns the list of block positions which are tracked by scarpet to trigger the block event events within scarpet.

### `is_registered(pos)`

Returns `true` or `false` based on whether that position is in the list of tracked block positions or not.

## Events

### `__on_microtiming_event(type, pos, dimension)`

This event will trigger any time any event that can be tracked with the loggers occurs on one of the blocks tracked by scarpet, and that can be updated by the `register_block` and `unregister_block` functions. 

The type determines the type of block event that occurs, which can be one of:

- `'detected_block_update'`
- `'block_state_changed'`
- `'executed_block_event'`
- `'executed_tile_tick'`
- `'emitted_block_update'`
- `'emitted_block_update_redstone_dust'`
- `'scheduled_block_event'`
- `'scheduled_tile_tick'`

Rule [microTiming](#microTiming) is required to be true for dispatching these events

-----------

# Other Stuffs

- Set the maximum length of fake player's name to 16 to prevent kicking out other players
- Set the maximum `/tick warp` duration to `Integer.MAX_VALUE` for fabric-carpet before v1.4.18 (fabric-carpet v1.4.18 removed the `/tick warp` limit)
- Display the version of TIS Carpet Addition inside `/carpet` command
- Make carpet rule `tntRandomRange` works without carpet rule `optimizedTNT` or with lithium mod
- Added `randomly` argument for `/player` command. e.g. `/player Steve use randomly 10 20` will make Steve right-click at dynamically varying random intervals in range \[10, 20]
- Added `/spawn tracking restart` for lazy man
- Cancelled player action pack (triggered by `/player` command) ticking during `/tick freeze`

-----------

# Development

Current main development branch: **1.15.2**

Current maintaining branches:
- 1.14.4, for Minecraft 1.14.4
- 1.15.2, for Minecraft 1.15.2
- 1.16.5, for Minecraft 1.16.4 to 1.16.5
- 1.17, for Minecraft 1.17 snapshots

Current archived branches:
- 1.16, for Minecraft 1.16 to 1.16.1
- 1.16.3, for Minecraft 1.16.2 to 1.16.3

For general new features, implement them in 1.15.2 branch first then merge it into other branches

Branches merge order:
- 1.15.2 -> 1.14.4
- 1.15.2 -> 1.16.5 -> 1.17
- 1.15.2 -> master (when release)

For version specific fixes / patches, implement them in relevant branches

master branches usually only receives doc updates directly

Try not to affect version compatibility unless it's necessary

The English doc and the Chinese doc are aligned line by line btw
