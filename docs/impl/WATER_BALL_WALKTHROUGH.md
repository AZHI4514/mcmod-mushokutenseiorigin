# 用水弹掌握一条完整链路

## 1. 玩家要的是什么

原作资料描述“生成并投掷水块，最入门攻击术”。设计希望它是可辨认的入门水系弹体，而不是瞬时射线。来源见[原作资料](../reference/LORE.md)。这是intent，不是代码测试结论。

## 2. 游戏规定是什么

看[水弹设计卡](../spec/magic/spells/water_ball.md)：1MP、1伤害、初速1.5、重力0.03、不设时间或距离上限、成功伤害后60%湿身5秒。咏唱由施法合同提供；能从哪本书学由学习合同决定。改水弹伤害只改此卡和对应代码/测试，不去改魔法书设计。

## 3. 代码在哪里做

1. [WaterBallSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_ball/WaterBallSpec.java)保存纯参数。
2. [ProjectileComposition](../../src/main/java/com/example/examplemod/bootstrap/ProjectileComposition.java)把水弹规格、Command、玩家端口和实体生成器连接起来。
3. [WaterBallEntity](../../src/main/java/com/example/examplemod/magic/WaterBallEntity.java)处理真实世界弹体；[WaterBallRenderer](../../src/client/java/com/example/examplemod/client/magic/WaterBallRenderer.java)负责显示。
4. [水弹实现索引](SPELL_INDEX.md#water_ball)定位本术行为测试。测试运行结果属于STATUS和日志，不写在此叙述里。

注意：旧定义中的lifetimeTicks字段不一定是实际飞行上限；不能看一个常量就推翻设计。发现差异先查调用链和[差异登记](../reference/SPEC_DEVIATIONS.md)，再决定是否修复。

## 4. 你如何验收

能否学习是学习验收；1MP/伤害/运动/湿身是魔法验收；尾迹与轮廓是视觉验收；重进后知识保存是玩家数据验收。每项分开判断，构建成功不能代替在游戏里看到它正常工作。
