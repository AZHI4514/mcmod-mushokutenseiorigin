# R07-lightning：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`lightning`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立LightningRules/Impact与LightningAdapter及显式Command装配。保留准备remaining15锁点，锁点后每3tick预警，释放前validPoint再检查，命中前execute再次validPoint；不重新按最终准星定位。220MP/32伤害/64距离/2水平半径32高不变，允许误伤owner但其他目标按allowed，天空/可见顺序不变。旧ThunderMagic point/validPoint/execute兼容转发。允许相应核心/适配/装配/准备局部/合同/文档。不游戏不Git。

## 雷光锁定落点与命中控制分离（2026-11-13）

LightningRules/Impact与独立Adapter/Command装配，保留remaining15锁点、每3tick警示与释放前及命中前二次validPoint；不重新按最终准星定位。220MP/32伤害/64距离/2水平半径32高与自身误伤特例不变。天空→非自身权限→可见→hurt→control→hook原序；纯规则7项合同覆盖短路和边界。实际Fabric伤害及锁点预警视觉未运行。

完整离线build退出0：63单测/648模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-lightning-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-electric抽单目标选择/免疫分支/命中协调，随后earth_wall与earth_fortress。
