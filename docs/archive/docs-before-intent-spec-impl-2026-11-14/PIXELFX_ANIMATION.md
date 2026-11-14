# 魔法像素特效持续制作规范

更新：2026-11-11。适用于当前102项的后续维护；保留已认可的原39术视觉和全部游戏数值。原作动画是首要依据，PixelFX是辅助，不以换色代替逐术轮廓、节奏与材质设计。参考目录为`D:/BlockbenchAssets/magic_animate_effect`，暂存模型目录为`D:/BlockbenchAssets/task1110`；它们仅用于制作，不是运行时依赖。

## 参考与许可证

- https://github.com/patch-gg/pixelfx ：使用core像素绘制器及fire/_lib.js的smokePuff、firePuff、ember烘焙素材，参考爆发→扩散→烟雾的分层。
- https://github.com/winkidney/pixelfx-godot ：借鉴共享BAKED帧缓存；没有引入Godot运行时或着色器。
- MIT许可证保留在`META-INF/licenses/pixelfx-MIT.txt`与`META-INF/licenses/pixelfx-godot-MIT.txt`。不承诺未实现的Bloom、屏幕折射或独立additive light缓冲。

## 制作与运行时约束

- 凝聚FORM、飞行FLIGHT、命中IMPACT分别设计；模型研究动画与Java运行时不是同一播放器。水用透明流层与断续高光；火用收尖火舌、小亮核与余烬，不用封闭白柱；冰/土保留刚性棱面，烟尘再扩散沉降。
- 火球弹、大火球、爆炸尾部朝玩家/速度反方向，不能在咏唱时翻转向上。风缚环身风带跟随真实状态；燃烧领域包裹全身。视觉曲率和粒子不得扩大实际伤害或碰撞。
- 原39参考网格路径`models/effect/reference/<id>.json`，纹理`textures/effect/reference/<id>.png`；其128×768图集包含24张128×32帧，由Java手动采样，nearest/clamp，不加原版animation重复切帧。扩展资源按实际导出检查，不能盲套同一尺寸。
- 可编辑源保留在`model-source/magic_effects/`；如用Blockbench创建/修改模型，应保存真实MCP导出，不能手写bbmodel冒充。新素材必须复制进项目，不能依赖制作目录。
- 粒子随机填充三维体积并混合切向运动，避免只沿直线/单圆周；等级依据定义读取，复杂度随等级增加，不凭耗蓝猜等级，不改平衡。
- 循环水滴/火星/治疗种子淡入淡出，冰土生长平滑起止；避免所有部件同相抖动。第一人称近眼避免遮挡，保留距离分级。

## 当前命中、咏唱与手臂参数

命中第1tick初级96/中级160粒；第4tick48/80粒，第8tick40/64粒形成二次扩散，其余前12tick18/30粒，之后分阶段减量消散。粒子沿随机三维方向喷发，混合元素碎屑、大亮点和延迟烟雾；总命中补充预算512粒/tick，新命中优先于旧余烟。补齐中级穿透弹体命中生物、二阶段直线法术撞墙、落石接触与火焰切片撞墙的视觉通知；不改伤害、穿透或碰撞处理。

咏唱降密度：每tick微粒基数初级6/中级12，外围光点基数2/4；乘以0.12+0.88×(0.5+0.5sin(0.18t))²，周期约35tick，低谷稀疏、波峰稍密，各玩家错相。初级通常1–8粒/tick、中级1–16粒/tick，波峰时另每6tick补少量烟雾；咏唱预算192粒/tick。随机三维分布与高等级更复杂保留。

第一/第三人称手臂围绕原施法姿势轻微连续摆动：主轴角偏移不超过约4.64度，偏航约2.01度、侧摆约2.58度，第一人称横向/上下位移不超过0.018格；使用渲染插值时间，无每tick跳变。原版物品使用优先逻辑、左右手选择和银河文字保持不变。

## 分系统预算与资源口径

命中补充512粒/tick、咏唱192粒/tick；实体尾迹/领域256粒/tick、额外风缚48粒/tick，旧有其他粒子另计，不能称为全局总上限。实体16/32格外降频，48格裁剪；咏唱优先本机，12/24格外减量。扩展服务端装饰另按每维度180包/tick、单包≤80粒限制，附近无人不发送；包预算不能与客户端粒数相加比较。

原39层共享9族×2角色×24帧＝432张32px PNG及78个粒子定义，是该层资源数，不是102术全局资源总量。下表记录原39基础风格参数，初始亮点列是旧基础权重，实际命中数量以本文件当前参数及代码等级配置为准，不另发一轮旧粒子。

|法术ID|元素帧族|相对粒子尺寸|初始亮点|补充时长tick|
|---|---|---:|---:|---:|
|water_ball|water|1|7|44|
|fireball|fire|1|7|44|
|wind_ball|wind|1|7|34|
|rock_bullet|earth|1|7|44|
|water_arrow|water|0.7|7|44|
|ice_smash|ice|1|7|34|
|flame_slice|fire|1|7|44|
|healing|life|1|7|34|
|water_shield|water|1|7|44|
|ice_blade|ice|0.7|7|34|
|heat_hand|warm|0.7|7|34|
|heat_island|warm|1|7|34|
|burning_place|fire|1|7|44|
|detoxification|detox|0.7|7|34|
|magic_shield|ward|1|7|34|
|water_cannon|water|1|7|44|
|water_splash|water|1.35|12|44|
|icicle_field|ice|1.35|12|34|
|ex_flame|fire|1.35|12|44|
|sonic_boom|wind|1|7|34|
|stone_cannon|earth|1.35|12|44|
|ice_pillar|ice|1|7|34|
|waterfall|water|1.35|12|44|
|ice_lance|ice|1|7|34|
|icicle_break|ice|1|7|34|
|icicle_edge|ice|1|7|34|
|flood_flush|water|1.35|12|44|
|ice_fortress|ice|1.35|12|34|
|explosion|fire|1.35|12|44|
|flame_pillar|fire|1.35|12|44|
|flamethrower|fire|1|7|44|
|wind_slice|wind|0.7|7|34|
|wind_bind|wind|1|7|34|
|wind_blast|wind|1|7|34|
|earth_lance|earth|1|7|44|
|earth_pillar|earth|1|7|44|
|ex_healing|life|1.35|12|34|
|antidote_ii|detox|1|7|34|
|physical_shield|ward|1|7|34|

## 原39素材溯源

以下是参考映射，不保证每个法术都有直接原作画面；同类推导不能宣称逐面复刻。历史面数仅作导出溯源，不作为后续模型的固定设计约束。

| 样板 | 参考片段与帧（从0开始） | 保留特征 | 有意限制 |
|---|---|---|---|
| 水球 | Waterball_2 48/58/104，Waterball 41/69 | 透明变形水体、断续白色高光、凝聚水滴、命中横向破裂 | 无屏幕折射/真实反射；像素反光只模拟原作明暗组织 |
| 火球 | 火球弹 13/39，Fireball 75 | 明亮浅黄核心、橙色破边外焰、定向尾焰 | 不把片段的爆炸后粉紫光误用为全程火球色；无Bloom承诺 |
| 冰击 | Ice_Smash 45/60/75 | 放射尖锐冰簇、冷蓝凹面、浅色长晶尖 | 动画中为巨型结晶；保留游戏既有单发弹体和碰撞，不擅自改成范围结冰 |
| 石弹 | Rock_Bullet 13/41/69/96 | 矿物大色面、不规则棱角、碎屑 | 此片段弹体细节被动作遮挡，背面与形状是转译，不声称逐面复刻；不借用石炮造型冒充证据 |
| 水盾 | Water_Shield 18/30/45；Water_Shield_2 107/138 | 弯曲连续水膜、断续流动反光、边缘水滴 | 原作可见包裹形水体；适配既有3×2定向水墙，只做前方弧面，不改成全身无敌球 |

WebP统一为40ms/帧，但剪辑包含人物镜头与慢镜。参考时序用于阶段比例，未把整个片段长度强加给游戏技能。



| 魔法 | 元素 | 本地参考（同类推导亦列出） | 批次导出面数（溯源，非新预算） |
|---|---|---|---|
| wind_ball | wind | Sonic_Boom.webp | 211 |
| water_arrow | water | Waterball_3.webp | 183 |
| flame_slice | fire | Flame_Slice.webp | 336 |
| healing | heal | Healing.webp | 479 |
| ice_blade | ice | Icicle_Edge.webp | 27 |
| heat_hand | warm | Heat_Hand.webp | 65 |
| heat_island | warm | Heat_Hand.webp | 186 |
| burning_place | fire | Burning_Place.webp | 400 |
| detoxification | detox | Healing_2.webp | 425 |
| magic_shield | barrier | Physical_Shield.webp | 380 |
| water_cannon | water | Waterball_3.webp | 248 |
| water_splash | water | Water_Splash.webp | 394 |
| icicle_field | ice | Icicle_Field.webp | 445 |
| ex_flame | fire | Ex-Flame.webp | 499 |
| sonic_boom | wind | Sonic_Boom.webp | 424 |
| stone_cannon | earth | Stone_Cannon_2.webp | 309 |
| ice_pillar | ice | Ice_Smash.webp | 76 |
| waterfall | water | Flood_Flush.webp | 448 |
| ice_lance | ice | Ice_Smash.webp | 49 |
| icicle_break | ice | Icicle_Break.webp | 94 |
| icicle_edge | ice | Icicle_Edge.webp | 169 |
| flood_flush | water | Flood_Flush.webp | 448 |
| ice_fortress | ice | Frost_Nova.webp | 136 |
| explosion | fire | Exodus_Flame_2.webp | 931 |
| flame_pillar | fire | Burning_Place.webp | 422 |
| flamethrower | fire | Flamethrower.webp | 209 |
| wind_slice | wind | Sonic_Boom.webp | 176 |
| wind_bind | wind | Tornado.webp | 340 |
| wind_blast | wind | Wind_Blast.webp | 240 |
| earth_lance | earth | Earth_Lance.webp | 154 |
| earth_pillar | earth | Earth_Pillar.webp | 220 |
| ex_healing | heal | Healing_2.webp | 790 |
| antidote_ii | detox | X-Healing.webp | 682 |
| physical_shield | purple | Physical_Shield.webp | 76 |

## 验证与当前状态

截至2026-11-11：102个独立法术/能力（原39＋扩展63）已接入代码、资源和教材；冰枪→吹雪为顺序组合，不另计法术数。最近一次离线构建通过，verifyMagic为15,658项行为断言；40类动态网格1,200边界采样及21种网格6,300离线采样通过。这些是既有验证记录，本次文档整理没有重跑构建。实际游戏视觉、Mixin加载、联机、权限兼容及性能仍待验收，用户当前要求暂不启动游戏。

离线软件渲染不是游戏截图，Blockbench截图不能证明世界光照、透明排序或帧率。实机还需覆盖明暗背景、正侧后轮廓、上下瞄准尾焰、手臂/持物遮挡、墙地命中、护盾拦截、风带解除和多人密集施法；未测试不得标通过。
