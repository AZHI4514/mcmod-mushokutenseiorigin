# R06-intermediate-effects：STRUCTURE_SLICE_DONE

## 本轮允许范围与合同（2026-11-13）

已核对：SpellExecutors仍调用execute，TARGET_AREA注册为water_splash/icicle_field；前者在execute开头已专用分流，后者及其他输入兼容fallback不能删除。blockImpact运行调用在IntermediateStepAdapter，常规只传水炮/大火球；其他输入兼容保留。

允许新增DeferredAreaImpact纯核心/模块合同、IntermediateAreaAdapter、SpellVisibilityAdapter和IntermediateImpactAdapter；IntermediateSpells保留签名与分流但迁出世界适配。领域命中保持逐目标筛选→wet快照→hurt→效果/消费湿身→afterHit，全部目标结算后才逐个encase，最后burst。目标或事件异常不得提前造冰/补burst。可见性保留逐样本hasChunkAt短路与COLLIDER/NONE射线；不加载新区块。blockImpact fallback按原表达式留在适配层，不擅自合并读取时点。本轮不将icicle_field记为整术迁移完成。

共享中级弹体命中职责迁移卡。目标是把 IntermediateSpells.hurt 中普通法术的成功伤害→击退→状态效果→afterHit 顺序抽为 magic-core 纯协调，并由 Fabric Adapter 提供世界伤害、原版击退、效果和事件端口。TornadoImpact 保持独立适配器与既有核心规则，不并入通用解释器。

约束：保留伤害失败短路、owner 与 motion 的读取时点、异常不回滚、法术注册/资源/存档/费用不变；不改变 blockImpact、visible、execute 的范围爆发语义。禁止新增空工厂、通用魔法解释器或跨术大门面。

允许路径：magic-core/src/main/java/com/example/examplemod/magic/mechanic/projectile/IntermediateHit.java、对应 moduleTest；src/main/java/com/example/examplemod/infrastructure/fabric/magic/IntermediateHitAdapter.java、IntermediateSpells.hurt 兼容委托；本任务卡、REFACTOR_STATE、MIGRATION_LEDGER、PATTERN_REVIEW、evidence 与 changed-files 清单。验证使用 gradlew.bat build --offline；游戏/Fabric事件/联机/视觉/性能验收仍在全部重构完成后统一进行。

下一步：先实现纯协调与窄 Adapter，再以组合合同验证成功/失败、击退读取时点、效果与 afterHit 顺序及异常截断。

## 中级普通命中协调（2026-11-13）

IntermediateHit 纯协调与 IntermediateHitAdapter 已接 IntermediateSpells.hurt；风枪龙卷仍使用独立 TornadoImpactHitAdapter。保持伤害成功后才判断击退并读取 motion、附效时才读取 owner，按 hurt→条件 knockback→effect→afterHit 原顺序执行。失败立即停止，异常原样传播、不回滚、不补调用。8项模块合同验证成功/拒绝、零击退、伤害后状态读取与三阶段异常截断。

完整离线 build 退出0：63单测/471模块测试，旧15,658断言、102有效定义、39/102教材、旧玩家存档fixture、资源与JAR/架构边界通过。证据 r06-intermediate-hit-build.log 及对应JUnit汇总/XML。端口测试不等于真实Fabric伤害/事件集成；游戏/Codec重载/视觉/联机/性能 NOT_RUN，全部重构完成后统一游戏测试，不启动游戏、不Git。

共享弹体运行时 STRUCTURE_SLICE_DONE，退出归属见 evidence/r06-intermediate-runtime-structure-exit.md；整体R06未完成。当前唯一任务 R06-intermediate-effects IN_PROGRESS。下一精确动作：审计 IntermediateSpells.execute 的剩余 TARGET_AREA fallback 与 blockImpact 其他输入fallback的真实调用及兼容合同，再拆范围命中/可见性世界适配；禁止仅凭注册路径无调用删除兼容行为。

## 中级范围结算和世界效果结构切片（2026-11-13）

DeferredAreaImpact纯核心负责按查询顺序筛选、wet快照、伤害及效果/事件，全部命中完成后才依次encase，最后burst。IntermediateAreaAdapter承接原12格加载检查/选点/半径6/效果及地形；保持兼容其他输入和water分支，不改数值。9项模块合同覆盖多目标先命中后造冰、筛选/伤害失败、湿身快照、效果决定入队、空目标和事件/效果/地形异常截断。

SpellVisibilityAdapter承接原加载采样和COLLIDER/NONE射线；9个已迁Adapter直接调用它，未迁世界类保留IntermediateSpells.visible委托。IntermediateImpactAdapter保留水炮/大火球/其他输入碰块分流及原读取时点，视觉burst后才读取owner播放音效。未扩大对其地形/视觉的测试声称。

完整离线build退出0：63单测/480模块测试、15,658旧行为断言、102定义和39/102教材、旧存档fixture、架构/资源/JAR检查通过，证据r06-intermediate-area-build.log及JUnit汇总/XML。游戏/Fabric/Codec重载/视觉/联机/性能NOT_RUN；按用户要求全部重构完成后统一实测。不启动游戏、不Git。

R06-intermediate-effects结构切片STRUCTURE_SLICE_DONE，不等于R06整体退出。IntermediateSpells保留弹体构造/执行分流及hurt分流；发布装配统一归R06-release-routing，实体核心/注册ID/资源未变，icicle_field不因此记为整术迁移完成。下一唯一任务R06-phase-two-runtime：先审计PhaseTwoEntity服务端tick、扫掠/停留/保存及已迁单术端口，建立职责清单后拆分；R07领域/障壁的未迁业务仍随单术处理。
