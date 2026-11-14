# 突风结构退出检查（2026-11-13）

结论：核心/协调/适配结构SLICE_DONE；整体重构和世界验收未完成。

intent ALL_MAGIC_EXPANSION.md突风与原有效定义保持16MP/4火伤、10格半角30度、0.6冲量。FULL rank2经CastingTiming核对为160准备/10恢复，未以构造参数代替有效值。BlastRules保留Math.cos(Math.PI/6)、length<1E-6近原点短路、Vec3.normalize的1E-5方向阈值及IEEE比较；不与35度爆音冲击波混用。BlastHit保留offset平方<1E-6才用准星方向的独立阈值，hurt成功后才判免疫，非免疫施冲量并标hurtMarked，免疫仍afterHit；无MagicEffects.apply或点燃。

BlastAdapter实现现有SelfEffect，世界查询仍眼部中心、排除owner、原allowed/PVP、目标包围盒中心锥形与visible，来源MagicAreaEntity configure但不spawn，全部目标处理后burst，空目标true。没有地形、更改方块、施术者反冲、独立tick/容器/保存字段。SelfEffectComposition显式装配既有Command/Preparing/Observer；MagicPreparation.selfCommand执行后return，不重复付款。ThermalGust.cast仅BLAST提前委托，BORA_BLAST原路径、contains及寒冷状态顺序保持未迁。共享ThermalGust不能删除，待bora_blast迁后再审计零引用；共享MagicPreparation容器仍属后续职责。

17项新增模块合同：3锥形（含10,000冻结算式随机差分）、6命中、6准备/付款、2真实Hit与付款组合。保持160准备无付款、16付款后效果/10恢复、单次提交、取消、输入/MP/知识拒绝、false恢复、观察者隔离；伤害拒绝仍释放付费、部分冲量异常保留付款且不重放。最终build退出0，63单测/387模块测试，旧15,658断言、102有效定义/教材/资源/JAR检查通过，证据r06-blast-build.log和JUnit汇总/XML。

测试只执行JDK核心和窄端口测试桩；真实Adapter/PVP/伤害/免疫/权限、Fabric/Mixin、Codec重载、游戏视觉/联机/性能均NOT_RUN。源码审计不当世界验收；未启动游戏、未Git操作。下一R06-bora_blast。
