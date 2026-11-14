
## 准备会话服务器归属切片（2026-11-13）

MagicPreparation进程全局ACTIVE已删除，PreparationRegistry<Pending>由服务器主世界preparation_runtime附件拥有，仅initializer、无persistent/sync/copyOnDeath。initialize在MagicSystem初始化注册附件；原逐玩家cancel顺序保持，停服逐玩家清理后removeAttached，回收剩余引用。所有get/contains/put/remove从当前玩家所属服务器读取。Pending、准备计时、心跳、付款、取消及特殊术分支未改；这不是整个MagicPreparation业务拆分完成。

4项新增模块合同覆盖同UUID两实例隔离、替换/删除身份、清理/重建、取消一个实例不影响另一个。首次测试误认为显式cancel后Idle.tick还应发布CANCELLED；修正为不能READY，保留现有State生产语义，失败日志r06-preparation-initial-failure.log。最终完整build退出0：63单测/412模块测试、15,658旧断言、102有效定义/教材、资源/JAR及架构边界通过，证据r06-preparation-build.log和JUnit汇总/XML。测试只验证核心实例容器及CastingSession组合，真实Fabric附件/停服/跨维/游戏/Mixin/视觉/联机/性能NOT_RUN。

残余矩阵evidence/r06-residual-matrix.md已记录共享Intermediate/PhaseTwo/准备协调等未迁职责，R06仍未整体退出。下一R06-intermediate-runtime先审计共享扫掠/墙流体/未加载/历史/Codec的旧顺序，建立窄协调切片，不改变TNT、水穿透或费用例外。不游戏、不Git。
