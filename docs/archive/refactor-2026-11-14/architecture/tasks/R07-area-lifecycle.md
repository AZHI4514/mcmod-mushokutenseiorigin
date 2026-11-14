# R07-area-lifecycle：STRUCTURE_SLICE_DONE

共享短期领域生命周期，独立于扩展领域各自tick。抽纯AreaTick协调和AreaTickAdapter；实体仍拥有remaining/pulse/anchor/contact容器，窄State回调不暴露公共字段setter。严格owner类型→removed→alive→同level→前置减remaining→chunk顺序；固定领域位移>=1E-6打断维护，非固定跟随，递增pulse、删过期接触、遍历。客户端粒子不改。普通领域不变成持久保存。允许对应核心/适配/实体局部/测试/文档，不Git不启动游戏。

## 短期领域生命周期与世界接触分离（2026-11-13）

AreaTick抽纯短路生命周期，AreaTickAdapter拥有查询和接触副作用，实体保留私有计数/anchor/接触容器并提供窄State回调。无owner/removed/dead/跨维不减寿命，寿命先减再查chunk；到期不查chunk。位移阈值及NaN比较、follow→pulse→prune→contacts均原序。8项合同覆盖短路/边界/异常；扩展领域仍各自tick和存档，不误套短期规则。

完整离线build退出0：63单测/617模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-area-lifecycle-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-blizzard_storm独立节拍、跟随/adopt和旧保存到期规则，随后squall。
