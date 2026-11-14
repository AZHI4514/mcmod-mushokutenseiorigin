# 爆炸术真实入口审计

- 准备：ProjectileComposition显式EXPLOSION绑定，被MagicPreparation既有handles接入Preparing/Idle；PhaseProjectileAdapter调用PhaseTwoSpells.entity→configure，保留Spell/方向/remaining/bounds初始化。旧PhaseTwoSpells.execute直接入口保留兼容，不额外收费。
- 飞行：PhaseTwoEntity先检查当前/前方区块加载（不加载暂停），到期discard不爆；++age后fly。预算与候选规则复用PiercingFlightRules，等价替换原表达式。120预算仍取实际SpellDefinition.flightTicks，未写死覆盖存档Remaining。
- 候选：LivingEntity且MagicEffects.allowed，未命中过且contact严格早于min(wall,fluid)；按contact稳定排序，contact<=distance再claim。每个新目标setPos接触点→视觉burst→ServerLevel.explode(TNT_POWER4,true,TNT)，piercing继续；无直接damage10调用。
- 流体：fluid<=distance且fluid<=wall直接discard不爆；MagicFlight.passesWaterBlock保留水、水流、干湿WATERLOGGED能力通过，熔岩终止的旧规则。
- 固体：wall<=distance且block非null，接触点退0.02→burst→原版爆炸→discard；不以自造AOE替换原版事件/伤害/归属/掉落。
- 保存：Spell/MagicAge/Remaining/AimYaw/AimPitch/MagicVictims及reader原样，ImpactHistory快照重建；age在重载后不重新归零。traveled不参与本术预算。
- 待迁：爆炸触发协调仍在实体内，窄世界端口和分支组合合同待下一动作。游戏实际PVP/遮挡/爆炸事件/存档重载/视觉/联机/性能NOT_RUN。本审计为源码审计，不冒充世界集成测试。
