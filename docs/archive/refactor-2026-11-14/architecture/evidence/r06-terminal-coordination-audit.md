
## 共享弹体终止协调检查点（2026-11-13）

ProjectileTermination已接IntermediateProjectile最后终止分支：流体<=distance且<=wall优先，position→particles→discard；否则wall<=distance且有block时position→wallEffect→particles→discard。核心无SpellId，匿名World保留狱炎碰墙爆发、随后重新读取owner及水炮/大火球blockImpact；异常保留已发生副作用，不自动discard或落入后续分支。无终止才原setPos(to)/traveled累加。命中历史、Codec、区块/年龄、风枪地形先于流体、候选排序等未改，仍待迁。

新增8项模块合同（3边界/含10,000差分、5动作顺序/异常），完整离线build退出0：63单测/420模块测试；旧15,658断言、102定义/教材、资源/JAR及架构边界通过。证据r06-terminal-build.log、JUnit汇总/XML。合同不是世界/Fabric/Mixin/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS，下一精确动作提取最终生物接触循环协调：保持contact重算、狱炎skipContact与历史短路、非狱炎先claim再setPos/hurt、piercing与非piercing各自particles/非piercing重复定位和discard顺序；先扩任务卡允许路径再实现。不启动游戏、不Git。
