# 火柱已验证切片退出与生命周期审计

## Intent / spec / observed
正式第30术为选中生物3次3伤害或方块放火，不是AOE柱体。生物分支配置未注册入世界的原PhaseTwoEntity伤害carrier；PhaseTwoSpells.damage仍先allowed/hurtServer成功后MagicEffects.apply/SpellHooks.afterHit。首次ignite60不以伤害成功为条件，成功火伤才沿原附效80%刷新200。核心未重新定义伤害/火抗/无敌帧。

## 入口与失败
MagicPreparation保留完成前SpellExecutors.canExecute只读预检；新LocalEffectAdapter于同一调用locate缓存trace目标，方块分支再次aim，place先消耗计划。Command先资格/定位再扣12/恢复10；显式false回到完整旧PlayerMagicState，部分世界异常不退款，重复Command拒绝。世界Adapter先消费计划，即使异常也不能直接重放。旧execute复用同Adapter。

方块分支没有新增伤害carrier/burst；已有普通火/灵魂火受权限检查后直接成功。尝试命中面再顶部，不去重两者相同的情况；只填空气，BaseFireBlock.getState/canSurvive/setBlockAndUpdate原样。目标生物脚下placeFire保持独立原语义，不误套方块候选回退规则。

## 任务与清理
start仍在首次burst之后取owner.level().getGameTime。每次施法append独立Burn，绝对世界时间20/40尝试，60结束；不补跳过时点，不新增目标卸载检查或暂停语义。同tick重复调用依旧可重复尝试，原版无敌帧决定实际伤害，不借重构修正历史行为。

burn_runtime是不持久化/不同步服务器主世界附件；静态仅注册类型。tickBurns在通道tick之前执行，松键stop/切换选择不撤回已提交任务；clear(player)仅删本服该玩家未结束任务，不灭已放火。LEAVE与跨维调用clear；AFTER_RESPAWN清旧玩家；SERVER_STOPPING逐玩家clear再移除附件。施法者死亡到重生间并没有新增取消条件（旧tickBurns同样只看目标生死/维度和期限），保留旧行为。目标死亡或不在owner当前level立即删任务。

## 验证和边界
63单测/154模块测试、完整offline build退出0。新增17模块合同含真实核心协调组合；不是Minecraft对象、Fabric事件、块更新、PVP/火伤/原版火蔓延或实际多服务器/存档运行验收。102有效定义、39教材白名单、102直学、原模型纹理/资源哈希与旧存档fixture通过；未改同步/网络/存档ID。

仅核心/释放/任务归属切片SLICE_DONE；共享世界trace、伤害/附效、客户端UI/视觉和遗留实体职责仍按后续阶段继续。GameTest/client/multiplayer/performance NOT_RUN。
