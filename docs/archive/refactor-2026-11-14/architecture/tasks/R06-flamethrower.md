# R06-flamethrower：SLICE_DONE（核心/协调；非全术或世界验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`flamethrower`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


第31术，前置fireball/burning_place。6MP/秒按tick，完成160tick准备后按住持续，停止10tick恢复；16格/半角25度可见锥内LivingEntity每目标20tick尝试4火伤，成功火伤沿原MagicEffects 80%点燃200tick。无实体飞行/地形破坏。

入口初查：MagicPreparation完成后PhaseTwoRuntime.channel；非waterfall分支ChannelSession，begin先pay→存channel→glow→flame→sound；tick先资格/heartbeat过期→pay→glow→flame，stop保留负债预付信用。flame搜集player bbox inflate16允许目标，PhaseTwoRules.inCone→visible→contactReady(owner UUID,target UUID,now)→damage，再输出旧8段粒子。CONTACTS目前共享静态表，不可未审计就改为每次释放独立表或清除历史20tick窗口。

下一精确动作：完整审计begin/tick/stop和contacts所有调用/清理（含火柱载体）及现有ChannelPayment/ChannelSession；用独立通道协调类接实际入口，抽纯锥形与命中节拍；需按真实需求接Channeling State而不是建空子类。保留流式费用/失败/先付款后伤害/心跳与冷却max规则，动画粒子/伤害/范围及ID保持。不复制水瀑Source端口、不造通用效果解释器。补边界和组合合同，完整离线build归档并更新唯一状态。实际游戏/权限/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 火焰放射通道/命中/服务器窗口切片（2026-11-11）

FlamethrowerChannel接真实PhaseTwoRuntime入口，复用ChannelPayment与既有CastingSession的Channeling/Idle State。begin只付首次费用并激活，runtime先注册再emit（发光→世界命中/旧粒子）→release sound；tick先资格/第7次缺心跳终止、再付费→emit。每20次付费发射累计6MP；零MP时可消费已预付信用，新开通道仍至少1MP；stop幂等、恢复max(existing,10)，不退款负债信用。水瀑独立协调保留，不套用其WaterSource/周期生水行为。

FlamethrowerHit迁出16格/25度锥形和候选→锥内→可见→claim→伤害顺序，Adapter保留原LivingEntity候选、原版伤害/附效，以及8段粒子、分散度和遮挡中止。PhaseTwoRules.inCone兼容入口委托同纯函数。FlamethrowerSpec接6MP/4伤害定义；102有效定义未变。

ContactCooldowns拥有来源UUID→目标UUID→时间窗口，仍20tick整点过期、claim在伤害前、不因失败伤害或短暂停止重开而重置。旧静态CONTACTS删除，contact_runtime为服务器主世界不持久化/不同步附件；火焰放射用玩家UUID，火柱实体载体用实体UUID，两真实调用均显式传ServerLevel。clear玩家仅清对应来源，停服移除整服附件；SHIELDS及其他遗留表尚未全迁。

新增17模块合同覆盖6MP/20发、资格/心跳/不足MP/小数信用、停止重建/异常/玩家隔离，锥形边界与顺序，命中窗口期限/来源隔离/倒退时间，以及停止重开不重置命中窗口与信用的核心组合。另有2000组旧锥形表达式差分采样。完整离线build退出0：63单测/171模块测试，旧15,658行为断言、102定义/教材、资源和JAR检查通过；证据r06-flamethrower-build.log、JUnit汇总与exit-audit。

已验证核心/通道/命中窗口切片SLICE_DONE；共享世界权限/射线/原版伤害与客户端拆分仍待后续，不等同全部法术重构完成。下一任务R06-wind_slice：核对风裂生物直击与3格AOE的共享身份窗口、原规格首击描述和当前多接触行为，再迁准备释放与纯命中协调。实际游戏/Mixin/多服务器附件/权限/保存/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。
