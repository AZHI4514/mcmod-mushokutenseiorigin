# 火焰放射核心与协调退出审计

## 真实入口与节拍
MagicPreparation原160tick准备完成后channel，服务端available仍核查生存/物品使用/睡眠/GUI/维度/已学/装槽/选择/hooks。ChannelRuntime服务器附件保留；旧非waterfall ChannelSession删除，由独立FlamethrowerChannel使用现有Channeling/Idle State驱动。heartbeat前6次不终止，第7次先取消不付款；entry支付→runtime注册→glow→命中/粒子→sound；同术重复输入只更新aim和heartbeat。通道tick与首发仍可同服务端tick发生，未增加延迟或改费用。

## 付款与异常
复用原ChannelPayment，CHANNEL_DEBT的ID/[-19,0] Codec/copyOnDeath保持。6/20 fixed-point每20次付费合计6MP，首发预付后可零MP继续消耗信用，entry至少1MP。emit没有新布尔失败/退款承诺；世界/观察者异常保留付款和已注册通道。stop不清信用，冷却max(existing,10)；tick已stop后runtime移除时再次stop不重复写MP。没有用水瀑目标可用性或每20tick生水节拍替代火焰放射行为。

## 命中、身份与可视输出
Adapter仍从玩家bbox.inflate(16)取allowed LivingEntity原顺序，取目标bbox中心delta，16格/25度锥形→visible→claim→PhaseTwoSpells.damage。claim先于hurtServer实际结果；无敌帧/免疫不免费重试。伤害成功后的MagicEffects/afterHit沿旧路径；未改4伤害、80%点燃200tick。

ContactCooldowns等价持有嵌套UUID表与时间差>=20失效逻辑。玩家UUID窗口在停止/重开间保留；火柱载体使用实体UUID，不与玩家来源混淆。两个真实调用均显式Level上下文；contact_runtime服务器主世界附件不持久化/不同步。每玩家tick仍prune当前服窗口，clear只移除该来源；LEAVE/跨维/重生/停服沿原clear链，停服移除整服附件。时间倒退不提前过期，未重写为计数倒计时。静态SHIELDS等尚未迁。

原八段粒子i=1..15步2、每段3粒、扩散i*.13/速度.01保持，原已加载检测/阻挡射线提前break保持。未改客户端模型/材质、注册/网络/存档ID。

## 证据与剩余边界
63单测/171模块测试与完整build --offline退出0，102有效定义/教材、15,658旧行为断言、资源哈希/许可证/JAR检查通过。新增17模块合同及2000表达式差分采样不等于游戏覆盖率；两个纯ContactCooldowns实例独立不冒称实际Fabric多服务器验收。

核心/通道/命中窗口切片SLICE_DONE；共享世界trace/权限/伤害适配、客户端职责和真实游戏/Mixin/实体重载/联机/性能仍NOT_RUN。R06与整个重构未完成。
