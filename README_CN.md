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
- [微时序](#微时序-microtick)

## 监视器

- [加载票](#加载票-ticket)
- [内存](#内存-memory)
- [掉落物](#掉落物-item)
- [经验球](#经验球-xporb)
- [袭击](#袭击-raid)
- [微时序](#微时序-microtick-1)

## 指令

- [袭击](#袭击-raid-1)
- [信息](#信息-info)

## 其它

- [其他](#其他)


# 特性列表

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


## 微时序 (microtick)

启用[微时序监视器](#微时序-microtick-1)的功能

使用羊毛块来输出红石元件的动作、方块更新与堆栈跟踪

使用 `/log microtick` 来开始监视

开启时服务端性能将受到一定影响

末地烛会检测方块更新，红石元件会输出它们的动作

| 方块类型                     | 如何记录动作        |
| ---------------------------- | ------------------- |
| 侦测器、活塞、末地烛         | 指向羊毛块          |
| 中继器、比较器、铁轨、按钮等 | 放置/依附在羊毛块上 |

除此之外，羊毛块上的末地烛指向方块的动作也会被记录

如果 [lithium mod](https://github.com/jellysquid3/lithium-fabric) 被加载，由于其会替换计划刻容器导致原监听“添加计划刻”事件的 Mixin 失效，一个朴素实现的备用 Mixin 会用来监听“添加计划刻”事件，不过在这个情况下并非所有方块的“添加计划刻”事件都能被监听

- 类型: `boolean`  
- 默认值: `false`  
- 参考选项: `false`, `true`
- 分类: `TIS`, `COMMAND`, `CREATIVE`

-----------

# 监视器

## 加载票 (ticket)

`/log ticket <监视类型>`

记录加载票的添加以及移除

用 csv 格式，例如 `portal,dragon` 来监视多种类型的加载票

可用的选项分隔符: `,`、`.` 与 ` ` (`.` 是 1.14.4 版本的唯一选择)

**警告:** 监视 `unknown` 加载票的话可能会导致你被刷屏

- 默认值: `portal`
- 参考选项: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## 内存 (memory)

`/log memory`

于 tab 栏中显示服务端当前消耗的内存以及占用的最大内存


## 掉落物 (item)

`/log item <事件>`

记录某些事件在掉落物实体身上的发生，如物品闲置五分钟后自然消失

可用的事件类型:
- `die`: 当物品死亡
- `despawn`: 当物品自然消失

用 csv 格式，例如 `despawn,die` 来监视多种事件

可用的选项分隔符: `,`、`.` 与 ` ` (`.` 是 1.14.4 版本的唯一选择)

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


## 微时序 (microtick)

`/log microtick <类型>`

记录元件的微时序，元件所在区块的加载票等级需至少为弱加载 (加载票等级 32)

见规则 [微时序](#微时序-microtick) 以获得详细信息，记得使用 `/carpet microTick true` 启用监视器功能

可用的类型选项: 
- `all`: 默认值，输出所有事件
- `unique`: 默认值，输出所有每游戏刻中第一次出现的事件

- 默认值: `all`
- 参考选项: `all`, `unique`

# 指令

## 袭击 (raid)

### 列表 (list)

`/raid list [<full>]`

列出目前所有袭击的信息

### 追踪 (tracking)

`raid tracking [<start|stop|restart|realtime>]`

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
