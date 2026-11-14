# R06-sonic_blast：SLICE_DONE（核心/协调/适配；世界待验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`sonic_blast`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


SpellId sonic_blast；intent ALL_MAGIC_EXPANSION.md及SpellDefinition有效值快照，先逐项核对正式数值，不以旧代码覆盖设定。前置R05/R06；旧SonicBlast.contains/cast、SpellExecutors、MagicPreparation；目标单术Spec/锥形纯规则/命中与地形协调/世界Adapter、现有SelfEffect Command与Preparing，不造空Factory。

已读旧实现：眼部16格半角35度锥，伤害成功且非MagicControl.immune才1.2冲量（近零offset用准星方向）；afterHit；永久地形先查询已加载锥形格、按距离排序并快照射线可见目标，再破坏最多128个成功方块，最后burst。该行为尚未迁移；必须先核对intent与有效定义。

允许sonic_blast core/tests、SonicBlast/Adapter/SelfEffectComposition/SpellDefinition及文档。契约保留目标排除owner、PVP、可见性、冲量近零分支、状态/费用/失败与取消、权限/掉落及原ID/存档兼容。无独立持久任务。最小测试锥形边界/归一化/近零、成功伤害与免疫、地形成功计数/可见快照、准备付款/取消/部分异常。

命令gradlew.bat build --offline；当前尚未执行本术迁移测试，不借用上一术PASS。上一构建r06-frost-final-build.log退出0。回撤仅当前差分，不动用户修改。下一精确动作核对正式sonic_blast规格及构造后MP/计时，再抽contains纯规则并保持世界Vec3归一化语义。下一SpellId blast。游戏验收NOT_RUN，不启动游戏、不Git操作。

## 爆音冲击波锥形规则（2026-11-13）

正式规格ALL_MAGIC_EXPANSION.md与有效定义核对：40MP、12伤害、16格半角35度、1.2冲量，rank3 FULL为240准备/20恢复。SonicBlastRules接旧contains入口，纯标量运算保留Vec3归一化1E-5阈值、近原点length<1E-6短路、16含边界和IEEE非有限比较；未引入Vec3d有限值拒绝。3项模块合同及10,000组冻结算式随机差分通过。未改命中、永久地形128成功预算、资源、注册或保存ID。

完整离线build退出0，63单测/352模块测试；证据r06-sonic-rules-build.log与JUnit汇总/XML。测试为核心规则合同，非实际世界/事件/Codec/视觉/联机/性能验收，以上均NOT_RUN。R06-sonic_blast仍IN_PROGRESS。

下一精确动作：提取伤害成功→免疫→1.2冲量→afterHit协调（offset平方<.001用原方向）；再提取先完整可见快照、后最多128成功破坏且false继续的地形协调，迁独立Adapter并接SelfEffect准备付款合同。不启动游戏，不Git操作。

# 爆音冲击波结构退出核对（2026-11-13）

结论：仅核心/准备/释放/世界适配结构SLICE_DONE，不是全项目重构或游戏验收完成。

## intent与数值

ALL_MAGIC_EXPANSION.md爆音冲击波及SpellDefinition.SONIC_BLAST保持40MP、12伤害、16格/35度锥体、1.2冲量；有效FULL rank3为240准备/20恢复。永久模式最多128次成功破坏石制工具可采方块，仍由ExpansionTerrain处理原权限与掉落。未改定义、资源、注册、网络或保存ID，102有效定义及教材基线通过。

## 调用和生命周期

SelfEffectComposition显式绑定SonicBlastAdapter，MagicPreparation的Pending持Preparing/Idle上下文与CastSelfEffectCommand，selfCommand执行后立即return，绕过旧泛型付款/执行分支。旧SpellExecutors→SonicBlast.cast仅委托同一Adapter，不单独付款；Adapter直接用SonicBlastRules，不反向调用兼容门面。R10须在证明旧入口零引用后再删除兼容类。

释放前保留MagicPreparation的资格、心跳、维度及cancel检查；释放同步完成，无独立tick、任务容器或保存状态。MagicAreaEntity仍configure/setPos但不addFreshEntity，只作真实伤害来源。未改共享MagicPreparation全局容器，它仍属后续架构迁移职责。

世界层仍以眼部为起点、query排除owner、原MagicEffects.allowed/PVP、目标包围盒中心锥形判定及IntermediateSpells.visible过滤。SonicBlastHit按hurt成功→MagicControl.immune→非免疫冲量→afterHit。offset平方<.001时用原准星方向乘1.2，否则offset.normalize乘1.2，标hurtMarked。免疫不跳过afterHit；伤害false不读免疫或冲量；异常向上传播，不回滚已发生伤害/运动。

永久地形先收集已加载锥形方块，保留BlockPos枚举和原稳定距离排序，无新增同距排序。SonicBlastTerrain先对完整候选作射线可见快照，之后才break；false继续，只有成功计数达到128才停止，不能复用风枪龙卷的首失败停止算法。原射线COLLIDER/NONE和破坏权限API保留。目标→地形→burst顺序、空目标也返回true均保持。

## 验证与限制

新增18项模块合同：命中6、地形6、付款/真实核心组合6。覆盖免疫、伤害拒绝、严格近零阈值、异常截断、先快照/后破坏、128成功上限/失败继续、准备240不扣费、40付款前置/20恢复、单次提交、取消、输入/知识/MP拒绝、显式false恢复、未知部分地形异常保留付款及观察者隔离。另有已通过的3项锥形规则合同和10,000差分。

初次构建因测试匿名类继承events字段遮蔽外部记录变量，导致事件记录断言失败；修正测试局部名为waveEvents，未改生产逻辑。原失败日志r06-sonic-initial-test-failure.log；最终r06-sonic-exit-build.log退出0，63单测/370模块测试，旧15,658断言、102定义/教材、资源/JAR、纯核心classpath及服务端/客户端边界通过。

合同使用真实核心协调器和窄端口测试桩；不执行真实Minecraft世界、Fabric/Mixin、伤害事件、PVP、权限/掉落、Codec重载、视觉、联机或性能，上述均NOT_RUN。付款合同内的目标/地形/burst顺序是端口组合合同，真实Adapter顺序另经源码核对，不冒充世界集成。下一账本项R06-blast。
