[English](loggers.md) | **中文**

\>\>\> [返回索引](readme_cn.md)

# 监视器

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

基本上与 [掉落物监视器](#掉落物-item) 相同，只不过监视的是经验球实体


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

见规则 [微时序](rules_cn.md#微时序-microtiming) 以获得详细信息，记得使用 `/carpet microTiming true` 启用监视器功能

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

计算平均值的采样时长可通过规则 [lightQueueLoggerSamplingDuration](rules_cn.md#光照队列记录器采样时长-lightqueueloggersamplingduration) 指定，默认值为 60gt

类似 `/log mobcaps`， 你可以通过记录器选项来指定你想要记录光照队列的世界。

属性:
- 默认选项: `dynamic`
- 参考选项: `dynamic`, `overworld`, `the_nether`, `the_end`


## tickWarp

`/log tickWarp <option>`

一个用于展示当前 tick warp 进度信息的 HUD 记录器

它仅在服务器正在 tick warp 时显示出来

见指令 [/tick warp status](commands_cn.md#warp-status) 以查询更多 tick warp 的信息

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

显示玩家所处在的维度中指定实体类型当前于 [存活时间追踪器](commands_cn.md#存活时间-lifetime) 中的数据

记录器选项需要为一个合法的实体类型

属性:
- 默认选项: N/A
- 参考选项: 所有在当前追踪中可用的实体类型
