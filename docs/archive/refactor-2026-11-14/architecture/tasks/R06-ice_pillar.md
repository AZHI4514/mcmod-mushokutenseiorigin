# R06-ice_pillar：准备/释放/柱体规则切片离线PASS

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`ice_pillar`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


SpellId：ice_pillar（冰柱，第22术）。前置R06-ice_smash切片、R06-stone_cannon切片；intent I-02/I-10/I-12，正式《魔法-游戏设定》第22术优先。

规格：8MP、4伤害、中级160tick准备/10tick恢复；12格首个有效生物/方块，瞬时1×3×1柱体，无飞行。只填空气，临时冰200tick；柱内生物一次伤害并弹起，80%冻结200tick。无目标或权限/空间预检失败不生成；保留原目标选择、未加载区块和抬升预检。

旧入口：SpellExecutors→PhaseTwoSpells.execute/instant→trace/raiseAt/preflight；PhaseTwoRuntime.growingColumn/tickColumns；MagicEffects.apply。旧phase_two_spell载体及临时冰/效果ID保持。不能套ProjectileSpawner；不能仅注册Spec就声称用例完成。

目标：IcePillarSpec、IcePillarRules纯几何/状态；IcePillarAdapter专属世界生成，ColumnPlacementAdapter共享原空气/权限/抬升预检。纯逻辑不强配模式；窄端口和真正Command/Observer等在后续有实际职责时接通，禁止空工厂。

本轮允许：magic-core ice_pillar规格/规则与测试、PhaseTwoSpells/MagicEffects定义接入、专用柱体Adapter及共享预检、对应证据和长期文档。不改原参数/资源/网络/存档/视觉，不迁其他术业务。撤回范围限本任务实际改动，保留所有既有用户工作，不用git reset。

执行顺序：先迁纯几何/冻结判定和raiseAt生成适配；再核对可拒绝的前置查找与不可逆应用边界，设计独立瞬时柱体用例，接入准备State/费用Command/Observer。成功只扣一次8MP，失败不扣、取消不生成、重复提交拒绝；真实权限、目标顺序、抬升/逐格生长与临时方块续期要单列游戏验收。

最小测试：柱体精确1×3×1/负坐标；冻结阈值/免疫/暖身；无目标/预检失败/施法成功/重复提交/观察者失败；只填空气、抬升净空/权限/未加载区块及临时冰重载边界分别记录纯合同与世界未测。命令gradlew.bat build --offline，归档evidence/r06-ice-pillar-build.log及JUnit XML；不得把源码审计冒充世界测试。

下一SpellId：waterfall。GameTest/视觉/联机/性能NOT_RUN；不启动游戏、不操作Git。

## 实际完成范围与证据

IcePillarSpec/Rules、IcePillarAdapter与共享ColumnPlacementAdapter已落地，PhaseTwoSpells.raiseAt零调用后删除，原预检原样移出而非新增转发。MagicEffects冻结分支复用EffectRules，保留每次成功伤害后的原随机采样时机。TargetedAreaComposition现在显式装配水蒸/冰柱，MagicPreparation选择实际术的Command并使用Preparing/Idle State。接口契约：locate只读且完成放置预检，空结果不扣费；apply在同一个服务端线程紧接扣费后执行，不再出现预检可拒绝分支。应用开始后的异常不退款部分效果，观察者异常不重施法。

41项单测/71项模块测试通过，含3项几何/冻结单测、4项冰柱模块合同及3项岩炮弹收尾合同；完整gradlew.bat build --offline退出0，evidence/r06-ice-pillar-build.log、r06-ice-pillar-junit-summary.json及ice-pillar-前缀XML。目标为空/预检失败、费用不足、160tick准备/取消/超时、提交顺序、重复提交、不可逆异常与观察者失败通过端口合同；不是实际方块/射线验证。

只关闭上述切片。trace、伤害/权限/实体载体、PhaseTwoRuntime逐格生长及其静态容器、MagicTerrain租约/重载、实际模型视觉保持兼容服务，后续世界生命周期工作仍待迁。旧phase_two_spell、临时冰/冻结ID、资源/网络/存档未改。GameTest、真实世界/权限/联机/性能/客户端均NOT_RUN。下一任务waterfall，不操作Git。
