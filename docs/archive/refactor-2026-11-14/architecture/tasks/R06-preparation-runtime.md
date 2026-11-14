# R06-preparation-runtime：SLICE_DONE（容器归属；非完整准备迁移）

共享功能任务，无新SpellId；依赖R06-exit-audit残余矩阵。intent I-10及提案每服务器实例RuntimeStore，保留所有术准备/心跳/取消及实际扣费时点。

旧MagicPreparation.ACTIVE为static Map<UUID,Pending>；目标核心PreparationRegistry<T>实例容器与Fabric主世界非持久/不同步Attachment，所有ACTIVE读取/写入/删除改从player.level().getServer().overworld()获得服务器实例；保留Pending、Command、tick/cancel顺序。停服原逐玩家cancel后额外clear该服务器容器，保持其他清理相对顺序。

允许核心PreparationRegistry及模块测试、MagicPreparation、MagicSystem生命周期、相关文档。不触碰定义/资源/存档/网络ID，新增attachment仅内存IDpreparation_runtime，不提供persistent/sync/copyOnDeath。不得把移Map当整个准备架构完成。

最小测试同UUID两实例隔离、替换/删除返回身份、clear只清本实例、重建无遗留；完整gradlew.bat build --offline含架构/102基线/资源/旧断言。真实Fabric附件初始化/生命周期与游戏均NOT_RUN。回撤只本批差分，不Git操作。

下一精确动作按卡实现服务器归属及模块合同，检查退出/死亡/跨维仍原cancel；通过后选择R06-intermediate-runtime审计卡，不能标R06 DONE。

## 准备会话服务器归属切片（2026-11-13）

MagicPreparation进程全局ACTIVE已删除，PreparationRegistry<Pending>由服务器主世界preparation_runtime附件拥有，仅initializer、无persistent/sync/copyOnDeath。initialize在MagicSystem初始化注册附件；原逐玩家cancel顺序保持，停服逐玩家清理后removeAttached，回收剩余引用。所有get/contains/put/remove从当前玩家所属服务器读取。Pending、准备计时、心跳、付款、取消及特殊术分支未改；这不是整个MagicPreparation业务拆分完成。

4项新增模块合同覆盖同UUID两实例隔离、替换/删除身份、清理/重建、取消一个实例不影响另一个。首次测试误认为显式cancel后Idle.tick还应发布CANCELLED；修正为不能READY，保留现有State生产语义，失败日志r06-preparation-initial-failure.log。最终完整build退出0：63单测/412模块测试、15,658旧断言、102有效定义/教材、资源/JAR及架构边界通过，证据r06-preparation-build.log和JUnit汇总/XML。测试只验证核心实例容器及CastingSession组合，真实Fabric附件/停服/跨维/游戏/Mixin/视觉/联机/性能NOT_RUN。

残余矩阵evidence/r06-residual-matrix.md已记录共享Intermediate/PhaseTwo/准备协调等未迁职责，R06仍未整体退出。下一R06-intermediate-runtime先审计共享扫掠/墙流体/未加载/历史/Codec的旧顺序，建立窄协调切片，不改变TNT、水穿透或费用例外。不游戏、不Git。
