# R06-ice_blade：准备/释放与冻结规则

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`ice_blade`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


状态：准备/释放及冻结纯规则代码与离线验证完成。依据《魔法-游戏设定》第10术，intent I-02/I-08/I-10。

- 5MP、5伤害、速度2.5、重力.02、100tick准备/0恢复；不设弹道时间/距离上限，不穿透。
- 例外发射：眼部原点、请求MagicAim、shoot(...,散布0)，不调用普通弹体shootFromRotation，不人为添加继承运动。初速度后继续保留原版阻力/重力。
- 成功伤害并通过合法目标后，仍只采样一次nextFloat；小于.8、可冻结且无温暖时冻结100tick。EffectRules.freezeTicks供冰击/冰刃复用，避免复制冻结公式；每术仍持有自己的参数。
- 独立模块合同覆盖准备、精确扣费、一次提交、失败/异常回滚、知识/MP/瞄准拒绝、取消/超时、冻结阈值及免疫。
- 原WaterBallEntity负责实际碰撞/伤害/存档，实体ID与资源未改。GameTest/视觉/联机/性能NOT_RUN。
- 验证：gradlew.bat build --offline；结果待归档。不启动游戏，不操作Git。

完整构建PASS（exit0）：evidence/r06-flame-ice-build.log及r06-flame-junit-summary.json。保留原版世界Adapter，不声明游戏实测通过。
