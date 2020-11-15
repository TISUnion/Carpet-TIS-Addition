# Carpet TIS Addition

[![License](https://img.shields.io/github/license/TISUnion/Carpet-TIS-Addition.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![Issues](https://img.shields.io/github/issues/TISUnion/Carpet-TIS-Addition.svg)](https://github.com/TISUnion/Carpet-TIS-Addition/issues)
[![MC Versions](http://cf.way2muchnoise.eu/versions/For%20MC_carpet-tis-addition_all.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-tis-addition)
[![CurseForge](http://cf.way2muchnoise.eu/full_carpet-tis-addition_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-tis-addition)

[>>> English <<<](https://github.com/TISUnion/Carpet-TIS-Addition)

这是一个 [Carpet mod](https://github.com/gnembon/fabric-carpet) (fabric-carpet) 的扩展 mod，包含了不少~~NotVanilla的~~有意思的功能以及特性

跟同 Minecraft 版本的 carpet mod 一起使用即可。尽可能地使用较新的 carpet mod

# 索引

## 规则

- [方块事件广播范围](#方块事件广播范围-blockEventPacketRange)
- [结构方块范围限制](#结构方块范围限制-structureBlockLimit)
- [经验球追踪距离](#经验球追踪距离-xpTrackingDistance)
- [tnt复制修复](#tnt复制修复-tntDupingFix)
- [假人名称前缀](#假人名称前缀-fakePlayerNamePrefix)
- [假人名称后缀](#假人名称后缀-fakePlayerNameSuffix)
- [可再生龙蛋](#可再生龙蛋-renewableDragonEgg)
- [发射器发射龙息](#发射器发射龙息-dispensersFireDragonBreath)
- [可再生龙首](#可再生龙首-renewableDragonHead)
- [hud监视器更新间隔](#hud监视器更新间隔-HUDLoggerUpdateInterval)
- [漏斗计数器无限速度](#漏斗计数器无限速度-hopperCountersUnlimitedSpeed)
- [可再生鞘翅](#可再生鞘翅-renewableElytra)
- [刷沙机修复](#刷沙机修复-sandDupingFix)
- [刷铁轨机修复](#刷铁轨机修复-railDupingFix)
- [袭击追踪器](#袭击追踪器-commandRaid)
- [保持弱加载区块的怪物](#保持弱加载区块的怪物-keepMobInLazyChunks)
- [发射器不消耗物品](#发射器不消耗物品-dispenserNoItemCost)
- [op玩家不准作弊](#op玩家不准作弊-opPlayerNoCheat)
- [红石粉随机更新顺序](#红石粉随机更新顺序-redstoneDustRandomUpdateOrder)
- [瞬时命令方块](#瞬时命令方块-instantCommandBlock)
- [光照更新](#光照更新-lightUpdates)
- [微时序](#微时序-microTiming)
- [微时序目标](#微时序目标-microTimingTarget)
- [禁用反刷屏监测](#禁用反刷屏监测-antiSpamDisabled)
- [方块放置碰撞检测](#方块放置碰撞检测-blockPlacementIgnoreEntity)
- [区块刻速度](#区块刻速度-chunkTickSpeed)
- [计划刻上限](#计划刻上限-tileTickLimit)
- [POI更新开关](#POI更新开关-poiUpdates)

## 监视器

- [加载票](#加载票-ticket)
- [内存](#内存-memory)
- [掉落物](#掉落物-item)
- [经验球](#经验球-xporb)
- [袭击](#袭击-raid)
- [微时序](#微时序-microTiming-1)
- [伤害](#伤害-damage)

## 指令

- [袭击](#袭击-raid-1)
- [信息](#信息-info)

## 其它

- [其他](#其他)


# 规则列表

## 方块事件广播范围 (blockEventPacketRange)

设置会在方块事件成功执行后收到数据包的玩家范围 

对于活塞而言，这一个数据包是用于显示活塞移动的话。把这个值调小以减小客户端卡顿

- 类型: `double`  
- 默认值: `64`  
- 参考选项: `0`, `16`, `64`, `128`
- 分类: `TIS`, `OPTIMIZATION` 


## 结构方块范围限制 (structureBlockLimit)

覆写结构方块的范围限制

当相对位置的值大于32时客户端里结构的位置可能会错误地显示

- 类型: `int`  
- 默认值: `32`  
- 参考选项: `32`, `64`, `96`, `127`
- 分类: `TIS`, `CREATIVE` 


## 经验球追踪距离 (xpTrackingDistance)

修改经验球检测并追踪玩家的距离

将其调至0以禁用追踪"

- 类型: `double`  
- 默认值: `8`
- 参考选项: `0`, `1`, `8`, `32`
- 分类: `TIS`, `CREATIVE` 


## TNT复制修复 (tntDupingFix)

禁用TNT、地毯以及部分铁轨的复制机

基于依附性方块的复制机会无法复制，基于红石原件更新的复制机会无法保留被复制的方块

~~Dupe bad dig good~~

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`, `EXPERIMENTAL` 


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


## 可再生龙蛋 (renewableDragonEgg)

让龙蛋变得可再生

当龙蛋处于龙息效果云内时，龙蛋有一定概率吸收龙息并“召唤”出一个新的龙蛋

可与选项 [dispenserFireDragonBreath](https://github.com/TISUnion/Carpet-TIS-Addition/blob/1.15.2/README_CN.md#发射器发射龙息-dispensersfiredragonbreath) 联动

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`


## 发射器发射龙息 (dispensersFireDragonBreath)

发射器可使用龙息瓶创造出龙息效果云

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `DISPENSER`


## 可再生龙首 (renewableDragonHead)

被高压爬行者杀死的末影龙将会掉落一个龙首

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`


## HUD监视器更新间隔 (HUDLoggerUpdateInterval)

覆写Carpet Mod HUD监视器的更新间隔，单位为gametick

- 类型: `int`  
- 默认值: `20`
- 参考选项: `1`, `5`, `20`, `100`
- 分类: `TIS`, `CARPET_MOD`


## 漏斗计数器无限速度 (hopperCountersUnlimitedSpeed)

当漏斗指向羊毛方块时，漏斗将拥有无限的物品吸取以及传输速度

仅当Carpet Mod中的hopperCounters开启时有效

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `CARPET_MOD`


## 可再生鞘翅 (renewableElytra)

当幻翼被潜影贝杀死时有给定概率掉落鞘翅

设置为0以禁用

- 类型: `double`  
- 默认值: `0`  
- 参考选项: `0`, `0.2`, `1`
- 分类: `TIS`, `FEATURE`


## 刷沙机修复 (sandDupingFix)

禁用使用末地门的刷沙机以及刷重力方块机

重力方块包括沙子、铁砧、龙蛋等

在开启后刷沙机的沙子将会仅被传送至另一个纬度

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`


## 刷铁轨机修复 (railDupingFix)

禁用老式的移动点亮的充能或激活铁轨的刷铁轨机

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `BUGFIX`


## 袭击追踪器 (commandRaid)

启用 `/raid` 命令用于追踪袭击信息

- 类型: `boolean`  
- 默认值: `true`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `COMMAND`


## 保持弱加载区块的怪物 (keepMobInLazyChunks)

弱加载区块的怪物不再会被刷新掉，就像 1.15 之前版本似的

此选项对1.15以前的版本无效

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `FEATURE`, `EXPERIMENTAL` 


## 发射器不消耗物品 (dispenserNoItemCost)

开启后，发射器和投掷器使用被激活时不再消耗物品

无论投掷物品还是使用物品都如此，但是投掷器传输物品仍会消耗物品

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `DISPENSER`, `CREATIVE`


## op玩家不准作弊 (opPlayerNoCheat)

禁用部分指令以避免op玩家意外地作弊

影响的指令列表：`/gamemode`, `/tp`, `/teleport`, `/give`, `/setblock`, `/summon`

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `SURVIVAL`


## 红石粉随机更新顺序 (redstoneDustRandomUpdateOrder)

随机化红石粉发出方块更新的顺序

有助于测试你的装置是否依赖于位置

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 瞬时命令方块 (instantCommandBlock)

令位于红石矿上的命令方块瞬间执行命令，而不是添加一个1gt的计划刻事件用于执行

仅影响普通命令方块

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 光照更新 (lightUpdates)

暂停或者禁止光照更新

若被设为抑制(suppressed)，光照更新不会被执行

若被设为关闭(off)，光照更新不会被计划或被执行

**【警告】**：若被设为抑制或关闭，新的区块将无法被加载。如果此时玩家等原因尝试加载新的区块，服务端将进入无法跳出的死循环

- 类型: `enum`  
- 默认值: `on`  
- 参考选项: `on`, `suppressed`, `off`
- 分类: `TIS`, `CREATIVE`, `EXPERIMENTAL`


## 微时序 (microTiming)

启用[微时序监视器](#微时序-microTiming-1)的功能

使用羊毛块来输出红石元件的动作、方块更新与堆栈跟踪

使用 `/log microTiming` 来开始监视

开启时服务端性能将受到一定影响

末地烛会检测方块更新，红石元件会输出它们的动作

| 方块类型                                                     | 如何记录            |
| ------------------------------------------------------------ | ------------------- |
| 侦测器、活塞、末地烛                                         | 指向羊毛块          |
| 中继器、比较器、红石火把、红石粉、铁轨、按钮、拉杆、压力板、拌线勾 | 放置/依附在羊毛块上 |

除此之外，一种通用的记录方块动作的收单是使用羊毛块上的末地烛指向需记录的方块

可通过操控规则 [微时序目标](#微时序目标-microTimingTarget) 以切换记录方法

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


## 微时序目标 (microTimingTarget)

设置指定微时序记录器记录目标的方法

`labelled`: 记录被羊毛块标记的事件

`in_range`: 记录离任意玩家 32m 内的事件

`all`: 记录所有事件。**谨慎使用**

- 类型: `enum`  
- 默认值: `labelled`  
- 参考选项: `labelled`, `in_range`, `all`
- 分类: `TIS`, `CREATIVE`


## 禁用反刷屏监测 (antiSpamDisabled)

禁用玩家身上的刷屏检测，包括：聊天信息发送冷却、创造模式扔物品冷却

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`, `SURVIVAL`


## 方块放置忽略实体 (blockPlacementIgnoreEntity)

方块可放置时无视实体碰撞检测，也就是你可以将方块放在实体内

仅对创造模式玩家有效

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


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


## 计划刻上限 (tileTickLimit)

修改每游戏刻中计划刻事件的执行次数上限

- 类型: `int`  
- 默认值: `65536`  
- 参考选项: `1024`, `65536`, `2147483647`
- 分类: `TIS`, `CREATIVE`


## POI更新开关 (poiUpdates)

方块变化时是否会更新 POI

将其设为 `false` 以禁用 POI 更新

- 类型: `boolean`  
- 默认值: `true`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `CREATIVE`


-----------

# 监视器

## 加载票 (ticket)

`/log ticket <监视类型>`

记录加载票的添加以及移除

用 csv 格式，例如 `portal,dragon` 来监视多种类型的加载票

可用的选项分隔符: `,`、`.` 与 ` ` (`.` 是 1.14.4 版本的唯一选择)

**警告:** 监视 `unknown` 加载票的话可能会导致你被刷屏

属性:
- 默认值: `portal`
- 参考选项: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## 内存 (memory)

`/log memory`

于 tab 栏中显示服务端当前消耗的内存以及占用的最大内存

属性:
- 默认值: N/A
- 参考选项: N/A


## 掉落物 (item)

`/log item <事件>`

记录某些事件在掉落物实体身上的发生，如物品闲置五分钟后自然消失

可用的事件类型:
- `die`: 当物品死亡
- `despawn`: 当物品自然消失

用 csv 格式，例如 `despawn,die` 来监视多种事件

可用的选项分隔符: `,`、`.` 与 ` ` (`.` 是 1.14.4 版本的唯一选择)

属性:
- 默认值: `despawn`
- 参考选项: `despawn`, `die`, `despawn,die`


## 经验球 (xporb)

`/log xporb <events>`

基本上与 [掉落物监视器](https://github.com/TISUnion/Carpet-TIS-Addition/blob/1.15.2/README_CN.md#掉落物-item) 相同，只不过监视的是经验球实体


## 袭击 (raid)

`/log raid`

记录以下袭击相关的事件：

- 袭击被创建
- 袭击被移除
- 袭击的不祥之兆等级被提升
- 袭击的中心点被移动

属性:
- 默认值: N/A
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
- 默认值: `merged`
- 参考选项: `all`, `merged` `unique`


## 伤害 (damage)

`/log damage <目标>`

记录生物的受伤，以及伤害结算的具体流程

可选的记录目标:
- `all`: 记录所有生物
- `players`: 记录有玩家参与的生物伤害
- `me`: 记录与自己相关的伤害

属性:
- 默认值: `all`
- 参考选项: `all`, `players`, `me`

# 指令

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

-----------

# 其他

- 将假人的名字长度限制调整为 16 以防止真实玩家被踢出
- 将 tick warp 最大时长限制调整为 `Integer.MAX_VALUE`
- 在 `/carpet` 指令中显示 Carpet TIS Addition 的版本信息
