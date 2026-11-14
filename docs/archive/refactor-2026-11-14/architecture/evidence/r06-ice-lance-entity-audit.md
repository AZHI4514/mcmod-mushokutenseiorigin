# 冰枪首击和实体兼容审计（2026-11-11）

- PhaseTwoEntity.tick先做下界/区块/owner检查，再age>=remaining移除，之后++age；冰枪仅age==1进入目标查询。卸载/无owner时年龄暂停的历史行为未改。
- 首击使用水平AimYaw方向与中心position+(0,1.5,0)，定向1×3×1。旧SpellSweep本来即委托SweptVolume，迁移继续用同一算法及游戏生成的方向，不改三角函数精度。
- IceLanceHitAdapter按原getEntities顺序筛LivingEntity；先精确体积，再从position+(0,.1,0)做IntermediateSpells.visible，再PhaseTwoSpells.damage。没有提前过滤owner/PVP以改变可见性调用顺序。
- damage保留MagicEffects.allowed→hurtServer成功→附效与afterHit。冻结随机样本的读取位置不变；不增加首击UUID集合，年龄仍是唯一触发时钟。
- addAdditionalSaveData/readAdditionalSaveData保持Spell/MagicAge/Remaining/AimYaw/AimPitch及MagicFlight命中UUID集合格式；读取年龄/时长仍max(0,value)，未知/非PHASE_TWO术仍wind_slice回退。实体注册与资源ID未改。
- Legacy lifetime12字段、客户端age/视觉行为未改；真实保存加载、权限/PVP、遮挡、客户端同步与性能均NOT_RUN。

新增3项真实IceLanceHit模块合同：几何→可见→伤害顺序及被遮挡过滤，非首击/重载后年龄零世界查询，世界伤害异常仍在当前候选停止。此为端口测试和源码审计，不是Minecraft集成验收。
