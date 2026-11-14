# R06-blast：SLICE_DONE（核心/协调/适配；世界待验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`blast`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


SpellId blast；正式名突风，唯一阶段R06，前置heat_hand/wind_blast。ALL_MAGIC_EXPANSION.md：16MP、长度10/半角30度热风锥一次4火伤、朝外0.6冲量；无标准点燃、不伤施术者、不破块、不放火、不施术者反冲。先核对构造后的有效准备/恢复值，不把构造参数当有效恢复。

旧实现ThermalGust.contains/cast同时服务blast与bora_blast；本术只迁热风路径，保留寒冷路径及共享入口，不提前宣称bora_blast完成。旧代码沿眼部查询排除owner/PVP，contains与visible后伤害；成功且非免疫才加冲量（offset平方<1E-6用aim.direction，否则normalize），afterHit后最后burst。该平方阈值不同于sonic_blast的.001，不可套用。热风无标准点燃或额外MagicEffects.apply。

目标blast独立纯规则/命中协调和世界Adapter，复用SelfEffect端口、Command、Preparing/Idle与成功Observer；不造空Factory，不新建通用效果解释器，不借重构改变数值。允许blast core/tests、ThermalGust及Adapter、SelfEffectComposition、SpellDefinition和文档；仅必要时调整共享热风锥几何，须保持bora路径行为。

最小合同：长度/角度/近零/方向归一化、成功/拒绝/免疫/异常、16MP单次付款及准备/取消/失败语义、无多余点燃/附效。无独立持久状态或tick，不新增Codec。保留注册/资源/教材ID。世界/GameTest/视觉/联机/性能NOT_RUN，不启动游戏、不Git操作。

下一精确动作：核对blast正式规格、有效mana及准备/恢复快照，再按原Math.cos(Math.PI/6)提纯规则，保留1E-6距离短路与1E-6平方冲量阈值、原Vec3归一化语义；随后迁热风逐目标协调及Adapter/付费组合合同。当前仅审计，不借用sonic_blast测试作为blast通过证据。下一SpellId bora_blast。

# 突风结构退出检查（2026-11-13）

结论：核心/协调/适配结构SLICE_DONE；整体重构和世界验收未完成。

intent ALL_MAGIC_EXPANSION.md突风与原有效定义保持16MP/4火伤、10格半角30度、0.6冲量。FULL rank2经CastingTiming核对为160准备/10恢复，未以构造参数代替有效值。BlastRules保留Math.cos(Math.PI/6)、length<1E-6近原点短路、Vec3.normalize的1E-5方向阈值及IEEE比较；不与35度爆音冲击波混用。BlastHit保留offset平方<1E-6才用准星方向的独立阈值，hurt成功后才判免疫，非免疫施冲量并标hurtMarked，免疫仍afterHit；无MagicEffects.apply或点燃。

BlastAdapter实现现有SelfEffect，世界查询仍眼部中心、排除owner、原allowed/PVP、目标包围盒中心锥形与visible，来源MagicAreaEntity configure但不spawn，全部目标处理后burst，空目标true。没有地形、更改方块、施术者反冲、独立tick/容器/保存字段。SelfEffectComposition显式装配既有Command/Preparing/Observer；MagicPreparation.selfCommand执行后return，不重复付款。ThermalGust.cast仅BLAST提前委托，BORA_BLAST原路径、contains及寒冷状态顺序保持未迁。共享ThermalGust不能删除，待bora_blast迁后再审计零引用；共享MagicPreparation容器仍属后续职责。

17项新增模块合同：3锥形（含10,000冻结算式随机差分）、6命中、6准备/付款、2真实Hit与付款组合。保持160准备无付款、16付款后效果/10恢复、单次提交、取消、输入/MP/知识拒绝、false恢复、观察者隔离；伤害拒绝仍释放付费、部分冲量异常保留付款且不重放。最终build退出0，63单测/387模块测试，旧15,658断言、102有效定义/教材/资源/JAR检查通过，证据r06-blast-build.log和JUnit汇总/XML。

测试只执行JDK核心和窄端口测试桩；真实Adapter/PVP/伤害/免疫/权限、Fabric/Mixin、Codec重载、游戏视觉/联机/性能均NOT_RUN。源码审计不当世界验收；未启动游戏、未Git操作。下一R06-bora_blast。
