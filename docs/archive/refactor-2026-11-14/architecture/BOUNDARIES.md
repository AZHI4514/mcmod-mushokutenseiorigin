# 边界与兼容合同

只新增一个纯Java25生产子项目magic-core，根项目继续Fabric；core不能依赖Minecraft/Fabric/Mojang或客户端。明确装配；只在实际变化点使用提案的六个GoF模式，不引入额外框架。

102独立ID与顺序、原39教材白名单、塞妮丝102直接学习、组合ice_lance_blizzard不另注册必须保留。phase_two_spell是实体兼容ID，类可迁移但ID不能改。包名com.example.examplemod与资源命名空间mushokutenseiorigin保留。

资源与模型源基线见evidence/r00-file-hashes.json，原字符串键位置见evidence/r00-compatibility-strings.json；键列表是定位线索，不是完整协议证明。存档字段、网络CODEC的字段顺序和旧reader逐切片核对。原有工作区已大量修改，基线记录当前工作区而非HEAD；不得reset用户改动。

原爆炸TNT强度4与多目标特例、恒速穿水/可含水不完整块、强风一次物理反冲与5秒缓降、全身火焰接触、银河4tick一字左眼斜上、手臂微动必须独立回归。默认MP100，不添加成长或暂缓系统。

游戏不启动，GameTest/视觉/联机/性能记NOT_RUN。离线数值差分不冒充原版集成验收。发布jar需核对核心只打包一份，license和所有实际资源可达。
