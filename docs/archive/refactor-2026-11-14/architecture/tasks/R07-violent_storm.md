# R07-violent_storm：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`violent_storm`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


独立ViolentStormPolicy实现共享窄WindFieldPolicy Strategy；真正拥有排除owner、16×16圆柱、每pulse水平外推.2和相消资格，世界适配引用。共享WindFieldContact抽懒目标几何/权限/可见→pulse伤害→移动（immune每tick原读）顺序。每术后续独立Strategy绑定与命中合同，不通用解释器。允许策略/共享contact端口/适配/装配/合同/文档，原伤害与交互副作用不变。不游戏不Git。

## 暴风独立策略与领域接触协调（2026-11-13）

ViolentStormPolicy作为真实Strategy持有16×16、owner排除、pulse外推.2及相消资格，独立SelfEffect绑定。WindFieldContact抽excluded→geometry→owner/permission→visible→pulse→immune→move原序，免疫每tick读时点不改。6项模块合同验证自身排除、免疫不挡伤害、异常阻止运动及策略；其余风术仍LegacyPolicy临时路径待逐卡替换，不声明全部完成。

完整离线build退出0：63单测/702模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-violent-storm-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-tornado独立移动/拉拽策略和脉冲协调，随后vacuum_storm/sky_tempest。
