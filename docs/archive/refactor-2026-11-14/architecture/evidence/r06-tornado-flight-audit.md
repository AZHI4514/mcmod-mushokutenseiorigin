# 风枪龙卷飞行/历史源码审计（2026-11-13）

super.tick及isRemoved后，服务器先检查minY/age>=flightTicks/运动平方<1E-8。体积1.5x1.5x3扫掠，任一扫掠区块未加载即discard并return，在++age之前；有效tick先++age再世界碰撞。原水穿透经MagicFlight保留。

永久模式先处理路径方块，成功破坏最多64个后取首个障碍；这发生在fluidHit和目标伤害之前，未为流体提前限制破坏路径。不能擅自改变该先后关系。目标候选先历史过滤，接触严格早于墙/流体，排序后再拒绝contact>distance；先hit.add，再setPos/hurt；伤害拒绝也占整发历史。piercing播放命中特效继续。

fluid<=distance且<=wall时先setPos、impactParticles、discard，流体同距优先；墙<=distance且block非空时setPos、impactParticles、discard，不生成TNT也不进水炮/大火球blockImpact。无碰撞setPos(to)并累计traveled，traveled不限制寿命。

MagicAge非负恢复，ExpansionBroken原clamp(0,64)，PaidMana原规则，MagicVictims读取先清空后解析合法UUID，自然去重。新增合同仅重建核心Set与计数，并非真实Codec或世界保存重载。共享载体/Mixin、权限/掉落、游戏/视觉/联机/性能NOT_RUN。
