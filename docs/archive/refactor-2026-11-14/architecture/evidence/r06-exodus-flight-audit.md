# 狱炎火弹飞行与历史源码审计

服务器tick在super/isRemoved后检查minY、age>=flightTicks或motionSquared<1E-8；未到期才查询扫掠区块。任一扫掠区块未加载则discard且不++age，不改为暂停。有效tick在碰撞查询前++age。候选接触必须严格小于min(wall,fluid)，按contact稳定排序；最后实际处理允许contact==distance，且每个候选再次读hit集合，防止前一次爆发已波及该目标后重复触发。

生物接触时先移动到接触位置，再exodusBurst；piercing继续并播放impactParticles。爆发中心再沿运动反向偏移0.025。流体<=distance且<=wall优先播放impactParticles后discard，无爆发；水及支持waterlogged的不完整方块仍复用MagicFlight穿透规则。墙<=distance且block非空时移到接触位置、爆发、impactParticles、discard，不生成TNT。无接触最终setPos(to)，traveled仅累计不作距离超时。

爆发包含owner查询，非owner权限过滤，先claim再伤害；同一HashSet供整发直接候选和所有球爆发共享。MagicVictims保存UUID字符串列表；loadHits先clear，逐个解析忽略非法UUID，HashSet自然去重。MagicAge读取非负，PaidMana/ExpansionBroken保持旧键；未迁共享Set/Codec，不声称已做真实存档重载。寿命、未加载、墙流体及历史读写保持原路径，真实Minecraft世界/Codec集成NOT_RUN。
