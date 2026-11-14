# R06-phase-two-runtime：STRUCTURE_SLICE_DONE

## 飞行/驻留/落石适配允许范围

本批允许PhaseTwoContactTraversal核心及合同、PhaseTwoFlightAdapter/PhaseTwoStationaryAdapter/PhaseTwoFallAdapter、PhaseTwoEntity窄回调装配。候选复用ContactCandidates，保留实时contact排序、原history读取顺序；接触先claim，再爆炸或普通命中。爆炸分支读取piercing两次，不缓存，且不额外burst/discard；普通分支effect后burst再读取piercing，非穿透discard。驻留/落石世界操作及原已有单术核心原样迁Adapter；实体保留同步/Codec/客户端/几何身份接口。禁止增加公共状态setter，原ImpactHistory引用直接传入。允许本任务和证据文档更新；不改变游戏数值。

## 服务端分派合同及允许实现范围（2026-11-13）

已审计：super.tick/updateBounds先执行，不新增removed短路；低于minY立即discard；当前bounds未加载暂停；随后读取owner，stationary且无玩家owner暂停；移动体ahead未加载暂停；非落石才检查年龄寿命；通过后++age，shield优先于stationary，再fallingRock，否则fly。落石内的第二次loaded检查可在age递增后失败，不能合并预检。shield移动只使renewable=false，不改anchor。保存仍载体逐字段更新，历史最后恢复且替换；不把该次恢复改为原Set清空。

本轮允许PhaseTwoServerTick核心/模块合同、PhaseTwoTickAdapter及PhaseTwoEntity.tick服务端委托，相关文档/证据。适配器只接收私有字段快照和窄回调，禁止新增公共setter。stationary/fall/fly内部业务暂不迁，本次不能关闭整个运行时任务。验证完整离线build，真实世界仍NOT_RUN。

先读REFACTOR_STATE、R06残余矩阵、earth/ice_lance/icicle/flood/wind_slice/physical_shield切片证据；本卡不替代R07各领域/障壁单术迁移。

目标：分清PhaseTwoEntity客户端/同步/Codec载体职责与服务端扫掠、停留、命中历史、计数、地形副作用的纯协调及Fabric适配。保留phase_two_spell注册、Spell/MagicAge/Remaining/AimYaw/AimPitch/MagicVictims保存键及默认/逐字段赋值顺序，保留方向/体积、已迁单术例外。不能把代码整体搬家冒称核心业务已拆。

本卡当前仅允许只读审计PhaseTwoEntity及调用者、更新此卡/状态/审计证据。实现前明确端口、真实失败及异常合同并扩允许路径。实际游戏测试全部重构完成后统一进行，不启动游戏、不Git；游戏/世界/事件/Codec重载/视觉/联机/性能NOT_RUN。

下一精确动作：逐行记录server tick中所有提前返回、年龄/remaining更新、未加载行为、碰撞/命中与保存恢复顺序，选择第一个尚未迁移的窄协调。

## PhaseTwo服务端预检和分派协调（2026-11-13）

PhaseTwoServerTick纯核心已接PhaseTwoTickAdapter与原实体tick，保留super/updateBounds先行、低于世界discard、当前区块检查、owner读取、stationary无owner暂停、移动ahead检查、寿命检查、age递增及shield→stationary→fall→fly顺序。stationary重复读取保持原次序；不新增isRemoved短路。适配器注入age/remaining/anchor快照及窄回调，不增公共setter。落石内部第二次loaded失败仍发生在age递增之后；客户端/存档/注册/资源不改。

新增12项模块合同，完整离线build退出0：63单测/492模块测试、15,658旧行为断言、102有效定义与39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-tick-build.log和JUnit汇总/XML。核心端口合同不是Fabric/真实世界/Codec重载/事件/视觉/联机/性能验收，以上NOT_RUN。用户要求全部重构完成后统一游戏测试，不启动游戏、不Git。

R06-phase-two-runtime仍IN_PROGRESS：stationaryTick、fall、fly的内部世界业务和共享候选/终止协调、保存归一化尚待逐项迁移。下一精确动作审计fly候选过滤/排序及接触循环，用已有ContactCandidates等核心时保持PiercingFlightRules与其他分支求值次序，尤其爆炸contact后的动态piercing重读与风裂独立AOE历史。先扩任务卡再实施，不因本分派切片关闭整个R06。

本批增加允许PhaseTwoTermination核心及合同、FlightAdapter终止委托和载体存档非负归一化复用。保留fluid同距优先、爆炸墙前0.02定位和自身explode生命周期，普通墙先burst再discard；异常不补执行。载体保存键及默认表达式/逐字段赋值次序不变。
用户已授权持续执行直至全部重构完成，无需反复确认；权限/工具真实阻挡才暂停。

## PhaseTwo飞行/驻留/落石结构切片（2026-11-13）

PhaseTwoFlightAdapter/StationaryAdapter/FallAdapter已承接原世界操作；载体只委托并注入原ImpactHistory、私有计数回调与方向/age快照。候选复用ContactCandidates筛完后实时contact稳定排序，保留历史求值次序；PhaseTwoContactTraversal先claim，爆炸定位后读取piercing→explode→重读piercing，不额外burst/discard；普通命中effect→burst→piercing→条件discard。风裂AOE继续使用同一ImpactHistory。PhaseTwoTermination保留流体同距优先、爆炸墙前0.02定位后explode、普通墙burst后discard。存档age/remaining复用原等价非负规则，默认值及逐字段赋值、历史最后替换保持。

18项新增模块合同（接触10、终止8），完整离线build通过：63单测/510模块测试、15,658旧断言、102有效定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-runtime-build.log及JUnit汇总/XML。结构切片不代表真实世界/Codec重载/视觉/联机/性能验收，以上NOT_RUN。用户授权持续执行全部重构，全部完成后统一游戏测试；不启动游戏、不Git。

R06-phase-two-runtime结构切片STRUCTURE_SLICE_DONE：实体同步/Codec/客户端与体积身份仍是载体，新增Adapter的世界API/单术分支继续依赖各自已有核心规则。几何身份配置与零引用兼容最终归R10，不能称整个R06完成。下一R06-phase-two-effects：迁damage/areaDamage的权限→伤害→附效→hook→洪流击退协调及共享加载/射线适配；准备/发布装配仍后续专卡。未把R07/R08未迁法术计为已完成。
