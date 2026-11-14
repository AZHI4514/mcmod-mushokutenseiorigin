# R06-exit-audit：IN_PROGRESS（共享职责及阶段退出审计）

独立共享任务，非新SpellId；依赖R06逐术已完成结构切片。用户批准提案第10.3节规定R07依赖R06；不得仅因账本逐术无NOT_STARTED便将R06整阶段标DONE。

读取提案R06退出标准、INTENT/BOUNDARIES、MIGRATION_LEDGER及各结构退出证据。定位IntermediateProjectile/IntermediateSpells、PhaseTwoEntity/Spells/Runtime/Rules、MagicPreparation/SpellExecutors、ThermalGust fallback和兼容入口的残余职责。建立旧类→已迁模块→剩余业务→明确后续阶段/任务的矩阵，区分共享载体/Codec的必要兼容与仍未迁的业务分支，不把源码核对当世界测试。

允许架构文档/源码只读审计；先形成证据矩阵及下一具体代码任务卡，再按卡拓展允许修改文件。需要迁代码时保留102定义、原39特例、注册/网络/存档/资源ID、TNT/水穿透/一次弹射/费用例外；不通过大改通用解释器消灭分支。不游戏、不Git。

最小检查：全部R06分配项唯一且有任务卡/证据；未迁共享付款/容器/命中/扫掠/存档归属有可执行任务；最早未完成R06职责先处理，只有有证据满足阶段退出才能进入R07-water_shield（账本首个未开始R07项）。当前63/408基线为r06-bora-build.log，仅上一代码批次验证，不借作新代码通过证据。

下一精确动作：对照提案R06阶段条件和逐术退出清单，枚举共享Intermediate/PhaseTwo/MagicPreparation遗留业务，写残余矩阵并选一个最早未完成的共享职责建立实现任务；不得把本审计等同整阶段完成。撤回只限本批文档差分，保留用户改动。

残余矩阵已形成：evidence/r06-residual-matrix.md。仅审计步骤完成，R06退出未满足；已建立R06-preparation-runtime实现卡，先移全局准备表。
