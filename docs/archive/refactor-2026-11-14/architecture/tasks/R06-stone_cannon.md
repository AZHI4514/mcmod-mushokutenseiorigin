# R06-stone_cannon：注魔/释放/规则切片离线PASS

更新：2026-11-14。代码迁移DONE，离线PASS，游戏NOT_RUN。当前最终模块/测试/适配与视觉证据以[102术追踪](../../../../reference/MIGRATION_LEDGER.md)的`stone_cannon`行和[最终交付](../FINAL_REFACTOR_REPORT.md)为准；下文旧状态仅为历史。


岩炮弹，第21术及注魔专节；intent I-02/I-03/I-10/I-11。准备完成预扣10MP，按ChargeCurve积分追加注魔；松V或MP耗尽发射，取消或生成失败退款。伤害10+floor(max(0,paid-10)/10)，恒速3、120tick、原穿透/含水规则保持。保留CHARGE_MANA/CHARGE_BUDGET、PaidMana、网络与资源ID及视觉。

## 已接通

- ChargeStep：每tick积分差、余额/预扣边界、tick饱和；ChargeSession：独立计时与心跳，11tick无心跳超时。只封装计时，不宣称已迁多态Charging State。
- ChargePayment：初始预扣、追加扣费、取消退款与已付费发射结算；ManaReservation/ChargeReservationAdapter保留附件，PaidProjectile只生成不再次扣费。生成异常保留预扣，供显式cancel恢复，避免未知部分生成误退款。
- StoneCharge不再拥有静态ACTIVE。每个服务器主世界拥有一个非持久化、不同步的StoneCharge附件实例；跨维仍取同一服务器主世界，再核对原维度。初始化注册附件，停止时先取消在线玩家并退款，再clear(server)移除该服务器附件。没有静态服务器索引、没有新增Mixin。
- JOIN无运行会话仍可退持久余额，LEAVE/AFTER_RESPAWN、非法aim、跨维、资格失效、心跳超时入口保持。实际游戏入口未运行，不以代码审查代替验收。

## 验证

完整build --offline退出0；38项纯JUnit、67项核心模块测试通过。费用合同覆盖重复取消、失败恰一次退款、成功不重复扣费、无运行会话余额恢复、异常保留余额；新增连续注魔100MP第68tick耗尽并发射、11tick心跳超时退累计费用后重新开始。既有15,658行为断言、31,000几何差分、102术参数、教材与存档/资源检查通过。

证据：evidence/r06-stone-final-build.log、r06-stone-final-junit-summary.json及stone-final-前缀JUnit XML。

## 切片收尾与后续边界

资格失效的8条件均与真实ChargeSession/ChargePayment组合验证，累计预扣取消恰一次退款；主动注魔20tick后释放恰一次，重试不生成、不再扣费；两个玩家的余额彼此隔离。瞄准有效性、原维度守卫、JOIN/LEAVE/死亡重生/停服/切术入口已做源码审计，真实Fabric入口未运行。MagicAge/PaidMana/MagicVictims/charge_mana/charge_budget未改名或改reader，实体重载依然NOT_RUN。

仅已验证切片完成；世界搜集/排序、持久UUID集合、存档与原版伤害仍在兼容实体/Adapter，不宣称整术全部模块化。State目前仍为准备阶段Preparing/Idle，本术ChargeSession是计时上下文，不冒称GoF Charging State。下一SpellId为ice_pillar，按独立任务卡继续；原版实体/生命周期/游戏视觉/联机/性能NOT_RUN，不启动游戏、不操作Git。

## 本轮规则与适配迁移

StoneCannonSpec接入定义费用与速度；StoneCannonRules接管PaidMana最低10归一化、120tick/低速度过期与直击先后判定；ChargeEligibility接收生命/旁观/物品使用/睡眠/菜单/选中/装槽/知识事实，StoneCharge映射后调用。专用StoneCannonProjectileAdapter只生成已付费实体，不再调用多术execute。原伤害公式已在ChargeCurve，不重复创建；穿水继续原WaterPassage与世界碰撞/流体适配，未增加AOE或地形破坏。

新增5项测试逐一翻转8个资格条件、119/120tick及速度阈值、墙/流体同距优先/历史目标排除、付款归一化/伤害分档、干湿含水能力判断。寿命计数增长、UUID记录与存档、实际权限/无敌帧仍在世界载体，不冒充全迁或实机验证。
