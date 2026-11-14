# R06-frost_nova：SLICE_DONE（核心/协调/适配）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`frost_nova`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


SpellId frost_nova；intent ALL_MAGIC_EXPANSION.md第137行：40MP，自身中心半径8一次10伤害，湿身额外4并消耗；冻结300tick，玩家控制20/其他40，温暖挡控制不挡伤害；不伤自己。前置R05/R06、水蒸及冰结领域。

旧类FrostNova.cast/SpellExecutors、MagicPreparation。目标独立FrostNovaRules/命中协调/世界Adapter，复用SelfEffect Command/Preparing及显式Composition；不是持续领域，不新增实体tick或容器。已接伤害/半径/控制时长纯规则，3项合同通过。

接口契约：查询排除owner，半径与可见性后先快照wet/warm再伤害；失败不移除状态，成功先移wet再移warm，只有原warm=false时冻结与控制，再afterHit；最后burst，返回true。保留MagicAreaEntity仅作为未addFreshEntity的伤害来源。40MP/240准备/20恢复，ID/教材/资源/原存档不变。

允许frost_nova core/tests、FrostNova/独立Adapter、SelfEffectComposition/MagicPreparation/SpellDefinition及架构文档。最小测试成功/失败/取消/费用/异常、湿暖快照/状态消费/控制免疫、目标可见性；无独立保存，真实事件/世界NOT_RUN。只回撤当前差分，不动用户改动。

精确命令gradlew.bat build --offline退出0，证据r06-frost-start-build.log及JUnit汇总/XML。下一步提逐目标命中协调与世界Adapter，再接SelfEffect Command并补准备付款合同。下一SpellId sonic_blast。不启动游戏、不Git操作。

## 冰霜新星逐目标命中协调（2026-11-13）

FrostNovaHit接真实FrostNova.cast的目标窄端口，按wet→warm读取快照，再hurt(10/14)；成功后按原快照先移wet再移warm。只有原warm=false才尝试canFreeze/冻结300，然后player判定及20/40tick控制，最后afterHit。冻结免疫不跳过控制；MagicControl实际免疫仍由旧实现判断。伤害失败不消耗状态；异常原样传播，不回滚已发生副作用。

6项新合同验证湿身伤害及顺序、温暖只挡控制、伤害拒绝、冻结免疫/玩家控制、伤害期间状态变化不覆盖快照、移除状态异常截断。完整离线build通过：63单测/341模块测试，旧15,658断言、102有效定义/教材/资源/JAR校验通过。证据r06-frost-hit-build.log及JUnit汇总/XML。真实伤害/控制免疫/游戏/视觉/联机/性能NOT_RUN。

R06-frost_nova仍IN_PROGRESS。下一步把FrostNova.cast世界查询/伤害源/burst迁为FrostNovaAdapter并实现SelfEffect，保留旧cast兼容委托；SelfEffectComposition接40MP/20恢复，复用240tick Preparing，补单次付款/取消/失败及部分副作用异常合同。无独立持久状态，不新增实体tick。不启动游戏、不自动Git操作。

## 冰霜新星世界适配与付费释放（2026-11-13）

FrostNovaAdapter实现SelfEffect，持有原玩家并承接全部世界查询/伤害源/爆发；旧FrostNova.cast仅兼容委托同一静态cast，不另收费或双跑。SelfEffectComposition显式绑定，MagicPreparation自动使用Preparing/Idle与单次CastSelfEffectCommand；保持40MP/240tick准备/20恢复，目标中心仍owner.position+Y1，未addFreshEntity的MagicAreaEntity仍仅作伤害来源，遍历完才burst，空目标仍成功。

新增6项模块合同覆盖准备不收费/单次及通知顺序、非法输入/MP知识拒绝、显式false完整退款、未知部分世界异常保留付款且不可重放、观察者隔离、取消准备。真实Adapter正常仅返回true，false合同是端口失败语义，不声称当前世界会返回false。完整离线build通过：63单测/347模块测试，旧15,658断言、102定义/教材/资源/JAR校验通过。证据r06-frost-release-build.log及JUnit汇总/XML。

当前R06-frost_nova仍IN_PROGRESS。下一步组合真实FrostNovaHit与CastSelfEffectCommand验证付费后状态消耗/伤害拒绝及部分异常，审计无独立保存/tick的生命周期与旧兼容调用、目标过滤和可见性，完成结构退出清单后再推进sonic_blast。游戏/Fabric/Mixin/控制免疫/视觉/联机/性能NOT_RUN。不启动游戏、不自动Git操作。

# 冰霜新星结构退出核对（2026-11-13）

仅结构SLICE_DONE。FrostNovaRules/Hit接真实FrostNovaAdapter；SelfEffectComposition绑定现有Preparing/Command，40MP/240准备/20恢复。MagicPreparation有命令时execute后return，旧SpellExecutors→FrostNova.cast仅兼容委托同一Adapter，不双跑。后续R10在全部调用切到明确装配后可证明兼容类零引用再删除。

世界中心owner.position+Y1，查询排除owner且使用原MagicEffects.allowed，目标中心半径<=8与原可见性后读取wet/warm快照。MagicAreaEntity配置但未addFreshEntity，只作来源；遍历结束burst，空目标也true，无新tick/容器/保存键。死亡/跨维/GUI等释放前由共享准备资格取消；释放同步完成，不保留任务，无新保存读写。

新增核心组合合同把CastSelfEffectCommand与FrostNovaHit连用，确认伤害拒绝仍收费不消费状态、部分状态消费异常保留付款且不能重放；不执行Minecraft世界查询/控制免疫或真实伤害事件。63单测/349模块测试、完整build及102定义/资源/JAR通过，日志r06-frost-final-build.log。游戏/Fabric/Mixin/控制免疫/原版事件/视觉/联机/性能NOT_RUN，非全游戏验收。
