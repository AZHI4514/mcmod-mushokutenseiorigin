# R07-absolute_zero：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`absolute_zero`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立AbsoluteZeroRules/Contact与Adapter/SelfEffect；160持续/半径16球，首次28+湿身6，伤害成功再移湿；warm快照在wet之前，warm则只移暖否则canFreeze→冻结600再control玩家40/生物60→hook。后续20tick冷雾无伤，不warm且canFreeze才随机.8；初次伤害失败不走冷雾。冻结地表16/4096/1200→tick→burst，Zero旧保存默认released true不变。复用BurstFieldSchedule真实同排程但独立效果协调。允许上述模块/兼容/装配/合同/文档。不游戏不Git。

## 绝对零度首次冻结与冷雾顺序分离（2026-11-13）

AbsoluteZeroRules/Contact与Adapter/SelfEffect接入，首次28+湿6，warm先wet快照；hurt成功才移湿，warm只移暖，否则canFreeze后冻结并控制。后续冷雾无伤，仅无warm/可冻才随机.8；初次拒伤不落入冷雾。160/半径16及freezeSurface16/4096/1200和旧Zero保存默认不变。7合同覆盖资格、快照、控制和异常。

完整离线build退出0：63单测/742模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-zero-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-holy-runtime共享几何/世界适配，随后exorcist_ray/exorcist_burst/exorcist_judge/holy_ray逐术。
