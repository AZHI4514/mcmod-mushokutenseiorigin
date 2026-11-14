# R06-wind_slice：SLICE_DONE（结构等价切片；D-006设定差异未决）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`wind_slice`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


第32术风裂，前置sonic_boom。8MP/7伤害/160tick准备/10tick恢复，恒速2/无重力/120tick预算理论240格；旧定义lifetime100/range20不能替换实际飞行预算。原版伤害/权限/资源和ID保持。

入口初查：旧PhaseTwoSpells默认分支配置PhaseTwoEntity并addFreshEntity；fly稳定按contact排序、先claim直击UUID，WIND_SLICE在直击目标bbox.inflate(3)取LivingEntity且中心距离<=9且allowed；e==target或hit.claim成功才伤害，不另检查视线。穿透后会继续候选，不同接触可能再AOE但共享命中集合。正式规格写“首个目标及中心3格”，需明确intent/spec/observed差异，不能自行改范围/重复规则或反改文档。当前只记录待审计，未决定冲突。

下一精确动作：核对第32术规格最新修订、PiercingFlightRules与ImpactHistory及旧测试；建立规格/准备释放显式绑定，提取窄命中协调，保留伤害尝试身份、目标顺序、群伤范围和无逐目标视线语义。与D-005历史命中身份冲突分开记录，不静默统一。覆盖直击被AOE提前claim、多群重叠、失败伤害及重建去重，完整离线build归档。只完成实际切片才SLICE_DONE，实际游戏/权限/重载/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 风裂准备/释放与命中协调检查点（2026-11-12）

WindSliceSpec提取8MP/7伤害/160tick准备/10tick恢复；ProjectileComposition显式绑定PhaseProjectileAdapter，真实Preparing/Command/Factory/Observer接入，不改实体/资源/保存ID。PhaseTwoEntity接已有PiercingFlightRules到期/候选/步内命中判定，保留120tick有效预算及已保存Remaining优先，不误用历史lifetime100/range20。

WindSliceHit与WindSliceHitAdapter提取原3格球形范围、候选与共享ImpactHistory协调；候选仍在任何伤害前完成中心距离<=9/allowed筛选，保持原枚举顺序、不新增逐目标视线。直击在fly已claim仍伤害，范围目标claim成功才伤害；被范围提前claim者不会再触发直击。不同新直击可再次触发群伤但整发共享去重。未知部分伤害异常不回滚已claim身份。

新增9模块合同：准备/一次收费/失败恢复/资格取消、直击预claim、范围提前claim、重叠群伤/历史重建、异常、球形与飞行边界。完整build --offline退出0，63单测/180模块测试，旧15,658行为断言及102有效定义/教材、资源/JAR检查通过。首次构建因测试import指向fire.wind_slice失败，改为wind.wind_slice后通过；失败日志与最终日志均保留。

当前仍R06-wind_slice IN_PROGRESS，不收尾：D-006明确记录正式“首个目标AOE”与当前每个新接触可再AOE的差异；本轮只保留现有行为做架构迁移，没有反向改设定。下一动作补付费发射→多候选/范围claim→保存历史重建的组合合同，完成与旧实现的命中序列差分并审计实体生命周期；未澄清D-006前不得宣称整术设定无差异或全迁完成。实际游戏/权限/重载/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 风裂序列差分与生命周期检查点（2026-11-12）

新增付费释放→范围身份记录→历史快照重建组合合同及2000组、每组10次接触的冻结旧算法序列差分。对比有序伤害尝试与最终UUID集合，覆盖重叠候选、先范围命中后直击、接触中途快照重建；全部通过。测试候选由替身提供，不证明真实世界枚举或Codec运行。

实体源码审计：Spell/MagicAge/Remaining/AimYaw/AimPitch/MagicVictims读写未改；未加载当前/前方区块先返回，年龄不推进；age>=saved remaining先discard，随后才++age/fly；风裂无需在线owner才能飞行，伤害仍经原allowed/hurtServer/附效/hooks。客户端轨迹、范围burst及原资源未改。共享实体保存与世界碰撞仍留后续职责迁移，不称全部完成。

完整build --offline退出0，63单测/182模块测试，旧15,658断言和102有效定义/教材、资源/JAR检查通过。证据r06-wind-sequence-build.log、r06-wind-sequence-junit-summary.json及XML。D-006依旧未决，本轮未改变首击/后续AOE玩法。当前风裂仍IN_PROGRESS，不把差分等价写成设定验收。

下一动作：核对风裂剩余共享实体职责与已验证切片退出边界，单列D-006后续决策项；可继续不依赖此玩法决定的风缚入口审计，禁止擅自解决D-006或反改正式设定。游戏/权限/保存重载/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

## 风裂结构切片边界与风缚规则开始（2026-11-12）

风裂已迁准备/释放/纯飞行判定/AOE身份协调，2000组10接触序列差分与付费/历史组合通过。本术结构等价核心切片SLICE_DONE，不代表整术设定符合性：D-006仍独立未决，旧流程按每个新直击可再次AOE保持，不修改正式设定。共享世界候选/实体保存与客户端继续后续职责阶段处理，不因切片收尾删除兼容实体。

当前R06-wind_bind IN_PROGRESS。WindBindSpec.pulse已接PhaseTwoRules.bindPulse，PULSE_DAMAGE接原WindBound.applyEffectTick。剩余100递减时触发80/60/40/20/1，末次为remaining1，不能重写为0或100即时伤害；注册wind_bound、原版magic伤害源、附效放置/同步wind_bound_visual和移动Mixin原样。新增3模块合同含-1000..1000旧谓词差分；完整离线build退出0，63单测/185模块测试，旧15,658断言/102定义教材/资源/JAR检查通过。证据r06-wind-bind-pulse-build.log及JUnit汇总。

下一精确动作：为风缚提取即时生物选点与addEffect窄Adapter，显式绑定既有LocalEffect Command/Preparing；保留免疫tag、addEffect false退款与成功后才同步visual=true，补准备/拒绝/重复/五次脉冲组合合同。D-006留差异账本不擅自解决。游戏/Mixin/权限/药水实际调度/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。
