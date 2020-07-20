Carpet-TIS-Addition
-----------

[>>> 中文 <<<](https://github.com/TISUnion/Carpet-TIS-Addition/blob/1.15.2/README_CN.md)

A [Carpet mod](https://github.com/gnembon/fabric-carpet) extension, a collection of specific needs of TIS and interesting features

-----------

# Features

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

Disable TNT, carpet and rail duping

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


## renewableDragonEgg

Make dragon egg renewable

When a dragon egg is in dragon breath effect cloud it has a possibility to absorb the effect cloud and "summon" a new dragon egg

Use with rule [dispensersFireDragonBreath](https://github.com/TISUnion/Carpet-TIS-Addition#dispensersfiredragonbreath) for more ease
 
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

-----------

# Loggers

**DO NOT works in 1.14.4 version due to carpet not supported loggers from extensions**

## ticket

`/log ticket <types>`

Info when a ticket is created or removed

Use csv format, like `portal,dragon` for logging multiple types of ticket

**Warning:** Logging `unknown` ticket may make you get spammed

- Default value: `portal`
- Suggested options: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## memory

`/log memory`

Display current consumed and total memory of the server in HUD


## item

`/log item <events>`

Info when something happens to an item entity, for example item despawned after 5min

Available events:
- `die`: An item entity died
- `despawn`: An item entity despawned

Use csv format, like `despawn,die` for logging multiple events

- Default value: `despawn`
- Suggested options: `despawn`, `die`, `despawn,die`


## raid

`/log raid`

Info when these raid related events happen:

- A raid has been created
- A raid has been invalidated;
- The bad omen level of a raid has been increased
- The center Position of a raid has been moved


# Commands

## raid

### list

`/raid list [<full>]`

List information of all current raids

### tracking

`raid tracking [<start|stop|restart|realtime>]`

Start a raid tracking to gather statistics from ongoing raids

-----------

# Statistics

Custom statistics have been removed. If you want to use them check this [more-statistics](https://github.com/Fallen-Breath/more-statistics) mod

-----------

# Other Stuffs

- Set the maximum length of fake player's name to 16 to prevent kicking out player
- set the maximum tick warp maximum duration to `Integer.MAX_VALUE`
