# R06-channel-runtime：STRUCTURE_SLICE_DONE

审计基线：PhaseTwoRuntime Channel服务器附件拥有Map，水瀑/火焰各已有独立Channel核心，7tick心跳、费用/小数债务与20tick水源节拍均保留。剩余准入、注册/首发/声音顺序及世界资格仍在旧类。

允许ChannelAdmission纯协调/合同、ChannelRuntimeAdapter与PhaseTwoRuntime兼容委托，CHANNEL_DEBT/channel_runtime附件ID和initializer/persistent/copyOnDeath标记不变；保持旧同术heartbeat、异术忽略、校验短路。首次water.begin成功之后先登记，再glow/release；火焰begin付款后先登记再emit最后release。begin false不登记，登记后初发异常保留已登记会话不回滚。stop先从容器移除，再恢复总计时及核心stop，重复stop无动作。

tick先保持旧columns/burns/shield/prune顺序，再通道处理；clear先stop，停服移除同一附件。游戏不启动，不Git；全部重构完成后统一实机验收。

## 持续通道运行时归属与首次提交（2026-11-13）

ChannelRuntimeAdapter承接原服务器附件容器、资格预检、heartbeat、begin/stop/tick及兼容generateWater，channel_debt/channel_runtime ID与持久属性不变。ChannelAdmission纯核心保证begin成功→register→emit→sound，6项合同覆盖拒绝/异常及提交后副作用保留。PhaseTwoRuntime保持columns/burns/shield/prune在通道前运行，停服移除其他附件顺序不变。WaterfallChannel/FlamethrowerChannel已有费用/心跳/停止合同继续通过。只是共享通道结构切片，真实附件重载/多服务器集成未验收。

完整离线build退出0：63单测/522模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-channel-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R06-release-routing：清理PhaseTwoSpells剩余构造/计划/释放世界业务，审计已迁R06全部装配和兼容路径，再处理准备资格与通知协调。
