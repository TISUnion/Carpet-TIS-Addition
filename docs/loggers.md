**English** | [中文](loggers_cn.md)

\>\>\> [Back to index](readme.md)

# Loggers

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

Check rule [microTiming](rules.md#microtiming) for detail. Remember to use `/carpet microTiming true` to enable logger functionality

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

The sampling duration can be specified with rule [lightQueueLoggerSamplingDuration](rules.md#lightqueueloggersamplingduration), default 60gt

Specify the logging option to select the world you want log its light queue, like `/log mobcaps`

Attributes:
- Default option: `dynamic`
- Suggested options: `dynamic`, `overworld`, `the_nether`, `the_end`


## tickWarp

`/log tickWarp <option>`

A HUD logger to display to progress of current tick warping

It only shows up when the server is tick warping

See [/tick warp status](commands.md#warp-status) command for displaying more details of tick warp

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

Displays the current lifetime statistic of specific entity type from the [LifeTime Tracker](commands.md#lifetime) in the dimension the player is in

The logging options is required to be an available entity type

Attributes:
- Default option: N/A
- Suggested options: All available entity types in current's lifetime tracking
