# R06-water_splash：即时范围施法切片（离线PASS）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`water_splash`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


Intent I-02/I-08/I-10；正式《魔法-游戏设定》第17术。8MP、1伤害，160tick准备/10tick恢复，最远12格选点，落点半径6格球形可见生物群伤；成功伤害100%湿身2000tick。

旧入口MagicPreparation→SpellExecutors→IntermediateSpells.execute：检查整条选点路径已加载，方块射线选点，未命中取12格端点，命中向空气侧退.02；范围先AABB后目标身体中心球距与可见性筛选。WORLD/权限/PVP由Adapter执行。先核对构造后快照和逐术规格。

下一步：先写12格落空/碰块选点、半径边界与遮挡输入、MP/取消/失败回滚、100%湿身及异常测试，再设计窄TargetedArea端口/用例。瞬时群伤不得伪装成ProjectileSpawner，不复制假Minecraft世界；复用已存在纯几何及真实必要的经典模式，不增DSL/空类。水炮D-005不能顺便改。

完整build --offline，保存日志/JUnit/XML及唯一状态。GameTest/实际视觉/联机/性能NOT_RUN，不启动游戏、不操作Git。

## 实际完成范围

WaterSplashSpec/Rules负责12格选点端点/碰块回退、6格球形身体中心筛选与范围包围盒。TargetedArea窄端口分离只读locate和不可逆apply；CastTargetedAreaCommand协调知识/选中/MP/角度校验、选点成功后一次扣费及提交后通知。没有伪装成ProjectileSpawner或添加空工厂。准备阶段复用CastingSession。TargetedAreaComposition仅装配水蒸，后续范围术按真实差异扩展。

WaterSplashAdapter保留原版区块加载检查、射线、可见性、伤害源和成功伤害后100%湿身2000tick；临时IntermediateProjectile仅当伤害源且不加入世界，原source构建语义保持。旧IntermediateSpells水蒸入口委托同一Adapter，不复制第二份实现；该批次类其余职责未迁，暂保留。

异常语义：选点失败/只读选点异常发生在扣费之前；apply开始后异常不退款、不发送成功通知，防止部分不可逆伤害变为免费。无目标依旧算成功释放，与旧实现一致。Observer异常不退款且继续后续通知；不尝试undo原版伤害。

验证：build --offline退出0，evidence/r06-water-splash-build.log及r06-water-splash-junit-summary.json；23单测、50核心模块测试、15,658旧断言、31,000几何差分、102参数及教材、旧存档/资源/JAR检查通过。实际区块/射线/PVP/无敌帧/Mixin/视觉/联机/性能仍NOT_RUN；纯端口测试不是模拟Minecraft验收。

下一R06为ex_flame（大火球）；icicle_field保留R07归属。未启动游戏，未操作Git。
