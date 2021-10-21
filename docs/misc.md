**English** | [中文](misc_cn.md)

\>\>\> [Back to index](readme.md)

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

# Other stuffs

## Carpet Rule Tweaks

- Make carpet rule `tntRandomRange` works without carpet rule `optimizedTNT` or with lithium mod
- Enhanced rule `creativeNoClip`: Dispenser block placement and xp orb tracking now ignore players in creativeNoClip state

## Carpet Command Tweaks

- Set the maximum `/tick warp` duration to `Integer.MAX_VALUE` for fabric-carpet before v1.4.18 (fabric-carpet v1.4.18 removed the `/tick warp` limit)
- Display the version of TIS Carpet Addition inside `/carpet` command
- Add `randomly` argument for `/player` command. e.g. `/player Steve use randomly 10 20` will make Steve right-click at dynamically varying random intervals in range \[10, 20]
- Add `/spawn tracking restart` for lazy man
- Add OP permission check to cheaty command `/player <someone> mount anything`
- Make `/info entity` work again
- Show tile tick events & block events in `/info block` command

## Misc

- Set the maximum length of fake player's markerName to 16 to prevent kicking out other players (Works before fabric-carpet v1.4.38, fabric-carpet v1.4.38 implemented the same check)
- Cancelled player action pack (triggered by `/player` command) ticking during `/tick freeze`
- Fixed carpet fake player not responding to knockback from player melee attack (https://github.com/gnembon/fabric-carpet/issues/745)
