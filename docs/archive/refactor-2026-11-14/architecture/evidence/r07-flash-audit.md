
## 闪燃独立首帧双伤与余焰状态（2026-11-13）

FlashOverRules/State/Contact与Adapter/SelfEffect接入，首帧16爆发和3余焰均执行，余焰八次，flash不claim、pulse先claim火20。flashed和next在世界命中前写，原owner随机/抗火/400点燃和保存键默认不变。共享ExpiringFieldTick仅state/expiry优先的相同生命周期族；8合同覆盖短路、首帧双伤、默认旧状态与异常。

完整离线build退出0：63单测/725模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-flash-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-flame_tornado独立火焰/上升策略并移除临时LegacyPolicy，随后inferno。
