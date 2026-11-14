# 土落弹保存与运行边界源码审计（2026-11-13）

PhaseTwoEntity仍保存Spell/MagicAge/Remaining/AimYaw/AimPitch及MagicFlight的MagicVictims。读取仍校验PHASE_TWO定义、夹紧非负age/remaining并用恢复UUID集合构造ImpactHistory；本轮未改Codec/注册/网络/资源ID。核心snapshot重建合同不等于真实实体存档重载。

服务端仍先虚空getY<minY移除，再检查当前bounds与预期下一步区块，未加载时返回；fall内部另检查完整扫掠bounds。外层++age位于前两项加载检查之后、内部扫掠检查之前，故不能声称内部挂起时age绝不递增。fallingRock仍绕过age>=remaining，不设固定寿命。

fall保留fluidHit、非空气且无流体格的完整方块接触、世界候选顺序和allowed过滤；不新增距离排序/视线。接触<=min(.5,stop)且UUID未claim才claim、damage与命中特效；被原版拒绝的伤害仍占一次历史。停止时move/update→burst→有owner放石→discard；放石false忽略，异常照旧传播。

权限、实体保存恢复、区块加载实际调度、伤害和视觉仍NOT_RUN。仅核心/协调结构切片可收尾；共享实体世界搜集与客户端后续阶段继续，不能删除PhaseTwoEntity。
