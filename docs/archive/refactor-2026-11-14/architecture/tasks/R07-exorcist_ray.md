# R07-exorcist_ray：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`exorcist_ray`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立ExorcistRayPolicy/Adapter/SelfEffect；单体最近严格<，未选中false，目标hurt失败仍成功与visual，无新增hook/PVP改变。共享HolyInstantPolicy只持plan变化/形状/visual差异，四术后续逐卡独立策略；临时LegacyPolicy待清理。允许策略/适配/装配/合同/文档，原数值/视觉不变。不游戏不Git。

## 驱魔射线独立最近单体策略（2026-11-13）

ExorcistRayPolicy独立单体/窄光束策略和SelfEffect准备绑定，严格最近<同距首个，资格/墙/卸载原序、hurt失败仍成功与原visual，无新增hook。2项合同覆盖最近比较及窄光束参数，四术策略剩余临时分支继续逐卡清理。

完整离线build退出0：63单测/747模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-ray-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-exorcist_burst终点球形/可见伤害协调，随后exorcist_judge/holy_ray。
