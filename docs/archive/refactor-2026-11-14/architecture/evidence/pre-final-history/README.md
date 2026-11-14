# 长期开发文档入口

更新：2026-11-13。项目实际目录是`docs/`，没有`docx/`。每次开发从本页开始；阶段性报告已合并到正式规范，不再作为平行规格保留。

## 当前进度与边界

截至2026-11-11：102个独立法术/能力（原39＋扩展63）已接入代码、资源和教材；冰枪→吹雪为顺序组合，不另计法术数。最近一次离线构建通过，verifyMagic为15,658项行为断言；40类动态网格1,200边界采样及21种网格6,300离线采样通过。重构本轮已重新运行完整离线构建；另有63项纯JUnit单测、936项核心模块测试及31,000组几何差分通过，详细日志见architecture/evidence。实际游戏视觉、Mixin加载、联机、权限兼容及性能仍待验收，用户当前要求暂不启动游戏。

塞妮丝书102项逐页点击直接学会，入门书原39项直学，分类教材保留前置和学习谱；轮盘10槽，施法MP和咏唱规则不变。暂缓范围以扩展规范为准。架构v2已获用户批准，正在按R00—R10实施；唯一进度入口为[重构状态](architecture/REFACTOR_STATE.md)。不启动游戏，不自动执行Git操作。

## 每次必读与按需阅读

| 文档 | 职责 |
|---|---|
| 根目录`AGENTS.md`、[工程规范](MAGIC_DEVELOPMENT.md) | 每次必读；代码边界、状态、验证和更新流程 |
| [原39术游戏设定](魔法-游戏设定.md) | 原39术逐术数值、历史有效特例 |
| [扩展63术规范](ALL_MAGIC_EXPANSION.md) | 扩展数值、当前操作、未验收及暂缓边界 |
| [学习与咏唱](TEXTBOOK_CHANT_SYSTEM.md) | 教材、直接学习、学习谱、施法时间 |
| [视觉制作规范](PIXELFX_ANIMATION.md) | 原作参考、模型/纹理/动画、粒子预算及许可证 |
| [物品约定](ADDING_ITEMS.md) | 修改或新增物品时必读 |
| [原作设定](魔术设定.md) | 原作参考；不等同已实现游戏功能或游戏数值 |
| [架构重构提案v2](ARCHITECTURE_REFACTOR_PROPOSAL.md) | 已获准设计；当前进度见architecture/REFACTOR_STATE.md |

## 规格与上下文恢复

用户最新明确决定优先，其后为正式游戏规格的最新有效修订；原作参考用于解释intent，不擅自覆盖游戏平衡。发现文档与代码冲突应列出intent/spec/observed及证据，不能以当前实现反向修改设定。编译通过、资源接入、游戏验收分别记录。

每次结束更新受影响的正式文档及本页的当前基线；只记录实际执行的检查。重构授权后按提案维护唯一任务状态与证据文件，恢复上下文先读持久状态再继续，不凭聊天记忆跳步。尚未创建的规划文件不能写成已存在。

## 架构迁移检查点（2026-11-11）

R00—R04、R05水弹试点，以及R06火球弹/风弹/石弹/水矢/冰击/火断/冰刃/水炮的准备与释放切片已落地（火断另迁核心飞行与截面碰撞），水蒸即时范围、大火球和真空波切片也已接入；其余88术和后续功能仍按[唯一状态](architecture/REFACTOR_STATE.md)推进，不等同整个重构完成。102术参数、原有模型/纹理/数值及兼容ID保持。游戏实测仍NOT_RUN。

岩炮弹注魔/释放/规则/服务器会话与冰柱准备/释放/柱体规则切片已通过离线合同；最新51单测/82模块测试。冰柱复用真实State/Command/Observer及TargetedArea窄端口，原权限/空气/抬升预检移至共享Adapter。实体世界/逐格生长/租约与真实生命周期仍未全迁或验收。当前任务R06-waterfall IN_PROGRESS：持续费用/小数预付、7tick心跳/20tick节拍及服务器通道容器已接入；独立水源适配、完整通道协调、Channeling State与连通水纯规则已接入；生命周期/兼容审计待收尾；整体重构未完成。

## 水瀑通道与连通水检查点（2026-11-11）

WaterfallChannel已接真实入口，依赖PlayerStore/ChannelDebt/WaterSource；首次false恢复MP和债务，保留目标检查→付款→光效→续水→每20tick生成顺序。WaterfallSourceAdapter拥有最新成功水源，保留首次生成后二次trace与旧instant兼容入口。Preparing/Idle用于准备，新增实际Channeling State响应心跳、资格取消和第7tick超时；停止幂等、冷却max(existing,10)，不退款预付信用、不删永久水。

ConnectedWater将六邻接搜索及世界时间每10tick刷新判定提为JDK纯函数，MagicWaterSources仅适配已加载水格查询与实际湿身应用。保留±16范围、DOWN/UP/NORTH/SOUTH/WEST/EAST顺序、4096仅在出队前检查（单轮可略超限）的历史边界。magic_water_sources、pos/expiry Codec、Long.MAX_VALUE永久水、旧有限租约及卸载处理未改。51单测/82模块测试与完整离线build通过；日志和XML见r06-waterfall-connectivity证据。不是Fabric/世界集成验收。

下一精确动作：审计水瀑停止与断线/死亡/跨维/停服入口及保存加载兼容调用链，补足可离线验证的实例隔离合同；核对后仅关闭已验证切片并创建ice_lance任务卡。水流、蒸发、连通湿身、权限、重载、游戏视觉/联机/性能均NOT_RUN；仍IN_PROGRESS。不启动游戏，不自动Git操作。

## 最新检查点：水瀑切片收尾，冰枪开始（2026-11-11）

水瀑核心/协调切片已通过源码生命周期审计与双实例隔离/停止重建信用合同，记SLICE_DONE，不代表整术全部迁移或世界验收。审计证据见architecture/evidence/r06-waterfall-lifecycle-audit.md（architecture内使用evidence相对路径）。永久水SavedData和实际世界适配保留，事件/Fabric/存档实际重载仍NOT_RUN。

当前R06-ice_lance IN_PROGRESS：IceLanceSpec接入原定义的MP/伤害/范围/历史寿命，IceLanceRules接入实体首个递增age命中与MagicEffects冻结判定，保留80%/200tick及随机采样位置。53单测/84模块测试和完整离线build通过，证据r06-waterfall-final-ice-start-build.log及对应JUnit。没有更改102术有效数值、资源或ID。

下一动作：按R06-ice_lance任务卡提取即时选点/生成窄端口与应用释放协调；明确addFreshEntity=false的无效果退款边界，不把该boolean结果塞进已有void TargetedArea.apply；接入Preparing State并保留SequentialMagic成功通知。首击目标集合/可见性/伤害、实体存档及视觉随后逐项审计。未启动游戏，未Git操作。

## 冰枪选点/准备/释放检查点（2026-11-11）

IceLanceAdapter实现LocalEffect窄端口，定位保留原12格trace与底面中心；place返回原addFreshEntity结果。CastLocalEffectCommand为真实单次释放Command，先验证玩家/角度与定位再扣费，显式false恢复完整旧状态，未知部分世界异常保留付款；成功后按恢复显示→SequentialMagic→发光→音效通知，观察者异常不退款。独立boolean契约避免错误复用void TargetedArea或飞行弹体端口。

MagicPreparation的冰枪Pending已持Preparing/Idle State及独立Command，取消仍取消State，原资格/跨维/心跳入口保留。PhaseTwoSpells.instant旧直接入口复用同Adapter，不额外扣费，不保留重复生成实现；没有新增空Factory或通用效果解释器。

完整离线build通过，53单测/90模块测试，新增6项模块合同覆盖准备成功、重复拒绝、无目标、false全状态恢复、非法输入/不足MP/未学拒绝、未知部分异常、观察者顺序/隔离、准备取消。日志r06-ice-lance-release-build.log及对应JUnit已归档。纯模块合同不证明真实顺序魔法事件、实体注册与世界效果。

下一精确动作：审计并拆分冰枪首击目标集合、定向1×3×1体积/可见性/伤害协调与实体保存字段职责；保留年龄递增时点、12tick历史字段、UUID/资源/同步ID及原伤害权限，不将切片标为整术完成。当前R06-ice_lance仍IN_PROGRESS；GameTest/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 冰枪首击协调检查点（2026-11-11）

IceLanceHit已接PhaseTwoEntity，LocalHitTargets窄端口由IceLanceHitAdapter实现；核心负责首击年龄、同一SweptVolume定向1×3×1精确筛选与可见后伤害顺序，世界边界保留原候选、射线、PVP/伤害/附效/hooks。原SpellSweep本已委托同一核心几何，未新增近似碰撞或改方向精度。保存/同步字段与年龄递增/卸载暂停逻辑未改，审计见evidence/r06-ice-lance-entity-audit.md。

完整离线构建通过：53单测/93模块测试；新增3项首击端口合同。日志r06-ice-lance-hit-build.log及对应XML已归档。当前R06-ice_lance仍IN_PROGRESS；下一动作补定向几何边界与准备/释放/首击组合合同，核对本术切片退出条件后再转icicle_break。真实实体重载、遮挡/权限、视觉/联机/性能NOT_RUN；不启动游戏、不执行Git操作。

## 冰枪切片收尾、冰霜击开始（2026-11-11）

冰枪已迁规格/冻结/首击规则、选点/释放Command、Preparing State及首击世界Adapter。新增旋转45度体积测试排除宽相包围盒角落、保留3格顶部边界；准备160tick→收费生成→按保存年龄递增触发一次的组合合同通过。仅将已验证核心/协调切片记SLICE_DONE，非整术全迁或世界验收；共享实体保存/注册、游戏实际遮挡/PVP和客户端仍按后续阶段验证。

当前R06-icicle_break IN_PROGRESS：规格MP12/伤害2/速度3/历史寿命100与历史range18接入定义；冻结纯规则保留80%/200tick、原随机采样位置与资格短路。实际恒速飞行仍使用原rank2的120tick预算，未用历史寿命替换或把range改成飞行上限。新增冻结样本/资格差分单测。最新完整离线build通过：54单测/95模块测试，原15,658断言和资源/存档/架构检查通过；证据r06-icicle-break-start-build.log及对应JUnit。冰枪收尾日志r06-ice-lance-final-build.log。

下一精确动作：按R06-icicle_break卡迁准备/付费发射协调，使用正确初始化PhaseTwoEntity的专属ProjectileSpawner Adapter，复用现有Command/Factory合同；之后迁飞行预算、每发命中集合、含水穿透与碰撞规则。不能让VanillaProjectileAdapter仅setOwner/shoot后丢失configure中的同步Spell/remaining/方向/边界。不启动游戏，不自动Git操作，世界/视觉/联机/性能NOT_RUN。

## 冰霜击准备/释放检查点（2026-11-11）

ProjectileComposition已显式绑定IcicleBreakSpec.PROJECTILE，沿用CastSpellCommand、ProjectileSpellFactory、Preparing/Idle State及成功后Observer。Binding注入真实ProjectileSpawner供应函数；普通弹体用具名vanilla装配，冰霜击用PhaseProjectileAdapter。供应函数/具名装配不是新增Factory Method模式，真正Factory Method仍为ProjectileSpellFactory.create。

PhaseProjectileAdapter核查计划术ID并通过原PhaseTwoSpells.entity→configure创建实体，保留眼高原点、请求准星、同步Spell、120tick实际预算、原始方向与边界；不调用会改变恒速初始化的普通shoot。旧直接执行入口仍保留原实体构建路径，不增加第二份扣费。

完整离线build通过：54单测/99模块测试。新增4项本术合同覆盖160tick准备、成功扣12MP/10tick恢复/3速度/零散布及重复拒绝，false/异常恢复旧快照、不足MP/未学/非法角度拒绝、取消和心跳超时。原有数值/教材/资源/架构检查通过，日志r06-icicle-break-release-build.log及对应JUnit已归档。实际Fabric初始化/世界注册未验收。

当前R06-icicle_break仍IN_PROGRESS；下一动作迁120tick飞行预算与逐发UUID命中记录，审计含水属性/水/熔岩/固体碰撞调用链，保留保存加载、原版权限/伤害与命中顺序。准备Command的spawn异常回滚沿用既有弹体契约，不改变其他术语义；不可逆伤害仍不在此spawn阶段执行。世界/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 冰霜击飞行/命中记录检查点（2026-11-11）

IcicleBreakFlight已接入原实体的预算到期、候选与最终步内接触判定；保存Remaining继续权威，不在重载时重置120。PhaseTwoEntity复用ImpactHistory替代原可写HashSet，保存MagicVictims用snapshot、原UUID容错reader读回后重建，claim仍在伤害之前；共享载体其他术保持等价集合语义，不代表这些术已完成逐术迁移。

含水通路继续复用已有WaterPassage，不复制新规则；源码审计保留WATERLOGGED能力（干/湿均可）与水流穿透、熔岩/普通阻挡、严格墙距与步末含等号语义。证据r06-icicle-break-flight-audit.md；此为源码审计而非世界测试。

完整离线build通过：57单测/101模块测试。新增3项单测及2项模块合同覆盖保存预算/边界、穿透资格、命中快照隔离与重建、伤害失败不重试。日志r06-icicle-break-flight-build.log及对应JUnit归档。当前仍IN_PROGRESS，下一精确动作补完整准备/释放/120tick飞行/命中快照组合合同并审计排序与保存退出条件，然后仅关闭已迁切片转icicle_edge；共享世界碰撞和实体载体清理仍有后续任务。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 冰霜击切片收尾、冰霜刃接入（2026-11-11）

冰霜击新增核心组合合同：真实Preparing→CastSpellCommand产出一次计划，眼高原点/请求角度不丢，120tick预算与3格速度组合360格、37tick重建命中快照后同目标仍一次，保存限额37仍按37到期。该测试仅组合真实核心对象，不是PhaseTwoEntity模拟或实际存档重载。候选稳定距离排序和MagicVictims原reader已源码审计，世界/Fabric/保存实测仍NOT_RUN。仅把已迁核心/协调切片记SLICE_DONE，非整术全迁。

当前R06-icicle_edge IN_PROGRESS：第26术正式名称冰霜刃。IcicleEdgeSpec接入10MP/6伤害/速度3/历史寿命100/range18，80%/200tick冻结保持原随机位置；ProjectileComposition显式绑定既有Command/Factory、Preparing State与PhaseProjectileAdapter，保留完整configure。新增4项本术模块合同验证成功/失败快照恢复/非法输入/取消及超时。完整离线build通过：57单测/106模块测试，日志r06-icicle-edge-start-build.log及对应JUnit已归档。

下一精确动作：审计并拆分冰霜刃完整截面遮挡规则及飞行判定，复用火断已有纯几何但避免长期水术依赖火术规格；保留当前负AimYaw、部分遮挡通行、完整准星方向、3×1×1扫掠命中和120tick预算。不能改成中心射线或普通实心长方体阻挡。每发命中与含水逻辑继续共享，世界/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 冰霜刃共享截面检查点（2026-11-11）

原core火断FlameSliceVolume算法已迁domain.geometry.BladeSectionVolume并删除旧core类；没有留下零职责术模块转发类。游戏侧FlameSliceVolume继续作为原版Vec3/AABB转换边界，火断调用保持；PhaseTwoEntity冰霜刃直接依赖共享几何，经GeometryAdapter转换，保留motion及负AimYaw、1/32格采样与完整覆盖判定。共享算法本来固定3×1×1，两术当前尺寸相同，不为未来假设增加尺寸配置层。部分遮挡通过、相邻方块并集全覆盖停止、微缝仍可通过。

迁移前实现已冻结在test-only FrozenBladeSection；500组固定随机方向/俯仰/位置/方块并集对比fullyBlocked/blockingDistance/contact通过，另有明确部分遮挡/全遮挡边界合同。完整离线build通过：59单测/106模块测试，原有15,658断言及资源/架构/保存fixture检查通过；证据r06-blade-section-build.log与对应JUnit。500是几何采样数，不是额外JUnit数或世界覆盖率。

当前R06-icicle_edge仍IN_PROGRESS；下一精确动作迁冰霜刃飞行预算/候选/步内接触判定到共享纯规则，避免水术互相调用；补负yaw斜向与准备/释放/穿透记录组合合同并审计保存/加载退出条件。真实世界碰撞体读取、区块、流体、伤害权限/存档重载、视觉/联机/性能NOT_RUN。不启动游戏、不执行Git操作。

## 冰霜刃共享飞行检查点（2026-11-11）

PiercingFlightRules迁至mechanic.projectile，原IcicleBreakFlight已删除，冰霜击/冰霜刃真实实体入口共同调用预算/候选/步内接触函数。没有兄弟术依赖或空转发类；保留保存Remaining权威、严格墙/流体距离、步末包含等号与先claim后伤害。其他术继续原判定，未批量宣称迁完。

新增负yaw斜向截面边界合同，以及冰霜刃Preparing→真实CastSpellCommand→计划→120tick预算/360格理论距离/命中快照恢复核心组合合同；此不模拟真实实体tick或证明真实存档加载。完整离线build通过：60单测/107模块测试，既有15,658断言/资源/存档fixture/架构检查通过；日志r06-edge-flight-build.log及对应JUnit归档。

当前R06-icicle_edge仍IN_PROGRESS。下一精确动作：审计本术实体保存、排序与生命周期调用链，核对切片退出条件后转flood_flush，创建其逐术卡；不要把共享规则接入等同整术全迁。实际游戏碰撞/权限/PVP/重载、客户端/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 冰霜刃切片收尾、水流开始（2026-11-11）

冰霜刃保存/排序/生命周期源码审计完成，证据r06-icicle-edge-exit-audit.md；与已有差分和核心合同一起仅关闭已迁核心/协调切片SLICE_DONE，非整术全迁或世界验收。

当前R06-flood_flush IN_PROGRESS：FloodFlushSpec接入原定义12MP/2伤害/.5速度/历史寿命100与range8；湿身60%/200tick独立纯函数接入且保留随机采样位置；水平击退1.2只改常量来源，仍由hurt成功后原版knockback执行。ProjectileComposition复用Preparing/Command/Factory/PhaseProjectileAdapter与原观察者，不新建空工厂。

完整离线build通过：61单测/111模块测试。新增湿身边界单测及4项本术准备/付费发射合同；日志r06-flood-start-build.log及对应JUnit归档。当前下一动作：迁水流飞行判定至已共享PiercingFlightRules，核对3×1×1定向扫掠与120tick预算、含水通过/普通固体阻挡，无需完整截面特例；补准备/释放/命中/保存信用组合合同及击退先后顺序审计。世界/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 水流共享飞行检查点（2026-11-11）

水流真实PhaseTwoEntity入口已接PiercingFlightRules预算、候选及步内接触判定，复用ImpactHistory与原保存reader；没有复制水术私有飞行引擎。3×1×1扫掠、.5速度、120tick预算/60格理论距离和普通实心阻挡保持，不套冰霜刃完整截面特例。新增准备→真实Command→计划→预算/命中快照重建核心组合合同。

源码审计确认伤害成功后附效→afterHit→原版knockback顺序不变，失败不击退；保存字段、方向/区块暂停不变。证据r06-flood-flight-audit.md。完整离线build通过：61单测/112模块测试；日志r06-flood-flight-build.log及对应JUnit归档。不是实体tick/真实存档/击退抗性验收。

当前R06-flood_flush仍IN_PROGRESS。下一精确动作核对已迁切片退出条件与共享载体剩余职责，记录明确未验收项后转ice_fortress并创建逐术卡；不得把原世界碰撞/保存服务或客户端当已全迁。实际游戏/PVP/重载/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 水流切片收尾、冰砦规划开始（2026-11-11）

水流已验证核心/协调切片记SLICE_DONE；退出依据是准备/发射/预算/命中恢复合同及r06-flood-flight-audit.md源码审计。共享PhaseTwoEntity世界碰撞/存档/实际击退、客户端尚未全迁或验收，明确保留后续职责，不把切片关闭当全术完成。

当前R06-ice_fortress IN_PROGRESS：IceFortressSpec.plan已接原PhaseTwoSpells.plan，纯函数只处理水平主轴与5×3有序格点，保留原版Vec3计算前方2格基点/BlockPos取整和完整准星主轴比较、相等取X跨度、底层到顶层顺序。规格MP14与200tick接入原定义。新增2项规划单测（负坐标/主轴/相等/顺序/唯一/不可变）。完整离线build通过：63单测/112模块测试；r06-fortress-start-build.log及对应JUnit归档。

下一精确动作：迁出冰砦独立世界放置Adapter及准备/释放协调，保留ColumnPlacementAdapter权限/空气/抬升净空预检、目标弹起→有目标逐格生长或无目标临时冰→视觉顺序；检查放置返回失败/不可逆开始的边界，不能将部分放置误判可退款。原地形租约/生长容器继续审计，实际游戏/权限/存档/视觉/联机/性能NOT_RUN。不启动游戏、不自动Git操作。

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

## 爆炸触发协调与切片收尾（2026-11-11）

ExplosionImpact拥有流体/固体终止纯判定及burst→原版TNT→非穿透discard协调；ExplosionImpactAdapter保留原载体、当前接触点、true起火和Level.ExplosionInteraction.TNT。PhaseTwoEntity真实生物/固体触发接入核心，固体退0.02不变，流体与固体等距时流体优先不爆。到期仍直接discard；区块未加载暂停、保存年龄/Remaining/命中UUID键保持。未将damage10当爆炸伤害，未新增AOE或80%点燃。

新增7项合同：穿透保留载体、非穿透结束顺序、流体/固体边界、原版抛异常不伪造成功/回滚、历史重建去重、到期/碰撞边界及付费释放→不同目标引爆→历史重建的核心组合。组合测试显式组合核心组件，不执行真实实体tick、Codec或原版爆炸。

完整离线build通过：63单测/137模块测试，15,658旧行为断言、102有效定义/教材、资源和最终JAR检查通过；证据r06-explosion-impact-build.log及JUnit汇总。本术已验证核心/协调切片SLICE_DONE；共享世界扫掠/实体存档服务、实际TNT事件和权限、客户端仍待对应阶段与实机验收，不称整术全部重构完成。

下一任务R06-flame_pillar：先拆首次目标/方块选点与放置窄端口，审计BURNS持续任务的60tick期限和0/20/40伤害时点，再迁服务器归属；不把每目标接触火柱载体与选中目标的持续伤害任务混为一谈。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 火柱核心/释放/任务归属切片（2026-11-11）

FlamePillarSpec提取12MP/3伤害/60tick任务等有效常量。FlamePillarAdapter分别缓存生物或方块选点，consume后不能重复place；LocalEffectComposition显式绑定既有CastLocalEffectCommand/Preparing/Observer，失败无效果返回false恢复原状态，首次世界副作用后异常保留付款。旧execute兼容入口委托同Adapter，不额外收费。二次只读aim现于locate内、扣费前完成，同一服务端调用不跨tick；原有效命中条件不变。

FlamePillarIgnition保留点燃60→首次伤害→首次脚下放火→burst→入队顺序。FlamePillarBurn以绝对start判定，age>0且20整倍数才尝试伤害，age>=60/目标死亡/目标跨维先结束；不补漏tick，不给重复同tick调用发明去重，不重复地形火。FlamePillarFirePlacement保留先命中面后顶部、权限优先、已有普通/灵魂火直接成功、仅空气与可存续火、false后继续候选的原逻辑；不去重重复候选。

静态BURNS已移除，burn_runtime附件让当前服务器主世界拥有玩家任务Map，不持久化/不同步；玩家clear删除本服该玩家任务，停服clearChannels同时移除该附件。多个施法任务仍独立追加，不合并；已有原版火不因停止/清理而熄灭。SHIELDS/CONTACTS等其他遗留状态未迁，真实服务器隔离仅源码审计。

新增17项模块合同：准备/收费/资格/取消/观察者/异常、首次效果顺序与20/40/60期限、死亡/跨维、时间跳跃/倒退、独立任务、放火回退/权限/已有火/重复候选，以及一次付费→首次点燃放火→两次延迟伤害组合。完整离线build通过：63单测/154模块测试，旧15,658行为断言、102定义/教材、资源/最终JAR检查通过。证据r06-flame-pillar-build.log、JUnit汇总、r06-flame-pillar-exit-audit.md。

火柱已验证核心/协调切片SLICE_DONE，不等同整个法术全部重构或世界验收；共享选点/权限/伤害/附效/实体保存服务留待相应阶段。当前任务转R06-flamethrower，下一步拆独立通道协调、锥形命中纯规则与每目标20tick节拍，复用现有ChannelPayment/ChannelSession而非复制水瀑。游戏/原版火/权限/存档/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 火焰放射通道/命中/服务器窗口切片（2026-11-11）

FlamethrowerChannel接真实PhaseTwoRuntime入口，复用ChannelPayment与既有CastingSession的Channeling/Idle State。begin只付首次费用并激活，runtime先注册再emit（发光→世界命中/旧粒子）→release sound；tick先资格/第7次缺心跳终止、再付费→emit。每20次付费发射累计6MP；零MP时可消费已预付信用，新开通道仍至少1MP；stop幂等、恢复max(existing,10)，不退款负债信用。水瀑独立协调保留，不套用其WaterSource/周期生水行为。

FlamethrowerHit迁出16格/25度锥形和候选→锥内→可见→claim→伤害顺序，Adapter保留原LivingEntity候选、原版伤害/附效，以及8段粒子、分散度和遮挡中止。PhaseTwoRules.inCone兼容入口委托同纯函数。FlamethrowerSpec接6MP/4伤害定义；102有效定义未变。

ContactCooldowns拥有来源UUID→目标UUID→时间窗口，仍20tick整点过期、claim在伤害前、不因失败伤害或短暂停止重开而重置。旧静态CONTACTS删除，contact_runtime为服务器主世界不持久化/不同步附件；火焰放射用玩家UUID，火柱实体载体用实体UUID，两真实调用均显式传ServerLevel。clear玩家仅清对应来源，停服移除整服附件；SHIELDS及其他遗留表尚未全迁。

新增17模块合同覆盖6MP/20发、资格/心跳/不足MP/小数信用、停止重建/异常/玩家隔离，锥形边界与顺序，命中窗口期限/来源隔离/倒退时间，以及停止重开不重置命中窗口与信用的核心组合。另有2000组旧锥形表达式差分采样。完整离线build退出0：63单测/171模块测试，旧15,658行为断言、102定义/教材、资源和JAR检查通过；证据r06-flamethrower-build.log、JUnit汇总与exit-audit。

已验证核心/通道/命中窗口切片SLICE_DONE；共享世界权限/射线/原版伤害与客户端拆分仍待后续，不等同全部法术重构完成。下一任务R06-wind_slice：核对风裂生物直击与3格AOE的共享身份窗口、原规格首击描述和当前多接触行为，再迁准备释放与纯命中协调。实际游戏/Mixin/多服务器附件/权限/保存/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 风裂准备/释放与命中协调检查点（2026-11-12）

WindSliceSpec提取8MP/7伤害/160tick准备/10tick恢复；ProjectileComposition显式绑定PhaseProjectileAdapter，真实Preparing/Command/Factory/Observer接入，不改实体/资源/保存ID。PhaseTwoEntity接已有PiercingFlightRules到期/候选/步内命中判定，保留120tick有效预算及已保存Remaining优先，不误用历史lifetime100/range20。

WindSliceHit与WindSliceHitAdapter提取原3格球形范围、候选与共享ImpactHistory协调；候选仍在任何伤害前完成中心距离<=9/allowed筛选，保持原枚举顺序、不新增逐目标视线。直击在fly已claim仍伤害，范围目标claim成功才伤害；被范围提前claim者不会再触发直击。不同新直击可再次触发群伤但整发共享去重。未知部分伤害异常不回滚已claim身份。

新增9模块合同：准备/一次收费/失败恢复/资格取消、直击预claim、范围提前claim、重叠群伤/历史重建、异常、球形与飞行边界。完整build --offline退出0，63单测/180模块测试，旧15,658行为断言及102有效定义/教材、资源/JAR检查通过。首次构建因测试import指向fire.wind_slice失败，改为wind.wind_slice后通过；失败日志与最终日志均保留。

当前仍R06-wind_slice IN_PROGRESS，不收尾：D-006明确记录正式“首个目标AOE”与当前每个新接触可再AOE的差异；本轮只保留现有行为做架构迁移，没有反向改设定。下一动作补付费发射→多候选/范围claim→保存历史重建的组合合同，完成与旧实现的命中序列差分并审计实体生命周期；未澄清D-006前不得宣称整术设定无差异或全迁完成。实际游戏/权限/重载/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 风裂序列差分与生命周期检查点（2026-11-12）

新增付费释放→范围身份记录→历史快照重建组合合同及2000组、每组10次接触的冻结旧算法序列差分。对比有序伤害尝试与最终UUID集合，覆盖重叠候选、先范围命中后直击、接触中途快照重建；全部通过。测试候选由替身提供，不证明真实世界枚举或Codec运行。

实体源码审计：Spell/MagicAge/Remaining/AimYaw/AimPitch/MagicVictims读写未改；未加载当前/前方区块先返回，年龄不推进；age>=saved remaining先discard，随后才++age/fly；风裂无需在线owner才能飞行，伤害仍经原allowed/hurtServer/附效/hooks。客户端轨迹、范围burst及原资源未改。共享实体保存与世界碰撞仍留后续职责迁移，不称全部完成。

完整build --offline退出0，63单测/182模块测试，旧15,658断言和102有效定义/教材、资源/JAR检查通过。证据r06-wind-sequence-build.log、r06-wind-sequence-junit-summary.json及XML。D-006依旧未决，本轮未改变首击/后续AOE玩法。当前风裂仍IN_PROGRESS，不把差分等价写成设定验收。

下一动作：核对风裂剩余共享实体职责与已验证切片退出边界，单列D-006后续决策项；可继续不依赖此玩法决定的风缚入口审计，禁止擅自解决D-006或反改正式设定。游戏/权限/保存重载/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 风裂结构切片边界与风缚规则开始（2026-11-12）

风裂已迁准备/释放/纯飞行判定/AOE身份协调，2000组10接触序列差分与付费/历史组合通过。本术结构等价核心切片SLICE_DONE，不代表整术设定符合性：D-006仍独立未决，旧流程按每个新直击可再次AOE保持，不修改正式设定。共享世界候选/实体保存与客户端继续后续职责阶段处理，不因切片收尾删除兼容实体。

当前R06-wind_bind IN_PROGRESS。WindBindSpec.pulse已接PhaseTwoRules.bindPulse，PULSE_DAMAGE接原WindBound.applyEffectTick。剩余100递减时触发80/60/40/20/1，末次为remaining1，不能重写为0或100即时伤害；注册wind_bound、原版magic伤害源、附效放置/同步wind_bound_visual和移动Mixin原样。新增3模块合同含-1000..1000旧谓词差分；完整离线build退出0，63单测/185模块测试，旧15,658断言/102定义教材/资源/JAR检查通过。证据r06-wind-bind-pulse-build.log及JUnit汇总。

下一精确动作：为风缚提取即时生物选点与addEffect窄Adapter，显式绑定既有LocalEffect Command/Preparing；保留免疫tag、addEffect false退款与成功后才同步visual=true，补准备/拒绝/重复/五次脉冲组合合同。D-006留差异账本不擅自解决。游戏/Mixin/权限/药水实际调度/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 风缚准备与释放检查点（2026-11-12）

WindBindAdapter实现既有LocalEffect端口；沿用12格trace、wind_bind_immune免疫tag和原版addEffect(100tick,0,player)。locate先清理并缓存单次选点，place消费缓存；保留旧配置但未加入世界的carrier构造。WindBindEffect负责先应用、成功后才同步wind_bound_visual；false不显示。旧instant入口复用Adapter，不额外收费。

LocalEffectComposition显式绑定风缚，MagicPreparation复用Preparing/Idle与CastLocalEffectCommand。无目标不付款；明确false恢复付款前完整状态；未知部分效果异常不伪造回滚。成功后仍按恢复→SequentialMagic→发光→音效通知。未新增空Factory或通用解释器。

完整离线build退出0；63单测/195模块测试，旧15,658行为断言、102有效定义/教材、旧保存fixture及资源/JAR检查通过。10项新增模块合同覆盖准备160tick、拒绝/取消/重复、扣10MP/恢复10、失败退款、异常边界及效果→风带→通知→五次延迟脉冲。纯端口合同不等于真实Fabric运行验收。证据r06-wind-bind-release-build.log及对应JUnit汇总/XML。

当前风缚仍IN_PROGRESS。下一精确动作：审计药水注册/原版效果调度、附件同步清理、移动Mixin及存档调用边界；能离线验证的仅记核心合同，真实运行/视觉继续NOT_RUN，再决定风缚结构切片收尾并建立wind_blast卡。D-006保持未决。不启动游戏，不自动Git操作。

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

## 土落弹生成几何与准备释放（2026-11-12）

EarthPillarSpawn提取选点上移6格及3×3×3边界纯几何，10,000组冻结位置差分通过。EarthPillarAdapter保留先trace再aim、aim为空拒绝、优先hit.base底面中心否则block位置；place保留loaded检查与原BlockPos枚举/1E-6上边界、全空气检查、向下方向和addFreshEntity返回值。LocalEffectComposition接入既有Preparing/Command，false恢复付款前状态；兼容execute复用同Adapter。未改实体、模型、纹理或保存ID。

完整离线build通过，63单测/232模块测试、旧15,658行为断言和102定义教材、资源/JAR检查通过。证据r06-earth-pillar-spawn-build.log及JUnit汇总/XML。生成几何合同不等于世界空间、权限或实际释放验收。

当前R06-earth_pillar仍IN_PROGRESS。下一精确动作：核对有效准备/恢复参数并补释放拒绝/退款合同，提取下坠实体的步进/方块流体停止/生物穿透规则和永久石头放置协调；保留卸载挂起、无固定寿命、存档键与原伤害。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 土落弹下坠规则与释放合同（2026-11-12）

构造后基线确认14MP/8伤害/160tick准备/10恢复。EarthPillarFall已接实体speed与fall接触/停止判定：固定0.5，contact<=min(0.5,stop)，历史命中过滤且claim后才伤害；stop<=0.5停止，移动量max(0,stop)。10,000组冻结输入差分、同停止面等号/历史/无穷停止距离边界通过。

新增6项LocalEffect核心释放合同覆盖准备、单次扣14MP/10恢复、拒绝/false完整退款、重复、异常付款保留、观察者和取消。连同2项下坠规则测试，完整离线build退出0，63单测/240模块测试，旧15,658行为断言、102定义教材和资源/JAR检查通过。证据r06-earth-pillar-fall-build.log及JUnit汇总/XML。

实体仍在虚空检查后检查加载边界，不设fallingRock寿命上限；age递增位置、MagicVictims/Spell/MagicAge/Remaining/AimYaw/AimPitch保存不改。碰撞流体/方块搜集、命中顺序、停止后burst→永久石头→discard仍原流程，尚未提取协调。真实世界/Mixin/权限/视觉/联机/性能NOT_RUN。

当前R06-earth_pillar仍IN_PROGRESS。下一精确动作：提取永久石头全空间权限预检/放置协调和下坠停止效果Adapter，补穿透命中历史/停止组合合同并审计保存边界；不改变原方块/流体接触及卸载语义。不启动游戏、不自动Git操作。

## 土落弹永久石头预检协调（2026-11-13）

EarthPillarStone已接原permanentStone：保留原BlockPos.betweenClosed枚举顺序及immutable格点；全部mayChange且空气通过后才开始放置，仍setBlockAndUpdate且忽略boolean，不替换已有方块。提前物化纯格点列表不查询世界；权限和空气仍按原顺序短路查询。未知部分放置异常传播，不新增回滚。

新增3项合同覆盖27格完整预检先于任意放置、27个拒绝位置均无放置、部分异常保留已放置边界。完整离线build通过，63单测/243模块测试，旧15,658行为断言、102有效定义教材及资源/JAR检查通过。证据r06-pillar-stone-build.log及JUnit汇总/XML。真实权限/方块更新/视觉未运行。

当前R06-earth_pillar仍IN_PROGRESS。下一精确动作：提取下坠停止的移动/更新边界→burst→有owner才放石→discard协调Adapter，补穿透历史与停止组合合同及保存边界审计。无固定寿命、卸载挂起、原存档键不变。游戏/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 土落弹停止协调（2026-11-13）

EarthPillarStop及世界Adapter已接真实fall停止分支，保持移动并更新bounds→burst→有owner才永久石头→discard。永久石头返回false仍移除；未知异常仍传播，不新增finally移除或地形回滚。石头中心的ceil(y-1E-6)、朝下特效方向及速度/伤害/保存字段不变。

新增3项合同覆盖完整顺序、无owner、放石异常的部分效果边界。完整离线build通过，63单测/246模块测试，旧15,658行为断言、102有效定义教材及资源/JAR检查通过。证据r06-pillar-stop-build.log及JUnit汇总/XML。端口合同不代表实际世界地形、伤害或视觉验收。

当前R06-earth_pillar仍IN_PROGRESS。下一精确动作：补穿透命中历史与停止效果组合合同，审计未加载暂停、虚空移除、无固定寿命与保存恢复，然后仅关闭已验证结构切片。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 土落弹结构切片收尾（2026-11-13）

新增2项命中历史/停止组合合同，恢复历史抑制重复、新目标同停止面可命中，穿透多目标先claim，放石预检false仍discard。完整离线build通过，63单测/248模块测试、旧15,658断言、102定义教材及资源/JAR检查通过。证据r06-pillar-history-build.log和JUnit汇总/XML。

保存/加载/虚空/无寿命及接触顺序源码审计见evidence/r06-earth-pillar-exit-audit.md。明确内部扫掠加载检查位于age递增之后，不笼统承诺所有挂起都不增age；fallingRock不依赖age超时。实际世界与保存重载NOT_RUN。土落弹仅核心/协调结构切片SLICE_DONE。

当前R06-ex_healing IN_PROGRESS（仅新任务卡，尚未迁代码）。下一精确动作：审计中级治愈有效参数、缺血资格和治疗量、付款/原版heal与通知顺序，再提取纯治疗规则和自身效果Adapter，复用SelfEffect Command。D-006未决；不启动游戏、不自动Git操作。

## 中级治愈纯规则与自身释放（2026-11-13）

有效基线16MP/160tick准备/10恢复/最多8生命。ExHealingRules提取finite、存活生命值及min(8,max(0,max-health))；SpellDefinition中级治愈分支委托，其他治疗定义保持。10,000组冻结生命对差分与满血/死亡/非有限/小缺血边界通过。ExHealingAdapter仍调用原版heal并返回true，包括兼容execute的0治疗；缺血资格仍由准备前及释放前canExecute核验。不得额外增加付款后的资格拒绝改变旧顺序。

SelfEffectComposition显式绑定并复用Preparing/Command/成功通知，不新造Factory。完整离线build通过，63单测/250模块测试、旧15,658断言、102定义教材及资源/JAR检查通过。证据r06-ex-healing-build.log及JUnit汇总/XML。真实原版heal、附着回调与事件兼容尚NOT_RUN。

当前R06-ex_healing仍IN_PROGRESS。下一精确动作：补16MP准备/释放与治疗组合合同，审计满血取消、重复输入、异常和兼容入口，核对后仅关闭核心结构切片。高阶治疗/疾病继续暂缓。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 中级治愈准备/释放合同（2026-11-13）

新增6项核心合同覆盖160tick准备、扣16MP后计算小缺血治疗量并通知、非法角度/不足MP/未学拒绝、单次命令、false退款、部分异常付款保留、观察者隔离与取消。完整离线build通过，63单测/256模块测试，旧15,658行为断言、102定义教材及资源/JAR检查通过。证据r06-heal-contracts-build.log与JUnit汇总/XML。

源码核对：MagicPreparation.request和释放前均用canExecute检查治疗量>0；准备期间回满血会在释放前返回且不扣费。SelfEffectCommand本身不重做缺血资格，兼容execute仍允许原版heal(0)并返回true；原版heal/模组事件真实运行NOT_RUN。上述满血资格为源码审计，未把测试替身当Fabric运行证明。

当前R06-ex_healing仍IN_PROGRESS。下一精确动作：将治疗量读取→原版heal的窄端口协调与真实Adapter接通，补零治疗兼容/读取顺序/异常合同，完成核心切片退出审计。高阶治疗/疾病继续暂缓。不启动游戏、不自动Git操作。

## 中级治愈核心切片收尾（2026-11-13）

ExHealingEffect.Patient窄端口已接真实Adapter，依次读health/maximum，计算后调用原版heal；零治疗仍调用并成功，异常传播不重试。3项新合同验证顺序、零治疗兼容及异常。完整离线build通过，63单测/259模块测试、旧15,658断言、102定义教材及资源/JAR检查通过；证据r06-heal-effect-build.log及JUnit汇总/XML。

准备前/释放前缺血检查仍由MagicPreparation→canExecute处理，付款后的heal调用保持旧行为。无新实体、网络或保存ID、无独立运行容器；死亡/跨维/断线共享准备取消未改。仅核心/协调结构切片SLICE_DONE，真实原版治疗/事件、视觉、联机、性能NOT_RUN。

账本归属校正：原MIGRATION_LEDGER将ex_healing归R08，之前R06任务卡命名是执行过程偏移，不代表R06或R08整阶段完成。保留历史tasks/R06-ex_healing.md及证据路径，账本仍R08-ex_healing并链接该历史卡；不擅自宣称全阶段退出。下一动作回到R06剩余项physical_shield审计，先读原39术规格与实际入口再建实施切片。高阶治疗/疾病仍暂缓。不启动游戏、不自动Git操作。

## 物理障壁续期与替换协调（2026-11-13）

PhysicalShieldRules提取renew资格：未移除、维护未中断、同世界、锚点距离平方<=1E-6；续期复用既有EffectRules.extend加160，容量重置16，已接实体。PhysicalShieldRelease已接原shield入口：先尝试旧盾续期；失败才按完整准星方向在眼前1格生成；addFreshEntity=false保留旧盾；成功才discard旧盾再登记。世界适配当前为原入口内的窄接口实现，未迁运行容器。

4项模块合同覆盖续期不生成、新盾false保留旧盾、替换顺序与资格边界。完整离线build通过，63单测/263模块测试、旧15,658断言、102定义教材与资源/JAR检查通过。证据r06-physical-shield-start-build.log及JUnit汇总/XML。没有改伤害、碰撞、续期时长、资源/保存ID或原版速度。

当前R06-physical_shield仍IN_PROGRESS。下一精确动作：将SHIELDS静态Map迁为服务器拥有的运行容器，核对近战/弹体拦截、clear与死亡/跨维/停服调用；补实例隔离及释放付款合同，再拆碰撞窄Adapter。不得先删实体或仅改类名。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 物理障壁服务器运行容器（2026-11-13）

SHIELDS静态Map已移除；PhysicalShieldRegistry实例由服务器overworld的physical_shield_runtime非持久/不同步附件拥有。近战ALLOW_DAMAGE按目标服务器取容器，弹体intercept仅ServerLevel入口按该服务器取容器；释放/续期/维护中断/tick/clear均使用同一容器。保持HashMap值迭代语义及原跨维过滤，不新增拦截排序。无旧保存/网络键变更，新附件仅运行态。

原LEAVE/AFTER_RESPAWN/跨维clear仍移除玩家盾并discard，tick仍清理死亡/异维/已移除盾；SERVER_STOPPING原在线玩家clear后clearChannels再removeAttached(SHIELD_RUNTIME)。服务器实例释放不保留静态实体引用。新增3项容器合同覆盖相同UUID跨实例隔离、替换/移除及新实例无历史；不等价于真实双服Fabric测试。

完整离线build通过，63单测/266模块测试，旧15,658断言、102定义教材及资源/JAR检查通过。证据r06-shield-registry-build.log及JUnit汇总/XML。真实事件、碰撞、视觉/联机/性能NOT_RUN。

当前R06-physical_shield仍IN_PROGRESS。下一精确动作：补物理盾14MP准备/续期与替换付款合同，接入自身释放Command；拆近战/物理弹体候选与容量扣减协调，保留原魔法排除、三叉戟反弹及移动裁剪行为。不启动游戏、不自动Git操作。

## 物理障壁准备与付款释放（2026-11-13）

PhysicalShieldAdapter实现SelfEffect，SelfEffectComposition显式绑定；MagicPreparation复用Preparing/Idle和单次CastSelfEffectCommand，参数保持160tick/14MP/10恢复。原PhaseTwoSpells.execute仍调用同PhaseTwoRuntime.shield作为不额外收费的兼容入口。成功续期也付14MP；新盾生成false回滚完整玩家状态且旧盾未删除；未知部分异常保留付款，观察者保持成功后顺序与隔离。不新增Factory/通用效果解释器。

7项新合同覆盖准备、角度/MP/学习拒绝、单次、false退款、部分异常、通知隔离/取消，并组合真实核心PhysicalShieldRelease验证付款后续期成功及替换失败。完整离线build通过，63单测/273模块测试，旧15,658断言、102定义教材与资源/JAR检查通过。证据r06-shield-command-build.log及JUnit汇总/XML。真实实体生成、附件回调与世界运行仍NOT_RUN。

当前R06-physical_shield仍IN_PROGRESS。下一精确动作：提取近战/物理弹体拦截和容量扣减协调，保留魔法排除、原候选遍历/最近接触、原版地形遮挡、三叉戟反弹及移动裁剪，补碰撞/生命周期退出审计。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 物理障壁容量协调（2026-11-13）

PhysicalShieldAbsorb已接真实实体absorb，复用EffectRules.shieldAfter，不改变IEEE/负伤害边界；保持写capacity→服务端burst→读取当前capacity<=0时discard。视觉回调可能影响状态，仍在show后重新读取容量，不用提前缓存决定。异常原样传播，不增加finally破坏或退款。

3项模块合同覆盖累计16伤害耗尽后的顺序、10,000组有限输入冻结差分及视觉异常保留已写容量。完整离线build通过，63单测/276模块测试，旧15,658断言、102有效定义教材及资源/JAR检查通过。证据r06-shield-absorb-build.log及JUnit汇总/XML。真实伤害事件、容量特效与碰撞尚NOT_RUN。

当前R06-physical_shield仍IN_PROGRESS。下一精确动作：拆近战/物理弹体候选选择与拦截后动作，保留魔法排除、同owner过滤、方向、最近接触与原地形clip、三叉戟反弹及其他弹体discard；之后审计移动裁剪与生命周期。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 物理障壁弹体拦截协调（2026-11-13）

PhysicalShieldInterception已接PhaseTwoRuntime.intercept的选中接触后流程：地形clip无阻挡才计算真实来袭伤害，先absorb，再判断三叉戟并反弹，其他弹体discard。保留反弹位置回退0.1和速度乘-0.1；异常传播，不增加finally清理。魔法排除、速度阈值、同owner身份比较、方向过滤、严格小于的最近接触及同距首个候选均仍为原实现，尚未提取。

新增5项模块合同验证遮挡短路、普通弹体顺序、三叉戟反弹、吸收后读取类型与异常不清理。完整离线build通过：63单测/281模块测试；旧15,658行为断言、102有效定义及教材/资源/JAR检查通过。证据r06-shield-interception-build.log和JUnit汇总/XML。此为核心协调合同，真实Fabric碰撞/视觉/联机/性能均NOT_RUN。

当前R06-physical_shield仍IN_PROGRESS。下一步提取近战/弹体候选选择，保留近战首相交与弹体最近接触的不同语义，然后审计移动裁剪及生命周期；不启动游戏、不自动Git操作。

## 物理障壁候选选择与退出审计（2026-11-13）

PhysicalShieldSelection已接真实伤害事件与弹体入口，近战首有限相交、弹体严格最近/同距首个分开实现；过滤条件仍在世界适配lambda，保持短路与原身份比较。无额外设计模式或排序。新增7项模块合同含10,000随机序列冻结差分、过滤短路、首相交/最近差异及非有限值兼容。

完整离线build通过：63单测/288模块测试，旧15,658行为断言和102定义/教材/资源/JAR校验通过。证据r06-shield-selection-build.log及JUnit汇总/XML；移动及生命周期源码核对见evidence/r06-shield-movement-lifecycle-audit.md（相对architecture）。实际游戏/Mixin/视觉/联机/性能NOT_RUN。

R06-physical_shield仍IN_PROGRESS；下一步把重叠运动与截断公式迁入core，保留原侧退出、相交立即返回、0.001退让及原候选迭代，补冻结差分后核对退出条件再判断结构切片是否可收尾。不启动游戏、不自动Git操作。

## 物理障壁移动标量规则（2026-11-13）

PhysicalShieldMovement接入PhaseTwoRules.shieldOverlapMotion与PhaseTwoRuntime.clipShieldMovement：原侧退出判定、严格接触比较与0.001格退让缩放均为JDK纯函数。世界向量点积/法向投影、equals判断、查询循环与重叠立即返回仍在适配层；没有将其宣称为已全部迁出。刻意不经Vec3d有限值构造，避免新增IEEE异常边界。

新增5项模块测试覆盖两侧进出、精确阈值、接触退让、NaN/无穷比较及20,000组冻结算式差分。完整离线build通过：63单测/293模块测试，旧15,658断言、102有效定义及教材/资源/JAR检查通过。证据r06-shield-movement-build.log与JUnit汇总/XML。实际游戏、Mixin、视觉、联机、性能仍NOT_RUN，不启动游戏、不自动Git操作。

当前R06-physical_shield仍IN_PROGRESS。下一步把物理盾伤害事件/弹体/移动世界适配从PhaseTwoRuntime集中搬入PhysicalShieldCollisionAdapter，沿用现有纯规则与服务器registry，不新增重复容器；核对调用链与退出清单后仅关闭可证明的结构切片。实体通用载体后续迁移与真实世界验收仍单独保留。

## 物理障壁碰撞世界适配迁移（2026-11-13）

PhysicalShieldCollisionAdapter集中ALLOW_DAMAGE注册/处理、弹体intercept及移动clip，复用已迁core候选、吸收协调和移动标量。PhaseTwoRuntime只保留这些入口的兼容委托，原初始化和调用顺序不变。PhysicalShieldRuntimeAdapter拥有唯一physical_shield_runtime非持久/不同步附件，原服务器overworld取容器和停服removeAttached保持；源码扫描确认只注册一处，未新建第二Map或持久键。

完整离线build通过，63单测/293模块测试；本轮为适配搬移，没有新增伪装成世界验收的mock测试。旧15,658行为断言、102有效定义及教材/资源/JAR校验通过。证据r06-shield-adapter-build.log及JUnit汇总。实际Fabric事件、Mixin、双服运行隔离、视觉、联机、性能仍NOT_RUN。

R06-physical_shield仍IN_PROGRESS：释放/续期/维护中断及玩家tick/clear协调尚在PhaseTwoRuntime，下一步迁至PhysicalShieldRuntimeAdapter并将SelfEffect适配直接接新入口；保留生成false旧盾不删、先discard后登记、死亡/跨维清理顺序，然后评估结构切片退出。实体通用载体迁移与真实世界验收仍单独保留。不启动游戏、不自动Git操作。

## 物理障壁释放与生命周期适配迁移（2026-11-13）

PhysicalShieldRuntimeAdapter现拥有释放/续期World适配、breakMaintenance、玩家tick/clear及唯一服务器附件。PhysicalShieldAdapter直接调用release，原PhaseTwoRuntime.shield/breakMaintenance仍为兼容委托，PhaseTwoSpells旧入口不额外付费。生成false不删旧盾，成功discardPrevious后remember，实体方向/眼前1格/参数均不变。

PhaseTwoRuntime.tick仍先tickColumns/tickBurns，再护盾清理，再contact prune及通道；护盾tick先discard再remove。clear仍先stop，再columns/burns移除，再护盾remove后discard，最后contact clear。停服附件移除位置不变；没有新增异步、重试、finally或状态容器。

完整离线build通过，63单测/293模块测试；旧15,658断言、102有效定义及教材/资源/JAR校验通过。证据r06-shield-runtime-build.log及JUnit汇总。此次为世界适配搬移，未新增测试数量，未把核心合同冒充实际Fabric生命周期测试。游戏/Mixin/视觉/联机/性能均NOT_RUN。

R06-physical_shield保持IN_PROGRESS：下一步对照批准提案及任务退出清单完成结构切片验收，核对残留PhaseTwoEntity载体和兼容入口的后续归属、R07账本阶段与历史R06卡命名，再选择下一未完成任务；不能据此宣称整阶段或整项目重构完成。不启动游戏、不自动Git操作。

## 物理障壁结构切片收尾，狱炎火弹开始（2026-11-13）

物理障壁核心/协调/世界适配切片SLICE_DONE；账本归R07，历史R06卡路径保留，残留实体载体/兼容委托的职责和清理条件见architecture/evidence/r07-physical-shield-structure-exit.md。不是整术/整阶段完成。返回R06第一项未开始术exodus_flame；已提球形半径判定并接真实爆发，准备释放尚待迁。

完整离线build通过：63单测/296模块测试；新增3项及10,000距离差分，旧15,658断言、102有效定义/教材/资源/JAR检查通过。证据r06-exodus-start-build.log与JUnit汇总/XML。未启动游戏、未Git操作；实际Fabric/Mixin/视觉/联机/性能NOT_RUN。

## 狱炎火弹准备与付费释放（2026-11-13）

ExodusFlameSpec及ProjectileComposition显式Binding已接：44MP、240tick准备、20恢复、速度2/散射0，沿请求准星从眼部原点发射。MagicPreparation自动选既有Preparing/Idle与CastSpellCommand，复用VanillaProjectileAdapter；IntermediateProjectile默认paid10与旧兼容入口一致，不误用44作为岩炮弹注魔数据。SpellDefinition有效值基线保持。旧IntermediateSpells兼容执行仍保留，不额外收费、不双发。

新增4项模块合同覆盖准备不收费/单次提交/正确plan，false及生成异常恢复完整快照，MP/知识/非法角度拒绝和取消/心跳超时。完整离线build通过：63单测/300模块测试，旧15,658断言、102定义及教材/资源/JAR检查通过。证据r06-exodus-release-build.log及JUnit汇总/XML。初次编译的ProjectileSpec访问器命名错误已修正为mana()后重建通过。实际实体生成、Fabric/Mixin/视觉/联机/性能NOT_RUN。

R06-exodus_flame仍IN_PROGRESS。下一步提取exodusBurst的逐目标动作协调：先查历史/半径/可见性/权限，再claim UUID、真实伤害，成功后owner自燃或其他目标MagicEffects，再afterHit；保留80%随机采样和火免疫判断顺序，补拒绝伤害、去重与异常合同，再审计飞行退出及保存。不得把此切片当整术完成。不启动游戏、不自动Git操作。

## 狱炎火弹逐目标爆发协调（2026-11-13）

ExodusFlameHit已接IntermediateProjectile.exodusBurst：历史→半径→可见性→owner/权限→claim→hurt，伤害成功后重新读取owner身份，走selfEffect或otherEffect再afterHit。伤害拒绝或抛异常仍保留claim，附效异常不继续afterHit；不添加回滚、重试或finally。世界窄端口保留真实目标/UUID/伤害源/权限，owner自燃仍先采随机数<0.8，再检查火免疫和抗火，其他目标继续MagicEffects.apply。

新增8项模块合同覆盖真实核心协调顺序、owner分支、拒绝后去重、半径/权限/可见性短路、伤害/附效异常及伤害后身份重读。完整离线build通过：63单测/308模块测试；旧15,658断言、102有效定义及教材/资源/JAR检查通过。证据r06-exodus-burst-build.log及JUnit汇总/XML。合同不执行真实Minecraft伤害事件、随机数或Codec；游戏/Mixin/视觉/联机/性能NOT_RUN。

R06-exodus_flame仍IN_PROGRESS。下一步审计并提取飞行生物接触/墙或流体/寿命/未加载退出与命中历史重建协调，保持同目标整发一次、爆发可波及owner、墙前偏移及原保存键；之后再迁爆发世界Adapter和评估结构退出。不启动游戏、不自动Git操作。

## 狱炎火弹飞行边界与保存审计（2026-11-13）

ExodusFlameFlight接入寿命/低速退出、候选严格早于墙流体、最终步内接触及再次去重判断；保留原短路命中历史读取。4项模块合同及10,000组冻结边界差分通过。共享HashSet及MagicVictims Codec保持，不为本术更改其他弹体历史语义。

完整离线build通过：63单测/312模块测试，旧15,658断言、102有效定义及教材/资源/JAR校验通过。证据r06-exodus-flight-build.log、JUnit汇总/XML和r06-exodus-flight-audit.md。实际世界碰撞/Codec重载/视觉/联机/性能NOT_RUN。

当前R06-exodus_flame仍IN_PROGRESS。下一步迁爆发世界适配至ExodusFlameBurstAdapter，采用原共享Set引用或窄历史端口，不复制/重置历史；补历史重建→爆发拒绝去重的核心组合合同，再核对结构切片退出。实体共享扫掠/存档仍需后续阶段，不把源码审计当实测。不启动游戏、不自动Git操作。

## 狱炎火弹爆发世界适配与共享历史（2026-11-13）

ExodusFlameBurstAdapter已承接世界目标查询、中心反向偏移0.025、真实伤害/自燃/附效/afterHit端口；IntermediateProjectile.exodusBurst仅委托，并传原hit Set引用。未复制或替换集合，直接接触和整发所有爆发仍共享同一历史。原实体/资源/保存键保持。

新增1项组合合同：用已保存UUID集合重建历史，历史目标跳过；新目标伤害拒绝后仍claim，后续飞行接触和再爆发跳过，重建集合保留两者；此为核心Set与协调器组合，不执行真实Minecraft Codec。完整离线build通过：63单测/313模块测试，旧15,658行为断言、102有效定义及教材/资源/JAR校验通过。证据r06-exodus-adapter-build.log及JUnit汇总/XML。

R06-exodus_flame仍IN_PROGRESS。下一步核对结构退出清单，查明IntermediateProjectile里剩余本术尺寸/飞行分支的载体职责和后续迁移归属，核对兼容执行与新Command只有一个生效入口；完成有证据的结构切片后建立账本下一项R06-tornado_impact任务卡。真实游戏/Mixin/伤害事件/Codec重载/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 狱炎火弹切片收尾，风枪龙卷开始（2026-11-13）

狱炎火弹核心/准备释放/爆发适配结构SLICE_DONE，残留共享载体及兼容路径退出条件见architecture/evidence/r06-exodus-structure-exit.md。不是整术全迁。下一账本项tornado_impact正式名风枪龙卷，已接TornadoImpactSpec/ProjectileComposition和既有Preparing/Command，保留38MP/240准备/20恢复/速度3散射0及原实体。

4项新准备释放模块合同通过。完整离线build：63单测/317模块测试，旧15,658断言、102定义/教材/资源/JAR校验通过，证据r06-tornado-start-build.log与JUnit汇总/XML。下一步迁命中冲量协调与永久地形预算；真实游戏/Mixin/权限/视觉/联机/性能NOT_RUN。不启动游戏、不Git操作。

## 风枪龙卷命中协调（2026-11-13）

TornadoImpactHit与TornadoImpactHitAdapter已接IntermediateSpells.hurt单术分支并立即return，旧重复冲量分支删除。保持hurt成功后才读BIND_IMMUNE，未免疫才加归一化运动冲量并标hurtMarked；随后原knockback条件、MagicEffects、afterHit。免疫标签仅影响冲量，不阻止伤害或附效。未引入回滚或重试。

5项新模块合同覆盖成功顺序、伤害拒绝短路、免疫仅抑制冲量及冲量/附效异常截断。完整离线build通过：63单测/322模块测试，旧15,658断言、102有效定义/教材/资源/JAR校验通过。证据r06-tornado-hit-build.log与JUnit汇总/XML。实际原版伤害、冲量及标签事件/视觉/联机/性能NOT_RUN。

R06-tornado_impact仍IN_PROGRESS。下一步提取永久地形路径预算协调：沿原按接触距离排序的候选，累计成功破坏最多64格，达到上限或首个破坏false格即停止；保留成功后才写expansionBroken、异常不虚增、原保存clamp及实际权限/掉落适配。然后审计飞行退出和命中历史。不启动游戏、不自动Git操作。

## 风枪龙卷永久地形预算协调（2026-11-13）

TornadoImpactTerrain.firstObstacle接入原永久模式/ServerPlayer条件分支。世界仍按原contacts HashMap entrySet和接触距离排序，不新增同距排序；协调沿该序列读取当前expansionBroken，<64才尝试原ExpansionTerrain.breakStoneHarvestable，成功立即increment并继续，失败/达到上限返回首障碍；返回后原wall/block/impact写入保持。空序列不读预算。原ExpansionBroken保存键与load clamp未改，权限/采集/掉落不替换。

6项新合同覆盖63→64后停止、首个false、不重排、异常保留此前成功计数、恢复64不再调用破坏、空序列。完整离线build通过：63单测/328模块测试，旧15,658断言、102有效定义/教材/资源/JAR检查通过。证据r06-tornado-terrain-build.log及JUnit汇总/XML。真实地形权限/掉落/Codec重载/视觉/联机/性能NOT_RUN。

R06-tornado_impact仍IN_PROGRESS。下一步审计并提取飞行寿命/低速/未加载退出、尺寸与候选规则，补命中历史重建及已破坏预算跨tick组合合同，核对液体/墙处理顺序；再评估结构退出，不把预算协调当全术完成。不启动游戏、不自动Git操作。

## 风枪龙卷飞行边界与跨tick合同（2026-11-13）

TornadoImpactFlight接入寿命/低速和候选严格接触判断，体积尺寸从TornadoImpactSpec读取。4项新合同覆盖边界、历史Set重建与62→63→64跨tick破坏预算/恢复到上限、10,000候选算式差分。共享Codec及集合保持原实现，不称真实重载验收。

完整离线build通过：63单测/332模块测试，旧15,658断言、102有效定义/教材/资源/JAR检查通过。证据r06-tornado-flight-build.log及JUnit汇总/XML；世界退出和地形先于流体的历史顺序见r06-tornado-flight-audit.md。游戏/Mixin/视觉/联机/性能NOT_RUN。

R06-tornado_impact仍IN_PROGRESS。下一步核对地形窄World适配的最终归属与结构退出清单，保留共享实体扫掠/存档职责；证明只有单一命中/释放路径后关闭有证据的结构切片，按账本建立R06-frost_nova任务卡。不启动游戏、不自动Git操作。

## 风枪龙卷结构切片收尾，冰霜新星开始（2026-11-13）

风枪龙卷核心/协调结构SLICE_DONE，地形匿名World适配与共享实体扫掠/保存的残留归属见architecture/evidence/r06-tornado-structure-exit.md，非整术全迁。冰霜新星已接FrostNovaRules：半径8、10/14伤害、300冻结、20/40控制时长，原温暖和状态消费顺序尚在旧世界实现待迁。

完整离线build通过：63单测/335模块测试，新增3项规则合同，旧15,658断言、102定义/教材/资源/JAR校验通过。证据r06-frost-start-build.log及JUnit汇总/XML。当前R06-frost_nova IN_PROGRESS，下一步逐目标命中协调/世界Adapter与SelfEffect准备付款。实际游戏/Mixin/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 冰霜新星逐目标命中协调（2026-11-13）

FrostNovaHit接真实FrostNova.cast的目标窄端口，按wet→warm读取快照，再hurt(10/14)；成功后按原快照先移wet再移warm。只有原warm=false才尝试canFreeze/冻结300，然后player判定及20/40tick控制，最后afterHit。冻结免疫不跳过控制；MagicControl实际免疫仍由旧实现判断。伤害失败不消耗状态；异常原样传播，不回滚已发生副作用。

6项新合同验证湿身伤害及顺序、温暖只挡控制、伤害拒绝、冻结免疫/玩家控制、伤害期间状态变化不覆盖快照、移除状态异常截断。完整离线build通过：63单测/341模块测试，旧15,658断言、102有效定义/教材/资源/JAR校验通过。证据r06-frost-hit-build.log及JUnit汇总/XML。真实伤害/控制免疫/游戏/视觉/联机/性能NOT_RUN。

R06-frost_nova仍IN_PROGRESS。下一步把FrostNova.cast世界查询/伤害源/burst迁为FrostNovaAdapter并实现SelfEffect，保留旧cast兼容委托；SelfEffectComposition接40MP/20恢复，复用240tick Preparing，补单次付款/取消/失败及部分副作用异常合同。无独立持久状态，不新增实体tick。不启动游戏、不自动Git操作。

## 冰霜新星世界适配与付费释放（2026-11-13）

FrostNovaAdapter实现SelfEffect，持有原玩家并承接全部世界查询/伤害源/爆发；旧FrostNova.cast仅兼容委托同一静态cast，不另收费或双跑。SelfEffectComposition显式绑定，MagicPreparation自动使用Preparing/Idle与单次CastSelfEffectCommand；保持40MP/240tick准备/20恢复，目标中心仍owner.position+Y1，未addFreshEntity的MagicAreaEntity仍仅作伤害来源，遍历完才burst，空目标仍成功。

新增6项模块合同覆盖准备不收费/单次及通知顺序、非法输入/MP知识拒绝、显式false完整退款、未知部分世界异常保留付款且不可重放、观察者隔离、取消准备。真实Adapter正常仅返回true，false合同是端口失败语义，不声称当前世界会返回false。完整离线build通过：63单测/347模块测试，旧15,658断言、102定义/教材/资源/JAR校验通过。证据r06-frost-release-build.log及JUnit汇总/XML。

当前R06-frost_nova仍IN_PROGRESS。下一步组合真实FrostNovaHit与CastSelfEffectCommand验证付费后状态消耗/伤害拒绝及部分异常，审计无独立保存/tick的生命周期与旧兼容调用、目标过滤和可见性，完成结构退出清单后再推进sonic_blast。游戏/Fabric/Mixin/控制免疫/视觉/联机/性能NOT_RUN。不启动游戏、不自动Git操作。

## 冰霜新星核心组合收尾（2026-11-13）

2项新组合合同通过，付费命令与真实FrostNovaHit组合验证伤害拒绝/状态消费和部分异常不可重放。结构切片SLICE_DONE，退出及无独立tick/保存审计见architecture/evidence/r06-frost-structure-exit.md。完整离线build通过：63单测/349模块测试、旧15,658断言、102定义/教材/资源/JAR通过。证据r06-frost-final-build.log及JUnit汇总/XML。

下一账本项R06-sonic_blast已建任务卡，仅开始源码审计、尚未代码迁移或测试；先核对正式规格再抽锥形纯规则。游戏/Fabric/Mixin/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 爆音冲击波锥形规则（2026-11-13）

正式规格ALL_MAGIC_EXPANSION.md与有效定义核对：40MP、12伤害、16格半角35度、1.2冲量，rank3 FULL为240准备/20恢复。SonicBlastRules接旧contains入口，纯标量运算保留Vec3归一化1E-5阈值、近原点length<1E-6短路、16含边界和IEEE非有限比较；未引入Vec3d有限值拒绝。3项模块合同及10,000组冻结算式随机差分通过。未改命中、永久地形128成功预算、资源、注册或保存ID。

完整离线build退出0，63单测/352模块测试；证据r06-sonic-rules-build.log与JUnit汇总/XML。测试为核心规则合同，非实际世界/事件/Codec/视觉/联机/性能验收，以上均NOT_RUN。R06-sonic_blast仍IN_PROGRESS。

下一精确动作：提取伤害成功→免疫→1.2冲量→afterHit协调（offset平方<.001用原方向）；再提取先完整可见快照、后最多128成功破坏且false继续的地形协调，迁独立Adapter并接SelfEffect准备付款合同。不启动游戏，不Git操作。

## 爆音冲击波结构切片收尾（2026-11-13）

SonicBlastHit/Terrain与SonicBlastAdapter已接真实SelfEffectComposition，保留40MP/240准备/20恢复、伤害成功与免疫顺序、近零冲量、完整可见快照后最多128成功破坏。旧兼容cast仅委托，不重复扣费；无新增实体tick或保存状态。结构SLICE_DONE，残留兼容入口/共享准备容器与实际世界验收边界见architecture/evidence/r06-sonic-structure-exit.md。

新增18项合同，完整离线build通过：63单测/370模块测试、旧15,658断言、102定义/教材和资源/JAR检查通过。证据r06-sonic-exit-build.log及JUnit汇总/XML。首轮测试事件记录变量遮蔽已修正，保留失败日志。合同不是Fabric/世界/事件/视觉/联机/性能验收，以上NOT_RUN。

下一R06-blast已建卡，仅源码审计、尚未迁代码；先核对有效准备/恢复，再抽30度热风锥规则与命中协调。整体架构重构仍未完成。不启动游戏、不Git操作。

## 突风结构切片收尾（2026-11-13）

BlastRules/Hit/Adapter已接SelfEffectComposition与既有准备/单次付款，16MP/160准备/10恢复保持；热风无标准点燃/破坏/反冲，寒冷突风旧分支未迁。结构SLICE_DONE，兼容与生命周期边界见architecture/evidence/r06-blast-structure-exit.md。新增17项模块合同及10,000锥形差分，完整离线build通过：63单测/387模块测试、15,658旧断言、102定义/教材、资源/JAR通过。证据r06-blast-build.log及JUnit汇总/XML。

下一任务R06-bora_blast，仅源码审计尚未迁代码；关键保持伤害后才读取温暖/湿身，与冰霜新星的伤害前快照不同。实际世界/Fabric/Mixin/视觉/联机/性能NOT_RUN，整体架构未完成。不启动游戏、不Git操作。

## 寒冷突风结构切片收尾（2026-11-13）

BoraBlastRules/Hit/Adapter已接SelfEffectComposition，38MP/240准备/20恢复及伤害后温暖/湿身读取顺序保持。新增21项模块合同与10,000随机几何差分通过；完整离线build退出0：63单测/408模块测试，15,658旧断言、102有效定义/教材/资源/JAR通过。证据r06-bora-build.log及JUnit汇总/XML，结构退出边界见architecture/evidence/r06-bora-structure-exit.md。

下一唯一任务R06-exit-audit：核对Intermediate/PhaseTwo/共享准备等残留业务，先形成逐项可执行任务再决定进入R07，不能将单术切片全部通过等同整阶段完成。ThermalGust仍有兼容contains及其他输入fallback，未删除。世界/Fabric/Mixin/事件/Codec/视觉/联机/性能NOT_RUN；不启动游戏、不Git操作。

## 准备会话服务器归属切片（2026-11-13）

MagicPreparation进程全局ACTIVE已删除，PreparationRegistry<Pending>由服务器主世界preparation_runtime附件拥有，仅initializer、无persistent/sync/copyOnDeath。initialize在MagicSystem初始化注册附件；原逐玩家cancel顺序保持，停服逐玩家清理后removeAttached，回收剩余引用。所有get/contains/put/remove从当前玩家所属服务器读取。Pending、准备计时、心跳、付款、取消及特殊术分支未改；这不是整个MagicPreparation业务拆分完成。

4项新增模块合同覆盖同UUID两实例隔离、替换/删除身份、清理/重建、取消一个实例不影响另一个。首次测试误认为显式cancel后Idle.tick还应发布CANCELLED；修正为不能READY，保留现有State生产语义，失败日志r06-preparation-initial-failure.log。最终完整build退出0：63单测/412模块测试、15,658旧断言、102有效定义/教材、资源/JAR及架构边界通过，证据r06-preparation-build.log和JUnit汇总/XML。测试只验证核心实例容器及CastingSession组合，真实Fabric附件/停服/跨维/游戏/Mixin/视觉/联机/性能NOT_RUN。

残余矩阵evidence/r06-residual-matrix.md已记录共享Intermediate/PhaseTwo/准备协调等未迁职责，R06仍未整体退出。下一R06-intermediate-runtime先审计共享扫掠/墙流体/未加载/历史/Codec的旧顺序，建立窄协调切片，不改变TNT、水穿透或费用例外。不游戏、不Git。

## 共享弹体终止协调检查点（2026-11-13）

ProjectileTermination已接IntermediateProjectile最后终止分支：流体<=distance且<=wall优先，position→particles→discard；否则wall<=distance且有block时position→wallEffect→particles→discard。核心无SpellId，匿名World保留狱炎碰墙爆发、随后重新读取owner及水炮/大火球blockImpact；异常保留已发生副作用，不自动discard或落入后续分支。无终止才原setPos(to)/traveled累加。命中历史、Codec、区块/年龄、风枪地形先于流体、候选排序等未改，仍待迁。

新增8项模块合同（3边界/含10,000差分、5动作顺序/异常），完整离线build退出0：63单测/420模块测试；旧15,658断言、102定义/教材、资源/JAR及架构边界通过。证据r06-terminal-build.log、JUnit汇总/XML。合同不是世界/Fabric/Mixin/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS，下一精确动作提取最终生物接触循环协调：保持contact重算、狱炎skipContact与历史短路、非狱炎先claim再setPos/hurt、piercing与非piercing各自particles/非piercing重复定位和discard顺序；先扩任务卡允许路径再实现。不启动游戏、不Git。

## 共享弹体生物接触协调（2026-11-13）

ContactTraversal已接IntermediateProjectile排序后的目标循环，逐目标重算contact，再由skip窄端口保留狱炎历史短路；非狱炎原Set先claim后定位/伤害，狱炎仍由爆发使用原Set。piercing循环前快照；穿透每目标effect后particles，非穿透effect后重复定位→particles→discard→return。核心不分派SpellId，异常保留已发生副作用且不补执行后续动作。候选/排序、世界伤害和爆发、终止、年龄/区块、存档/注册/资源保持。

新增8项核心合同验证顺序、非穿透停止、跳过、后续接触重算、爆发对后续历史的影响、伤害/粒子异常和空列表。完整离线build退出0：63单测/428模块测试，旧15,658断言、102有效定义/教材、资源/JAR及架构边界通过。证据r06-contact-build.log与JUnit汇总/XML。测试桩只验证端口/核心组合，不是实际Minecraft/Fabric/Mixin/伤害/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS。下一精确动作审计并提取候选收集/排序协调，保持各术contact与history求值次序、strict小于墙流体、稳定同距顺序；先更新允许路径和合同再实现。共享载体扫掠/区块/Codec等仍待迁，不能称R06完成。不游戏、不Git。

候选筛选排序已迁ContactCandidates，保留原筛选表达式及求值次序、实时contact稳定排序，不缓存或新增同距排序键。新增5合同、1000组冻结排序差分；完整离线build通过63单测/433模块测试、15,658旧断言、102定义/教材/资源/JAR。证据r06-candidates-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计命中历史保存恢复及MagicVictims UUID容错、PaidMana/ExpansionBroken与年龄边界，先定义合同再迁移。不游戏、不Git。

命中历史字符串格式已迁HitHistoryFormat并由MagicFlight委托。保留MagicVictims及Codec.STRING.listOf、先clear再Supplier读取、UUID.fromString容错、原Set身份与部分异常语义。新增5项合同；完整离线build通过63单测/438模块测试、15,658旧断言、102定义/教材/资源/JAR。证据r06-history-build.log及JUnit汇总/XML。真实Codec重载/世界/Fabric/Mixin/视觉/联机/性能NOT_RUN。PaidMana、ExpansionBroken及MagicAge仍由原载体保存，未改默认值或clamp；下一核对并提取这些读取归一化的纯规则及旧默认值合同。R06-intermediate-runtime仍IN_PROGRESS，不游戏、不Git。

共享弹体存档计数边界已迁SavedProjectileCounters，IntermediateProjectile保持原读取默认值及逐字段立即赋值顺序；MagicAge非负无新上限，ExpansionBroken仍clamp0..64，PaidMana仍由StoneCannonRules处理且默认10。未改Codec/保存键/注册/资源。新增4项模块合同及10000组冻结计数差分；完整离线build通过63单测/442模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-saved-counters-build.log及JUnit汇总/XML。实际世界/Codec重载/Fabric/Mixin/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS。下一审计并迁扫掠区块可用性检查，保持枚举顺序、首次未加载退出、狱炎和风枪discard而其他暂停、age仅通过预检后递增；先扩任务卡再实现。不游戏、不Git。

扫掠区块可用性检查已迁SweptChunkGate，IntermediateProjectile使用窄Loaded回调读取原hasChunkAt，保持x外z内、floor转int再右移、首次未加载退出。失败后才判断狱炎/风枪销毁，其他暂停，成功后才++age；不加载新区块，不改法术/资源/存档。新增4项核心合同及1000组冻结枚举差分；完整离线build通过63单测/446模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-chunk-gate-build.log及JUnit汇总/XML。测试只验证核心枚举与短路，真实Fabric/区块卸载/世界/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计固体碰撞候选收集与首墙/impact选点协调，保留水穿透跳过、接触<=步长才记contacts但contact<wall仍选墙的原分支、同距首个shape和永久地形先于流体；先定义端口及边界合同再迁移。不游戏、不Git。

固体接触合并与首墙选择已迁SolidContacts，保持contact<=步长才合并HashMap、contact<wall才更新墙和impact、同距取首个形状、含水过滤和世界枚举不变，地形仍先于流体。新增6项核心合同及1000组冻结差分；完整离线build通过63单测/452模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-solid-contacts-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计流体候选扫描及固体终止交接，保持读取时点、未加载跳过及流体同距优先，先定义端口再迁移。不游戏、不Git。

流体扫描已迁FluidContactScan并由MagicFlight窄World适配。保持loaded→fluid→empty→ignoresWater→water短路、同一FluidState高度读取、逐项Math.min且零距离不提前停止；近零运动和方块枚举仍原世界层。新增5项模块合同；完整离线build通过63单测/457模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-fluid-scan-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一核对已迁核心协调与载体剩余职责，建立完整服务端单步协调及窄世界适配合同，保持年龄、地形、流体、生物、终止顺序；不游戏、不Git。

ProjectileServerStep已协调寿命和几何准备后的完整服务端扫掠：区块检查→年龄递增→固体和永久地形→流体→生物→边界终止→移动。世界匿名适配仍负责原API和单术差异，不分派到通用解释器；未加载不推进年龄，目标终止或边界终止均跳过后续移动，异常原样传播且不回滚。新增6项核心合同；完整离线build通过63单测/463模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-server-step-build.log及JUnit汇总/XML。首次提取多余括号导致编译失败，已修正并重跑全构建，保留初次日志。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS：寿命、几何选择、世界匿名适配和保存载体仍未全迁。下一明确世界Adapter归属与载体窄状态端口，先审计避免扩大实体公共可变接口；不游戏、不Git。

IntermediateStepAdapter已承接服务端世界单步操作，原命中Set直接传入，年龄和破坏计数及距离通过窄回调注入，实体字段保持私有，无新增公共setter。原区块/年龄/地形/流体/目标/终止顺序、原注册和保存键不变。完整离线build通过63单测/463模块测试、15658旧断言、102有效定义/教材/资源/JAR。证据r06-step-adapter-build.log及JUnit汇总/XML。未新增测试，以原核心合同和编译/基线回归验证适配迁移，不声称世界集成。用户决定全部重构完成后统一游戏实测；世界/Fabric/Mixin/Codec/视觉/联机/性能仍NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，寿命和几何选择/保存载体尚待收尾，下一审计这些剩余职责并形成明确退出清单，不游戏、不Git。

IntermediateStepAdapter.prepareAndApply已承接原寿命检查与扫掠几何准备，载体tick保留super/removed、客户端轨迹，服务端只注入原命中Set和私有计数回调。保留原寿命表达式短路、体积尺寸、速度、区块/年龄/地形/流体/目标/终止顺序，未改变注册和保存格式。完整离线build通过63单测/463模块测试、15658旧断言、102定义/教材/资源/JAR；未新增测试，本批为世界适配迁移并运行原合同回归。证据r06-step-preparation-build.log及JUnit汇总/XML。用户明确全部重构完再统一实机测试；世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一核对载体保存及适配器单术分支归属，补结构退出清单后推进IntermediateSpells命中适配，不能称R06全部完成。不游戏、不Git。

## 中级普通命中协调（2026-11-13）

IntermediateHit 纯协调与 IntermediateHitAdapter 已接 IntermediateSpells.hurt；风枪龙卷仍使用独立 TornadoImpactHitAdapter。保持伤害成功后才判断击退并读取 motion、附效时才读取 owner，按 hurt→条件 knockback→effect→afterHit 原顺序执行。失败立即停止，异常原样传播、不回滚、不补调用。8项模块合同验证成功/拒绝、零击退、伤害后状态读取与三阶段异常截断。

完整离线 build 退出0：63单测/471模块测试，旧15,658断言、102有效定义、39/102教材、旧玩家存档fixture、资源与JAR/架构边界通过。证据 r06-intermediate-hit-build.log 及对应JUnit汇总/XML。端口测试不等于真实Fabric伤害/事件集成；游戏/Codec重载/视觉/联机/性能 NOT_RUN，全部重构完成后统一游戏测试，不启动游戏、不Git。

共享弹体运行时 STRUCTURE_SLICE_DONE，退出归属见 evidence/r06-intermediate-runtime-structure-exit.md；整体R06未完成。当前唯一任务 R06-intermediate-effects IN_PROGRESS。下一精确动作：审计 IntermediateSpells.execute 的剩余 TARGET_AREA fallback 与 blockImpact 其他输入fallback的真实调用及兼容合同，再拆范围命中/可见性世界适配；禁止仅凭注册路径无调用删除兼容行为。

## 中级范围结算和世界效果结构切片（2026-11-13）

DeferredAreaImpact纯核心负责按查询顺序筛选、wet快照、伤害及效果/事件，全部命中完成后才依次encase，最后burst。IntermediateAreaAdapter承接原12格加载检查/选点/半径6/效果及地形；保持兼容其他输入和water分支，不改数值。9项模块合同覆盖多目标先命中后造冰、筛选/伤害失败、湿身快照、效果决定入队、空目标和事件/效果/地形异常截断。

SpellVisibilityAdapter承接原加载采样和COLLIDER/NONE射线；9个已迁Adapter直接调用它，未迁世界类保留IntermediateSpells.visible委托。IntermediateImpactAdapter保留水炮/大火球/其他输入碰块分流及原读取时点，视觉burst后才读取owner播放音效。未扩大对其地形/视觉的测试声称。

完整离线build退出0：63单测/480模块测试、15,658旧行为断言、102定义和39/102教材、旧存档fixture、架构/资源/JAR检查通过，证据r06-intermediate-area-build.log及JUnit汇总/XML。游戏/Fabric/Codec重载/视觉/联机/性能NOT_RUN；按用户要求全部重构完成后统一实测。不启动游戏、不Git。

R06-intermediate-effects结构切片STRUCTURE_SLICE_DONE，不等于R06整体退出。IntermediateSpells保留弹体构造/执行分流及hurt分流；发布装配统一归R06-release-routing，实体核心/注册ID/资源未变，icicle_field不因此记为整术迁移完成。下一唯一任务R06-phase-two-runtime：先审计PhaseTwoEntity服务端tick、扫掠/停留/保存及已迁单术端口，建立职责清单后拆分；R07领域/障壁的未迁业务仍随单术处理。

## PhaseTwo服务端预检和分派协调（2026-11-13）

PhaseTwoServerTick纯核心已接PhaseTwoTickAdapter与原实体tick，保留super/updateBounds先行、低于世界discard、当前区块检查、owner读取、stationary无owner暂停、移动ahead检查、寿命检查、age递增及shield→stationary→fall→fly顺序。stationary重复读取保持原次序；不新增isRemoved短路。适配器注入age/remaining/anchor快照及窄回调，不增公共setter。落石内部第二次loaded失败仍发生在age递增之后；客户端/存档/注册/资源不改。

新增12项模块合同，完整离线build退出0：63单测/492模块测试、15,658旧行为断言、102有效定义与39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-tick-build.log和JUnit汇总/XML。核心端口合同不是Fabric/真实世界/Codec重载/事件/视觉/联机/性能验收，以上NOT_RUN。用户要求全部重构完成后统一游戏测试，不启动游戏、不Git。

R06-phase-two-runtime仍IN_PROGRESS：stationaryTick、fall、fly的内部世界业务和共享候选/终止协调、保存归一化尚待逐项迁移。下一精确动作审计fly候选过滤/排序及接触循环，用已有ContactCandidates等核心时保持PiercingFlightRules与其他分支求值次序，尤其爆炸contact后的动态piercing重读与风裂独立AOE历史。先扩任务卡再实施，不因本分派切片关闭整个R06。

## PhaseTwo飞行/驻留/落石结构切片（2026-11-13）

PhaseTwoFlightAdapter/StationaryAdapter/FallAdapter已承接原世界操作；载体只委托并注入原ImpactHistory、私有计数回调与方向/age快照。候选复用ContactCandidates筛完后实时contact稳定排序，保留历史求值次序；PhaseTwoContactTraversal先claim，爆炸定位后读取piercing→explode→重读piercing，不额外burst/discard；普通命中effect→burst→piercing→条件discard。风裂AOE继续使用同一ImpactHistory。PhaseTwoTermination保留流体同距优先、爆炸墙前0.02定位后explode、普通墙burst后discard。存档age/remaining复用原等价非负规则，默认值及逐字段赋值、历史最后替换保持。

18项新增模块合同（接触10、终止8），完整离线build通过：63单测/510模块测试、15,658旧断言、102有效定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-runtime-build.log及JUnit汇总/XML。结构切片不代表真实世界/Codec重载/视觉/联机/性能验收，以上NOT_RUN。用户授权持续执行全部重构，全部完成后统一游戏测试；不启动游戏、不Git。

R06-phase-two-runtime结构切片STRUCTURE_SLICE_DONE：实体同步/Codec/客户端与体积身份仍是载体，新增Adapter的世界API/单术分支继续依赖各自已有核心规则。几何身份配置与零引用兼容最终归R10，不能称整个R06完成。下一R06-phase-two-effects：迁damage/areaDamage的权限→伤害→附效→hook→洪流击退协调及共享加载/射线适配；准备/发布装配仍后续专卡。未把R07/R08未迁法术计为已完成。

## PhaseTwo共享伤害与目标查询结构切片（2026-11-13）

PhaseTwoHit和PhaseTwoDamageAdapter承接权限短路→伤害→附效→hook→洪流条件击退，读取spell/owner/level的时点保持。6项模块合同验证顺序、拒绝及异常；areaDamage仍逐目标可见后伤害，末尾一次粒子。PhaseTwoTargetingAdapter承接aim/aimedEntity/trace/traceFrom，loaded复用SweptChunkGate.available（传入回调的是块坐标）。trace的同距后者胜与aimedEntity的同距首者胜保持独立。首轮编译接口名误写loaded已修复，失败日志留在工作目录。构造/预检/释放、plan/construct/permanentStone/launch兼容世界入口归后续release-routing及R10审计，未标整阶段完成。

完整离线build退出0：63单测/516模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-effects-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R06-channel-runtime，拆服务端通道容器/准入/初次提交与tick适配，保留水源首次成功和火焰先登记再发射的顺序。

## 持续通道运行时归属与首次提交（2026-11-13）

ChannelRuntimeAdapter承接原服务器附件容器、资格预检、heartbeat、begin/stop/tick及兼容generateWater，channel_debt/channel_runtime ID与持久属性不变。ChannelAdmission纯核心保证begin成功→register→emit→sound，6项合同覆盖拒绝/异常及提交后副作用保留。PhaseTwoRuntime保持columns/burns/shield/prune在通道前运行，停服移除其他附件顺序不变。WaterfallChannel/FlamethrowerChannel已有费用/心跳/停止合同继续通过。只是共享通道结构切片，真实附件重载/多服务器集成未验收。

完整离线build退出0：63单测/522模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-channel-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R06-release-routing：清理PhaseTwoSpells剩余构造/计划/释放世界业务，审计已迁R06全部装配和兼容路径，再处理准备资格与通知协调。

## 准备资格和释放适配结构收尾（2026-11-13）

PreparationEligibility纯核心保留counter/silence/玩家状态/GUI/冷却/选择/知识/装槽/MP/事件的原短路顺序，4项合同含13个拒绝位置验证。PreparationRuntimeAdapter持服务器准备附件及原特殊仪式协调，MagicPreparation仅兼容委托；未改变剩余R07/R08特殊分支。PhaseTwoConstructionAdapter/ReleaseAdapter和IntermediateReleaseAdapter承接原构造/兼容释放世界业务，PhaseTwoSpells/IntermediateSpells现仅签名委托/选择。六个无状态查询/仪式服务开放跨包调用，不增加实体公共setter；首次访问编译失败已修复，原失败日志保留。R06的33术装配源码逐项核对见r06-release-bindings.json：这是接线证据，非实际世界验收。已完成R06共享核心/协调结构切片；R10仍须清理兼容批次命名和实例几何身份，所有世界边界NOT_RUN。

完整离线build退出0：63单测/526模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-preparation-routing-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-water_shield：按账本顺序建立逐术卡，审计水幕体积/续期/容量/接触湿身/弹体拦截及全局实例归属后迁独立核心和适配。

## 水盾结构切片及领域注册表服务器归属（2026-11-13）

WaterShieldRules/Contact/Absorb/Selection与WaterShieldAdapter/ContactAdapter/CollisionAdapter已接实际准备付款、实体接触及服务器弹体拦截；保持6MP/100准备/0恢复/80持续、锚点strict<1E-6、容量8、每接触drag后mark/首次claim后湿身、每20tick原Map清理。最近strict<同距首项，流体/地形不改；共享ShieldInterception已由物理盾与水盾双调用，先地形→incoming/absorb→三叉戟类型→反弹/销毁。原资源/ID/数值不变。独立R07-area-runtime把全局ACTIVE改成AreaRegistry服务器附件，仅initializer，五个清理调用传入player/server；6项核心隔离/异常合同不是Fabric附件集成。新添29项合同（水盾命令6、规则/接触/吸收8、选择4、共享拦截5、注册表6），本切片结构离线PASS；实际世界/同服多人/生命周期仍未验收。

完整离线build退出0：63单测/555模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-water-shield-final-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-heat_hand：审计交互权限、工具/方块处理、持续资格与付款/准备，提纯规则及独立世界Adapter；其后按账本heat_island/burning_place推进。

## 灼热手核心规则与世界适配（2026-11-13）

HeatHandRules保存命中面2×2 a外b内轴映射及冰/蒸发转化，HeatHandMelt保存所有格非短路setBlock后读取warmth、changed短路frozen再温暖及末尾逐格粒子顺序；HeatHandAdapter保存4格OUTLINE及权限，兼容入口委托。SelfEffectComposition接独立2MP/100准备/0恢复命令。新增14项模块合同（8规则/协调、6付款/取消/部分异常），原200温暖/方块资源数值不改，无Recipe/工具分支凭空新增。世界方块权限/环境属性仍未实机验收。

完整离线build退出0：63单测/569模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-heat-hand-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-heat_island：拆半径3区域温暖、清湿身及固定锚点续期，保持200持续和现有效statusTicks；随后燃烧领域。

## 地热区域规则与独立释放（2026-11-13）

HeatIslandRules保持目标getX/Z到领域中心的半径3含边界判断，10000随机冻结算式差分通过；HeatIslandAdapter先removeWet后MagicEffects.warm，非攻击不新增PVP过滤。6MP/100准备/0恢复和独立SelfEffect命令已接，200领域及原statusTicks保持。新增10项模块合同（规则/顺序4，准备/付款6）。共享Area实体owner/剩余/加载和原地续期职责仍后续公共生命周期任务，不把领域世界行为称已实测。

完整离线build退出0：63单测/579模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-heat-island-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-burning_place：抽每目标接触/权限/可见/claim/伤害/点燃事件顺序，保留原全身接触体和20tick尝试冷却。

## 燃烧领域贴身接触协调（2026-11-13）

BurningPlaceContact/Adapter保留history→全身接触→权限→原clip可见→claim→hurt→effects→hook，伤害拒绝仍消耗本目标本次20tick尝试，异常不回滚claim。贴身owner包围盒inflate(.12)、自身排除与PVP、原无chunk采样的可见射线不变。10MP/100准备/0恢复独立命令已接，80持续/2伤害/原随机点燃不变；删除原实体零调用的局部几何/可见帮助函数。新增14项模块合同（8命中协调、6付款），共享Area生命周期仍单独收尾。

完整离线build退出0：63单测/593模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-burning-place-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-magic_shield：抽魔法直伤允许事件/容量吸收及独立准备付款，随后icicle_field；共享Area生命周期另卡处理。

## 魔法盾直伤边界与共享容量吸收（2026-11-13）

魔法盾独立SelfEffect/准备付款，原伤害事件位置委托懒查询适配。仅已知魔法Projectile直接伤害进入盾检查；有同level有效盾则吸收并拒绝伤害。三盾共享容量扣除→显示→重新读取容量→移除协调，保留异常副作用和8点容量，不共用几何资格。8MP/100准备/0恢复/100持续不变。

完整离线build退出0：63单测/603模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-magic-shield-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R07-icicle_field独立领域选点、湿身快照和延迟冰封；随后共享领域生命周期。

## 冰霜领域独立选点与延迟冰封（2026-11-13）

IcicleFieldRules/Adapter接入TargetedArea Command及Preparing State；保持14MP/5伤害/12选点/6半径和旧paidMana，未生成飞行实体。复用DeferredAreaImpact，湿身先快照，hurt成功才附效及移湿并hook，全部命中完成再冰封，最后burst。新增6项边界/异常合同及10000几何差分。首次测试错误使用禁止NaN的Vec3d已改成标量比较合同，失败日志保留。

完整离线build退出0：63单测/609模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-icicle-field-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-area-lifecycle拆短期领域服务端时序，随后blizzard_storm。

## 短期领域生命周期与世界接触分离（2026-11-13）

AreaTick抽纯短路生命周期，AreaTickAdapter拥有查询和接触副作用，实体保留私有计数/anchor/接触容器并提供窄State回调。无owner/removed/dead/跨维不减寿命，寿命先减再查chunk；到期不查chunk。位移阈值及NaN比较、follow→pulse→prune→contacts均原序。8项合同覆盖短路/边界/异常；扩展领域仍各自tick和存档，不误套短期规则。

完整离线build退出0：63单测/617模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-area-lifecycle-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-blizzard_storm独立节拍、跟随/adopt和旧保存到期规则，随后squall。

## 吹雪独立节拍与旧到期保存合同（2026-11-13）

BlizzardStormTick/Rules与世界Adapter接入独立SelfEffect和准备；释放首帧立即tick、跟随后adopt，拒绝和异常不增pulse，80tick内8次伤害脉冲。保留water cadence claim→hurt→附效→hook及原可见射线，36MP/2伤害不变。旧ExpansionSpell/Pulse/Expiry键和默认80/0、绝对时间恢复不变。8项模块合同覆盖短路、节拍、异常和整数/long边界。

完整离线build退出0：63单测/625模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-blizzard-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-squall局部降雨生命周期/暴露/时长规则，随后cumulonimbus。

## 局部降雨独立生命周期与暴露规则（2026-11-13）

SquallRules/Tick与独立Adapter接真实SelfEffect/Preparing，LocalMagicRain为兼容API。900/450时长、48格逐段碰撞、16半径、10tick节拍、清火→湿身300不变。RainExposure抽懒半径→chunk→天空→清火→湿身协调。RainExpiry旧键与shorten保留，7项模块合同覆盖短路、时长、暴露、异常。弱实体索引仍为适配器私有，不声称完成真实世界生命周期测试。

完整离线build退出0：63单测/632模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-squall-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-cumulonimbus纯排程/受害冷却/生命周期与世界查询分离，随后lightning/electric。

## 积雨云独立排程与受害冷却状态（2026-11-13）

CumulonimbusTick/Rules/State与世界Adapter、SelfEffect准备接入。保留旧短路和未检查removed的原边界，40tick落雷间隔先写next再清victims，选点失败仍消耗本次落雷；80tick受害尝试先claim再cadence/hurt。1200时长/24半径/48高/16随机候选、天空/伤害/视觉与旧保存键默认不变。9项模块合同覆盖排程、短路、状态实例隔离和异常。

完整离线build退出0：63单测/641模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-storm-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-lightning保持remaining15锁点和重复validPoint，抽命中控制与独立付款，随后electric。

## 雷光锁定落点与命中控制分离（2026-11-13）

LightningRules/Impact与独立Adapter/Command装配，保留remaining15锁点、每3tick警示与释放前及命中前二次validPoint；不重新按最终准星定位。220MP/32伤害/64距离/2水平半径32高与自身误伤特例不变。天空→非自身权限→可见→hurt→control→hook原序；纯规则7项合同覆盖短路和边界。实际Fabric伤害及锁点预警视觉未运行。

完整离线build退出0：63单测/648模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-lightning-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-electric抽单目标选择/免疫分支/命中协调，随后earth_wall与earth_fortress。

## 电击独立目标选择与免疫命中分支（2026-11-13）

ElectricRules/Impact与ElectricAdapter接入SelfEffect/Preparing，ThunderMagic仅双术兼容API。32格原逐段碰撞/卸载停止、严格最近<保留首个同距。无目标false可退款；有目标hurt拒绝仍burst，成功immune则缓慢60/1否则玩家40生物60控制，再hook。260MP/10伤害及世界查询原序不变。6项模块合同覆盖命中、拒绝、免疫、异常和距离比较。

完整离线build退出0：63单测/654模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-electric-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_wall有序布局纯化与原租约入口，随后earth_fortress。

## 土墙独立有序布局与租约适配（2026-11-13）

EarthWallLayout纯5×3有序布局接独立Adapter/SelfEffect；保留24格逐段选点、面法向.02偏移、主轴>=同距决策与y外x内顺序。原1200/true租约预检/排队和首格burst不变。4项模块合同包含10000随机方向逐格有序差分，世界权限/租约实际执行尚未游戏验收。

完整离线build退出0：63单测/658模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-earth-wall-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_fortress纯半球壳/门洞/稳定排序与独立适配，随后earth_hedgehog。

## 土堡纯半球壳与门洞有序布局（2026-11-13）

EarthFortressLayout接独立Adapter/SelfEffect，保留y/x/z遍历、9..16平方半径、两格高方向门洞、稳定按层/平方距排序，最多200计划与原2400/false租约。4项模块合同含1000随机方向及9轴向完整有序差分、门洞/空心/不可变性。实际世界租约权限和恢复仍NOT_RUN。

完整离线build退出0：63单测/662模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-earth-fortress-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_hedgehog纯布局/processed状态/命中计数与世界tick分离，随后quagmire。

## 土刺独立时序与命中尝试状态（2026-11-13）

EarthHedgehogRules/State/Tick与世界Adapter/SelfEffect接入，12刺黄金角偏移原精度，400寿命/2tick递进、严格age==i*2不补漏帧。processed先写再世界处理、命中计数先写再hurt，受伤拒绝仍占两次预算。旧Spikes保存键和附件不变，原基地搜索及三层非短路碰撞保留。8项合同覆盖时序、状态隔离、计数与异常。

完整离线build退出0：63单测/670模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-hedgehog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-quagmire表面体积/跳跃cap/免疫附效和领域生命周期，随后magma_gush。

## 泥沼体积、运动限制与免疫接触分离（2026-11-13）

QuagmireRules/Contact/Tick与Adapter/SelfEffect接入；保留半径6与y[-.2,.65]、向上.115限制不下拉、400到期、owner允许受影响/他人权限与可见、首领仅弱慢和普通先慢后束缚。表面.025偏移/MudExpiry与原运动Mixin API保留。8项模块合同覆盖几何、运动、免疫、短路和异常。

完整离线build退出0：63单测/678模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-quagmire-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-magma_gush独立首帧/余焰排程、processed与自伤火焰分支，随后violent_storm。

## 熔岩喷涌首帧与余焰排程分离（2026-11-13）

MagmaGushRules/State/Tick/Hit与Adapter/SelfEffect接入。首帧10伤、20..160八次2伤、>160到期，processed在damage判断前写。保留火cadence先claim再hurt，owner随机采样/抗火检查/300点燃和其他目标附效分支、scorch→tick→burst、旧保存默认及附件。9项合同覆盖排程、几何、命中原序及异常。

完整离线build退出0：63单测/687模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-magma-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：先R07-wind-runtime共享依赖，再按violent_storm/tornado/vacuum_storm/sky_tempest逐术策略和合同，flame_tornado按账本位置。

## 风领域共享生命周期与冲量规则（2026-11-13）

WindFieldTick/State/Rules抽共享生命周期、20tick排程、圆柱和归一化冲量，World Adapter保留原世界查询和领域相消。移动失败只discard不立即删弱状态；pulse更新next在重叠抑制前；接触完再相消。旧WindStart/Expiry/Next/Direction和原注册ID不变。9项模块合同通过，未将五术标完成，逐术Strategy和命中继续。

完整离线build退出0：63单测/696模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-wind-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-violent_storm独立策略/命中协调，然后tornado/vacuum_storm/sky_tempest，其他术按账本。

## 暴风独立策略与领域接触协调（2026-11-13）

ViolentStormPolicy作为真实Strategy持有16×16、owner排除、pulse外推.2及相消资格，独立SelfEffect绑定。WindFieldContact抽excluded→geometry→owner/permission→visible→pulse→immune→move原序，免疫每tick读时点不改。6项模块合同验证自身排除、免疫不挡伤害、异常阻止运动及策略；其余风术仍LegacyPolicy临时路径待逐卡替换，不声明全部完成。

完整离线build退出0：63单测/702模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-violent-storm-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-tornado独立移动/拉拽策略和脉冲协调，随后vacuum_storm/sky_tempest。

## 龙卷独立移动拉拽策略与脉冲命中（2026-11-13）

TornadoPolicy独立5×20移动/每pulse拉拽策略及SelfEffect接入，原首帧不移动、后续.1移动/loaded检查和同owner火龙卷重叠抑制留在世界适配。WindFieldPulse抽claim→hurt→附效→hook→真空慢原序，6项模块合同覆盖拉拽方向、玩家上升cap和伤害拒绝/异常。原数值/资源/保存键不变。

完整离线build退出0：63单测/708模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-tornado-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-vacuum_storm独立策略与拒绝伤害仍慢合同，随后sky_tempest。

## 真空暴风独立策略与拒伤减速合同（2026-11-13）

VacuumStormPolicy独立12×12固定圆柱，无移动/相消，SelfEffect/Preparing绑定。WindFieldPulse保持cadence或hurt拒绝仍加500tick慢，成功hook后慢，异常不继续附效。5项合同验证拒伤、拒cadence、hook异常与策略；260MP/6伤害/300持续及旧Wind保存键不变。

完整离线build退出0：63单测/713模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-vacuum-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-sky_tempest独立中心伤害和每tick切向冲量，随后flash_over。

## 天空风暴中心伤害与切向冲量策略（2026-11-13）

SkyTempestPolicy独立24×40、中心半径8伤7/外4、每tick切向.08上升.04、玩家y<=.35与自身可伤规则。独立SelfEffect，pulse仅相消雨/积雨云，暴风烟/火特例不扩大。4项合同覆盖中心包端点/NaN、每tick移动与360方向切向及玩家限速。620MP/600持续和旧Wind保存键不变。

完整离线build退出0：63单测/717模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-sky-tempest-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-flash_over首帧双伤与余焰独立排程，随后flame_tornado/inferno/absolute_zero。

## 闪燃独立首帧双伤与余焰状态（2026-11-13）

FlashOverRules/State/Contact与Adapter/SelfEffect接入，首帧16爆发和3余焰均执行，余焰八次，flash不claim、pulse先claim火20。flashed和next在世界命中前写，原owner随机/抗火/400点燃和保存键默认不变。共享ExpiringFieldTick仅state/expiry优先的相同生命周期族；8合同覆盖短路、首帧双伤、默认旧状态与异常。

完整离线build退出0：63单测/725模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-flash-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-flame_tornado独立火焰/上升策略并移除临时LegacyPolicy，随后inferno。

## 火龙卷独立火焰与上升策略（2026-11-13）

FlameTornadoPolicy独立5×18移动/火脉冲策略及SelfEffect绑定，上升最终仍用原速度+.06，玩家.35上限。成功hurt后火附效再hook，自身原随机/抗火/500点燃不变。五风术策略全部显式绑定并删除临时LegacyPolicy，公共几何帮助对非风术保留旧默认16。5项模块合同通过，原300MP/7伤害/240持续不变。

完整离线build退出0：63单测/730模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-flame-tornado-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-inferno独立规则、401到期与20次余焰，复用实际相同爆发排程后absolute_zero。

## 炼狱独立规则与真实共享爆发排程（2026-11-13）

InfernoRules/Adapter/SelfEffect接入，首帧18，20..400二十次5伤、401结束，半径20高8和scorch20/2048/401保留。提炼实际相同BurstFieldSchedule/Contact共享时序，闪燃保留兼容核心API并重跑合同。旧Inferno保存键默认及owner随机/抗火/600点燃不变；5新增合同验证独立规则、时序和跳帧不补发。

完整离线build退出0：63单测/735模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-inferno-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-absolute_zero湿身增伤/暖身快照/首次冻结控制与后续冷雾，随后holy逐术。

## 绝对零度首次冻结与冷雾顺序分离（2026-11-13）

AbsoluteZeroRules/Contact与Adapter/SelfEffect接入，首次28+湿6，warm先wet快照；hurt成功才移湿，warm只移暖，否则canFreeze后冻结并控制。后续冷雾无伤，仅无warm/可冻才随机.8；初次拒伤不落入冷雾。160/半径16及freezeSurface16/4096/1200和旧Zero保存默认不变。7合同覆盖资格、快照、控制和异常。

完整离线build退出0：63单测/742模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-zero-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-holy-runtime共享几何/世界适配，随后exorcist_ray/exorcist_burst/exorcist_judge/holy_ray逐术。

## 圣系共享几何与资格适配（2026-11-13）

HolyGeometry与HolyEligibility抽纯柱体/懒资格，HolyInstantAdapter持原plan/instant/visual，HolyMagic兼容API保留TARGETS/DAMAGE身份。SacredNova/DivineJudge仅公开原无状态服务以便跨包调用，无字段setter。3项合同含10000方向投影差分、边界及玩家/死亡/owner/tag读取短路；四即时术尚待逐术策略，不标整体迁移完成。

完整离线build退出0：63单测/745模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-exorcist_ray独立单体策略，随后exorcist_burst/exorcist_judge/holy_ray。

## 驱魔射线独立最近单体策略（2026-11-13）

ExorcistRayPolicy独立单体/窄光束策略和SelfEffect准备绑定，严格最近<同距首个，资格/墙/卸载原序、hurt失败仍成功与原visual，无新增hook。2项合同覆盖最近比较及窄光束参数，四术策略剩余临时分支继续逐卡清理。

完整离线build退出0：63单测/747模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-ray-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-exorcist_burst终点球形/可见伤害协调，随后exorcist_judge/holy_ray。

## 驱魔爆破独立终点球形命中（2026-11-13）

ExorcistBurstPolicy与HolyAreaHit接入终点4半径球、标签资格、形状、可见、hurt顺序；不画beam且hurt拒绝仍成功。3项模块合同覆盖懒读取和异常。

完整离线build退出0：63单测/750模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-burst-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R07-exorcist_judge与holy_ray逐术策略。

## 驱魔审判独立单体策略与接口隔离（2026-11-13）

单体审判策略已绑定；HolyVolumePolicy仅范围术实现形状，HolyNearest严格比较保留同距首个。两项合同及完整构建通过。

完整离线build退出0：63单测/752模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-judge-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成holy_ray独立策略后继续sacred_nova。

## 圣光射线独立柱形策略（2026-11-13）

HolyRayPolicy、HolyRayAdapter接入2半径柱形命中和宽光束视觉；移除HolyInstantComposition最后LegacyPolicy，未支持法术显式拒绝。

完整离线build退出0：63单测/754模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-ray-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成圣域及神之净化共用生命周期适配，继续divine_judge。

## 圣域与神之净化纯规则及适配迁移（2026-11-13）

HolySanctuaryAdapter保留WeakHashMap、NovaExpiry和初始伤害/击退顺序；独立16/48范围规则、自然生成抑制短路、环形节拍已提纯，SacredNova/GodPurge均有SelfEffect绑定。首次编译发现旧包内HolyMagic不可见，改依赖同包HolyInstantAdapter后完整构建通过。

完整离线build退出0：63单测/760模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-fields-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成divine_judge脉冲模块，审计god_purge后继续剩余R07。

## 神之审判三脉冲状态与协调（2026-11-13）

DivineJudgeTick按原次序完成状态/时间/归属检查，提交count后命中；独立规则保留60tick三段、柱体和加载clamp。6项合同覆盖跳帧不补发、异常不重放及短路。

完整离线build退出0：63单测/766模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-divine-judge-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：迁移火土领域五种Strategy，再抽取实际命中与生命周期模块。

## 五种火土领域规则与实际生命周期命中协调（2026-11-13）

五术独立GroundFieldPolicy与SelfEffect已绑定；GroundFieldTick/GroundFieldContact提纯生命周期和命中顺序，原世界/地形/视觉保留于GroundFieldRuntimeAdapter，旧ExpansionFields仅兼容门面。17项领域合同及10,000组火墙差分通过；各术副作用Strategy和地形仍待后续收尾，不能标整项完成。

完整离线build退出0：63单测/783模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-ground-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：烟雾视距状态和五术模块拆分，随后审计火土各术副作用Strategy与地形。

## 烟雾五术生命周期与视距协调（2026-11-13）

FogSight纯化强弱叠加和目标获取；SmokeFieldTick/SmokeContact接入实际五术生命周期，飞行撞击帧不再进入雾分支（原行为保留）。五术各自Strategy和SelfEffect适配，旧SmokeFields保留存档与Mixin入口；12项合同通过。各术效果策略/服务器归属仍待后续结构收尾。

完整离线build退出0：63单测/795模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-smoke-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成ocean_freeze独立扩散/扫描/命中模块后处理模式与障壁。

## 海洋冻结纯状态与命中模块（2026-11-13）

独立OceanFreezeState/Rules/Hit与Fabric适配已接入；半径/扫描/配额/加载clamp、先claim后hurt、UUID容错均保留。6项模块合同通过，真实水面/租约恢复仍未实测。

完整离线build退出0：63单测/801模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-ocean-freeze-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：提纯模式选择及核爆额外费用，再迁移大气/重力/核爆独立运行模块。

## 三种模式术独立规则与持续费用协调（2026-11-13）

ModeSelection及三术规则、MaintainedFieldTick、NuclearExplosionTick、AtmosphereContact、GravityContact均接实际路径。保留核爆额外支付、先schedule后upkeep、费用失败只discard、核爆先标记后targets/terrain及原命中副作用。18项合同通过；world状态/模式attachment隔离与实际费用模块接线仍待后续审计。

完整离线build退出0：63单测/819模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-mode-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：迁移障壁几何与独立模块；随后R08辅助功能及R09/R10收尾。

## 有限障壁容量与移动弹体协调（2026-11-13）

双重盾/灵体封印/拘束各自规则与SelfEffect适配接入；BarrierCapacity、BarrierDamage/Movement/Projectile纯协调保持旧顺序。三叉戟先反弹后击破、保留容量独立与旧存档默认；13项合同通过。供能障壁另拆，真实Mixin和事件未启动验收。

完整离线build退出0：63单测/832模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-barrier-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：龙圣斗气独立后继续城塞/魔神封印供能模块，然后R08其他功能。

## 龙圣斗气独立与核心障壁供能协调（2026-11-13）

龙圣斗气独立adapter和tick规则接入；城塞FortressUpkeep保留真实takeEnergy及100tick断供宽限；魔神封印目标资格与PoweredBarrierTick已抽离。13项合同通过，核心能量和保存键不变。

完整离线build退出0：63单测/845模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-powered-barrier-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成治疗/解毒/同意与声音模块；已发现高阶阵面成功消费接线遗漏，立即修复并补合同后统一归档。

## 治疗解毒支持同意独立模块及阵面回归（2026-11-13）

治疗/初中高级解毒、SupportConsent、SupportRay独立；移除批次执行器内相应业务，保留最近目标后资格判定和非短路移除。补修成功SelfEffect释放后阵面消费，顺序为效果→阵面→通知，失败不消费；13项合同通过。

完整离线build退出0：63单测/858模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-healing-support-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆howling/hypnosis，再处理效用、召唤、通信、转移、幻影。

## 声音效用幻影独立模块及召唤规则（2026-11-13）

Howling/Hypnosis、Disturb/Divination各有独立实际适配与纯协调；幻影保留先付费后schedule的历史差异并接入独立SelfEffect。召唤slot/灯灵信用/尸体expiry/精灵防护规则已提纯；19项合同通过。

完整离线build退出0：63单测/877模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-sound-utility-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续召唤生命周期/归属/付费，随后通信和转移。R09/R10及先前切片结构收尾仍未完成。

## 召唤归属支付生命周期与四术释放端口（2026-11-13）

SummonTick接实际owner/level/alive/expiry/slot校验和schedule→pay→act；四术独立SelfEffect已接入，原UUID/attachment保留。首次跨包编译发现LightLeases窄操作不可见，开放update/release后通过完整构建，未暴露状态。5项合同通过，四术专属AI与世界共享适配仍待结构收尾。

完整离线build退出0：63单测/882模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-summon-lifecycle-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：通信草稿服务器隔离与发送协调，再迁移转移邀请/安全预检/原子移动。

## 通信转移与71项释放绑定真实接线复核（2026-11-13）

通信草稿与转移邀请已由服务器实例持有，纯资格/安全/发送/移动回滚合同接入。审计发现此前部分SelfEffect接线替换静默失败，已以71项显式绑定修复；冰结领域paidMana恢复历史10；高阶成功释放补回阵面消费。结构切片通过，不代表R07/R08共享世界分支或R09/R10全部完成。

完整离线build退出0：63单测/896模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-routing-reconciliation-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：将102术释放绑定覆盖纳入构建，移除局部效果装配条件链，隔离输入会话服务器归属；随后继续逐术世界职责与客户端拆分。

## 102术绑定构建门禁、服务器输入归属与客户端职责拆分（2026-11-13）

构建检查102术唯一释放绑定与完整覆盖，检查实际SelfEffect适配器文件存在；局部5术与选点4术装配改为显式Map。输入会话由overworld运行时附件持有，纯InputSessions保留先提交sequence再限流语义，3项隔离/生命周期合同通过。HUD、知识、轮盘、共享样式、渲染注册、网络传输拆为独立职责，MagicScreens无引用后删除；轮盘几何提核，2项合同覆盖1—10槽中心及边界。实体注册唯一归属MagicEntityTypes，MagicSystem保留18个公开兼容别名及原注册顺序/参数。首次客户端安装因教材样式引用未迁而中止，补全后构建通过；Loom一次缓存锁重建失败，离线重试成功。R09和R10仅部分职责完成，SpellExecutors条件链、共享世界适配、客户端视觉和旧批次类仍需继续。

完整离线build退出0：63单测/901模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-routing-client-ownership-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续102术资格/已付费释放模块注册，替代SpellExecutors条件链；随后逐术世界策略、运行状态归属、视觉采样和旧批次职责清理。

## 执行注册、领域世界归属、纯视觉采样与火土地形策略（2026-11-13）

SpellExecutors改为兼容门面，102项显式资格/已付费释放绑定在启动核查覆盖与重复，保留付费调用边界；源码表达式审计见r10-execution-binding-source-audit.json。18处领域索引改为level附件，障壁/圣域查询显式使用目标world；学习与组合会话由server持有，粒子预算由level持有。附件延迟注册，避免纯函数测试初始化Fabric Mixin；首次verifyMagic发现此问题已修复并全测。无业务依赖的ExampleMixin及配置项删除，其他Mixin保留。70项既有氛围配置移入纯核显式Map，粒子渐隐/成长/漂移/帧和4tick字母采样已接；新增4项合同含40004组角色采样。火土5术的命中/次数/节拍/伤害策略和燃烧/震动/塌陷/落石副作用分离，3项合同；地形开挖/落石填土独立适配，原权限/租约/配额不变。仍有烟雾/模式/障壁/召唤共享分支、旧批次载体/视觉职责、定义规格及完整逐术账本收尾，未完成全部重构。

完整离线build退出0：63单测/908模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-ownership-visual-ground-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分烟雾五术、模式三术及召唤专属世界副作用；继续视觉profile/采样、旧批次职责与逐术账本审计，不启动游戏、不Git。

## 烟雾逐术规格、三模式世界更新与18种载体形状（2026-11-13）

烟雾5术视距/残留/颜色/减速/泥雨束缚/伤害/天空暴露/粒子数量/发射属性由逐术纯规格提供，原class中的术ID分支移除；砂云扫掠与转雾世界算法独立，保持接触当帧return。大气/重力/核爆三术世界更新独立Strategy，生命周期仍共享，模式选择/费用和形状后续继续拆。18种旧批次载体形状移入纯核EffectShapeCatalog，实体不再维护四串形状分支。增加5项模块合同；执行注册表在verifySpellBaseline中实际初始化，资格/已付费释放与视觉绑定覆盖检查纳入architectureTest。已运行完整离线build成功，仍不是实际世界/碰撞/视觉验收。

完整离线build退出0：63单测/913模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-smoke-modes-shapes-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分模式选择/供能费用与边界、召唤AI/建造职责；继续旧批次运行类与客户端视觉模块收尾及102术账本核对。

## 旧批次运行职责及空门面清理、召唤Template Method与模式归属（2026-11-13）

柱体生长队列、火柱锁定灼烧队列、接触节拍索引拆为三个独立运行适配，原PhaseTwoRuntime仅剩明确生命周期协调后改为PlayerSpellRuntime；原附件key与取消/停服入口保留。免疫tag归属MagicControl、Ray归属射线适配、通道类型归属通道模块，调用者直连既有窄适配器后删除PhaseTwoSpells/IntermediateSpells空门面。首次编译补齐建造适配的entity/damage静态依赖后全测通过。召唤用Template Method固定读取指令→指定目标→精灵自动目标→清理无效目标→行为顺序；独立地面导航/精灵飞行/光精灵行为，保留原导航和物理移动，怪物与死灵共享地面机制但绑定各自视觉。模式选择持久化与指令迁至SpellModeSelectionAdapter，核爆冷却归属NuclearExplosionAdapter，原key/copyOnDeath保持。仍有批次实体/渲染/规则/部分适配器及定义/供能/召唤生成等职责未清，整体IN_PROGRESS。

完整离线build退出0：63单测/913模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-runtime-facades-summons-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续形状/模式费用与边界策略、召唤生成及供能职责；移除残留批次规则分支，客户端视觉与完整102术账本最终审计。

## 批次规则与释放分支移除、模式领域规格收尾（2026-11-13）

IntermediateRules与PhaseTwoRules已删除，块爆体积和抛射求解迁入纯核，原断言改为调用真实职责所有者。PhaseTwoReleaseAdapter已移除，其资格与已付款释放直接进入102术显式绑定；实体创建独立EffectCarrierSpawner，冰柱/冰枪保留原trace和放置顺序。持续通道仅保留原有有效角度检查。大气/重力/核爆通过各自世界策略负责选点、就绪及释放通知；供能、跟随、尺寸和核爆球形上下界提为纯规格，生命周期不再判断术ID。新增6项模块合同，当前63/919；全部旧数值、资源、保存键及原版世界副作用未修改。整体重构仍IN_PROGRESS，尚有召唤、障壁、定义及客户端视觉等职责收尾。

完整离线build退出0：63单测/919模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-release-rules-maintained-policy-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分召唤生成/供能/尸体存储与精灵保护，清理IntermediateReleaseAdapter残留分派；随后客户端视觉、定义规格及102术最终账本审计。

## 批次规则与释放分支移除、模式领域规格收尾（2026-11-13）

IntermediateRules与PhaseTwoRules已删除，块爆体积和抛射求解迁入纯核，原断言改为调用真实职责所有者。PhaseTwoReleaseAdapter已移除，其资格与已付款释放直接进入102术显式绑定；实体创建独立EffectCarrierSpawner，冰柱/冰枪保留原trace和放置顺序。持续通道仅保留原有有效角度检查。大气/重力/核爆通过各自世界策略负责选点、就绪及释放通知；供能、跟随、尺寸和核爆球形上下界提为纯规格，生命周期不再判断术ID。新增6项模块合同，当前63/919；全部旧数值、资源、保存键及原版世界副作用未修改。整体重构仍IN_PROGRESS，尚有召唤、障壁、定义及客户端视觉等职责收尾。

完整离线build退出0：63单测/919模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-release-rules-maintained-policy-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分召唤生成/供能/尸体存储与精灵保护，清理IntermediateReleaseAdapter残留分派；随后客户端视觉、定义规格及102术最终账本审计。

## 召唤职责、纯材质动画策略与客户端输入策略（2026-11-13）

召唤尸体记录及选点、实体生成、三类供能策略与精灵保护独立，持久键和实际调用顺序保持。IntermediateReleaseAdapter及零调用IntermediateAreaAdapter删除，原有效逐术入口直连窄适配，生成仅由SweptProjectileSpawner负责。ExtendedSpellMotion已删除；原34术变形按七类材质Strategy抽入纯核MeshMotion及各MeshDeformation，35个ID含unknown×26角色×3阶段×10时间点，共27300组变形/透明度冻结差分精确通过。MagicClient只保留初始化与兼容委托，键位注册、物理输入/UI边界独立；CastingInputState纯状态负责初始松键、打断、两tick连发、模式锁，3项实际接线合同通过。完整离线build通过，整体仍IN_PROGRESS；未声称世界/视觉实际验收。

完整离线build退出0：63单测/923模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-summon-motion-input-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续实体类型身份映射和渲染尺寸/姿态纯规格；拆ReferenceSpellModels资源加载与纯采样，随后障壁、定义规格及逐术账本最终核对。

## 参考模型纯采样、渲染规格与注册身份归属（2026-11-13）

ReferenceSpellModels的透明度和变形已抽到纯核ReferenceMeshMotion，特殊术显式不可变采样策略保留原首个匹配，未擅改原flame_tornado不可达第二分支；20,010组变形/透明度冻结差分通过。渲染尺寸与载体姿态抽为ProjectileMeshSpec，薄片/箭/岩炮/未知默认与竖直落石/静止盾姿态合同通过。基础和扫掠实体的类型→法术映射统一归属MagicEntityTypes，未知类型默认保持，实际注册顺序/ID/尺寸不变。当前63单测/926模块测试，整个重构仍IN_PROGRESS。

完整离线build退出0：63单测/926模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r09-reference-render-identity-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：抽取法术定义隐式分类为纯规格，清理长位置参数及目录职责；随后继续有限障壁和供能障壁逐术策略、残留批次命名与102术账本。

## 纯法术定义、Builder与障壁/建造/静止载体职责（2026-11-13）

SpellDefinition、CastingProfile、MagicCategory的实际类型迁入magic-core，公共全名与record组件不变；SpellTraits独立纯分类规则，102术快照通过。90处长位置构造改为经典Builder具名构建，兼容构造器保留，rank及recovery实际值保持。有限障壁的三种目标/伤害/弹体/生物拦截和亡灵击壳分离为独立Strategy；城塞与魔神封印的生成/付核心能量/维持拆为策略。PhaseTwoConstructionAdapter删除，土枪建造/土柱永久石块/冰城预检/原版物理冲量分别归属专属适配。PhaseTwoStationaryAdapter删除，水瀑/冰枪/火柱静止世界行为显式装配；未知默认接触保持。迁移时两个缺失导入和三个receiver错误由编译发现并修正，最终完整build通过；没有游戏实测或Git操作，整体仍IN_PROGRESS。

完整离线build退出0：63单测/926模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-definition-barrier-stationary-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分扫掠弹体的逐术过期/体积/碰块策略与定向载体飞行专属策略，再移除已清空批次命名；继续视觉目录/定义目录和102术逐项完成证据。

## 逐术飞行策略、纯目录完整合同与机制类型清理（2026-11-13）

SweptProjectilePolicy明确绑定六种扫掠术的寿命/体积/接触/卸载/地形/爆发/碰块能力，CarrierFlightPolicy维护严格接触/截面/爆炸/风切特例；共享扫掠和飞行适配不再持有对应术ID长条件。ExpansionFields/ExpansionBarriers无职责转发已删除。新增纯JDK102术逐字段冻结目录合同及Builder不可变/独立/非法输入合同、飞行边界测试，63/930通过。既有职责拆分后将17个PhaseTwo/Intermediate类型改为SweptSpellProjectile/DirectionalSpellCarrier等真实机制名，完整清单r10-mechanism-class-names.json；原phase_two_spell实体ID、PHASE_TWO兼容枚举、所有附件/存档/网络ID保留。完整离线build通过；仍未完成整体重构，不启动游戏、不Git。

完整离线build退出0：63单测/930模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-flight-mechanism-catalog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续Reference模型资产加载/渲染分离与视觉目录，拆地形租约纯计划/预算和服务端MagicSystem输入网络职责；最后逐术账本、模式审查及R00—R10完成门禁审计。

## 地形纯调度、视觉网格策略、元素目录与领域生命周期（2026-11-13）

MagicSystem服务端网络/动作/会话/生命周期及参考模型资源加载已分责。地形租约采用JDK-only TerrainLeaseScheduler与实例TickQuota，4项合同覆盖恢复优先、失败耗额、过期/延期、不足配额和12次成功组装上限；持久ID/Codec及玩家替换保护保留。40术分层网格生成迁入纯核11类材质Strategy，820组旧新网格逐顶点/颜色精确差分，2项模块合同；39项领域视觉布局纯规格，客户端不再依赖服务端世界适配器。102定义拆10个元素目录，公共常量/ALL顺序和逐字段基线保留。领域实体38项tick/范围/save/load显式绑定，Illusion/CoreSpell/ExpansionSpell及早晚加载顺序保持。20个已无职责领域及单术转发门面删除，真实调用直连；地形注册与权限/临时放置/爆破掉落拆分，expiry通过窄方法封装。全部完整离线build通过；整体最终审计尚未完成，实际游戏NOT_RUN。

完整离线build退出0：63单测/936模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-terrain-visual-catalog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：核对领域绑定兼容语义和视觉布局合同，处理SpellHooks可写静态扩展点及残余职责；完成102术追踪、模式审查、真实文件树与最终门禁。
