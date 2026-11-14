
## 冰霜领域独立选点与延迟冰封（2026-11-13）

IcicleFieldRules/Adapter接入TargetedArea Command及Preparing State；保持14MP/5伤害/12选点/6半径和旧paidMana，未生成飞行实体。复用DeferredAreaImpact，湿身先快照，hurt成功才附效及移湿并hook，全部命中完成再冰封，最后burst。新增6项边界/异常合同及10000几何差分。首次测试错误使用禁止NaN的Vec3d已改成标量比较合同，失败日志保留。

完整离线build退出0：63单测/609模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-icicle-field-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-area-lifecycle拆短期领域服务端时序，随后blizzard_storm。
