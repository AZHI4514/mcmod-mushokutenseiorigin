# R06-icicle_break：SLICE_DONE（核心/协调；非全术或世界验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`icicle_break`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


第25术冰霜击，正式docs/魔法-游戏设定.md第25术；前置ice_smash。12MP/伤害2/160tick准备/10tick恢复，恒速3格/tick无重力、120tick飞行预算，生物穿透每发每生物一次，成功伤害后80%冻结200tick，无群伤/地形改变。水/流水/有WATERLOGGED属性的不完整方块（干湿均可）穿透，普通固体/熔岩仍阻挡。历史定义寿命100/range18不等于实际飞行预算/上限，保留有效快照。

已做：IcicleBreakSpec数值及冻结函数接入定义/MagicEffects；样本差分单测通过。

下一动作：审计PhaseTwoSpells.execute→entity/configure和PhaseTwoEntity.tick/fly/save，迁到复用现有CastSpellCommand/ProjectileSpellFactory的显式绑定；专属ProjectileSpawner必须完整configure而非普通setOwner/shoot，保留请求准星与初始eye原点。先合同覆盖160tick准备/取消、成功/false恢复/重复拒绝、实际spawn plan；再迁120tick预算、每发UUID与存档、含水/熔岩/固体碰撞、伤害权限与附效边界。保持原版不可逆世界异常提交语义，不建空Factory/DSL/批次门面。

允许本术core/adapter/composition/旧入口/测试/文档；保留已有用户修改。每次完整build --offline并归档证据。真实游戏/Fabric/实体重载/PVP/地形/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

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
