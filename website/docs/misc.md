---
sidebar_position: 4
---

# Miscellaneous

# Scarpet

**Available in Minecraft 1.16.4+**

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

Rule [microTiming](rules.md#microtiming) is required to be true for dispatching these events

-----------

# Other stuffs

## Carpet Rule Tweaks

- Make carpet rule `tntRandomRange` works without carpet rule `optimizedTNT` or with lithium mod
- Enhanced rule `creativeNoClip`: Following actions now ignore players in creativeNoClip state
  - Dispenser block placement
  - Xp orb tracking
  - Tripwire and pressure plate detecting
  - Entity movement caused by block change, e.g. farmland being broken
  - Calculation of collision boxes from entities, which will be used in e.g. entity (especially minecart and boat) movement calculation, boat placement

## Carpet Command Tweaks

### player

- Add `randomly` argument for `/player` command to make the player perform action at dynamically varying random intervals
  - Supported random generators: uniform, poisson, gaussian, triangular
  - Support test run with `--simulate`
  - Use `/player someone someaction randomly` to get more help
- Add `rejoin` argument for `/player` command to spawn fake player. Like `/player spawn`, but it preserves the fake player's last-login position and rotation
- Add `after` argument for `/player` action pack commands. e.g. `/player Steve use after 10` will make Steve right-click after a 10gt delay
- Add `perTick` argument for `/player` action pack commands. e.g. `/player Steve use perTick 4` will make Steve right-click 4 times per gametick
- Add OP permission check to cheaty command `/player <someone> mount anything`
- Set the maximum length of fake player's markerName to 16 to prevent kicking out other players (Works before fabric-carpet v1.4.38, fabric-carpet v1.4.38 implemented the same check)

### tick

- Set the maximum `/tick warp` duration to `Integer.MAX_VALUE` for fabric-carpet before v1.4.18 (fabric-carpet v1.4.18 removed the `/tick warp` limit)
- Add `/tick warp status` command to show the ongoing / previous tick warp status

### info

- Make `/info entity` work again
- Show tile tick events & block events in `/info block` command
- Add chunk loading state check for `/info block` command. Player with permission level < 2 cannot query block in unloaded chunk

### other

- Display the version of TIS Carpet Addition inside `/carpet` command
- Add `/spawn tracking restart` for lazy man
- Add OP permission check to command `/log <loggerName> <option> <playerName>` and `/log clean <playerName>` that controls logger subscription for other players

## Carpet Misc Tweaks

- Cancelled player action pack (triggered by `/player` command) ticking during `/tick freeze`
- Fixed carpet fake player not responding to knockback from player melee attack (https://github.com/gnembon/fabric-carpet/issues/745), which is fixed in fabric-carpet v1.4.33
- Fixed scarpet shape crash when sending shapes to non-carpet clients using alternative particles, affecting MC version 1.20.5 ~ 1.21.4. It's fixed in fabric-carpet v1.4.169 at [da5b937](https://github.com/gnembon/fabric-carpet/commit/da5b937e78c949ecea743cf607fb3d31249b48e6)
