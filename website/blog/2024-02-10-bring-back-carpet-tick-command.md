---
title: Bring back carpet /tick command to vanilla 1.20.3+
slug: bring-back-carpet-tick-command
---

tl;dr, just input the following commands to configure the related rules:

```
/carpet tickCommandEnhance true
/carpet tickCommandPermission 2
/carpet tickFreezeCommandToggleable true
/carpet tickProfilerCommandsReintroduced true
/carpet tickWarpCommandAsAnAlias true
```

Requires Carpet TIS Addition v1.55.0 or higher

<!-- truncate -->

Below is a further explanation on what these rules do:

## [tickCommandEnhance](../docs/rules#tickcommandenhance)

- **Behavior before mc1.20.3 in carpet**: Carpet TIS Addition automatically adds the `/tick warp status` command
- **Behavior in vanilla after mc1.20.4**: To ensure vanilla behavior under default conditions, this rule needs to be enabled to activate the `/tick sprint status` command from Carpet TIS Addition
- **Usage scenario**: Use the `/tick warp status` command to show the progress of tick warping

## [tickCommandPermission](../docs/rules#tickcommandpermission)

- **Behavior before mc1.20.3 in carpet**: The `/tick` command requires permission level `2` by default
- **Behavior in vanilla after mc1.20.4**: The `/tick` command requires permission level `3`
- **Usage scenario**: Use the `/tick` command in scenarios like command blocks where its permission level is less than `3`

## [tickFreezeCommandToggleable](../docs/rules#tickfreezecommandtoggleable)

- **Behavior before mc1.20.3 in carpet**: The `/tick freeze` command does two things: freeze the game if it's not frozen, unfreeze the game if it's frozen
- **Behavior in vanilla after mc1.20.4**: The `/tick freeze` command does one thing only: freeze the game. The command to unfreeze the game is now split to `/tick unfreeze`
- **Usage scenario**: Like in previous versions, use only the `/tick freeze` command to enter / exit the freeze state

## [tickProfilerCommandsReintroduced](../docs/rules#tickprofilercommandsreintroduced)

- **Behavior before mc1.20.3 in carpet**: The `/tick health` and `/tick entities` commands will trigger the carpet profiler to analyze server tick costs
- **Behavior in vanilla after mc1.20.4**: The `/tick health` and `/tick entities` commands no longer exist, and `/profile health` and `/profile entities` are the only way to trigger the carpet profiler
- **Usage scenario**: Like in previous versions, continue using `/tick health` and `/tick entities` to trigger the carpet profiler

## [tickWarpCommandAsAnAlias](../docs/rules#tickwarpcommandasanalias)

- **Behavior before mc1.20.3 in carpet**: The command prefix is `/tick warp`
- **Behavior in vanilla after mc1.20.4**: The command prefix is `/tick sprint`
- **Usage scenario**: Like in previous versions, use `/tick warp` to execute related commands
