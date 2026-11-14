# R06-intermediate-runtime：STRUCTURE_SLICE_DONE（共享运行时结构切片）

共享任务，非新法术；依赖R06残余矩阵及已迁Flight/Hit/Adapter。旧IntermediateProjectile.tick、IntermediateSpells与MagicFlight；目标先冻结实际扫掠、墙/流体、未加载、历史与Codec职责，再按一项窄协调实施，不以重写实体替代设计。

先读各exodus/tornado/flood/icicle结构退出证据；核对super.tick/isRemoved、区块预检、age递增、墙与流体选点、候选排序/去重、永久地形副作用先后、discard/impact、MagicVictims/PaidMana/ExpansionBroken读写。保留原注册ID及载体同步/存档格式；源码审计不算实际Codec重载。

当前只允许读源码、写架构审计文档，确认首个未迁算法和窄端口后再拓展实现路径并更新本卡。最小检查边界差分、成功/拒绝/部分异常、同历史实例与保存恢复核心组合；需真实世界的仍NOT_RUN。命令gradlew.bat build --offline；上一基线63/412证据r06-preparation-build.log，不借给未实现代码。撤回只本批差分，不启动游戏、不Git。

下一精确动作：逐行审计IntermediateProjectile.tick与MagicFlight，记录旧世界动作顺序和每术分支，选择最小未迁协调并形成允许路径/合同再落代码。共享职责未清，R06不得标DONE。

## 共享弹体终止边界切片（2026-11-13）

源码顺序已核对：super.tick/removed→客户端移动或服务端寿命→扫掠区块预检→age递增→墙接触→风枪永久地形→fluidHit→生物候选/排序/结算→流体终止优先于墙→移动。未加载时狱炎/风枪discard，其余return且不增age。当前仅抽取最后流体/墙比较为ProjectileTermination纯规则，保持fluid<=distance且fluid<=wall、wall<=distance且block非空，NaN及无穷比较不新增验证。世界动作、命中集合及Codec不迁也不改。允许核心ProjectileTermination/tests、IntermediateProjectile及相关文档。下一继续迁终止动作协调端口，保留狱炎爆发及水炮/大火球碰块特例。

本批允许路径拓展：ProjectileTermination.World/apply及IntermediateProjectile终止匿名世界适配，核心仅决定终止动作顺序，不分派SpellId；世界Adapter保留狱炎爆发/owner重读/碰块特例，异常不自动discard、不新增重试。新增5项协调合同。

## 共享弹体终止协调检查点（2026-11-13）

ProjectileTermination已接IntermediateProjectile最后终止分支：流体<=distance且<=wall优先，position→particles→discard；否则wall<=distance且有block时position→wallEffect→particles→discard。核心无SpellId，匿名World保留狱炎碰墙爆发、随后重新读取owner及水炮/大火球blockImpact；异常保留已发生副作用，不自动discard或落入后续分支。无终止才原setPos(to)/traveled累加。命中历史、Codec、区块/年龄、风枪地形先于流体、候选排序等未改，仍待迁。

新增8项模块合同（3边界/含10,000差分、5动作顺序/异常），完整离线build退出0：63单测/420模块测试；旧15,658断言、102定义/教材、资源/JAR及架构边界通过。证据r06-terminal-build.log、JUnit汇总/XML。合同不是世界/Fabric/Mixin/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS，下一精确动作提取最终生物接触循环协调：保持contact重算、狱炎skipContact与历史短路、非狱炎先claim再setPos/hurt、piercing与非piercing各自particles/非piercing重复定位和discard顺序；先扩任务卡允许路径再实现。不启动游戏、不Git。

设计审查：窄World端口为真实世界动作的Adapter边界，纯协调只决定顺序，不添加新模式、通用解释器或空Factory；旧载体剩余职责未清。

本批允许ContactTraversal核心/模块测试及IntermediateProjectile循环适配。核心按已排序候选重新读取contact，skip端口保留单术历史短路；claim端口仅非狱炎写原Set，effect端口保留原hurt或burst。piercing在循环前快照，非穿透重复定位后particles/discard并返回true。异常不回滚、不重试。候选筛选/排序、Codec及后续终止协调不改。

## 共享弹体生物接触协调（2026-11-13）

ContactTraversal已接IntermediateProjectile排序后的目标循环，逐目标重算contact，再由skip窄端口保留狱炎历史短路；非狱炎原Set先claim后定位/伤害，狱炎仍由爆发使用原Set。piercing循环前快照；穿透每目标effect后particles，非穿透effect后重复定位→particles→discard→return。核心不分派SpellId，异常保留已发生副作用且不补执行后续动作。候选/排序、世界伤害和爆发、终止、年龄/区块、存档/注册/资源保持。

新增8项核心合同验证顺序、非穿透停止、跳过、后续接触重算、爆发对后续历史的影响、伤害/粒子异常和空列表。完整离线build退出0：63单测/428模块测试，旧15,658断言、102有效定义/教材、资源/JAR及架构边界通过。证据r06-contact-build.log与JUnit汇总/XML。测试桩只验证端口/核心组合，不是实际Minecraft/Fabric/Mixin/伤害/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS。下一精确动作审计并提取候选收集/排序协调，保持各术contact与history求值次序、strict小于墙流体、稳定同距顺序；先更新允许路径和合同再实现。共享载体扫掠/区块/Codec等仍待迁，不能称R06完成。不游戏、不Git。

实审：ContactTraversal负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

本批允许ContactCandidates核心/tests与IntermediateProjectile筛选排序适配。原筛选表达式保留求值顺序；全部筛完后稳定排序，比较时重算接触，不缓存、不加同距排序键。

候选筛选排序已迁ContactCandidates，保留原筛选表达式及求值次序、实时contact稳定排序，不缓存或新增同距排序键。新增5合同、1000组冻结排序差分；完整离线build通过63单测/433模块测试、15,658旧断言、102定义/教材/资源/JAR。证据r06-candidates-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计命中历史保存恢复及MagicVictims UUID容错、PaidMana/ExpansionBroken与年龄边界，先定义合同再迁移。不游戏、不Git。

实审：ContactCandidates负责候选筛选及稳定排序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

本批允许HitHistoryFormat核心/tests与MagicFlight保存恢复委托。保留先clear再读取Supplier、逐项UUID.fromString与IllegalArgumentException容错；不替换原Set，不改变Codec键和字段。PaidMana/ExpansionBroken/age仍原载体，后续审计。

命中历史字符串格式已迁HitHistoryFormat并由MagicFlight委托。保留MagicVictims及Codec.STRING.listOf、先clear再Supplier读取、UUID.fromString容错、原Set身份与部分异常语义。新增5项合同；完整离线build通过63单测/438模块测试、15,658旧断言、102定义/教材/资源/JAR。证据r06-history-build.log及JUnit汇总/XML。真实Codec重载/世界/Fabric/Mixin/视觉/联机/性能NOT_RUN。PaidMana、ExpansionBroken及MagicAge仍由原载体保存，未改默认值或clamp；下一核对并提取这些读取归一化的纯规则及旧默认值合同。R06-intermediate-runtime仍IN_PROGRESS，不游戏、不Git。

实审：HitHistoryFormat负责UUID字符串格式及同集合恢复，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

本批允许SavedProjectileCounters核心/tests与IntermediateProjectile读取委托。保留原字段默认值、读取后立即赋值顺序和PaidMana原规则，不构建整包快照。

共享弹体存档计数边界已迁SavedProjectileCounters，IntermediateProjectile保持原读取默认值及逐字段立即赋值顺序；MagicAge非负无新上限，ExpansionBroken仍clamp0..64，PaidMana仍由StoneCannonRules处理且默认10。未改Codec/保存键/注册/资源。新增4项模块合同及10000组冻结计数差分；完整离线build通过63单测/442模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-saved-counters-build.log及JUnit汇总/XML。实际世界/Codec重载/Fabric/Mixin/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS。下一审计并迁扫掠区块可用性检查，保持枚举顺序、首次未加载退出、狱炎和风枪discard而其他暂停、age仅通过预检后递增；先扩任务卡再实现。不游戏、不Git。

实审：SavedProjectileCounters负责存档计数归一化，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

本批允许SweptChunkGate核心/tests和IntermediateProjectile预检适配。保持x外z内、floor转int再右移、首次未加载退出；世界回调只查询hasChunkAt，不加载区块；discard判断仍延迟到失败后，年龄在成功后递增。

扫掠区块可用性检查已迁SweptChunkGate，IntermediateProjectile使用窄Loaded回调读取原hasChunkAt，保持x外z内、floor转int再右移、首次未加载退出。失败后才判断狱炎/风枪销毁，其他暂停，成功后才++age；不加载新区块，不改法术/资源/存档。新增4项核心合同及1000组冻结枚举差分；完整离线build通过63单测/446模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-chunk-gate-build.log及JUnit汇总/XML。测试只验证核心枚举与短路，真实Fabric/区块卸载/世界/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计固体碰撞候选收集与首墙/impact选点协调，保留水穿透跳过、接触<=步长才记contacts但contact<wall仍选墙的原分支、同距首个shape和永久地形先于流体；先定义端口及边界合同再迁移。不游戏、不Git。

实审：SweptChunkGate负责区块枚举及短路，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

本批允许SolidContacts核心/tests与IntermediateProjectile固体接触协调。保留原HashMap合并及严格首墙比较，impact计算延迟到更近contact；形状/含水过滤/世界枚举不改，地形分支仍先于流体。

固体接触合并与首墙选择已迁SolidContacts，保持contact<=步长才合并HashMap、contact<wall才更新墙和impact、同距取首个形状、含水过滤和世界枚举不变，地形仍先于流体。新增6项核心合同及1000组冻结差分；完整离线build通过63单测/452模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-solid-contacts-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计流体候选扫描及固体终止交接，保持读取时点、未加载跳过及流体同距优先，先定义端口再迁移。不游戏、不Git。

实审：SolidContacts负责固体接触合并和首墙选择，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

本批允许FluidContactScan核心/tests与MagicFlight流体扫描适配。保持loaded→fluid→empty→ignoresWater→water的短路次序，使用同一FluidState计算高度；逐个Math.min无提前终止。距离近零与边界枚举仍世界层。

流体扫描已迁FluidContactScan并由MagicFlight窄World适配。保持loaded→fluid→empty→ignoresWater→water短路、同一FluidState高度读取、逐项Math.min且零距离不提前停止；近零运动和方块枚举仍原世界层。新增5项模块合同；完整离线build通过63单测/457模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-fluid-scan-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一核对已迁核心协调与载体剩余职责，建立完整服务端单步协调及窄世界适配合同，保持年龄、地形、流体、生物、终止顺序；不游戏、不Git。

实审：FluidContactScan负责流体读取及最小接触扫描，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

本批允许ProjectileServerStep核心/tests与IntermediateProjectile单步适配。范围为寿命及几何准备之后：区块门控→age递增→固体/地形→流体→生物→终止→移动；未加载回调保留原销毁选择。核心不解释SpellId，生命周期和几何准备仍载体职责。

ProjectileServerStep已协调寿命和几何准备后的完整服务端扫掠：区块检查→年龄递增→固体和永久地形→流体→生物→边界终止→移动。世界匿名适配仍负责原API和单术差异，不分派到通用解释器；未加载不推进年龄，目标终止或边界终止均跳过后续移动，异常原样传播且不回滚。新增6项核心合同；完整离线build通过63单测/463模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-server-step-build.log及JUnit汇总/XML。首次提取多余括号导致编译失败，已修正并重跑全构建，保留初次日志。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS：寿命、几何选择、世界匿名适配和保存载体仍未全迁。下一明确世界Adapter归属与载体窄状态端口，先审计避免扩大实体公共可变接口；不游戏、不Git。

实审：ProjectileServerStep负责完整服务端单步顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

本批允许IntermediateStepAdapter和载体装配：原Set引用及窄回调注入私有计数，不增实体公共setter；游戏测试全部重构完成后统一进行。

IntermediateStepAdapter已承接服务端世界单步操作，原命中Set直接传入，年龄和破坏计数及距离通过窄回调注入，实体字段保持私有，无新增公共setter。原区块/年龄/地形/流体/目标/终止顺序、原注册和保存键不变。完整离线build通过63单测/463模块测试、15658旧断言、102有效定义/教材/资源/JAR。证据r06-step-adapter-build.log及JUnit汇总/XML。未新增测试，以原核心合同和编译/基线回归验证适配迁移，不声称世界集成。用户决定全部重构完成后统一游戏实测；世界/Fabric/Mixin/Codec/视觉/联机/性能仍NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，寿命和几何选择/保存载体尚待收尾，下一审计这些剩余职责并形成明确退出清单，不游戏、不Git。

实审：ProjectileServerStep负责完整服务端单步顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

本批允许IntermediateStepAdapter.prepareAndApply迁移原寿命检查与几何准备，载体仅传当前age快照和计数回调；保留原表达式短路与尺寸，不改客户端或存档。

IntermediateStepAdapter.prepareAndApply已承接原寿命检查与扫掠几何准备，载体tick保留super/removed、客户端轨迹，服务端只注入原命中Set和私有计数回调。保留原寿命表达式短路、体积尺寸、速度、区块/年龄/地形/流体/目标/终止顺序，未改变注册和保存格式。完整离线build通过63单测/463模块测试、15658旧断言、102定义/教材/资源/JAR；未新增测试，本批为世界适配迁移并运行原合同回归。证据r06-step-preparation-build.log及JUnit汇总/XML。用户明确全部重构完再统一实机测试；世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一核对载体保存及适配器单术分支归属，补结构退出清单后推进IntermediateSpells命中适配，不能称R06全部完成。不游戏、不Git。

实审：ProjectileServerStep负责完整服务端单步顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

# R06-intermediate-runtime 结构退出审计（2026-11-13）

本共享运行时切片 STRUCTURE_SLICE_DONE，非整阶段 R06 DONE。

- IntermediateProjectile.tick：super/removed、客户端轨迹与移动仍是游戏实体载体职责；服务端寿命/几何及单步适配已交 IntermediateStepAdapter。
- 纯协调：ProjectileServerStep、ContactCandidates、ContactTraversal、ProjectileTermination、SolidContacts、SweptChunkGate 和 FluidContactScan 均已有独立核心合同。
- 保存：实体继续持有私有 age、expansionBroken、paid 与原 hit Set；ValueInput/Output 和父类调用保留载体。纯归一化与 UUID 格式交 SavedProjectileCounters、StoneCannonRules、HitHistoryFormat；MagicVictims/Codec.STRING.listOf 和逐字段立即赋值不变。traveled 不新增保存或寿命约束。
- 世界差异：IntermediateStepAdapter 的狱炎爆发、风枪地形、石炮伤害、截面选择等明确委托各已有单术规则/Adapter；共享几何与世界 API 枚举属于基础设施适配，不声称完全消除法术选择分支。最终 R10 仍需审计兼容命名和零调用路径。
- 未完成归属：IntermediateSpells.execute/hurt/blockImpact/visible 聚合入口交 R06-intermediate-effects，PhaseTwo 扫掠/效果、通道、发布与准备资格仍按残余矩阵独立推进。

实际 Codec 重载、Fabric/世界/事件/视觉/联机/性能均 NOT_RUN；按用户要求全部重构完成后统一实机验收。

## 中级普通命中协调（2026-11-13）

IntermediateHit 纯协调与 IntermediateHitAdapter 已接 IntermediateSpells.hurt；风枪龙卷仍使用独立 TornadoImpactHitAdapter。保持伤害成功后才判断击退并读取 motion、附效时才读取 owner，按 hurt→条件 knockback→effect→afterHit 原顺序执行。失败立即停止，异常原样传播、不回滚、不补调用。8项模块合同验证成功/拒绝、零击退、伤害后状态读取与三阶段异常截断。

完整离线 build 退出0：63单测/471模块测试，旧15,658断言、102有效定义、39/102教材、旧玩家存档fixture、资源与JAR/架构边界通过。证据 r06-intermediate-hit-build.log 及对应JUnit汇总/XML。端口测试不等于真实Fabric伤害/事件集成；游戏/Codec重载/视觉/联机/性能 NOT_RUN，全部重构完成后统一游戏测试，不启动游戏、不Git。

共享弹体运行时 STRUCTURE_SLICE_DONE，退出归属见 evidence/r06-intermediate-runtime-structure-exit.md；整体R06未完成。当前唯一任务 R06-intermediate-effects IN_PROGRESS。下一精确动作：审计 IntermediateSpells.execute 的剩余 TARGET_AREA fallback 与 blockImpact 其他输入fallback的真实调用及兼容合同，再拆范围命中/可见性世界适配；禁止仅凭注册路径无调用删除兼容行为。
