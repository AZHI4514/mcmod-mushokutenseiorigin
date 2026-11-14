# 冰霜击飞行/含水/命中保存源码审计

2026-11-11；源码与核心合同，不是实际游戏验收。

- configure按CONSTANT选择原flightTicks（rank2=120），保存Remaining读回后继续作为age>=remaining判定预算，不重置120。下界/当前区块和前向区块未加载检查仍早于age递增；有限预算只在实际模拟tick推进。
- fly先枚举固体shape并跳过MagicFlight.passesWaterBlock；该方法仍读取WATERLOGGED属性存在性而非属性值，水/流水fluid tag，并委托既有WaterPassage。MagicRules.passesWater读取spell.piercing；不是“浸在水中的任意方块”全部通过。
- fluidHit忽略可穿透水，保留熔岩/其他非空流体体积碰撞；不加载新区块。solid或fluid在步内先于继续移动终止，原视觉和伤害执行顺序不改。
- 候选contact严格小于min(wall,fluid)，按接触距离稳定排序；最终contact<=distance且未命中过才claim。claim先于hurtServer尝试，免疫/无敌帧失败也不会下tick重试；没有增设群伤。
- PhaseTwoEntity原HashSet改复用ImpactHistory，因同一载体共享命中集合，其他PhaseTwo术也使用等价contains/claim。MagicVictims字符串列表键和MagicFlight.UUID解析保持；保存用不可变快照，读回用原容错reader再构建独立集合。没有第二份可写命中集合。
- 已确认原版damage→allowed/hurtServer→附效/afterHit路径未改。PVP、保护、真实实体保存/加载、流体碰撞与联机仍NOT_RUN。
