[English](rules.md) | **中文**

\>\>\> [返回索引](readme_cn.md)

# 规则

## 禁用反刷屏监测 (antiSpamDisabled)

禁用玩家身上的刷屏检测，包括：聊天信息发送冷却、创造模式扔物品冷却

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `SURVIVAL`


## 方块事件广播范围 (blockEventPacketRange)

设置会在方块事件成功执行后收到数据包的玩家范围

对于活塞而言，这一个数据包用于显示活塞的运动。把这个值调小以减小客户端卡顿

- 类型: `double`
- 默认值: `64`
- 参考选项: `0`, `16`, `64`, `128`
- 分类: `TIS`, `OPTIMIZATION`


## 方块放置忽略实体 (blockPlacementIgnoreEntity)

方块可放置时无视实体碰撞检测，也就是你可以将方块放在实体内

仅对创造模式玩家有效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 炼药锅方块类物品交互修复 (cauldronBlockItemInteractFix)

让玩家可以对着填充有水的炼药锅放置方块

仅对 Minecraft <= 1.16.x 有效。这个烦人的机制已经在 1.17+ 中被修复了

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 区块更新数据包阈值 (chunkUpdatePacketThreshold)

如果方块变化的数量大于这个阈值，则游戏将仅发送区块更新数据包而非若干个方块变更数据包

增加该数值或许可以减小网络带宽用量，并在子区段同时存在不少方块实体与方块变化时提升客户端的帧数

将其设为非常高以模拟1.16+的表现，也就是不存在区块更新数据包，仅有多个方块变更数据包

该规则仅于1.16前的版本有效

- 类型: `int`
- 默认值: `64`
- 参考选项: `64`, `4096`, `65536`
- 分类: `TIS`, `OPTIMIZATION`, `EXPERIMENTAL`


## 区块刻速度 (chunkTickSpeed)

修改每游戏刻每区块的区块刻运算的频率

默认值为 `1`。将其设为 `0` 以禁用区块刻

受影响的游戏阶段：
- 雷电
- 结冰与积雪
- 随机刻

在值为 `n` 时，每游戏刻每区块，气候相关的阶段会发生 `n` 次，而随机刻会在每区段中发生 `n` * `randomTickSpeed` 次

- 类型: `int`
- 默认值: `1`
- 参考选项: `0`, `1`, `10`, `100`, `1000`
- 分类: `TIS`, `CREATIVE`


## 存活时间追踪器 (commandLifeTime)

启用 `/lifetime` 命令用于追踪生物存活时间等信息

可助于调试刷怪塔等

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `COMMAND`


## 世界控制命令开关 (commandManipulate)

启用 `/manipulate` 命令用于控制世界

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `COMMAND`, `EXPERIMENTAL`


## 袭击追踪器 (commandRaid)

启用 `/raid` 命令用于列出或追踪袭击信息

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `COMMAND`


## 刷新命令开关 (commandRefresh)

启用 `/refresh` 命令让你的客户端与服务端保持同步

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `COMMAND`


## 创造玩家强制打开容器 (creativeOpenContainerForcibly)

允许创造模式的玩家打开被阻挡的容器，如潜影盒

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 发射器不消耗物品 (dispenserNoItemCost)

开启后，发射器和投掷器使用被激活时不再消耗物品

无论投掷物品还是使用物品都如此，但是投掷器传输物品仍会消耗物品

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `DISPENSER`, `CREATIVE`


## 发射器发射龙息 (dispensersFireDragonBreath)

发射器可使用龙息瓶创造出龙息效果云

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `DISPENSER`


## enchant指令约束移除 (enchantCommandNoRestriction)

移除 `/enchant` 指令中所有对目标附魔的约束

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 实体速度丢失 (entityMomentumLoss)

将其设为 `false` 以关闭从磁盘载入时实体超过10m/gt部分的沿轴速度的丢失

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `EXPERIMENTAL`


## 实体放置无视碰撞 (entityPlacementIgnoreCollision)

在使用物品放置实体时禁用相关的方块与实体的碰撞检测

受影响的物品：盔甲架、末影水晶、所有种类的船

刷怪蛋物品不在作用范围内

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 实体同步距离 (entityTrackerDistance)

服务器同步实体信息至客户端的最大水平切比雪夫距离（单位: 区块）

基本上这就是服务端的“实体渲染视距”，不过这个距离依旧会被服务端视距所约束

将其设为一个不小于服务端视距的数值，就能令服务端将玩家视距内的所有实体都同步至客户端

将其设为一个非正值以使用原版逻辑

需要重新加载区块以将新的规则数值应用到实体上

- 类型: `int`
- 默认值: `-1`
- 参考选项: `0`, `16`, `64`
- 分类: `TIS`, `CREATIVE`


## 实体同步间隔 (entityTrackerInterval)

服务器同步实体信息至客户端的时间间隔（单位: gt）

如果设为一个较小的数值，如 1，服务器将每 1gt 都同步实体信息至客户端，这能减小客户端发生实体不同步现象的概率

将其设为一个非正值以使用原版逻辑

需要重新加载区块以将新的规则数值应用到实体上

- 类型: `int`
- 默认值: `-1`
- 参考选项: `0`, `1`
- 分类: `TIS`, `CREATIVE`


## 爆炸数据包广播范围 (explosionPacketRange)

设置在爆炸发生时，爆炸数据包对玩家的广播范围

- 类型: `double`
- 默认值: `64`
- 参考选项: `0`, `16`, `64`, `128`, `2048`
- 分类: `TIS`, `CREATIVE`


## 方块状态解析忽略失败 (failSoftBlockStateParsing)

忽略在 `/setblock` 等指令的方块状态参数中出现的无效键/值参数

在原版中这些无效的键/值会导致指令解析出错。这个规则抑制了这一出错

有助于跨版本粘贴 litematica 原理图等

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 假人名称前缀 (fakePlayerNamePrefix)

为 `/player` 指令召唤出来的假人名称添加指定前缀

将其设置为 `#none` 以阻止添加前缀

这可阻止玩家召唤奇怪名字的假人，还能让玩家列表变得更整洁

- 类型: `String`
- 默认值: `#none`
- 参考选项: `#none`, `bot_`
- 分类: `TIS`, `CARPET_MOD`


## 假人名称后缀 (fakePlayerNameSuffix)

为 `/player` 指令召唤出来的假人名称添加指定后缀

将其设置为 `#none` 以阻止添加后缀

- 类型: `String`
- 默认值: `#none`
- 参考选项: `#none`, `_fake`
- 分类: `TIS`, `CARPET_MOD`


## 禁用流体破坏 (fluidDestructionDisabled)

禁用流体流动造成的方块破坏

流体会简单的停留在即将破坏方块时的状态

这个规则可以用于在创造模式中防止流体意外地冲坏你的红石电路

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 漏斗计数器无限速度 (hopperCountersUnlimitedSpeed)

当漏斗指向羊毛方块时，漏斗将拥有无限的物品吸取以及传输速度，且无冷却时间

仅当 Carpet Mod 中的 hopperCounters 开启时有效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `CARPET_MOD`


## 漏斗不消耗物品 (hopperNoItemCost)

上方放有羊毛方块的漏斗可不消耗物品地无限输出储存的物品

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## HUD监视器更新间隔 (HUDLoggerUpdateInterval)

覆写Carpet Mod HUD监视器的更新间隔，单位为gametick

- 类型: `int`
- 默认值: `20`
- 参考选项: `1`, `5`, `20`, `100`
- 分类: `TIS`, `CARPET_MOD`


## 瞬时命令方块 (instantCommandBlock)

令位于红石矿上的命令方块瞬间执行命令，而不是添加一个1gt的计划刻事件用于执行

仅影响普通命令方块

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 禁用物品实体跳过移动运算 (itemEntitySkipMovementDisabled)

移除物品实体跳过移动运算的机制

改回为 1.13 及以前的物品实体机制，也就是低速着地的物品实体依然会每gt都运算移动，而非每4gt

当你需要精准使用物品实体运动逻辑时有用

会导致利用了相关机制的机器无法工作，如 2no2name 的无线红石

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 保持弱加载区块的怪物 (keepMobInLazyChunks)

弱加载区块的怪物不再会被刷新掉，就像 1.15 之前版本似的

此选项仅对 1.15 至 1.16 间的版本有效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `EXPERIMENTAL`


## 大木桶 (largeBarrel)

史上最棒的物品仓储方块: 大木桶

两个相邻的底部相连木桶可以组成一个大木桶

大木桶的行为逻辑跟大箱子相近

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `EXPERIMENTAL`


## 存活时间追踪器考虑怪物容量 (lifeTimeTrackerConsidersMobcap)

存活时间追踪器对不占怪物容量的生物的策略

`true`: 不追踪不占用怪物容量的生物，并与生物不影响怪物容量的时刻将其标记为已移除，如当它们捡起物品时。便于设计刷怪塔

`false`: 追踪所有可追踪的生物，在生物确实被删除时将其标记为已移除。便于设计袭击农场或非刷怪塔的机器

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 光照队列记录器采样时长 (lightQueueLoggerSamplingDuration)

光照队列记录器的采样时长，单位为游戏刻

影响记录器中显示的，除队列大小外的所有数据

- 类型: `int`
- 默认值: `60`
- 参考选项: `1`, `20`, `60`, `100`, `6000`
- 分类: `TIS`


## 光照更新 (lightUpdates)

暂停或者禁止光照更新

若被设为抑制(suppressed)，光照更新不会被执行，这可用于模拟光照抑制器

若被设为忽略(ignored)，光照更新不会被计划，这常用于在创造模式中制造光照错误

若被设为关闭(off)，光照更新不会被计划或被执行

**【警告】**：若被设为抑制或关闭，新的区块将无法被加载。如果此时玩家等原因尝试加载新的区块，服务端将进入无法跳出的死循环

- 类型: `enum`
- 默认值: `on`
- 参考选项: `on`, `suppressed`, `ignored`, `off`
- 分类: `TIS`, `CREATIVE`, `EXPERIMENTAL`


## 微时序 (microTiming)

启用[微时序监视器](loggers_cn.md#微时序-microTiming)的功能

使用羊毛块来输出红石元件的动作、方块更新与堆栈跟踪

使用 `/log microTiming` 来开始监视

开启时服务端性能将受到一定影响

末地烛会检测方块更新，红石元件会输出它们的动作

| 方块类型                                                     | 如何记录            |
| ------------------------------------------------------------ | ------------------- |
| 侦测器、活塞、末地烛                                         | 指向羊毛块          |
| 中继器、比较器、红石火把、红石粉、铁轨、按钮、拉杆、压力板、拌线勾 | 放置/依附在羊毛块上 |

除此之外，一种通用的记录方块动作的手段是使用羊毛块上的末地烛指向需记录的方块

可通过操控规则 [微时序目标](#微时序目标-microTimingTarget) 以切换记录方法

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 微时序染料记号 (microTimingDyeMarker)

允许玩家手持染料右击方块来将其标记为微时序监视器的目标

你需要订阅微时序监视器才能标记并渲染方块

使用相同颜色的染料再次右击以切换至末地烛模式，此时记录器将会额外地记录标记位置处的方块更新。再次右击则会移除颜色记号

使用粘液球物品右击标记可将其设为可移动。当标记依附的方块被活塞移动时，它会自动的移动到对应的新位置

使用指令 `/carpet microTimingDyeMarker clear` 以移除所有记号

你可以使用命名的染料物品来创建一个命名的记号。记号的名称同时将会在监视器的输出信息中展示

如果客户端拥有 fabric-carpet mod，被标记的方块将会显示出一个边框。如果客户端还带有carpet-tis-addition，则记号的名称还可透过方块查看

*由于 fabric carpet 对 scarpet 形状渲染及 carpet 网络通讯协议支持的缺乏，视觉渲染相关功能无法在 1.14.4 分支中使用*

- 类型: `string`
- 默认值: `true`
- 参考选项: `false`, `true`, `clear`
- 分类: `TIS`, `CREATIVE`


## 微时序目标 (microTimingTarget)

设置指定微时序记录器记录目标的方法。被染料记号标记的方块总会被记录

`labelled`: 记录被羊毛块标记的事件

`in_range`: 记录离任意玩家 32m 内的事件

`all`: 记录所有事件。**谨慎使用**

`marker_only`: 仅记录被染料记号标记的方块。将其与规则 [microTimingDyeMarker（微时序染料记号）](#微时序染料记号-microTimingDyeMarker) 一起使用

- 类型: `enum`
- 默认值: `labelled`
- 参考选项: `labelled`, `in_range`, `all`
- 分类: `TIS`, `CREATIVE`


## 微时序游戏刻划分 (microTimingTickDivision)

设置指定微时序记录器划分两个游戏刻的方法

`world_timer`: 划分于世界计时器自增时

`player_action`: 划分于玩家操作阶段开始前

- 类型: `enum`
- 默认值: `world_timer`
- 参考选项: `world_timer`, `player_action`
- 分类: `TIS`, `CREATIVE`


## op玩家不准作弊 (opPlayerNoCheat)

禁用部分指令以避免op玩家意外地作弊

影响的指令列表：`/gamemode`, `/tp`, `/teleport`, `/give`, `/setblock`, `/summon`

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `SURVIVAL`


## 优化高速实体移动 (optimizedFastEntityMovement)

通过仅检测沿轴移动方向的方块碰撞来优化高速实体的移动

受 [carpetmod112](https://github.com/gnembon/carpetmod112) 的规则 `fastMovingEntityOptimization` 启发

同规则 `optimizedTNT` 一起使用可大幅度提升炮的性能表现

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `OPTIMIZATION`, `EXPERIMENTAL`


## 优化硬碰撞箱实体碰撞 (optimizedHardHitBoxEntityCollision)

优化实体与硬碰撞箱实体的碰撞

它使用了一个额外的独立的列表在区块中储存带有硬碰撞箱的实体，包括船和潜影贝

它能在实体移动并搜索路径上的带有硬碰撞箱的实体时减少大量无用的运算，因为世界里船和潜影贝的数量总是少数

在加载区块前开启它以使其工作，在地狱门刷怪塔中有~20%的性能提升

与添加了新实体的 mod 可能不兼容

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `OPTIMIZATION`, `EXPERIMENTAL`


## TNT优化高优先级 (optimizedTNTHighPriority)

用带有更高优先级的 Mixin 注入来实现 carpet 规则 `optimizedTNT`

因此规则 `optimizedTNT` 可以覆盖 lithium 的爆炸优化

当然，它需要规则 `optimizedTNT` 开启才能工作

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `OPTIMIZATION`, `EXPERIMENTAL`


## POI更新开关 (poiUpdates)

方块变化时是否会更新 POI

将其设为 `false` 以禁用 POI 更新

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 持久性记录器订阅 (persistentLoggerSubscription)

在服务器重启后依然记忆着玩家订阅的记录器及记录器选项

仅在玩家首次登录时应用 carpet 的 `defaultLoggers` 规则

记录器订阅储存于 `config/carpettisaddition/logger_subscriptions.json` 中

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CARPET_MOD`


## 精准实体放置 (preciseEntityPlacement)

当使用物品放置/召唤实体时，将实体准确地放置在玩家指针指向的坐标点

受影响的物品：刷怪蛋、盔甲架、末影水晶

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 刷铁轨机修复 (railDupingFix)

禁用老式的移动点亮的充能或激活铁轨的刷铁轨机

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`


## 红石粉随机更新顺序 (redstoneDustRandomUpdateOrder)

随机化红石粉发出方块更新的顺序

有助于测试你的装置是否依赖于位置

在规则 `fastRedstoneDust` 启用时无效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 可再生龙蛋 (renewableDragonEgg)

让龙蛋变得可再生

当龙蛋处于龙息效果云内时，龙蛋有一定概率吸收龙息并“召唤”出一个新的龙蛋

可与选项 [dispenserFireDragonBreath](https://github.com/TISUnion/Carpet-TIS-Addition/blob/master/README_CN.md#发射器发射龙息-dispensersfiredragonbreath) 联动

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`


## 可再生龙首 (renewableDragonHead)

被高压爬行者杀死的末影龙将会掉落一个龙首

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`


## 可再生鞘翅 (renewableElytra)

当幻翼被潜影贝杀死时有给定概率掉落鞘翅

设置为0以禁用

- 类型: `double`
- 默认值: `0`
- 参考选项: `0`, `0.2`, `1`
- 分类: `TIS`, `FEATURE`


## 中继器延迟折半 (repeaterHalfDelay)

当红石中继器位于红石矿上方时，红石中继器的延迟将减半

延迟将会由 2, 4, 6, 8 游戏刻变为 1, 2, 3, 4 游戏刻

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 刷沙机修复 (sandDupingFix)

禁用使用末地门的刷沙机以及刷重力方块机

重力方块包括沙子、铁砧、龙蛋等

在开启后刷沙机的沙子将会仅被传送至另一个纬度

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`


## 融雪最小亮度 (snowMeltMinLightLevel)

雪片融化所需的最小亮度等级

在原版里这个值为12，意味着雪片将在亮度等级>=12时于随机刻中融化

将其设为0以将所有位于你建筑上的烦人的雪片融化

将其设为与防止降雪的最小亮度等级 (12) 来方便地测试你的建筑是否能借助亮度来防降雪

你可以修改游戏规则 `randomTickSpeed` 来加速雪的融化，也可以修改地毯规则 `chunkTickSpeed` 来加速降雪的过程

- 类型: `int`
- 默认值: `12`
- 参考选项: `0`, `10`, `12`
- 分类: `TIS`, `CREATIVE`


## 结构方块不保留流体 (structureBlockDoNotPreserveFluid)

结构方块在放置含水方块时，不保留已存在的流体

同时有着抑制 [MC-130584](https://bugs.mojang.com/browse/MC-130584) 发生的副作用

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`


## 结构方块范围限制 (structureBlockLimit)

覆写结构方块的范围限制

当相对位置的值大于32时客户端里结构的位置可能会错误地显示

- 类型: `int`
- 默认值: `32`
- 参考选项: `32`, `64`, `96`, `127`
- 分类: `TIS`, `CREATIVE`


## 同步光照线程 (synchronizedLightThread)

将光照线程与主线程同步，这样光照线程就不会于落后主线程而失去同步

服务器将会在每个世界开始运算时等待光照线程的任务完成

你可以借此安全地 `/tick warp` 而不用担心潜在的光照抑制或光照不同步

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `EXPERIMENTAL`


## 计划刻上限 (tileTickLimit)

修改每游戏刻中计划刻事件的执行次数上限

- 类型: `int`
- 默认值: `65536`
- 参考选项: `1024`, `65536`, `2147483647`
- 分类: `TIS`, `CREATIVE`


## TNT复制修复 (tntDupingFix)

禁用TNT、地毯以及部分铁轨的复制机

基于依附性方块的复制机会无法复制，基于红石原件更新的复制机会无法保留被复制的方块

~~Dupe bad dig good~~

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`, `EXPERIMENTAL`


## TNT引信时长 (tntFuseDuration)

覆盖 TNT 的默认引信时长

这也会影响被爆炸点燃的 TNT 的引信时长

- 类型: `int`
- 默认值: `80`
- 参考选项: `0`, `80`, `32767`
- 分类: `TIS`, `CREATIVE`


## TNT忽略红石信号 (tntIgnoreRedstoneSignal)

阻止 TNT 被红石信号点燃

你仍可以使用爆炸等方式点燃TNT

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 工具化TNT (tooledTNT)

由玩家引发的爆炸破坏并掉落物品时会应用玩家手上的工具

因此你可以点燃TNT以采集需要特定工具或者附魔的方块，只要你在爆炸时拿着正确的工具

比如，你可以拿着精准采集镐子来采集冰，或者拿着剪刀来采集草

此规则同样适用于玩家以外的生物

技术上来讲，此规则将来源生物主手上的物品应用在了爆炸里战利品表的创建中

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`


## 完全没有方块更新 (totallyNoBlockUpdate)

禁用所有方块更新以及状态更新的执行

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 禁用海龟蛋被践踏 (turtleEggTrampledDisabled)

阻止海龟蛋因实体踩踏而破坏

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 可视化投掷物记录器 (visualizeProjectileLoggerEnabled)

启用可视化投掷物记录器

试试 `/log projectiles visualize` 吧

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 虚空相对海拔高度 (voidRelatedAltitude)

修改虚空相对世界底部的海拔高度，此处的虚空指实体会受到虚空伤害的区域

- 类型: `double`
- 默认值: `-64`
- 参考选项: `-64`, `-512`, `-4096`
- 分类: `TIS`, `CREATIVE`


## 禁用凋灵生成音效 (witherSpawnedSoundDisabled)

禁用凋灵在召唤后生命值回满时发出的世界中所有玩家都能听到的音效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 经验球追踪距离 (xpTrackingDistance)

修改经验球检测并追踪玩家的距离

将其调至 0 以禁用追踪

- 类型: `double`
- 默认值: `8`
- 参考选项: `0`, `1`, `8`, `32`
- 分类: `TIS`, `CREATIVE`


-----------

# 移植的规则列表

## 光照引擎最大任务组数 (lightEngineMaxBatchSize)

- 移植自：fabric carpet [1.4.23](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.23)
- 移植的分支：1.14.4, 1.15.2

## 结构方块轮廓距离 (structureBlockOutlineDistance)

- 移植自：fabric carpet [1.4.25](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.25)
- 移植的分支：1.14.4, 1.15.2
