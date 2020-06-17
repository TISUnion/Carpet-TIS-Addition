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
  

# Carpet TIS Addition loggers

## ticket

Info when a ticket is created

Use csv format, like `portal,dragon` for logging multiple types of ticket

**Warning:** Logging `unknown` ticket may make you get spammed

- Default value: `portal`
- Suggested options: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`
