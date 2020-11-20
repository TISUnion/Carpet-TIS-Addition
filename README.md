# Carpet TIS Addition

[![License](https://img.shields.io/github/license/TISUnion/Carpet-TIS-Addition.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![Issues](https://img.shields.io/github/issues/TISUnion/Carpet-TIS-Addition.svg)](https://github.com/TISUnion/Carpet-TIS-Addition/issues)
[![MC Versions](http://cf.way2muchnoise.eu/versions/For%20MC_carpet-tis-addition_all.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-tis-addition)
[![CurseForge](http://cf.way2muchnoise.eu/full_carpet-tis-addition_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-tis-addition)

[>>> 中文 <<<](https://github.com/TISUnion/Carpet-TIS-Addition/blob/master/README_CN.md)

A [Carpet mod](https://github.com/gnembon/fabric-carpet) (fabric-carpet) extension, a collection of carpet mod style useful tools and interesting features

Use with carpet mod in the same Minecraft version. Use newer carpet mod versions whenever possible

# Index

## Rules

- [blockEventPacketRange](#blockEventPacketRange)
- [structureBlockLimit](#structureBlockLimit)
- [xpTrackingDistance](#xpTrackingDistance)
- [tntDupingFix](#tntDupingFix)
- [fakePlayerNamePrefix](#fakePlayerNamePrefix)
- [fakePlayerNameSuffix](#fakePlayerNameSuffix)
- [renewableDragonEgg](#renewableDragonEgg)
- [dispensersFireDragonBreath](#dispensersFireDragonBreath)
- [renewableDragonHead](#renewableDragonHead)
- [HUDLoggerUpdateInterval](#HUDLoggerUpdateInterval)
- [hopperCountersUnlimitedSpeed](#hopperCountersUnlimitedSpeed)
- [renewableElytra](#renewableElytra)
- [sandDupingFix](#sandDupingFix)
- [railDupingFix](#railDupingFix)
- [commandRaid](#commandRaid)
- [keepMobInLazyChunks](#keepMobInLazyChunks)
- [dispenserNoItemCost](#dispenserNoItemCost)
- [opPlayerNoCheat](#opPlayerNoCheat)
- [redstoneDustRandomUpdateOrder](#redstoneDustRandomUpdateOrder)
- [instantCommandBlock](#instantCommandBlock)
- [lightUpdates](#lightUpdates)
- [microTiming](#microTiming)
- [microTimingTarget](#microTimingTarget)
- [antiSpamDisabled](#antiSpamDisabled)
- [blockPlacementIgnoreEntity](#blockPlacementIgnoreEntity)
- [chunkTickSpeed](#chunkTickSpeed)
- [tileTickLimit](#tileTickLimit)

## Loggers

- [ticket](#ticket)
- [memory](#memory)
- [item](#item)
- [xporb](#xporb)
- [raid](#raid)
- [microTiming](#microTiming-1)
- [damage](#damage)

## Commands

- [raid](#raid-1)
- [info](#info)

## Others

- [other stuffs](#other-stuffs)


# Rule List

## blockEventPacketRange

Set the range where player will receive a block event packet after a block event fires successfully

For piston the packet is used to render the piston movement animation. Decrease it to reduce client's lag

- Type: `double`  
- Default value: `64`  
- Suggested options: `0`, `16`, `64`, `128`
- Categories: `TIS`, `OPTIMIZATION` 


## structureBlockLimit

Overwrite the size limit of structure block

Relative position might display wrongly on client side if it's larger than 32

- Type: `int`  
- Default value: `32`  
- Suggested options: `32`, `64`, `96`, `127`
- Categories: `TIS`, `CREATIVE` 


## xpTrackingDistance

Overwrite the tracking distance of xp orb

Change it to 0 to disable tracking

- Type: `double`  
- Default value: `8`
- Suggested options: `0`, `1`, `8`, `32`
- Categories: `TIS`, `CREATIVE` 


## tntDupingFix

Disable TNT, carpet and part of rail dupers

Attachment block update based dupers will do nothing and redstone component update based dupers can no longer keep their duped block

~~Dupe bad dig good~~

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `BUGFIX`, `EXPERIMENTAL` 


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


## renewableDragonEgg

Make dragon egg renewable

When a dragon egg is in dragon breath effect cloud it has a possibility to absorb the effect cloud and "summon" a new dragon egg

Use with rule [dispensersFireDragonBreath](#dispensersfiredragonbreath) for more ease

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`


## dispensersFireDragonBreath

Dispenser can fire dragon breath bottle to create a dragon breath effect cloud

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `DISPENSER`


## renewableDragonHead

Ender dragon killed by charged creeper will drop dragon head

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`


## HUDLoggerUpdateInterval

Overwrite HUD loggers update interval (gametick)

- Type: `int`  
- Default value: `20`
- Suggested options: `1`, `5`, `20`, `100`
- Categories: `TIS`, `CARPET_MOD`


## hopperCountersUnlimitedSpeed

Make hopper pointing towards wool has infinity speed to suck in or transfer items

Only works when hopperCounters option in Carpet Mod is on

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `CARPET_MOD`


## renewableElytra

Phathom killed by shulker will drops an elytra with given possibility

Set it to 0 to disable

- Type: `double`  
- Default value: `0`  
- Suggested options: `0`, `0.2`, `1`
- Categories: `TIS`, `FEATURE`


## sandDupingFix

Disable sand and other gravity block duping using end portal

Gravity block includes sand, anvil, dragon egg and so on

In sand dupers sand will only get teleported to the other dimension

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `BUGFIX`


## railDupingFix

Disable rail duping using old school pushing lit powered or activator rail method

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `BUGFIX`


## commandRaid

Enables `/raid` command for raid tracking

- Type: `boolean`  
- Default value: `true`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `COMMAND`


## keepMobInLazyChunks

The mobs in lazy chunks will not despawn, like the behavior before 1.15

This option has no effect in versions before 1.15

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `FEATURE`, `EXPERIMENTAL` 


## dispenserNoItemCost

Dispensers and droppers execute without having the itemstack inside decreased

Either dropping and using items do not cost, but dropper transferring item still costs

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `DISPENSER`, `CREATIVE`


## opPlayerNoCheat

Disable some command to prevent accidentally cheating

Affects command list: `/gamemode`, `/tp`, `/teleport`, `/give`, `/setblock`, `/summon`

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `SURVIVAL`


## redstoneDustRandomUpdateOrder

Randomize the order for redstone dust to emit block updates

It's useful to test if your contraption is locational or not

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## instantCommandBlock

Make command blocks on redstone ores execute command instantly instead of scheduling a 1gt delay TileTick event for execution

Only affects normal command blocks

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`


## lightUpdates

Pause or disable light updates

If set to suppressed, no light update can be executed

If set to off, no light update can be scheduled or executed

**\[WARNING\]** If set to suppressed or off, new chunks cannot be loaded. Then if the server tries to load chunk for player movement or whatever reason the server will be stuck forever

- Type: `enum`  
- Default value: `on`  
- Suggested options: `on`, `suppressed`, `off`
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


## antiSpamDisabled

Disable spamming checks on players, including: chat message cooldown, creative item drop cooldown

- Type: `boolean`  
- Default value: `false`  
- Suggested options: `false`, `true`
- Categories: `TIS`, `CREATIVE`, `SURVIVAL`


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

## tileTickLimit

Modify the limit of executed tile tick events per game tick

- Type: `int`  
- Default value: `65536`  
- Suggested options: `1024`, `65536`, `2147483647`
- Categories: `TIS`, `CREATIVE`

-----------

# Loggers

## ticket

`/log ticket <types>`

Info when a ticket is created or removed

Use csv format, like `portal,dragon` for logging multiple types of ticket

Available option separators: `,`, `.` and ` ` (`.` is the only choice in 1.14.4 version)

**Warning:** Logging `unknown` ticket may make you get spammed

- Default value: `portal`
- Suggested options: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## memory

`/log memory`

Display current consumed and total memory of the server in HUD

Attributes:
- Default value: N/A
- Suggested options: N/A


## item

`/log item <events>`

Info when something happens to an item entity, for example item despawned after 5min

Available events:
- `die`: An item entity died
- `despawn`: An item entity despawned

Use csv format, like `despawn,die` for logging multiple events

Available option separators: `,`, `.` and ` ` (`.` is the only choice in 1.14.4 version)

Attributes:
- Default value: `despawn`
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
- Default value: N/A
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
- Default value: `merged`
- Suggested options: `all`, `merged`, `unique`


## damage

`/log damage <target>`

Info when a living entity gets damage and display how the damage gets calculated

Available logging targets:
- `all`: Log damage from all living entities
- `players`: Log damage with player participation
- `me`: Log damage related to yourself

Attributes:
- Default value: `all`
- Suggested options: `all`, `players`, `me`

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

-----------

# Other Stuffs

- Set the maximum length of fake player's name to 16 to prevent kicking out other players
- Display the version of TIS Carpet Addition inside `/carpet` command
