
## 逐术飞行策略、纯目录完整合同与机制类型清理（2026-11-13）

SweptProjectilePolicy明确绑定六种扫掠术的寿命/体积/接触/卸载/地形/爆发/碰块能力，CarrierFlightPolicy维护严格接触/截面/爆炸/风切特例；共享扫掠和飞行适配不再持有对应术ID长条件。ExpansionFields/ExpansionBarriers无职责转发已删除。新增纯JDK102术逐字段冻结目录合同及Builder不可变/独立/非法输入合同、飞行边界测试，63/930通过。既有职责拆分后将17个PhaseTwo/Intermediate类型改为SweptSpellProjectile/DirectionalSpellCarrier等真实机制名，完整清单r10-mechanism-class-names.json；原phase_two_spell实体ID、PHASE_TWO兼容枚举、所有附件/存档/网络ID保留。完整离线build通过；仍未完成整体重构，不启动游戏、不Git。

完整离线build退出0：63单测/930模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-flight-mechanism-catalog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续Reference模型资产加载/渲染分离与视觉目录，拆地形租约纯计划/预算和服务端MagicSystem输入网络职责；最后逐术账本、模式审查及R00—R10完成门禁审计。
