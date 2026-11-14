# R07-burning_place：STRUCTURE_SLICE_DONE

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`burning_place`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


正式/有效值：10MP，2伤害，80持续，100准备/0恢复，随玩家全身移动，以owner包围盒inflate(.12)接触。每目标20tick先claim再尝试hurt；失败仍占本次尝试，成功后附效/afterHit。80%火附效仍原MagicEffects通路，保留火免/抗火与随机读取顺序。

允许BurningPlaceContact核心/合同、BurningPlaceAdapter及Area实体该术委托、SelfEffectComposition与独立命令合同；原可见性只有COLLIDER/NONE clip，不添加新chunk预检。PVP/自身过滤在接触后可见性前；原Map每tick统一prune，先于目标查询，保留。

共享领域生命周期与几何随后独立任务；本术不新增存档、实体或资源。全部完成后统一游戏实测，不Git。

## 燃烧领域贴身接触协调（2026-11-13）

BurningPlaceContact/Adapter保留history→全身接触→权限→原clip可见→claim→hurt→effects→hook，伤害拒绝仍消耗本目标本次20tick尝试，异常不回滚claim。贴身owner包围盒inflate(.12)、自身排除与PVP、原无chunk采样的可见射线不变。10MP/100准备/0恢复独立命令已接，80持续/2伤害/原随机点燃不变；删除原实体零调用的局部几何/可见帮助函数。新增14项模块合同（8命中协调、6付款），共享Area生命周期仍单独收尾。

完整离线build退出0：63单测/593模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-burning-place-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-magic_shield：抽魔法直伤允许事件/容量吸收及独立准备付款，随后icicle_field；共享Area生命周期另卡处理。
