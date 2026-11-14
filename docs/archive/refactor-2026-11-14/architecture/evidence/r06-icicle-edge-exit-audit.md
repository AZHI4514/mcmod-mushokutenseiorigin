# 冰霜刃切片退出源码审计

2026-11-11。MagicPreparation资格/跨维/心跳取消与释放前校验保留；Pending按迁移装配持Preparing与Command。实体存续独立于玩家停止键，发射后继续存在，未增设取消弹体行为。

PhaseTwoEntity保存Spell/MagicAge/Remaining/AimYaw/AimPitch和MagicVictims字段保持，原容错UUID reader和max(0,age/remaining)保持；非stationary前向未加载检查先于到期与age递增，区块未加载暂停。命中按volume.contact距离稳定排序，先记录再伤害，权限/无敌帧失败不重试；damage依旧allowed→hurtServer→附效/afterHit。

完整截面仍使用负AimYaw/同一motion与原方块shape顺序，水能力方块先跳过，再计算全截面阻挡；熔岩仍由fluidHit处理。共享几何差分与组合合同已通过，保存和生命周期这里只是源码审计。仅关闭核心/协调切片，原世界载体/渲染与实际事件、实体重载/PVP/联机仍NOT_RUN。
