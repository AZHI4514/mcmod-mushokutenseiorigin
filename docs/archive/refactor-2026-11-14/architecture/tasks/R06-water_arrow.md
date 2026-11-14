# R06-water_arrow：准备/释放及附效纯规则

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`water_arrow`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


状态：切片代码及离线验证完成。依据《魔法-游戏设定》第05术，intent I-02/I-08/I-10。

- 2MP、2伤害、速度2.5F、重力.03；100tick准备/0恢复；无限弹道时间与距离。
- 成功伤害且合法LivingEntity之后，仍只采样一次原版随机数；严格小于.6F触发100tick湿身。
- 独立Spec/ImpactPolicy已桥接旧定义与MagicEffects，共享真实State/Command/Factory/Adapter/Observer；核对阈值上下邻域与免疫输入。
- WaterArrowModuleTest覆盖精确扣费、重复提交、失败/异常回滚、知识/MP/非法瞄准、取消/超时、附效边界。
- 原实体ID、eyeY-.1、服务端旋转/散布1/继承运动、空气阻力、碰撞/存档及资源不改。
- GameTest/视觉/联机：NOT_RUN。验证：gradlew.bat build --offline；证据待归档。不启动游戏、不操作Git。

验证：完整离线构建PASS，退出码0；见evidence/r06-water-ice-build.log及r06-junit-summary.json。仅本卡所述核心切片完成，碰撞/命中去重/存档重载的原版GameTest、视觉、联机均NOT_RUN。普通弹体不设时间/距离上限，D-004已澄清。
