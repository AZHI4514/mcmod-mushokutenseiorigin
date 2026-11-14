# R06-wind_blast：SLICE_DONE（结构切片；世界未验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`wind_blast`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


正式第34术：8MP；无目标、自身单次反向弹射、100tick缓降。有效准备/恢复参数下一步从构造后profile核对，不采用历史构造值直接推断。

## 风缚结构切片收尾与强风规则提取（2026-11-12）

风缚准备/释放/效果与脉冲切片记SLICE_DONE，源码边界审计见evidence/r06-wind-bind-exit-audit.md；原版药水保存/调度、共享移动Mixin与客户端仍非实际运行验收。风裂D-006继续未决。

当前R06-wind_blast IN_PROGRESS：WindBlastRules提取单次弹射纯函数，PhaseTwoRules兼容入口委托。保持准星反方向由调用者传入、水平0.9/抬升0.42、向上48轮二分反解、向下-1.2比例及100tick缓降；零向量/1E-5归一化边界保持。共享launchVelocity用于其他法术的函数未改。10,000方向冻结差分和水平/竖直/微小向量合同通过；完整离线构建退出0，63单测/198模块测试、旧15,658断言、102定义教材及资源/JAR检查通过。证据r06-wind-blast-rules-build.log及JUnit汇总。

下一精确动作：提取强风自身施加效果端口和Adapter，复用合适的准备/单次释放Command，不能伪造需要目标的locate；保留一次launch→缓降的顺序、原版速度包、忽略addEffect返回值的既有成功语义以及重复施法重设速度。核对有效准备/恢复参数再接线。不启动游戏、不自动Git操作，世界/视觉/联机/性能NOT_RUN。

## 强风自身释放检查点（2026-11-12）

构造后冻结基线确认160tick准备、8MP、10tick恢复。新增SelfEffect窄端口和CastSelfEffectCommand，避免将无目标术伪装成LocalEffect选点；复用Preparing/Idle与成功Observer顺序。SelfEffectComposition显式绑定强风，MagicPreparation接入独立命令；兼容PhaseTwoSpells.execute复用WindBlastAdapter。没有新增Factory或通用解释器。

Adapter先反向一次launch，再尝试100tick缓降，保持addEffect返回false仍成功的旧语义。launch仍setOnGround(false)、setDeltaMovement、hurtMarked及一次原版速度包；不加推进队列。明确false可回滚完整玩家状态，未知部分异常保留付款，单次命令不可重放。观察者按恢复→组合→发光→音效执行且异常隔离。

完整离线build退出0；63单测/204模块测试、旧15,658行为断言及102定义/教材、资源/JAR检查通过。新增6项核心合同验证160tick准备、参数/资格拒绝、单次扣费与通知顺序、false退款、部分异常和取消。Adapter真实速度包/原版药水及游戏视觉仍NOT_RUN，测试替身的launch/slowfall记录仅证明端口调用约定，不冒充世界效果验证。证据r06-wind-blast-release-build.log及JUnit汇总/XML。

当前R06-wind_blast仍IN_PROGRESS。下一精确动作：补强风纯效果顺序协调的独立合同（包括缓降拒绝仍成功及部分异常），审计兼容入口与重复施法/生命周期边界后收尾结构切片，再建立earth_lance任务卡。游戏/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 强风结构切片收尾（2026-11-12）

WindBlastEffect将反向弹射→100tick缓降的顺序接入真实Adapter。缓降拒绝仍成功、launch异常不刷新、缓降异常不撤销已发速度、重复施法固定时长请求的4项合同通过。清理SelfEffectComposition遗留无用import和日志措辞。源码兼容/无持续任务审计见evidence/r06-wind-blast-exit-audit.md；原版更强/更长药水合并语义保留，不承诺强制覆盖。

完整离线build退出0，63单测/208模块测试、旧15,658行为断言、102有效定义/教材及资源/JAR检查通过。日志r06-wind-blast-effect-build.log及JUnit证据已归档。强风仅核心/协调结构切片SLICE_DONE，世界、实际药水/网络包、视觉、联机、性能仍NOT_RUN。

当前R06-earth_lance IN_PROGRESS（仅建立任务卡，尚未迁代码）。下一精确动作：审计土枪12格首生物滴水石与自身脚下永久泥土双分支、有效参数/权限/失败退款/伤害和生成顺序，再抽纯规则及窄Adapter；不得合并两种不同副作用或套通用解释器。D-006仍未决。不启动游戏、不自动Git操作。
