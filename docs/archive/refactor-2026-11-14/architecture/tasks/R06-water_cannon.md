# R06-water_cannon：准备/释放与规则切片（离线PASS）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`water_cannon`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


依赖R05准备/释放及R06恒速穿透规则；intent I-02/I-03/I-08/I-10/I-12。正式《魔法-游戏设定》第16术和有效参数快照。

- 8MP、7伤害、160tick准备/10tick恢复；恒速2格/tick、120tick/240格。请求眼部准星、散布0；直击每目标每发一次，不爆炸，成功伤害后60%湿身200tick、水平击退.8。
- 碰方块3×3×3群伤，各7伤害；木工具可采且有掉落方块的1×1×1破坏/掉落保留；必须遵守权限。流体仅终止，穿水及可含水不完整块（含干燥）规则保留，熔岩阻挡。
- 旧入口SpellExecutors→IntermediateSpells.execute/projectile→IntermediateProjectile.tick→IntermediateSpells.hurt/blockImpact→MagicTerrain.blast。先读IntermediateRules、MagicFlight和逐术规格，核对160tick准备及10tick恢复的构造后有效值再落代码。
- 下一精确动作：先写成功/取消/失败回滚/恒速寿命、碰块与直击分离、整发去重、湿身/击退、权限/地形合同；提取纯规则和实际应用模块，游戏层负责原版伤害/方块操作，不复制假世界。只切water_cannon，保留Intermediate里尚未迁其他术。
- 复用真实必要的Strategy/State/Command/Factory Method/Adapter/Observer，不加解释器/DSL或无业务空类。不更改原实体/资源/存档ID。
- 执行build --offline，归档日志和JUnit证据；原版世界GameTest、视觉/联机/性能NOT_RUN。不启动游戏，不操作Git。

## 已迁范围与证据

WaterCannonSpec/Rules、真实CastingSession/Command及显式ProjectileComposition绑定已接入。请求眼部准星发射、8MP、160tick准备、10tick恢复、120tick寿命、.6严格湿身概率保持。IntermediateProjectile仍为兼容世界载体，纯规则负责水炮的寿命与直击先后判定。新WaterCannonImpactAdapter只接碰块群伤，继续调用既有MagicTerrain.blast(width1,false)，未替换权限、木工具与真实掉落结算；这些仍需后续地形阶段及GameTest覆盖，不能将纯合同声称为权限测试通过。

原来的直击UUID集合及MagicVictims保存未改；碰块群伤独立于直击集合，原代码可能再次尝试伤害已直击目标。按行为保持不擅自统一去重，D-005记录待核查。PaidMana仍默认10（原paidMana(8)也截断为10），对水炮伤害无作用，兼容字段不改。

完整构建exit0，evidence/r06-water-cannon-build.log及r06-water-cannon-junit-summary.json。21项纯单测、44项核心模块测试、31,000几何差分与15,658旧行为断言通过；102参数、39/102教材、旧玩家Codec、资源哈希/JAR检查通过。实体世界存档、地形权限/掉落、PVP、实机视觉/联机/性能NOT_RUN。本卡只完成上述切片，Intermediate的其他职责与世界运行态尚未完成重构。

后续共享抽取：R06-ex_flame将碰块群伤统一为BlockBurstImpactAdapter，水炮直接传width1/ignitefalse，原WaterCannonImpactAdapter零引用后删除；参数及原权限/掉落路径保持。最新证据r06-ex-flame-build.log。
