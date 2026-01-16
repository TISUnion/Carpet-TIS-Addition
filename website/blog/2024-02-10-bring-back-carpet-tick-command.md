---
title: Bring back carpet /tick command to vanilla 1.20.3+
slug: bring-back-carpet-tick-command
---

tl;dr, just enter the following command to enable the [tickCommandCarpetfied](/docs/rules#tickcommandcarpetfied) rule:

```
/carpet tickCommandCarpetfied true
```

or

```
/carpet setDefault tickCommandCarpetfied true
```

Requires Carpet TIS Addition v1.56.0 or higher to enable this rule

For Carpet TIS Addition v1.55.0, you can manually configure the rules below to achieve the same result

<!-- truncate -->

## [tickCommandEnhance](/docs/rules#tickcommandenhance)

```
/carpet tickCommandEnhance true
```

- **Behavior before mc1.20.3 in carpet**: Carpet TIS Addition automatically adds the `/tick warp status` command
- **Behavior in vanilla after mc1.20.4**: To ensure vanilla behavior under default conditions, this rule needs to be enabled to activate the `/tick sprint status` command from Carpet TIS Addition
- **Usage scenario**: Use the `/tick warp status` command to show the progress of tick warping

## [tickCommandPermission](/docs/rules#tickcommandpermission)

```
/carpet tickCommandPermission 2
```

- **Behavior before mc1.20.3 in carpet**: The `/tick` command requires permission level `2` by default
- **Behavior in vanilla after mc1.20.4**: The `/tick` command requires permission level `3`
- **Usage scenario**: Use the `/tick` command in scenarios like command blocks where its permission level is less than `3`

## [tickFreezeCommandToggleable](/docs/rules#tickfreezecommandtoggleable)

```
/carpet tickFreezeCommandToggleable true
```

- **Behavior before mc1.20.3 in carpet**: The `/tick freeze` command does two things: freeze the game if it's not frozen, unfreeze the game if it's frozen
- **Behavior in vanilla after mc1.20.4**: The `/tick freeze` command does one thing only: freeze the game. The command to unfreeze the game is now split to `/tick unfreeze`
- **Usage scenario**: Like in previous versions, use only the `/tick freeze` command to enter / exit the freeze state

## [tickFreezeDeepCommand](/docs/rules#tickfreezedeepcommand)

Requires Carpet TIS Addition v1.75.0 or higher

```
/carpet tickFreezeDeepCommand true
```

- **Behavior before mc1.20.3 in carpet**：`/tick freeze deep` further freezes the server beyond the regular `/tick freeze`, preventing ticket expiration ticking
- **Behavior in vanilla after mc1.20.4**：`/tick freeze deep` command does not exist
- **Usage scenario**：Like in previous versions, continue using the `/tick freeze deep` command for deep freezing

## [tickProfilerCommandsReintroduced](/docs/rules#tickprofilercommandsreintroduced)

```
/carpet tickProfilerCommandsReintroduced true
```

- **Behavior before mc1.20.3 in carpet**: The `/tick health` and `/tick entities` commands will trigger the carpet profiler to analyze server tick costs
- **Behavior in vanilla after mc1.20.4**: The `/tick health` and `/tick entities` commands no longer exist, and `/profile health` and `/profile entities` are the only way to trigger the carpet profiler
- **Usage scenario**: Like in previous versions, continue using `/tick health` and `/tick entities` to trigger the carpet profiler

## [tickWarpCommandAsAnAlias](/docs/rules#tickwarpcommandasanalias)

```
/carpet tickWarpCommandAsAnAlias true
```

- **Behavior before mc1.20.3 in carpet**: The command prefix is `/tick warp`
- **Behavior in vanilla after mc1.20.4**: The command prefix is `/tick sprint`
- **Usage scenario**: Like in previous versions, use `/tick warp` to execute related commands
