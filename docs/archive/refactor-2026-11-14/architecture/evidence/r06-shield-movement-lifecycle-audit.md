# 物理障壁移动与生命周期源码审计（2026-11-13）

本证据为源码核对，真实Fabric事件/Mixin、碰撞、存档重载、视觉、联机、性能均NOT_RUN。

## 候选与命中

PhaseTwoRuntime.initialize保留伤害源魔法弹体排除、物理弹体标签/生物直击资格及origin非空；PhysicalShieldSelection.firstIntersecting按原HashMap.values顺序返回首个有限contact，不排序、不取最近、不新增地形射线。近零源目标距离仍在每个合格候选查询内拒绝。

intercept保留ServerLevel、魔法/移除/速度阈值前置检查；候选按原顺序短路检查移除、世界身份、owner身份（双方null也排除）、方向dot>=0。nearest严格<比较，NaN和正无穷不选中，负无穷仍按历史比较接受；同距首个不替换。几何继续使用反向单位运动、原体积和本tick长度。

## 移动

MagicMovementMixin仍调用PhaseTwoRuntime.clipShieldMovement，然后ExpansionBarriers和CoreFields；未调整混合碰撞顺序。短运动<1E-8、Projectile、旁观者返回原运动。世界查询仍为扩大运动包围盒再inflate(0.1)，只取shield实体；无额外owner豁免。相交时PhaseTwoRules.shieldOverlapMotion允许从原侧退出，否则移除法向分量并立即返回；未相交时取最小contact，最终以max(0,first-0.001)截断。该世界循环和重叠纯公式尚待迁出兼容类，不能记整术全部迁移。

## 生命周期

PhaseTwoEntity.tick先super/updateBounds；服务器低于minY移除，当前包围盒未加载则暂停；stationary且owner非ServerPlayer时返回，年龄不增长。寿命在++age之前判断，physical_shield按age>=remaining退出；正常盾tick仅增加age并检查owner距anchor平方>1E-6时永久打断续期，不跟随玩家移动。renew仍增加remaining160并重置容量16，不重置age。

MagicSystem的release/stop/select/toggle及使用物品/死亡路径打断maintenance，保留盾至寿命/容量耗尽或替换；tick在盾已移除、异世界或玩家死亡时discard并从registry删除。LEAVE、AFTER_RESPAWN和维度变化调用clear，停服逐玩家clear后clearChannels移除服务器容器。PhaseTwoEntity.shouldBeSaved对shield返回false，无新增持久化/同步字段。死亡瞬间到服务器玩家tick清理的事件时序仍需真实游戏验证。

后续更新：原侧退出、接触比较与退让缩放已迁PhysicalShieldMovement；PhaseTwoRules保留世界向量投影，PhaseTwoRuntime保留查询循环与重叠立即返回。20,000组标量差分通过，不等价实际Minecraft向量/碰撞事件集成测试。

后续更新：ALLOW_DAMAGE与弹体/移动循环已迁PhysicalShieldCollisionAdapter；原PhaseTwoRuntime入口为委托。附件所有权移至PhysicalShieldRuntimeAdapter，键/初始化/清理次序不变，仅单处注册；释放和玩家生命周期协调尚待迁移。

后续更新：释放/续期/maintenance/player tick/clear现已迁PhysicalShieldRuntimeAdapter。原PhaseTwoRuntime tick在columns/burns后调用护盾tick，clear在columns/burns移除后调用护盾clear，contact及通道顺序不变；SelfEffect直接接新release。此为源码及构建证据，真实事件验收仍NOT_RUN。
