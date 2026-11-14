# R06-sonic_boom：准备/释放与规格切片（离线PASS）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`sonic_boom`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


真空波，正式《魔法-游戏设定》第20术；8MP、5伤害、160tick准备/10tick恢复；恒速2格/tick、120tick/240格。连续3宽×1高×.25厚扫掠，路径多生物每目标每发一次，单体伤害而非AOE；成功伤害后原版水平击退1，无地形破坏。

先核对IntermediateProjectile的SONIC_BOOM体积、IntermediateSpells.hurt及构造后参数快照，保持眼部请求方向/零散布；水与可含水不完整块干湿均可穿，熔岩/实墙阻挡。使用现有SweptVolume、ImpactHistory/真实需要的纯规则，保留原UUID存档和未加载区块语义；不以改名替代拆职责，不增加空模式类。

先写费用/重复提交/失败取消、扫掠边缘/厚度、寿命、击退及穿透先后合同，再接composition和Adapter。原版抗击退/无敌帧/PVP需GameTest，不能用纯测试冒充。build --offline后归档日志/XML与唯一状态。游戏/视觉/联机/性能NOT_RUN，不启动游戏、不操作Git。

## 本次实际迁移

SonicBoomSpec持有8MP、5伤害、160tick准备、10tick恢复、2速度、120tick寿命及3×1×.25扫掠尺寸/击退1；旧SpellDefinition与世界扫掠引用该规格。准备/扣费/生成失败回滚通过已有真实State/Command/Factory及REQUESTED_AIM装配接入，无新增空模式类。显式Map改为Map.ofEntries支持11个绑定，保持不可变、无全局玩家运行状态。

只完成规格/准备/释放切片：连续扫掠仍用已迁核心SweptVolume，但世界目标搜集/排序、UUID集合和保存、寿命判断、原版击退及伤害仍在Intermediate载体中，未宣称这些全部重构。没有添加碰块爆炸或地形效果，击退仍在成功伤害之后，原抗击退/无敌帧/PVP保持；尚未实际验收。

evidence/r06-sonic-build.log（build --offline，exit0）、r06-sonic-junit-summary.json：28项单测、58项模块测试、31,000几何差分、15,658旧行为断言及102参数/教材/存档/资源/JAR检查通过。新几何测试覆盖宽边缘、宽高外排除和垂直薄截面，不是GameTest。游戏/视觉/联机/性能NOT_RUN。下一项stone_cannon需要独立蓄力状态与预扣退款合同，不能套普通瞬发费用。
