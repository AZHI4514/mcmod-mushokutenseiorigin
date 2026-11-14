# R06-rock_bullet：准备/释放切片

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`rock_bullet`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


状态：切片代码及离线验证完成。依据《魔法-游戏设定》第04术及构造后参数快照；intent I-02/I-08/I-10。

- 3MP、4伤害、1.5格/tick、重力.05、击退0，100tick准备及0恢复；不设时间/距离上限。
- 独立RockBulletSpec供旧SpellDefinition桥接；ProjectileComposition显式绑定原实体ID，State/Command/Factory/Adapter/Observer复用已验证普通弹体合同。
- 核心测试先于路由编写：成功一次、精确费用、失败/异常回滚、不通知、知识/MP/非法瞄准、取消/心跳超时。
- 仍由WaterBallEntity负责原版碰撞和伤害，成功伤害后才调用原版knockback；抗击退、无敌帧、PVP及存档重载GameTest均NOT_RUN，不用核心测试冒充。
- 保留eyeY-.1、服务端旋转、散布1、继承运动、空气阻力及未加载区块挂起；资源/视觉/存档字段未改。
- 验证命令：gradlew.bat build --offline；证据待构建后归档。
- 仅本切片回退，不覆盖原有用户改动。不启动游戏，不操作Git。

验证：完整离线构建PASS，退出码0；见evidence/r06-water-ice-build.log及r06-junit-summary.json。仅本卡所述核心切片完成，碰撞/命中去重/存档重载的原版GameTest、视觉、联机均NOT_RUN。普通弹体不设时间/距离上限，D-004已澄清。
