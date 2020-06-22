Carpet-TIS-Addition
-----------

[>>> English <<<](https://github.com/TISUnion/Carpet-TIS-Addition)

这是一个 [Carpet mod](https://github.com/gnembon/fabric-carpet) 的扩展 mod，包含了不少~~NotVanilla的~~有意思的功能以及特性

-----------

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

禁用TNT、地毯以及铁轨的复制

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

-----------

# 监视器

**在 1.14.4 版本中不可用，因为 Carpet 未给出相关接口支持**

## 加载票 (ticket)

`/log ticket <监视类型>`

记录加载票的创建

用 csv 格式，例如 `portal,dragon` 来监视多种类型的加载票

**警告:** 监视 `unknown` 加载票的话可能会导致你被刷屏

- 默认值: `portal`
- 参考选项: `portal,dragon`, `start`, `dragon`, `player`, `forced`, `light`, `portal`, `post_teleport`, `unknown`


## 内存 (memory)

`/log memory`

于 tab 栏中显示服务端当前消耗的内存以及占用的最大内存

-----------

# 统计信息

为了兼容原版客户端，这些自定义的统计信息均无法在客户端的统计信息页面查看


## 破基岩 (break_bedrock)

当一个基岩被活塞或者是粘性活塞删除时，这块基岩五米范围内最近的玩家将会将此统计信息数值 +1

准则: `minecraft.custom:minecraft.break_bedrock`

-----------
# 其他

- 将假人的名字长度限制调整为 16 以防止真实玩家被踢出
