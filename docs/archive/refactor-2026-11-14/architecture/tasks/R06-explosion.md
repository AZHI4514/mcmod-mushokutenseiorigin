# R06-explosion：SLICE_DONE（核心/协调；非全术或世界验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`explosion`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


第29术，前置ex_flame。16MP/160tick准备/10tick恢复，恒速2、无重力、120tick飞行预算。定义damage10/历史寿命1/range12不是原版TNT动态伤害与飞行上限。

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
