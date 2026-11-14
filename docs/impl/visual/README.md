# 视觉实现入口

只说明已有代码与资源，不能据此定义最终造型。逐术实现定位从[法术索引](../SPELL_INDEX.md)查；最终目标从[视觉卡](../../spec/visual/INDEX.md)查。

| 职责 | 当前定位线索 |
|---|---|
| 单术渲染/载体 | src/client/.../client/magic；WaterBallRenderer、DirectionalSpellCarrier对应renderer、MagicAreaRenderer |
| 参考网格加载 | ReferenceMeshRepository、ReferenceSpellModels |
| 动画/变形与采样 | magic-core视觉纯逻辑和客户端采样提交 |
| 粒子与咏唱 | SpellAtmosphere、SpellParticleDensity、MagicCastMotion等；先查实际调用链 |
| 可编辑源 | model-source/magic_effects |
| 运行资产 | src/main/resources/assets/mushokutenseiorigin下的models/textures/particles |

这些是查找线索，不保证每术使用同一路径；重做前按ID核对实际绑定。[旧调参与资源快照](PLACEHOLDER_SNAPSHOT.md)供回溯，不能当作最终性能预算或视觉设计。重做允许替换模型、材质和动画实现，但需要独立检查旧资源引用、存档ID和世界判定不被破坏。
