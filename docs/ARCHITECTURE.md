# 当前架构与不可变约束

状态：当前有效。更新：2026-11-14。本文合并原意图、边界与模式审查；后续架构修改只更新本文，不重新开启历史任务卡。

阅读顺序：先看职责边界，再按修改范围查模式和约束。文件定位见[实际文件树](reference/ACTUAL_FILE_TREE.md)，逐术定位见[102术索引](reference/MIGRATION_LEDGER.md)。

# 冻结用户意图

intent_revision: 2026-11-11-approved-v2

用户已明确回复“开始重构”。授权执行v2设计的R00—R10，保留原数值、视觉和兼容数据；没有授权启动游戏或Git操作。



| Intent编号 | 用户目的/已确认规则 | 重构后归属及验收 |
|---|---|---|
| I-01 | 102独立术；组合不多算一术；暂缓术不注册 | CatalogContract；法术清单及教材集合测试 |
| I-02 | 设定原MP、伤害、时长、概率、几何、前置与地形特例不变 | 每术spec、差分测试；不按等级统一伤害 |
| I-03 | 恒速穿生物、水、可含水的不完整方块；实墙/熔岩阻挡 | MotionSpec + CollisionPolicy；按碰撞形状和能力判断，不是“在水里就穿” |
| I-04 | 原爆炸多目标TNT等旧特例不套新增整发去重 | ExplosionModule独立命中策略；明确异常规格 |
| I-05 | 强风一次反准星物理弹射，10格标定，缓降5秒；不传送、不漂浮 | WindBlastPolicy；冲量单测、实际位移GameTest |
| I-06 | 燃烧领域包全身、接触生物伤害；风缚有环身风带 | 接触体积spec与绑定视觉profile分别测试 |
| I-07 | 银河字母每4tick一字、左眼斜上漂浮渐隐，粒子脉动、手臂微动 | ChantTimeline/ParticleEnvelope；客户端数值及人工录屏验收 |
| I-08 | 视觉优先原作WEBP、像素3D、github仅辅助；高阶更华丽但预算有界 | VfxSpec引用来源/模型/材质；不以统一术环替代所有独特形态 |
| I-09 | 塞妮丝书102术直接点按钮学，分类教材保留前置和学习谱；轮盘10槽 | BookPolicy策略；服务端实持书核查，不由客户端绕过 |
| I-10 | 服务端决定学习、扣费、目标、命中；GUI/物品动作优先、死亡跨维等打断 | CastingSession、InputGate、应用服务和Fabric适配器 |
| I-11 | 无MP成长；现有恢复和管理员上限不因拆类改变 | ManaPolicy；上限/恢复/扣费边界单测 |
| I-12 | 地形有权限、配额、到期恢复、玩家替换保护，不强载区块 | TerrainLease功能模块及真实方块事件集成测试 |
| I-13 | 核心真实供能、召唤不复制、转移邀请/安全落点、通信签名过滤 | 对应功能模块，不能为易测删掉实际规则 |
| I-14 | 高阶治疗疾病、复活转生、过去转移、龙门、使魔/NPC仍暂缓 | CatalogContract排除；不增加“未来”空实现 |


# 边界与兼容合同

只新增一个纯Java25生产子项目magic-core，根项目继续Fabric；core不能依赖Minecraft/Fabric/Mojang或客户端。明确装配；只在实际变化点使用提案的六个GoF模式，不引入额外框架。

102独立ID与顺序、原39教材白名单、塞妮丝102直接学习、组合ice_lance_blizzard不另注册必须保留。phase_two_spell是实体兼容ID，类可迁移但ID不能改。包名com.example.examplemod与资源命名空间mushokutenseiorigin保留。

资源与模型源基线见evidence/r00-file-hashes.json，原字符串键位置见evidence/r00-compatibility-strings.json；键列表是定位线索，不是完整协议证明。存档字段、网络CODEC的字段顺序和旧reader逐切片核对。原有工作区已大量修改，基线记录当前工作区而非HEAD；不得reset用户改动。

原爆炸TNT强度4与多目标特例、恒速穿水/可含水不完整块、强风一次物理反冲与5秒缓降、全身火焰接触、银河4tick一字左眼斜上、手臂微动必须独立回归。默认MP100，不添加成长或暂缓系统。

游戏不启动，GameTest/视觉/联机/性能记NOT_RUN。离线数值差分不冒充原版集成验收。发布jar需核对核心只打包一份，license和所有实际资源可达。


# SOLID与经典模式最终审查

更新：2026-11-14。仅描述实际职责与调用。纯数学、不可变规格、Map注册表不冒称GoF模式；无需每术复制空工厂或构造通用效果解释器。历史审查保留于[evidence/pre-final-history/architecture__PATTERN_REVIEW.md](archive/refactor-2026-11-14/architecture/evidence/pre-final-history/architecture__PATTERN_REVIEW.md)。

| 模式 | 实际角色与调用 | 合同与边界 |
|---|---|---|
| Strategy | LearnSpell→BookPolicy三种教材；逐术FieldPolicy/FiniteBarrierEffect/PoweredBarrierEffect；MeshDeformation与LayeredPixelMesh.Geometry | LearningModuleTest；SmokeSpecifications/PoweredBarrier/BarrierRuntime；27,300与20,010组动画、820组网格差分。显式Map仅装配策略，不是Factory |
| State | CastingSession持CastPhase；Preparing/Idle/Channeling分别响应tick、heartbeat、cancel | WaterBall/WaterfallChannel/FlamethrowerChannel及ChannelingStateTest；没有enum大switch替代状态对象 |
| Command | PreparationRuntimeAdapter调用CastSpellCommand、CastSelfEffectCommand、CastLocalEffectCommand、CastTargetedAreaCommand；窄端口是Receiver | 成功/重复/无效果退款/未知部分异常/取消合同；不可逆伤害不伪造undo。DetoxificationCommand新增无毒退款合同 |
| Factory Method | 抽象SpellFactory.create；ProjectileSpellFactory覆盖，创建ProjectileBehavior，行为产出ProjectileSpawnPlan | SpellFactoryRegistryTest、WaterBallModuleTest；不为102术新建空Creator |
| Adapter | PlayerStateAdapter、各术SelfEffect/ProjectileSpawner/目标/通道端口实现；GeometryAdapter转换值；Codec单独映射存档 | core生产classpath仅JDK，architectureTest禁游戏依赖；世界权限/实际伤害仍在Fabric边界，未伪造实机验收 |
| Observer | 释放提交后VfxObserver/有序Runnable列表；SpellHooks.HitObserver通过Fabric Event注册 | Command失败不通知；提交回调先于视觉；观察者异常保留付款；命中观察者同步、按注册顺序执行，不再暴露可写回调字段 |
| Template Method | SummonBehavior固定读取指令/选目标/失效清理/perform；CombatSummonBehavior固定move→距离/可见性→攻击，地面与精灵实现movement钩子 | 保持原寻路与物理移动；SummonRules/Lifecycle和源码顺序审计。它是经典GoF，不是自定义框架；真实寻路仍NOT_RUN |

| 原则 | 实际审查结论 |
|---|---|
| SRP | bootstrap仅装配；输入接收/网络注册/会话/生命周期分离；102不可变规格分10元素目录；领域实体为世界载体，生命周期策略显式绑定；模型Repository与绘制、纯采样分开；地形Registry/权限/临时放置/爆破/租约调度分开 |
| OCP | 新术以新增spec/rules/adapter及显式装配为路径；共享Command不按ID增加分支；已登记有限术集的目录更新是装配职责。没有承诺新增任意玩法永远无需改变端口 |
| LSP | 普通弹体用ProjectileSpawner；无效果可拒绝的自身/局部效果有独立boolean端口；持续通道、锁定雷击、注魔走各自协议，不把它们硬塞弹体spawn；各术保留失败/异常与提交顺序 |
| ISP | 核心接口只暴露实际用例所需能力，无通用tick/save空接口；TerrainLeaseScheduler分别用Restoration和Assembly；视觉不读取服务端世界Adapter |
| DIP | 应用Command依赖PlayerStore/SelfEffect/ProjectileSpawner等端口，由Fabric装配注入；纯数学/几何/时序/变形不用Minecraft；游戏实体/Codec对游戏API的依赖留在边界 |

限制：SOLID审查是职责与合同审查，不是数学证明。944项模块测试为纯对象/内存端口集成，不能证明Mixin、世界权限、Codec实际重载、联网、帧率或像素视觉在游戏中通过。D-005/D-006保持旧行为，未借重构裁决玩法冲突。
