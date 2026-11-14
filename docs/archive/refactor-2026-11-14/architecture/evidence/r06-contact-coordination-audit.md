
## 共享弹体生物接触协调（2026-11-13）

ContactTraversal已接IntermediateProjectile排序后的目标循环，逐目标重算contact，再由skip窄端口保留狱炎历史短路；非狱炎原Set先claim后定位/伤害，狱炎仍由爆发使用原Set。piercing循环前快照；穿透每目标effect后particles，非穿透effect后重复定位→particles→discard→return。核心不分派SpellId，异常保留已发生副作用且不补执行后续动作。候选/排序、世界伤害和爆发、终止、年龄/区块、存档/注册/资源保持。

新增8项核心合同验证顺序、非穿透停止、跳过、后续接触重算、爆发对后续历史的影响、伤害/粒子异常和空列表。完整离线build退出0：63单测/428模块测试，旧15,658断言、102有效定义/教材、资源/JAR及架构边界通过。证据r06-contact-build.log与JUnit汇总/XML。测试桩只验证端口/核心组合，不是实际Minecraft/Fabric/Mixin/伤害/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS。下一精确动作审计并提取候选收集/排序协调，保持各术contact与history求值次序、strict小于墙流体、稳定同距顺序；先更新允许路径和合同再实现。共享载体扫掠/区块/Codec等仍待迁，不能称R06完成。不游戏、不Git。
