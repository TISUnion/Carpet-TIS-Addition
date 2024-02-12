---
title: 将 carpet 风格的 /tick 指令带回原版 1.20.3+
slug: bring-back-carpet-tick-command
---

长话短说，只需输入以下指令来配置相关规则：

```
/carpet tickCommandEnhance true
/carpet tickCommandPermission 2
/carpet tickFreezeCommandToggleable true
/carpet tickProfilerCommandsReintroduced true
/carpet tickWarpCommandAsAnAlias true
```

<!-- truncate -->

下面将进一步解释这些规则的作用：

## [tickCommandEnhance](../docs/rules#tick指令增强-tickcommandenhance)

- **mc1.20.3 前 carpet 表现**：Carpet TIS Addition 自动添加 `/tick warp status` 指令
- **"mc1.20.4 及以后原版表现**：为保证默认条件下的原版性，需要启用此规则，Carpet TIS Addition 提供的 `/tick sprint status` 功能才可生效
- **使用场景**：添加查看 tick warp 进度的 `/tick warp status` 指令

## [tickCommandPermission](../docs/rules#tick指令权限-tickcommandpermission)

- **mc1.20.3 前 carpet 表现**：`/tick` 指令默认需要权限等级 `2` 即可执行
- **"mc1.20.4 及以后原版表现**：`/tick` 指令需要权限等级 `3` 才可执行
- **使用场景**：在命令方块等权限等级 < `3` 的场景使用 `/tick` 指令

## [tickFreezeCommandToggleable](../docs/rules#切换式tickfreeze指令-tickfreezecommandtoggleable)

- **mc1.20.3 前 carpet 表现**：`/tick freeze` 这一条指令会做两件事情：在游戏正常运行时冻结游戏、在游戏被冻结时恢复游戏的运行
- **"mc1.20.4 及以后原版表现**：`/tick freeze` 这条指令只能让游戏进入冻结状态。让游戏恢复正常运行的指令被单独放到了 `/tick unfreeze` 指令里
- **使用场景**：像旧版本那样，只用一条 `/tick freeze` 指令就能进入或退出冻结状态

## [tickProfilerCommandsReintroduced](../docs/rules#tick性能分析指令重现-tickprofilercommandsreintroduced)

- **mc1.20.3 前 carpet 表现**：`/tick health`、`/tick entities` 这两条指令可以触发 carpet 的性能分析器，来分析服务端的运行耗时
- **"mc1.20.4 及以后原版表现**：`/tick health`、`/tick entities` 这两个指令不再存在，需使用 `/profile health`、`/profile entities` 命令来触发 carpet 的性能分析器
- **使用场景**：像旧版本那样，继续用 `/tick health`、`/tick entities` 来触发 carpet 的性能分析器

## [tickWarpCommandAsAnAlias](../docs/rules#tickwarp指令别名重现-tickwarpcommandasanalias)

- **mc1.20.3 前 carpet 表现**：指令前缀为 `/tick warp`
- **"mc1.20.4 及以后原版表现**：指令前缀为 `/tick sprint`
- **使用场景**：像旧版本那样，用 `/tick warp` 来执行相关的指令
