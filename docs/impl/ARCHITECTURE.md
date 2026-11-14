# 代码架构与职责地图

游戏设计不依赖这些类名；下面只解释实现，不规定魔法数值。纯逻辑在magic-core；世界访问在Fabric适配；客户端只表现；bootstrap显式装配。不得引入自创模式或为了套模式加空工厂。

| 位置 | 做什么 | 不做什么 |
|---|---|---|
| `magic-core/.../magic/spell/<元素>/<ID>` | 单术规格对象、纯规则 | 访问Minecraft世界 |
| `magic-core/.../magic/application` | 协调准备、支付、释放和学习用例 | 绘制、直接改方块 |
| `magic-core/.../magic/feature`、`mechanic` | 可复用学习、状态、碰撞等机制 | 吞并所有单术特例 |
| `src/main/.../infrastructure/fabric` | 目标查询、实体、世界操作、权限和持久化适配 | 决定玩家设计目标 |
| `src/main/.../bootstrap` | 把具体法术、端口和策略连起来 | 保存全局玩家状态 |
| `src/client` | 输入、界面、模型、动画和粒子 | 决定伤害或扣费 |
| `src/main/resources`、`model-source` | 运行资源、可继续编辑的模型源 | 引用本机路径运行 |

102个ID及顺序、兼容实体ID `phase_two_spell`、包名与资源命名空间保持。旧存档、网络字段顺序、资源ID改变前需迁移设计。世界里已经发生的伤害不能假装undo；无效果拒绝和未知部分异常分开处理。

## 实际采用的经典模式与SOLID

| 模式 | 实际角色与调用 | 合同与边界 |
|---|---|---|
| Strategy | LearnSpell→BookPolicy三种教材；逐术FieldPolicy/FiniteBarrierEffect/PoweredBarrierEffect；MeshDeformation与LayeredPixelMesh.Geometry | LearningModuleTest；SmokeSpecifications/PoweredBarrier/BarrierRuntime。显式Map仅装配策略，不是Factory |
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

限制：SOLID审查是职责与合同审查，不是数学证明。模块测试为纯对象/内存端口集成，不能证明Mixin、世界权限、Codec实际重载、联网、帧率或像素视觉在游戏中通过。D-005/D-006保持旧行为，未借重构裁决玩法冲突。


图集接口：`Context.getAtlas()`接收逻辑图集ID如`AtlasIds.BLOCKS`；透明渲染纹理使用`TextureAtlas.LOCATION_BLOCKS`。两者虽同为Identifier却不能混用。
