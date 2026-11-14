# R06共享职责残余矩阵（2026-11-13）

R06逐术结构切片不等于整个R06退出。提案10.3要求拆PhaseTwo/Intermediate职责，R10才最终清零旧类业务；不能把所有残余推迟到R10而跳过本阶段业务迁移。

| 旧类/证据定位 | 已迁 | 剩余及后续独立任务 |
|---|---|---|
| MagicPreparation.ACTIVE/request/tick/cancel | Preparing/Idle、已迁术Command | R06-preparation-runtime：先移除进程全局Map，使服务器实例拥有准备会话；之后另卡拆资格/发布协调，R07/R08仪式分支各随其术迁 |
| IntermediateProjectile.tick/addAdditionalSaveData/readAdditionalSaveData | 多术Flight/Hit/Terrain纯协调与Adapter | R06-intermediate-runtime：共享扫掠、生物/墙/流体顺序、未加载挂起、命中历史、PaidMana/ExpansionBroken与Codec仍在载体；按现有实体ID分步提窄世界端口，不改存档格式 |
| IntermediateSpells.execute/hurt/blockImpact/visible | 部分生成、范围爆发、TornadoImpactHitAdapter | R06-intermediate-effects：其余伤害/附效/原版击退适配及兼容执行入口仍聚合，保留原爆炸多目标/费用特例；visible世界射线可作为Adapter方法而非核心逻辑 |
| PhaseTwoEntity.tick及保存方法 | 冰枪、土柱、风裂等规则/协调 | R06-phase-two-runtime：R06弹体/命中路径仍有共享扫掠和载体分支；R07领域/障壁路径随各术迁；保持phase_two_spell及同步/保存键 |
| PhaseTwoSpells/PhaseTwoRules | 部分规则已核心委托 | R06-phase-two-effects：R06剩余发射/伤害/碰撞职责逐方法迁；R07预检/范围则随各术，不能一并宣称完成 |
| PhaseTwoRuntime | 水瀑通道实例及物理盾Runtime已迁 | R06-channel-runtime：火焰放射与水瀑共享通道的剩余资格/付款/停止协调须审计；R07维持/领域表各随术迁 |
| ThermalGust.cast/contains | 两个注册术已各自Adapter | R10兼容退出：contains有MagicStateTest调用；cast保留其他输入fallback，先证零运行调用再收口；不是已删除 |
| SpellExecutors及MagicPreparation泛型执行 | 已迁术走显式Composition并return | R06-release-routing：审计R06全部装配与兼容调用；R07/R08未迁分支不能提前删除 |

上述命名为待建立的任务标识，仅当前preparation-runtime创建可执行卡，其余矩阵行不是声称文件已存在。优先处理全局准备容器，避免跨服务器同UUID会话串扰；只改内存归属，不新增持久化、不改变每tick与取消顺序。下一步仍须完成共享残余代码，R07不得据本矩阵自动开始。

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

## 中级范围结算和世界效果结构切片（2026-11-13）

DeferredAreaImpact纯核心负责按查询顺序筛选、wet快照、伤害及效果/事件，全部命中完成后才依次encase，最后burst。IntermediateAreaAdapter承接原12格加载检查/选点/半径6/效果及地形；保持兼容其他输入和water分支，不改数值。9项模块合同覆盖多目标先命中后造冰、筛选/伤害失败、湿身快照、效果决定入队、空目标和事件/效果/地形异常截断。

SpellVisibilityAdapter承接原加载采样和COLLIDER/NONE射线；9个已迁Adapter直接调用它，未迁世界类保留IntermediateSpells.visible委托。IntermediateImpactAdapter保留水炮/大火球/其他输入碰块分流及原读取时点，视觉burst后才读取owner播放音效。未扩大对其地形/视觉的测试声称。

完整离线build退出0：63单测/480模块测试、15,658旧行为断言、102定义和39/102教材、旧存档fixture、架构/资源/JAR检查通过，证据r06-intermediate-area-build.log及JUnit汇总/XML。游戏/Fabric/Codec重载/视觉/联机/性能NOT_RUN；按用户要求全部重构完成后统一实测。不启动游戏、不Git。

R06-intermediate-effects结构切片STRUCTURE_SLICE_DONE，不等于R06整体退出。IntermediateSpells保留弹体构造/执行分流及hurt分流；发布装配统一归R06-release-routing，实体核心/注册ID/资源未变，icicle_field不因此记为整术迁移完成。下一唯一任务R06-phase-two-runtime：先审计PhaseTwoEntity服务端tick、扫掠/停留/保存及已迁单术端口，建立职责清单后拆分；R07领域/障壁的未迁业务仍随单术处理。

## PhaseTwo飞行/驻留/落石结构切片（2026-11-13）

PhaseTwoFlightAdapter/StationaryAdapter/FallAdapter已承接原世界操作；载体只委托并注入原ImpactHistory、私有计数回调与方向/age快照。候选复用ContactCandidates筛完后实时contact稳定排序，保留历史求值次序；PhaseTwoContactTraversal先claim，爆炸定位后读取piercing→explode→重读piercing，不额外burst/discard；普通命中effect→burst→piercing→条件discard。风裂AOE继续使用同一ImpactHistory。PhaseTwoTermination保留流体同距优先、爆炸墙前0.02定位后explode、普通墙burst后discard。存档age/remaining复用原等价非负规则，默认值及逐字段赋值、历史最后替换保持。

18项新增模块合同（接触10、终止8），完整离线build通过：63单测/510模块测试、15,658旧断言、102有效定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-runtime-build.log及JUnit汇总/XML。结构切片不代表真实世界/Codec重载/视觉/联机/性能验收，以上NOT_RUN。用户授权持续执行全部重构，全部完成后统一游戏测试；不启动游戏、不Git。

R06-phase-two-runtime结构切片STRUCTURE_SLICE_DONE：实体同步/Codec/客户端与体积身份仍是载体，新增Adapter的世界API/单术分支继续依赖各自已有核心规则。几何身份配置与零引用兼容最终归R10，不能称整个R06完成。下一R06-phase-two-effects：迁damage/areaDamage的权限→伤害→附效→hook→洪流击退协调及共享加载/射线适配；准备/发布装配仍后续专卡。未把R07/R08未迁法术计为已完成。
