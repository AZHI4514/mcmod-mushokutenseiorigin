# R07-core-fields：STRUCTURE_SLICE_DONE

先把龙圣斗气从CoreFields分离为独立R08模块，再按城塞/魔神封印与共享供能拆分。attachment标识与Codec不改；斗气结束、费用失败和主动状态标记的不同时间序必须保留。不游戏不Git。

## 龙圣斗气独立与核心障壁供能协调（2026-11-13）

龙圣斗气独立adapter和tick规则接入；城塞FortressUpkeep保留真实takeEnergy及100tick断供宽限；魔神封印目标资格与PoweredBarrierTick已抽离。13项合同通过，核心能量和保存键不变。

完整离线build退出0：63单测/845模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-powered-barrier-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成治疗/解毒/同意与声音模块；已发现高阶阵面成功消费接线遗漏，立即修复并补合同后统一归档。
