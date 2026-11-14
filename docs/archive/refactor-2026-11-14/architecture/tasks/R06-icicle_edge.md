# R06-icicle_edge：SLICE_DONE（核心/协调；非全术或世界验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`icicle_edge`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


第26术冰霜刃，正式docs/魔法-游戏设定.md第26术；前置ice_blade。10MP/6伤害，160tick准备/10tick恢复，完整准星恒速3格/tick，120tick飞行，路径穿透每目标每发一次，80%冻结200tick；无群伤/地形改变。含水属性干湿均穿透、水流通过、熔岩阻挡。保留完整截面部分遮挡通行特例，不能以普通box/中心射线替换。

已做Spec/冻结接入，准备State与Command/Factory及PhaseProjectileAdapter显式装配，4项模块合同通过。历史寿命100/range18不当飞行上限。

下一动作：审计PhaseTwoEntity.fly的FlameSliceVolume(motion,-AimYaw)完整截面阻挡和3×1×1扫掠命中，提取共享几何到mechanic/domain正确位置，火断与冰霜刃分别提供规格，避免水术依赖火术。先冻结原实现差分/部分遮挡与满截面/斜向边界合同，再改真实调用；保持方向精度/选点顺序/稳定距离排序/流体判定。随后飞行预算/保存/命中组合合同和生命周期审计，只关闭有证据切片。

允许本术core/adapter/composition/entity/共享截面几何与测试文档；保留其他修改。禁止空Factory/DSL/新模式，完整build --offline归档。实际游戏/实体保存/PVP/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 冰霜刃共享截面检查点（2026-11-11）

原core火断FlameSliceVolume算法已迁domain.geometry.BladeSectionVolume并删除旧core类；没有留下零职责术模块转发类。游戏侧FlameSliceVolume继续作为原版Vec3/AABB转换边界，火断调用保持；PhaseTwoEntity冰霜刃直接依赖共享几何，经GeometryAdapter转换，保留motion及负AimYaw、1/32格采样与完整覆盖判定。共享算法本来固定3×1×1，两术当前尺寸相同，不为未来假设增加尺寸配置层。部分遮挡通过、相邻方块并集全覆盖停止、微缝仍可通过。

迁移前实现已冻结在test-only FrozenBladeSection；500组固定随机方向/俯仰/位置/方块并集对比fullyBlocked/blockingDistance/contact通过，另有明确部分遮挡/全遮挡边界合同。完整离线build通过：59单测/106模块测试，原有15,658断言及资源/架构/保存fixture检查通过；证据r06-blade-section-build.log与对应JUnit。500是几何采样数，不是额外JUnit数或世界覆盖率。

当前R06-icicle_edge仍IN_PROGRESS；下一精确动作迁冰霜刃飞行预算/候选/步内接触判定到共享纯规则，避免水术互相调用；补负yaw斜向与准备/释放/穿透记录组合合同并审计保存/加载退出条件。真实世界碰撞体读取、区块、流体、伤害权限/存档重载、视觉/联机/性能NOT_RUN。不启动游戏、不执行Git操作。

## 冰霜刃共享飞行检查点（2026-11-11）

PiercingFlightRules迁至mechanic.projectile，原IcicleBreakFlight已删除，冰霜击/冰霜刃真实实体入口共同调用预算/候选/步内接触函数。没有兄弟术依赖或空转发类；保留保存Remaining权威、严格墙/流体距离、步末包含等号与先claim后伤害。其他术继续原判定，未批量宣称迁完。

新增负yaw斜向截面边界合同，以及冰霜刃Preparing→真实CastSpellCommand→计划→120tick预算/360格理论距离/命中快照恢复核心组合合同；此不模拟真实实体tick或证明真实存档加载。完整离线build通过：60单测/107模块测试，既有15,658断言/资源/存档fixture/架构检查通过；日志r06-edge-flight-build.log及对应JUnit归档。

当前R06-icicle_edge仍IN_PROGRESS。下一精确动作：审计本术实体保存、排序与生命周期调用链，核对切片退出条件后转flood_flush，创建其逐术卡；不要把共享规则接入等同整术全迁。实际游戏碰撞/权限/PVP/重载、客户端/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 冰霜刃切片收尾、水流开始（2026-11-11）

冰霜刃保存/排序/生命周期源码审计完成，证据r06-icicle-edge-exit-audit.md；与已有差分和核心合同一起仅关闭已迁核心/协调切片SLICE_DONE，非整术全迁或世界验收。

当前R06-flood_flush IN_PROGRESS：FloodFlushSpec接入原定义12MP/2伤害/.5速度/历史寿命100与range8；湿身60%/200tick独立纯函数接入且保留随机采样位置；水平击退1.2只改常量来源，仍由hurt成功后原版knockback执行。ProjectileComposition复用Preparing/Command/Factory/PhaseProjectileAdapter与原观察者，不新建空工厂。

完整离线build通过：61单测/111模块测试。新增湿身边界单测及4项本术准备/付费发射合同；日志r06-flood-start-build.log及对应JUnit归档。当前下一动作：迁水流飞行判定至已共享PiercingFlightRules，核对3×1×1定向扫掠与120tick预算、含水通过/普通固体阻挡，无需完整截面特例；补准备/释放/命中/保存信用组合合同及击退先后顺序审计。世界/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。
