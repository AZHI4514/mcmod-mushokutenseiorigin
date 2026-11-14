# 水流飞行与击退顺序源码审计

2026-11-11。非实际Minecraft验收。

PhaseTwoEntity仍使用width3/height1/depth1与完整准星direction构建SpellSweep；位置即中心，不套stationary抬高或冰霜刃BladeSectionVolume特殊全遮挡。普通solid shape接触即阻挡，WATERLOGGED能力和水按原MagicFlight跳过；熔岩保留fluidHit。

初始化由PhaseProjectileAdapter→原configure设置Spell、方向、预算与边界，CONSTANT按rank2 flightTicks=120；保存Remaining权威，MagicAge/Remaining/AimYaw/AimPitch/MagicVictims及reader不改。区块未加载仍在年龄推进前暂停；玩家停止键不删除已发射弹体。

候选原getEntities顺序，按contact稳定排序，严格小于wall/fluid；最终接触距离<=本tick速度.5，先claim再PhaseTwoSpells.damage。damage的allowed/hurtServer失败直接返回，不附效/钩子/击退；成功才MagicEffects.apply→afterHit→knockback(1.2,-direction.x,-direction.z)。原版knockback负责水平化/抗性，不自行替代速度计算。目标一次规则包括无敌帧失败尝试。

新增核心组合合同仅组合Preparing/CastSpellCommand/PiercingFlightRules/ImpactHistory，验证120tick与60格理论预算及快照恢复，不是实体tick集成或真实存档重载。PVP/世界碰撞/击退抗性/联机仍NOT_RUN。
