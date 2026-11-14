# R06-intermediate-effects 结构退出清单

- hurt：普通命中核心IntermediateHit＋Fabric Adapter；龙卷保留独立核心/Adapter。
- execute范围fallback：DeferredAreaImpact＋IntermediateAreaAdapter；原半径/加载检查/选点及其他输入fallback保留。无新增弹体spawn或费用处理。
- visible：原逐样本加载预检/世界射线迁SpellVisibilityAdapter；旧签名仅兼容委托，不强行抽取Minecraft API。
- blockImpact/impactParticles：IntermediateImpactAdapter保持原分流和执行顺序，实际地形与表现仍待实机验收。
- 原IntermediateSpells剩余：实体构造及释放/命中分流属于R06-release-routing和最终R10兼容清理，不假称已删除所有旧类。
- 模式/SOLID：World窄端口仅暴露真实范围协调所需动作；Adapter隔离游戏API，纯核心不依赖Minecraft。无通用法术解释器、空Factory或新增自定义模式。

## 中级范围结算和世界效果结构切片（2026-11-13）

DeferredAreaImpact纯核心负责按查询顺序筛选、wet快照、伤害及效果/事件，全部命中完成后才依次encase，最后burst。IntermediateAreaAdapter承接原12格加载检查/选点/半径6/效果及地形；保持兼容其他输入和water分支，不改数值。9项模块合同覆盖多目标先命中后造冰、筛选/伤害失败、湿身快照、效果决定入队、空目标和事件/效果/地形异常截断。

SpellVisibilityAdapter承接原加载采样和COLLIDER/NONE射线；9个已迁Adapter直接调用它，未迁世界类保留IntermediateSpells.visible委托。IntermediateImpactAdapter保留水炮/大火球/其他输入碰块分流及原读取时点，视觉burst后才读取owner播放音效。未扩大对其地形/视觉的测试声称。

完整离线build退出0：63单测/480模块测试、15,658旧行为断言、102定义和39/102教材、旧存档fixture、架构/资源/JAR检查通过，证据r06-intermediate-area-build.log及JUnit汇总/XML。游戏/Fabric/Codec重载/视觉/联机/性能NOT_RUN；按用户要求全部重构完成后统一实测。不启动游戏、不Git。

R06-intermediate-effects结构切片STRUCTURE_SLICE_DONE，不等于R06整体退出。IntermediateSpells保留弹体构造/执行分流及hurt分流；发布装配统一归R06-release-routing，实体核心/注册ID/资源未变，icicle_field不因此记为整术迁移完成。下一唯一任务R06-phase-two-runtime：先审计PhaseTwoEntity服务端tick、扫掠/停留/保存及已迁单术端口，建立职责清单后拆分；R07领域/障壁的未迁业务仍随单术处理。
