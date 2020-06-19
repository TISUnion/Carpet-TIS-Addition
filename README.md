# Carpet-TIS-Addition

Carpet TIS Addition

# Carpet TIS Addition features

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
- Categories: `TIS` 


# Carpet TIS Addition loggers

**DO NOT works in 1.14.4 version due to carpet not supported loggers from extensions**

## ticket

Info when a ticket is created

Use csv format, like `portal,dragon` for logging multiple types of ticket

**Warning:** Logging `unknown` ticket may make you get spammed

- Default value: `portal`
- Suggested options: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## Memory

Display current consumed and total memory of the server in HUD



# Other Stuffs

- Set the maximum length of fake player's name to 16 to prevent kicking out player
