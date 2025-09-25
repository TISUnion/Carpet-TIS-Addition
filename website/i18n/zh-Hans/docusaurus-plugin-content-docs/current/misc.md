---
sidebar_position: 4
---

# 杂项

# Scarpet

**于 Minecraft 1.16.4+ 中可用**

## 函数

### `register_block(pos)`

注册一个可以被 scarpet 追踪事件的方块位置。它将会被添加至一个全局的追踪器中（你可以使用 `is_registered()` 来查询）

位于追踪器所追踪的方块将会在事件发生时触发 scarpet 事件 `__on_microtiming_event`。该事件独立于微时序记录器的开关触发

若该方块位置在之前并不位于列表中，返回 `true`；若该方块位置曾位于列表中，返回 `false`

### `unregister_block(pos)`

从追踪的方块位置列表中移除给定的方块位置

若该方块位置在之前曾位于列表中，返回 `true`；若该方块位置并不存在于列表中，返回 `false`

### `registered_blocks()`

返回被 scarpet 追踪事件的方块位置列表

### `is_registered(pos)`

判断该位置是否位于追踪事件的列表中，返回 `true` 或 `false`

## 事件

### `__on_microtiming_event(type, pos, dimension)`

该事件在被追踪的方块位置发生任何可被微时序记录器记录的事件时触发

`type` 表示着时间的类型，可为：

- `'detected_block_update'`
- `'block_state_changed'`
- `'executed_block_event'`
- `'executed_tile_tick'`
- `'emitted_block_update'`
- `'emitted_block_update_redstone_dust'`
- `'scheduled_block_event'`
- `'scheduled_tile_tick'`

规则 [微时序](rules.md#微时序-microtiming) 需要被设置为 true 来使这些事件能被触发

-----------

# 其他

## Carpet 相关规则修改

- 使 carpet 规则 `tntRandomRange` 能在不开启 `optimizedTNT` 规则或存在 lithium mod 时正常工作
- 增强规则 `creativeNoClip`，在以下动作中忽略处于 creativeNoClip 状态下的玩家
  - 发射器放置方块
  - 经验球追踪玩家
  - 拌线、压力板检测实体
  - 方块变化导致的实体移动，如踩坏耕地
  - 计算实体的碰撞箱。用途例子：实体（尤其是矿车和船）移动时跟其他实体的碰撞、船的放置等
  - (< mc1.16) 末地折跃门传送实体

## Carpet 相关指令修改

### player

- 为 `/player` 指令添加 `randomly` 子命令，使玩家以动态变化的随机间隔执行给定操作
  - 支持的随机数发生器：均匀分布、泊松分布、正态分布、三角分布
  - 支持带 `--simulate` 参数来测试运行
  - 使用 `/player someone someaction randomly` 来获取更多帮助
- 为 `/player` 指令添加 `rejoin` 参数。类似 `/player spawn`，但该指令会保留假人之前下线时所在的位置和朝向
- 为 `/player` 动作包相关指令添加 `after` 参数。如 `/player Steve use after 10` 将使 Steve 在 10gt 的延迟后点击右键
- 为 `/player` 动作包相关指令添加 `perTick` 参数。如 `/player Steve use perTick 4` 将使 Steve 每游戏刻点击右键 4 次。另见：规则 [commandPlayerActionPerTick](rules.md#玩家动作包pertick模式开关-commandplayeractionpertick)
- 为有作弊嫌疑的 `/player <someone> mount anything` 指令添加 OP 权限检查
- 将假人的名字长度限制调整为 16 以防止真实玩家被踢出，对 v1.4.38 前的 fabric-carpet 有效（fabric-carpet v1.4.38 也实现了相关的约束）

### tick

- 将 `/tick warp` 最大时长限制调整为 `Integer.MAX_VALUE`，对 1.4.18 前的 fabric-carpet 有效（fabric-carpet 1.4.18 移除了 `/tick warp` 限制）
- 增加 `/tick warp status` 以查看正在进行中/上次的 tick warp 的状态

### info

- 使指令 `/info entity` 能正常地运行
- 在指令 `/info block` 中显示目标位置的计划刻事件及方块事件
- 为指令 `/info block` 添加区块加载状态检查。权限等级小于 2 的玩家不可用查询未加载区块中的方块信息

### 其他

- 在 `/carpet` 指令中显示 Carpet TIS Addition 的版本信息
- 添加懒人最爱的 `/spawn tracking restart`
- 为操控其他玩家的记录器订阅的 `/log <loggerName> <option> <playerName>` 和 `/log clean <playerName>` 指令添加 OP 权限检查

## Carpet 相关其他修改

- 取消玩家动作包（由 `/player` 指令触发的 PlayerActionPack）在 `/tick freeze` 时的更新
- 修复地毯假人不响应玩家近战攻击的击退的 bug (https://github.com/gnembon/fabric-carpet/issues/745)。此问题已在 fabric-carpet v1.4.33 中得以修复
- 修复了在使用粒子效果这一替代方案来将 scarpet shape 发送给非 carpet 客户端时，服务端发生的 `unknown_particle` 崩溃，影响 MC 1.20.5 ~ 1.21.4。此问题已在 fabric-carpet v1.4.169 中的提交 [da5b937](https://github.com/gnembon/fabric-carpet/commit/da5b937e78c949ecea743cf607fb3d31249b48e6) 中修复
