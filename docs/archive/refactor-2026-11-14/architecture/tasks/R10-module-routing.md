# R10-module-routing：DONE（代码迁移）

授权：src/main 的装配/输入会话及其纯核心、模块测试、build.gradle架构检查与进度证据。
目标：102术必须各有且仅有一个明确释放绑定；重复/遗漏构建失败；局部效果使用现有窄端口显式装配，保留费用和观察顺序。服务器输入会话禁止跨服务器共享。
截至2026-11-14，资格/释放注册、共享世界策略、R09客户端与批次职责清理已完成。最终代码门禁通过，游戏验收仍NOT_RUN。见../FINAL_REFACTOR_REPORT.md；下文为历史检查点，不再是当前待办。不得启动游戏或自动Git。
验收：完整离线build，102绑定覆盖、资源/数值基线、JUnit；世界/Fabric验收仍NOT_RUN。

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
