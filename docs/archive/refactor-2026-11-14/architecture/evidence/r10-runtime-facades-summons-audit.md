
## 旧批次运行职责及空门面清理、召唤Template Method与模式归属（2026-11-13）

柱体生长队列、火柱锁定灼烧队列、接触节拍索引拆为三个独立运行适配，原PhaseTwoRuntime仅剩明确生命周期协调后改为PlayerSpellRuntime；原附件key与取消/停服入口保留。免疫tag归属MagicControl、Ray归属射线适配、通道类型归属通道模块，调用者直连既有窄适配器后删除PhaseTwoSpells/IntermediateSpells空门面。首次编译补齐建造适配的entity/damage静态依赖后全测通过。召唤用Template Method固定读取指令→指定目标→精灵自动目标→清理无效目标→行为顺序；独立地面导航/精灵飞行/光精灵行为，保留原导航和物理移动，怪物与死灵共享地面机制但绑定各自视觉。模式选择持久化与指令迁至SpellModeSelectionAdapter，核爆冷却归属NuclearExplosionAdapter，原key/copyOnDeath保持。仍有批次实体/渲染/规则/部分适配器及定义/供能/召唤生成等职责未清，整体IN_PROGRESS。

完整离线build退出0：63单测/913模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-runtime-facades-summons-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续形状/模式费用与边界策略、召唤生成及供能职责；移除残留批次规则分支，客户端视觉与完整102术账本最终审计。
