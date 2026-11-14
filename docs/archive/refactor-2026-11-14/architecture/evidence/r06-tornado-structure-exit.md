# 风枪龙卷结构切片退出

2026-11-13，SLICE_DONE仅指Spec/准备/付费释放/命中协调及世界HitAdapter/地形预算/飞行纯规则。新命令分支在MagicPreparation.release后return，旧IntermediateSpells兼容释放不双发；hurt单术Adapter后return，不重复冲量。

地形World匿名适配仍在IntermediateProjectile，只读写载体expansionBroken并调用原ExpansionTerrain；其永久模式、ServerPlayer资格、候选排序、wall/block/impact写入属于共享载体世界扫掠的剩余职责，后续与其他弹体世界适配一起迁，当前不冒充已迁出。实体age/hit/ExpansionBroken保存、原实体注册ID及客户端仍保留；R10必须逐项证明已无业务分支和调用后才能清理旧类。未改水穿透、地形先于流体、64成功计数和原存档键。

证据：r06-tornado-flight-audit.md及完整r06-frost-start-build.log、现有核心合同。实际Fabric/Mixin、原版冲量/权限/掉落、Codec重载、视觉/联机/性能NOT_RUN。不是全术/整阶段完成。下一账本R06-frost_nova。
