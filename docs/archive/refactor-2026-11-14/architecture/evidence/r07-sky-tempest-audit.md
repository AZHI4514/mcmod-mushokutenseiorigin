
## 天空风暴中心伤害与切向冲量策略（2026-11-13）

SkyTempestPolicy独立24×40、中心半径8伤7/外4、每tick切向.08上升.04、玩家y<=.35与自身可伤规则。独立SelfEffect，pulse仅相消雨/积雨云，暴风烟/火特例不扩大。4项合同覆盖中心包端点/NaN、每tick移动与360方向切向及玩家限速。620MP/600持续和旧Wind保存键不变。

完整离线build退出0：63单测/717模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-sky-tempest-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-flash_over首帧双伤与余焰独立排程，随后flame_tornado/inferno/absolute_zero。
