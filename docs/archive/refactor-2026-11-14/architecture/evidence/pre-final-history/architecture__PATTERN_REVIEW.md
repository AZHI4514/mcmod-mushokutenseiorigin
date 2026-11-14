# 已接入对象的SOLID/经典模式审查

这是实际调用对象记录，不是以名字判定模式，也不宣称102术全部完成。

| 模式 | 真实角色和调用 | 合同证据 |
|---|---|---|
| Strategy | MagicBooks/LearnSpell上下文→BookPolicy；Zenith/Primer/Classified三种策略 | LearningModuleTest共享持书拒绝和已注册集合合同，分类专有前置/挑战 |
| State | Pending持有CastingSession上下文→CastPhase；Preparing/Idle/Channeling各自响应tick/cancel/heartbeat | WaterBallModuleTest精确100tick、取消及超时，无enum switch；Charging等尚不需要，不建空状态 |
| Command | MagicPreparation最终释放作为Invoker→CastSpellCommand.execute→PlayerStore/ProjectileSpawner业务Receiver | 成功扣费、失败回滚、重复提交拒绝；不用undo包装伤害 |
| Factory Method | abstract SpellFactory.create；ProjectileSpellFactory覆盖创建ProjectileBehavior | 用共同prepare输入合同，无每术空工厂，无Map冒称Factory |
| Adapter | PlayerStateAdapter/VanillaProjectileAdapter实现core窄端口，转换原版附件/实体API | moduleTest只验证端口语义，Fabric实际加载仍NOT_RUN |
| Observer | Command成功提交后按List顺序通知VfxObserver；视觉/音效与扣费分离 | 失败不通知、观察者异常不退款且后续通知执行 |

SRP：state只算转换、codec只管存档格式、工厂只造行为、Command只协调释放；各自修改原因独立。OCP：学习新策略不改用例；水弹试点后其余术仍待注册目录迁移，不冒称全局已满足。LSP：同BookPolicy输入域和共同拒绝合同测试；弹体产品仅已实现的普通弹体，不把持续术硬塞spawn接口。ISP：ProjectileBehavior没有tick/save空方法，端口不暴露Minecraft实体。DIP：核心依赖自定义窄接口，Fabric构造器注入；核心编译classpath只有JDK。遗留静态ACTIVE等仍由后续任务逐项迁移，不把局部切片完成称整个架构完成。

R06实审：ProjectileComposition显式不可变绑定六种已迁弹体，Command无需追加SpellId分支；实体类型由VanillaProjectileAdapter构造器注入。水矢/冰击附效纯函数不拥有随机源，不改变采样次数。原版碰撞和PVP仍为Adapter验收范围；32项模块测试不是原版集成测试。原水弹独占装配零引用后删除。

R06火断/冰刃实审：ProjectileLaunch用两个常量各自覆盖输入与shoot行为，实现真正Strategy；SERVER_ROTATION保留原版旋转发射，REQUESTED_AIM保留原始请求角度和直接shoot，未通过enum大switch模拟策略。Composition注入实体供应函数和策略，Command/Factory产品合同不变，不新增每术空工厂。FlameSliceVolume和FlightState只含数学/不可变值；ImpactHistory每发实例拥有命中集合，快照不暴露可写集合；原版伤害与持久化留在Adapter。现有PhaseTwo/Intermediate运行状态尚未迁，不能宣称全局DIP/SRP完成。

水炮审查：复用已有Command/State/Factory与REQUESTED_AIM Strategy，未新增空工厂；块命中Adapter只拥有该术群伤与地形委托职责，权限/真实战斗仍由原版世界层执行。新增纯规则不依赖游戏；水蒸瞬时群伤应另建窄应用端口，不复用弹体spawn来伪造LSP。

水蒸审查：Command接收独立TargetedArea Receiver；Adapter实现只读选点与不可逆范围应用，遵守ISP/LSP，未硬塞ProjectileSpawner。Observer为注入的Runnable列表，仅提交后通知。纯规则无世界依赖，没有为了凑六种模式添加Factory/State空类。范围世界应用开始后不声称可以回滚伤害；该异常语义已有模块合同。

大火球审查：复用真实准备State/释放Command/Factory/请求发射Strategy；BlockBurstRules抽共享算式而非让水火两术互相依赖。BlockBurstImpactAdapter集中同一群伤执行，width/ignite为实际差异，仍调用既有权限/掉落服务；删除已无职责的水炮委托，避免继续积累兼容门面。未声称地形服务/批次实体全局SRP已完成。

真空波审查：继续使用原有State/Command/Factory/请求发射Strategy，无每术空工厂；SonicBoomSpec供旧定义/扫掠共同读取。显式不可变Map.ofEntries不是Factory模式，实际Factory Method仍由ProjectileSpellFactory实现。世界载体职责尚未全部剥离，不能将规格迁移标成全局架构完成。

岩炮弹审查：ChargePayment依赖PlayerStore/ManaReservation/PaidProjectile窄端口，适配器只映射附件与添加已付费实体；不复用普通CastSpellCommand重复扣10MP。ChargeSession只算计时，不宣称Charging State；服务器主世界附件拥有实例Map，静态常量仅是附件类型。费用/资格/计时组合合同与世界Adapter验收分开。没有增加每术空工厂、通用效果解释器或新模式。

冰柱审查：复用Preparing/Idle State、CastTargetedAreaCommand和提交后Observer，TargetedArea接收的是区域作用基点，冰柱的整数方块角点满足该接口，不把柱体伪装成飞行体。locate已做只读目标/放置预检，apply为同步不可逆区域应用；与水蒸共享提交/异常合同，未新增空Factory。ColumnPlacementAdapter承接原预检的实际职责并由冰土构造共享，非只转发旧方法；独立IcePillarRules只做数学/冻结，世界碰撞和租约不泄入core。PhaseTwoRuntime与旧全局准备容器仍未全迁，不能宣称整体SOLID完成。

水瀑进行中审查：ChannelPayment仅依赖PlayerStore与ChannelDebt，首次效果用BooleanSupplier明确false无效果与异常未知部分提交的区别，复用现有JDK契约，不新增通用效果解释器。ChannelCost/ChannelSession是纯算法与计时上下文，不宣称GoF State；该历史检查点后已接入WaterfallChannel独立通道用例。ChannelRuntime实例Map依附服务器主世界，静态仅附件类型；其他旧表未迁。水火共用付款的原始预付债务持久契约保持，未制造每术独立余额。模块合同不是游戏/Fabric/权限验收。

## 水瀑通道与连通水检查点（2026-11-11）

WaterfallChannel已接真实入口，依赖PlayerStore/ChannelDebt/WaterSource；首次false恢复MP和债务，保留目标检查→付款→光效→续水→每20tick生成顺序。WaterfallSourceAdapter拥有最新成功水源，保留首次生成后二次trace与旧instant兼容入口。Preparing/Idle用于准备，新增实际Channeling State响应心跳、资格取消和第7tick超时；停止幂等、冷却max(existing,10)，不退款预付信用、不删永久水。

ConnectedWater将六邻接搜索及世界时间每10tick刷新判定提为JDK纯函数，MagicWaterSources仅适配已加载水格查询与实际湿身应用。保留±16范围、DOWN/UP/NORTH/SOUTH/WEST/EAST顺序、4096仅在出队前检查（单轮可略超限）的历史边界。magic_water_sources、pos/expiry Codec、Long.MAX_VALUE永久水、旧有限租约及卸载处理未改。51单测/82模块测试与完整离线build通过；日志和XML见r06-waterfall-connectivity证据。不是Fabric/世界集成验收。

下一精确动作：审计水瀑停止与断线/死亡/跨维/停服入口及保存加载兼容调用链，补足可离线验证的实例隔离合同；核对后仅关闭已验证切片并创建ice_lance任务卡。水流、蒸发、连通湿身、权限、重载、游戏视觉/联机/性能均NOT_RUN；仍IN_PROGRESS。不启动游戏，不自动Git操作。

模式审查：Channeling是真正CastPhase实现，由CastingSession委托转换；WaterSource由有实际选点/水源身份职责的Adapter实现。ConnectedWater是纯遍历函数，不称新设计模式，不造通用解释器。

冰枪首批仅纯规格/命中判定，不宣称已完成Command或Adapter迁移。现有TargetedArea.apply返回void，不能掩盖实体生成false，应按真实提交边界选择窄端口；不为凑模式增加空类。水瀑隔离合同是纯模块测试，服务器附件生命周期仍仅源码审计。

## 冰枪选点/准备/释放检查点（2026-11-11）

IceLanceAdapter实现LocalEffect窄端口，定位保留原12格trace与底面中心；place返回原addFreshEntity结果。CastLocalEffectCommand为真实单次释放Command，先验证玩家/角度与定位再扣费，显式false恢复完整旧状态，未知部分世界异常保留付款；成功后按恢复显示→SequentialMagic→发光→音效通知，观察者异常不退款。独立boolean契约避免错误复用void TargetedArea或飞行弹体端口。

MagicPreparation的冰枪Pending已持Preparing/Idle State及独立Command，取消仍取消State，原资格/跨维/心跳入口保留。PhaseTwoSpells.instant旧直接入口复用同Adapter，不额外扣费，不保留重复生成实现；没有新增空Factory或通用效果解释器。

完整离线build通过，53单测/90模块测试，新增6项模块合同覆盖准备成功、重复拒绝、无目标、false全状态恢复、非法输入/不足MP/未学拒绝、未知部分异常、观察者顺序/隔离、准备取消。日志r06-ice-lance-release-build.log及对应JUnit已归档。纯模块合同不证明真实顺序魔法事件、实体注册与世界效果。

下一精确动作：审计并拆分冰枪首击目标集合、定向1×3×1体积/可见性/伤害协调与实体保存字段职责；保留年龄递增时点、12tick历史字段、UUID/资源/同步ID及原伤害权限，不将切片标为整术完成。当前R06-ice_lance仍IN_PROGRESS；GameTest/视觉/联机/性能NOT_RUN，不启动游戏、不自动Git操作。

SOLID审查：LocalEffect不暴露Minecraft实体；IceLanceAdapter承担原版选点/注册职责，Command只协调费用/提交/通知，Preparing处理阶段转换。装配类只是显式依赖注入，不称Factory Method。没有为了复用改变TargetedArea的不可逆void语义。

冰枪首击审查：IceLanceHit仅协调领域筛选，依赖LocalHitTargets窄端口；Adapter转换真实世界候选/可见性/伤害。复用SweptVolume，不引入每术几何引擎或新模式。目标泛型仅隔离原版实体类型，无反射/DSL。实体保存与客户端渲染仍由后续兼容载体/阶段承担，不能称全局SRP完成。

冰枪组合与旋转边界合同已通过，仅关闭已迁切片。冰霜击目前仅数值/纯冻结函数，尚无新Command/Adapter接入；下一步复用既有Factory Method与Command，PhaseTwoEntity初始化差异应由真正Adapter转换，不增加新模式或空工厂。

## 冰霜击准备/释放检查点（2026-11-11）

ProjectileComposition已显式绑定IcicleBreakSpec.PROJECTILE，沿用CastSpellCommand、ProjectileSpellFactory、Preparing/Idle State及成功后Observer。Binding注入真实ProjectileSpawner供应函数；普通弹体用具名vanilla装配，冰霜击用PhaseProjectileAdapter。供应函数/具名装配不是新增Factory Method模式，真正Factory Method仍为ProjectileSpellFactory.create。

PhaseProjectileAdapter核查计划术ID并通过原PhaseTwoSpells.entity→configure创建实体，保留眼高原点、请求准星、同步Spell、120tick实际预算、原始方向与边界；不调用会改变恒速初始化的普通shoot。旧直接执行入口仍保留原实体构建路径，不增加第二份扣费。

完整离线build通过：54单测/99模块测试。新增4项本术合同覆盖160tick准备、成功扣12MP/10tick恢复/3速度/零散布及重复拒绝，false/异常恢复旧快照、不足MP/未学/非法角度拒绝、取消和心跳超时。原有数值/教材/资源/架构检查通过，日志r06-icicle-break-release-build.log及对应JUnit已归档。实际Fabric初始化/世界注册未验收。

当前R06-icicle_break仍IN_PROGRESS；下一动作迁120tick飞行预算与逐发UUID命中记录，审计含水属性/水/熔岩/固体碰撞调用链，保留保存加载、原版权限/伤害与命中顺序。准备Command的spawn异常回滚沿用既有弹体契约，不改变其他术语义；不可逆伤害仍不在此spawn阶段执行。世界/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。

SOLID：Binding依赖ProjectileSpawner端口而非假设所有实体都适用vanilla shoot；PhaseProjectileAdapter拥有实际原版configure转换职责，保持端口LSP，不新增每术空工厂。准备和付费仍复用既有State/Command，非重复协调器。旧PhaseTwoEntity飞行/保存尚未剥离，不能宣称全术全迁。

冰霜击飞行审查：预算/接触为纯函数，ImpactHistory为每发独立领域对象，不冒称新State或Strategy。复用WaterPassage和原世界几何，未新增解释器。共享实体集合迁移保持其他术行为，不把集合替换称这些术已迁。实际SavedData/原版碰撞仍需世界验收。

冰霜刃复用现有Preparing State/Command/ProjectileSpellFactory/PhaseProjectileAdapter与Observer，未建新工厂。完整截面世界调用目前仍依赖旧火断几何兼容边界，下一步抽共享算法与分别规格，当前不能称依赖边界全部完成。冰霜击核心组合合同不冒称实体tick/保存集成测试。

截面几何审查：BladeSectionVolume是共享数学算法，不冒称设计模式；原火断core类零职责后删除。游戏FlameSliceVolume仍承担真实坐标/方块包围盒转换职责，冰霜刃直接使用同一共享算法。没有新增DSL/空工厂/每术引擎，固定3×1×1符合两术当前共同合同，不增加未使用配置抽象。测试夹具冻结旧实现仅用于有限迁移差分，不进生产JAR。

共享飞行审查：PiercingFlightRules为纯判定函数，两个实际消费者使用，删除原术内类；不冒称新模式。不新增配置框架或通用效果解释器。准备/释放仍复用原State/Command/Factory/Adapter，世界排序/保存/生命周期尚待退出审计。

水流复用既有State/Command/Factory/Adapter/Observer；湿身纯函数不称模式，击退继续原版API以保留抵抗/速度语义。冰霜刃仅已迁切片通过源码退出审计，世界载体未全迁。

水流飞行复用PiercingFlightRules/ImpactHistory，未新增模式或通用引擎；原版knockback保留在世界边界，不用纯函数冒充真实抗性/世界物理测试。核心组合合同只证明端口协调，实体生命周期与世界验收分开。

冰砦规划是纯格点函数，不称设计模式；原版方向和基点计算留世界边界，避免改浮点取整。独立世界放置与释放尚未迁，不增加空工厂。水流仅已迁核心/协调切片收尾，原世界载体职责仍明确保留。

冰砦Adapter拥有预检计划和实际生成职责，实现现有TargetedArea窄端口；apply不可逆语义与原接口一致，不强塞boolean退款。复用State/Command/Observer，不增加Factory空类。生长容器/租约未迁完，当前不能宣称全术SRP完成。

ColumnGrowth拥有待生成序列与到期规则，Placement隔离世界查询/放置，泛型只隔离BlockPos，不是DSL或新模式。未建每术空工厂；COLUMNS归属仍待迁，不能宣称全局状态清理完成。

冰砦放置协调依赖实际World窄端口，Adapter负责原版数据转换，不新增设计模式。ColumnRuntime按服务器主世界实例归属，静态仅附件类型；其他旧静态运行表仍待迁，不能称全局完成。放置/生长合同不冒称Fabric多服务器运行测试。

爆炸术复用既有State/Command/Factory Method/Adapter/Observer，显式装配，不新增模式。原版TNT世界行为保留；PiercingFlightRules仅复用等价纯判定，不造通用效果解释器。冰砦异常边界保持不可逆操作语义，未把无状态协调器包装成全局single-use。

爆炸触发依赖ExplosionImpact.World窄接口，原版由ExplosionImpactAdapter实现Adapter；纯判定无Minecraft依赖，协调顺序有合同。复用既有模式，不增加空Factory/事件总线/通用效果解释器；到期与流体不伪装成爆炸事件。原版部分副作用异常不提供虚假Undo。

火柱复用Preparing State、CastLocalEffectCommand、Observer与LocalEffect窄接口，FlamePillarAdapter承担Adapter职责；明确显式装配不是Factory Method。首次效果/持续任务/放火纯协调各有独立职责，通过窄接口依赖原版边界，无通用效果DSL/新模式/虚假Undo。BurnRuntime服务器附件与纯Burn任务分开，不能把两个测试任务独立冒称Fabric服务器隔离实测。

火焰放射使用已有Channeling/Idle State，ChannelPayment复用同一信用合同；FlameStream窄端口与FlamethrowerAdapter实现DIP/Adapter，固定通道协调不造新Factory/通用DSL。FlamethrowerHit专属锥形命中，ContactCooldowns独立共享机制拥有身份窗口；服务器附件负责实例所有权，不把共享窗口错误放进每次通道会话。水瀑WaterSource与火焰喷射端口保持独立，无虚假Undo。

风裂复用Preparing/Command/Factory Method/Observer，专属WindSliceHit.Targets窄接口由Adapter提供世界候选；共享ImpactHistory保留身份所有权，未另造AOE解释器/空工厂。候选权限在伤害前批量筛选的时点保留，不以延迟谓词改变可观察顺序。D-006未解决，不将结构等价冒称设定无差异。

风缚先迁无游戏依赖的纯脉冲谓词与常量，旧效果类委托；不为纯计时公式创建空Strategy/Factory。风裂结构切片关闭不隐含D-006设定冲突已解决。

风缚复用Preparing State、单次CastLocalEffectCommand、LocalEffect Adapter与成功Observer；WindBindEffect只协调boolean应用后视觉顺序，不新造Factory/Strategy。真实原版药水及Mixin仍属世界适配边界。

强风数值/弹射为纯函数，不创建无必要的Strategy/Factory；无目标自施术不能假造目标以适配LocalEffect。共享原版移动与药水边界继续由Adapter承担。

强风使用SelfEffect窄端口、独立单次Command和显式Composition；复用Preparing State及成功Observer，不假造locate，不新增无意义Factory。

WindBlastEffect为单术效果顺序协调；Caster窄接口隔离原版动作，未新增Strategy/Factory或效果DSL。Adapter处理原版包和药水，Command处理一次扣费与通知，职责独立。

土枪EarthLanceSpikes仅负责三格预检与放置顺序，Placement隔离世界/SavedData；MagicSpikes保留存档及原版地形边界，不新建Factory或通用地形DSL。

土枪复用LocalEffect Adapter、Preparing State、单次Command及成功Observer；Adapter保留双分支真实语义，不建立通用地形解释器。旧construct暂保留为公开兼容边界，尚待拆分，不视为清理完成。

EarthLanceLift承担单术自身分支动作顺序，World窄接口由Adapter实现；复用既有ColumnGrowth，不重复造生长状态机、Factory或通用地形引擎。

中级治愈使用纯治疗公式、Patient端口及Fabric Adapter，复用State/Command/Observer；不增加治疗Factory/Strategy或高阶治疗范围。

物理盾续期为纯规则，替换协调通过窄World接口隔离世界动作；不添加多余Factory/Strategy，已有Command/State接线后续验证，静态注册容器尚待迁移。

PhysicalShieldRegistry为普通实例容器，由Fabric服务器附件拥有；不是Singleton或Service Locator。不新建静态缓存，原世界适配入口访问归属服务器，核心不依赖Minecraft。

物理盾准备/付款复用已有State/Command/Observer，PhysicalShieldAdapter只适配自身世界动作；续期/替换使用单术核心协调，未引入多余模式。

PhysicalShieldAbsorb为单术动作协调，窄Shield接口隔离容量写入/视觉/实体移除；纯扣减复用EffectRules，不造额外Strategy或重复数值实现。

PhysicalShieldInterception为单术流程协调，Contact窄端口由真实Fabric入口适配；未添加不必要的Strategy、Factory或通用解释器。

PhysicalShieldSelection为单术静态纯选择函数，使用JDK Predicate/ToDoubleFunction接世界查询；近战首交与弹体最近为不同业务规则，不增加自定义Strategy层或泛化碰撞框架。

PhysicalShieldMovement为标量纯函数，无需Strategy或向量新类型；现有世界向量投影保留边界层，避免有限Vec3d校验改变异常/IEEE语义。

PhysicalShieldCollisionAdapter承担Fabric/world边界，PhysicalShieldRuntimeAdapter拥有唯一附件；复用core窄端口，无新Factory/Strategy。PhaseTwoRuntime暂留原调用兼容委托，后续只在调用链全部迁移后清理。

PhysicalShieldRuntimeAdapter承担服务器生命周期和World窄端口实现，SelfEffect直接调用，旧PhaseTwoRuntime保留兼容委托；不新造管理器或重复容器，释放协调继续复用PhysicalShieldRelease。

ExodusFlameSpec复用现有ProjectileSpellFactory/Command/Preparing及VanillaProjectileAdapter，显式composition绑定；无独立空工厂或新State，保留单一生效准备/释放路径。

ExodusFlameHit为单术命中流程协调，Target窄端口隔离Minecraft，保留claim不可回退语义；未用通用效果解释器、额外Strategy或空Factory。

ExodusFlameBurstAdapter隔离世界查询与伤害事件，复用ExodusFlameHit窄Target，不新造历史容器；旧载体委托传live Set，单一历史所有权保持。

TornadoImpactHit为单术命中协调，窄Target接口配真实Minecraft Adapter；伤害/标签/冲量/附效顺序保留，不增加空工厂或自造事务模式。

TornadoImpactTerrain为单术有序地形动作协调，窄World端口读写真实载体计数，保留成功立即提交和异常边界；不另建持久容器或事务模式，不泛化其他法术破坏规则。

FrostNovaHit为单术命中协调，Target窄端口隔离世界，状态快照属于业务语义而非新模式；不增加Strategy/空Factory，不把不可逆伤害包装成可回滚事务。

FrostNovaAdapter实现现有SelfEffect窄端口；Composition复用单次Command/State/Observer，兼容FrostNova只委托同一实现。无新空Factory/持久容器或tick；未知不可逆世界异常不伪造回滚。

爆音冲击波：SonicBlastRules纯几何规则已接兼容入口；无新增模式层、无空Factory；世界命中/地形/付费仍待迁，保持IN_PROGRESS。证据r06-sonic-rules-build.log（63/352）。

爆音冲击波实审：SonicBlastAdapter实现现有SelfEffect，显式Composition装配并复用Command/State/Observer；核心Hit/地形算法依赖实际窄Target/World端口，世界层保留原版API及权限。地形泛型仅让有序候选算法不依赖BlockPos，不做通用魔法解释器或新模式。职责分为锥形数学、伤害协调、先可见快照后破坏、世界适配及付款，未添加每术空Factory。旧兼容入口与共享准备容器仍待后续退出，不宣称全局SOLID完成。18项核心合同及最终r06-sonic-exit-build.log通过，实际世界NOT_RUN。

突风实审：独立BlastAdapter适配既有SelfEffect；Command/State/Observer继续承担付款、准备与提交后通知，未新增模式层。纯锥形算式与窄Target命中协调不依赖游戏；未把热风伪装成弹体。ThermalGust寒冷分支和共享准备容器保留后续迁移职责；不宣称全局SOLID完成。17项核心合同通过（63/387），世界未验收。

寒冷突风实审：BoraBlastAdapter实现SelfEffect，核心Target为伤害/免疫/状态的窄端口；命中协调负责明确顺序，世界API只在Adapter。复用Command/State/Observer无新增模式；既有共享准备容器及ThermalGust fallback仍列残余，不冒称整体SOLID完成。21项合同（63/408）通过，真实世界未验收。

## 准备会话服务器归属切片（2026-11-13）

MagicPreparation进程全局ACTIVE已删除，PreparationRegistry<Pending>由服务器主世界preparation_runtime附件拥有，仅initializer、无persistent/sync/copyOnDeath。initialize在MagicSystem初始化注册附件；原逐玩家cancel顺序保持，停服逐玩家清理后removeAttached，回收剩余引用。所有get/contains/put/remove从当前玩家所属服务器读取。Pending、准备计时、心跳、付款、取消及特殊术分支未改；这不是整个MagicPreparation业务拆分完成。

4项新增模块合同覆盖同UUID两实例隔离、替换/删除身份、清理/重建、取消一个实例不影响另一个。首次测试误认为显式cancel后Idle.tick还应发布CANCELLED；修正为不能READY，保留现有State生产语义，失败日志r06-preparation-initial-failure.log。最终完整build退出0：63单测/412模块测试、15,658旧断言、102有效定义/教材、资源/JAR及架构边界通过，证据r06-preparation-build.log和JUnit汇总/XML。测试只验证核心实例容器及CastingSession组合，真实Fabric附件/停服/跨维/游戏/Mixin/视觉/联机/性能NOT_RUN。

残余矩阵evidence/r06-residual-matrix.md已记录共享Intermediate/PhaseTwo/准备协调等未迁职责，R06仍未整体退出。下一R06-intermediate-runtime先审计共享扫掠/墙流体/未加载/历史/Codec的旧顺序，建立窄协调切片，不改变TNT、水穿透或费用例外。不游戏、不Git。

模式实审：Registry为实例容器，不冒称新GoF模式；服务器附件负责生命周期归属，核心无Fabric依赖。既有Command/State角色未改，未造空Factory。

## 共享弹体终止协调检查点（2026-11-13）

ProjectileTermination已接IntermediateProjectile最后终止分支：流体<=distance且<=wall优先，position→particles→discard；否则wall<=distance且有block时position→wallEffect→particles→discard。核心无SpellId，匿名World保留狱炎碰墙爆发、随后重新读取owner及水炮/大火球blockImpact；异常保留已发生副作用，不自动discard或落入后续分支。无终止才原setPos(to)/traveled累加。命中历史、Codec、区块/年龄、风枪地形先于流体、候选排序等未改，仍待迁。

新增8项模块合同（3边界/含10,000差分、5动作顺序/异常），完整离线build退出0：63单测/420模块测试；旧15,658断言、102定义/教材、资源/JAR及架构边界通过。证据r06-terminal-build.log、JUnit汇总/XML。合同不是世界/Fabric/Mixin/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS，下一精确动作提取最终生物接触循环协调：保持contact重算、狱炎skipContact与历史短路、非狱炎先claim再setPos/hurt、piercing与非piercing各自particles/非piercing重复定位和discard顺序；先扩任务卡允许路径再实现。不启动游戏、不Git。

设计审查：窄World端口为真实世界动作的Adapter边界，纯协调只决定顺序，不添加新模式、通用解释器或空Factory；旧载体剩余职责未清。

## 共享弹体生物接触协调（2026-11-13）

ContactTraversal已接IntermediateProjectile排序后的目标循环，逐目标重算contact，再由skip窄端口保留狱炎历史短路；非狱炎原Set先claim后定位/伤害，狱炎仍由爆发使用原Set。piercing循环前快照；穿透每目标effect后particles，非穿透effect后重复定位→particles→discard→return。核心不分派SpellId，异常保留已发生副作用且不补执行后续动作。候选/排序、世界伤害和爆发、终止、年龄/区块、存档/注册/资源保持。

新增8项核心合同验证顺序、非穿透停止、跳过、后续接触重算、爆发对后续历史的影响、伤害/粒子异常和空列表。完整离线build退出0：63单测/428模块测试，旧15,658断言、102有效定义/教材、资源/JAR及架构边界通过。证据r06-contact-build.log与JUnit汇总/XML。测试桩只验证端口/核心组合，不是实际Minecraft/Fabric/Mixin/伤害/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS。下一精确动作审计并提取候选收集/排序协调，保持各术contact与history求值次序、strict小于墙流体、稳定同距顺序；先更新允许路径和合同再实现。共享载体扫掠/区块/Codec等仍待迁，不能称R06完成。不游戏、不Git。

实审：ContactTraversal负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

候选筛选排序已迁ContactCandidates，保留原筛选表达式及求值次序、实时contact稳定排序，不缓存或新增同距排序键。新增5合同、1000组冻结排序差分；完整离线build通过63单测/433模块测试、15,658旧断言、102定义/教材/资源/JAR。证据r06-candidates-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计命中历史保存恢复及MagicVictims UUID容错、PaidMana/ExpansionBroken与年龄边界，先定义合同再迁移。不游戏、不Git。

实审：ContactCandidates负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

命中历史字符串格式已迁HitHistoryFormat并由MagicFlight委托。保留MagicVictims及Codec.STRING.listOf、先clear再Supplier读取、UUID.fromString容错、原Set身份与部分异常语义。新增5项合同；完整离线build通过63单测/438模块测试、15,658旧断言、102定义/教材/资源/JAR。证据r06-history-build.log及JUnit汇总/XML。真实Codec重载/世界/Fabric/Mixin/视觉/联机/性能NOT_RUN。PaidMana、ExpansionBroken及MagicAge仍由原载体保存，未改默认值或clamp；下一核对并提取这些读取归一化的纯规则及旧默认值合同。R06-intermediate-runtime仍IN_PROGRESS，不游戏、不Git。

实审：HitHistoryFormat负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

共享弹体存档计数边界已迁SavedProjectileCounters，IntermediateProjectile保持原读取默认值及逐字段立即赋值顺序；MagicAge非负无新上限，ExpansionBroken仍clamp0..64，PaidMana仍由StoneCannonRules处理且默认10。未改Codec/保存键/注册/资源。新增4项模块合同及10000组冻结计数差分；完整离线build通过63单测/442模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-saved-counters-build.log及JUnit汇总/XML。实际世界/Codec重载/Fabric/Mixin/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS。下一审计并迁扫掠区块可用性检查，保持枚举顺序、首次未加载退出、狱炎和风枪discard而其他暂停、age仅通过预检后递增；先扩任务卡再实现。不游戏、不Git。

实审：SavedProjectileCounters负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

扫掠区块可用性检查已迁SweptChunkGate，IntermediateProjectile使用窄Loaded回调读取原hasChunkAt，保持x外z内、floor转int再右移、首次未加载退出。失败后才判断狱炎/风枪销毁，其他暂停，成功后才++age；不加载新区块，不改法术/资源/存档。新增4项核心合同及1000组冻结枚举差分；完整离线build通过63单测/446模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-chunk-gate-build.log及JUnit汇总/XML。测试只验证核心枚举与短路，真实Fabric/区块卸载/世界/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计固体碰撞候选收集与首墙/impact选点协调，保留水穿透跳过、接触<=步长才记contacts但contact<wall仍选墙的原分支、同距首个shape和永久地形先于流体；先定义端口及边界合同再迁移。不游戏、不Git。

实审：SweptChunkGate负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

固体接触合并与首墙选择已迁SolidContacts，保持contact<=步长才合并HashMap、contact<wall才更新墙和impact、同距取首个形状、含水过滤和世界枚举不变，地形仍先于流体。新增6项核心合同及1000组冻结差分；完整离线build通过63单测/452模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-solid-contacts-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计流体候选扫描及固体终止交接，保持读取时点、未加载跳过及流体同距优先，先定义端口再迁移。不游戏、不Git。

实审：SolidContacts负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

流体扫描已迁FluidContactScan并由MagicFlight窄World适配。保持loaded→fluid→empty→ignoresWater→water短路、同一FluidState高度读取、逐项Math.min且零距离不提前停止；近零运动和方块枚举仍原世界层。新增5项模块合同；完整离线build通过63单测/457模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-fluid-scan-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一核对已迁核心协调与载体剩余职责，建立完整服务端单步协调及窄世界适配合同，保持年龄、地形、流体、生物、终止顺序；不游戏、不Git。

实审：FluidContactScan负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

ProjectileServerStep已协调寿命和几何准备后的完整服务端扫掠：区块检查→年龄递增→固体和永久地形→流体→生物→边界终止→移动。世界匿名适配仍负责原API和单术差异，不分派到通用解释器；未加载不推进年龄，目标终止或边界终止均跳过后续移动，异常原样传播且不回滚。新增6项核心合同；完整离线build通过63单测/463模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-server-step-build.log及JUnit汇总/XML。首次提取多余括号导致编译失败，已修正并重跑全构建，保留初次日志。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS：寿命、几何选择、世界匿名适配和保存载体仍未全迁。下一明确世界Adapter归属与载体窄状态端口，先审计避免扩大实体公共可变接口；不游戏、不Git。

实审：ProjectileServerStep负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

IntermediateStepAdapter已承接服务端世界单步操作，原命中Set直接传入，年龄和破坏计数及距离通过窄回调注入，实体字段保持私有，无新增公共setter。原区块/年龄/地形/流体/目标/终止顺序、原注册和保存键不变。完整离线build通过63单测/463模块测试、15658旧断言、102有效定义/教材/资源/JAR。证据r06-step-adapter-build.log及JUnit汇总/XML。未新增测试，以原核心合同和编译/基线回归验证适配迁移，不声称世界集成。用户决定全部重构完成后统一游戏实测；世界/Fabric/Mixin/Codec/视觉/联机/性能仍NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，寿命和几何选择/保存载体尚待收尾，下一审计这些剩余职责并形成明确退出清单，不游戏、不Git。

实审：ProjectileServerStep负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

IntermediateStepAdapter.prepareAndApply已承接原寿命检查与扫掠几何准备，载体tick保留super/removed、客户端轨迹，服务端只注入原命中Set和私有计数回调。保留原寿命表达式短路、体积尺寸、速度、区块/年龄/地形/流体/目标/终止顺序，未改变注册和保存格式。完整离线build通过63单测/463模块测试、15658旧断言、102定义/教材/资源/JAR；未新增测试，本批为世界适配迁移并运行原合同回归。证据r06-step-preparation-build.log及JUnit汇总/XML。用户明确全部重构完再统一实机测试；世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一核对载体保存及适配器单术分支归属，补结构退出清单后推进IntermediateSpells命中适配，不能称R06全部完成。不游戏、不Git。

实审：ProjectileServerStep负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

## 中级普通命中协调（2026-11-13）

IntermediateHit 纯协调与 IntermediateHitAdapter 已接 IntermediateSpells.hurt；风枪龙卷仍使用独立 TornadoImpactHitAdapter。保持伤害成功后才判断击退并读取 motion、附效时才读取 owner，按 hurt→条件 knockback→effect→afterHit 原顺序执行。失败立即停止，异常原样传播、不回滚、不补调用。8项模块合同验证成功/拒绝、零击退、伤害后状态读取与三阶段异常截断。

完整离线 build 退出0：63单测/471模块测试，旧15,658断言、102有效定义、39/102教材、旧玩家存档fixture、资源与JAR/架构边界通过。证据 r06-intermediate-hit-build.log 及对应JUnit汇总/XML。端口测试不等于真实Fabric伤害/事件集成；游戏/Codec重载/视觉/联机/性能 NOT_RUN，全部重构完成后统一游戏测试，不启动游戏、不Git。

共享弹体运行时 STRUCTURE_SLICE_DONE，退出归属见 evidence/r06-intermediate-runtime-structure-exit.md；整体R06未完成。当前唯一任务 R06-intermediate-effects IN_PROGRESS。下一精确动作：审计 IntermediateSpells.execute 的剩余 TARGET_AREA fallback 与 blockImpact 其他输入fallback的真实调用及兼容合同，再拆范围命中/可见性世界适配；禁止仅凭注册路径无调用删除兼容行为。

模式实审：IntermediateHit只封装命中成功后操作顺序，Target为窄边界；IntermediateHitAdapter实现该边界，属于Adapter的实际用途。未引入新模式、空Factory或通用解释器。单术特殊行为继续显式委托。

## 中级范围结算和世界效果结构切片（2026-11-13）

DeferredAreaImpact纯核心负责按查询顺序筛选、wet快照、伤害及效果/事件，全部命中完成后才依次encase，最后burst。IntermediateAreaAdapter承接原12格加载检查/选点/半径6/效果及地形；保持兼容其他输入和water分支，不改数值。9项模块合同覆盖多目标先命中后造冰、筛选/伤害失败、湿身快照、效果决定入队、空目标和事件/效果/地形异常截断。

SpellVisibilityAdapter承接原加载采样和COLLIDER/NONE射线；9个已迁Adapter直接调用它，未迁世界类保留IntermediateSpells.visible委托。IntermediateImpactAdapter保留水炮/大火球/其他输入碰块分流及原读取时点，视觉burst后才读取owner播放音效。未扩大对其地形/视觉的测试声称。

完整离线build退出0：63单测/480模块测试、15,658旧行为断言、102定义和39/102教材、旧存档fixture、架构/资源/JAR检查通过，证据r06-intermediate-area-build.log及JUnit汇总/XML。游戏/Fabric/Codec重载/视觉/联机/性能NOT_RUN；按用户要求全部重构完成后统一实测。不启动游戏、不Git。

R06-intermediate-effects结构切片STRUCTURE_SLICE_DONE，不等于R06整体退出。IntermediateSpells保留弹体构造/执行分流及hurt分流；发布装配统一归R06-release-routing，实体核心/注册ID/资源未变，icicle_field不因此记为整术迁移完成。下一唯一任务R06-phase-two-runtime：先审计PhaseTwoEntity服务端tick、扫掠/停留/保存及已迁单术端口，建立职责清单后拆分；R07领域/障壁的未迁业务仍随单术处理。

## PhaseTwo服务端预检和分派协调（2026-11-13）

PhaseTwoServerTick纯核心已接PhaseTwoTickAdapter与原实体tick，保留super/updateBounds先行、低于世界discard、当前区块检查、owner读取、stationary无owner暂停、移动ahead检查、寿命检查、age递增及shield→stationary→fall→fly顺序。stationary重复读取保持原次序；不新增isRemoved短路。适配器注入age/remaining/anchor快照及窄回调，不增公共setter。落石内部第二次loaded失败仍发生在age递增之后；客户端/存档/注册/资源不改。

新增12项模块合同，完整离线build退出0：63单测/492模块测试、15,658旧行为断言、102有效定义与39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-tick-build.log和JUnit汇总/XML。核心端口合同不是Fabric/真实世界/Codec重载/事件/视觉/联机/性能验收，以上NOT_RUN。用户要求全部重构完成后统一游戏测试，不启动游戏、不Git。

R06-phase-two-runtime仍IN_PROGRESS：stationaryTick、fall、fly的内部世界业务和共享候选/终止协调、保存归一化尚待逐项迁移。下一精确动作审计fly候选过滤/排序及接触循环，用已有ContactCandidates等核心时保持PiercingFlightRules与其他分支求值次序，尤其爆炸contact后的动态piercing重读与风裂独立AOE历史。先扩任务卡再实施，不因本分派切片关闭整个R06。

实审：PhaseTwoServerTick负责执行顺序，World为其真实依赖的窄端口，PhaseTwoTickAdapter隔离Fabric实体/区块查询与状态回调。未新增自创模式、空Factory或通用魔法解释器；单术业务未混入核心分派。

## PhaseTwo飞行/驻留/落石结构切片（2026-11-13）

PhaseTwoFlightAdapter/StationaryAdapter/FallAdapter已承接原世界操作；载体只委托并注入原ImpactHistory、私有计数回调与方向/age快照。候选复用ContactCandidates筛完后实时contact稳定排序，保留历史求值次序；PhaseTwoContactTraversal先claim，爆炸定位后读取piercing→explode→重读piercing，不额外burst/discard；普通命中effect→burst→piercing→条件discard。风裂AOE继续使用同一ImpactHistory。PhaseTwoTermination保留流体同距优先、爆炸墙前0.02定位后explode、普通墙burst后discard。存档age/remaining复用原等价非负规则，默认值及逐字段赋值、历史最后替换保持。

18项新增模块合同（接触10、终止8），完整离线build通过：63单测/510模块测试、15,658旧断言、102有效定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-runtime-build.log及JUnit汇总/XML。结构切片不代表真实世界/Codec重载/视觉/联机/性能验收，以上NOT_RUN。用户授权持续执行全部重构，全部完成后统一游戏测试；不启动游戏、不Git。

R06-phase-two-runtime结构切片STRUCTURE_SLICE_DONE：实体同步/Codec/客户端与体积身份仍是载体，新增Adapter的世界API/单术分支继续依赖各自已有核心规则。几何身份配置与零引用兼容最终归R10，不能称整个R06完成。下一R06-phase-two-effects：迁damage/areaDamage的权限→伤害→附效→hook→洪流击退协调及共享加载/射线适配；准备/发布装配仍后续专卡。未把R07/R08未迁法术计为已完成。

## PhaseTwo共享伤害与目标查询结构切片（2026-11-13）

PhaseTwoHit和PhaseTwoDamageAdapter承接权限短路→伤害→附效→hook→洪流条件击退，读取spell/owner/level的时点保持。6项模块合同验证顺序、拒绝及异常；areaDamage仍逐目标可见后伤害，末尾一次粒子。PhaseTwoTargetingAdapter承接aim/aimedEntity/trace/traceFrom，loaded复用SweptChunkGate.available（传入回调的是块坐标）。trace的同距后者胜与aimedEntity的同距首者胜保持独立。首轮编译接口名误写loaded已修复，失败日志留在工作目录。构造/预检/释放、plan/construct/permanentStone/launch兼容世界入口归后续release-routing及R10审计，未标整阶段完成。

完整离线build退出0：63单测/516模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-effects-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R06-channel-runtime，拆服务端通道容器/准入/初次提交与tick适配，保留水源首次成功和火焰先登记再发射的顺序。

## 持续通道运行时归属与首次提交（2026-11-13）

ChannelRuntimeAdapter承接原服务器附件容器、资格预检、heartbeat、begin/stop/tick及兼容generateWater，channel_debt/channel_runtime ID与持久属性不变。ChannelAdmission纯核心保证begin成功→register→emit→sound，6项合同覆盖拒绝/异常及提交后副作用保留。PhaseTwoRuntime保持columns/burns/shield/prune在通道前运行，停服移除其他附件顺序不变。WaterfallChannel/FlamethrowerChannel已有费用/心跳/停止合同继续通过。只是共享通道结构切片，真实附件重载/多服务器集成未验收。

完整离线build退出0：63单测/522模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-channel-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R06-release-routing：清理PhaseTwoSpells剩余构造/计划/释放世界业务，审计已迁R06全部装配和兼容路径，再处理准备资格与通知协调。

## 准备资格和释放适配结构收尾（2026-11-13）

PreparationEligibility纯核心保留counter/silence/玩家状态/GUI/冷却/选择/知识/装槽/MP/事件的原短路顺序，4项合同含13个拒绝位置验证。PreparationRuntimeAdapter持服务器准备附件及原特殊仪式协调，MagicPreparation仅兼容委托；未改变剩余R07/R08特殊分支。PhaseTwoConstructionAdapter/ReleaseAdapter和IntermediateReleaseAdapter承接原构造/兼容释放世界业务，PhaseTwoSpells/IntermediateSpells现仅签名委托/选择。六个无状态查询/仪式服务开放跨包调用，不增加实体公共setter；首次访问编译失败已修复，原失败日志保留。R06的33术装配源码逐项核对见r06-release-bindings.json：这是接线证据，非实际世界验收。已完成R06共享核心/协调结构切片；R10仍须清理兼容批次命名和实例几何身份，所有世界边界NOT_RUN。

完整离线build退出0：63单测/526模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-preparation-routing-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-water_shield：按账本顺序建立逐术卡，审计水幕体积/续期/容量/接触湿身/弹体拦截及全局实例归属后迁独立核心和适配。

## 水盾结构切片及领域注册表服务器归属（2026-11-13）

WaterShieldRules/Contact/Absorb/Selection与WaterShieldAdapter/ContactAdapter/CollisionAdapter已接实际准备付款、实体接触及服务器弹体拦截；保持6MP/100准备/0恢复/80持续、锚点strict<1E-6、容量8、每接触drag后mark/首次claim后湿身、每20tick原Map清理。最近strict<同距首项，流体/地形不改；共享ShieldInterception已由物理盾与水盾双调用，先地形→incoming/absorb→三叉戟类型→反弹/销毁。原资源/ID/数值不变。独立R07-area-runtime把全局ACTIVE改成AreaRegistry服务器附件，仅initializer，五个清理调用传入player/server；6项核心隔离/异常合同不是Fabric附件集成。新添29项合同（水盾命令6、规则/接触/吸收8、选择4、共享拦截5、注册表6），本切片结构离线PASS；实际世界/同服多人/生命周期仍未验收。

完整离线build退出0：63单测/555模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-water-shield-final-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-heat_hand：审计交互权限、工具/方块处理、持续资格与付款/准备，提纯规则及独立世界Adapter；其后按账本heat_island/burning_place推进。

## 灼热手核心规则与世界适配（2026-11-13）

HeatHandRules保存命中面2×2 a外b内轴映射及冰/蒸发转化，HeatHandMelt保存所有格非短路setBlock后读取warmth、changed短路frozen再温暖及末尾逐格粒子顺序；HeatHandAdapter保存4格OUTLINE及权限，兼容入口委托。SelfEffectComposition接独立2MP/100准备/0恢复命令。新增14项模块合同（8规则/协调、6付款/取消/部分异常），原200温暖/方块资源数值不改，无Recipe/工具分支凭空新增。世界方块权限/环境属性仍未实机验收。

完整离线build退出0：63单测/569模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-heat-hand-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-heat_island：拆半径3区域温暖、清湿身及固定锚点续期，保持200持续和现有效statusTicks；随后燃烧领域。

## 地热区域规则与独立释放（2026-11-13）

HeatIslandRules保持目标getX/Z到领域中心的半径3含边界判断，10000随机冻结算式差分通过；HeatIslandAdapter先removeWet后MagicEffects.warm，非攻击不新增PVP过滤。6MP/100准备/0恢复和独立SelfEffect命令已接，200领域及原statusTicks保持。新增10项模块合同（规则/顺序4，准备/付款6）。共享Area实体owner/剩余/加载和原地续期职责仍后续公共生命周期任务，不把领域世界行为称已实测。

完整离线build退出0：63单测/579模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-heat-island-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-burning_place：抽每目标接触/权限/可见/claim/伤害/点燃事件顺序，保留原全身接触体和20tick尝试冷却。

## 燃烧领域贴身接触协调（2026-11-13）

BurningPlaceContact/Adapter保留history→全身接触→权限→原clip可见→claim→hurt→effects→hook，伤害拒绝仍消耗本目标本次20tick尝试，异常不回滚claim。贴身owner包围盒inflate(.12)、自身排除与PVP、原无chunk采样的可见射线不变。10MP/100准备/0恢复独立命令已接，80持续/2伤害/原随机点燃不变；删除原实体零调用的局部几何/可见帮助函数。新增14项模块合同（8命中协调、6付款），共享Area生命周期仍单独收尾。

完整离线build退出0：63单测/593模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-burning-place-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-magic_shield：抽魔法直伤允许事件/容量吸收及独立准备付款，随后icicle_field；共享Area生命周期另卡处理。

## 魔法盾直伤边界与共享容量吸收（2026-11-13）

魔法盾独立SelfEffect/准备付款，原伤害事件位置委托懒查询适配。仅已知魔法Projectile直接伤害进入盾检查；有同level有效盾则吸收并拒绝伤害。三盾共享容量扣除→显示→重新读取容量→移除协调，保留异常副作用和8点容量，不共用几何资格。8MP/100准备/0恢复/100持续不变。

完整离线build退出0：63单测/603模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-magic-shield-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R07-icicle_field独立领域选点、湿身快照和延迟冰封；随后共享领域生命周期。

## 冰霜领域独立选点与延迟冰封（2026-11-13）

IcicleFieldRules/Adapter接入TargetedArea Command及Preparing State；保持14MP/5伤害/12选点/6半径和旧paidMana，未生成飞行实体。复用DeferredAreaImpact，湿身先快照，hurt成功才附效及移湿并hook，全部命中完成再冰封，最后burst。新增6项边界/异常合同及10000几何差分。首次测试错误使用禁止NaN的Vec3d已改成标量比较合同，失败日志保留。

完整离线build退出0：63单测/609模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-icicle-field-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-area-lifecycle拆短期领域服务端时序，随后blizzard_storm。

## 短期领域生命周期与世界接触分离（2026-11-13）

AreaTick抽纯短路生命周期，AreaTickAdapter拥有查询和接触副作用，实体保留私有计数/anchor/接触容器并提供窄State回调。无owner/removed/dead/跨维不减寿命，寿命先减再查chunk；到期不查chunk。位移阈值及NaN比较、follow→pulse→prune→contacts均原序。8项合同覆盖短路/边界/异常；扩展领域仍各自tick和存档，不误套短期规则。

完整离线build退出0：63单测/617模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-area-lifecycle-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-blizzard_storm独立节拍、跟随/adopt和旧保存到期规则，随后squall。

## 吹雪独立节拍与旧到期保存合同（2026-11-13）

BlizzardStormTick/Rules与世界Adapter接入独立SelfEffect和准备；释放首帧立即tick、跟随后adopt，拒绝和异常不增pulse，80tick内8次伤害脉冲。保留water cadence claim→hurt→附效→hook及原可见射线，36MP/2伤害不变。旧ExpansionSpell/Pulse/Expiry键和默认80/0、绝对时间恢复不变。8项模块合同覆盖短路、节拍、异常和整数/long边界。

完整离线build退出0：63单测/625模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-blizzard-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-squall局部降雨生命周期/暴露/时长规则，随后cumulonimbus。

## 局部降雨独立生命周期与暴露规则（2026-11-13）

SquallRules/Tick与独立Adapter接真实SelfEffect/Preparing，LocalMagicRain为兼容API。900/450时长、48格逐段碰撞、16半径、10tick节拍、清火→湿身300不变。RainExposure抽懒半径→chunk→天空→清火→湿身协调。RainExpiry旧键与shorten保留，7项模块合同覆盖短路、时长、暴露、异常。弱实体索引仍为适配器私有，不声称完成真实世界生命周期测试。

完整离线build退出0：63单测/632模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-squall-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-cumulonimbus纯排程/受害冷却/生命周期与世界查询分离，随后lightning/electric。

## 积雨云独立排程与受害冷却状态（2026-11-13）

CumulonimbusTick/Rules/State与世界Adapter、SelfEffect准备接入。保留旧短路和未检查removed的原边界，40tick落雷间隔先写next再清victims，选点失败仍消耗本次落雷；80tick受害尝试先claim再cadence/hurt。1200时长/24半径/48高/16随机候选、天空/伤害/视觉与旧保存键默认不变。9项模块合同覆盖排程、短路、状态实例隔离和异常。

完整离线build退出0：63单测/641模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-storm-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-lightning保持remaining15锁点和重复validPoint，抽命中控制与独立付款，随后electric。

## 雷光锁定落点与命中控制分离（2026-11-13）

LightningRules/Impact与独立Adapter/Command装配，保留remaining15锁点、每3tick警示与释放前及命中前二次validPoint；不重新按最终准星定位。220MP/32伤害/64距离/2水平半径32高与自身误伤特例不变。天空→非自身权限→可见→hurt→control→hook原序；纯规则7项合同覆盖短路和边界。实际Fabric伤害及锁点预警视觉未运行。

完整离线build退出0：63单测/648模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-lightning-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-electric抽单目标选择/免疫分支/命中协调，随后earth_wall与earth_fortress。

## 电击独立目标选择与免疫命中分支（2026-11-13）

ElectricRules/Impact与ElectricAdapter接入SelfEffect/Preparing，ThunderMagic仅双术兼容API。32格原逐段碰撞/卸载停止、严格最近<保留首个同距。无目标false可退款；有目标hurt拒绝仍burst，成功immune则缓慢60/1否则玩家40生物60控制，再hook。260MP/10伤害及世界查询原序不变。6项模块合同覆盖命中、拒绝、免疫、异常和距离比较。

完整离线build退出0：63单测/654模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-electric-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_wall有序布局纯化与原租约入口，随后earth_fortress。

## 土墙独立有序布局与租约适配（2026-11-13）

EarthWallLayout纯5×3有序布局接独立Adapter/SelfEffect；保留24格逐段选点、面法向.02偏移、主轴>=同距决策与y外x内顺序。原1200/true租约预检/排队和首格burst不变。4项模块合同包含10000随机方向逐格有序差分，世界权限/租约实际执行尚未游戏验收。

完整离线build退出0：63单测/658模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-earth-wall-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_fortress纯半球壳/门洞/稳定排序与独立适配，随后earth_hedgehog。

## 土堡纯半球壳与门洞有序布局（2026-11-13）

EarthFortressLayout接独立Adapter/SelfEffect，保留y/x/z遍历、9..16平方半径、两格高方向门洞、稳定按层/平方距排序，最多200计划与原2400/false租约。4项模块合同含1000随机方向及9轴向完整有序差分、门洞/空心/不可变性。实际世界租约权限和恢复仍NOT_RUN。

完整离线build退出0：63单测/662模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-earth-fortress-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_hedgehog纯布局/processed状态/命中计数与世界tick分离，随后quagmire。

## 土刺独立时序与命中尝试状态（2026-11-13）

EarthHedgehogRules/State/Tick与世界Adapter/SelfEffect接入，12刺黄金角偏移原精度，400寿命/2tick递进、严格age==i*2不补漏帧。processed先写再世界处理、命中计数先写再hurt，受伤拒绝仍占两次预算。旧Spikes保存键和附件不变，原基地搜索及三层非短路碰撞保留。8项合同覆盖时序、状态隔离、计数与异常。

完整离线build退出0：63单测/670模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-hedgehog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-quagmire表面体积/跳跃cap/免疫附效和领域生命周期，随后magma_gush。

## 泥沼体积、运动限制与免疫接触分离（2026-11-13）

QuagmireRules/Contact/Tick与Adapter/SelfEffect接入；保留半径6与y[-.2,.65]、向上.115限制不下拉、400到期、owner允许受影响/他人权限与可见、首领仅弱慢和普通先慢后束缚。表面.025偏移/MudExpiry与原运动Mixin API保留。8项模块合同覆盖几何、运动、免疫、短路和异常。

完整离线build退出0：63单测/678模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-quagmire-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-magma_gush独立首帧/余焰排程、processed与自伤火焰分支，随后violent_storm。

## 熔岩喷涌首帧与余焰排程分离（2026-11-13）

MagmaGushRules/State/Tick/Hit与Adapter/SelfEffect接入。首帧10伤、20..160八次2伤、>160到期，processed在damage判断前写。保留火cadence先claim再hurt，owner随机采样/抗火检查/300点燃和其他目标附效分支、scorch→tick→burst、旧保存默认及附件。9项合同覆盖排程、几何、命中原序及异常。

完整离线build退出0：63单测/687模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-magma-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：先R07-wind-runtime共享依赖，再按violent_storm/tornado/vacuum_storm/sky_tempest逐术策略和合同，flame_tornado按账本位置。

## 风领域共享生命周期与冲量规则（2026-11-13）

WindFieldTick/State/Rules抽共享生命周期、20tick排程、圆柱和归一化冲量，World Adapter保留原世界查询和领域相消。移动失败只discard不立即删弱状态；pulse更新next在重叠抑制前；接触完再相消。旧WindStart/Expiry/Next/Direction和原注册ID不变。9项模块合同通过，未将五术标完成，逐术Strategy和命中继续。

完整离线build退出0：63单测/696模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-wind-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-violent_storm独立策略/命中协调，然后tornado/vacuum_storm/sky_tempest，其他术按账本。

## 暴风独立策略与领域接触协调（2026-11-13）

ViolentStormPolicy作为真实Strategy持有16×16、owner排除、pulse外推.2及相消资格，独立SelfEffect绑定。WindFieldContact抽excluded→geometry→owner/permission→visible→pulse→immune→move原序，免疫每tick读时点不改。6项模块合同验证自身排除、免疫不挡伤害、异常阻止运动及策略；其余风术仍LegacyPolicy临时路径待逐卡替换，不声明全部完成。

完整离线build退出0：63单测/702模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-violent-storm-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-tornado独立移动/拉拽策略和脉冲协调，随后vacuum_storm/sky_tempest。

## 龙卷独立移动拉拽策略与脉冲命中（2026-11-13）

TornadoPolicy独立5×20移动/每pulse拉拽策略及SelfEffect接入，原首帧不移动、后续.1移动/loaded检查和同owner火龙卷重叠抑制留在世界适配。WindFieldPulse抽claim→hurt→附效→hook→真空慢原序，6项模块合同覆盖拉拽方向、玩家上升cap和伤害拒绝/异常。原数值/资源/保存键不变。

完整离线build退出0：63单测/708模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-tornado-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-vacuum_storm独立策略与拒绝伤害仍慢合同，随后sky_tempest。

## 真空暴风独立策略与拒伤减速合同（2026-11-13）

VacuumStormPolicy独立12×12固定圆柱，无移动/相消，SelfEffect/Preparing绑定。WindFieldPulse保持cadence或hurt拒绝仍加500tick慢，成功hook后慢，异常不继续附效。5项合同验证拒伤、拒cadence、hook异常与策略；260MP/6伤害/300持续及旧Wind保存键不变。

完整离线build退出0：63单测/713模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-vacuum-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-sky_tempest独立中心伤害和每tick切向冲量，随后flash_over。

## 天空风暴中心伤害与切向冲量策略（2026-11-13）

SkyTempestPolicy独立24×40、中心半径8伤7/外4、每tick切向.08上升.04、玩家y<=.35与自身可伤规则。独立SelfEffect，pulse仅相消雨/积雨云，暴风烟/火特例不扩大。4项合同覆盖中心包端点/NaN、每tick移动与360方向切向及玩家限速。620MP/600持续和旧Wind保存键不变。

完整离线build退出0：63单测/717模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-sky-tempest-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-flash_over首帧双伤与余焰独立排程，随后flame_tornado/inferno/absolute_zero。

## 闪燃独立首帧双伤与余焰状态（2026-11-13）

FlashOverRules/State/Contact与Adapter/SelfEffect接入，首帧16爆发和3余焰均执行，余焰八次，flash不claim、pulse先claim火20。flashed和next在世界命中前写，原owner随机/抗火/400点燃和保存键默认不变。共享ExpiringFieldTick仅state/expiry优先的相同生命周期族；8合同覆盖短路、首帧双伤、默认旧状态与异常。

完整离线build退出0：63单测/725模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-flash-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-flame_tornado独立火焰/上升策略并移除临时LegacyPolicy，随后inferno。

## 火龙卷独立火焰与上升策略（2026-11-13）

FlameTornadoPolicy独立5×18移动/火脉冲策略及SelfEffect绑定，上升最终仍用原速度+.06，玩家.35上限。成功hurt后火附效再hook，自身原随机/抗火/500点燃不变。五风术策略全部显式绑定并删除临时LegacyPolicy，公共几何帮助对非风术保留旧默认16。5项模块合同通过，原300MP/7伤害/240持续不变。

完整离线build退出0：63单测/730模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-flame-tornado-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-inferno独立规则、401到期与20次余焰，复用实际相同爆发排程后absolute_zero。

## 炼狱独立规则与真实共享爆发排程（2026-11-13）

InfernoRules/Adapter/SelfEffect接入，首帧18，20..400二十次5伤、401结束，半径20高8和scorch20/2048/401保留。提炼实际相同BurstFieldSchedule/Contact共享时序，闪燃保留兼容核心API并重跑合同。旧Inferno保存键默认及owner随机/抗火/600点燃不变；5新增合同验证独立规则、时序和跳帧不补发。

完整离线build退出0：63单测/735模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-inferno-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-absolute_zero湿身增伤/暖身快照/首次冻结控制与后续冷雾，随后holy逐术。

## 绝对零度首次冻结与冷雾顺序分离（2026-11-13）

AbsoluteZeroRules/Contact与Adapter/SelfEffect接入，首次28+湿6，warm先wet快照；hurt成功才移湿，warm只移暖，否则canFreeze后冻结并控制。后续冷雾无伤，仅无warm/可冻才随机.8；初次拒伤不落入冷雾。160/半径16及freezeSurface16/4096/1200和旧Zero保存默认不变。7合同覆盖资格、快照、控制和异常。

完整离线build退出0：63单测/742模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-zero-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-holy-runtime共享几何/世界适配，随后exorcist_ray/exorcist_burst/exorcist_judge/holy_ray逐术。

## 圣系共享几何与资格适配（2026-11-13）

HolyGeometry与HolyEligibility抽纯柱体/懒资格，HolyInstantAdapter持原plan/instant/visual，HolyMagic兼容API保留TARGETS/DAMAGE身份。SacredNova/DivineJudge仅公开原无状态服务以便跨包调用，无字段setter。3项合同含10000方向投影差分、边界及玩家/死亡/owner/tag读取短路；四即时术尚待逐术策略，不标整体迁移完成。

完整离线build退出0：63单测/745模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-exorcist_ray独立单体策略，随后exorcist_burst/exorcist_judge/holy_ray。

## 驱魔射线独立最近单体策略（2026-11-13）

ExorcistRayPolicy独立单体/窄光束策略和SelfEffect准备绑定，严格最近<同距首个，资格/墙/卸载原序、hurt失败仍成功与原visual，无新增hook。2项合同覆盖最近比较及窄光束参数，四术策略剩余临时分支继续逐卡清理。

完整离线build退出0：63单测/747模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-ray-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-exorcist_burst终点球形/可见伤害协调，随后exorcist_judge/holy_ray。

## 驱魔爆破独立终点球形命中（2026-11-13）

ExorcistBurstPolicy与HolyAreaHit接入终点4半径球、标签资格、形状、可见、hurt顺序；不画beam且hurt拒绝仍成功。3项模块合同覆盖懒读取和异常。

完整离线build退出0：63单测/750模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-burst-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R07-exorcist_judge与holy_ray逐术策略。

## 驱魔审判独立单体策略与接口隔离（2026-11-13）

单体审判策略已绑定；HolyVolumePolicy仅范围术实现形状，HolyNearest严格比较保留同距首个。两项合同及完整构建通过。

完整离线build退出0：63单测/752模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-judge-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成holy_ray独立策略后继续sacred_nova。

## 圣光射线独立柱形策略（2026-11-13）

HolyRayPolicy、HolyRayAdapter接入2半径柱形命中和宽光束视觉；移除HolyInstantComposition最后LegacyPolicy，未支持法术显式拒绝。

完整离线build退出0：63单测/754模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-ray-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成圣域及神之净化共用生命周期适配，继续divine_judge。

## 圣域与神之净化纯规则及适配迁移（2026-11-13）

HolySanctuaryAdapter保留WeakHashMap、NovaExpiry和初始伤害/击退顺序；独立16/48范围规则、自然生成抑制短路、环形节拍已提纯，SacredNova/GodPurge均有SelfEffect绑定。首次编译发现旧包内HolyMagic不可见，改依赖同包HolyInstantAdapter后完整构建通过。

完整离线build退出0：63单测/760模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-fields-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成divine_judge脉冲模块，审计god_purge后继续剩余R07。

## 神之审判三脉冲状态与协调（2026-11-13）

DivineJudgeTick按原次序完成状态/时间/归属检查，提交count后命中；独立规则保留60tick三段、柱体和加载clamp。6项合同覆盖跳帧不补发、异常不重放及短路。

完整离线build退出0：63单测/766模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-divine-judge-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：迁移火土领域五种Strategy，再抽取实际命中与生命周期模块。

## 五种火土领域规则与实际生命周期命中协调（2026-11-13）

五术独立GroundFieldPolicy与SelfEffect已绑定；GroundFieldTick/GroundFieldContact提纯生命周期和命中顺序，原世界/地形/视觉保留于GroundFieldRuntimeAdapter，旧ExpansionFields仅兼容门面。17项领域合同及10,000组火墙差分通过；各术副作用Strategy和地形仍待后续收尾，不能标整项完成。

完整离线build退出0：63单测/783模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-ground-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：烟雾视距状态和五术模块拆分，随后审计火土各术副作用Strategy与地形。

## 烟雾五术生命周期与视距协调（2026-11-13）

FogSight纯化强弱叠加和目标获取；SmokeFieldTick/SmokeContact接入实际五术生命周期，飞行撞击帧不再进入雾分支（原行为保留）。五术各自Strategy和SelfEffect适配，旧SmokeFields保留存档与Mixin入口；12项合同通过。各术效果策略/服务器归属仍待后续结构收尾。

完整离线build退出0：63单测/795模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-smoke-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成ocean_freeze独立扩散/扫描/命中模块后处理模式与障壁。

## 海洋冻结纯状态与命中模块（2026-11-13）

独立OceanFreezeState/Rules/Hit与Fabric适配已接入；半径/扫描/配额/加载clamp、先claim后hurt、UUID容错均保留。6项模块合同通过，真实水面/租约恢复仍未实测。

完整离线build退出0：63单测/801模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-ocean-freeze-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：提纯模式选择及核爆额外费用，再迁移大气/重力/核爆独立运行模块。

## 三种模式术独立规则与持续费用协调（2026-11-13）

ModeSelection及三术规则、MaintainedFieldTick、NuclearExplosionTick、AtmosphereContact、GravityContact均接实际路径。保留核爆额外支付、先schedule后upkeep、费用失败只discard、核爆先标记后targets/terrain及原命中副作用。18项合同通过；world状态/模式attachment隔离与实际费用模块接线仍待后续审计。

完整离线build退出0：63单测/819模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-mode-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：迁移障壁几何与独立模块；随后R08辅助功能及R09/R10收尾。

## 有限障壁容量与移动弹体协调（2026-11-13）

双重盾/灵体封印/拘束各自规则与SelfEffect适配接入；BarrierCapacity、BarrierDamage/Movement/Projectile纯协调保持旧顺序。三叉戟先反弹后击破、保留容量独立与旧存档默认；13项合同通过。供能障壁另拆，真实Mixin和事件未启动验收。

完整离线build退出0：63单测/832模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-barrier-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：龙圣斗气独立后继续城塞/魔神封印供能模块，然后R08其他功能。

## 龙圣斗气独立与核心障壁供能协调（2026-11-13）

龙圣斗气独立adapter和tick规则接入；城塞FortressUpkeep保留真实takeEnergy及100tick断供宽限；魔神封印目标资格与PoweredBarrierTick已抽离。13项合同通过，核心能量和保存键不变。

完整离线build退出0：63单测/845模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-powered-barrier-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成治疗/解毒/同意与声音模块；已发现高阶阵面成功消费接线遗漏，立即修复并补合同后统一归档。

## 治疗解毒支持同意独立模块及阵面回归（2026-11-13）

治疗/初中高级解毒、SupportConsent、SupportRay独立；移除批次执行器内相应业务，保留最近目标后资格判定和非短路移除。补修成功SelfEffect释放后阵面消费，顺序为效果→阵面→通知，失败不消费；13项合同通过。

完整离线build退出0：63单测/858模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-healing-support-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆howling/hypnosis，再处理效用、召唤、通信、转移、幻影。

## 声音效用幻影独立模块及召唤规则（2026-11-13）

Howling/Hypnosis、Disturb/Divination各有独立实际适配与纯协调；幻影保留先付费后schedule的历史差异并接入独立SelfEffect。召唤slot/灯灵信用/尸体expiry/精灵防护规则已提纯；19项合同通过。

完整离线build退出0：63单测/877模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-sound-utility-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续召唤生命周期/归属/付费，随后通信和转移。R09/R10及先前切片结构收尾仍未完成。

## 召唤归属支付生命周期与四术释放端口（2026-11-13）

SummonTick接实际owner/level/alive/expiry/slot校验和schedule→pay→act；四术独立SelfEffect已接入，原UUID/attachment保留。首次跨包编译发现LightLeases窄操作不可见，开放update/release后通过完整构建，未暴露状态。5项合同通过，四术专属AI与世界共享适配仍待结构收尾。

完整离线build退出0：63单测/882模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-summon-lifecycle-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：通信草稿服务器隔离与发送协调，再迁移转移邀请/安全预检/原子移动。

## 通信转移与71项释放绑定真实接线复核（2026-11-13）

通信草稿与转移邀请已由服务器实例持有，纯资格/安全/发送/移动回滚合同接入。审计发现此前部分SelfEffect接线替换静默失败，已以71项显式绑定修复；冰结领域paidMana恢复历史10；高阶成功释放补回阵面消费。结构切片通过，不代表R07/R08共享世界分支或R09/R10全部完成。

完整离线build退出0：63单测/896模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-routing-reconciliation-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：将102术释放绑定覆盖纳入构建，移除局部效果装配条件链，隔离输入会话服务器归属；随后继续逐术世界职责与客户端拆分。

## 102术绑定构建门禁、服务器输入归属与客户端职责拆分（2026-11-13）

构建检查102术唯一释放绑定与完整覆盖，检查实际SelfEffect适配器文件存在；局部5术与选点4术装配改为显式Map。输入会话由overworld运行时附件持有，纯InputSessions保留先提交sequence再限流语义，3项隔离/生命周期合同通过。HUD、知识、轮盘、共享样式、渲染注册、网络传输拆为独立职责，MagicScreens无引用后删除；轮盘几何提核，2项合同覆盖1—10槽中心及边界。实体注册唯一归属MagicEntityTypes，MagicSystem保留18个公开兼容别名及原注册顺序/参数。首次客户端安装因教材样式引用未迁而中止，补全后构建通过；Loom一次缓存锁重建失败，离线重试成功。R09和R10仅部分职责完成，SpellExecutors条件链、共享世界适配、客户端视觉和旧批次类仍需继续。

完整离线build退出0：63单测/901模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-routing-client-ownership-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续102术资格/已付费释放模块注册，替代SpellExecutors条件链；随后逐术世界策略、运行状态归属、视觉采样和旧批次职责清理。

## 执行注册、领域世界归属、纯视觉采样与火土地形策略（2026-11-13）

SpellExecutors改为兼容门面，102项显式资格/已付费释放绑定在启动核查覆盖与重复，保留付费调用边界；源码表达式审计见r10-execution-binding-source-audit.json。18处领域索引改为level附件，障壁/圣域查询显式使用目标world；学习与组合会话由server持有，粒子预算由level持有。附件延迟注册，避免纯函数测试初始化Fabric Mixin；首次verifyMagic发现此问题已修复并全测。无业务依赖的ExampleMixin及配置项删除，其他Mixin保留。70项既有氛围配置移入纯核显式Map，粒子渐隐/成长/漂移/帧和4tick字母采样已接；新增4项合同含40004组角色采样。火土5术的命中/次数/节拍/伤害策略和燃烧/震动/塌陷/落石副作用分离，3项合同；地形开挖/落石填土独立适配，原权限/租约/配额不变。仍有烟雾/模式/障壁/召唤共享分支、旧批次载体/视觉职责、定义规格及完整逐术账本收尾，未完成全部重构。

完整离线build退出0：63单测/908模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-ownership-visual-ground-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分烟雾五术、模式三术及召唤专属世界副作用；继续视觉profile/采样、旧批次职责与逐术账本审计，不启动游戏、不Git。

## 烟雾逐术规格、三模式世界更新与18种载体形状（2026-11-13）

烟雾5术视距/残留/颜色/减速/泥雨束缚/伤害/天空暴露/粒子数量/发射属性由逐术纯规格提供，原class中的术ID分支移除；砂云扫掠与转雾世界算法独立，保持接触当帧return。大气/重力/核爆三术世界更新独立Strategy，生命周期仍共享，模式选择/费用和形状后续继续拆。18种旧批次载体形状移入纯核EffectShapeCatalog，实体不再维护四串形状分支。增加5项模块合同；执行注册表在verifySpellBaseline中实际初始化，资格/已付费释放与视觉绑定覆盖检查纳入architectureTest。已运行完整离线build成功，仍不是实际世界/碰撞/视觉验收。

完整离线build退出0：63单测/913模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-smoke-modes-shapes-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分模式选择/供能费用与边界、召唤AI/建造职责；继续旧批次运行类与客户端视觉模块收尾及102术账本核对。

## 旧批次运行职责及空门面清理、召唤Template Method与模式归属（2026-11-13）

柱体生长队列、火柱锁定灼烧队列、接触节拍索引拆为三个独立运行适配，原PhaseTwoRuntime仅剩明确生命周期协调后改为PlayerSpellRuntime；原附件key与取消/停服入口保留。免疫tag归属MagicControl、Ray归属射线适配、通道类型归属通道模块，调用者直连既有窄适配器后删除PhaseTwoSpells/IntermediateSpells空门面。首次编译补齐建造适配的entity/damage静态依赖后全测通过。召唤用Template Method固定读取指令→指定目标→精灵自动目标→清理无效目标→行为顺序；独立地面导航/精灵飞行/光精灵行为，保留原导航和物理移动，怪物与死灵共享地面机制但绑定各自视觉。模式选择持久化与指令迁至SpellModeSelectionAdapter，核爆冷却归属NuclearExplosionAdapter，原key/copyOnDeath保持。仍有批次实体/渲染/规则/部分适配器及定义/供能/召唤生成等职责未清，整体IN_PROGRESS。

完整离线build退出0：63单测/913模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-runtime-facades-summons-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续形状/模式费用与边界策略、召唤生成及供能职责；移除残留批次规则分支，客户端视觉与完整102术账本最终审计。

## 批次规则与释放分支移除、模式领域规格收尾（2026-11-13）

IntermediateRules与PhaseTwoRules已删除，块爆体积和抛射求解迁入纯核，原断言改为调用真实职责所有者。PhaseTwoReleaseAdapter已移除，其资格与已付款释放直接进入102术显式绑定；实体创建独立EffectCarrierSpawner，冰柱/冰枪保留原trace和放置顺序。持续通道仅保留原有有效角度检查。大气/重力/核爆通过各自世界策略负责选点、就绪及释放通知；供能、跟随、尺寸和核爆球形上下界提为纯规格，生命周期不再判断术ID。新增6项模块合同，当前63/919；全部旧数值、资源、保存键及原版世界副作用未修改。整体重构仍IN_PROGRESS，尚有召唤、障壁、定义及客户端视觉等职责收尾。

完整离线build退出0：63单测/919模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-release-rules-maintained-policy-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分召唤生成/供能/尸体存储与精灵保护，清理IntermediateReleaseAdapter残留分派；随后客户端视觉、定义规格及102术最终账本审计。

## 批次规则与释放分支移除、模式领域规格收尾（2026-11-13）

IntermediateRules与PhaseTwoRules已删除，块爆体积和抛射求解迁入纯核，原断言改为调用真实职责所有者。PhaseTwoReleaseAdapter已移除，其资格与已付款释放直接进入102术显式绑定；实体创建独立EffectCarrierSpawner，冰柱/冰枪保留原trace和放置顺序。持续通道仅保留原有有效角度检查。大气/重力/核爆通过各自世界策略负责选点、就绪及释放通知；供能、跟随、尺寸和核爆球形上下界提为纯规格，生命周期不再判断术ID。新增6项模块合同，当前63/919；全部旧数值、资源、保存键及原版世界副作用未修改。整体重构仍IN_PROGRESS，尚有召唤、障壁、定义及客户端视觉等职责收尾。

完整离线build退出0：63单测/919模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-release-rules-maintained-policy-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分召唤生成/供能/尸体存储与精灵保护，清理IntermediateReleaseAdapter残留分派；随后客户端视觉、定义规格及102术最终账本审计。

## 召唤职责、纯材质动画策略与客户端输入策略（2026-11-13）

召唤尸体记录及选点、实体生成、三类供能策略与精灵保护独立，持久键和实际调用顺序保持。IntermediateReleaseAdapter及零调用IntermediateAreaAdapter删除，原有效逐术入口直连窄适配，生成仅由SweptProjectileSpawner负责。ExtendedSpellMotion已删除；原34术变形按七类材质Strategy抽入纯核MeshMotion及各MeshDeformation，35个ID含unknown×26角色×3阶段×10时间点，共27300组变形/透明度冻结差分精确通过。MagicClient只保留初始化与兼容委托，键位注册、物理输入/UI边界独立；CastingInputState纯状态负责初始松键、打断、两tick连发、模式锁，3项实际接线合同通过。完整离线build通过，整体仍IN_PROGRESS；未声称世界/视觉实际验收。

完整离线build退出0：63单测/923模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-summon-motion-input-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续实体类型身份映射和渲染尺寸/姿态纯规格；拆ReferenceSpellModels资源加载与纯采样，随后障壁、定义规格及逐术账本最终核对。

## 参考模型纯采样、渲染规格与注册身份归属（2026-11-13）

ReferenceSpellModels的透明度和变形已抽到纯核ReferenceMeshMotion，特殊术显式不可变采样策略保留原首个匹配，未擅改原flame_tornado不可达第二分支；20,010组变形/透明度冻结差分通过。渲染尺寸与载体姿态抽为ProjectileMeshSpec，薄片/箭/岩炮/未知默认与竖直落石/静止盾姿态合同通过。基础和扫掠实体的类型→法术映射统一归属MagicEntityTypes，未知类型默认保持，实际注册顺序/ID/尺寸不变。当前63单测/926模块测试，整个重构仍IN_PROGRESS。

完整离线build退出0：63单测/926模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r09-reference-render-identity-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：抽取法术定义隐式分类为纯规格，清理长位置参数及目录职责；随后继续有限障壁和供能障壁逐术策略、残留批次命名与102术账本。

## 纯法术定义、Builder与障壁/建造/静止载体职责（2026-11-13）

SpellDefinition、CastingProfile、MagicCategory的实际类型迁入magic-core，公共全名与record组件不变；SpellTraits独立纯分类规则，102术快照通过。90处长位置构造改为经典Builder具名构建，兼容构造器保留，rank及recovery实际值保持。有限障壁的三种目标/伤害/弹体/生物拦截和亡灵击壳分离为独立Strategy；城塞与魔神封印的生成/付核心能量/维持拆为策略。PhaseTwoConstructionAdapter删除，土枪建造/土柱永久石块/冰城预检/原版物理冲量分别归属专属适配。PhaseTwoStationaryAdapter删除，水瀑/冰枪/火柱静止世界行为显式装配；未知默认接触保持。迁移时两个缺失导入和三个receiver错误由编译发现并修正，最终完整build通过；没有游戏实测或Git操作，整体仍IN_PROGRESS。

完整离线build退出0：63单测/926模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-definition-barrier-stationary-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分扫掠弹体的逐术过期/体积/碰块策略与定向载体飞行专属策略，再移除已清空批次命名；继续视觉目录/定义目录和102术逐项完成证据。

## 逐术飞行策略、纯目录完整合同与机制类型清理（2026-11-13）

SweptProjectilePolicy明确绑定六种扫掠术的寿命/体积/接触/卸载/地形/爆发/碰块能力，CarrierFlightPolicy维护严格接触/截面/爆炸/风切特例；共享扫掠和飞行适配不再持有对应术ID长条件。ExpansionFields/ExpansionBarriers无职责转发已删除。新增纯JDK102术逐字段冻结目录合同及Builder不可变/独立/非法输入合同、飞行边界测试，63/930通过。既有职责拆分后将17个PhaseTwo/Intermediate类型改为SweptSpellProjectile/DirectionalSpellCarrier等真实机制名，完整清单r10-mechanism-class-names.json；原phase_two_spell实体ID、PHASE_TWO兼容枚举、所有附件/存档/网络ID保留。完整离线build通过；仍未完成整体重构，不启动游戏、不Git。

完整离线build退出0：63单测/930模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-flight-mechanism-catalog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续Reference模型资产加载/渲染分离与视觉目录，拆地形租约纯计划/预算和服务端MagicSystem输入网络职责；最后逐术账本、模式审查及R00—R10完成门禁审计。

## 地形纯调度、视觉网格策略、元素目录与领域生命周期（2026-11-13）

MagicSystem服务端网络/动作/会话/生命周期及参考模型资源加载已分责。地形租约采用JDK-only TerrainLeaseScheduler与实例TickQuota，4项合同覆盖恢复优先、失败耗额、过期/延期、不足配额和12次成功组装上限；持久ID/Codec及玩家替换保护保留。40术分层网格生成迁入纯核11类材质Strategy，820组旧新网格逐顶点/颜色精确差分，2项模块合同；39项领域视觉布局纯规格，客户端不再依赖服务端世界适配器。102定义拆10个元素目录，公共常量/ALL顺序和逐字段基线保留。领域实体38项tick/范围/save/load显式绑定，Illusion/CoreSpell/ExpansionSpell及早晚加载顺序保持。20个已无职责领域及单术转发门面删除，真实调用直连；地形注册与权限/临时放置/爆破掉落拆分，expiry通过窄方法封装。全部完整离线build通过；整体最终审计尚未完成，实际游戏NOT_RUN。

完整离线build退出0：63单测/936模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-terrain-visual-catalog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：核对领域绑定兼容语义和视觉布局合同，处理SpellHooks可写静态扩展点及残余职责；完成102术追踪、模式审查、真实文件树与最终门禁。
