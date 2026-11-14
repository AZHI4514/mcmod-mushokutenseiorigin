# R07-flash_over：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`flash_over`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立FlashOverRules/State/Contact与Adapter/SelfEffect；首帧flash16与pulse3均执行（非二选一），flash不claim，pulse claim fire20。160寿命，next=now+20，flashed先写再世界查询，旧FlashStart/Expiry/Next/Flashed默认true不变。半径16高6，owner自伤80%点燃400/抗火原采样顺序；其他原附效。允许对应模块/兼容/装配/合同/文档。不游戏不Git。

## 闪燃独立首帧双伤与余焰状态（2026-11-13）

FlashOverRules/State/Contact与Adapter/SelfEffect接入，首帧16爆发和3余焰均执行，余焰八次，flash不claim、pulse先claim火20。flashed和next在世界命中前写，原owner随机/抗火/400点燃和保存键默认不变。共享ExpiringFieldTick仅state/expiry优先的相同生命周期族；8合同覆盖短路、首帧双伤、默认旧状态与异常。

完整离线build退出0：63单测/725模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-flash-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-flame_tornado独立火焰/上升策略并移除临时LegacyPolicy，随后inferno。
