# 39 种魔法特效交付

本轮完成其余 34 种的 Blockbench 网格、像素材质和 Java 动画，接入全部 39 种的实体、蓄力、持续区域、命中显示入口。前五种认可版本保留。

银河字母每 4 个服务器 tick 发出一个，沿用左眼位置、斜向上漂浮和渐隐。未改变伤害、碰撞、耗魔或施法逻辑。

## 设计与验证边界

依据本地 WebP 的轮廓、色彩与运动方向建模；参考名称存在错位时使用同元素素材推导，不声称每种都有原作直接参考。Wiki 连接失败，未将未取得的内容当成设定依据。水/风采用透明流层，火焰采用收尖火舌与热核，冰/土使用实体棱面，治疗/解毒使用绿金光点和光丝。没有增加屏幕折射或 Bloom 着色器。

瀑布按当前每 20 tick 更新的施法事件显示下落水幕；炎柱显示初始 20 tick 火柱，随后沿用游戏本身燃烧。炎柱不会假装跟随移动目标。风缚随实际状态绘制，护盾和持续区域避免重复命中模型。

所有模型通过运行中的 Blockbench MCP 导出。runtime-39.jpg 是真实 Java 输出顶点的离线软件渲染，并非 Minecraft 截图；blockbench-34.jpg 是编辑器截图。离线检查覆盖 39 种×3 阶段×16 时间点，检查顶点、UV、透明度、法线、边界、提交数量、消失时机，并逐顶点比较原五种。完整 Gradle 构建另行记录。尚未启动 Minecraft，因此透明排序、世界光照和实际运行帧率需游戏内复核。

## 本轮模型与素材对应

| 魔法 | 元素 | 本地参考（同类推导亦列出） | 网格面数 |
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
