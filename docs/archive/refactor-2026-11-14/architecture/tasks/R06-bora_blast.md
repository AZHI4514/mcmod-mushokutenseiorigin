# R06-bora_blast：SLICE_DONE（核心/协调/适配；世界待验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`bora_blast`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


寒冷突风；ALL_MAGIC_EXPANSION.md：38MP、20格/30度锥体8冰伤、朝外0.7冲量、冻伤300tick、湿身额外减速II100tick；温暖抵消冻结并消耗但不挡伤害，不伤施术者。FULL rank3为240准备/20恢复，先核对有效定义快照。

旧入口SpellExecutors→ThermalGust.cast，BLAST已转独立Adapter，剩余寒冷分支待迁。必须保持hurt成功→免疫/冲量→读取warm并移除或canFreeze/冻结→重新读取wet/减速→afterHit→全部目标后burst；warm/wet在伤害与冲量之后读取，不套用FrostNova的伤害前快照。冲量offset平方<1E-6才用准星方向。无标准点燃、无方块破坏、无独立tick/持久任务。

目标独立BoraBlast规则/命中协调/世界Adapter及现有SelfEffect Command/Preparing，不造空Factory或通用解释器。允许bora_blast core/adapter/tests、ThermalGust、SelfEffectComposition及文档；如共享热风锥数学，只抽有实际双方调用的几何函数，不让单术互相依赖，不改变Math.cos(PI/6)与原版归一化/IEEE行为。先审计ThermalGust.contains在verifyMagic或其他调用，再决定兼容入口保留。

最小合同：伤害拒绝、免疫仅挡冲量、温暖消费/冻结能力、伤害或冲量期间状态变化、湿身额外减速、状态异常截断、38MP付费前置/单次/取消/部分异常、几何边界。无新增Codec，ID/资源/教材/有效值保持；世界/Fabric/Mixin/视觉/联机/性能NOT_RUN，不游戏、不Git。

下一精确动作：核对原寒冷路径与正式规格/240准备20恢复，提取伤害后温暖/湿身读取顺序的核心命中协调，再迁世界Adapter和SelfEffect装配，补准备付款/部分状态异常组合合同；完成后核对R06剩余项和R07入口，不能直接宣称R06整体完成。

# 寒冷突风结构退出核对（2026-11-13）

结构SLICE_DONE，仅核心/协调/世界适配/准备释放切片，不宣称整个R06或项目完成。

正式ALL_MAGIC_EXPANSION.md与SpellDefinition.BORA_BLAST保持38MP、8冰伤、20格半角30度、0.7冲量、冻结300tick、湿身减速II100tick。rank3 FULL为240准备/20恢复；有效定义基线未变。BoraBlastRules保留Math.cos(PI/6)、近原点length<1E-6、方向normalize的1E-5阈值与非有限IEEE比较。命中冲量独立使用offset平方<1E-6的原阈值。

BoraBlastHit顺序：hurt成功→免疫判定/非免疫冲量→读取warm，真则removeWarm，假才canFreeze/冻结300→重新读取wet，真则slow(100,1)→afterHit。不是FrostNova伤害前状态快照；伤害与冲量期间状态变化可见，温暖消费之后的湿身变化也可见。免疫只挡冲量，冻结能力只挡冻结；伤害false不读状态。异常截断，不回滚已经产生的伤害、冲量、状态消费。

BoraBlastAdapter持原玩家，实现SelfEffect；原眼部查询、排除owner、MagicEffects.allowed、目标中心锥形及visible、原伤害源和状态API保持。MagicAreaEntity configure/setPos但不spawn，仅作为来源；遍历目标后burst，空目标true。无独立tick/容器/保存、方块破坏或标准点燃。SelfEffectComposition显式绑定既有Command/Preparing/Observer；MagicPreparation.selfCommand执行后return，不进旧付款分支。未知部分副作用异常保留付款，显式false才恢复状态（真实Adapter正常返回true）。

ThermalGust.cast的BORA_BLAST与BLAST各提前委托独立Adapter；保留其其他输入旧fallback行为，不擅自扩大新适配器输入域。contains仍被MagicStateTest调用，不能声称零引用而删除；R10应先审计所有调用再清理。fallback不再是已注册两种热风的运行路径，但仍是遗留业务残留，后续须明确迁移/退出，不能把兼容类称已删。两个单术规则当前各自拥有几何，没有为本轮强行引入通用解释器/新模式。

新增21项模块合同：3几何含10,000随机冻结算式差分、10命中、6准备/付款、2真实Hit/付款组合。覆盖暖/湿读取时点、冻结能力、免疫、阈值、状态异常、240准备/38付款/20恢复、单次/取消/拒绝/false恢复/观察者隔离、部分消费异常保留付款且不重放。完整离线build退出0：63单测/408模块测试、旧15,658断言、102有效定义/教材、资源/JAR与架构边界通过。证据r06-bora-build.log和JUnit汇总/XML。

这些是纯核心/端口合同，不是Minecraft世界/Fabric/Mixin/事件/Codec/视觉/联机/性能实测，上述均NOT_RUN。不启动游戏、不Git操作。下一为R06结构退出审计：账本逐术切片不代表共享载体/容器已经满足阶段退出标准，先核对残余再决定R07入口。
