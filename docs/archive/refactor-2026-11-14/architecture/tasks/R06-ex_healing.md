# 中级治愈：SLICE_DONE（归属R08；历史R06任务卡）

正式第37术中级治愈，不扩大到暂缓高阶治疗/疾病。先审计构造后有效参数及原治疗规则。

## 土落弹结构切片收尾（2026-11-13）

新增2项命中历史/停止组合合同，恢复历史抑制重复、新目标同停止面可命中，穿透多目标先claim，放石预检false仍discard。完整离线build通过，63单测/248模块测试、旧15,658断言、102定义教材及资源/JAR检查通过。证据r06-pillar-history-build.log和JUnit汇总/XML。

保存/加载/虚空/无寿命及接触顺序源码审计见evidence/r06-earth-pillar-exit-audit.md。明确内部扫掠加载检查位于age递增之后，不笼统承诺所有挂起都不增age；fallingRock不依赖age超时。实际世界与保存重载NOT_RUN。土落弹仅核心/协调结构切片SLICE_DONE。

当前R06-ex_healing IN_PROGRESS（仅新任务卡，尚未迁代码）。下一精确动作：审计中级治愈有效参数、缺血资格和治疗量、付款/原版heal与通知顺序，再提取纯治疗规则和自身效果Adapter，复用SelfEffect Command。D-006未决；不启动游戏、不自动Git操作。

## 中级治愈纯规则与自身释放（2026-11-13）

有效基线16MP/160tick准备/10恢复/最多8生命。ExHealingRules提取finite、存活生命值及min(8,max(0,max-health))；SpellDefinition中级治愈分支委托，其他治疗定义保持。10,000组冻结生命对差分与满血/死亡/非有限/小缺血边界通过。ExHealingAdapter仍调用原版heal并返回true，包括兼容execute的0治疗；缺血资格仍由准备前及释放前canExecute核验。不得额外增加付款后的资格拒绝改变旧顺序。

SelfEffectComposition显式绑定并复用Preparing/Command/成功通知，不新造Factory。完整离线build通过，63单测/250模块测试、旧15,658断言、102定义教材及资源/JAR检查通过。证据r06-ex-healing-build.log及JUnit汇总/XML。真实原版heal、附着回调与事件兼容尚NOT_RUN。

当前R06-ex_healing仍IN_PROGRESS。下一精确动作：补16MP准备/释放与治疗组合合同，审计满血取消、重复输入、异常和兼容入口，核对后仅关闭核心结构切片。高阶治疗/疾病继续暂缓。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 中级治愈准备/释放合同（2026-11-13）

新增6项核心合同覆盖160tick准备、扣16MP后计算小缺血治疗量并通知、非法角度/不足MP/未学拒绝、单次命令、false退款、部分异常付款保留、观察者隔离与取消。完整离线build通过，63单测/256模块测试，旧15,658行为断言、102定义教材及资源/JAR检查通过。证据r06-heal-contracts-build.log与JUnit汇总/XML。

源码核对：MagicPreparation.request和释放前均用canExecute检查治疗量>0；准备期间回满血会在释放前返回且不扣费。SelfEffectCommand本身不重做缺血资格，兼容execute仍允许原版heal(0)并返回true；原版heal/模组事件真实运行NOT_RUN。上述满血资格为源码审计，未把测试替身当Fabric运行证明。

当前R06-ex_healing仍IN_PROGRESS。下一精确动作：将治疗量读取→原版heal的窄端口协调与真实Adapter接通，补零治疗兼容/读取顺序/异常合同，完成核心切片退出审计。高阶治疗/疾病继续暂缓。不启动游戏、不自动Git操作。

## 中级治愈核心切片收尾（2026-11-13）

ExHealingEffect.Patient窄端口已接真实Adapter，依次读health/maximum，计算后调用原版heal；零治疗仍调用并成功，异常传播不重试。3项新合同验证顺序、零治疗兼容及异常。完整离线build通过，63单测/259模块测试、旧15,658断言、102定义教材及资源/JAR检查通过；证据r06-heal-effect-build.log及JUnit汇总/XML。

准备前/释放前缺血检查仍由MagicPreparation→canExecute处理，付款后的heal调用保持旧行为。无新实体、网络或保存ID、无独立运行容器；死亡/跨维/断线共享准备取消未改。仅核心/协调结构切片SLICE_DONE，真实原版治疗/事件、视觉、联机、性能NOT_RUN。

账本归属校正：原MIGRATION_LEDGER将ex_healing归R08，之前R06任务卡命名是执行过程偏移，不代表R06或R08整阶段完成。保留历史tasks/R06-ex_healing.md及证据路径，账本仍R08-ex_healing并链接该历史卡；不擅自宣称全阶段退出。下一动作回到R06剩余项physical_shield审计，先读原39术规格与实际入口再建实施切片。高阶治疗/疾病仍暂缓。不启动游戏、不自动Git操作。
