# R07-earth_wall：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`earth_wall`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


纯EarthWallLayout有序5×3格布局；独立EarthWallAdapter/SelfEffect。24格逐段trace、命中面法向.02偏移、abs(z)>=abs(x)方向比较、y外x内顺序不变。原ExpansionTerrainLeases enqueue1200/true全部预检租约保留，第一计划格中心+1作burst。禁止换成每格独立成功或跳过失败格。允许相应核心/适配/兼容/装配/合同/文档。不游戏不Git。

## 土墙独立有序布局与租约适配（2026-11-13）

EarthWallLayout纯5×3有序布局接独立Adapter/SelfEffect；保留24格逐段选点、面法向.02偏移、主轴>=同距决策与y外x内顺序。原1200/true租约预检/排队和首格burst不变。4项模块合同包含10000随机方向逐格有序差分，世界权限/租约实际执行尚未游戏验收。

完整离线build退出0：63单测/658模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-earth-wall-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_fortress纯半球壳/门洞/稳定排序与独立适配，随后earth_hedgehog。
