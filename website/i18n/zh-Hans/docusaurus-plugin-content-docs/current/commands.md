---
sidebar_position: 3
---

# 指令

## 信息 (info)

### world ticking_order

`/info world ticking_order`

显示游戏中所有维度运算的顺序

### world weather

`/info world weather`

显示当前的世界中天气相关变量的数据，并将其整理成一份天气预报


## 存活时间 (lifetime)

一个追踪所有新生成生物的存活时间及生成/移除原因的记录器

该记录器主要用于测试各种刷怪塔，用于追踪从生物开始影响怪物容量上限，至移出怪物容量上限的这个过程。该记录器的生成追踪并未覆盖所有的生物生成原因

对于一个生物，除了其因各种原因被移出世界，当其**首次**变为不计入怪物容量上限时，例如被命名、捡起物品，也会被标记移除。如果一个生物生成时已不在怪物容量上限中，那它将不会被追踪

作为一项附加功能，该跟踪器还跟踪由方块或生物掉落的物品和经验球的存活时间。注意该追踪器并未追踪所有可能的掉落物或经验球生成，推荐先做好相关测试

给指令添加 `realtime` 后缀可将速率结果从基于游戏时间转换为基于现实时间

如果你需要切换对不占用怪物容量的生物的策略，可以去看看规则 [存活时间追踪器考虑怪物容量](rules.md#存活时间追踪器考虑怪物容量-lifetimetrackerconsidersmobcap)

### tracking

`/lifetime tracking [<start|stop|restart>]`

控制存活时间追踪器

追踪的实体类型：
- 所有种类的生物 (MobEntity)。生物需占用刷怪上限才可被追踪（可将规则 [存活时间追踪器考虑怪物容量](rules.md#存活时间追踪器考虑怪物容量-lifetimetrackerconsidersmobcap) 设置为 false 以跳过这个刷怪上限占用检查）
- 掉落物实体
- 经验球实体

追踪的实体生成原因
- 方块掉落（仅掉落物）
- 繁殖
- `/summon` 指令
- 从容器中掉出（仅掉落物）
- 被方块投出
- 怪物蛋等
- 骑手
- 骑手的坐骑
- 自然刷新
- 末影人放下方块（1.16+）。需要规则 [存活时间追踪器考虑怪物容量](rules.md#存活时间追踪器考虑怪物容量-lifetimetrackerconsidersmobcap) 的值为 true
- 地狱门僵尸猪人生成
- 袭击
- 史莱姆分裂
- 刷怪笼
- 被生物或方块召唤
- 离开载具（1.16+）。需要规则 [存活时间追踪器考虑怪物容量](rules.md#存活时间追踪器考虑怪物容量-lifetimetrackerconsidersmobcap) 的值为 true
- 僵尸增援
- 生物掉落（仅掉落物及经验球）
- 生物扔出（仅掉落物）
- 生物转换
- 跨维度到来

注意，只有被追踪了生成的实体会被统计

追踪的实体移除原因
- 消失，包括立即消失、随机消失、因游戏难度的消失以及超时消失
- 受伤致死
- 获取持久（persistent）标签。需要规则 [存活时间追踪器考虑怪物容量](rules.md#存活时间追踪器考虑怪物容量-lifetimetrackerconsidersmobcap) 的值为 true
- 骑上载具（1.16+）。需要规则 [存活时间追踪器考虑怪物容量](rules.md#存活时间追踪器考虑怪物容量-lifetimetrackerconsidersmobcap) 的值为 true
- 末影人捡起方块（1.16+）。需要规则 [存活时间追踪器考虑怪物容量](rules.md#存活时间追踪器考虑怪物容量-lifetimetrackerconsidersmobcap) 的值为 true
- 实体合并（仅掉落物及经验球）
- 被漏斗或漏斗矿车收集（仅掉落物）
- 进入虚空
- 自爆（如苦力怕）
- 被玩家或生物捡起（仅掉落物及经验球）
- 生物转换
- 跨维度离开
- 其他（其余未被统计的原因）。如果有些重要的移除原因未被统计，欢迎来提相关 feature request 的 issue

存活时间的定义为：**实体生成时刻与移除时刻间的经过的自然生物刷新阶段的数量**，也就是刷怪时被该实体影响了怪物容量上限的游戏刻数。技术上来讲，刷怪阶段数量计数器的注入位置是世界重新计算怪物容量上限前

统计信息以数量所占比例排序

### \<实体类型\>

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

### entity

操控目标实体

`/manipulate entity <实体选择器> [各类操作]`

指令列表：

目标实体自定义名称设置/清楚

```
/manipulate entity <目标> rename <名称文本>
/manipulate entity <目标> rename clear
```

目标实体的持久标签状态查询/修改

```
/manipulate entity <目标> persistent
/manipulate entity <目标> persistent set <持久标签状态>
```

目标实体载具逻辑控制

```
/manipulate entity <目标> mount <载具>
/manipulate entity <目标> dismount
```

目标实体速度控制

```
/manipulate entity <目标> velocity [add|set] <x> <y> <z>
```


## 网络测速

测试客户端和服务器之间的网络连接，包括下载/上传速度和平均ping值

### 帮助

```
/speedtest
```

显示速度测试命令的一些信息，包括：

- 简单的命令描述
- 当前最大测试大小
- 客户端支持状态

### 下载测试

```
/speedtest download [<size_mib>]
```

测试下载速度，即流量从服务器到客户端的速度

参数：

- `size_mib`：测试的流量大小。单位：MiB。默认值：10

### 上传测试

```
/speedtest upload [<size_mib>]
```

测试上传速度，即流量从客户端到服务器的速度

参数：

- `size_mib`：测试的流量大小。单位：MiB。默认值：10

### ping 测试

```
/speedtest ping [<count>] [<interval>]
```

测试服务器和客户端之间的 ping 值，进行指定轮测试

服务器将向客户端发送一个 ping，然后客户端将回复一个 pong 给服务器。
此轮测试的 ping 值将是服务器发送 ping 和接收 pong 之间的时间间隔

参数：

- `count`：ping 测试轮次的数量。默认值：4
- `interval`：两次 ping 之间的最小间隔。单位：秒。默认值：1

### 中止测试

```
/speedtest abort [<size_mib>]
```

中止当前正在进行的测试（下载、上传或 ping）


## 袭击 (raid)

### 列表 (list)

`/raid list [<full>]`

列出目前所有袭击的信息

### 追踪 (tracking)

`/raid tracking [<start|stop|restart|realtime>]`

开启一个袭击追踪器以收集并统计进行中的袭击的状态信息


### 射线追踪 (raycast)

`/raycast block <x1> <y1> <z1> <x2> <y2> <z2> [<shapeMode>] [<fluidMode>]`

使用给定的形状模式（shapeMode）以及流体模式（fluidMode），执行一次从 (x1, y1, z1) 到 (x2, y2, z2) 的方块射线追踪，然后显示射线追踪结果

参数 `<shapeMode>` 和 `<fluidMode>` 是可选的。它们的默认值见下文

形状模式（见 `net.minecraft.world.RaycastContext.ShapeType`）：

- `collider`（默认值）
- `outline`
- `visual`（mc1.16+）
- `falldamage_resetting`（mc1.18.2+）

流体模式（见 `net.minecraft.world.RaycastContext.FluidHandling`）：

- `none`（默认值）
- `source_only`
- `any`
- `water`（mc1.18.2+）

**警告**：两个相距过远的点之间的射线追踪可能会触发意料之外的区块加载


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

`/removeentity <target>`: 将选中实体直接从世界中移除

注意，对于储存型载具实体，它们不会掉落所包含的物品

该指令对玩家无效


## 供给计数器 (scounter)

类似 carpet 的漏斗计数器对应的 `/counter` 指令，该指令用于统计使用规则 [hopperNoItemCost](rules.md#漏斗不消耗物品-hoppernoitemcost) 创建的漏斗无限物品供给器所输出的物品数量

`/scounter`: 查看所有供给计数器的统计数据

`/scounter reset`: 重置所有供给计数器

`/scounter <color> [realtime]`: 查看指定供给计数器的统计数据，带上后缀 ` realtime` 则基于现实时间输出速率

`/scounter <color> reset`: 重置指定供给计数器


## 睡眠 (sleep)

即时阻塞当前线程给定持续时间，可用于制造卡顿

你可以将本条指令与命令方块以及规则 [瞬时命令方块](rules.md#瞬时命令方块-instantcommandblock) 配合使用，以在任意你想要的时间点制造卡顿

`/sleep`: 显示帮助信息

`/sleep <duration> (s|ms|us)`: 使用给定的时间单位，睡眠给定持续时间

可用的时间单位：

- `s`: 秒，1 * 10 ^ 0s
- `ms`: 毫秒，1 * 10 ^ -3s
- `us`: 微秒，1 * 10 ^ -6s


## spawn

### mobcapsLocal

**于 Minecraft 1.18.2+ 中可用**

`/spawn mobcapsLocal [<玩家>]`

以类似 carpet 的 `/spawn mobcaps` 命令的格式显示指定玩家的局部怪物容量

若未指定玩家，则它将显示指令源的局部怪物容量

另见: [mobcapslocal记录器](loggers.md#局部怪物容量-mobcapslocal)


## tick

### warp status

`/tick warp status`

显示当前 tick warp 的状态信息，如启动者、估计剩余时间等

见 [tickWarp 记录器](loggers.md#tickwarp) 以了解更方便地查询信息的方法
