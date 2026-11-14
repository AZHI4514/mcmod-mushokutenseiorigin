# R07-heat_hand：STRUCTURE_SLICE_DONE（审计）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`heat_hand`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


先读正式规格与HeatHand全部调用。拆canUse/execute/tick/特殊方块及目标规则，保留服务器权限、双手工具、原熔炼配方查找与费用/准备；本卡先允许只读和文档，找到真实核心边界后扩实现路径。不可把热手改为普通火焰攻击。游戏/世界/Recipe实际运行NOT_RUN，全部完成后统一实测，不Git。

源码核对：当前没有工具/熔炼配方分支，前述初始审计提示不代表真实实现。本批允许HeatHandRules/HeatHandMelt与模块合同、HeatHandAdapter/兼容入口/SelfEffectComposition。面2×2格枚举保持a外b内及轴映射；所有setBlock即便前项成功仍执行，之后先读取warmth，再按changed短路frozen；未发生变化且已温暖无冻结返回false退款。200tick温暖、4格OUTLINE权限检查不变，原实现无额外预加载射线检查，不私自新增。

## 灼热手核心规则与世界适配（2026-11-13）

HeatHandRules保存命中面2×2 a外b内轴映射及冰/蒸发转化，HeatHandMelt保存所有格非短路setBlock后读取warmth、changed短路frozen再温暖及末尾逐格粒子顺序；HeatHandAdapter保存4格OUTLINE及权限，兼容入口委托。SelfEffectComposition接独立2MP/100准备/0恢复命令。新增14项模块合同（8规则/协调、6付款/取消/部分异常），原200温暖/方块资源数值不改，无Recipe/工具分支凭空新增。世界方块权限/环境属性仍未实机验收。

完整离线build退出0：63单测/569模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-heat-hand-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-heat_island：拆半径3区域温暖、清湿身及固定锚点续期，保持200持续和现有效statusTicks；随后燃烧领域。
