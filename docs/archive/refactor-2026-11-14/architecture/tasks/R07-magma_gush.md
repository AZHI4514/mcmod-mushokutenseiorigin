# R07-magma_gush：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`magma_gush`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


抽MagmaGushRules/State/Tick/Hit与Adapter/SelfEffect；age0伤10，20..160每20伤2共8次，>160结束。processed先写再damage判断；火系cadence先claim再hurt；owner随机.8先采样再fireImmune/resistance，非owner原MagicEffects。保留scorch→tick→burst释放顺序、MagmaStart/Processed默认、SPIKES_STARTED附件。允许上述模块/兼容/装配/合同/文档。不游戏不Git。

## 熔岩喷涌首帧与余焰排程分离（2026-11-13）

MagmaGushRules/State/Tick/Hit与Adapter/SelfEffect接入。首帧10伤、20..160八次2伤、>160到期，processed在damage判断前写。保留火cadence先claim再hurt，owner随机采样/抗火检查/300点燃和其他目标附效分支、scorch→tick→burst、旧保存默认及附件。9项合同覆盖排程、几何、命中原序及异常。

完整离线build退出0：63单测/687模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-magma-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：先R07-wind-runtime共享依赖，再按violent_storm/tornado/vacuum_storm/sky_tempest逐术策略和合同，flame_tornado按账本位置。
