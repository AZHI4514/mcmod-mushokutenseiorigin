# 39 术像素立体特效重制

本批替换全部 39 套运行网格、表面材质和 gather/trail/burst 粒子图。
`.bbmodel` 经 Blockbench 5.2.2 / MCP 1.10.1 创建和导出，运行时直接使用导出的顶点与 UV。
参考映射在 `design-specs.json`；实际导出成本在 `pixel-manifest.json`。

## 设计与动画

- 水系：阶梯水体、连续流动带、白色浪边；水幕/瀑布分条流动，洪流有独立波峰。
- 冰系：带实心四棱尖端的晶体、晶簇、逐根生长的冰墙与冰野。
- 火系：明亮内核、橙红外层、向后延展的尾焰；火柱/火环分火舌起伏，上升余烬独立淡出。
- 风系：连续、封闭、有厚度的断环与风刃，不再用光滑球壳代替气流。
- 土系：阶梯岩块、硬边石纹、弹体翻滚及落石碎块；地刺保留实心体积。
- 治疗/解毒：反向转动的断环、独立浮升的十字光点；高级法术增加一层结构。
- 障壁：魔力障壁包围玩家，物理障壁仍为前方厚面板，轮廓符带独立呼吸。

像素材质为重新绘制的四段色板图集，不包含动画影片画面。每术一张 64×1536 PNG，24 个 64×64 帧；UV 手动换帧，纹理不进行双帧透明混合或模糊。每类附属粒子为 24 张 16×16 图，主体仍为三维几何。

原参考中没有同名影片的法术沿用对应元素/用途的近似参考，具体见映射，并不声称每个片段都是该法术的原作演示。

`SpellMeshModel` 驱动游戏中的分部旋转、伸缩、移动、增长与透明度；`SpellVolumeEffects` 驱动成型与命中时序。Blockbench 源文件附带分组循环动画用于编辑预览，游戏不直接加载其关键帧，不能将编辑器预览当作游戏实测。

银河字形从每 8 tick 1 个改为每 4 tick 3 个（2.5 → 15 个/秒）；仍为头部附近随机定点出现，不增加绕身体公转。

## 铁魔法参考

使用本地缓存的 Iron's Spells 'n Spellbooks 官方 GitHub 源码作为直接参考：

- [FireballRenderer 源码 blob](https://api.github.com/repos/iron431/irons-spells-n-spellbooks/git/blobs/350a8e52fc4e46457c8d71f3337f525836b9c969)：立体核心、独立外层、全亮与离散材质换帧。
- [ShieldRenderer 源码 blob](https://api.github.com/repos/iron431/irons-spells-n-spellbooks/git/blobs/51cb1146572063639216a6c74844106aaf888eb9)：障壁本体、边饰与流动表面分层。

本项目为 Fabric / Minecraft 26.1，自行实现对应视觉组织方式；没有接入 NeoForge 的铁魔法依赖，也没有复制其贴图与源码。

## 资源与再生成

编辑源：本目录的 `<spell>.bbmodel`，材质内嵌；外部工作副本在 `D:/BlockbenchAssets/magic_animate_effect_<spell>/`。
运行网格：`src/main/resources/assets/mushokutenseiorigin/models/effect/`。
材质：`textures/effect/surfaces/`；附属粒子：`textures/particle/magic/`。

```powershell
# 要求本机 Blockbench MCP 已启动，以及 Pillow、numpy、bb_client.py。
python tools/build_pixel_magic.py --output <候选目录> --force
python tools/build_pixel_particles.py
.\gradlew.bat build --offline
```

生成脚本只输出候选，需审核后发布，不会自动覆盖外部模型目录。游戏运行时不读取本机绝对路径。

## 验收范围

导出审计应检查 39 个唯一法术、正体积、有限坐标、UV、全部 24 帧材质及粒子引用；Blockbench 多视角截图仅验证模型与贴图。
构建的 `verifyMagic` 验证既有行为规则。未自动启动 Minecraft，第一人称遮挡、多人透明叠加、实际 FPS 与整套特效观感须在客户端验收，不能由离线构建代替。


## 纹理与流式咏唱修订

39 术表面改为 16 像素周期的低反差材质细节：岩石矿物色簇、冰晶阶梯细纹、水纹、火舌、风带与障壁刻线分别绘制，移除原先跨色板的突兀白点与粗斜条。仍用原图集尺寸和 24 帧布局，保持硬边与最近邻采样；模型几何/UV/分组不变。游戏材质、项目 bbmodel 内嵌预览与外部 Blockbench 工作副本同步。

手边聚集粒子从每 4 tick 一个改为每 2 tick 一个，适度扩大散布半径。
银河字母改为每次服务端 preparation tick 精确发出一个，位于头顶上方，固定向上速度 0.035 格/tick、24 tick 寿命，轻微横向漂移、冒出后渐隐；原先每 4 tick 三个的随机簇已替换。随机字母使用现有银河字形图集，不代表输出咏唱原文。


## 实际材质与逐面 UV 修正（当前生效版本）

本节覆盖上文旧材质尺寸与旧字母位置说明。前一轮仅改变原色带的微弱细节，没有修正逐面 UV 拉伸，不能作为视觉效果已完成的依据。

当前 39 术使用 `textures/effect/vanilla_materials/<spell>.png`，渲染器已切换到这个新路径。表面细节来自本机 Minecraft 26.1 游戏资源中的 water_still、fire_0、packed_ice、stone、snow、emerald_block、magma、amethyst_block，按元素重新调色。准确对应关系见 vanilla-material-manifest.json。每个面映射完整 16×16 材质块，图集尺寸 64×384，24 帧；封闭网格顶点、分部名称及动画保持。项目和外部 bbmodel 的 UV 分辨率为 64×16，内嵌第一帧预览已同步。

银河文字：每个有效咏唱 tick 输出一个字母。发射点为眼部中心沿头部朝向左移 0.105 格、沿视线前移 0.24 格；速度为自身左方 0.023、水平前方 0.008、世界向上 0.032 格/tick，寿命 24 tick，随后渐隐。左右方向以人物自身为准，随头部朝向变化，非屏幕固定方向。客户端保留服务端发来的斜向速度。

旧版 build_pixel_magic.py / build_reference_spell_effects.py 生成的是旧 UV/色带，不能直接覆盖本版材质及运行网格。当前材质生成和安装工具为 tools/apply_vanilla_spell_materials.py；只更改既有模型 UV 和纹理，不新建几何。

此轮验证：39 套实际网格与 UV 的离线纹理渲染前后比较；新路径已核对编译后的类；全部纹理、网格与 78 份模型预览一致；clean build --offline 与 7019 条行为断言通过。对照图是离线网格渲染，不是 Minecraft 游戏截图；尚未进行客户端实测。新模型 UV 需要重启客户端加载，现有静态网格加载器不会在 F3+T 时重建 UV。
