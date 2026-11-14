
## PhaseTwo服务端预检和分派协调（2026-11-13）

PhaseTwoServerTick纯核心已接PhaseTwoTickAdapter与原实体tick，保留super/updateBounds先行、低于世界discard、当前区块检查、owner读取、stationary无owner暂停、移动ahead检查、寿命检查、age递增及shield→stationary→fall→fly顺序。stationary重复读取保持原次序；不新增isRemoved短路。适配器注入age/remaining/anchor快照及窄回调，不增公共setter。落石内部第二次loaded失败仍发生在age递增之后；客户端/存档/注册/资源不改。

新增12项模块合同，完整离线build退出0：63单测/492模块测试、15,658旧行为断言、102有效定义与39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-tick-build.log和JUnit汇总/XML。核心端口合同不是Fabric/真实世界/Codec重载/事件/视觉/联机/性能验收，以上NOT_RUN。用户要求全部重构完成后统一游戏测试，不启动游戏、不Git。

R06-phase-two-runtime仍IN_PROGRESS：stationaryTick、fall、fly的内部世界业务和共享候选/终止协调、保存归一化尚待逐项迁移。下一精确动作审计fly候选过滤/排序及接触循环，用已有ContactCandidates等核心时保持PiercingFlightRules与其他分支求值次序，尤其爆炸contact后的动态piercing重读与风裂独立AOE历史。先扩任务卡再实施，不因本分派切片关闭整个R06。
