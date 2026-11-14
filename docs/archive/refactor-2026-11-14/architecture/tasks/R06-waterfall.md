# R06-waterfall：SLICE_DONE（核心/协调切片，非全术迁移或世界验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`waterfall`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


SpellId：waterfall（水瀑，第23术）。前置R06-ice_pillar切片；intent I-02/I-10/I-11/I-12。按正式《魔法-游戏设定》第23术与当前有效快照核对，不以当前bug反改设定。

规格：中级160tick准备，持续4MP/秒按tick分摊，停止后10tick恢复；首次与每20tick即时尝试于12格首个目标旁空格生成1格水源。无飞行/伤害；永久原版水自然流动，蒸发规则保持；最新成功水源连通水内其他生物100%湿身200tick，约每10tick刷新，停止不再会话刷新。

旧路径：MagicPreparation→PhaseTwoRuntime.channel/pay/tick/stop；PhaseTwoSpells.instant/trace；MagicWaterSources.place/renew；MagicGlow/MagicSounds。CHANNEL_DEBT的channel_debt持久键、[-19,0]范围/copyOnDeath语义，以及magic_water_sources保存ID/pos/expiry reader保持。原水源已为Long.MAX_VALUE永久；不要误读旧类注释改回临时水。

当前审计：首次pay后instant失败会同时恢复旧MagicState和oldDebt；首次成功后记录Channel。每tick资格/原维度/7tick心跳超时检查后pay，更新最近水源renew，++ticks每20tick再尝试生成。停止只移除会话并设max(旧冷却,术冷却)，不删水，不整MP退款预付小数。水与火焰放射共享pay，迁移不可改另一术行为或让共享债务翻倍。

目标：纯ChannelCost/计时与资格输入，真实需要时扩展既有State；窄应用用例和水源Adapter，不套瞬时TargetedArea Command或ProjectileSpawner。状态按服务器实例归属，断线/死亡/跨维/停止钩子保留。端口持久余额与世界不可逆效果明确提交/失败边界，不建DSL/空工厂。

允许文件：magic-core waterfall/channel规则与合同、PhaseTwoRuntime/PhaseTwoSpells/MagicWaterSources及专属Adapter/装配、对应测试和docs。撤回仅限本任务实际改动，保留已有用户工作；不reset。

执行顺序：先读pay完整公式和PhaseTwoRuntime.clear生命周期、原通道纯计算；写4MP/秒20/200tick/不足1MP/初次失败回滚/跨停止保持小数预付等合同，再迁运行协调/水源端口并核对水火共享费用。随后处理服务器状态归属。每次持久指针保留精确未完成动作，不把源码审计当游戏验证。

最小合同：成功/失败/取消/重复心跳/超时/费用守恒/20tick生成节拍/10tick湿身刷新时点/实例隔离/旧债务恢复。实际原版放水、蒸发、连通搜索4096上限/16格范围、PVP、卸载/重载/流动及联机性能单列NOT_RUN。完整gradlew.bat build --offline，归档evidence/r06-waterfall-build.log和JUnit。下一SpellId：ice_lance。

GameTest/视觉/联机/性能NOT_RUN；不启动游戏，不执行git add/commit/push。

## 当前实际落地（覆盖前面的初始执行顺序）

ChannelCost、ChannelPayment、ChannelDebt/ChannelDebtAdapter、ChannelSession、WaterfallSpec已存在并被生产入口使用。PhaseTwoRules旧数值入口委托纯公式；PhaseTwoRuntime共用pay走ChannelPayment，水瀑首次生成用begin执行付款/false双快照恢复，火焰放射仍按原pay路径。ChannelSession替代原通道heartbeat/ticks，原顺序仍是资格/7tick超时→水瀑有效目标→付款→光效→续水→每20tick生成。

CHANNELS全局表已移除，主世界非持久化/不同步channel_runtime附件持有实例Map。MagicSystem停服遍历旧clear后再clearChannels(server)，不清其他服务器通道；旧channel_debt的Codec[-19,0]/copyOnDeath与水源存档未改。其他PhaseTwo静态表未改，不能宣称整个RuntimeStore迁完。

build --offline退出0；45单测/76模块测试。新增4项单测验证冻结旧费用公式、交替4/6速率守恒、整数边界、7tick心跳和20tick节拍；5项模块合同验证首次false完全回滚、200个付款tick消耗40MP/首次加后续共10次生成、0MP消耗小数预付、重建用例/切速率余额延续、未知部分生成异常不退款。证据evidence/r06-waterfall-channel-build.log、r06-waterfall-channel-junit-summary.json和waterfall-channel-前缀XML。真实水流/湿身/生命周期并未实测。

以上为上一检查点历史；当前完成内容与唯一下一动作见下方。

## 水瀑通道与连通水检查点（2026-11-11）

WaterfallChannel已接真实入口，依赖PlayerStore/ChannelDebt/WaterSource；首次false恢复MP和债务，保留目标检查→付款→光效→续水→每20tick生成顺序。WaterfallSourceAdapter拥有最新成功水源，保留首次生成后二次trace与旧instant兼容入口。Preparing/Idle用于准备，新增实际Channeling State响应心跳、资格取消和第7tick超时；停止幂等、冷却max(existing,10)，不退款预付信用、不删永久水。

ConnectedWater将六邻接搜索及世界时间每10tick刷新判定提为JDK纯函数，MagicWaterSources仅适配已加载水格查询与实际湿身应用。保留±16范围、DOWN/UP/NORTH/SOUTH/WEST/EAST顺序、4096仅在出队前检查（单轮可略超限）的历史边界。magic_water_sources、pos/expiry Codec、Long.MAX_VALUE永久水、旧有限租约及卸载处理未改。51单测/82模块测试与完整离线build通过；日志和XML见r06-waterfall-connectivity证据。不是Fabric/世界集成验收。

下一精确动作：审计水瀑停止与断线/死亡/跨维/停服入口及保存加载兼容调用链，补足可离线验证的实例隔离合同；核对后仅关闭已验证切片并创建ice_lance任务卡。水流、蒸发、连通湿身、权限、重载、游戏视觉/联机/性能均NOT_RUN；仍IN_PROGRESS。不启动游戏，不自动Git操作。

## 最新检查点：水瀑切片收尾，冰枪开始（2026-11-11）

水瀑核心/协调切片已通过源码生命周期审计与双实例隔离/停止重建信用合同，记SLICE_DONE，不代表整术全部迁移或世界验收。审计证据见architecture/evidence/r06-waterfall-lifecycle-audit.md（architecture内使用evidence相对路径）。永久水SavedData和实际世界适配保留，事件/Fabric/存档实际重载仍NOT_RUN。

当前R06-ice_lance IN_PROGRESS：IceLanceSpec接入原定义的MP/伤害/范围/历史寿命，IceLanceRules接入实体首个递增age命中与MagicEffects冻结判定，保留80%/200tick及随机采样位置。53单测/84模块测试和完整离线build通过，证据r06-waterfall-final-ice-start-build.log及对应JUnit。没有更改102术有效数值、资源或ID。

下一动作：按R06-ice_lance任务卡提取即时选点/生成窄端口与应用释放协调；明确addFreshEntity=false的无效果退款边界，不把该boolean结果塞进已有void TargetedArea.apply；接入Preparing State并保留SequentialMagic成功通知。首击目标集合/可见性/伤害、实体存档及视觉随后逐项审计。未启动游戏，未Git操作。
