# R07-flame_tornado：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`flame_tornado`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立FlameTornadoPolicy/Adapter/SelfEffect，5×18移动圆柱，pulse拉拽-.12，上升最终用原速度+.06覆盖通用+.08，玩家cap.35/生物Double.MAX_VALUE；hurt成功先火附效再hook，自身随机.8/抗火检查点燃500。移除WindFieldComposition临时LegacyPolicy，五术全部显式Strategy，未知误用抛出只限内部policy，不改变公共radius/height旧默认16。允许对应模块/装配/合同/文档。不游戏不Git。

## 火龙卷独立火焰与上升策略（2026-11-13）

FlameTornadoPolicy独立5×18移动/火脉冲策略及SelfEffect绑定，上升最终仍用原速度+.06，玩家.35上限。成功hurt后火附效再hook，自身原随机/抗火/500点燃不变。五风术策略全部显式绑定并删除临时LegacyPolicy，公共几何帮助对非风术保留旧默认16。5项模块合同通过，原300MP/7伤害/240持续不变。

完整离线build退出0：63单测/730模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-flame-tornado-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-inferno独立规则、401到期与20次余焰，复用实际相同爆发排程后absolute_zero。
