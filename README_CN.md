# Carpet TIS Addition

[![License](https://img.shields.io/github/license/TISUnion/Carpet-TIS-Addition.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![Issues](https://img.shields.io/github/issues/TISUnion/Carpet-TIS-Addition.svg)](https://github.com/TISUnion/Carpet-TIS-Addition/issues)
[![MC Versions](http://cf.way2muchnoise.eu/versions/For%20MC_carpet-tis-addition_all.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-tis-addition)
[![CurseForge](http://cf.way2muchnoise.eu/full_carpet-tis-addition_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-tis-addition)

[English](README.md) | **中文**

这是一个 [Carpet mod](https://github.com/gnembon/fabric-carpet) (fabric-carpet) 的扩展 mod，包含了不少~~NotVanilla的~~有意思的功能以及特性

在默认配置条件下，本模组不会改变任何原版机制

跟同 Minecraft 版本的 carpet mod 一起使用即可。尽可能地使用较新的 carpet mod

# 索引

## [规则](#规则列表)

- [禁用反刷屏监测](#禁用反刷屏监测-antiSpamDisabled)
- [方块事件广播范围](#方块事件广播范围-blockEventPacketRange)
- [方块放置碰撞检测](#方块放置碰撞检测-blockPlacementIgnoreEntity)
- [区块更新数据包阈值](#区块更新数据包阈值-chunkUpdatePacketThreshold)
- [区块刻速度](#区块刻速度-chunkTickSpeed)
- [存活时间追踪器](#存活时间追踪器-commandLifeTime)
- [袭击追踪器](#袭击追踪器-commandRaid)
- [刷新命令开关](#刷新命令开关-commandRefresh)
- [创造玩家强制打开容器](#创造玩家强制打开容器-creativeOpenContainerForcibly)
- [发射器不消耗物品](#发射器不消耗物品-dispenserNoItemCost)
- [发射器发射龙息](#发射器发射龙息-dispensersFireDragonBreath)
- [enchant指令约束移除](#enchant指令约束移除-enchantCommandNoRestriction)
- [实体速度丢失](#实体速度丢失-entityMomentumLoss)
- [爆炸数据包广播范围](#爆炸数据包广播范围-explosionPacketRange)
- [假人名称前缀](#假人名称前缀-fakePlayerNamePrefix)
- [假人名称后缀](#假人名称后缀-fakePlayerNameSuffix)
- [禁用流体破坏](#禁用流体破坏-fluidDestructionDisabled)
- [漏斗计数器无限速度](#漏斗计数器无限速度-hopperCountersUnlimitedSpeed)
- [漏斗不消耗物品](#漏斗不消耗物品-hopperNoItemCost)
- [hud监视器更新间隔](#hud监视器更新间隔-HUDLoggerUpdateInterval)
- [瞬时命令方块](#瞬时命令方块-instantCommandBlock)
- [保持弱加载区块的怪物](#保持弱加载区块的怪物-keepMobInLazyChunks)
- [光照队列记录器采样时长](#光照队列记录器采样时长-lightQueueLoggerSamplingDuration)
- [光照更新](#光照更新-lightUpdates)
- [微时序](#微时序-microTiming)
- [微时序染料记号](#微时序染料记号-microTimingDyeMarker)
- [微时序目标](#微时序目标-microTimingTarget)
- [微时序游戏刻划分](#微时序游戏刻划分-microTimingTickDivision)
- [op玩家不准作弊](#op玩家不准作弊-opPlayerNoCheat)
- [优化高速实体移动](#优化高速实体移动-optimizedFastEntityMovement)
- [优化硬碰撞箱实体碰撞](#优化硬碰撞箱实体碰撞-optimizedHardHitBoxEntityCollision)
- [TNT优化高优先级](#TNT优化高优先级-optimizedTNTHighPriority)
- [POI更新开关](#POI更新开关-poiUpdates)
- [刷铁轨机修复](#刷铁轨机修复-railDupingFix)
- [红石粉随机更新顺序](#红石粉随机更新顺序-redstoneDustRandomUpdateOrder)
- [可再生龙蛋](#可再生龙蛋-renewableDragonEgg)
- [可再生龙首](#可再生龙首-renewableDragonHead)
- [可再生鞘翅](#可再生鞘翅-renewableElytra)
- [中继器延迟折半](#中继器延迟折半-repeaterHalfDelay)
- [刷沙机修复](#刷沙机修复-sandDupingFix)
- [结构方块不保留流体](#结构方块不保留流体-structureBlockDoNotPreserveFluid)
- [结构方块范围限制](#结构方块范围限制-structureBlockLimit)
- [同步光照线程](#同步光照线程-synchronizedLightThread)
- [计划刻上限](#计划刻上限-tileTickLimit)
- [TNT复制修复](#TNT复制修复-tntDupingFix)
- [TNT引信时长](#TNT引信时长-tntFuseDuration)
- [TNT忽略红石信号](#TNT忽略红石信号-tntIgnoreRedstoneSignal)
- [工具化TNT](#工具化TNT-tooledTNT)
- [完全没有方块更新](#完全没有方块更新-totallyNoBlockUpdate)
- [禁用海龟蛋被践踏](#禁用海龟蛋被践踏-turtleEggTrampledDisabled)
- [可视化投掷物记录器](#可视化投掷物记录器-visualizeProjectileLoggerEnabled)
- [经验球追踪距离](#经验球追踪距离-xpTrackingDistance)

## [移植的规则](#移植的规则列表)

- [光照引擎最大任务组数](#光照引擎最大任务组数-lightEngineMaxBatchSize)
- [结构方块轮廓距离](#结构方块轮廓距离-structureBlockOutlineDistance)

## [监视器](#监视器列表)

- [加载票](#加载票-ticket)
- [内存](#内存-memory)
- [掉落物](#掉落物-item)
- [经验球](#经验球-xporb)
- [袭击](#袭击-raid)
- [微时序](#微时序-microTiming-1)
- [伤害](#伤害-damage)
- [命令方块](#命令方块-commandBlock)
- [光照队列](#光照队列-lightQueue)
- [tickWarp](#tickWarp-tickWarp)
- [海龟蛋](#海龟蛋-turtleEgg)
- [存活时间](#存活时间-lifeTime)

## [指令](#指令列表)

- [袭击](#袭击-raid-1)
- [信息](#信息-info)
- [存活时间](#存活时间-lifetime-1)
- [tick](#tick)
- [刷新](#刷新-refresh)

## [Scarpet](#scarpet-1)

### [Functions](#functions-1)

- [`register_block(pos)`](#register_blockpos)
- [`unregister_block(pos)`](#unregister_blockpos)
- [`registered_blocks()`](#registered_blocks)
- [`is_registered(pos)`](#is_registeredpos)
  
### [Events](#events)

- [`__on_microtiming_event(type, pos, dimension)`](#__on_microtiming_eventtype-pos-dimension)


## 其他

- [其他](#其他)
- [开发](#开发)



-----------

# 监视器列表

## 加载票 (ticket)

`/log ticket <监视类型>`

记录加载票的添加以及移除

用 csv 格式，例如 `portal,dragon` 来监视多种类型的加载票

可用的选项分隔符: `,`、`.` 与 ` ` (`.` 是 1.14.4 版本的唯一选择)

**警告:** 监视 `unknown` 加载票的话可能会导致你被刷屏

属性:
- 默认选项: `portal`
- 参考选项: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## 内存 (memory)

`/log memory`

于 tab 栏中显示服务端当前消耗的内存以及占用的最大内存

属性:
- 默认选项: N/A
- 参考选项: N/A


## 掉落物 (item)

`/log item <事件>`

记录某些事件在掉落物实体身上的发生，如物品闲置五分钟后自然消失

可用的事件类型:
- `create`: 当物品于世界中因任何原因被创建。信息中包含堆栈追踪信息
- `die`: 当物品死亡
- `despawn`: 当物品自然消失

用 csv 格式，例如 `despawn,die` 来监视多种事件

可用的选项分隔符: `,`、`.` 与 ` ` (`.` 是 1.14.4 版本的唯一选择)

属性:
- 默认选项: `despawn`
- 参考选项: `despawn`, `die`, `despawn,die`


## 经验球 (xporb)

`/log xporb <events>`

基本上与 [掉落物监视器](https://github.com/TISUnion/Carpet-TIS-Addition/blob/master/README_CN.md#掉落物-item) 相同，只不过监视的是经验球实体


## 袭击 (raid)

`/log raid`

记录以下袭击相关的事件：

- 袭击被创建
- 袭击被移除
- 袭击的不祥之兆等级被提升
- 袭击的中心点被移动

属性:
- 默认选项: N/A
- 参考选项: N/A


## 微时序 (microTiming)

`/log microTiming <类型>`

记录元件的微时序，元件所在区块的加载票等级需至少为弱加载 (加载票等级 32)

见规则 [微时序](#微时序-microTiming) 以获得详细信息，记得使用 `/carpet microTiming true` 启用监视器功能

可用的类型选项: 
- `all`: 默认值，输出所有事件
- `merged`: 输出所有事件并合并连续相同的事件
- `unique`: 输出所有每游戏刻中第一次出现的事件

属性:
- 默认选项: `merged`
- 参考选项: `all`, `merged` `unique`


## 伤害 (damage)

`/log damage <目标>`

记录生物的受伤，以及伤害结算的具体流程

可选的记录目标:
- `all`: 记录所有生物
- `players`: 记录有玩家参与的生物伤害
- `me`: 记录与自己相关的伤害

属性:
- 默认选项: `all`
- 参考选项: `all`, `players`, `me`


## 命令方块 (commandBlock)

`/log commandBlock <option>`

记录命令方块或命令方块矿车的指令执行

有助于找到烦人的不知所踪的命令方块在何处

当使用默认的 `throttled` 选项，每个命令方块最高以每 3 秒一次的频率记录其执行

属性:
- 默认选项: `throttled`
- 参考选项: `throttled`, `all`


## 光照队列 (lightQueue)

`/log lightQueue`

一个用于调试光照抑制的 HUD 记录器。它将显示光照队列的如下信息：

- 当前光照队列的大小。用符号 `S` 表示
- 如果光照抑制器被关闭，光照抑制将会持续的预估时长。用符号 `T` 表示
- 光照更新任务平均每游戏刻入队速度。用符号 `I` 表示
- 光照更新任务平均每游戏刻执行速度。用符号 `O` 表示
- 光照更新任务平均每游戏刻积累速度

计算平均值的采样时长可通过规则 [lightQueueLoggerSamplingDuration](#lightQueueLoggerSamplingDuration) 指定，默认值为 60gt

类似 `/log mobcaps`， 你可以通过记录器选项来指定你想要记录光照队列的世界。

属性:
- 默认选项: `dynamic`
- 参考选项: `dynamic`, `overworld`, `the_nether`, `the_end`


## tickWarp (tickWarp)

`/log tickWarp <option>`

一个用于展示当前 tick warp 进度信息的 HUD 记录器

它仅在服务器正在 tick warp 时显示出来

见指令 [/tick warp status](#warp-status) 以查询更多 tick warp 的信息

属性:
- 默认选项: `bar`
- 参考选项: `bar`, `value`


## 海龟蛋 (turtleEgg)

`/log turtleEgg`

在海龟蛋被踩碎时输出记录信息

属性:
- 默认选项: N/A
- 参考选项: N/A


## 存活时间 (lifeTime)

`/log lifeTime <实体类型>`

一个用于 HUD 记录器

显示玩家所处在的维度中指定实体类型当前于 [存活时间追踪器](#存活时间-lifetime-1) 中的数据

记录器选项需要为一个合法的实体类型

属性:
- 默认选项: N/A
- 参考选项: 所有在当前追踪中可用的实体类型


# 指令列表

## 袭击 (raid)

### 列表 (list)

`/raid list [<full>]`

列出目前所有袭击的信息

### 追踪 (tracking)

`/raid tracking [<start|stop|restart|realtime>]`

开启一个袭击追踪器以收集并统计进行中的袭击的状态信息


## 信息 (info)

### world ticking_order

`/info world ticking_order`

显示游戏中所有维度运算的顺序


## 存活时间 (lifetime)

一个追踪所有新生成生物的存活时间及生成/移除原因的记录器

该记录器主要用于测试各种刷怪塔，用于追踪从生物开始影响怪物容量上限，至移出怪物容量上限的这个过程。该记录器的生成追踪并未覆盖所有的生物生成原因

对于一个生物，除了其因各种原因被移出世界，当其**首次**变为不计入怪物容量上限时，例如被命名、捡起物品，也会被标记移除。如果一个生物生成时已不在怪物容量上限中，那它将不会被追踪

作为一项附加功能，该跟踪器还跟踪由方块或生物掉落的物品和经验球的存活时间。注意该追踪器并未追踪所有可能的掉落物或经验球生成，推荐先做好相关测试

给指令添加 `realtime` 后缀可将速率结果从基于游戏时间转换为基于现实时间

### tracking

`/lifetime tracking [<start|stop|restart>]`

控制存活时间追踪器

追踪的实体类型：
- 所有种类的生物 (MobEntity)
- 掉落物实体
- 经验球实体

追踪的实体生成原因
- 自然刷新
- 地狱门僵尸猪人生成
- 因传送门跨维度到来
- 被物品生成（怪物蛋等）
- 史莱姆分裂 (对于史莱姆以及岩浆怪)
- 僵尸增援
- 刷怪笼
- `/summon` 指令
- 生物掉落（仅掉落物及经验球）
- 方块掉落（仅掉落物）

注意，只有被追踪了生成的实体会被统计

追踪的实体移除原因
- 消失，包括立即消失、随机消失、因游戏难度的消失以及超时消失
- 受伤致死
- 获取持久（persistent）标签。注意此时实体并未移除出世界
- 骑上载具（1.16+）。注意此时实体并未移除出世界
- 因传送门跨维度离开
- 实体合并（仅掉落物及经验球）
- 被玩家捡起（仅掉落物及经验球）
- 被漏斗或漏斗矿车收集（仅掉落物）
- 进入虚空
- 其他（其余未被统计的原因）

存活时间的定义为：**实体生成时刻与移除时刻间的经过的自然生物刷新阶段的数量**，也就是刷怪时被该实体影响了怪物容量上限的游戏刻数。技术上来讲，刷怪阶段数量计数器的注入位置是世界重新计算怪物容量上限前

统计信息以数量所占比例排序 

### <实体类型>

`/lifetime <实体类型> [<life_time|removal|spawning>]`

显示指定实体类型的详细统计信息。你可以指定输出哪一部分统计信息

比如 `/lifetime creeper` 将详细地显示爬行者的统计信息，`/lifetime creeper removal` 则只详细显示爬行者的移除统计信息 

### filter

```
/lifetime filter <实体类型> set <实体选择器>
/lifetime filter <实体类型> clear`
```

为指定实体类型设置实体筛选器。在 `<实体类型>` 处键入 `global` 以设置全局筛选器

实体需要被对应的筛选器所接受才可被存活时间追踪器进行追踪

使用 `@e` 类型的 Minecraft 实体选择器来输入实体筛选器，如：`@e[distance=..100,nbt={Item:{id:"minecraft:oak_sapling"}}]`

使用 `/lifetime filter` 来显示激活的实体筛选器


## tick

### warp status

`/tick warp status`

显示当前 tick warp 的状态信息，如启动者、估计剩余时间等

见 [tickWarp 记录器](#tickWarp) 以了解更方便地查询信息的方法


## 刷新 (refresh)

### inventory

`/refresh inventory`: 刷新你的物品栏

`/refresh inventory <players>`: 刷新指定玩家的物品栏。需要权限等级 2

### chunk

`/refresh chunk`: 同 `/refresh chunk current`

`/refresh chunk current`: 刷新你所在的区块

`/refresh chunk all`: 刷新视距内的所有区块

`/refresh chunk inrange <chebyshevDistance>`: 刷新给定切比雪夫距离内的所有区块

`/refresh chunk at <chunkX> <chunkZ>`: 刷新指定位置的区块

所有区块刷新操作均仅影响视距之内的区块

受到数据包压缩操作影响，区块批量刷新操作会对服务端的网络线程造成卡顿，因此该指令包含一个输入限速器来防止数据包过多地堆积


-----------

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

规则 [微时序](#微时序-microTiming) 需要被设置为 true 来使这些事件能被触发

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
- 修复地毯假人不响应玩家近战攻击的击退的 bug（https://github.com/gnembon/fabric-carpet/issues/745）

-----------

# 开发

当前主开发分支：**1.15.2**

目前维护的分支：
- 1.14.4，对应 Minecraft 1.14.4
- 1.15.2，对应 Minecraft 1.15.2
- 1.16.5，对应 Minecraft 1.16.4 至 1.16.5
- 1.17.x，对应 Minecraft 1.17.1
- 1.18-exp，对应 Minecraft 1.18 实验性快照

目前存档的分支：
- archive/1.16，对应 Minecraft 1.16 至 1.16.1
- archive/1.16.3，对应 Minecraft 1.16.2 至 1.16.3
- archive/1.17，对应 Minecraft 1.17

对于通用的新特性，在 1.15.2 分支中实现，再将其合并至其他分支

分支合并顺序：
- 1.15.2 -> 1.14.4
- 1.15.2 -> 1.16.5 -> 1.17.x -> 1.18-exp
- 1.15.2 -> master (发布 release 时)

对于版本专用的修复/补丁，在对应的分支上操作即可

master 分支通常仅接受文档更新

除非必要，尽量不要影响版本兼容性

英文文档与中文文档是逐行对应的
