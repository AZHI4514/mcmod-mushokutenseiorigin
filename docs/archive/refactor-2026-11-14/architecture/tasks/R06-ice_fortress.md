# R06-ice_fortress：SLICE_DONE（核心/协调；非全术或世界验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`ice_fortress`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


第28术冰砦，前置ice_pillar/water_shield。14MP/零伤害/160tick准备/10tick恢复，玩家脚同Y、水平准星前方2格，按水平主轴5宽×3高×1厚临时冰墙200tick，只填空气；权限/空间不足失败，非12格准星落点。

已做：IceFortressSpec有序格点纯函数接入旧plan；保留原版方向/取整，MP与时长接定义，2单测通过。

下一动作：独立放置Adapter接现有适合的窄端口与Command/State，不用飞行端口。先核对旧construct预检、目标搜集/自身、弹起、逐格生长/无目标立即临时方块、burst顺序；失败发生在不可逆前才允许退款，进入世界应用后异常不可冒称undo。旧ColumnPlacementAdapter含抬升净空检查，复用不复制。审计MagicTerrain临时冰租约和PhaseTwoRuntime.growingColumn，不改权限/存档/资源ID。覆盖规划/失败/准备取消/单次扣费/观察者和放置组合合同，完整build --offline归档；真实世界/权限/区块/保存/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 冰砦独立放置检查点（2026-11-11）

IceFortressAdapter已实现TargetedArea：locate计算并保存本次有序墙体计划，复用ColumnPlacementAdapter权限/空气/抬升净空只读预检；apply消耗已预检计划，原顺序搜集LivingEntity并按需追加自身→弹起→有目标growingColumn、无目标临时冰200tick→burst，零伤害不造伤害载体。保留旧只读aim调用及原版前方2格基点/方向取整。旧PhaseTwoSpells.execute冰砦入口复用同Adapter，不保留双份世界生成逻辑；土枪原construct路径不变。

TargetedAreaComposition显式装配冰砦，MagicPreparation通过既有handles自动持Preparing与CastTargetedAreaCommand，成功后Observer顺序保持。预检失败无扣费，apply开始后不可逆异常不退款；不把地形临时放置内部false伪装为全效果可回滚。首次构建发现装配wall变量遗漏已修正，最终完整build退出0。

新增6项本术模块合同，63单测/118模块测试通过；覆盖160tick准备/成功单次扣14MP/10tick恢复、无目标不扣费、非法输入/知识/余额拒绝、不可逆异常/重复提交、观察者顺序与取消。证据r06-fortress-adapter-build.log及对应JUnit。测试替身验证端口协调，不证明真实地形权限/抬升/方块租约。

当前R06-ice_fortress仍IN_PROGRESS。下一精确动作审计并迁出growingColumn的纯生长节拍/高度规则与冰砦临时冰租约调用，覆盖有目标延迟生长/无目标直接填充组合合同；核对停止、跨维、卸载与存档边界。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 冰砦有序生长检查点（2026-11-11）

ColumnGrowth<T>已迁至mechanic.terrain并被PhaseTwoRuntime真实Column调用：保存独立有序计划与绝对expiry，权限/非空气格移除并继续，首个占据格暂停后续，空闲格可同tick连续生成，到期先结束不查询世界，临时方块按expiry-now剩余时长放置。Placement窄接口由原运行时世界边界提供，没有固定一格/tick新动画节拍。共享冰柱/土枪等调用保持等价，不宣称它们新完成全术。COLUMNS仍旧静态表，服务器归属与持久化未迁。

新增2项真实生长核心模块合同，验证输入复制/计划顺序、阻挡暂停与释放、剩余租约/到期零查询、实例隔离。完整离线build通过：63单测/120模块测试；r06-column-growth-build.log及对应JUnit归档。

源码审计：MagicTerrain.temporary仍mayChange/空气/setBlock成功后写IceExpiry.ExpiresAt并scheduleTick；onPlace默认120tick后被调用者200或剩余租约覆盖，到期tick若尚未过期则重新调度，最终只移除仍为对应临时方块的格子；无掉落原路径保持。clear(owner)只删除未完成COLUMNS任务，已生成方块由各自块实体租约处理；跨维/LEAVE/重生/停服沿旧clear，释放按键只stop通道不取消已提交生长。卸载时mayChange=false的待生成格按旧逻辑丢弃，不能误称暂停。实际调度/卸载重载与ExpiresAt存档仍NOT_RUN。

当前R06-ice_fortress仍IN_PROGRESS。下一精确动作：补冰砦放置端口协调有目标延迟生长/无目标直接放置的组合合同及期限边界，审计COLUMNS服务器归属迁移方案和退出条件；原版地形/权限/存档/视觉/联机/性能NOT_RUN。不启动游戏、不自动Git操作。

## 冰砦放置协调与生长归属检查点（2026-11-11）

IceFortressPlacement核心已被真实Adapter调用，窄World端口分离目标搜集/弹起/入队/临时方块/视觉。无目标立即有序放置200tick后burst；有目标按原顺序弹起、排队生长后burst，后续ColumnGrowth共用绝对expiry与占据暂停。新增3项真实放置核心+生长核心组合合同，覆盖直接/延迟/到期不放置。端口测试不证明真实目标抬升或方块事件。

原静态COLUMNS已移除，改服务器主世界column_runtime附件拥有实例Map，不持久化/不同步；clear(player)只移除当前服务器该玩家未完成任务，原停服逐玩家clear后clearChannels(server)同时移除column_runtime附件。已生成临时方块仍由ExpiresAt租约负责；未改存档ID。SHIELDS/CONTACTS/BURNS与MagicPreparation等其他遗留状态仍未迁。

完整离线build通过：63单测/123模块测试，日志r06-fortress-ownership-build.log及对应JUnit归档。真实多服务器附件隔离、停服/跨维/重载仍仅源码审计，NOT_RUN。

当前R06-ice_fortress仍IN_PROGRESS；下一精确动作核对生长放置异常/失败与旧清理调用链，补最小异常和重复应用边界合同，再审计本术已迁切片退出条件转explosion。实际游戏/权限/存档/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 冰砦切片收尾与爆炸准备/飞行检查点（2026-11-11）

冰砦新增3项异常合同：生长放置抛异常保留当前格，下次以最新剩余租约重试，先前已完成格不重复；弹起/立即放置中途异常停止后续入队和burst，不回滚已发生世界副作用。真实Adapter在apply前消耗plan，Command拒绝重复提交；纯Placement协调器不伪装成有状态单次对象。底层temporary正常返回false仍消耗格子的旧语义保持；clear只移除未完成任务，已放置冰由ExpiresAt管理。已验证核心/协调切片标SLICE_DONE，世界权限/实际租约调度/重载与共享实体仍非全迁或验收。

当前R06-explosion IN_PROGRESS。ExplosionSpec提取16MP/10tick恢复/160tick准备和原版TNT强度4；显式ProjectileComposition绑定复用PhaseProjectileAdapter.configure、Preparing/Command/Factory/Observer，未新增空工厂。新增4项准备释放模块合同覆盖一次性收费、失败恢复、非法资格、取消/超时；102有效定义基线保持一致。PhaseTwoEntity的爆炸分支复用已有PiercingFlightRules到期/候选/步内接触规则，保留严格早于墙/流体、等距目标稳定排序及先claim后引爆。

完整build --offline退出0，63项单测/130项核心模块测试通过，旧15,658行为断言、102定义/教材、资源及JAR检查通过。证据r06-fortress-explosion-build.log与r06-fortress-explosion-junit-summary.json。模块合同不是实际Minecraft运行验收。

下一精确动作：提取并测试爆炸触发的窄世界端口协调（视觉→原版TNT→按piercing结束），补生物/固体/流体/到期及同目标重载去重组合合同；保留动态原版伤害/击退/起火/掉落，不替换为定义damage10或统一80%点燃。实体保存/原版爆炸/权限/客户端视觉/联机/性能NOT_RUN；不启动游戏，不自动Git操作。
