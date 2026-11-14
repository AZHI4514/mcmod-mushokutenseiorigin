# R05-water_ball任务卡

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`water_ball`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


SpellId: water_ball；intent: I-01/I-02/I-07/I-10。
前置R04；现行规格：魔法-游戏设定.md水弹、教材完整咏唱5秒、费用1、后冷却0。发射保持服务器原朝向、原版shootFromRotation继承运动与1.0散布，不擅自换成客户端aim。命中/重载仍使用旧water_ball EntityType载体，由本卡审计，不顺便修旧bug。

旧入口：MagicPreparation.request/tick/cancel → SpellExecutors.execute → WaterBallEntity；新入口：每次准备持有CastingSession、显式构造CastSpellCommand及ProjectileSpellFactory，原版实体生成由适配器完成，成功后通知原视觉/音效观察者。

允许：magic-core领域施法/弹体及应用目录、bootstrap、infrastructure/fabric/magic、水弹切换点及对应测试。保留所有注册/资源/存档/网络ID。

最小验证：100tick完成、heartbeat第11tick取消、明确取消不扣费；余额不足拒绝、生成失败回滚、成功只扣一次、重复提交拒绝、观察者只在成功提交后通知且异常不回滚。测试用真实核心Command/Factory/State和内存适配器，不声称Minecraft集成。

命令：gradlew.bat build --offline。证据evidence/r05-build.log。切换仅water_ball，其他101术旧路径不变；撤回范围为本卡新增文件及MagicPreparation水弹分支，不能撤销其他已迁核心。

下一步：R06-fireball（仅在本卡已审计通过后）。游戏命中、PVP、实体存档和双视角仍NOT_RUN。

状态：准备/释放试点代码DONE，完整构建PASS；evidence/r05-build.log和r05-junit-summary.json。旧实体计时差异D-004保留未修，游戏验证NOT_RUN。
