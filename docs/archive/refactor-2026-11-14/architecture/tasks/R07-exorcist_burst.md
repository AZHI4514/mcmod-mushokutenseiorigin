# R07-exorcist_burst：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`exorcist_burst`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立ExorcistBurstPolicy/Adapter/SelfEffect；终点沿轴-.02原点，4半径球，标签候选→形状→可见→hurt，无afterHit，不画beam而burst spread1.5。hurt拒绝不影响成功。抽HolyAreaHit懒形状/可见/伤害纯协调供范围圣术。允许模块/适配/装配/合同/文档。不游戏不Git。

## 驱魔爆破独立终点球形命中（2026-11-13）

ExorcistBurstPolicy与HolyAreaHit接入终点4半径球、标签资格、形状、可见、hurt顺序；不画beam且hurt拒绝仍成功。3项模块合同覆盖懒读取和异常。

完整离线build退出0：63单测/750模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-burst-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R07-exorcist_judge与holy_ray逐术策略。
