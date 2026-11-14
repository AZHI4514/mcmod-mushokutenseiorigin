# R07-holy-runtime：STRUCTURE_SLICE_DONE

圣系逐术依赖：抽HolyGeometry纯柱体/半径与HolyEligibility资格规则；HolyInstantAdapter持旧plan/instant/visual，HolyMagic只兼容入口。TARGETS/DAMAGE不改注册身份，确保原包内调用仍可访问；SacredNova/DivineJudge公开原静态服务仅供跨包适配，不暴露字段。保持单体hurt结果不影响cast成功，无afterHit新增；原几何/可见/视觉。后续四个即时术各卡接独立Strategy及Command，不批次标完成。不游戏不Git。

## 圣系共享几何与资格适配（2026-11-13）

HolyGeometry与HolyEligibility抽纯柱体/懒资格，HolyInstantAdapter持原plan/instant/visual，HolyMagic兼容API保留TARGETS/DAMAGE身份。SacredNova/DivineJudge仅公开原无状态服务以便跨包调用，无字段setter。3项合同含10000方向投影差分、边界及玩家/死亡/owner/tag读取短路；四即时术尚待逐术策略，不标整体迁移完成。

完整离线build退出0：63单测/745模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-exorcist_ray独立单体策略，随后exorcist_burst/exorcist_judge/holy_ray。
