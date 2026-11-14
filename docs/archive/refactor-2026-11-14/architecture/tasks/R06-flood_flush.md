# R06-flood_flush：SLICE_DONE（核心/协调；非全术或世界验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`flood_flush`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


第27术水流，前置water_shield，正式设定第27术。12MP/2伤害/160tick准备/10tick恢复，完整准星恒速.5格/tick，无重力，120tick预算/理论60格；定向3×1×1，每目标每发一次，无群伤。成功伤害后60%湿身200tick及原版水平knockback(1.2,-direction.x,-direction.z)。水与WATERLOGGED能力干湿均通过，熔岩/普通固体阻挡，不套冰霜刃完整截面遮挡特例。历史寿命100/range8不替换实际预算。

已做：规格/湿身接入，原击退常量统一，准备/Command/Factory/PhaseProjectileAdapter显式装配与4模块合同通过。

下一：接已共享PiercingFlightRules；核对原bounds、区块暂停、命中排序、先claim后伤害/附效/hooks/击退顺序，补准备发射与预算/命中重建组合合同；保存MagicAge/Remaining/Spell/AimYaw/AimPitch/MagicVictims与reader保持。只关闭已验证切片，不称整术世界验收。允许本术核心/实体/适配/装配/测试文档；不造DSL/空工厂。完整build --offline并归档证据；实际游戏/PVP/实体保存/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 水流共享飞行检查点（2026-11-11）

水流真实PhaseTwoEntity入口已接PiercingFlightRules预算、候选及步内接触判定，复用ImpactHistory与原保存reader；没有复制水术私有飞行引擎。3×1×1扫掠、.5速度、120tick预算/60格理论距离和普通实心阻挡保持，不套冰霜刃完整截面特例。新增准备→真实Command→计划→预算/命中快照重建核心组合合同。

源码审计确认伤害成功后附效→afterHit→原版knockback顺序不变，失败不击退；保存字段、方向/区块暂停不变。证据r06-flood-flight-audit.md。完整离线build通过：61单测/112模块测试；日志r06-flood-flight-build.log及对应JUnit归档。不是实体tick/真实存档/击退抗性验收。

当前R06-flood_flush仍IN_PROGRESS。下一精确动作核对已迁切片退出条件与共享载体剩余职责，记录明确未验收项后转ice_fortress并创建逐术卡；不得把原世界碰撞/保存服务或客户端当已全迁。实际游戏/PVP/重载/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 水流切片收尾、冰砦规划开始（2026-11-11）

水流已验证核心/协调切片记SLICE_DONE；退出依据是准备/发射/预算/命中恢复合同及r06-flood-flight-audit.md源码审计。共享PhaseTwoEntity世界碰撞/存档/实际击退、客户端尚未全迁或验收，明确保留后续职责，不把切片关闭当全术完成。

当前R06-ice_fortress IN_PROGRESS：IceFortressSpec.plan已接原PhaseTwoSpells.plan，纯函数只处理水平主轴与5×3有序格点，保留原版Vec3计算前方2格基点/BlockPos取整和完整准星主轴比较、相等取X跨度、底层到顶层顺序。规格MP14与200tick接入原定义。新增2项规划单测（负坐标/主轴/相等/顺序/唯一/不可变）。完整离线build通过：63单测/112模块测试；r06-fortress-start-build.log及对应JUnit归档。

下一精确动作：迁出冰砦独立世界放置Adapter及准备/释放协调，保留ColumnPlacementAdapter权限/空气/抬升净空预检、目标弹起→有目标逐格生长或无目标临时冰→视觉顺序；检查放置返回失败/不可逆开始的边界，不能将部分放置误判可退款。原地形租约/生长容器继续审计，实际游戏/权限/存档/视觉/联机/性能NOT_RUN。不启动游戏、不自动Git操作。
