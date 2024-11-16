---
sidebar_position: 1
---

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


## cauldronBlockItemInteractFix

Make player be able to place block against cauldron block with any filled level

Affected Minecraft \<= 1.16.x. This annoying behavior is already been fixed in 1.17+

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## chatMessageLengthLimitUnlocked

Unlock the chat message / command string length limit

The limit will be raised from `256` to `32000`

Carpet TIS Addition is required to be installed on the client

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CLIENT`


## chunkUpdatePacketThreshold

The threshold which the game will just send an chunk data packet if the amount of block changes is more than it

Increasing this value might reduce network bandwidth usage, and boost client's fps if there are lots of tile entities in a chunk section with a lot of block changes

Set it to really high to simulate 1.16+ behavior, which is no chunk packet but only multiple block change packet

This rule is only available in \<1.16

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


## clientSettingsLostOnRespawnFix

Fixed stored client settings are not migrated from the old player entity during player respawn or entering end portal in the end

So mods relies on client settings are always able to work correctly, e.g. serverside translation of this mod and worldedit mod

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `BUGFIX`


## commandLifeTime

Enables `/lifetime` command to track entity lifetime and so on

Useful for mob farm debugging etc.

- Type: `String`
- Default value: `true`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `COMMAND`


## commandManipulate

Enables `/manipulate` command for world related manipulation command

- Type: `String`
- Default value: `false`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `COMMAND`, `EXPERIMENTAL`


## commandRaid

Enables `/raid` command for raid listing and tracking

- Type: `String`
- Default value: `true`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `COMMAND`


## commandRaycast

Enables `/raycast` command for debugging raycast

- Type: `String`
- Default value: `ops`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `COMMAND`


## commandRefresh

Enables `/refresh` command for synchronizing your client to the server

- Type: `String`
- Default value: `true`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `COMMAND`


## commandRemoveEntity

Enables `/removeentity` command for directly erase target entities from the world

- Type: `String`
- Default value: `ops`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `COMMAND`, `CREATIVE`


## commandSleep

Enables `/sleep` command for creating lag

- Type: `String`
- Default value: `ops`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `COMMAND`, `CREATIVE`


## commandSpeedTest

Enables `/speedtest` command for network speed test

You can change the maximum allowed test size with rule [speedTestCommandMaxTestSize](#speedtestcommandmaxtestsize)

- Type: `String`
- Default value: `ops`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `COMMAND`, `TISCM_PROTOCOL`


## creativeInstantTame

Let create player tame animals instantly

Affects cat, wolf, parrot and horse-like animals

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## creativeNetherWaterPlacement

Allow creative players place water via water bucket in nether

Technically this rule applies to all ultrawarm dimensions

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## creativeNoItemCooldown

Remove the cooldown of items used by the creative players

e.g. the 20gt cooldown after throwing an ender pearl

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## creativeOpenContainerForcibly

Allow creative players to open a container even if the container is blocked. e.g. for shulker box

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## debugNbtQueryNoPermission

Remove the permission requirement for the debug nbt request of client's F3 + I action

In vanilla, the request needs permission level 2 at least

Carpet TIS Addition is required to be installed on the client

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CLIENT`


## deobfuscateCrashReportStackTrace

Deobfuscate stack traces in crash report

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`


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


## dustTrapdoorReintroduced

**Minecraft >= 1.20 only**

Reintroduced the dust-trapdoor instant update looper

This rule actually does the following two things:

1. Makes opened trapdoors unable to redirect redstone dust (reintroducing the behavior before mc1.20)
2. Makes redstone dust ignore state updates from below (reintroducing the behavior before mc1.20.3)

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `PORTING`


## enchantCommandNoRestriction

Remove all enchantment restriction checks inside `/enchant` command

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## endPortalOpenedSoundDisabled

Disable the sound emitted when opening an end portal

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## entityBrainMemoryUnfreedFix

Fix brain memory of living entity staying unfreed after the entity has been removed

This could lead to a memory leak if living entities remember other and then get removed continuously and create an endless memory chain in their brains

Fixed [MC-260605](https://bugs.mojang.com/browse/MC-260605), using the same fix that Mojang implemented in 1.19.4-pre3

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `BUGFIX`


## entityInstantDeathRemoval

Remove the 20gt delay before living entity removal after death

When enabled, living entities will despawn immediately after their death

- Type: `boolean`
- Default value: `true`
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


## entityTrackerDistance

The maximum horizontal chebyshev distance (in chunks) for the server to sync entities information to the client

Basically this works as a "entity view distance", but will still be limited to the server view distance

Set it to a value not less than the server view distance to make the server sync all entities within the view distance to the client

Set it to a non-positive value to use vanilla logic

Requires chunk reloading to set the new rule value to entities

- Type: `int`
- Default value: `-1`
- Suggested options: `-1`, `16`, `64`
- Categories: `TIS`, `CREATIVE`


## entityTrackerInterval

The time interval (in gametick) for the server to sync entities information to the client

With a small number e.g. 1, entity information will be synced to the client every 1 gametick, resulting in less-likely client-side entity desync

Set it to a non-positive value to use vanilla logic

Requires chunk reloading to set the new rule value to entities

- Type: `int`
- Default value: `-1`
- Suggested options: `-1`, `1`
- Categories: `TIS`, `CREATIVE`


## explosionNoEntityInfluence

Explosions won't affect any entity

Influences here include damage, acceleration etc.

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


## fakePlayerTicksLikeRealPlayer

Adjust the game phase where carpet's fake player logic and `/player` action packs are ticked, 
to make their behavior as close to real players as possible

Adjustments, before -> after:

1. Fake player entity-related ticking: Entity Phase -> Network Phase
2. `/player` command action packs: Entity Phase -> Asynchronous Task Phase

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CARPET_MOD`


## fakePlayerRemoteSpawning

The permission requirement for spawning remotely a fake player with `/player` command

Here "remotely" means spawning a fake player at more than 16m away, or in other dimension

- Type: `String`
- Default value: `true`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `CARPET_MOD`


## farmlandTrampledDisabled

Disable farmland being able to be trampled into dirt by mobs

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## fillCommandModeEnhance

Enhance modes in the `/fill` command

Add `softreplace` mode: Keep the block state of the original block as much as possible. You can use it to replace block type of stairs / slabs etc.

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `COMMAND`


## fortressNetherBricksPackSpawningFix

**Minecraft >= 1.18.2 only**

Fixed fortress mobs pack spawning isolation issue introduced in mc1.18.2+

Introduced in 1.18.2-pre1. For pack spawning with first attempt on nether bricks, if the spawning attempt location shifts onto a non-nether brick within the inner bounding box, fortress mobs will not able to spawn, and vice versa

Example issue impact: rate of wither rose based wither skeleton farm with bedrock ceiling might have the rate decreased if nether bricks pack spawning floor extension is built

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `BUGFIX`


## flattenTriangularDistribution

**Minecraft >= 1.19 only**

Change all triangle distribution in Minecraft randomizers into uniform distribution

With that edge cases are more likely to happen

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


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

You can use [`/scounter` command](commands.md#scounter) or subscribe to the [`scounter` logger](loggers.md#scounter) to track the amount of the output items

This rule is also the switch of the [`/scounter` command](commands.md#scounter)

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `COMMAND`


## hopperXpCounters

Make hopper counters be able to count experience amount from XP orbs

When enabled, hopper counters will be able to "absorb" XP orbs and count their XP values

You can use the [`/xcounter` command](commands.md#xcounter) command or subscribe to the [`xcounter` logger](loggers.md#xcounter) to see the XP counter results

This rule is also the switch of the [`/xcounter` command](commands.md#xcounter)

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `COMMAND`


## HUDLoggerUpdateInterval

Overwrite HUD loggers update interval (gametick)

- Type: `int`
- Default value: `20`
- Suggested options: `1`, `5`, `20`, `100`
- Categories: `TIS`, `CARPET_MOD`


## instantBlockUpdaterReintroduced

**Minecraft >= 1.19 only**

Reintroduce the instant block update behavior from versions before 1.19

With that update suppression is doable in 1.19+ again

It also provides a better logging results with more clear logic for [microTiming logger](./loggers.md#microtiming), as clean as versions before 1.19

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `PORTING`


## instantCommandBlock

Make command blocks on redstone ores execute command instantly instead of scheduling a 1gt delay TileTick event for execution

Only affects normal command blocks

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## itemEntitySkipMovementDisabled

Removed the movement skipping mechanism when ticking of item entity

Brings back \<=1.13 item entity behavior, where item entities with low velocity on ground still tick movement every gt instead of every 4gt

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


## loggerMovement

The switch / permission requirement of movement logger

- Type: `string`
- Default value: `ops`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `LOGGER`


## microTiming

Enable the function of [MicroTiming logger](./loggers#microtiming)

Log and display redstone components actions, block updates and stacktrace with dye item

Use `/log microTiming` to start logging

Might impact the server performance when it's on

See description of rule [microTimingDyeMarker](#microtimingdyemarker) for instructions of dye markers

**Following stuffs are deprecated and will be removed in the future**

Use wool blocks and end rods to mark blocks that need to be logged

EndRods will detect block updates and redstone components will show their actions

| Block Type                                                   | How to log            |
| ------------------------------------------------------------ | --------------------- |
| Observer, Piston, EndRod                                     | pointing towards wool |
| Repeater, Comparator, RedstoneTorch, RedstoneDust, Rail, Button, Lever, PressurePlate, TripwireHook | placed on wool        |

Beside that, a universal block actions logging method is using EndRod on wool block to point on the block you want to log

Check rule [microTimingTarget](#microtimingtarget) to see how to switch logging method

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

`labelled`: Logs events labelled with wool. **Deprecated**

`in_range`: Logs events within 32m of any player. **Deprecated**

`all`: Logs every event. **Use with caution**. **Deprecated**

`marker_only`: Logs event labelled with dye marker only. Use it with rule [microTimingDyeMarker](#microtimingdyemarker)

- Type: `enum`
- Default value: `marker_only`
- Suggested options: `labelled`, `in_range`, `all`, `marker_only`
- Categories: `TIS`, `CREATIVE`


## microTimingTickDivision

Determine the way to divide game ticks

`world_timer`: Divides at Overworld timer increment

`player_action`: Divides at the beginning of player action

- Type: `enum`
- Default value: `world_timer`
- Suggested options: `world_timer`, `player_action`
- Categories: `TIS`, `CREATIVE`


## minecartFullDropBackport

Backport the feature from Minecraft 1.19+ that minecart entity drops the full cart item on destroy

Only works in Minecraft \< 1.19

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `PORTING`


## minecartPlaceableOnGround

Make minecart being able to be placed directly on ground without rails, like boat

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `FEATURE`


## minecartTakePassengerMinVelocity

Determine the minimum required horizontal velocity (m/gt) for a minecart to pick up nearby entity as its passenger

Set it to 0 to let minecart always take passenger no matter how fast it is, just like a boat

Set it to NaN to let minecart never takes passenger

- Type: `double`
- Default value: `0.1`
- Suggested options: `0`, `0.1`, `NaN`
- Categories: `TIS`, `CREATIVE`


## mobcapsDisplayIgnoreMisc

Ignore mob type "misc" in carpet mobcaps displays

Since it's useless: Nothing in misc category spawns, and it's ignored when calculating mobcap

Affects mobcaps logger and `/spawn mobcap` command

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CARPET_MOD`


## moveableReinforcedDeepslate

**Minecraft >= 1.19 only**

Make reinforced deepslate movable by pistons

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`


## naturalSpawningUse13Heightmap

Change the upper limit of the Y value during the initial coordinate selection in natural mob spawning, to the highest block that blocks light at that XZ position

Basically, this rule reverts the use of the heightmap during natural spawning back to version 1.13 and earlier

Note: This will slightly increase lag during the natual spawning phase

See also: rule [naturalSpawningUse13HeightmapExtra](#naturalSpawningUse13Heightmapextra)

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `PORTING`


## naturalSpawningUse13HeightmapExtra

Ignore piston block, slime block, honey block when calculating the modified heightmap in rule [naturalSpawningUse13Heightmap](#naturalSpawningUse13Heightmap)

It's designed to keep the behavior unchanged from pre-1.13, but might introduce some unintended usage abuse

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `PORTING`


## oakBalloonPercent

The chance for oak sapling top grow into a balloon oak (fancy_oak) in percent

e.g. 0 means no balloon oak, 50 means 50% balloon oak, 100 means always balloon oak

Set it to -1 to disable the rule and use vanilla logic (10% balloon oak)

- Type: `int`
- Default value: `-1`
- Suggested options: `-1`, `0`, `50`, `100`
- Categories: `TIS`, `CREATIVE`


## observerNoDetection

Stop observer from scheduling tile tick event after it receives a state update

Basically this rule disables the detection functionality of observers

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## obsidianPlatformBlockBreakerBackport

Backport the feature from Minecraft 1.21+ that the creation of the obsidian platform in the end dimension can break existing blocks

Notes: block iterating order for obsidian platform creation for normal entity in vanilla [1.16, 1.21) is different from mc1.21+

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `PORTING`


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


## redstoneDustRepeaterComparatorIgnoreUpwardsStateUpdate

**Minecraft >= 1.20.2 only**

Make redstone dust, redstone repeater and redstone comparator ignore state updates from below

This reverts the change introduced in 23w35a (1.20.2 snapshot). With the rule, floating comparator can be easily created once

Notes: rule `dustTrapdoorReintroduced` implements the same revert for redstone dust

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `PORTING`


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


## shulkerBoxCCEReintroduced

**Minecraft >= 1.20.2 only**

Reintroduce the `ClassCastException` suppressor by reading comparator signal from a shulker block box with non `Inventory`-type block entity at it

Long live void's magic box!

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `PORTING`


## shulkerBoxContentDropBackport

**Minecraft < 1.17 only**

Backport the feature that item entity of skulker box drops all of its contents when the item entity is damaged to die

This feature is introduced to vanilla Minecraft in mc1.17

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `PORTING`


## snowMeltMinLightLevel

The minimum light level allowed for snow to melt

In vanilla the value is 12, which means snow will melt when the light level >=12 when random ticked

Set it to 0 to melt all annoying snow on your builds

Set it to the same level as the minimum light level for snow to not fall on block (light level 10) to easily test if your build is snow-proof with light or not

You can modify gamerule `randomTickSpeed` to speed up the melting progress, or modify carpet rule `chunkTickSpeed` to speed up the snowfall progress

- Type: `int`
- Default value: `12`
- Suggested options: `0`, `10`, `12`
- Categories: `TIS`, `CREATIVE`


## spawnBabyProbably

When spawning mobs, if baby variant exists, spawn the baby variant with given probably

Set it to `-1` to disable the rule and use vanilla logic

- Type: `double`
- Default value: `-1`
- Suggested options: `-1`, `0`, `0.5`, `1`
- Categories: `TIS`, `CREATIVE`


## spawnJockeyProbably

When spawning mobs, if jockey variant exists, spawn the jockey variant with given probably

Affected jockeys: chicken jockey, spider jockey, strider jockey

For striders, the spawn ratio between zombified piglin and baby strider is still 1:3

Set it to `-1` to disable the rule and use vanilla logic

- Type: `double`
- Default value: `-1`
- Suggested options: `-1`, `0`, `0.5`, `1`
- Categories: `TIS`, `CREATIVE`


## speedTestCommandMaxTestSize

The max test size in MiB when using the `/speedtest` command for network speed test

- Type: `int`
- Default value: `10`
- Suggested options: `10`, `100`, `1024`, `10240`
- Categories: `TIS`, `COMMAND`


## stopCommandDoubleConfirmation

Add a double confirmation for `/stop` command to prevent stopping server accidentally

You need to enter `/stop` twice within 1 minute to stop the server

This mechanics only works for players

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `COMMAND`


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


## syncServerMsptMetricsData

Sync server's mspt metrics data to the client, so players can see that in the debug screen with F3 + ALT

Carpet TIS Addition is required to be installed on the client

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `TISCM_PROTOCOL`


## tickCommandCarpetfied

**Minecraft >= 1.20.3 only**

The only rule you need to bring back the `/tick` command from carpet mod before mc1.20.3

Enabling this rule equals to setting the following rules to the following values:

- [tickCommandEnhance](#tickcommandenhance) = `true`
- [tickCommandPermission](#tickcommandpermission) = `2`
- [tickFreezeCommandToggleable](#tickfreezecommandtoggleable) = `true`
- [tickProfilerCommandsReintroduced](#tickprofilercommandsreintroduced) = `true`
- [tickWarpCommandAsAnAlias](#tickwarpcommandasanalias) = `true`

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CARPET_MOD`, `COMMAND`, `PORTING`


## tickCommandEnhance

**Minecraft >= 1.20.3 only**

Enable the enhancements for the /tick command from carpet mod before mc1.20.3

Enhancement list:

1. Enable the `/tick sprint status` command (previously it was "/tick warp status")

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CARPET_MOD`, `COMMAND`


## tickCommandPermission

**Minecraft >= 1.20.3 only**

Override the permission level requirement for the  `/tick` command

Set it to `2` or `ops` to restore the default carpet mod behavior before mc1.20.3

It's useful if you want to execute the `/tick` command in e.g. command blocks

- Type: `String`
- Default value: `3`
- Suggested options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `TIS`, `CARPET_MOD`, `COMMAND`


## tickFreezeCommandToggleable

**Minecraft >= 1.20.3 only**

Make the `/tick freeze` command toggleable again, just like how carpet mode behaved before

i.e. Make the `/tick freeze` command unfreeze the game if the game is already frozen

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CARPET_MOD`, `COMMAND`, `PORTING`


## tickProfilerCommandsReintroduced

**Minecraft >= 1.20.3 only**

Bring back the `/tick health` and `/tick entities` subcommands from carpet mod

Just like the previous carpet mod, these 2 subcommands have the same behavior as `/profile [health|entities]` subcommands

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CARPET_MOD`, `COMMAND`, `PORTING`


## tickWarpCommandAsAnAlias

**Minecraft >= 1.20.3 only**

Bring back the `/tick warp` subcommand, as an alias of the vanilla `/tick sprint` command

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CARPET_MOD`, `COMMAND`, `PORTING`


## tileTickLimit

Modify the limit of executed tile tick events per game tick

- Type: `int`
- Default value: `65536`
- Suggested options: `1024`, `65536`, `2147483647`
- Categories: `TIS`, `CREATIVE`


## tiscmNetworkProtocol

The switch of the TISCM network protocol

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `TIS`, `TISCM_PROTOCOL`


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


## toughWitherRose

Birth from death, wither rose is tough enough to be planted on any surface

This rule removes all placement requirement of wither rose block, which means you're able to place wither rose at anywhere

It's useful when you want to play around with update suppressed wither roses for wither skeleton farms

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


## undeadDontBurnInSunlight

Prevent undead creatures burning in sunlight

Their helmets will still get damaged in sunlight though

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## updateSuppressionSimulator

Activator / Powered rail on a lapis ore simulates an update suppressor

Right before a powered activator / powered rail on a lapis ore setting its powered state to false, throw the given JVM throwable

`false`: rule disabled; `true`: rule enable and use `StackOverflowError`; others: feature enable and use given throwable

- Type: `String`
- Default value: `false`
- Suggested options: `false`, `true`, `StackOverflowError`, `OutOfMemoryError`, `ClassCastException`
- Categories: `TIS`, `CREATIVE`


## vaultBlacklistDisabled

Disable functionalities related to the vault block's player blacklist

After enabling this rule:

1. When players open the vault, their UUIDs will not be added to the vault blacklist
2. Regardless of whether players are on the vault blacklist, they can activate or open the vault

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## visualizeProjectileLoggerEnabled

Enable visualize projectile logger

Try `/log projectiles visualize`

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## voidDamageAmount

Modify the amount of void damage

- Type: `double`
- Default value: `4`
- Suggested options: `0`, `4`, `-1000`
- Categories: `TIS`, `CREATIVE`


## voidDamageIgnorePlayer

Prevent players from taking void damage

Completely harmless void for players, yay!

If the rule value is set to a comma-separated game mode list, then only players in these game modes are immutable to void damage

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## voidRelatedAltitude

Modify the related altitude between the bottom of the world and the void where entities will receive void damages

- Type: `double`
- Default value: `-64`
- Suggested options: `-64`, `-512`, `-4096`
- Categories: `TIS`, `CREATIVE`


## witherSpawnedSoundDisabled

Disable the wither spawned sound emitted when a wither fully reset its health after summoned

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## xpTrackingDistance

Overwrite the tracking distance of xp orb

Change it to 0 to disable tracking

- Type: `double`
- Default value: `8`
- Suggested options: `0`, `1`, `8`, `32`
- Categories: `TIS`, `CREATIVE`


## yeetOutOfOrderChatKick

**Minecraft >= 1.19 only**

Prevent players being kicked with message "Out-of-order chat packet received..." caused by whatever reason

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `TIS`


-----------

# Ported rules

## lightEngineMaxBatchSize

- Source: fabric carpet [1.4.23](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.23)
- Target branches: 1.14.4, 1.15.2

## structureBlockOutlineDistance

- Source: fabric carpet [1.4.25](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.25)
- Target branches: 1.14.4, 1.15.2

## yeetUpdateSuppressionCrash

Prevent the server from crashing due to `StackOverflowError`, `OutOfMemoryError` or `ClassCastException`

Do the same thing as fabric carpet's `updateSuppressionCrashFix` rule, but with more information

- Source: 
  - rule `updateSuppressionCrashFix` from fabric carpet [1.4.50](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.50)
  - rule `yeetUpdateSuppressionCrash` from TISCarpet13 [build238](https://github.com/TISUnion/TISCarpet113/releases/tag/build238)
