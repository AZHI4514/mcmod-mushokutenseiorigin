# R07-water_shield：STRUCTURE_SLICE_DONE（审计）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`water_shield`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


按唯一账本R07首项；不是修改既有特效。读取魔法-游戏设定、有效定义基线及MagicAreas/MagicAreaEntity/MagicRules，保持正面3×2×0.3水幕、8容量、续期/锚点、同玩家同术单实例、伤害和水穿透例外。

先审计cast/renew/absorb/contact/intercept所有读取时点，物理/魔法弹体区别与三叉戟反弹行为，区块/owner失效和清理。共享ACTIVE归属必须独立任务后处理，不能用全局Map单元测试冒充服务器集成。

允许先只读审计并写本卡/证据。实现前拓展WaterShieldRules/Absorb/Contact及Adapter、既有Area实体装配和独立SelfEffect/准备付款合同路径。不得用高级障壁通用解释器复用该术，保留原数值/资源/注册/保存ID。全部重构完成后统一游戏实测，不启动游戏、不Git。

允许实现WaterShieldRules/Contact/Absorb核心与合同，WaterShieldAdapter/ContactAdapter，MagicAreaEntity独立委托、SelfEffectComposition绑定。正式有效值6MP/100准备/0恢复/80寿命；contact原Map每20tick清理、随机湿身留MagicEffects且自己只受阻力不触发状态。拦截/ACTIVE实例归属未迁，不能提前关闭整术卡。

本批扩允许WaterShieldSelection/CollisionAdapter及共享ShieldInterception核心（物理盾委托同一实现，既有合同继续验证）。最近接触strict<保留同距首项、NaN/Infinity行为；资格先过滤再contact，法线>=0跳过，保留IEEE。先地形可见才算incoming/absorb，再读取三叉戟并反弹或销毁；上游Core/Expansion/Physical拦截顺序不动。ACTIVE已由独立R07-area-runtime迁服务器附件。

## 水盾结构切片及领域注册表服务器归属（2026-11-13）

WaterShieldRules/Contact/Absorb/Selection与WaterShieldAdapter/ContactAdapter/CollisionAdapter已接实际准备付款、实体接触及服务器弹体拦截；保持6MP/100准备/0恢复/80持续、锚点strict<1E-6、容量8、每接触drag后mark/首次claim后湿身、每20tick原Map清理。最近strict<同距首项，流体/地形不改；共享ShieldInterception已由物理盾与水盾双调用，先地形→incoming/absorb→三叉戟类型→反弹/销毁。原资源/ID/数值不变。独立R07-area-runtime把全局ACTIVE改成AreaRegistry服务器附件，仅initializer，五个清理调用传入player/server；6项核心隔离/异常合同不是Fabric附件集成。新添29项合同（水盾命令6、规则/接触/吸收8、选择4、共享拦截5、注册表6），本切片结构离线PASS；实际世界/同服多人/生命周期仍未验收。

完整离线build退出0：63单测/555模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-water-shield-final-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-heat_hand：审计交互权限、工具/方块处理、持续资格与付款/准备，提纯规则及独立世界Adapter；其后按账本heat_island/burning_place推进。
