[English](loggers.md) | **中文**

\>\>\> [返回索引](readme_cn.md)

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

`/log damage <目标>`

记录生物的受伤，以及伤害结算的具体流程

可选的记录目标:
- `all`: 记录所有生物
- `players`: 记录有玩家参与的生物伤害
- `me`: 记录与订阅者自己相关的伤害
- `<实体类型>`：记录与指定实体类型相关的伤害。例如 `creeper`
- `<实体选择器>`：记录与符合实体选择器所述实体相关的伤害。例如 `Steve`（玩家名）、`@e[distance=..10]`（`@` 选择器，需要权限等级 2）

此外，
- 如果在目标字符串前面附加一个 `<-`，或在目标字符串后面附加一个 `->`，则只会记录由目标造成的伤害
- 如果在目标字符串前面附加一个 `->`，或在目标字符串后面附加一个 `<-`，则只会记录对目标造成的伤害

目标示例：
- `->me`: 对订阅者自己造成的伤害
- `->creeper`: 对爬行者造成的伤害
- `vex->`: 由恼鬼造成的伤害
- `<-players`: 由玩家造成的伤害
- `zombie`: 由/对僵尸造成的伤害
- `minecraft:zombie`, `<-zombie->`, `->zombie<-`: 同 `zombie`
- `->@e[distance=..10]`: 对订阅者 10m 范围内实体造成的伤害

属性:
- 默认选项: `all`
- 参考选项: `all`, `players`, `me`, `->creeper`, `vex->`, `Steve`, `@e[distance=..10]`


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


## 存活时间 (lifeTime)

`/log lifeTime <实体类型>`

一个 HUD 记录器

显示玩家所处在的维度中指定实体类型当前于 [存活时间追踪器](commands_cn.md#存活时间-lifetime) 中的数据

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

计算平均值的采样时长可通过规则 [lightQueueLoggerSamplingDuration](rules_cn.md#光照队列记录器采样时长-lightqueueloggersamplingduration) 指定，默认值为 60gt

类似 `/log mobcaps`， 你可以通过记录器选项来指定你想要记录光照队列的世界。

属性:
- 默认选项: `dynamic`
- 参考选项: `dynamic`, `overworld`, `the_nether`, `the_end`


## 内存 (memory)

`/log memory`

于 tab 栏中显示服务端当前消耗的内存以及占用的最大内存

属性:
- 默认选项: N/A
- 参考选项: N/A


## 微时序 (microTiming)

`/log microTiming <类型>`

记录元件的微时序，元件所在区块的加载票等级需至少为弱加载 (加载票等级 32)

见规则 [微时序](rules_cn.md#微时序-microtiming) 以获得详细信息，记得使用 `/carpet microTiming true` 启用记录器功能

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


## 经验球 (xporb)

`/log xporb <events>`

基本上与 [掉落物记录器](#掉落物-item) 相同，只不过监视的是经验球实体
