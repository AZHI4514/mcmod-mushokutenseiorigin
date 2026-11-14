# 法术实现索引

仅定位代码与测试，不拥有数值或进度。规则见[魔法规格目录](../spec/magic/INDEX.md)。源码移动后更新此索引；测试执行结果只写STATUS/日志。

<a id="water_ball"></a>

## 水弹 `water_ball`

纯逻辑：[WaterBallSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_ball/WaterBallSpec.java)。

行为测试：[WaterBallModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WaterBallModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="fireball"></a>

## 火球弹 `fireball`

纯逻辑：[FireballImpactPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/fireball/FireballImpactPolicy.java)、[FireballSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/fireball/FireballSpec.java)。

行为测试：[FireballModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FireballModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="wind_ball"></a>

## 风弹 `wind_ball`

纯逻辑：[WindBallSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/wind_ball/WindBallSpec.java)。

行为测试：[WindBallModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WindBallModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="rock_bullet"></a>

## 石弹 `rock_bullet`

纯逻辑：[RockBulletSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/rock_bullet/RockBulletSpec.java)。

行为测试：[RockBulletModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/RockBulletModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="water_arrow"></a>

## 水矢 `water_arrow`

纯逻辑：[WaterArrowImpactPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_arrow/WaterArrowImpactPolicy.java)、[WaterArrowSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_arrow/WaterArrowSpec.java)。

行为测试：[WaterArrowModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WaterArrowModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="ice_smash"></a>

## 冰击 `ice_smash`

纯逻辑：[IceSmashImpactPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_smash/IceSmashImpactPolicy.java)、[IceSmashSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_smash/IceSmashSpec.java)。

行为测试：[IceSmashModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IceSmashModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="flame_slice"></a>

## 火断 `flame_slice`

纯逻辑：[FlameSliceFlightState](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flame_slice/FlameSliceFlightState.java)、[FlameSliceRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flame_slice/FlameSliceRules.java)、[FlameSliceSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flame_slice/FlameSliceSpec.java)。

行为测试：[FlameSliceModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlameSliceModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="healing"></a>

## 初级治愈 `healing`

纯逻辑：[ExHealingEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/healing/ex_healing/ExHealingEffect.java)、[ExHealingRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/healing/ex_healing/ExHealingRules.java)、[HealingEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/healing/healing/HealingEffect.java)、[HealingRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/healing/healing/HealingRules.java)。

行为测试：[ExHealingEffectModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExHealingEffectModuleTest.java)、[ExHealingReleaseModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExHealingReleaseModuleTest.java)、[ExHealingRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExHealingRulesModuleTest.java)、[HealingDetoxModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/HealingDetoxModuleTest.java)。

世界适配：HealingAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="water_shield"></a>

## 水盾 `water_shield`

纯逻辑：[WaterShieldAbsorb](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_shield/WaterShieldAbsorb.java)、[WaterShieldContact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_shield/WaterShieldContact.java)、[WaterShieldRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_shield/WaterShieldRules.java)、[WaterShieldSelection](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_shield/WaterShieldSelection.java)。

行为测试：[WaterShieldModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WaterShieldModuleTest.java)、[WaterShieldSelectionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WaterShieldSelectionModuleTest.java)。

世界适配：WaterShieldAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="ice_blade"></a>

## 冰刃 `ice_blade`

纯逻辑：[IceBladeImpactPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_blade/IceBladeImpactPolicy.java)、[IceBladeSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_blade/IceBladeSpec.java)。

行为测试：[IceBladeModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IceBladeModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="heat_hand"></a>

## 灼热手 `heat_hand`

纯逻辑：[HeatHandMelt](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/heat_hand/HeatHandMelt.java)、[HeatHandRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/heat_hand/HeatHandRules.java)。

行为测试：[HeatHandModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/HeatHandModuleTest.java)。

世界适配：HeatHandAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="heat_island"></a>

## 地热 `heat_island`

纯逻辑：[HeatIslandRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/heat_island/HeatIslandRules.java)。

行为测试：[HeatIslandModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/HeatIslandModuleTest.java)。

世界适配：HeatIslandAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="burning_place"></a>

## 燃烧领域 `burning_place`

纯逻辑：[BurningPlaceContact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/burning_place/BurningPlaceContact.java)。

行为测试：[BurningPlaceModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BurningPlaceModuleTest.java)。

世界适配：BurningPlaceAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="detoxification"></a>

## 初级解毒 `detoxification`

纯逻辑：[DetoxificationEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/detox/detoxification/DetoxificationEffect.java)。

行为测试：[DetoxificationCommandModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/DetoxificationCommandModuleTest.java)。

世界适配：DetoxificationAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="magic_shield"></a>

## 魔力障壁 `magic_shield`

纯逻辑：[MagicShieldDamageGate](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/magic_shield/MagicShieldDamageGate.java)。

行为测试：[MagicShieldModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/MagicShieldModuleTest.java)。

世界适配：MagicShieldAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="water_cannon"></a>

## 水炮 `water_cannon`

纯逻辑：[WaterCannonRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_cannon/WaterCannonRules.java)、[WaterCannonSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_cannon/WaterCannonSpec.java)。

行为测试：[FlightPoliciesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlightPoliciesModuleTest.java)、[WaterCannonModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WaterCannonModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="water_splash"></a>

## 水蒸 `water_splash`

纯逻辑：[WaterSplashRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_splash/WaterSplashRules.java)、[WaterSplashSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/water_splash/WaterSplashSpec.java)。

行为测试：[WaterSplashModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WaterSplashModuleTest.java)。

世界适配：WaterSplashAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="icicle_field"></a>

## 冰结领域 `icicle_field`

纯逻辑：[IcicleFieldRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/icicle_field/IcicleFieldRules.java)。

行为测试：[IcicleFieldModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IcicleFieldModuleTest.java)。

世界适配：IcicleFieldAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="ex_flame"></a>

## 大火球 `ex_flame`

纯逻辑：[ExFlameRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/ex_flame/ExFlameRules.java)、[ExFlameSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/ex_flame/ExFlameSpec.java)。

行为测试：[ExFlameModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExFlameModuleTest.java)、[FlightPoliciesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlightPoliciesModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="sonic_boom"></a>

## 真空波 `sonic_boom`

纯逻辑：[SonicBoomSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/sonic_boom/SonicBoomSpec.java)。

行为测试：[SonicBoomModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SonicBoomModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="stone_cannon"></a>

## 岩炮弹 `stone_cannon`

纯逻辑：[ChargeEligibility](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/stone_cannon/ChargeEligibility.java)、[ChargeSession](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/stone_cannon/ChargeSession.java)、[ChargeStep](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/stone_cannon/ChargeStep.java)、[StoneCannonRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/stone_cannon/StoneCannonRules.java)、[StoneCannonSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/stone_cannon/StoneCannonSpec.java)。

行为测试：[ChargePaymentModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ChargePaymentModuleTest.java)、[FlightPoliciesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlightPoliciesModuleTest.java)、[SavedProjectileCountersModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SavedProjectileCountersModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="ice_pillar"></a>

## 冰柱 `ice_pillar`

纯逻辑：[IcePillarRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_pillar/IcePillarRules.java)、[IcePillarSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_pillar/IcePillarSpec.java)。

行为测试：[IcePillarModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IcePillarModuleTest.java)。

世界适配：IcePillarAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="waterfall"></a>

## 水瀑 `waterfall`

纯逻辑：[ConnectedWater](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/waterfall/ConnectedWater.java)、[WaterfallSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/waterfall/WaterfallSpec.java)。

行为测试：[WaterfallChannelModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WaterfallChannelModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="ice_lance"></a>

## 冰枪 `ice_lance`

纯逻辑：[IceLanceHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_lance/IceLanceHit.java)、[IceLanceRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_lance/IceLanceRules.java)、[IceLanceSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_lance/IceLanceSpec.java)。

行为测试：[IceLanceHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IceLanceHitModuleTest.java)、[IceLanceModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IceLanceModuleTest.java)。

世界适配：IceLanceAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="icicle_break"></a>

## 冰霜击 `icicle_break`

纯逻辑：[IcicleBreakSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/icicle_break/IcicleBreakSpec.java)。

行为测试：[IcicleBreakModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IcicleBreakModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="icicle_edge"></a>

## 冰霜刃 `icicle_edge`

纯逻辑：[IcicleEdgeSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/icicle_edge/IcicleEdgeSpec.java)。

行为测试：[IcicleEdgeModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IcicleEdgeModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="flood_flush"></a>

## 水流 `flood_flush`

纯逻辑：[FloodFlushSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/flood_flush/FloodFlushSpec.java)。

行为测试：[FloodFlushModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FloodFlushModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="ice_fortress"></a>

## 冰砦 `ice_fortress`

纯逻辑：[IceFortressPlacement](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_fortress/IceFortressPlacement.java)、[IceFortressSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ice_fortress/IceFortressSpec.java)。

行为测试：[IceFortressModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IceFortressModuleTest.java)、[IceFortressPlacementModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/IceFortressPlacementModuleTest.java)。

世界适配：IceFortressAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="explosion"></a>

## 爆炸 `explosion`

纯逻辑：[ExplosionImpact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/explosion/ExplosionImpact.java)、[ExplosionSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/explosion/ExplosionSpec.java)。

行为测试：[ExplosionImpactModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExplosionImpactModuleTest.java)、[ExplosionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExplosionModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="flame_pillar"></a>

## 火柱 `flame_pillar`

纯逻辑：[FlamePillarBurn](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flame_pillar/FlamePillarBurn.java)、[FlamePillarFirePlacement](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flame_pillar/FlamePillarFirePlacement.java)、[FlamePillarIgnition](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flame_pillar/FlamePillarIgnition.java)、[FlamePillarSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flame_pillar/FlamePillarSpec.java)。

行为测试：[FlamePillarBurnModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlamePillarBurnModuleTest.java)、[FlamePillarFireModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlamePillarFireModuleTest.java)、[FlamePillarModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlamePillarModuleTest.java)。

世界适配：FlamePillarAdapter, SpellRaycastAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="flamethrower"></a>

## 火焰放射 `flamethrower`

纯逻辑：[FlamethrowerHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flamethrower/FlamethrowerHit.java)、[FlamethrowerSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flamethrower/FlamethrowerSpec.java)。

行为测试：[ContactCooldownsModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ContactCooldownsModuleTest.java)、[FlamethrowerChannelModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlamethrowerChannelModuleTest.java)、[FlamethrowerHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlamethrowerHitModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="wind_slice"></a>

## 风裂 `wind_slice`

纯逻辑：[WindSliceHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/wind_slice/WindSliceHit.java)、[WindSliceSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/wind_slice/WindSliceSpec.java)。

行为测试：[WindSliceHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WindSliceHitModuleTest.java)、[WindSliceModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WindSliceModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="wind_bind"></a>

## 风缚 `wind_bind`

纯逻辑：[WindBindEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/wind_bind/WindBindEffect.java)、[WindBindSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/wind_bind/WindBindSpec.java)。

行为测试：[WindBindEffectModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WindBindEffectModuleTest.java)、[WindBindModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WindBindModuleTest.java)、[WindBindPulseModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WindBindPulseModuleTest.java)。

世界适配：WindBindAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="wind_blast"></a>

## 强风 `wind_blast`

纯逻辑：[WindBlastEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/wind_blast/WindBlastEffect.java)、[WindBlastRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/wind_blast/WindBlastRules.java)。

行为测试：[WindBlastEffectModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WindBlastEffectModuleTest.java)、[WindBlastRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/WindBlastRulesModuleTest.java)。

世界适配：WindBlastAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="earth_lance"></a>

## 土枪 `earth_lance`

纯逻辑：[EarthLanceHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_lance/EarthLanceHit.java)、[EarthLanceLift](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_lance/EarthLanceLift.java)、[EarthLanceSelection](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_lance/EarthLanceSelection.java)、[EarthLanceSpikes](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_lance/EarthLanceSpikes.java)。

行为测试：[EarthLanceHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthLanceHitModuleTest.java)、[EarthLanceLiftModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthLanceLiftModuleTest.java)、[EarthLanceReleaseModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthLanceReleaseModuleTest.java)、[EarthLanceSelectionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthLanceSelectionModuleTest.java)、[EarthLanceSpikesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthLanceSpikesModuleTest.java)。

世界适配：EarthLanceAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="earth_pillar"></a>

## 土落弹 `earth_pillar`

纯逻辑：[EarthPillarFall](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_pillar/EarthPillarFall.java)、[EarthPillarSpawn](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_pillar/EarthPillarSpawn.java)、[EarthPillarStone](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_pillar/EarthPillarStone.java)、[EarthPillarStop](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_pillar/EarthPillarStop.java)。

行为测试：[EarthPillarFallModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthPillarFallModuleTest.java)、[EarthPillarHistoryModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthPillarHistoryModuleTest.java)、[EarthPillarSpawnModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthPillarSpawnModuleTest.java)、[EarthPillarStoneModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthPillarStoneModuleTest.java)、[EarthPillarStopModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthPillarStopModuleTest.java)。

世界适配：EarthPillarAdapter, SpellRaycastAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="ex_healing"></a>

## 中级治愈 `ex_healing`

纯逻辑：[ExHealingEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/healing/ex_healing/ExHealingEffect.java)、[ExHealingRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/healing/ex_healing/ExHealingRules.java)。

行为测试：[ExHealingEffectModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExHealingEffectModuleTest.java)、[ExHealingReleaseModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExHealingReleaseModuleTest.java)、[ExHealingRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExHealingRulesModuleTest.java)。

世界适配：ExHealingAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="antidote_ii"></a>

## 解毒II `antidote_ii`

纯逻辑：[AntidoteIIEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/detox/antidote_ii/AntidoteIIEffect.java)。

行为测试：[HealingDetoxModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/HealingDetoxModuleTest.java)。

世界适配：AntidoteIIAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="physical_shield"></a>

## 物理障壁 `physical_shield`

纯逻辑：[PhysicalShieldAbsorb](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/physical_shield/PhysicalShieldAbsorb.java)、[PhysicalShieldInterception](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/physical_shield/PhysicalShieldInterception.java)、[PhysicalShieldMovement](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/physical_shield/PhysicalShieldMovement.java)、[PhysicalShieldRegistry](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/physical_shield/PhysicalShieldRegistry.java)、[PhysicalShieldRelease](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/physical_shield/PhysicalShieldRelease.java)、[PhysicalShieldRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/physical_shield/PhysicalShieldRules.java)、[PhysicalShieldSelection](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/physical_shield/PhysicalShieldSelection.java)。

行为测试：[PhysicalShieldAbsorbModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/PhysicalShieldAbsorbModuleTest.java)、[PhysicalShieldCommandModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/PhysicalShieldCommandModuleTest.java)、[PhysicalShieldInterceptionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/PhysicalShieldInterceptionModuleTest.java)、[PhysicalShieldMovementModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/PhysicalShieldMovementModuleTest.java)、[PhysicalShieldRegistryModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/PhysicalShieldRegistryModuleTest.java)、[PhysicalShieldReleaseModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/PhysicalShieldReleaseModuleTest.java)、[PhysicalShieldSelectionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/PhysicalShieldSelectionModuleTest.java)。

世界适配：PhysicalShieldAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="blizzard_storm"></a>

## 吹雪 `blizzard_storm`

纯逻辑：[BlizzardStormRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/blizzard_storm/BlizzardStormRules.java)、[BlizzardStormTick](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/blizzard_storm/BlizzardStormTick.java)。

行为测试：[BlizzardStormModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BlizzardStormModuleTest.java)。

世界适配：BlizzardStormAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="squall"></a>

## 大雨 `squall`

纯逻辑：[SquallRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/squall/SquallRules.java)、[SquallTick](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/squall/SquallTick.java)。

行为测试：[SquallModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SquallModuleTest.java)。

世界适配：SquallAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="exodus_flame"></a>

## 狱炎火弹 `exodus_flame`

纯逻辑：[ExodusFlameFlight](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/exodus_flame/ExodusFlameFlight.java)、[ExodusFlameHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/exodus_flame/ExodusFlameHit.java)、[ExodusFlameRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/exodus_flame/ExodusFlameRules.java)、[ExodusFlameSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/exodus_flame/ExodusFlameSpec.java)。

行为测试：[ExodusFlameFlightModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExodusFlameFlightModuleTest.java)、[ExodusFlameHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExodusFlameHitModuleTest.java)、[ExodusFlameModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExodusFlameModuleTest.java)、[ExodusFlameRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExodusFlameRulesModuleTest.java)、[FlightPoliciesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlightPoliciesModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="tornado_impact"></a>

## 风枪龙卷 `tornado_impact`

纯逻辑：[TornadoImpactFlight](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/tornado_impact/TornadoImpactFlight.java)、[TornadoImpactHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/tornado_impact/TornadoImpactHit.java)、[TornadoImpactSpec](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/tornado_impact/TornadoImpactSpec.java)、[TornadoImpactTerrain](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/tornado_impact/TornadoImpactTerrain.java)。

行为测试：[FlightPoliciesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlightPoliciesModuleTest.java)、[TornadoImpactFlightModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/TornadoImpactFlightModuleTest.java)、[TornadoImpactHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/TornadoImpactHitModuleTest.java)、[TornadoImpactModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/TornadoImpactModuleTest.java)、[TornadoImpactTerrainModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/TornadoImpactTerrainModuleTest.java)。

世界适配：显式弹体/专属协议绑定；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="cumulonimbus"></a>

## 豪雷积雨云 `cumulonimbus`

纯逻辑：[CumulonimbusRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/cumulonimbus/CumulonimbusRules.java)、[CumulonimbusState](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/cumulonimbus/CumulonimbusState.java)、[CumulonimbusTick](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/cumulonimbus/CumulonimbusTick.java)。

行为测试：[CumulonimbusModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/CumulonimbusModuleTest.java)。

世界适配：CumulonimbusAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="lightning"></a>

## 雷光 `lightning`

纯逻辑：[LightningImpact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/lightning/LightningImpact.java)、[LightningRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/lightning/LightningRules.java)。

行为测试：[LightningModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/LightningModuleTest.java)。

世界适配：LightningAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="electric"></a>

## 电击 `electric`

纯逻辑：[ElectricImpact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/electric/ElectricImpact.java)、[ElectricRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/electric/ElectricRules.java)。

行为测试：[ElectricModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ElectricModuleTest.java)。

世界适配：ElectricAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="frost_nova"></a>

## 冰霜新星 `frost_nova`

纯逻辑：[FrostNovaHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/frost_nova/FrostNovaHit.java)、[FrostNovaRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/frost_nova/FrostNovaRules.java)。

行为测试：[FrostNovaCommandModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FrostNovaCommandModuleTest.java)、[FrostNovaHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FrostNovaHitModuleTest.java)、[FrostNovaRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FrostNovaRulesModuleTest.java)。

世界适配：FrostNovaAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="sonic_blast"></a>

## 爆音冲击波 `sonic_blast`

纯逻辑：[SonicBlastHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/sonic_blast/SonicBlastHit.java)、[SonicBlastRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/sonic_blast/SonicBlastRules.java)、[SonicBlastTerrain](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/sonic_blast/SonicBlastTerrain.java)。

行为测试：[SonicBlastCommandModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SonicBlastCommandModuleTest.java)、[SonicBlastHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SonicBlastHitModuleTest.java)、[SonicBlastRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SonicBlastRulesModuleTest.java)、[SonicBlastTerrainModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SonicBlastTerrainModuleTest.java)。

世界适配：SonicBlastAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="earth_wall"></a>

## 土壁 `earth_wall`

纯逻辑：[EarthWallLayout](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_wall/EarthWallLayout.java)。

行为测试：[EarthWallModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthWallModuleTest.java)。

世界适配：EarthWallAdapter；视觉归属：terrain block construction。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="earth_fortress"></a>

## 土砦 `earth_fortress`

纯逻辑：[EarthFortressLayout](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_fortress/EarthFortressLayout.java)。

行为测试：[EarthFortressModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthFortressModuleTest.java)。

世界适配：EarthFortressAdapter；视觉归属：terrain block construction。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="earth_hedgehog"></a>

## 土针鼠 `earth_hedgehog`

纯逻辑：[EarthHedgehogRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_hedgehog/EarthHedgehogRules.java)、[EarthHedgehogState](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_hedgehog/EarthHedgehogState.java)、[EarthHedgehogTick](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earth_hedgehog/EarthHedgehogTick.java)。

行为测试：[EarthHedgehogModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/EarthHedgehogModuleTest.java)。

世界适配：EarthHedgehogAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="quagmire"></a>

## 泥沼 `quagmire`

纯逻辑：[QuagmireContact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/quagmire/QuagmireContact.java)、[QuagmireRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/quagmire/QuagmireRules.java)、[QuagmireTick](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/quagmire/QuagmireTick.java)。

行为测试：[QuagmireModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/QuagmireModuleTest.java)。

世界适配：EarthHedgehogAdapter, QuagmireAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="blast"></a>

## 突风 `blast`

纯逻辑：[BlastHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/blast/BlastHit.java)、[BlastRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/blast/BlastRules.java)。

行为测试：[BlastCombinationModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BlastCombinationModuleTest.java)、[BlastHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BlastHitModuleTest.java)、[BlastRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BlastRulesModuleTest.java)。

世界适配：BlastAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="bora_blast"></a>

## 寒冷突风 `bora_blast`

纯逻辑：[BoraBlastHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/bora_blast/BoraBlastHit.java)、[BoraBlastRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/bora_blast/BoraBlastRules.java)。

行为测试：[BoraBlastCombinationModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BoraBlastCombinationModuleTest.java)、[BoraBlastHitModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BoraBlastHitModuleTest.java)、[BoraBlastRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BoraBlastRulesModuleTest.java)。

世界适配：BoraBlastAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="magma_gush"></a>

## 熔岩/溶岩 `magma_gush`

纯逻辑：[MagmaGushHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/magma_gush/MagmaGushHit.java)、[MagmaGushRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/magma_gush/MagmaGushRules.java)、[MagmaGushState](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/magma_gush/MagmaGushState.java)、[MagmaGushTick](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/magma_gush/MagmaGushTick.java)。

行为测试：[MagmaGushModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/MagmaGushModuleTest.java)。

世界适配：EarthHedgehogAdapter, MagmaGushAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="violent_storm"></a>

## 飓风 `violent_storm`

纯逻辑：[ViolentStormPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/violent_storm/ViolentStormPolicy.java)。

行为测试：[ViolentStormModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ViolentStormModuleTest.java)。

世界适配：ViolentStormAdapter, WindFieldRuntimeAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="tornado"></a>

## 龙卷 `tornado`

纯逻辑：[TornadoPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/tornado/TornadoPolicy.java)。

行为测试：[TornadoModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/TornadoModuleTest.java)。

世界适配：TornadoAdapter, WindFieldRuntimeAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="vacuum_storm"></a>

## ☆真空岚 `vacuum_storm`

纯逻辑：[VacuumStormPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/vacuum_storm/VacuumStormPolicy.java)。

行为测试：[VacuumStormModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/VacuumStormModuleTest.java)。

世界适配：VacuumStormAdapter, WindFieldRuntimeAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="sky_tempest"></a>

## ☆天岚 `sky_tempest`

纯逻辑：[SkyTempestPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/sky_tempest/SkyTempestPolicy.java)。

行为测试：[SkyTempestModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SkyTempestModuleTest.java)。

世界适配：SkyTempestAdapter, WindFieldRuntimeAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="flash_over"></a>

## 闪光炎 `flash_over`

纯逻辑：[FlashOverContact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flash_over/FlashOverContact.java)、[FlashOverRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flash_over/FlashOverRules.java)、[FlashOverState](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flash_over/FlashOverState.java)。

行为测试：[FlashOverModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlashOverModuleTest.java)。

世界适配：FlashOverAdapter, WindFieldRuntimeAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="flame_tornado"></a>

## 火炎龙卷 `flame_tornado`

纯逻辑：[FlameTornadoPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/flame_tornado/FlameTornadoPolicy.java)。

行为测试：[FlameTornadoModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlameTornadoModuleTest.java)。

世界适配：FlameTornadoAdapter, WindFieldRuntimeAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="inferno"></a>

## ☆焦热狱 `inferno`

纯逻辑：[InfernoRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/inferno/InfernoRules.java)。

行为测试：[InfernoModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/InfernoModuleTest.java)。

世界适配：InfernoAdapter, WindFieldRuntimeAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="absolute_zero"></a>

## 绝对零度 `absolute_zero`

纯逻辑：[AbsoluteZeroContact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/absolute_zero/AbsoluteZeroContact.java)、[AbsoluteZeroRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/absolute_zero/AbsoluteZeroRules.java)。

行为测试：[AbsoluteZeroModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/AbsoluteZeroModuleTest.java)。

世界适配：AbsoluteZeroAdapter, WindFieldRuntimeAdapter；视觉归属：reference mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="exorcist_ray"></a>

## 神击光 `exorcist_ray`

纯逻辑：[ExorcistRayPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/holy/exorcist_ray/ExorcistRayPolicy.java)。

行为测试：[ExorcistRayModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExorcistRayModuleTest.java)。

世界适配：ExorcistRayAdapter, HolyInstantAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="exorcist_burst"></a>

## ☆神击爆 `exorcist_burst`

纯逻辑：[ExorcistBurstPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/holy/exorcist_burst/ExorcistBurstPolicy.java)。

行为测试：[ExorcistBurstModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExorcistBurstModuleTest.java)。

世界适配：ExorcistBurstAdapter, HolyInstantAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="exorcist_judge"></a>

## ☆神击审判 `exorcist_judge`

纯逻辑：[ExorcistJudgePolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/holy/exorcist_judge/ExorcistJudgePolicy.java)。

行为测试：[ExorcistJudgeModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ExorcistJudgeModuleTest.java)。

世界适配：ExorcistJudgeAdapter, HolyInstantAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="holy_ray"></a>

## ☆圣光 `holy_ray`

纯逻辑：[HolyRayPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/holy/holy_ray/HolyRayPolicy.java)。

行为测试：[HolyRayModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/HolyRayModuleTest.java)。

世界适配：HolyInstantAdapter, HolyRayAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="sacred_nova"></a>

## ☆神圣新星 `sacred_nova`

纯逻辑：[SacredNovaRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/holy/sacred_nova/SacredNovaRules.java)。

行为测试：[HolySanctuaryModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/HolySanctuaryModuleTest.java)。

世界适配：HolyInstantAdapter, SacredNovaAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="divine_judge"></a>

## ☆神圣裁决 `divine_judge`

纯逻辑：[DivineJudgePulse](../../magic-core/src/main/java/com/example/examplemod/magic/spell/holy/divine_judge/DivineJudgePulse.java)、[DivineJudgeRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/holy/divine_judge/DivineJudgeRules.java)、[DivineJudgeTick](../../magic-core/src/main/java/com/example/examplemod/magic/spell/holy/divine_judge/DivineJudgeTick.java)。

行为测试：[DivineJudgeModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/DivineJudgeModuleTest.java)。

世界适配：DivineJudgeAdapter, HolyInstantAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="god_purge"></a>

## ☆神之净化 `god_purge`

纯逻辑：[GodPurgeRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/holy/god_purge/GodPurgeRules.java)。

行为测试：[HolySanctuaryModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/HolySanctuaryModuleTest.java)。

世界适配：GodPurgeAdapter, HolyInstantAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="flame_storm"></a>

## ☆火炎岚 `flame_storm`

纯逻辑：[FlameStormGeometry](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flame_storm/FlameStormGeometry.java)、[FlameStormPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/flame_storm/FlameStormPolicy.java)。

行为测试：[FlameStormGeometryModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/FlameStormGeometryModuleTest.java)、[GroundClaimPolicyModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundClaimPolicyModuleTest.java)、[GroundFieldPolicyModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundFieldPolicyModuleTest.java)。

世界适配：FlameStormAdapter, GroundFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="apocalypse_flame"></a>

## ☆天焦 `apocalypse_flame`

纯逻辑：[ApocalypseFlamePolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/fire/apocalypse_flame/ApocalypseFlamePolicy.java)。

行为测试：[GroundClaimPolicyModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundClaimPolicyModuleTest.java)、[GroundFieldPolicyModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundFieldPolicyModuleTest.java)。

世界适配：ApocalypseFlameAdapter, GroundFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="earthquake"></a>

## ☆地裂 `earthquake`

纯逻辑：[EarthquakePolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/earthquake/EarthquakePolicy.java)。

行为测试：[GroundFieldPolicyModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundFieldPolicyModuleTest.java)。

世界适配：EarthquakeAdapter, GroundFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="terra_fall"></a>

## ☆大地葬 `terra_fall`

纯逻辑：[TerraFallPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/terra_fall/TerraFallPolicy.java)。

行为测试：[GroundFieldPolicyModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundFieldPolicyModuleTest.java)。

世界适配：GroundFieldRuntimeAdapter, TerraFallAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="continental_sink"></a>

## ☆大陆陷没 `continental_sink`

纯逻辑：[ContinentalSinkPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/continental_sink/ContinentalSinkPolicy.java)。

行为测试：[GroundClaimPolicyModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundClaimPolicyModuleTest.java)、[GroundFieldPolicyModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/GroundFieldPolicyModuleTest.java)。

世界适配：ContinentalSinkAdapter, GroundFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="sand_cloud"></a>

## 砂云 `sand_cloud`

纯逻辑：[SandCloudPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/sand_cloud/SandCloudPolicy.java)。

行为测试：[SmokeSpecificationsModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SmokeSpecificationsModuleTest.java)。

世界适配：SandCloudAdapter, SmokeFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="dust_storm"></a>

## 砂尘岚 `dust_storm`

纯逻辑：[DustStormPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/dust_storm/DustStormPolicy.java)。

行为测试：[SmokeSpecificationsModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SmokeSpecificationsModuleTest.java)。

世界适配：DustStormAdapter, SmokeFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="sandstorm"></a>

## 沙暴/沙岚 `sandstorm`

纯逻辑：[SandstormPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/earth/sandstorm/SandstormPolicy.java)。

行为测试：[SmokeSpecificationsModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SmokeSpecificationsModuleTest.java)。

世界适配：SandstormAdapter, SmokeFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="deep_mist"></a>

## 浓雾 `deep_mist`

纯逻辑：[DeepMistPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/deep_mist/DeepMistPolicy.java)。

行为测试：[SmokeSpecificationsModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SmokeSpecificationsModuleTest.java)。

世界适配：DeepMistAdapter, SmokeFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="mud_rain"></a>

## 泥雨 `mud_rain`

纯逻辑：[MudRainPolicy](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/mud_rain/MudRainPolicy.java)。

行为测试：[SmokeSpecificationsModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SmokeSpecificationsModuleTest.java)。

世界适配：MudRainAdapter, SmokeFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="antidote_iii"></a>

## ☆解毒III `antidote_iii`

纯逻辑：[AntidoteIIIEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/detox/antidote_iii/AntidoteIIIEffect.java)。

行为测试：[SupportModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SupportModuleTest.java)。

世界适配：AntidoteIIIAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="howling"></a>

## 声之魔术/吠魔术 `howling`

纯逻辑：[HowlingHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/howling/HowlingHit.java)、[HowlingRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/howling/HowlingRules.java)。

行为测试：[SoundModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SoundModuleTest.java)。

世界适配：HowlingAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="hypnosis_magic"></a>

## 催眠魔术 `hypnosis_magic`

纯逻辑：[HypnosisEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/hypnosis_magic/HypnosisEffect.java)、[HypnosisRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/hypnosis_magic/HypnosisRules.java)。

行为测试：[SoundModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SoundModuleTest.java)。

世界适配：HypnosisAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="atmosphere_dominion"></a>

## ☆大气掌握 `atmosphere_dominion`

纯逻辑：[AtmosphereContact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/atmosphere_dominion/AtmosphereContact.java)、[AtmosphereDominionRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/wind/atmosphere_dominion/AtmosphereDominionRules.java)。

行为测试：[ModeContactModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ModeContactModuleTest.java)、[ModeSelectionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ModeSelectionModuleTest.java)。

世界适配：AtmosphereDominionAdapter, MaintainedFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="gravity_magic"></a>

## 重力魔术 `gravity_magic`

纯逻辑：[GravityContact](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/gravity_magic/GravityContact.java)、[GravityMagicRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/gravity_magic/GravityMagicRules.java)。

行为测试：[ModeContactModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ModeContactModuleTest.java)、[ModeSelectionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ModeSelectionModuleTest.java)。

世界适配：GravityMagicAdapter, MaintainedFieldRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="nuclear_explosion"></a>

## 核爆炸 `nuclear_explosion`

纯逻辑：[NuclearExplosionRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/nuclear_explosion/NuclearExplosionRules.java)、[NuclearExplosionTick](../../magic-core/src/main/java/com/example/examplemod/magic/spell/mixed/nuclear_explosion/NuclearExplosionTick.java)。

行为测试：[MaintainedFieldModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/MaintainedFieldModuleTest.java)、[ModeSelectionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/ModeSelectionModuleTest.java)。

世界适配：MaintainedFieldRuntimeAdapter, NuclearExplosionAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="dual_shield"></a>

## ☆复合障壁 `dual_shield`

纯逻辑：[DualShieldRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/dual_shield/DualShieldRules.java)。

行为测试：[BarrierCapacityModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BarrierCapacityModuleTest.java)。

世界适配：DualShieldAdapter, FiniteBarrierRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="spirit_sealing_barrier"></a>

## 精灵封锁结界 `spirit_sealing_barrier`

纯逻辑：[SpiritSealingBarrierRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/spirit_sealing_barrier/SpiritSealingBarrierRules.java)。

行为测试：[BarrierCapacityModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BarrierCapacityModuleTest.java)。

世界适配：FiniteBarrierRuntimeAdapter, SpiritSealingBarrierAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="confinement_barrier"></a>

## 封锁结界 `confinement_barrier`

纯逻辑：[ConfinementBarrierRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/confinement_barrier/ConfinementBarrierRules.java)。

行为测试：[BarrierCapacityModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/BarrierCapacityModuleTest.java)。

世界适配：ConfinementBarrierAdapter, FiniteBarrierRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="disturb_magic"></a>

## 乱魔 `disturb_magic`

纯逻辑：[DisturbEffect](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/disturb_magic/DisturbEffect.java)。

行为测试：[UtilityIllusionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/UtilityIllusionModuleTest.java)。

世界适配：DisturbMagicAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="divination_magic"></a>

## 占命魔术 `divination_magic`

纯逻辑：[DivinationRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/divination_magic/DivinationRules.java)。

行为测试：[UtilityIllusionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/UtilityIllusionModuleTest.java)。

世界适配：DivinationMagicAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="monster_summon"></a>

## 魔兽召唤 `monster_summon`

纯逻辑：[MonsterSummonRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/summon/monster_summon/MonsterSummonRules.java)。

行为测试：[SummonRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SummonRulesModuleTest.java)。

世界适配：MonsterSummonAdapter, SummonRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="spirit_summon"></a>

## 精灵召唤 `spirit_summon`

纯逻辑：[SpiritSummonRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/summon/spirit_summon/SpiritSummonRules.java)。

行为测试：[SummonRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SummonRulesModuleTest.java)。

世界适配：SpiritSummonAdapter, SummonRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="lamplight_spirits"></a>

## 灯之精灵 `lamplight_spirits`

纯逻辑：[LamplightUpkeep](../../magic-core/src/main/java/com/example/examplemod/magic/spell/summon/lamplight_spirits/LamplightUpkeep.java)。

行为测试：[SummonRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SummonRulesModuleTest.java)。

世界适配：LamplightSpiritsAdapter, SummonRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="necromancy"></a>

## 死灵魔术 `necromancy`

纯逻辑：[NecromancyRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/necromancy/NecromancyRules.java)。

行为测试：[SummonRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SummonRulesModuleTest.java)。

世界适配：NecromancyAdapter, SummonRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="fortress_barrier"></a>

## ☆城塞结界 `fortress_barrier`

纯逻辑：[FortressUpkeep](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/fortress_barrier/FortressUpkeep.java)。

行为测试：[PoweredBarrierModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/PoweredBarrierModuleTest.java)。

世界适配：FortressBarrierAdapter, PoweredBarrierRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="demon_god_sealing_barrier"></a>

## 魔神封印结界 `demon_god_sealing_barrier`

纯逻辑：[DemonGodSealRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/demon_god_sealing_barrier/DemonGodSealRules.java)。

行为测试：[PoweredBarrierModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/PoweredBarrierModuleTest.java)。

世界适配：DemonGodSealingBarrierAdapter, PoweredBarrierRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="saint_dragon_battle_aura"></a>

## 龙圣斗气 `saint_dragon_battle_aura`

纯逻辑：[DragonAuraRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/saint_dragon_battle_aura/DragonAuraRules.java)、[DragonAuraTick](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/saint_dragon_battle_aura/DragonAuraTick.java)。

行为测试：[DragonAuraModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/DragonAuraModuleTest.java)。

世界适配：DragonAuraAdapter, PoweredBarrierRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="communication_magic"></a>

## 通信魔术 `communication_magic`

纯逻辑：[CommunicationDrafts](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/communication_magic/CommunicationDrafts.java)、[CommunicationRelease](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/communication_magic/CommunicationRelease.java)、[CommunicationRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/communication_magic/CommunicationRules.java)。

行为测试：[CommunicationModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/CommunicationModuleTest.java)。

世界适配：CommunicationAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="teleportation_magic"></a>

## 转移魔术 `teleportation_magic`

纯逻辑：[TeleportInvitations](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/teleportation_magic/TeleportInvitations.java)、[TeleportRelease](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/teleportation_magic/TeleportRelease.java)、[TeleportRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/teleportation_magic/TeleportRules.java)、[TeleportSafety](../../magic-core/src/main/java/com/example/examplemod/magic/spell/special/teleportation_magic/TeleportSafety.java)。

行为测试：[TeleportModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/TeleportModuleTest.java)。

世界适配：TeleportationAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="illusion_barrier"></a>

## 幻影结界 `illusion_barrier`

纯逻辑：[IllusionPulse](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/illusion_barrier/IllusionPulse.java)、[IllusionRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/barrier/illusion_barrier/IllusionRules.java)。

行为测试：[SummonRulesModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/SummonRulesModuleTest.java)、[UtilityIllusionModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/UtilityIllusionModuleTest.java)。

世界适配：IllusionAdapter, WindFieldRuntimeAdapter；视觉归属：illusion block projection。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。

<a id="ocean_freeze"></a>

## ☆冰海 `ocean_freeze`

纯逻辑：[OceanFreezeHit](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ocean_freeze/OceanFreezeHit.java)、[OceanFreezeRules](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ocean_freeze/OceanFreezeRules.java)、[OceanFreezeState](../../magic-core/src/main/java/com/example/examplemod/magic/spell/water/ocean_freeze/OceanFreezeState.java)。

行为测试：[OceanFreezeModuleTest](../../magic-core/src/moduleTest/java/com/example/examplemod/magic/casting/OceanFreezeModuleTest.java)。

世界适配：OceanFreezeAdapter, OceanFreezeRuntimeAdapter；视觉归属：layered pixel mesh。释放路由统一从[SpellExecutionBindings](../../src/main/java/com/example/examplemod/magic/SpellExecutionBindings.java)查本ID。
