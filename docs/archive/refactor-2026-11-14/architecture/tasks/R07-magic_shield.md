# R07-magic_shield：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`magic_shield`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


审计MagicAreas.ALLOW_DAMAGE：先directEntity是Projectile且isMagic，才取target UUID领域，存在/未移除/同level才absorb并拒绝原伤害。只挡原magic实体直伤，不扩大环境/近战。容量8，8MP/100准备/0恢复/100持续，跟随owner，续期容量重置。

允许MagicShieldDamageGate/Rules及合同、MagicShieldAdapter/事件适配、AreaRuntimeAdapter事件委托、Area实体吸收与SelfEffectComposition绑定。共用ShieldAbsorption实际用于水/物理/魔法三盾，原子动作和show异常语义不变；不共用盾的几何/拦截资格。现有注册回调位置不变，非每术新建事件监听。游戏/Fabric伤害事件仍NOT_RUN，不Git。

## 魔法盾直伤边界与共享容量吸收（2026-11-13）

魔法盾独立SelfEffect/准备付款，原伤害事件位置委托懒查询适配。仅已知魔法Projectile直接伤害进入盾检查；有同level有效盾则吸收并拒绝伤害。三盾共享容量扣除→显示→重新读取容量→移除协调，保留异常副作用和8点容量，不共用几何资格。8MP/100准备/0恢复/100持续不变。

完整离线build退出0：63单测/603模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-magic-shield-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R07-icicle_field独立领域选点、湿身快照和延迟冰封；随后共享领域生命周期。
