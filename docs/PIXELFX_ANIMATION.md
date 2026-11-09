# PixelFX 像素氛围动画

本轮保留39种已确认的网格、朝向、碰撞和游戏数值，升级客户端粒子及动作时间线。

## 参考与移植范围

- https://github.com/patch-gg/pixelfx ：使用其 core 像素绘制器及 fire/_lib.js 的 smokePuff、firePuff、ember 烘焙像素素材；参考 fire-breath、big-explosion、frost-nova、heal、boulder 的先爆发后扩散消散分层。
- https://github.com/winkidney/pixelfx-godot ：参考 PixelFx2D 的共享 BAKED 帧缓存思路；没有将 Godot 运行时或着色器带入 Fabric。
- 两项目 MIT 许可证随模组打包于 META-INF/licenses。此实现使用颜色帧，未实现 Godot 的独立 additive light 缓冲。

## 动画组织

命中第1tick喷出7–12个元素亮点；前8tick补充碎屑；第5tick起混入冷却烟雾/水雾/尘土；尾段每3tick少量残留，34–44tick结束。单个烟雾存活30–44tick，亮点14–22tick；像素帧不做模糊缩放。烟雾使用环境照明，亮点使用发光照明。

附近弹体沿运动轨迹分段发射尾迹；领域在实体可视范围内散布。火焰上浮冷却，水/冰略下沉，土尘沉降，风使用轻薄风弧，治疗/解毒/结界使用对应配色光点。手边增加低频亮点并略微扩大分散范围。循环水滴、火星和治疗种子在重置前后淡出，冰土生长改为平滑起止，部分共振改为异相波动，保留实体模型刚性。

性能边界：持续效果每tick最多64粒、最多64个发射体；命中队列最多32条、每tick最多56粒；16/32格距离分级降低发射频率，超过48格不显示命中补充。手部和旧有原版发射逻辑另计，不将此限制误称为全局粒子上限。新增资源9族×2角色×24帧=432张32px PNG，在39种魔法间共享；新增78个粒子定义。

银河文字仍每4tick一字，从左眼斜向上渐隐。伤害、穿透、强风反冲/缓降与燃烧领域规则不变。

## 逐法术氛围配置

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

## 验证边界

离线粒子预览仅验证烘焙素材与生命周期，并非游戏实录；实际透明排序、多人密集场景和第一人称遮挡需游戏内验收。没有自动启动游戏。


## 等级化高密度补充（以此节覆盖上文旧密度）

读取 SpellDefinition.rank()，目前39种魔法分初级/中级两档，不根据耗蓝量猜等级。近距离咏唱每tick：初级12个聚集微粒+4个外围亮点，另每2tick补1个烟雾/细光点；中级24+8，另每2tick补3个烟雾/细光点。聚集微粒延长为12–16tick。两档随机体积半径约0.62/0.82格，外围亮点带更强切向旋流；近眼0.22格不生成，以降低第一人称遮挡。大部分粒子填充球体积而非一条弹道/一个圆周；独立随机位置和速度保留立体感。

持续实体近处每tick初级10/中级15粒，领域另加4粒；沿上一段速度随机取样后，再叠加随机三维偏移。命中首次喷射按原7–12粒基础乘(2+等级)，前8tick每tick8/16粒，残留期每3tick4/8粒。风缚外围每2tick6个随机高度风屑，已有环身风带保留。

分系统每tick上限：咏唱384、实体尾迹/领域256、命中224、额外风缚48；旧有其他粒子另计。咏唱优先本机玩家；12/24格外降密度。实体16/32格外降频降密度，保留48格裁剪。高等级通过数量、分布范围、外围层及运动层次增强复杂度，不改变游戏数值。


### 命中爆发优先、咏唱波动与手臂微动（覆盖之前密度）

命中第1tick初级96/中级160粒；第4tick48/80粒，第8tick40/64粒形成二次扩散，其余前12tick18/30粒，之后分阶段减量消散。粒子沿随机三维方向喷发，混合元素碎屑、大亮点和延迟烟雾；总命中补充预算512粒/tick，新命中优先于旧余烟。补齐中级穿透弹体命中生物、二阶段直线法术撞墙、落石接触与火焰切片撞墙的视觉通知；不改伤害、穿透或碰撞处理。

咏唱降密度：每tick微粒基数初级6/中级12，外围光点基数2/4；乘以0.12+0.88×(0.5+0.5sin(0.18t))²，周期约35tick，低谷稀疏、波峰稍密，各玩家错相。初级通常1–8粒/tick、中级1–16粒/tick，波峰时另每6tick补少量烟雾；咏唱预算192粒/tick。随机三维分布与高等级更复杂保留。

第一/第三人称手臂围绕原施法姿势轻微连续摆动：主轴角偏移不超过约1.61度，另外两轴约0.8/0.69度，第一人称上下偏移不超过0.006格；使用渲染插值时间，无每tick跳变。原版物品使用优先逻辑、左右手选择和银河文字保持不变。
