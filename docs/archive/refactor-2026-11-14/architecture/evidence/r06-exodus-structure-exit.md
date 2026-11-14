# 狱炎火弹结构切片退出核对

2026-11-13：仅核心/准备/释放/爆发适配结构SLICE_DONE，非整术或游戏验收。

Spec/Rules/Hit/Flight已接生产入口；ProjectileComposition显式绑定请求准星，MagicPreparation的command执行后立即return，旧SpellExecutors/IntermediateSpells只保留未走新命令时的兼容入口，不双扣/双发。ExodusFlameBurstAdapter使用载体原hit Set，去重/自燃/附效顺序有合同。最新完整build及102有效定义/资源/JAR校验通过。

残留归属：IntermediateProjectile仍有3x3x3体积选择、飞行世界扫掠、age/traveled、墙流体动作和命中Codec；这些属于共享弹体载体与世界几何适配后续迁移，不因单术切片通过而称已清除批次职责。MagicSystem.EXODUS_FLAME注册与MagicVictims/MagicAge等键不可删除；R10须在实体策略及调用链全迁后再证明零引用。旧exodusBurst仅委托，不另持历史。领域/客户端不在该切片范围。

游戏/Fabric/Mixin/伤害事件/Codec实际重载/视觉/联机/性能NOT_RUN。返回R06账本下一NOT_STARTED tornado_impact。
