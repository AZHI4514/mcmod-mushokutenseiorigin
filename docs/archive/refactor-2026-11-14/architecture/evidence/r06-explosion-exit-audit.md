# 爆炸术已验证切片退出审计

1. 规格和准备/付费释放：ExplosionSpec、显式ProjectileComposition、PhaseProjectileAdapter，既有Command/Factory/Preparing/Observer真实接入，单次收费16/恢复10；4原合同+1组合合同通过。
2. 飞行：真实实体复用PiercingFlightRules；排序、先claim、载体位置设置和保存reader原样。到期discard不爆；接触不同目标可重复TNT，命中历史重建排除旧目标。合同测试核心重建，不证明Codec运行。
3. 触发：ExplosionImpact与原版Adapter真实接入；视觉在原版TNT之前，非穿透discard在后；异常沿旧调用栈传播，未引入捕获/回滚。流体优先于等距固体，固体接触退0.02，原版事件/掉落/火/伤害归属保留。
4. 证据：build退出0、63单测/137模块测试、102有效参数/教材一致、15,658旧行为断言、资源/JAR检查。没有改视觉资源/注册/网络/保存ID。
5. 剩余：共享世界候选/扫掠适配和实体保存服务将在后续共享职责清理；真实区块暂停/重载/TNT事件/PVP/权限/视觉/联机/性能NOT_RUN。只标核心/协调SLICE_DONE，不将所有102术或整个R06标DONE。
