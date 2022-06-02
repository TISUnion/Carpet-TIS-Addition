[English](commands.md) | **中文**

\>\>\> [返回索引](readme_cn.md)

# 指令

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

如果你需要切换对不占用怪物容量的生物的策略，可以去看看规则 [lifeTimeTrackerConsidersMobcap](rules_cn.md#存活时间追踪器考虑怪物容量-lifetimetrackerconsidersmobcap)

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
- `/summon` 指令
- 怪物蛋等
- 方块掉落（仅掉落物）
- 史莱姆分裂
- 僵尸增援
- 刷怪笼
- 袭击
- 被生物或方块召唤
- 繁殖
- 被方块投出
- 生物掉落（仅掉落物及经验球）
- 生物扔出（仅掉落物）
- 生物转换
- 因传送门跨维度到来

注意，只有被追踪了生成的实体会被统计

追踪的实体移除原因
- 消失，包括立即消失、随机消失、因游戏难度的消失以及超时消失
- 受伤致死
- 获取持久（persistent）标签。注意此时实体并未移除出世界
- 骑上载具（1.16+）。注意此时实体并未移除出世界
- 末影人捡起方块（1.16+）。注意此时实体并未移除出世界
- 实体合并（仅掉落物及经验球）
- 被漏斗或漏斗矿车收集（仅掉落物）
- 进入虚空
- 被玩家或生物捡起（仅掉落物及经验球）
- 生物转换
- 因传送门跨维度离开
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
/lifetime filter <实体类型> clear
```

为指定实体类型设置实体筛选器。在 `<实体类型>` 处键入 `global` 以设置全局筛选器

实体需要被对应的筛选器所接受才可被存活时间追踪器进行追踪

使用 `@e` 类型的 Minecraft 实体选择器来输入实体筛选器，如：`@e[distance=..100,nbt={Item:{id:"minecraft:oak_sapling"}}]`

使用 `/lifetime filter` 来显示激活的实体筛选器


## 操控世界 (manipulate)

操控世界

### container

`/manipulate container`

操控世界的数据结构容器，包括：

| 容器名 | 指令前缀 | 支持的操作               |
| --- | --- |---------------------|
| 实体列表 | `entity` | 翻转、随机打乱             |
| 可运算的方块实体列表 | `tileentity` | 翻转、随机打乱、查询给定位置/总览信息 |
| 计划刻队列 | `tiletick` | 添加元素、移除给定位置的元素      |
| 方块事件队列 | `blockevent` | 添加元素、移除给定位置的元素      |

指令列表：

```
/manipulate container entity [revert|shuffle]
/manipulate container tileentity [query|revert|shuffle|statistic]
/manipulate container tiletick add <pos> <block> <delay> [<priority>]
/manipulate container tiletick remove <pos>
/manipulate container blockevent add <pos> <block> <type> <data>
/manipulate container blockevent remove <pos>
```


## 袭击 (raid)

### 列表 (list)

`/raid list [<full>]`

列出目前所有袭击的信息

### 追踪 (tracking)

`/raid tracking [<start|stop|restart|realtime>]`

开启一个袭击追踪器以收集并统计进行中的袭击的状态信息


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


## 移除实体 (removeentity)

`/removeentity <target>`: 将选中实体直接从世界中移除。对玩家无效


## spawn

### mobcapsLocal

**于 Minecraft 1.18.2+ 中可用**

`/spawn mobcapsLocal [<玩家>]`

以类似 carpet 的 `/spawn mobcaps` 命令的格式显示指定玩家的局部怪物容量

若未指定玩家，则它将显示指令源的局部怪物容量

另见: [mobcapslocal记录器](loggers_cn.md#局部怪物容量-mobcapslocal)


## tick

### warp status

`/tick warp status`

显示当前 tick warp 的状态信息，如启动者、估计剩余时间等

见 [tickWarp 记录器](loggers_cn.md#tickwarp) 以了解更方便地查询信息的方法
