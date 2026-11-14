# R06-tornado_impact：SLICE_DONE（核心/协调；非全术）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`tornado_impact`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


SpellId tornado_impact，正式名风枪龙卷。intent ALL_MAGIC_EXPANSION.md第105行：38MP、16伤害、直径1.5长3、恒速3、180tick、沿飞行方向1.0冲量；永久模式最多破64个石制工具可采方块。前置R05/R06弹体切片、sonic_boom/wind_slice。

旧类：IntermediateSpells.projectile/hurt、IntermediateProjectile.tick、SpellDefinition、MagicSystem.TORNADO_IMPACT。目标TornadoImpactSpec/单术命中与地形规则、既有ProjectileComposition/Preparing/Command/VanillaProjectileAdapter；不建空工厂。已接准备释放，240tick/38MP/20恢复，眼部请求准星速度3散射0；原paid10保存保持。

契约与最小测试：成功单次收费/false与生成异常退款/MP知识角度拒绝/取消超时；后续伤害成功才冲量、免疫标签、去重、最多64格计数、不可破格停止、流体/墙/寿命/未加载退出及保存重建。真实权限/掉落/游戏重载NOT_RUN。实体/法术ID tornado_impact、MagicAge/MagicVictims/ExpansionBroken/PaidMana等旧键不变。

允许：tornado_impact core/tests、IntermediateProjectile/IntermediateSpells/SpellDefinition、ProjectileComposition及必要Adapter、架构文档。只回撤本卡差分，不动用户其他改动。

命令gradlew.bat build --offline退出0，证据r06-tornado-start-build.log与JUnit汇总/XML。4项准备释放合同通过；不是世界集成。旧入口切换由ProjectileComposition.handles触发MagicPreparation命令分支且执行后return。

下一动作：提取伤害成功后冲量/免疫标签/附效/afterHit协调，再迁永久模式64方块路径预算规则与实际Adapter，审计飞行退出保存。下一SpellId frost_nova。不启动游戏、不Git操作。

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

# 风枪龙卷结构切片退出

2026-11-13，SLICE_DONE仅指Spec/准备/付费释放/命中协调及世界HitAdapter/地形预算/飞行纯规则。新命令分支在MagicPreparation.release后return，旧IntermediateSpells兼容释放不双发；hurt单术Adapter后return，不重复冲量。

地形World匿名适配仍在IntermediateProjectile，只读写载体expansionBroken并调用原ExpansionTerrain；其永久模式、ServerPlayer资格、候选排序、wall/block/impact写入属于共享载体世界扫掠的剩余职责，后续与其他弹体世界适配一起迁，当前不冒充已迁出。实体age/hit/ExpansionBroken保存、原实体注册ID及客户端仍保留；R10必须逐项证明已无业务分支和调用后才能清理旧类。未改水穿透、地形先于流体、64成功计数和原存档键。

证据：r06-tornado-flight-audit.md及完整r06-frost-start-build.log、现有核心合同。实际Fabric/Mixin、原版冲量/权限/掉落、Codec重载、视觉/联机/性能NOT_RUN。不是全术/整阶段完成。下一账本R06-frost_nova。
