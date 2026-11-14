# R06-earth_pillar：SLICE_DONE（核心/协调结构切片；世界未验收）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`earth_pillar`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


先读正式第36术及构造后基线。保持数值、注册/保存/资源ID，不将发布接入误认为架构迁移完成。

## 土枪结构切片收尾（2026-11-12）

EarthLanceSelection已接真实Adapter：懒查询自支撑块优先，命中则不查生物；不成立才trace生物；legacy instant跳过自查询。每次locate清空选择，place单次消费。4项合同覆盖优先/查询顺序/旧入口/连续独立查询。完整离线build通过，63单测/231模块测试、旧15,658行为断言、102定义教材及资源/JAR检查通过。证据r06-earth-selection-build.log及JUnit汇总/XML。

土枪准备/释放、选择、滴水石预检和命中、永久柱协调记SLICE_DONE（核心/协调结构切片）。construct仅保留原公开兼容实现，当前调用来自EarthLanceAdapter自分支；非self旧分支不擅删，仍属后续兼容清理。共享世界射线/权限/伤害/柱运行容器及客户端不是全迁完成。死亡至重生之间柱可推进的原语义保留，记录在生命周期审计，不声称死亡即时清除。

当前R06-earth_pillar IN_PROGRESS（仅任务卡，代码尚未迁）。下一精确动作：审计土落弹选点、上方6格生成、3×3×3空气检查、下坠实体命中与保存，然后拆纯几何/规则及生成Adapter，保留实际生成false退款。D-006未决；游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 土落弹生成几何与准备释放（2026-11-12）

EarthPillarSpawn提取选点上移6格及3×3×3边界纯几何，10,000组冻结位置差分通过。EarthPillarAdapter保留先trace再aim、aim为空拒绝、优先hit.base底面中心否则block位置；place保留loaded检查与原BlockPos枚举/1E-6上边界、全空气检查、向下方向和addFreshEntity返回值。LocalEffectComposition接入既有Preparing/Command，false恢复付款前状态；兼容execute复用同Adapter。未改实体、模型、纹理或保存ID。

完整离线build通过，63单测/232模块测试、旧15,658行为断言和102定义教材、资源/JAR检查通过。证据r06-earth-pillar-spawn-build.log及JUnit汇总/XML。生成几何合同不等于世界空间、权限或实际释放验收。

当前R06-earth_pillar仍IN_PROGRESS。下一精确动作：核对有效准备/恢复参数并补释放拒绝/退款合同，提取下坠实体的步进/方块流体停止/生物穿透规则和永久石头放置协调；保留卸载挂起、无固定寿命、存档键与原伤害。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 土落弹下坠规则与释放合同（2026-11-12）

构造后基线确认14MP/8伤害/160tick准备/10恢复。EarthPillarFall已接实体speed与fall接触/停止判定：固定0.5，contact<=min(0.5,stop)，历史命中过滤且claim后才伤害；stop<=0.5停止，移动量max(0,stop)。10,000组冻结输入差分、同停止面等号/历史/无穷停止距离边界通过。

新增6项LocalEffect核心释放合同覆盖准备、单次扣14MP/10恢复、拒绝/false完整退款、重复、异常付款保留、观察者和取消。连同2项下坠规则测试，完整离线build退出0，63单测/240模块测试，旧15,658行为断言、102定义教材和资源/JAR检查通过。证据r06-earth-pillar-fall-build.log及JUnit汇总/XML。

实体仍在虚空检查后检查加载边界，不设fallingRock寿命上限；age递增位置、MagicVictims/Spell/MagicAge/Remaining/AimYaw/AimPitch保存不改。碰撞流体/方块搜集、命中顺序、停止后burst→永久石头→discard仍原流程，尚未提取协调。真实世界/Mixin/权限/视觉/联机/性能NOT_RUN。

当前R06-earth_pillar仍IN_PROGRESS。下一精确动作：提取永久石头全空间权限预检/放置协调和下坠停止效果Adapter，补穿透命中历史/停止组合合同并审计保存边界；不改变原方块/流体接触及卸载语义。不启动游戏、不自动Git操作。

## 土落弹永久石头预检协调（2026-11-13）

EarthPillarStone已接原permanentStone：保留原BlockPos.betweenClosed枚举顺序及immutable格点；全部mayChange且空气通过后才开始放置，仍setBlockAndUpdate且忽略boolean，不替换已有方块。提前物化纯格点列表不查询世界；权限和空气仍按原顺序短路查询。未知部分放置异常传播，不新增回滚。

新增3项合同覆盖27格完整预检先于任意放置、27个拒绝位置均无放置、部分异常保留已放置边界。完整离线build通过，63单测/243模块测试，旧15,658行为断言、102有效定义教材及资源/JAR检查通过。证据r06-pillar-stone-build.log及JUnit汇总/XML。真实权限/方块更新/视觉未运行。

当前R06-earth_pillar仍IN_PROGRESS。下一精确动作：提取下坠停止的移动/更新边界→burst→有owner才放石→discard协调Adapter，补穿透历史与停止组合合同及保存边界审计。无固定寿命、卸载挂起、原存档键不变。游戏/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 土落弹停止协调（2026-11-13）

EarthPillarStop及世界Adapter已接真实fall停止分支，保持移动并更新bounds→burst→有owner才永久石头→discard。永久石头返回false仍移除；未知异常仍传播，不新增finally移除或地形回滚。石头中心的ceil(y-1E-6)、朝下特效方向及速度/伤害/保存字段不变。

新增3项合同覆盖完整顺序、无owner、放石异常的部分效果边界。完整离线build通过，63单测/246模块测试，旧15,658行为断言、102有效定义教材及资源/JAR检查通过。证据r06-pillar-stop-build.log及JUnit汇总/XML。端口合同不代表实际世界地形、伤害或视觉验收。

当前R06-earth_pillar仍IN_PROGRESS。下一精确动作：补穿透命中历史与停止效果组合合同，审计未加载暂停、虚空移除、无固定寿命与保存恢复，然后仅关闭已验证结构切片。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

## 土落弹结构切片收尾（2026-11-13）

新增2项命中历史/停止组合合同，恢复历史抑制重复、新目标同停止面可命中，穿透多目标先claim，放石预检false仍discard。完整离线build通过，63单测/248模块测试、旧15,658断言、102定义教材及资源/JAR检查通过。证据r06-pillar-history-build.log和JUnit汇总/XML。

保存/加载/虚空/无寿命及接触顺序源码审计见evidence/r06-earth-pillar-exit-audit.md。明确内部扫掠加载检查位于age递增之后，不笼统承诺所有挂起都不增age；fallingRock不依赖age超时。实际世界与保存重载NOT_RUN。土落弹仅核心/协调结构切片SLICE_DONE。

当前R06-ex_healing IN_PROGRESS（仅新任务卡，尚未迁代码）。下一精确动作：审计中级治愈有效参数、缺血资格和治疗量、付款/原版heal与通知顺序，再提取纯治疗规则和自身效果Adapter，复用SelfEffect Command。D-006未决；不启动游戏、不自动Git操作。
