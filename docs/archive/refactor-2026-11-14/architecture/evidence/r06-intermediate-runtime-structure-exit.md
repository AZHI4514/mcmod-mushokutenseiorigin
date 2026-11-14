# R06-intermediate-runtime 结构退出审计（2026-11-13）

本共享运行时切片 STRUCTURE_SLICE_DONE，非整阶段 R06 DONE。

- IntermediateProjectile.tick：super/removed、客户端轨迹与移动仍是游戏实体载体职责；服务端寿命/几何及单步适配已交 IntermediateStepAdapter。
- 纯协调：ProjectileServerStep、ContactCandidates、ContactTraversal、ProjectileTermination、SolidContacts、SweptChunkGate 和 FluidContactScan 均已有独立核心合同。
- 保存：实体继续持有私有 age、expansionBroken、paid 与原 hit Set；ValueInput/Output 和父类调用保留载体。纯归一化与 UUID 格式交 SavedProjectileCounters、StoneCannonRules、HitHistoryFormat；MagicVictims/Codec.STRING.listOf 和逐字段立即赋值不变。traveled 不新增保存或寿命约束。
- 世界差异：IntermediateStepAdapter 的狱炎爆发、风枪地形、石炮伤害、截面选择等明确委托各已有单术规则/Adapter；共享几何与世界 API 枚举属于基础设施适配，不声称完全消除法术选择分支。最终 R10 仍需审计兼容命名和零调用路径。
- 未完成归属：IntermediateSpells.execute/hurt/blockImpact/visible 聚合入口交 R06-intermediate-effects，PhaseTwo 扫掠/效果、通道、发布与准备资格仍按残余矩阵独立推进。

实际 Codec 重载、Fabric/世界/事件/视觉/联机/性能均 NOT_RUN；按用户要求全部重构完成后统一实机验收。
