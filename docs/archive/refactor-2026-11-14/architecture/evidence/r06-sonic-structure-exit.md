# 爆音冲击波结构退出核对（2026-11-13）

结论：仅核心/准备/释放/世界适配结构SLICE_DONE，不是全项目重构或游戏验收完成。

## intent与数值

ALL_MAGIC_EXPANSION.md爆音冲击波及SpellDefinition.SONIC_BLAST保持40MP、12伤害、16格/35度锥体、1.2冲量；有效FULL rank3为240准备/20恢复。永久模式最多128次成功破坏石制工具可采方块，仍由ExpansionTerrain处理原权限与掉落。未改定义、资源、注册、网络或保存ID，102有效定义及教材基线通过。

## 调用和生命周期

SelfEffectComposition显式绑定SonicBlastAdapter，MagicPreparation的Pending持Preparing/Idle上下文与CastSelfEffectCommand，selfCommand执行后立即return，绕过旧泛型付款/执行分支。旧SpellExecutors→SonicBlast.cast仅委托同一Adapter，不单独付款；Adapter直接用SonicBlastRules，不反向调用兼容门面。R10须在证明旧入口零引用后再删除兼容类。

释放前保留MagicPreparation的资格、心跳、维度及cancel检查；释放同步完成，无独立tick、任务容器或保存状态。MagicAreaEntity仍configure/setPos但不addFreshEntity，只作真实伤害来源。未改共享MagicPreparation全局容器，它仍属后续架构迁移职责。

世界层仍以眼部为起点、query排除owner、原MagicEffects.allowed/PVP、目标包围盒中心锥形判定及IntermediateSpells.visible过滤。SonicBlastHit按hurt成功→MagicControl.immune→非免疫冲量→afterHit。offset平方<.001时用原准星方向乘1.2，否则offset.normalize乘1.2，标hurtMarked。免疫不跳过afterHit；伤害false不读免疫或冲量；异常向上传播，不回滚已发生伤害/运动。

永久地形先收集已加载锥形方块，保留BlockPos枚举和原稳定距离排序，无新增同距排序。SonicBlastTerrain先对完整候选作射线可见快照，之后才break；false继续，只有成功计数达到128才停止，不能复用风枪龙卷的首失败停止算法。原射线COLLIDER/NONE和破坏权限API保留。目标→地形→burst顺序、空目标也返回true均保持。

## 验证与限制

新增18项模块合同：命中6、地形6、付款/真实核心组合6。覆盖免疫、伤害拒绝、严格近零阈值、异常截断、先快照/后破坏、128成功上限/失败继续、准备240不扣费、40付款前置/20恢复、单次提交、取消、输入/知识/MP拒绝、显式false恢复、未知部分地形异常保留付款及观察者隔离。另有已通过的3项锥形规则合同和10,000差分。

初次构建因测试匿名类继承events字段遮蔽外部记录变量，导致事件记录断言失败；修正测试局部名为waveEvents，未改生产逻辑。原失败日志r06-sonic-initial-test-failure.log；最终r06-sonic-exit-build.log退出0，63单测/370模块测试，旧15,658断言、102定义/教材、资源/JAR、纯核心classpath及服务端/客户端边界通过。

合同使用真实核心协调器和窄端口测试桩；不执行真实Minecraft世界、Fabric/Mixin、伤害事件、PVP、权限/掉落、Codec重载、视觉、联机或性能，上述均NOT_RUN。付款合同内的目标/地形/burst顺序是端口组合合同，真实Adapter顺序另经源码核对，不冒充世界集成。下一账本项R06-blast。
