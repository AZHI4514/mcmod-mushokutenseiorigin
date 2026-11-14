# R07-earth_hedgehog：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`earth_hedgehog`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


抽十二刺布局/时序/命中次数纯规则与状态、独立Adapter/SelfEffect；保留32trace地面向UP，dy3到-3找基地，三层碰撞都读取且不短路，12基地必须唯一；400tick寿命及仅age==i*2发伤，不补漏掉的帧。processed在世界处理前写入，命中次数在hurt前写入最多2（受伤拒绝也占一次）。旧SpikesStart/Processed/Bases/Hits和Attachment同步不变。允许上述核心/适配/兼容/装配/合同/文档。不游戏不Git。

## 土刺独立时序与命中尝试状态（2026-11-13）

EarthHedgehogRules/State/Tick与世界Adapter/SelfEffect接入，12刺黄金角偏移原精度，400寿命/2tick递进、严格age==i*2不补漏帧。processed先写再世界处理、命中计数先写再hurt，受伤拒绝仍占两次预算。旧Spikes保存键和附件不变，原基地搜索及三层非短路碰撞保留。8项合同覆盖时序、状态隔离、计数与异常。

完整离线build退出0：63单测/670模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-hedgehog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-quagmire表面体积/跳跃cap/免疫附效和领域生命周期，随后magma_gush。
