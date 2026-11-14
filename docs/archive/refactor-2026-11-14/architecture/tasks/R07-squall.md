# R07-squall：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`squall`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


局部降雨：独立SquallRules/Tick与SquallAdapter/SelfEffect，LocalMagicRain留兼容API。48格逐段射线、16水平半径/32高度、普通900蒸发维度450、10tick湿身300、清火→湿身原序，不改全局天气/不放水/不强载。RainExpiry旧键和shorten clamp不变。抽纯生命周期及暴露判定，保留owner alive→removed→level→到期→adopt短路。不改变cast即apply及失败清理。允许对应核心/适配/装配/兼容/合同/文档。不游戏不Git。

## 局部降雨独立生命周期与暴露规则（2026-11-13）

SquallRules/Tick与独立Adapter接真实SelfEffect/Preparing，LocalMagicRain为兼容API。900/450时长、48格逐段碰撞、16半径、10tick节拍、清火→湿身300不变。RainExposure抽懒半径→chunk→天空→清火→湿身协调。RainExpiry旧键与shorten保留，7项模块合同覆盖短路、时长、暴露、异常。弱实体索引仍为适配器私有，不声称完成真实世界生命周期测试。

完整离线build退出0：63单测/632模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-squall-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-cumulonimbus纯排程/受害冷却/生命周期与世界查询分离，随后lightning/electric。
