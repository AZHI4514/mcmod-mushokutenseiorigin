# R06-ex_flame：准备/释放与纯规则切片（离线PASS）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`ex_flame`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


大火球，正式《魔法-游戏设定》第19术；intent I-02/I-03/I-08/I-10/I-12。12MP、8火伤、160tick准备/10tick恢复；恒速2格/tick、120tick/240格，2×2×2飞行体。成功伤害后严格小于.8概率点燃200tick。

直击生物穿透且每发每目标一次，不触发地形爆破；碰块3×3×3群伤（每个8火伤），2×2×2木工具可采/有掉落方块破坏并保留掉落，空位生成原版火。流体仅终止；水和可含水不完整块干/湿均可穿、熔岩阻挡。保持原版无敌帧及D-005类似独立碰块群伤语义，不能擅自统一去重。

先读IntermediateProjectile/Spells/Rules、MagicTerrain.blast/mayChange及有效参数快照；先写纯合同，再接显式ProjectileComposition/REQUESTED_AIM，不额外建空Factory或通用解释器。权限/掉落/火蔓延留真实游戏Adapter，不能用假世界测试声称GameTest通过。

边界：仅迁ex_flame及真实共享规则；旧批次类其他术不改名不删除，实体/资源/存档ID不变，视觉保持。build --offline后归档日志/XML与唯一状态。游戏/联机/性能NOT_RUN；不启动游戏，不操作Git。

## 实际完成与未验收边界

ExFlameSpec/Rules独立持有12MP、8伤害、160tick准备/10tick恢复、120tick飞行、2格体积、2格地形宽度及200tick点燃。显式ProjectileComposition接入State/Command/Factory/REQUESTED_AIM Adapter，保留眼部请求方向零散布；无需每术空工厂。

水炮/大火球的寿命、直击先后及3×3×3群伤包围盒公式统一在BlockBurstRules，世界群伤统一BlockBurstImpactAdapter；原WaterCannonImpactAdapter只有委托后已删除，调用者直接注入width/ignite，不留无用门面。原Intermediate仍为未迁术实体载体，本卡不代表其全部职责完成。

MagicEffects仍先合法目标判断、一次nextFloat，再调用严格.8点燃策略，火免疫/抗火保持；地形依旧调用MagicTerrain.blast(width2,true)，每块保留mayChange、木工具/掉落与原版火canSurvive。碰块群伤仍独立于直击UUID去重，D-005语义保留。原资源/实体/存档键与动画未改。

最终build --offline退出0：evidence/r06-ex-flame-build.log及r06-ex-flame-junit-summary.json。26单测、54模块测试、31,000几何差分与15,658旧断言通过；102术参数/39与102教材、旧玩家存档、资源/JAR检查通过。原版区块碰撞/伤害/PVP、地形权限/掉落/蔓延、实体save/load、视觉/联机/性能均NOT_RUN，代码路径审查不等同这些GameTest通过。

下一项sonic_boom真空波；不启动游戏、不操作Git。
