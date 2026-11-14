
## PhaseTwo飞行/驻留/落石结构切片（2026-11-13）

PhaseTwoFlightAdapter/StationaryAdapter/FallAdapter已承接原世界操作；载体只委托并注入原ImpactHistory、私有计数回调与方向/age快照。候选复用ContactCandidates筛完后实时contact稳定排序，保留历史求值次序；PhaseTwoContactTraversal先claim，爆炸定位后读取piercing→explode→重读piercing，不额外burst/discard；普通命中effect→burst→piercing→条件discard。风裂AOE继续使用同一ImpactHistory。PhaseTwoTermination保留流体同距优先、爆炸墙前0.02定位后explode、普通墙burst后discard。存档age/remaining复用原等价非负规则，默认值及逐字段赋值、历史最后替换保持。

18项新增模块合同（接触10、终止8），完整离线build通过：63单测/510模块测试、15,658旧断言、102有效定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-runtime-build.log及JUnit汇总/XML。结构切片不代表真实世界/Codec重载/视觉/联机/性能验收，以上NOT_RUN。用户授权持续执行全部重构，全部完成后统一游戏测试；不启动游戏、不Git。

R06-phase-two-runtime结构切片STRUCTURE_SLICE_DONE：实体同步/Codec/客户端与体积身份仍是载体，新增Adapter的世界API/单术分支继续依赖各自已有核心规则。几何身份配置与零引用兼容最终归R10，不能称整个R06完成。下一R06-phase-two-effects：迁damage/areaDamage的权限→伤害→附效→hook→洪流击退协调及共享加载/射线适配；准备/发布装配仍后续专卡。未把R07/R08未迁法术计为已完成。
