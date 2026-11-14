# R06-ice_lance：SLICE_DONE（核心/协调，非全术或世界验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`ice_lance`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


ID ice_lance，第24术，前置冰柱。正式规格docs/魔法-游戏设定.md第24术及冻结有效值快照优先。MP10、伤害8、160tick准备/10tick恢复、12格首个生物或方块选点；即时生成向上1×3×1局部视觉，无飞行/方块生成，生成后首tick可见目标各命中一次，80%冻结200tick。保留历史寿命字段12、实体递增age与现有同步/保存ID，不擅自修正寿命计数。

已做：Spec被SpellDefinition引用；damageDue用于PhaseTwoEntity；freezeTicks用于MagicEffects且不改变随机采样。2项规则单测通过。

执行：先核对PhaseTwoSpells.instant的选点和addFreshEntity返回值、MagicPreparation失败恢复及SequentialMagic；建立只有即时定位/布置boolean职责的窄端口及真实应用用例，复用现有Command/State/Observer的正确契约，不强塞void TargetedArea或飞行弹体spawn；覆盖选点失败不扣费、生成false回滚、成功一次收费、重复提交/取消、组合成功通知合同。之后审计首击范围/可见性/伤害/存档职责，逐项抽纯逻辑与世界Adapter。

仅允许本术核心、专属适配器/装配、旧入口、测试和文档修改；保留其他用户改动。禁止批量改名、空工厂、DSL和新模式。完整build --offline并归档证据。世界/Fabric/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

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
