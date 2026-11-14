# R06-earth_lance：SLICE_DONE（核心/协调结构切片；世界未验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`earth_lance`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


第35术：10MP、6点伤害；12格首个生物脚下3块向上滴水石10秒；自身脚下支撑块上表面为弹起并逐层垫1×3×1永久泥土分支。普通方块/落空不生成。先审计真实路径与构造后有效profile，不修改设定。

## 强风结构切片收尾（2026-11-12）

WindBlastEffect将反向弹射→100tick缓降的顺序接入真实Adapter。缓降拒绝仍成功、launch异常不刷新、缓降异常不撤销已发速度、重复施法固定时长请求的4项合同通过。清理SelfEffectComposition遗留无用import和日志措辞。源码兼容/无持续任务审计见evidence/r06-wind-blast-exit-audit.md；原版更强/更长药水合并语义保留，不承诺强制覆盖。

完整离线build退出0，63单测/208模块测试、旧15,658行为断言、102有效定义/教材及资源/JAR检查通过。日志r06-wind-blast-effect-build.log及JUnit证据已归档。强风仅核心/协调结构切片SLICE_DONE，世界、实际药水/网络包、视觉、联机、性能仍NOT_RUN。

当前R06-earth_lance IN_PROGRESS（仅建立任务卡，尚未迁代码）。下一精确动作：审计土枪12格首生物滴水石与自身脚下永久泥土双分支、有效参数/权限/失败退款/伤害和生成顺序，再抽纯规则及窄Adapter；不得合并两种不同副作用或套通用解释器。D-006仍未决。不启动游戏、不自动Git操作。

## 土枪滴水石预检与放置协调（2026-11-12）

已核对构造后有效参数：10MP/6伤害/160tick准备/10tick恢复/12格。两分支保留：先用原aim判断自身脚下支撑块UP面走construct；否则instant trace首个生物，先构造未入世界carrier，再MagicSpikes.raise，成功后damage和burst。自身构造仍走原抬升预检、launch及永久泥土ColumnGrowth，尚未抽离该分支。

EarthLanceSpikes提取三格完整预检→begin→自下向上放置→finish协调，MagicSpikes实现窄Placement边界。保留mayChange短路后才查空气、全部通过才取SavedData、UP方向BASE/FRUSTUM/TIP、setBlock flags2且忽略boolean、每格gameTime+200及最后dirty。magic_spikes与pos/expiry/state Codec、到期仅删除状态相同方块和卸载暂停清理全部未改。未知部分放置异常不伪装成false、不自造地形回滚。

4项模块合同覆盖预检/放置顺序、各层拒绝无副作用、部分异常和全部8种可用掩码；完整离线build退出0，63单测/212模块测试、旧15,658断言、102定义教材及资源/JAR检查通过。日志r06-earth-spikes-build.log及JUnit证据已归档。真实权限/方块更新、保存重载、伤害及视觉NOT_RUN。

当前R06-earth_lance仍IN_PROGRESS。下一精确动作：提取土枪双分支选点/释放Adapter，复用LocalEffect Command/Preparing但不得将自分支误走生物路径；先保留重复aim/trace时点和construct副作用顺序，再单独拆永久柱体协调。对明确false才退款，已放置后异常不回滚。D-006未决；不启动游戏、不自动Git操作。

## 土枪双分支准备与释放（2026-11-12）

EarthLanceAdapter实现LocalEffect窄端口，优先原aim下方支撑块UP面判断；否则原12格trace选首个生物。locate缓存单次分支/目标，place消费；自分支仍调用原construct并保留其二次aim、完整柱预检/抬升/增长顺序。生物分支保持carrier构造→三格滴水石→damage→burst；明确false退款，部分世界异常保留付款。旧execute复用cast，旧instant复用castTarget且故意不增自分支。

LocalEffectComposition显式绑定，MagicPreparation既有Preparing/Idle和单次Command自动接通；参数仍160tick/10MP/10恢复。选点现在由Command在扣费前同步执行，无目标不写付款状态；自分支第二次aim/世界预检仍在付款后，失败恢复完整旧状态。未增加世界线程间延迟；真实附着回调兼容尚未运行，不宣称此边界已经世界验证。

新增6项核心模块合同覆盖准备、拒绝/无目标、false退款、重复释放、异常付款、观察者隔离及取消；这些是端口合同，不覆盖真实两条世界分支。完整离线build退出0，63单测/218模块测试、旧15,658行为断言、102定义教材和资源/JAR检查通过。证据r06-earth-release-build.log与JUnit汇总/XML。

当前R06-earth_lance仍IN_PROGRESS。下一精确动作：提取永久泥土分支的柱体抬升/目标集合与生长协调，补两分支组合合同，审计存档和生命周期；保留原伤害权限及原版方块更新，不把端口测试称为世界验收。D-006未决，游戏/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 土枪永久泥土柱协调检查点（2026-11-12）

EarthLanceLift纯协调与EarthLanceLiftAdapter已接construct的self分支：原plan及ColumnPlacementAdapter完整预检后，目标仍为世界返回顺序且玩家最后；依次launch后，有目标登记永久ColumnGrowth，无目标直接永久泥土，最后burst。无自伤、无新增队列、无地形回滚。原非self兼容分支保留，未直接清理旧construct。抬升仍由原launchVelocity(UP,3)及速度包执行，不增加传送。

新增4项核心合同覆盖顺序、空目标、部分launch异常、独立调用及调用者列表不变；完整离线build退出0，63单测/222模块测试，旧15,658行为断言、102定义教材及资源/JAR检查通过。证据r06-earth-column-build.log与JUnit汇总/XML。仅核心及端口合同，实际碰撞、权限、地形更新、世界生命周期和视觉NOT_RUN。

当前R06-earth_lance仍IN_PROGRESS。下一精确动作：补双分支端口组合合同并审计ColumnRuntime的死亡/跨维/断线/停服清理、永久已放泥土与MagicSpikes持久过期边界，核对后仅关闭已验证结构切片。D-006保持未决；不启动游戏、不自动Git操作。

## 土枪组合合同与生命周期边界（2026-11-12）

新增2项组合合同：付款后先抬升目标再登记永久生长/显示/通知；滴水石第三层预检拒绝时完整退款且无放置。完整离线build通过，63单测/224模块测试，旧15,658行为断言及102定义教材、资源/JAR检查通过。证据r06-earth-composition-build.log及对应JUnit汇总/XML。

生命周期源码审计见evidence/r06-earth-lifecycle-audit.md：离线/重生/跨维/停服清理未放柱，已放永久泥土不清；滴水石独立持久到期。死亡到重生之间柱tick仍可能推进，保留旧行为，不能声称死亡即时清除。实际世界事件和保存重载NOT_RUN。

当前R06-earth_lance仍IN_PROGRESS。下一精确动作：抽离并验证双分支选择及生物分支raise→damage→burst协调，核对遗留construct兼容边界后再决定结构切片退出；死亡即时取消语义未决定，D-006仍未决。不启动游戏、不自动Git操作。

## 土枪生物命中协调（2026-11-12）

EarthLanceHit已接EarthLanceAdapter，保持carrier先构造，之后raise成功才damage再burst；放置false不伤害/不显示；伤害无效果仍继续显示，未知伤害异常不伪造地形回滚。新增3项核心模块合同验证顺序、拒绝和异常。分支选择仍在世界Adapter，不能称纯逻辑全迁移。

完整离线build退出0，63单测/227模块测试，旧15,658行为断言、102有效定义教材及资源/JAR检查通过。证据r06-earth-hit-build.log、JUnit汇总与XML。真实权限、伤害、射线、视觉、世界生命周期及保存加载仍NOT_RUN。

当前R06-earth_lance仍IN_PROGRESS。下一精确动作：提取可离线验证的双分支选择协调（自支撑块优先、无效时才trace生物、legacy instant不走自分支），审计construct兼容边界后决定结构切片退出。保留死亡到重生柱推进的旧语义；D-006未决。不启动游戏、不自动Git操作。

## 土枪结构切片收尾（2026-11-12）

EarthLanceSelection已接真实Adapter：懒查询自支撑块优先，命中则不查生物；不成立才trace生物；legacy instant跳过自查询。每次locate清空选择，place单次消费。4项合同覆盖优先/查询顺序/旧入口/连续独立查询。完整离线build通过，63单测/231模块测试、旧15,658行为断言、102定义教材及资源/JAR检查通过。证据r06-earth-selection-build.log及JUnit汇总/XML。

土枪准备/释放、选择、滴水石预检和命中、永久柱协调记SLICE_DONE（核心/协调结构切片）。construct仅保留原公开兼容实现，当前调用来自EarthLanceAdapter自分支；非self旧分支不擅删，仍属后续兼容清理。共享世界射线/权限/伤害/柱运行容器及客户端不是全迁完成。死亡至重生之间柱可推进的原语义保留，记录在生命周期审计，不声称死亡即时清除。

当前R06-earth_pillar IN_PROGRESS（仅任务卡，代码尚未迁）。下一精确动作：审计土落弹选点、上方6格生成、3×3×3空气检查、下坠实体命中与保存，然后拆纯几何/规则及生成Adapter，保留实际生成false退款。D-006未决；游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。
