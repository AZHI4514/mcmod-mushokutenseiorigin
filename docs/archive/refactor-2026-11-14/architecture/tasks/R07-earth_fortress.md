# R07-earth_fortress：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`earth_fortress`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


纯有序半球壳布局：y0..4/x,z-4..4，平方半径9..16，两格高门洞按视线主轴方向，稳定按y/距base平方排序，保留同距原x/z顺序。适配BlockPos与最多200格计划，原enqueue2400/false租约及burst不变。独立SelfEffect付款准备。允许核心/适配/兼容/装配/合同/文档。不游戏不Git。

## 土堡纯半球壳与门洞有序布局（2026-11-13）

EarthFortressLayout接独立Adapter/SelfEffect，保留y/x/z遍历、9..16平方半径、两格高方向门洞、稳定按层/平方距排序，最多200计划与原2400/false租约。4项模块合同含1000随机方向及9轴向完整有序差分、门洞/空心/不可变性。实际世界租约权限和恢复仍NOT_RUN。

完整离线build退出0：63单测/662模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-earth-fortress-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_hedgehog纯布局/processed状态/命中计数与世界tick分离，随后quagmire。
