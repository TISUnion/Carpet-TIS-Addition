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
- Categories: `TIS`, `CARPET`


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
- Categories: `TIS`, `CARPET`

-----------

# Loggers

**DO NOT works in 1.14.4 version due to carpet not supported loggers from extensions**

## ticket

`/log ticket <types>`

Info when a ticket is created

Use csv format, like `portal,dragon` for logging multiple types of ticket

**Warning:** Logging `unknown` ticket may make you get spammed

- Default value: `portal`
- Suggested options: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## memory

`/log memory`

Display current consumed and total memory of the server in HUD

-----------

# Statistics

All these custom stats can't be found in the statistic page in the client for vanilla compatibility


## break_bedrock

When a bedrock is deleted by a piston or a sticky piston, the nearest player to the bedrock within 5m will increase this stat by 1

Criteria: `minecraft.custom:minecraft.break_bedrock`

-----------

# Other Stuffs

- Set the maximum length of fake player's name to 16 to prevent kicking out player
