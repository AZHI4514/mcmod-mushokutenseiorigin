# R07-heat_island：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`heat_island`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


正式地热：6MP/100准备/0恢复，固定半径3、200tick领域，原地续期累加；清湿身再warm(statusTicks)，温暖实际有效值随原定义，不反改规格。保留实体owner/剩余时间/加载失效顺序与无PVP过滤的非攻击温暖。

允许HeatIslandRules/HeatIslandContact、HeatIslandAdapter、MagicAreaEntity该术分支、SelfEffectComposition与模块测试/文档。几何按目标getX/Z而非包围盒中心、<=9含边界。共享Area生命周期随后独立任务，不把一次温暖委托称全部实体迁完。游戏未跑，不Git。

## 地热区域规则与独立释放（2026-11-13）

HeatIslandRules保持目标getX/Z到领域中心的半径3含边界判断，10000随机冻结算式差分通过；HeatIslandAdapter先removeWet后MagicEffects.warm，非攻击不新增PVP过滤。6MP/100准备/0恢复和独立SelfEffect命令已接，200领域及原statusTicks保持。新增10项模块合同（规则/顺序4，准备/付款6）。共享Area实体owner/剩余/加载和原地续期职责仍后续公共生命周期任务，不把领域世界行为称已实测。

完整离线build退出0：63单测/579模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-heat-island-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-burning_place：抽每目标接触/权限/可见/claim/伤害/点燃事件顺序，保留原全身接触体和20tick尝试冷却。
