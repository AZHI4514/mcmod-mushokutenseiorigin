# 当前开发状态

更新：2026-11-14。**R00—R10代码重构完成；离线自动验证通过；实际游戏验收NOT_RUN。** 这里的完成仅指已批准的等价架构迁移，不表示修复所有旧玩法差异或游戏验收通过。

逐术追踪见[MIGRATION_LEDGER](reference/MIGRATION_LEDGER.md)，实际树见[ACTUAL_FILE_TREE](reference/ACTUAL_FILE_TREE.md)，模式和SOLID审查见[当前架构](ARCHITECTURE.md)。102术每术有纯模块、独立释放绑定和对应行为模块测试引用；原39特例及102术构造后有效值由冻结合同保持。对应JSON可用于下一轮机器核对，不能把源码匹配解释成测试覆盖率。

| 阶段 | 代码结果与证据 |
|---|---|
| R00—R02 | 冻结102术/教材/资源/兼容键；JDK-only核心与构建/依赖/资源门禁 |
| R03—R04 | 几何、计费、时序、玩家状态与教材纯核；外置Codec及旧七字段fixture |
| R05—R06 | 准备State、Command、Factory Method、窄Adapter；逐术弹体/命中/注魔/持续/反冲/束缚；清除PhaseTwo/Intermediate业务类 |
| R07 | 领域/天气/有限及供能障壁策略；核心仪式；服务器/level归属；地形权限/放置/掉落/队列/恢复分责 |
| R08 | 治疗/解毒、声音/控制、召唤Template Method与供能/尸体存储、通信签名、传送邀请/补偿/安全落点、幻影模块 |
| R09 | 客户端输入/UI/传输/注册分离；纯像素网格和变形策略；资源加载Repository；视觉不依赖服务端世界Adapter |
| R10 | 102唯一注册覆盖、逐术测试/视觉/兼容键门禁；无用门面和示例Mixin清理；最终无缓存干净构建、打包与资源校验 |

原类去向：PhaseTwoEntity→DirectionalSpellCarrier；IntermediateProjectile→SweptSpellProjectile；对应Renderer按载体命名；PhaseTwo/Intermediate的释放/飞行/碰撞/运行队列职责迁到专门Adapter与纯规则，清空后删除。ExpansionFields/Barriers及CoreFields/ModeFields/SmokeFields/WindFields转发门面删除；ExpansionTerrainLeases→TerrainLeaseRepository+TerrainLeaseScheduler+TickQuota，ExpansionTerrain→PermanentTerrainAdapter，ExpansionParticles→ParticlePacketAdapter，ExpansionVfx→LayeredPixelMeshRenderer+LayeredPixelMesh。SpellDefinition类型进入核心，102构造值分10元素目录。具体历史类型清单见[evidence/r10-final-old-class-destinations.json](archive/refactor-2026-11-14/architecture/evidence/r10-final-old-class-destinations.json)、[机制名映射](archive/refactor-2026-11-14/architecture/evidence/r10-mechanism-class-names.json)及[转发门面清单](archive/refactor-2026-11-14/architecture/evidence/r10-removed-forwarding-facades.json)。有真实边界职责的Vec3/AABB转换、兼容Codec及公共MagicSystem别名保留，不能为减少文件数删掉。

验证命令：`gradlew.bat clean build --offline --no-build-cache --rerun-tasks`，退出0，26个任务全部执行。63项JUnit单测、944项模块测试；旧verifyMagic 15,658项动态断言；102构造后有效值、39/102教材、旧玩家存档fixture、31,000组几何差分、资源哈希、许可证、最终JAR核心恰一份全部通过。新增动画20,010组与27,300组、分层网格820组差分通过。源码兼容字符串全量仍存在，记录[evidence/r10-final-compatibility.json](archive/refactor-2026-11-14/architecture/evidence/r10-final-compatibility.json)；不等同实际Codec重载。构建日志[evidence/r10-final-build.log](archive/refactor-2026-11-14/architecture/evidence/r10-final-build.log)，JUnit汇总[evidence/r10-final-junit-summary.json](archive/refactor-2026-11-14/architecture/evidence/r10-final-junit-summary.json)。

未验收：Minecraft启动/Fabric与Mixin加载、真实世界碰撞/权限/PVP/区块、实体与附件Codec实际重载、双视角手臂和模型/透明排序、联机、性能/大量粒子、第三方保护及跨维兼容，全部NOT_RUN。未启动游戏，未Git暂存/提交/推送。

保留原差异：[SPEC_DEVIATIONS](reference/SPEC_DEVIATIONS.md)中的D-005碰块独立群伤身份与D-006风裂多次新接触AOE语义没有擅改；D-001不能由重构测试宣称所有设定无差异。它们属于玩法规格/实机验收，不作为架构未迁移项。暂缓成长/高阶治疗疾病/复活转生/过去转移/龙门/使魔NPC继续暂缓。

后续操作：用户明确要求后开展游戏验收；每项单独记录PASS/FAIL/NOT_RUN。新的功能仍先读正式游戏规格与MAGIC_DEVELOPMENT；不要重复实施历史切片。旧追加进度完整归档到evidence/pre-final-history，当前状态只读本文件；归档状态指针不再更新。


## 后续维护

本文件是唯一当前进度，不再更新归档的REFACTOR_STATE、任务卡或最终报告。当前没有待继续的架构迁移任务；实机验收等待用户明确要求。新任务写明目标、已完成、未完成、验证结果和下一步；任务完成后清除过期下一步。测试计数是2026-11-14基线，不代表未来每次都已重新执行。
