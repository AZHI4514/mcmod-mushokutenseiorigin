# 项目文档唯一入口

先读[项目导览](START_HERE.md)：用一个水弹例子理解整个项目。

| 层 | 回答什么 | 入口 | 不放什么 |
|---|---|---|---|
| intent | 为什么做、希望什么体验 | [项目目标](intent/PROJECT.md)、[魔法](intent/MAGIC.md)、[学习](intent/LEARNING.md) | 数值表、类名、进度 |
| spec | 游戏必须如何表现 | [魔法目录](spec/magic/INDEX.md)、[系统合同](spec/README.md) | 开发日志、代码路径 |
| impl | 代码怎样实现 | [实现入口](impl/README.md) | 新的玩法决定、进度 |
| development | 每次怎样开发 | [标准流程](development/WORKFLOW.md)、[工程约束](development/ENGINEERING.md)、[模板](development/templates/SPELL.md) | 某一术的具体设计 |
| status | 现在做到哪 | [STATUS](STATUS.md) | 长篇开发经过 |
| logs | 过去发生过什么 | [开发记录](logs/README.md) | 当前规格 |
| reference/archive | 来源、差异、旧证据 | [差异](reference/SPEC_DEVIATIONS.md)、[归档](archive/README.md) | 默认阅读任务 |

## AI最小阅读协议

根AGENTS→本页→STATUS→活动任务（如果存在）；按任务只读相关intent、系统合同、具体法术卡、impl与代码。新开发再读WORKFLOW/ENGINEERING。默认不读取全量102术、不扫描archive、不恢复旧任务卡。

中断恢复用ACTIVE_TASK的决定、文件、证据和下一步；完成后只在STATUS更新当前结果、日志追加过程。规格只有规则改变时才改，禁止附加“本轮已完成/最新进度”。用户最新明确决定优先；冲突不得凭实现倒推设计。

视觉重做入口：[视觉意图](intent/VISUAL.md) → [视觉目录](spec/visual/INDEX.md) → [视觉工作流](development/VISUAL_WORKFLOW.md)。玩法与视觉按同一ID分别维护。
