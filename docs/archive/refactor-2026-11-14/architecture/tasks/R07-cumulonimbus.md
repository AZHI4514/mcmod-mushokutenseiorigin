# R07-cumulonimbus：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`cumulonimbus`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立CumulonimbusTick/Rules/State和世界Adapter；保留1200时长、40落雷间隔、80受击冷却、24水平/48高、最多16随机空地候选。先状态到期再owner alive/level/adopt（原不查removed，禁止补行为），暴露目标清火湿身400，先更新next并prune再选敌人/随机点。受害名单先于cadence与hurt写入；旧StormExpiry/Next/Victims默认值不变。允许对应核心/适配/兼容/装配/合同/文档。不启动游戏不Git。

## 积雨云独立排程与受害冷却状态（2026-11-13）

CumulonimbusTick/Rules/State与世界Adapter、SelfEffect准备接入。保留旧短路和未检查removed的原边界，40tick落雷间隔先写next再清victims，选点失败仍消耗本次落雷；80tick受害尝试先claim再cadence/hurt。1200时长/24半径/48高/16随机候选、天空/伤害/视觉与旧保存键默认不变。9项模块合同覆盖排程、短路、状态实例隔离和异常。

完整离线build退出0：63单测/641模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-storm-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-lightning保持remaining15锁点和重复validPoint，抽命中控制与独立付款，随后electric。
