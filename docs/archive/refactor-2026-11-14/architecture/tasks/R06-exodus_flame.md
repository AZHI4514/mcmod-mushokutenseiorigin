# R06-exodus_flame：SLICE_DONE（核心/适配；非全术）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`exodus_flame`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


- SpellId：exodus_flame；intent：ALL_MAGIC_EXPANSION.md第93行及魔术设定；44MP、18火伤、直径3、速度2、半径4、上级180tick飞行、15秒火附效，不生成TNT。
- 前置：R05与R06原弹体核心切片；物理盾为提前实施R07结构切片，不重复分配。
- 旧入口：SpellDefinition.EXODUS_FLAME，SpellExecutors ADVANCED_PROJECTILE，IntermediateSpells与IntermediateProjectile.tick/exodusBurst；MagicSystem.EXODUS_FLAME实体注册保持。
- 目标：ExodusFlameRules/后续Spec、既有ProjectileComposition/Command/Preparing复用、真实弹体Adapter与专属爆发协调；无新空Factory/自造解释器。
- 已迁：半径球形判断与查询半径常量，真实exodusBurst接线。保留否定outside比较及NaN语义。3项模块合同及10,000距离差分通过。
- 契约：目标UUID去重先于伤害；原owner伤害和80%自燃路径不与他人MagicEffects合并；保留可见性、PVP过滤、命中钩子顺序与异常传播。准备/扣费/取消接线尚未迁，不能标已完成。
- 兼容：exodus_flame实体/法术/资源ID、MagicAge/ExpansionBroken/PaidMana和命中保存键不变；不更改飞行卸载discard行为。
- 允许文件：exodus_flame core/tests，IntermediateProjectile/IntermediateSpells/SpellDefinition/ProjectileComposition/MagicPreparation及必要Adapter，相关架构文档。
- 最小验证：成功/false退款/取消/重复提交/非法资格，爆发半径与同目标去重、伤害成功/拒绝、owner分支、墙/流体/寿命/未加载退出、命中历史重建；真实世界/重载另列NOT_RUN。
- 命令：gradlew.bat build --offline（包含verifyMagic/core test/moduleTest/architecture/resource）；最新退出0，证据r06-exodus-start-build.log与JUnit汇总/XML。
- 回撤范围：只回撤本卡差分，保留用户改动；不git reset，不改旧定义有效数值或模型纹理。
- 下一精确动作：审计AdvancedProjectile准备/释放与原发射参数，再接单术Spec和显式Composition绑定及付款合同；之后爆发动作协调和飞行退出审计。下一SpellId tornado_impact。

## 狱炎火弹准备与付费释放（2026-11-13）

ExodusFlameSpec及ProjectileComposition显式Binding已接：44MP、240tick准备、20恢复、速度2/散射0，沿请求准星从眼部原点发射。MagicPreparation自动选既有Preparing/Idle与CastSpellCommand，复用VanillaProjectileAdapter；IntermediateProjectile默认paid10与旧兼容入口一致，不误用44作为岩炮弹注魔数据。SpellDefinition有效值基线保持。旧IntermediateSpells兼容执行仍保留，不额外收费、不双发。

新增4项模块合同覆盖准备不收费/单次提交/正确plan，false及生成异常恢复完整快照，MP/知识/非法角度拒绝和取消/心跳超时。完整离线build通过：63单测/300模块测试，旧15,658断言、102定义及教材/资源/JAR检查通过。证据r06-exodus-release-build.log及JUnit汇总/XML。初次编译的ProjectileSpec访问器命名错误已修正为mana()后重建通过。实际实体生成、Fabric/Mixin/视觉/联机/性能NOT_RUN。

R06-exodus_flame仍IN_PROGRESS。下一步提取exodusBurst的逐目标动作协调：先查历史/半径/可见性/权限，再claim UUID、真实伤害，成功后owner自燃或其他目标MagicEffects，再afterHit；保留80%随机采样和火免疫判断顺序，补拒绝伤害、去重与异常合同，再审计飞行退出及保存。不得把此切片当整术完成。不启动游戏、不自动Git操作。

## 狱炎火弹逐目标爆发协调（2026-11-13）

ExodusFlameHit已接IntermediateProjectile.exodusBurst：历史→半径→可见性→owner/权限→claim→hurt，伤害成功后重新读取owner身份，走selfEffect或otherEffect再afterHit。伤害拒绝或抛异常仍保留claim，附效异常不继续afterHit；不添加回滚、重试或finally。世界窄端口保留真实目标/UUID/伤害源/权限，owner自燃仍先采随机数<0.8，再检查火免疫和抗火，其他目标继续MagicEffects.apply。

新增8项模块合同覆盖真实核心协调顺序、owner分支、拒绝后去重、半径/权限/可见性短路、伤害/附效异常及伤害后身份重读。完整离线build通过：63单测/308模块测试；旧15,658断言、102有效定义及教材/资源/JAR检查通过。证据r06-exodus-burst-build.log及JUnit汇总/XML。合同不执行真实Minecraft伤害事件、随机数或Codec；游戏/Mixin/视觉/联机/性能NOT_RUN。

R06-exodus_flame仍IN_PROGRESS。下一步审计并提取飞行生物接触/墙或流体/寿命/未加载退出与命中历史重建协调，保持同目标整发一次、爆发可波及owner、墙前偏移及原保存键；之后再迁爆发世界Adapter和评估结构退出。不启动游戏、不自动Git操作。

## 狱炎火弹飞行边界与保存审计（2026-11-13）

ExodusFlameFlight接入寿命/低速退出、候选严格早于墙流体、最终步内接触及再次去重判断；保留原短路命中历史读取。4项模块合同及10,000组冻结边界差分通过。共享HashSet及MagicVictims Codec保持，不为本术更改其他弹体历史语义。

完整离线build通过：63单测/312模块测试，旧15,658断言、102有效定义及教材/资源/JAR校验通过。证据r06-exodus-flight-build.log、JUnit汇总/XML和r06-exodus-flight-audit.md。实际世界碰撞/Codec重载/视觉/联机/性能NOT_RUN。

当前R06-exodus_flame仍IN_PROGRESS。下一步迁爆发世界适配至ExodusFlameBurstAdapter，采用原共享Set引用或窄历史端口，不复制/重置历史；补历史重建→爆发拒绝去重的核心组合合同，再核对结构切片退出。实体共享扫掠/存档仍需后续阶段，不把源码审计当实测。不启动游戏、不自动Git操作。

## 狱炎火弹爆发世界适配与共享历史（2026-11-13）

ExodusFlameBurstAdapter已承接世界目标查询、中心反向偏移0.025、真实伤害/自燃/附效/afterHit端口；IntermediateProjectile.exodusBurst仅委托，并传原hit Set引用。未复制或替换集合，直接接触和整发所有爆发仍共享同一历史。原实体/资源/保存键保持。

新增1项组合合同：用已保存UUID集合重建历史，历史目标跳过；新目标伤害拒绝后仍claim，后续飞行接触和再爆发跳过，重建集合保留两者；此为核心Set与协调器组合，不执行真实Minecraft Codec。完整离线build通过：63单测/313模块测试，旧15,658行为断言、102有效定义及教材/资源/JAR校验通过。证据r06-exodus-adapter-build.log及JUnit汇总/XML。

R06-exodus_flame仍IN_PROGRESS。下一步核对结构退出清单，查明IntermediateProjectile里剩余本术尺寸/飞行分支的载体职责和后续迁移归属，核对兼容执行与新Command只有一个生效入口；完成有证据的结构切片后建立账本下一项R06-tornado_impact任务卡。真实游戏/Mixin/伤害事件/Codec重载/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

# 狱炎火弹结构切片退出核对

2026-11-13：仅核心/准备/释放/爆发适配结构SLICE_DONE，非整术或游戏验收。

Spec/Rules/Hit/Flight已接生产入口；ProjectileComposition显式绑定请求准星，MagicPreparation的command执行后立即return，旧SpellExecutors/IntermediateSpells只保留未走新命令时的兼容入口，不双扣/双发。ExodusFlameBurstAdapter使用载体原hit Set，去重/自燃/附效顺序有合同。最新完整build及102有效定义/资源/JAR校验通过。

残留归属：IntermediateProjectile仍有3x3x3体积选择、飞行世界扫掠、age/traveled、墙流体动作和命中Codec；这些属于共享弹体载体与世界几何适配后续迁移，不因单术切片通过而称已清除批次职责。MagicSystem.EXODUS_FLAME注册与MagicVictims/MagicAge等键不可删除；R10须在实体策略及调用链全迁后再证明零引用。旧exodusBurst仅委托，不另持历史。领域/客户端不在该切片范围。

游戏/Fabric/Mixin/伤害事件/Codec实际重载/视觉/联机/性能NOT_RUN。返回R06账本下一NOT_STARTED tornado_impact。
