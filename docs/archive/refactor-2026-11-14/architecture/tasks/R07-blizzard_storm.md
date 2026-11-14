# R07-blizzard_storm：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`blizzard_storm`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立BlizzardStormTick/Rules与Adapter/SelfEffect装配；保持80tick、10tick节拍、半径8、2伤害、36MP及官方准备恢复。启动cast时立即tick、跟随owner后adopt、adopt拒绝不递增pulse；完整命中后才pulse++。保留water共享cadence claim在hurt前及effects/hook顺序。ExpansionSpell/Pulse/Expiry旧键、默认pulse80与绝对到期数学不变；客户端跟随保留。允许相应核心/实体局部/适配/装配/测试/文档。不启动游戏不Git。

## 吹雪独立节拍与旧到期保存合同（2026-11-13）

BlizzardStormTick/Rules与世界Adapter接入独立SelfEffect和准备；释放首帧立即tick、跟随后adopt，拒绝和异常不增pulse，80tick内8次伤害脉冲。保留water cadence claim→hurt→附效→hook及原可见射线，36MP/2伤害不变。旧ExpansionSpell/Pulse/Expiry键和默认80/0、绝对时间恢复不变。8项模块合同覆盖短路、节拍、异常和整数/long边界。

完整离线build退出0：63单测/625模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-blizzard-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-squall局部降雨生命周期/暴露/时长规则，随后cumulonimbus。
