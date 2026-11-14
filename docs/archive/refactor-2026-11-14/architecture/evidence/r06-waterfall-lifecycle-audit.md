# 水瀑生命周期与存档兼容源码审计

2026-11-11，源码审计与核心模块合同，不是Minecraft集成测试。

- MagicSystem.LEAVE→PhaseTwoRuntime.clear→stop；AFTER_RESPAWN先clear(oldPlayer)，再保留原refill逻辑。
- START_SERVER_TICK跨维先clear并阻断输入；死亡/使用物品及维度、菜单、已学/装槽/选择、SpellHooks由available拒绝，WaterfallChannel.tick在付款/世界调用前停止。
- release/stop/select/toggle入口均stop。SERVER_STOPPING逐玩家clear后clearChannels(server)，通道附件不持久化/不同步，按当前服务器主世界归属。
- CHANNEL_DEBT继续使用channel_debt、Codec.intRange(-19,0)、copyOnDeath；stop不删除债务、不把预付小数退款。
- MagicWaterSources保持SavedData ID magic_water_sources与pos/expiry Codec；新水永久Long.MAX_VALUE；旧有限expiry在已加载区块到期处理，未加载延迟清理；remove逻辑未改。水源放置权限/空气/蒸发条件保持。
- 新增两个真实WaterfallChannel模块合同：双实例心跳/停止/费用/续水互不影响，停止后新建用例延续原信用，旧用例不再续水。

源码和端口合同无法证明真实事件分发、附件重载、跨维时序、水流/湿身或联机；这些仍NOT_RUN。仅关闭水瀑已迁核心/协调切片，旧世界SavedData服务与客户端视觉后续仍待对应阶段审查。
