---
sidebar_position: 2
---

# 记录器

## 命令方块 (commandBlock)

`/log commandBlock <option>`

记录命令方块或命令方块矿车的指令执行

有助于找到烦人的不知所踪的命令方块在何处

当使用默认的 `throttled` 选项，每个命令方块最高以每 3 秒一次的频率记录其执行

属性:
- 默认选项: `throttled`
- 参考选项: `throttled`, `all`


## 伤害 (damage)

`/log damage <选择器>`

记录生物的受伤，以及伤害结算的具体流程

`<选择器>` 选项由 1 或 2 个对象定义串，
及一个可选的 `->` 或 `<->` 方向指示符构成

选择器结构例子，其中 `A` 和 `B` 分别代表两个对象定义串:

- `A`: 伤害来源或目标是 `A`
- `->A`: 伤害来源是 `A`
- `A->`: 伤害目标是 `A`
- `A->B`: 伤害来源是 `A`，伤害目标是 `B` (`A` 对 `B` 造成了一些伤害)
- `A<->B`: 伤害来源是 `A` 且伤害目标是 `B`，或伤害来源是 `B` 且伤害目标是 `A`

要定义一个对象，你可以选择下述任意一种语法：

- 硬编码串:
  - *空*, `*` or `all`: 匹配所有情况
  - `me`: 匹配记录器订阅者自身
  - `players`: 匹配玩家实体
- 实体类型 (matches given type of entities):
  - `cat`: 匹配所有的猫
  - `minecraft:cat`: 同上
  - `entity_type/cat`: 同上
- 伤害名（仅可匹配伤害来源）:
  - `hotFloor`: 匹配那些伤害的消息 ID 为 `hotFloor` 的伤害源 (即伤害类型为 `minecraft:hot_floor`)
  - `damage_name/hotFloor`: 同上
- 伤害类型 (在 mc1.19.4+ 中可用，仅可匹配伤害来源):
  - `hot_floor`: 匹配那些伤害类型为 `minecraft:hot_floor` 的伤害源
  - `minecraft:hot_floor`: 同上
  - `damage_type/hot_floor`: 同上
- 实体选择器:
  - `@e[distance=..20]`: 订阅者 10m 范围内实体
  - `Steve`: 匹配名字为 `Steve` 的玩家
  - `some-uuid-string`: 匹配 UUID 为给定串的实体

`<选择器>` 的一些例子:

- `->me`: 对订阅者自己造成的伤害
- `->creeper`: 对爬行者造成的伤害
- `vex->`: 由恼鬼造成的伤害
- `zombie`: 由/对僵尸造成的伤害
- `minecraft:zombie`: 同 `zombie`
- `me->zombie`: 由订阅者对僵尸造成的伤害
- `me<->zombie`: 订阅者与僵尸之间的伤害
- `hotFloor->zombie`: 僵尸被岩浆块烫脚
- `->@e[distance=..10]`: 对订阅者 10m 范围内实体造成的伤害

属性:
- 默认选项: `all`
- 参考选项: `all`, `players`, `me`, `->creeper`, `vex->`, `me->zombie`, `hotFloor->zombie`, `Steve`, `@e[distance=..10]`


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


## 存活时间 (lifetime)

`/log lifetime <实体类型>`

一个 HUD 记录器

显示玩家所处在的维度中指定实体类型当前于 [存活时间追踪器](commands.md#存活时间-lifetime) 中的数据

记录器选项需要为一个合法的实体类型

属性:
- 默认选项: N/A
- 参考选项: 所有在当前追踪中可用的实体类型


## 光照队列 (lightQueue)

`/log lightQueue`

一个用于调试光照抑制的 HUD 记录器。它将显示光照队列的如下信息：

- 当前光照队列的大小。用符号 `S` 表示
- 如果光照抑制器被关闭，光照抑制将会持续的预估时长。用符号 `T` 表示
- 光照更新任务平均每游戏刻入队速度。用符号 `I` 表示
- 光照更新任务平均每游戏刻执行速度。用符号 `O` 表示
- 光照更新任务平均每游戏刻积累速度

计算平均值的采样时长可通过规则 [lightQueueLoggerSamplingDuration](rules.md#光照队列记录器采样时长-lightqueueloggersamplingduration) 指定，默认值为 60gt

类似 `/log mobcaps`， 你可以通过记录器选项来指定你想要记录光照队列的世界。

属性:
- 默认选项: `dynamic`
- 参考选项: `dynamic`, `overworld`, `the_nether`, `the_end`


## 内存 (memory)

`/log memory`

于 tab 栏中显示服务端的内存使用信息

格式：`已使用的内存` / `从操作系统分配的内存` | `最大可用的内存`

属性:
- 默认选项: N/A
- 参考选项: N/A


## 微时序 (microTiming)

`/log microTiming <类型>`

记录元件的微时序，元件所在区块的加载票等级需至少为弱加载 (加载票等级 32)

见规则 [微时序](rules.md#微时序-microtiming) 以获得详细信息，记得使用 `/carpet microTiming true` 启用记录器功能

可用的类型选项:
- `all`: 默认值，输出所有事件
- `merged`: 输出所有事件并合并连续相同的事件
- `unique`: 输出所有每游戏刻中第一次出现的事件

属性:
- 默认选项: `merged`
- 参考选项: `all`, `merged` `unique`


## 局部怪物容量 (mobcapsLocal)

**于 Minecraft 1.18.2+ 中可用**

`/log mobcapsLocal [<玩家名>]`

一个 HUD 记录器

类似 carpet 的 mobcaps 记录器，不过它显示的数据为指定玩家的局部怪物容量

若未指定玩家，则它将显示订阅者的局部怪物容量

属性:
- 默认选项: N/A
- 参考选项: 所有在线玩家的玩家名


## 移动 (movement)

`/log movement <目标>`

开关：规则 [移动记录器](rules.md#移动记录器-loggermovement)

记录实体的移动尝试，以及计算最终移动矢量的具体流程

`<目标>` 是一个实体选择器。请确保你仅选中了那些你关注的实体，否则刷屏预警（`@` 选择器需要权限等级 2）

此外，你可以在 `<目标>` 字符串中添加一个 `non_zero:` 前缀，从而让那些最终移动矢量为 0 的记录被筛去并不显示

属性:
- 默认选项: `non_zero:@a[distance=..10]`
- 参考选项: `non_zero:@a[distance=..10]`, `@s`, `non_zero:@e[type=creeper,distance=..5]`, `Steve`


## 幻翼 (phantom)

`/log phantom <选项列表>`

当选项包含 `spawning` 时，它将在某名玩家召唤了一波幻翼时通知你

当选项包含 `reminder` 时，它将在你已经过了 45 分钟 / 60 分钟没睡觉时通知你

属性:
- 默认选项: `spawning`
- 参考选项: `spawning`, `reminder`, `spawning,reminder`


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


## 供给计数器 (scounter)

`/log scounter <颜色>`

这是一个 HUD 记录器

类似 carpet 的漏斗计数器对应的 `counter` 记录器，该记录器用于展示使用规则 [hopperNoItemCost](rules.md#漏斗不消耗物品-hoppernoitemcost) 创建的漏斗无限物品供给器所输出的物品数量

属性:
- 默认选项: N/A
- 参考选项: 所有染料颜色名


## 加载票 (ticket)

`/log ticket <监视类型>`

记录加载票的添加以及移除

用 csv 格式，例如 `portal,dragon` 来监视多种类型的加载票

可用的选项分隔符: `,`、`.` 与 ` ` (`.` 是 1.14.4 版本的唯一选择)

**警告:** 监视 `unknown` 加载票的话可能会导致你被刷屏

属性:
- 默认选项: `portal`
- 参考选项: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## tickWarp

`/log tickWarp <展示类型>`

一个用于展示当前 tick warp 进度信息的 HUD 记录器

它仅在服务器正在 tick warp 时显示出来

见指令 [/tick warp status](commands.md#warp-status) 以查询更多 tick warp 的信息

属性:
- 默认选项: `bar`
- 参考选项: `bar`, `value`


## 海龟蛋 (turtleEgg)

`/log turtleEgg`

在海龟蛋被踩碎时输出记录信息

属性:
- 默认选项: N/A
- 参考选项: N/A


## 流浪商人 (wanderingTrader)

`/log wanderingTrader`

在玩家召唤出了（事实上基于随机玩家的特殊刷怪）流浪商人时，输出记录信息


## 经验计数器 (xcounter)

`/log xcounter <颜色>`

这是一个 HUD 记录器

类似 carpet 的漏斗计数器对应的 `counter` 记录器，该记录器用于展示使用规则 [hopperXpCounters](rules.md#漏斗经验计数器-hopperxpcounters) 创建的漏斗经验计数器的统计结果

属性:
- 默认选项: N/A
- 参考选项: 所有染料颜色名



## 经验球 (xporb)

`/log xporb <事件>`

基本上与 [掉落物记录器](#掉落物-item) 相同，只不过监视的是经验球实体
