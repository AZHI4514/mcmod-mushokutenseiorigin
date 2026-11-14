# R07-inferno：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`inferno`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立InfernoRules/Adapter/SelfEffect；首帧18、从20至400共20次余焰5，401到期、半径20高8，scorch20/2048/401→tick→burst。与闪燃相同的一次爆发/后续排程和懒接触次序提到共享BurstFieldSchedule/BurstFieldContact，单术Spec只持差异不套通用效果解释器。旧InfernoStart/Expiry/Next/Infernoed默认true及自伤点燃600不变。允许模块/兼容/装配/合同/文档。不游戏不Git。

## 炼狱独立规则与真实共享爆发排程（2026-11-13）

InfernoRules/Adapter/SelfEffect接入，首帧18，20..400二十次5伤、401结束，半径20高8和scorch20/2048/401保留。提炼实际相同BurstFieldSchedule/Contact共享时序，闪燃保留兼容核心API并重跑合同。旧Inferno保存键默认及owner随机/抗火/600点燃不变；5新增合同验证独立规则、时序和跳帧不补发。

完整离线build退出0：63单测/735模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-inferno-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-absolute_zero湿身增伤/暖身快照/首次冻结控制与后续冷雾，随后holy逐术。
