# R06-fireball：准备/释放切片

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`fireball`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


- SpellId: fireball；依赖R05-water_ball；intent I-02/I-07/I-08/I-10。
- spec：魔法-游戏设定.md第02术，冻结有效定义见spell-baseline.json。3MP、3火伤、1.5格/tick基础速度、0.01重力，完整咏唱100tick/恢复0；以正式最新规则核对点燃概率及状态时长，不采用早期“无着火”旧文。
- 旧类/方法：MagicPreparation.request/tick→SpellExecutors.execute→WaterBallEntity.spell/tick/onHitEntity/onHit；MagicDamage/Effects负责原版伤害及附效；实体ID fireball与所有资源路径保持。
- 目标：普通弹体Spec与ProjectileBehavior共用工厂，火系命中策略独立；GameAdapter保留shootFromRotation散布/继承移动，不能误用水弹硬编码EntityType。准备使用真实State，费用由Command统一协调，提交后观察者通知。
- 接口合同：prepare纯计算、失败有明确结果；同请求只提交一次；原版免疫/PVP/护甲/无敌帧依旧由游戏Adapter；粒子/纹理不因抽类改变。
- 允许路径：magic-core spell/fire/fireball及projectile/combat共享切片、火球游戏Adapter/装配、对应旧路由、测试和本任务证据。无关法术不改。
- 先写成功、非法角度/目标、余额不足、取消/超时、生成失败、命中仅一次、保存重载的核心合同；原版伤害、火抗及PVP必须列GameTest NOT_RUN。D-004先对照本地父类源码和spec，不把旧age未更新当正确规则；不能擅自修复数值行为。
- 精确命令：gradlew.bat :magic-core:test :magic-core:moduleTest verifyMagic verifySpellBaseline verifyPlayerSave verifyGeometryMigration architectureTest resourceTest build --offline。
- 证据：evidence/r06-fireball-build.log及JUnit XML；旧入口只切fireball，其他未迁术维持旧路由。回退只撤本卡文件，不覆盖用户原有改动。
- 下一SpellId：wind_ball。游戏与Git仍未授权，不启动、不暂存、不提交。

验证：完整离线构建PASS，退出码0；见evidence/r06-water-ice-build.log及r06-junit-summary.json。仅本卡所述核心切片完成，碰撞/命中去重/存档重载的原版GameTest、视觉、联机均NOT_RUN。普通弹体不设时间/距离上限，D-004已澄清。
