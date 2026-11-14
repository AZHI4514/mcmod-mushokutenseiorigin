
## 三种模式术独立规则与持续费用协调（2026-11-13）

ModeSelection及三术规则、MaintainedFieldTick、NuclearExplosionTick、AtmosphereContact、GravityContact均接实际路径。保留核爆额外支付、先schedule后upkeep、费用失败只discard、核爆先标记后targets/terrain及原命中副作用。18项合同通过；world状态/模式attachment隔离与实际费用模块接线仍待后续审计。

完整离线build退出0：63单测/819模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-mode-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：迁移障壁几何与独立模块；随后R08辅助功能及R09/R10收尾。
