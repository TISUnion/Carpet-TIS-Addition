---
sidebar_position: 1
---

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


## 禁用繁殖冷却 (breedingCooldownDisabled)

移除所有生物的繁殖冷却时间

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 炼药锅方块类物品交互修复 (cauldronBlockItemInteractFix)

让玩家可以对着填充有水的炼药锅放置方块

仅对 Minecraft \<= 1.16.x 有效。这个烦人的机制已经在 1.17+ 中被修复了

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 聊天信息长度上限解锁 (chatMessageLengthLimitUnlocked)

解锁在聊天信息 / 聊天栏指令串的长度限制

长度限制将从 `256` 提升至 `32000`

需要在客户端中安装 Carpet TIS Addition 模组

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CLIENT`


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


## 玩家重生丢失客户端设置数据修复 (clientSettingsLostOnRespawnFix)

修复在玩家重生或从末地进入末地门时，新创建的玩家实体未迁移旧玩家实体中储存着的客户端设置的问题

因此依赖客户端设置数据的模组总能正常的工作，如本模组以及worldedit模组的服务端翻译

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`


## 存活时间追踪器 (commandLifeTime)

启用 `/lifetime` 命令用于追踪生物存活时间等信息

可助于调试刷怪塔等

- 类型: `String`
- 默认值: `true`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `COMMAND`


## 世界控制命令开关 (commandManipulate)

启用 `/manipulate` 命令用于控制世界

- 类型: `String`
- 默认值: `false`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `COMMAND`, `EXPERIMENTAL`


## 袭击追踪器 (commandRaid)

启用 `/raid` 命令用于列出或追踪袭击信息

- 类型: `String`
- 默认值: `true`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `COMMAND`


## 刷新命令开关 (commandRefresh)

启用 `/refresh` 命令让你的客户端与服务端保持同步

- 类型: `String`
- 默认值: `true`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `COMMAND`


## 射线追踪命令开关 (commandRaycast)

启用 `/raycast` 命令用于分析射线追踪

- 类型: `String`
- 默认值: `ops`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `COMMAND`


## 移除实体命令开关 (commandRemoveEntity)

启用 `/removeentity` 命令用于直接在世界中抹除目标实体

- 类型: `String`
- 默认值: `ops`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `COMMAND`, `CREATIVE`


## 睡眠命令开关 (commandSleep)

启用 `/sleep` 命令用于制造卡顿

- 类型: `String`
- 默认值: `ops`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `COMMAND`, `CREATIVE`


## 网络测速指令开关 (commandSpeedTest)

启用 `/speedtest` 命令用于网络速度测试

可以使用选项 [speedTestCommandMaxTestSize](#测速指令最大测试大小-speedtestcommandmaxtestsize) 来调整最大允许的测试大小

- 类型: `String`
- 默认值: `ops`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `COMMAND`, `TISCM_PROTOCOL`


## 创造模式瞬间驯服动物 (creativeInstantTame)

让创造模式的玩家可以瞬间驯服各种动物

影响范围：猫、狼、鹦鹉、驯服行为类似马的所有动物

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 创造玩家地狱放水 (creativeNetherWaterPlacement)

允许创造模式的玩家在地狱通过水桶放出水

技术上来讲，本条规则对所有 ultrawarm 的维度都生效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 创造玩家无物品冷却 (creativeNoItemCooldown)

取消创造模式玩家的任何物品使用冷却

例如使用末影珍珠的 20gt 冷却

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 创造玩家强制打开容器 (creativeOpenContainerForcibly)

允许创造模式的玩家打开被阻挡的容器，如潜影盒

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## NBT调试拉取请求无需权限 (debugNbtQueryNoPermission)

让客户端 F3 + I 操作所触发的调试请求无需任何权限等级需求

在原版中，该操作需要权限等级至少为 2

需要在客户端中安装 Carpet TIS Addition 模组

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CLIENT`


## 反混淆崩溃报告堆栈追踪 (deobfuscateCrashReportStackTrace)

反混淆崩溃报告中输出的堆栈追踪

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`


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


## 重新引入粉板瞬时更新循环 (dustTrapdoorReintroduced)

**本规则仅在 Minecraft >= 1.20 中存在**

重新引入由红石粉 + 活板门组成的瞬时更新循环结构

该规则实际上做了如下2件事情：

1. 让打开的活板门无法重定向红石粉的朝向（还原 mc1.20 前的表现）
2. 让红石粉忽略来自下方的状态更新（还原 mc1.20.3 前的表现）

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


## 鞘翅状态对方块使用烟花可推进移植 (elytraFireworkBoostOnBlockBackport)

**本规则仅在 Minecraft < 1.21.6 中存在**

移植 Minecraft 1.21.6+ 版本中的特性：在鞘翅飞行状态下，对着方块使用烟花火箭物品时，会推进玩家而非发射烟花

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`


## 鞘翅烟花加速别断栓绳 (elytraFireworkKeepLeashConnection)

**本规则仅在 Minecraft >= 1.21.6 中存在**

启用时，鞘翅飞行中使用烟花不再会断开已有的栓绳连接，即还原mc1.21.6前的表现

注意：该规则不会阻止因相对距离过远而导致的栓绳断开

本规则仅在 Minecraft >= 1.21.6 中有效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`


## enchant指令约束移除 (enchantCommandNoRestriction)

移除 `/enchant` 指令中所有对目标附魔的约束

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 禁用末地门开启音效 (endPortalOpenedSoundDisabled)

禁用末地门开启时发出的音效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 实体大脑记忆未释放修复 (entityBrainMemoryUnfreedFix)

修复了生物实体被移除出世界后，其大脑记忆依然未被释放的bug

如果有一个接连不断生物流，其中的生物在被移除前记住其他先被移除的生物，那么就有可能创建出无限长的大脑记忆链，并导致游戏出现内存泄漏

修复了 [MC-260605](https://bugs.mojang.com/browse/MC-260605)。本规则使用了 Mojang 在 1.19.4-pre3 中采取的同款修复方式

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`


## 生物死亡立即移除 (entityInstantDeathRemoval)

移除生物实体死亡前的 20gt 等待时间

启用时，生物实体将在死亡后立即消失

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


## 爆炸不影响实体 (explosionNoEntityInfluence)

爆炸不会影响任何实体

这里的影响包括伤害、加速等效果

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
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


## 假人像真人一样运算 (fakePlayerTicksLikeRealPlayer)

调整 carpet 执行假人运算和 `/player` 动作所位于的游戏阶段，
使它们的表现尽可能与真实玩家一致

调整前的 carpet 逻辑 -> 调整后的逻辑:

1. 假人的实体相关运算: 实体阶段 -> 网络阶段逻辑
2. `/player` 指令的玩家动作包: 实体阶段 -> 异步任务阶段

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CARPET_MOD`


## 假人远程召唤 (fakePlayerRemoteSpawning)

使用/player指令远程召唤假人的权限需求

在这里，“远程”指的是被召唤的假人位于16m以外，或另一个维度

- 类型: `String`
- 默认值: `true`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `CARPET_MOD`


## 禁用耕地被踩踏 (farmlandTrampledDisabled)

阻止耕地被生物踩成泥土

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## fill指令模式增强 (fillCommandModeEnhance)

增加 `/fill` 指令中各种模式的功能

增加 `softreplace` 模式: 尽可能地保留原方块的方块状态，可用于替换楼梯/半砖的材质等

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 地狱堡垒地狱砖刷怪游走修复 (fortressNetherBricksPackSpawningFix)

**本规则仅在 Minecraft [1.18.2, 1.21.5) 中存在**

修复 [1.18.2, 1.21.5) 里，地狱堡垒怪物在地狱砖/非地狱砖上的游走出现隔离性的问题

该问题引入自 1.18.2-pre1。对于首次游走结束后位于地狱砖上的成群生成，游走至内结构非地狱砖上后无法刷出怪物，反之亦然

问题影响例子：带基岩天花板的凋灵玫瑰凋灵骷髅塔的效率，在增加了地狱砖游走地板延伸后，效率有可能会不升反降

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`


## 拍扁三角形分布 (flattenTriangularDistribution)

**本规则仅在 Minecraft >= 1.19 中存在**

把 Minecraft 随机数发生器的三角形分布改为均匀分布

借此，边界情况就更有可能发生了

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


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

你可以使用 [`/scounter` 指令](commands.md#供给计数器-scounter)，或者订阅 [`scounter` 记录器](loggers.md#供给计数器-scounter)，来追踪这类漏斗的物品输出数量

该规则同时是 [`/scounter` 指令](commands.md#供给计数器-scounter) 的开关

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `COMMAND`


## 漏斗经验计数器 (hopperXpCounters)

使漏斗计数器能够统计经验球中的经验值

启用后，漏斗计数器将可以“吸收”经验球并统计它们的经验值

你可以使用 [`/xcounter` 指令](commands.md#经验计数器-xcounter)，或者订阅 [`xcounter` 记录器](loggers.md#经验计数器-xcounter)，来查看经验计数器的统计结果

该规则同时也是 [`/xcounter` 指令](commands.md#经验计数器-xcounter) 指令的开关

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `COMMAND`


## HUD记录器更新间隔 (HUDLoggerUpdateInterval)

覆写Carpet Mod HUD记录器的更新间隔，单位为gametick

- 类型: `int`
- 默认值: `20`
- 参考选项: `1`, `5`, `20`, `100`
- 分类: `TIS`, `CARPET_MOD`


## 重新引入瞬时方块更新逻辑 (instantBlockUpdaterReintroduced)

**本规则仅在 Minecraft >= 1.19 中存在**

重新引入 1.19 以前的瞬时方块更新逻辑

本规则让更新抑制在 1.19+ 中再次可行

它还可以让[微时序记录器](loggers#微时序-microtiming)的记录结果更加清晰具有逻辑，如 1.19 以前的版本一样的清晰

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `PORTING`


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


## 移动记录器 (loggerMovement)

移动记录器的开关 / 权限等级需求

- 类型: `string`
- 默认值: `ops`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `LOGGER`


## 操纵方块指令数量上限 (manipulateBlockLimit)

`/manipulate block` 系列指令可操作的方块数量上限

- 类型: `int`
- 默认值: `1000000`
- 参考选项:`1000`, `1000000`, `1000000000`
- 分类: `TIS`, `COMMAND`, `EXPERIMENTAL`


## 微时序 (microTiming)

启用[微时序记录器](loggers.md#微时序-microtiming)的功能

使用染料物品来标记并输出红石元件的动作、方块更新与堆栈跟踪

使用 `/log microTiming` 来开始监视

开启时服务端性能将受到一定影响

见规则 [microTimingDyeMarker](#微时序染料记号-microtimingdyemarker) 的介绍以获得有关染料记号的使用指令

**下述内容已弃用，将于未来被移除**

用羊毛方块以及末地烛来标记需记录的方块

末地烛会检测方块更新，红石元件会输出它们的动作

| 方块类型                                                     | 如何记录            |
| ------------------------------------------------------------ | ------------------- |
| 侦测器、活塞、末地烛                                         | 指向羊毛块          |
| 中继器、比较器、红石火把、红石粉、铁轨、按钮、拉杆、压力板、拌线勾 | 放置/依附在羊毛块上 |

除此之外，一种通用的记录方块动作的手段是使用羊毛块上的末地烛指向需记录的方块

可通过操控规则 [微时序目标](#微时序目标-microtimingtarget) 以切换记录方法

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 微时序染料记号 (microTimingDyeMarker)

允许玩家手持染料右击方块来将其标记为微时序记录器的目标

你需要订阅微时序记录器才能标记并渲染方块

使用相同颜色的染料再次右击以切换至末地烛模式，此时记录器将会额外地记录标记位置处的方块更新。再次右击则会移除颜色记号

使用粘液球物品右击标记可将其设为可移动。当标记依附的方块被活塞移动时，它会自动的移动到对应的新位置

使用指令 `/carpet microTimingDyeMarker clear` 以移除所有记号

你可以使用命名的染料物品来创建一个命名的记号。记号的名称同时将会在记录器的输出信息中展示

如果客户端拥有 fabric-carpet mod，被标记的方块将会显示出一个边框。如果客户端还带有carpet-tis-addition，则记号的名称还可透过方块查看

*由于 fabric carpet 对 scarpet 形状渲染及 carpet 网络通讯协议支持的缺乏，视觉渲染相关功能无法在 1.14.4 分支中使用*

- 类型: `string`
- 默认值: `true`
- 参考选项: `false`, `true`, `clear`
- 分类: `TIS`, `CREATIVE`


## 微时序目标 (microTimingTarget)

设置指定微时序记录器记录目标的方法。被染料记号标记的方块总会被记录

`labelled`: 记录被羊毛块标记的事件。**已弃用**

`in_range`: 记录离任意玩家 32m 内的事件。**已弃用**

`all`: 记录所有事件。**谨慎使用**。**已弃用**

`marker_only`: 仅记录被染料记号标记的方块。将其与规则 [microTimingDyeMarker（微时序染料记号）](#微时序染料记号-microtimingdyemarker) 一起使用

- 类型: `enum`
- 默认值: `marker_only`
- 参考选项: `labelled`, `in_range`, `all`, `marker_only`
- 分类: `TIS`, `CREATIVE`


## 微时序游戏刻划分 (microTimingTickDivision)

设置指定微时序记录器划分两个游戏刻的方法

`world_timer`: 划分于世界计时器自增时

`player_action`: 划分于玩家操作阶段开始前

- 类型: `enum`
- 默认值: `world_timer`
- 参考选项: `world_timer`, `player_action`
- 分类: `TIS`, `CREATIVE`


## 矿车完整掉落移植 (minecartFullDropBackport)

移植 Minecraft 1.19+ 版本中的特性：矿车实体在被破坏时会掉落完整的矿车物品

本规则仅在 Minecraft \< 1.19 中有效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


## 矿车可在地面放置 (minecartPlaceableOnGround)

让矿车可以被放置在非铁轨的普通地面上，就像船一样

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `FEATURE`


## 矿车搭载乘客最小速度 (minecartTakePassengerMinVelocity)

决定矿车将其附近实体作为乘客搭载上车所需的最低水平方向速度（m/gt）

将其设为 0 以让矿车忽略速度，像船一样总能将附件实体载上车

将其设为 NaN 以让矿车永远不能把实体载上车

- 类型: `double`
- 默认值: `0.1`
- 参考选项: `0`, `0.1`, `NaN`
- 分类: `TIS`, `CREATIVE`


## 怪物容量显示忽略杂项 (mobcapsDisplayIgnoreMisc)

在 carpet 怪物容量显示中忽略杂项 (misc) 这一生物类型

因为它既占空间还没用：杂项生物类型不参与刷怪循环，在统计怪物容量时也被游戏忽略

影响 mobcaps 记录器以及 `/spawn mobcaps` 指令

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CARPET_MOD`


## 可移动强化深板岩 (moveableReinforcedDeepslate)

**本规则仅在 Minecraft >= 1.19 中存在**

令强化深板岩可被活塞移动

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`


## 自然刷怪使用1.13风格的高度图 (naturalSpawningUse13Heightmap)

令自然刷怪中初始坐标选取时Y值上限变为该XZ最高的遮光方块

基本上，本规则将自然刷怪中对高度图（heightmap）的使用，还原回了1.13及之前的版本

注意：这将增加少许刷怪阶段的卡顿

另见：规则 [naturalSpawningUse13HeightmapExtra](#自然刷怪使用113风格的高度图-额外选项-naturalSpawningUse13Heightmapextra)

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


## 自然刷怪使用1.13风格的高度图-额外选项 (naturalSpawningUse13HeightmapExtra)

在计算规则 [naturalSpawningUse13Heightmap](#自然刷怪使用113风格的高度图-naturalSpawningUse13Heightmap) 中的修改版高度图时，忽略活塞、粘液块、蜂蜜块

本规则的目的是确保与 1.13 前表现的一致性，但可能会导致一些非预期的机制滥用行为

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


## 地狱门实体交互检测使用完整方块形状 (netherPortalEntityInteractionCheckUseFullBlockShape)

**本规则仅在 Minecraft [1.21.5, 1.21.6) 中存在**

将地狱门方块在实体移动交互检测中使用的形状修改为完整方块

基本上，这条规则会将下界传送门的传送范围从传送门轮廓框改为一个完整的立方体

它将相关表现还原回了 MC < 25w04a（1.21.5 的快照）的表现，撤销了 [MC-101556](https://bugs.mojang.com/browse/MC-101556) 的修复

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


## 地狱门最大尺寸 (netherPortalMaxSize)

修改地狱门的最大尺寸限制

如果将其设置为了一个较大值，请小心使用

- 类型: `int`
- 默认值: `21`
- 参考选项: `21`, `64`, `128`, `384`
- 分类: `TIS`, `CREATIVE`


## 橡树长成鸡腿树百分率 (oakBalloonPercent)

橡树树苗长成鸡腿树（fancy_oak）的概率，使用百分率作为值

如，0 代表没有鸡腿树，50 代表有 50% 的概率长成鸡腿树，100 代表总长成鸡腿树

将其设为 -1 以禁用本规则并使用原版逻辑（10% 概率长成鸡腿树）

- 类型: `int`
- 默认值: `-1`
- 参考选项: `-1`, `0`, `50`, `100`
- 分类: `TIS`, `CREATIVE`


## 禁用侦测器检测功能 (observerNoDetection)

不准侦测器在受状态更新时添加计划刻事件

可以认为这条规则禁用了观察者的检测功能

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 黑曜石平台方块破坏器移植 (obsidianPlatformBlockBreakerBackport)

移植 Minecraft 1.21+ 版本中的特性：末地黑曜石平台的生成可以破坏已存在的方块

值得注意，在原版 MC [1.16, 1.21) 里，普通实体创建黑曜石平台时的方块遍历顺序，和原版 mc1.21+ 的顺序，是不一样的

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


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

已知不兼容的 mod: [async](https://modrinth.com/mod/async)

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

**本规则仅在 Minecraft < 1.16.2 中存在**

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


## 红石粉中继器比较器 (redstoneDustRepeaterComparatorIgnoreUpwardsStateUpdate)

**本规则仅在 Minecraft >= 1.20.2 only 中存在**

让红石粉、红石中继器和红石比较器忽略来自下方的状态更新

这将撤销在 23w35a (1.20.2 快照) 中引入的更改。这一规则可帮你再次轻松地创建浮空比较器

备注：规则 `dustTrapdoorReintroduced` 对红石粉方块实现了同款更改撤销

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


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


## 重新引入潜影盒CCE抑制器 (shulkerBoxCCEReintroduced)

**本规则仅在 Minecraft >= 1.20.2 中存在**

重新引入使用比较器读取带有非 `Inventory` 类方块实体的潜影盒方块而触发的 `ClassCastException` 更新抑制器

愿虚空的魔法盒永存！

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


## 潜影盒内容物掉落移植 (shulkerBoxContentDropBackport)

**本规则仅在 Minecraft < 1.17 中存在**

移植潜影盒物品在被伤害摧毁后掉落其内含的全部物品的特性

该特性引入于 Minecraft 1.17

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


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


## 生成幼年生物概率 (spawnBabyProbably)

调整刷怪时生成幼年生物变种的概率

将其设为 `-1` 以禁用本规则并使用原版逻辑

- 类型: `double`
- 默认值: `-1`
- 参考选项: `-1`, `0`, `0.5`, `1`
- 分类: `TIS`, `CREATIVE`


## 生成骑手概率 (spawnJockeyProbably)

调整刷怪时生成骑手变种的概率

影响的骑手类型: 鸡骑士、蜘蛛骑士、炽足兽骑手

对于炽足兽，生成僵尸猪灵、幼年炽足兽的概率比将保持 1:3

将其设为 `-1` 以禁用本规则并使用原版逻辑

- 类型: `double`
- 默认值: `-1`
- 参考选项: `-1`, `0`, `0.5`, `1`
- 分类: `TIS`, `CREATIVE`


## 测速指令最大测试大小 (speedTestCommandMaxTestSize)

在使用 `/speedtest` 测速指令进行网络速度测试时，最大的测试数据大小，单位 MiB

- 类型: `int`
- 默认值: `10`
- 参考选项: `10`, `100`, `1024`, `10240`
- 分类: `TIS`, `COMMAND`


## stop指令两步确认 (stopCommandDoubleConfirmation)

为 `/stop` 指令添加两步确认机制，以防止误触导致意外地关掉了服务器

你需要在1分钟内输入两次 `/stop` 指令来关闭服务器

该确认机制仅对玩家有效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `COMMAND`


## 结构方块不保留流体 (structureBlockDoNotPreserveFluid)

结构方块在放置含水方块时，不保留已存在的流体

同时有着抑制 [MC-130584](https://bugs.mojang.com/browse/MC-130584) 发生的副作用

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `BUGFIX`


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


## 同步服务端mspt指标数据 (syncServerMsptMetricsData)

向客户端同步服务端的 mspt 指标数据，借此，玩家可使用 F3 + ALT 在调试界面中看到这一服务端的指标

需要在客户端中安装 Carpet TIS Addition 模组

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `TISCM_PROTOCOL`


## tick指令carpet化 (tickCommandCarpetfied)

**本规则仅在 Minecraft >= 1.20.3 中存在**

只需这一条规则，即可将地毯模组中的 `/tick` 命令恢复到 mc1.20.3 之前的版本

启用这条规则相当于将以下规则设置为以下值:

- [tickCommandEnhance](#tick指令增强-tickcommandenhance) = `true`
- [tickCommandPermission](#tick指令权限-tickcommandpermission) = `2`
- [tickFreezeCommandToggleable](#切换式tickfreeze指令-tickfreezecommandtoggleable) = `true`
- [tickProfilerCommandsReintroduced](#重新引入tick性能分析指令-tickprofilercommandsreintroduced) = `true`
- [tickWarpCommandAsAnAlias](#tickwarp指令别名重现-tickwarpcommandasanalias) = `true`

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CARPET_MOD`, `COMMAND`, `PORTING`


## tick指令增强 (tickCommandEnhance)

**本规则仅在 Minecraft >= 1.20.3 中存在**

重新启用那些 mc1.20.3 前，用于增强 carpet 模组 `/tick` 指令功能的特性

启用的增强功能：

1. 启用 `/tick sprint status` 指令（即以前的 `/tick warp status` 指令）

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CARPET_MOD`, `COMMAND`


## tick指令权限 (tickCommandPermission)

**本规则仅在 Minecraft >= 1.20.3 中存在**

覆盖原版 `/tick` 指令的权限等级需求

将其设为 `2` 或 `ops` 以还原 carpet 模组在 mc1.20.3 之前的默认表现

如果你想在例如命令方块中执行 `/tick` 命令，本规则将会很有用

- 类型: `String`
- 默认值: `3`
- 参考选项: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `TIS`, `CARPET_MOD`, `COMMAND`


## 切换式tickFreeze指令 (tickFreezeCommandToggleable)

**本规则仅在 Minecraft >= 1.20.3 中存在**

让 `/tick freeze` 像以前的 carpet 模组一样，切换式地设置游戏的冻结状态

也就是说，若游戏运行已冻结，则再次输入 `/tick freeze` 将解除冻结状态

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CARPET_MOD`, `COMMAND`, `PORTING`


## 重新引入tick性能分析指令 (tickProfilerCommandsReintroduced)

**本规则仅在 Minecraft >= 1.20.3 中存在**

带回 carpet 模组的 `/tick health`、`/tick entities` 这两个子命令

与以前的 carpet 模组一样，这两个指令的功能将与 carpet 模组的 `/profile [health|entities]` 子命令一致

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CARPET_MOD`, `COMMAND`, `PORTING`


## tickWarp指令别名重现 (tickWarpCommandAsAnAlias)

**本规则仅在 Minecraft >= 1.20.3 中存在**

带回 `/tick warp` 子命令，让它变成原版 `/tick sprint` 指令的一个别名

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CARPET_MOD`, `COMMAND`, `PORTING`


## 计划刻上限 (tileTickLimit)

修改每游戏刻中计划刻事件的执行次数上限

- 类型: `int`
- 默认值: `65536`
- 参考选项: `1024`, `65536`, `2147483647`
- 分类: `TIS`, `CREATIVE`


## TISCM网络协议 (tiscmNetworkProtocol)

TISCM网络协议的开关

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `TIS`, `TISCM_PROTOCOL`


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


## 坚韧的凋零玫瑰 (toughWitherRose)

由死而生，凋零玫瑰非常坚韧，能在任意表面上种植

该规则移除了凋零玫瑰所有的放置约束，这意味着你可以将零玫瑰种植在任何地方

在你想用更新抑制的凋零玫瑰做凋灵骷髅塔时，这条规则可以帮你一把

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


## 亡灵生物别在阳光下着火 (undeadDontBurnInSunlight)

阻止亡灵生物在阳光下着火

不过他们的头盔依然会在阳光下损失耐久

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 更新抑制模拟器 (updateSuppressionSimulator)

青金石矿石上方的激活 / 充能铁轨可模拟更新抑制器

在青金石矿石上已亮起的激活 / 充能铁轨将要熄灭时，抛出所提供的 JVM 异常

`false`: 关闭规则; `true`: 启用规则并使用 `StackOverflowError`; 其他: 启用规则并使用所给异常

- 类型: `String`
- 默认值: `false`
- 参考选项: `false`, `true`, `StackOverflowError`, `OutOfMemoryError`, `ClassCastException`
- 分类: `TIS`, `CREATIVE`


## 禁用宝库黑名单 (vaultBlacklistDisabled)

禁用宝库方块实体中储存的玩家黑名单的相关功能

在本规则启用后：

1. 玩家开启宝库后，玩家的 UUID 不会被添加到宝库黑名单中
2. 无论玩家是否位于宝库黑名单中，玩家均可激活、开启宝库

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


## 大力地狱门创建 (violentNetherPortalCreation)

允许地狱门创建时无视门框内已存在的方块，直接强制开启地狱门

`false`：关闭此规则，原版表现

`replaceable`：仅允许无视那些可替换的方块（如流体、草）

`all`：允许无视任何方块，除了黑曜石。黑曜石因涉及门框计算逻辑，不能被无视

注意: 强制开门过程中的方块替换不会造成方块更新，与原版的表现一致

- 类型: `enum`
- 默认值: `false`
- 参考选项: `false`, `replaceable`, `all`
- 分类: `TIS`, `CREATIVE`


## 虚空伤害数值 (voidDamageAmount)

修改虚空伤害的数值

- 类型: `double`
- 默认值: `4`
- 参考选项: `0`, `4`, `1000`
- 分类: `TIS`, `CREATIVE`


## 虚空伤害忽略玩家 (voidDamageIgnorePlayer)

阻止玩家受到任何虚空伤害

对玩家完全无害的虚空，好耶！

如果规则的值被设置为了一个 `,` 分隔的游戏模式列表，则只有游戏模式位于该列表中的玩家能免疫虚空伤害

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


## 重新引入水爆 (wetExplosionReintroduced)

重新引入了 MC 版本 1.21.2 ~ 1.21.5 中的“水爆”机制

如果一个爆炸由实体产生（例如点燃的 TNT），并且该实体正与水接触，那么该爆炸将不会对物品实体、盔甲架实体以及所有装饰性实体造成伤害

该规则在 MC 1.21.2 ~ 1.21.5 中无任何效果

另见：[MC-3697](https://bugs.mojang.com/browse/MC-3697)

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `PORTING`


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


## 去除异步任务执行延迟 (yeetAsyncTaskExecutionDelay)

让 MinecraftServer 的异步任务不被延迟，尽早执行

在原版中，当服务器进行 tick warp 时，异步任务的执行最多可能被延迟 4 tick

此规则开启后，可确保异步任务总会在最近的异步任务阶段被执行

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`


## 抑制空闲mspt (yeetIdleMspt)

抑制空闲 mspt 的出现

一些操作，比如区块卸载和兴趣点保存，可能会造成空闲 mspt 开销

只有当前 gt 的耗时尚未达到掉刻极限时（mspt 还没到50，同时没在 tick warp），空闲mspt才会出现

当启用此规则时，空闲 mspt 将被抑制。若服务器未掉刻也未 tick warp，其 mspt 可能会有所降低

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`


## 阻止乱序聊天数据包踢出 (yeetOutOfOrderChatKick)

**本规则仅在 Minecraft >= 19 中存在**

阻止玩家被服务器以“接收到了乱序的聊天数据包...”为由踢出游戏

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `TIS`


-----------

# 移植的规则列表

## 光照引擎最大任务组数 (lightEngineMaxBatchSize)

- 移植自：fabric carpet [1.4.23](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.23)
- 移植的分支：1.14.4, 1.15.2

## 结构方块轮廓距离 (structureBlockOutlineDistance)

- 移植自：fabric carpet [1.4.25](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.25)
- 移植的分支：1.14.4, 1.15.2

## 阻止更新抑制崩溃 (yeetUpdateSuppressionCrash)

阻止服务端因 `StackOverflowError`、`OutOfMemoryError` 或 `ClassCastException` 异常而造成的服务器崩溃

具体功能实现类似 carpet 的 `updateSuppressionCrashFix` 规则，但包含更多信息

- 移植自：
  - fabric carpet [1.4.50](https://github.com/gnembon/fabric-carpet/releases/tag/1.4.50) 的规则 `updateSuppressionCrashFix`
  - TISCarpet13 [build238](https://github.com/TISUnion/TISCarpet113/releases/tag/build238) 的规则 `yeetUpdateSuppressionCrash`
