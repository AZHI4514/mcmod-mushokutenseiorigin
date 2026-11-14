# 系统规格入口

| 规格 | 唯一职责 |
|---|---|
| [魔法目录](magic/INDEX.md) / [共同规则](magic/RULES.md) | 单术效果、数值、判定与特例 |
| [施法合同](casting/CONTRACT.md) | 输入、准备、冷却、取消和阶段转换 |
| [学习合同](learning/CONTRACT.md) / [课程关系](learning/CURRICULUM.md) | 教材、学习谱、学习资格 |
| [玩家合同](player/CONTRACT.md) | MP上限、恢复、持久状态 |
| [世界合同](world/CONTRACT.md) | 仪式、供能、地形、传送、通信和召唤生命周期 |
| [视觉目录](visual/INDEX.md) / [公共合同](visual/CONTRACT.md) | 独立单术视觉方案、共享表现与验收约束 |

单术明确覆盖共同规则时只覆盖指定字段；没有写覆盖就引用所属系统合同。遇到未决冲突先看差异登记，不按文件日期猜规则。设计卡用Markdown维护，运行仍由Java实现，不在此次文档整理中创建第二套配置加载系统。
