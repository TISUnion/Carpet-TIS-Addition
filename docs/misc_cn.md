[English](misc.md) | **中文**

\>\>\> [返回索引](readme_cn.md)

# Scarpet

于 Carpet TIS Addition 1.16.4+ 中可用

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

规则 [微时序](rules_cn.md#微时序-microTiming) 需要被设置为 true 来使这些事件能被触发

-----------

# 其他

## Carpet 相关规则修改

- 使 carpet 规则 `tntRandomRange` 能在不开启 `optimizedTNT` 规则或存在 lithium mod 时正常工作
- 增强规则 `creativeNoClip`：发射器放置方块、经验球追踪时无视处于 creativeNoClip 状态下的玩家

## Carpet 相关指令修改

- 将 `/tick warp` 最大时长限制调整为 `Integer.MAX_VALUE`，对 1.4.18 前的 fabric-carpet 有效（fabric-carpet 1.4.18 移除了 `/tick warp` 限制）
- 在 `/carpet` 指令中显示 Carpet TIS Addition 的版本信息
- 为 `/player` 指令添加 `randomly` 参数。如 `/player Steve use randomly 10 20` 将使 Steve 以动态变化的随机间隔点击右键，间隔区间为 \[10, 20]
- 添加懒人最爱的 `/spawn tracking restart`
- 为有作弊嫌疑的 `/player <someone> mount anything` 指令添加 OP 权限检查
- 使指令 `/info entity` 能正常地运行
- 在指令 `/info block` 中显示目标位置的计划刻事件及方块事件

## 杂项

- 将假人的名字长度限制调整为 16 以防止真实玩家被踢出，对 1.4.38 前的 fabric-carpet 有效（fabric-carpet 1.4.38 也实现了相关的约束）
- 取消玩家动作包（由 `/player` 指令触发的 PlayerActionPack）在 `/tick freeze` 时的更新
- 修复地毯假人不响应玩家近战攻击的击退的 bug（https://github.com/gnembon/fabric-carpet/issues/745)
