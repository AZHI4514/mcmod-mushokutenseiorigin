
固体接触合并与首墙选择已迁SolidContacts，保持contact<=步长才合并HashMap、contact<wall才更新墙和impact、同距取首个形状、含水过滤和世界枚举不变，地形仍先于流体。新增6项核心合同及1000组冻结差分；完整离线build通过63单测/452模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-solid-contacts-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计流体候选扫描及固体终止交接，保持读取时点、未加载跳过及流体同距优先，先定义端口再迁移。不游戏、不Git。
