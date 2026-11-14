# 冰霜新星结构退出核对（2026-11-13）

仅结构SLICE_DONE。FrostNovaRules/Hit接真实FrostNovaAdapter；SelfEffectComposition绑定现有Preparing/Command，40MP/240准备/20恢复。MagicPreparation有命令时execute后return，旧SpellExecutors→FrostNova.cast仅兼容委托同一Adapter，不双跑。后续R10在全部调用切到明确装配后可证明兼容类零引用再删除。

世界中心owner.position+Y1，查询排除owner且使用原MagicEffects.allowed，目标中心半径<=8与原可见性后读取wet/warm快照。MagicAreaEntity配置但未addFreshEntity，只作来源；遍历结束burst，空目标也true，无新tick/容器/保存键。死亡/跨维/GUI等释放前由共享准备资格取消；释放同步完成，不保留任务，无新保存读写。

新增核心组合合同把CastSelfEffectCommand与FrostNovaHit连用，确认伤害拒绝仍收费不消费状态、部分状态消费异常保留付款且不能重放；不执行Minecraft世界查询/控制免疫或真实伤害事件。63单测/349模块测试、完整build及102定义/资源/JAR通过，日志r06-frost-final-build.log。游戏/Fabric/Mixin/控制免疫/原版事件/视觉/联机/性能NOT_RUN，非全游戏验收。
