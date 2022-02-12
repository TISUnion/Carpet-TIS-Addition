**English** | [中文](rules_cn.md)

\>\>\> [Back to index](readme.md)

# Rules

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


## chunkUpdatePacketThreshold

The threshold which the game will just send an chunk data packet if the amount of block changes is more than it

Increasing this value might reduce network bandwidth usage, and boost client's fps if there are lots of tile entities in a chunk section with a lot of block changes

Set it to really high to simulate 1.16+ behavior, which is no chunk packet but only multiple block change packet

This rule is only available in <1.16

- Type: `int`
- Default value: `64`
- Suggested options: `64`, `4096`, `65536`
- Categories: `TIS`, `OPTIMIZATION`, `EXPERIMENTAL`


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


## commandManipulate

Enables `/manipulate` command for world related manipulation command

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `COMMAND`, `EXPERIMENTAL`


## commandRaid

Enables `/raid` command for raid listing and tracking

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `COMMAND`


## commandRefresh

Enables `/refresh` command for synchronizing your client to the server

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


## enchantCommandNoRestriction

Remove all enchantment restriction checks inside `/enchant` command

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## entityMomentumLoss

Set it to `false` to disable entity axis momentum cancellation if it's above 10m/gt when being loaded from disk

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `EXPERIMENTAL`


## entityPlacementIgnoreCollision

Disable block and entity collision check during entity placement with items

Affected items: armorstand, end crystal, all kinds of boat

Spawn egg items are not affected

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## explosionPacketRange

Set the range where player will receive an explosion packet when an explosion happens

- Type: `double`
- Default value: `64`
- Suggested options: `0`, `16`, `64`, `128`, `2048`
- Categories: `TIS`, `CREATIVE`


## failSoftBlockStateParsing

Ignore invalid property keys/values in block state arguments used in e.g. `/setblock` command

In vanilla invalid property keys/values cause command failure when parsing, this rule suppresses that

Useful during cross-version litematica schematic pasting etc.

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## fakePlayerNamePrefix

Add a markerName prefix for fake players spawned with `/player` command

Which can prevent summoning fake player with illegal names and make player list look nicer

Set it to `#none` to stop adding a prefix

- Type: `String`
- Default value: `#none`
- Suggested options: `#none`, `bot_`
- Categories: `TIS`, `CARPET_MOD`


## fakePlayerNameSuffix

Add a markerName suffix for fake players spawned with `/player` command

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

Make hopper pointing towards wool has infinity speed to suck in or transfer items with no cooldown

Only works when hopperCounters option in Carpet Mod is on

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `CARPET_MOD`


## hopperNoItemCost

Hopper with wool block on top outputs item infinitely without having its item decreased

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


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


## itemEntitySkipMovementDisabled

Removed the movement skipping mechanism when ticking of item entity

Brings back <=1.13 item entity behavior, where item entities with low velocity on ground still tick movement every gt instead of every 4gt

Useful when you require precise item entity movement timing

Breaks related redstone devices, e.g. 2no2name's wireless redstone

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## keepMobInLazyChunks

The mobs in lazy chunks will not despawn, like the behavior before 1.15

This option only have effects between Minecraft 1.15 and 1.16

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `EXPERIMENTAL`


## largeBarrel

The best storage block ever: Large barrel

Two adjacent barrel blocks with their bottom side facing towards each other create a large barrel

The behavior and logic of large barrel is just like large chest

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `EXPERIMENTAL`


## lifeTimeTrackerConsidersMobcap

Strategy for lifetime tracker to deal with mob that doesn't count towards mobcap

`true`: Don't track mobs that don't count towards mobcap, and treat mobs as removal as soon as they don't affect mobcap e.g. right when they pick up some items. Good for mob farm designing

`false`: Tracks everything it can track and mark mobs as removal when they actually get removed. Good for raid testing or non-mobfarms.

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


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


## microTimingDyeMarker

Allow player to right click with dye item to mark a block to be logged by microTiming logger

You need to subscribe to microTiming logger for marking or displaying blocks

Right click with the same dye to switch the marker to end rod mode with which block update information will be logged additionally. Right click again to remove the marker

Right click a marker with slime ball item to make it movable. It will move to the corresponding new position when the attaching block is moved by a piston

Use `/carpet microTimingDyeMarker clear` to remove all markers

You can create a named marker by using a renamed dye item. Marker name will be shown in logging message as well

You can see boxes at marked blocks with fabric-carpet installed on your client. With carpet-tis-addition installed the marker name could also be seen through blocks.

*Visual rendering thing doesn't work in 1.14.4 branch due to lack of carpet network and scarpet shape rendering framework in frabic carpet*

- Type: `string`
- Default value: `true`
- Suggested options: `false`, `true`, `clear`
- Categories: `TIS`, `CREATIVE`


## microTimingTarget

Modify the way to specify events to be logged in microTiming logger. Events labelled with dye marker are always logged

`labelled`: Logs events labelled with wool

`in_range`: Logs events within 32m of any player

`all`: Logs every event. **Use with caution**

`marker_only`: Logs event labelled with dye marker only. Use it with rule [microTimingDyeMarker](#microTimingDyeMarker)

- Type: `enum`
- Default value: `labelled`
- Suggested options: `labelled`, `in_range`, `all`
- Categories: `TIS`, `CREATIVE`


## microTimingTickDivision

Determine the way to divide game ticks

`world_timer`: Divides at Overworld timer increment

`player_action`: Divides at the beginning of player action

- Type: `enum`
- Default value: `world_timer`
- Suggested options: `world_timer`, `player_action`
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


## persistentLoggerSubscription

Remember the subscribed loggers as well as logging options of the player during server restart

Only applies carpet's defaultLoggers rule at someone's first login

Logger subscriptions are saved in `config/carpettisaddition/logger_subscriptions.json`

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CARPET_MOD`


## preciseEntityPlacement

When placing / summoning entity with item, place the entity at the exact position of player's cursor target position

Affected items: Spawn eggs, armorstand, ender crystal

- Type: `boolean`
- Default value: `false`
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

Does not work with rule `fastRedstoneDust`

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

Phantom killed by shulker will drops an elytra with given possibility

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


## structureBlockDoNotPreserveFluid

Structure block do not preserve existed fluid when placing waterlogged-able blocks

Has a side effect of suppressing bug [MC-130584](https://bugs.mojang.com/browse/MC-130584) happening

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `BUGFIX`


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


## tntIgnoreRedstoneSignal

Prevent TNT blocks from being ignited from redstone

You can still use explosion etc. to ignite a tnt

- Type: `int`
- Default value: `80`
- Suggested options: `0`, `80`, `32767`
- Categories: `TIS`, `CREATIVE`


## tntFuseDuration

Overwrite the default fuse duration of TNT

This might also affect the fuse duration of TNT ignited in explosion

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
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
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## turtleEggTrampledDisabled

Disable turtle egg trampled to broken

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## visualizeProjectileLoggerEnabled

Enable visualize projectile logger

Try `/log projectiles visualize`

- Type: `boolean`
- Default value: `false `
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## voidRelatedAltitude

Modify the related altitude between the bottom of the world and the void where entities will receive void damages

- Type: `double`
- Default value: `-64`
- Suggested options: `-64`, `-512`, `-4096`
- Categories: `TIS`, `CREATIVE`


## xpTrackingDistance

Overwrite the tracking distance of xp orb

Change it to 0 to disable tracking

- Type: `double`
- Default value: `8`
- Suggested options: `0`, `1`, `8`, `32`
- Categories: `TIS`, `CREATIVE`


-----------

# Ported rules

## lightEngineMaxBatchSize

- Source: fabric carpet [1.4.23](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.23)
- Target branches: 1.14.4, 1.15.2

## structureBlockOutlineDistance

- Source: fabric carpet [1.4.25](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.25)
- Target branches: 1.14.4, 1.15.2
