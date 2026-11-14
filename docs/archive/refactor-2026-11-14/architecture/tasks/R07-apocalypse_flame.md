# R07-apocalypse_flame：DONE（代码迁移）

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`apocalypse_flame`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。

Intent：I-01/I-02及逐术正式游戏规格；保留基线费用/命中/取消/权限/兼容字段。

真实纯模块：

- `magic-core/src/main/java/com/example/examplemod/magic/spell/fire/apocalypse_flame/ApocalypseFlamePolicy.java`

行为测试：

- `magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundClaimPolicyModuleTest.java`
- `magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundFieldPolicyModuleTest.java`

命令：`gradlew.bat clean build --offline --no-build-cache --rerun-tasks`；证据`../evidence/r10-final-build.log`。世界/Fabric/重载/视觉/联机/性能NOT_RUN；没有更改数值或资源。
