
## 召唤职责、纯材质动画策略与客户端输入策略（2026-11-13）

召唤尸体记录及选点、实体生成、三类供能策略与精灵保护独立，持久键和实际调用顺序保持。IntermediateReleaseAdapter及零调用IntermediateAreaAdapter删除，原有效逐术入口直连窄适配，生成仅由SweptProjectileSpawner负责。ExtendedSpellMotion已删除；原34术变形按七类材质Strategy抽入纯核MeshMotion及各MeshDeformation，35个ID含unknown×26角色×3阶段×10时间点，共27300组变形/透明度冻结差分精确通过。MagicClient只保留初始化与兼容委托，键位注册、物理输入/UI边界独立；CastingInputState纯状态负责初始松键、打断、两tick连发、模式锁，3项实际接线合同通过。完整离线build通过，整体仍IN_PROGRESS；未声称世界/视觉实际验收。

完整离线build退出0：63单测/923模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-summon-motion-input-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续实体类型身份映射和渲染尺寸/姿态纯规格；拆ReferenceSpellModels资源加载与纯采样，随后障壁、定义规格及逐术账本最终核对。
