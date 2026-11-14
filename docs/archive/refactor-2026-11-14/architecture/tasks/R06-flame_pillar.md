# R06-flame_pillar：SLICE_DONE（核心/协调；非全术或世界验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`flame_pillar`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


第30术，前置burning_place；12MP/160tick准备/10tick恢复，最远12格选中生物或方块。选中生物首次点燃60tick，立即3火伤，20/40tick再尝试3火伤，60tick到期；成功火伤80%刷新200tick依旧由原MagicEffects处理。地形火只在首次生物脚下或选中方块合法空位生成，不沿路径重复放火。禁止改成群伤柱体。

已完成入口源码初查：PhaseTwoSpells.execute先trace，target非null走PhaseTwoRuntime.igniteTarget；否则二次aim/igniteBlock。igniteTarget配置但不加入世界的伤害carrier，先点燃→伤害→placeFire→burst→BURNS入队；tickBurns按绝对start计时，死亡/跨维/age>=60移除，age>0且age%20==0伤害。当前静态BURNS仍未迁，clear删除玩家任务；需详细审计停服/断线/重复施法与负age边界。不要用新规格掩盖原代码差异。

下一精确动作：读取igniteBlock/trace、MagicPreparation有效160/10规则和damage/clear链；选择合适布尔放置端口，保留首次世界副作用后异常的真实退款边界。提取纯时点规则并接真实入口，补取消/失败/重复施放/任务隔离/期限合同。完整build --offline并归档，更新唯一状态与账本。游戏/权限/原版火/Mixin/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 火柱核心/释放/任务归属切片（2026-11-11）

FlamePillarSpec提取12MP/3伤害/60tick任务等有效常量。FlamePillarAdapter分别缓存生物或方块选点，consume后不能重复place；LocalEffectComposition显式绑定既有CastLocalEffectCommand/Preparing/Observer，失败无效果返回false恢复原状态，首次世界副作用后异常保留付款。旧execute兼容入口委托同Adapter，不额外收费。二次只读aim现于locate内、扣费前完成，同一服务端调用不跨tick；原有效命中条件不变。

FlamePillarIgnition保留点燃60→首次伤害→首次脚下放火→burst→入队顺序。FlamePillarBurn以绝对start判定，age>0且20整倍数才尝试伤害，age>=60/目标死亡/目标跨维先结束；不补漏tick，不给重复同tick调用发明去重，不重复地形火。FlamePillarFirePlacement保留先命中面后顶部、权限优先、已有普通/灵魂火直接成功、仅空气与可存续火、false后继续候选的原逻辑；不去重重复候选。

静态BURNS已移除，burn_runtime附件让当前服务器主世界拥有玩家任务Map，不持久化/不同步；玩家clear删除本服该玩家任务，停服clearChannels同时移除该附件。多个施法任务仍独立追加，不合并；已有原版火不因停止/清理而熄灭。SHIELDS/CONTACTS等其他遗留状态未迁，真实服务器隔离仅源码审计。

新增17项模块合同：准备/收费/资格/取消/观察者/异常、首次效果顺序与20/40/60期限、死亡/跨维、时间跳跃/倒退、独立任务、放火回退/权限/已有火/重复候选，以及一次付费→首次点燃放火→两次延迟伤害组合。完整离线build通过：63单测/154模块测试，旧15,658行为断言、102定义/教材、资源/最终JAR检查通过。证据r06-flame-pillar-build.log、JUnit汇总、r06-flame-pillar-exit-audit.md。

火柱已验证核心/协调切片SLICE_DONE，不等同整个法术全部重构或世界验收；共享选点/权限/伤害/附效/实体保存服务留待相应阶段。当前任务转R06-flamethrower，下一步拆独立通道协调、锥形命中纯规则与每目标20tick节拍，复用现有ChannelPayment/ChannelSession而非复制水瀑。游戏/原版火/权限/存档/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。
