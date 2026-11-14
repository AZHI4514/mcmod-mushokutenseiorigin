# R06-flame_slice：核心、准备与释放切片（离线PASS）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`flame_slice`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


依赖：R05准备/释放状态机及R06基础弹体切片。Intent I-02/I-03/I-07/I-08/I-10；正式规格《魔法-游戏设定》第07术。

- 4MP、4伤害、100tick准备/0恢复；恒速2格/tick、60tick/120格，不受重力/阻力影响。不能套用基础弹体的无限寿命。
- 每个新目标触发水平3×3且竖直为该目标身体高度的AOE；整发记录已结算目标UUID。成功伤害后80%点燃100tick。
- 水/流水及具有WATERLOGGED属性的不完整方块（干燥也可）通过；熔岩及不支持含水的完整截面障碍停止，部分截面遮挡可通过。不是“所有水中的方块都穿过”。
- 从眼部原点沿请求MagicAim完整方向shoot(...,inaccuracy0)，保留继承运动语义，不能用服务端旧旋转的普通弹体adapter替代。
- 旧入口：SpellExecutors→FlameSliceEntity；FlameSliceFlight/Volume/SpellSweep/MagicFlight负责距离、截面和含水碰撞，MagicEffects负责附效。先核对所有委托和旧测试再迁。
- 下一步先写纯合同：恒速与寿命/距离边界、AOE命中集合/去重、斜向完整截面、干/湿含水方块分类、取消/失败回滚、旧save字段重载；新模块只用真实需要的Strategy/Factory/Command/Adapter/Observer，不引入DSL/解释器或空模式类。
- 原实体注册ID、MagicAge/FlameFlight/hit存档字段、资源和已认可动画全部保持。旧注释“首次命中消失”与实现不符，不能按旧注释倒改穿透设定。
- 完整命令：gradlew.bat build --offline；按新失败/修改范围增加必要测试。GameTest/视觉/联机/性能NOT_RUN，不启动游戏、不操作Git。

## 本次实现与审查

FlameSliceSpec/FlightState/Volume/Rules在magic-core承担参数、距离、连续分离轴/截面多边形裁剪、AOE及点燃判断；ImpactHistory封装整发命中身份，先claim再尝试伤害，不因免疫重试；WaterPassage按含水能力分类。FlameSliceVolume旧入口只转换游戏值对象，仍被未迁的icicle_edge使用，暂不能删除。

FlameSliceFlight仅兼容旧Codec和record接口，数值委托核心；MagicAge/MagicVictims/FlameFlight及travelled键不变，旧verifyMagic的真实JSON Codec往返继续通过。UUID快照/恢复由纯测试覆盖；整个实体世界存档重载仍NOT_RUN。

ProjectileLaunch是实际Strategy：SERVER_ROTATION与REQUESTED_AIM各自提供输入和原版发射调用；bootstrap显式选取，Command无需法术ID分支。火断及冰刃用眼部原点、请求角度和shoot(...,散布0)，未添加shootFromRotation的继承运动。

证据：evidence/r06-flame-ice-build.log（build --offline，exit0），r06-flame-junit-summary.json；verifyFlameSliceMigration冻结旧算法做1,000组扫掠/截面/阻挡距离/边界差分，原30,000几何差分继续通过。当前18项单测、40项模块测试及15,658旧断言通过。资源哈希、102术参数、39/102教材、旧玩家存档与发布JAR检查通过。

范围外：实际原版伤害/PVP、碰撞回调、实体save/load、Mixin及视觉/联机/性能均NOT_RUN；本切片完成不表示R06或全项目完成。旧SpellExecutors兼容路径到R10统一清理，不能连带删未迁术。下一个R06任务water_cannon，heal/shield等保持所属R08/R07阶段。
