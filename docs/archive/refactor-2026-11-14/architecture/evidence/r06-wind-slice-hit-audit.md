# 风裂本轮入口与身份审计

- 准备释放：显式ProjectileComposition→既有Command/Factory/PhaseProjectileAdapter→configure，server eye position与请求yaw/pitch不变；旧PhaseTwoSpells默认兼容入口仍保留。
- 飞行：加载检查、保存age/remaining、稳定接触排序、strict contact<wall/fluid及contact<=distance、先claim后命中不变。PiercingFlightRules等价替换三处表达式。
- AOE：真实Adapter先构造原有候选List，bbox.inflate(3)宽相，中心距离平方<=9及allowed筛选，在任何伤害前完成，不延迟allowed到各次伤害之间。核心保留直击对象identity比较或history.claim，再调用原PhaseTwoSpells.damage。没有逐目标visible条件。身份保存MagicVictims reader/writer未改。
- D-006：每个未被claim的新直击仍可再触发范围，而正式表含首个目标措辞。保留现有行为，不算冲突已解决，不收尾本任务。
- 核心测试9项新增，63/180完整离线build通过。首次测试import错误失败有独立日志，修正后通过。真实实体tick/序列化重载/伤害权限/视觉等NOT_RUN。
- 下一步：组合合同、冻结旧序列差分、生命周期退出审计；D-006须单列，不用既有断言总数替代验收。

## 风裂序列差分与生命周期检查点（2026-11-12）

新增付费释放→范围身份记录→历史快照重建组合合同及2000组、每组10次接触的冻结旧算法序列差分。对比有序伤害尝试与最终UUID集合，覆盖重叠候选、先范围命中后直击、接触中途快照重建；全部通过。测试候选由替身提供，不证明真实世界枚举或Codec运行。

实体源码审计：Spell/MagicAge/Remaining/AimYaw/AimPitch/MagicVictims读写未改；未加载当前/前方区块先返回，年龄不推进；age>=saved remaining先discard，随后才++age/fly；风裂无需在线owner才能飞行，伤害仍经原allowed/hurtServer/附效/hooks。客户端轨迹、范围burst及原资源未改。共享实体保存与世界碰撞仍留后续职责迁移，不称全部完成。

完整build --offline退出0，63单测/182模块测试，旧15,658断言和102有效定义/教材、资源/JAR检查通过。证据r06-wind-sequence-build.log、r06-wind-sequence-junit-summary.json及XML。D-006依旧未决，本轮未改变首击/后续AOE玩法。当前风裂仍IN_PROGRESS，不把差分等价写成设定验收。

下一动作：核对风裂剩余共享实体职责与已验证切片退出边界，单列D-006后续决策项；可继续不依赖此玩法决定的风缚入口审计，禁止擅自解决D-006或反改正式设定。游戏/权限/保存重载/视觉/联机/性能NOT_RUN；不启动游戏、不自动Git操作。
