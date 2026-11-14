# 重构后的实际文件树

生成：2026-11-14。以下为实际Java源文件；资源路径和素材保持原样。

```text
magic-core/src/main/java/
└── com/
    └── example/
        └── examplemod/
            ├── bootstrap/
            │   ├── GroundFieldComposition.java
            │   ├── SmokeFieldComposition.java
            │   └── WindFieldComposition.java
            └── magic/
                ├── application/
                │   ├── casting/
                │   │   ├── CastLocalEffectCommand.java
                │   │   ├── CastSelfEffectCommand.java
                │   │   ├── CastSpellCommand.java
                │   │   ├── CastTargetedAreaCommand.java
                │   │   ├── ChannelPayment.java
                │   │   ├── ChargePayment.java
                │   │   ├── FlamethrowerChannel.java
                │   │   ├── PreparationEligibility.java
                │   │   ├── PreparationRegistry.java
                │   │   └── WaterfallChannel.java
                │   ├── learning/
                │   │   └── LearnSpell.java
                │   └── port/
                │       ├── ChannelDebt.java
                │       ├── FlameStream.java
                │       ├── LocalEffect.java
                │       ├── LocalHitTargets.java
                │       ├── ManaReservation.java
                │       ├── PaidProjectile.java
                │       ├── PlayerStore.java
                │       ├── ProjectileSpawner.java
                │       ├── SelfEffect.java
                │       ├── TargetedArea.java
                │       ├── VfxObserver.java
                │       └── WaterSource.java
                ├── catalog/
                │   ├── LearningCatalog.java
                │   ├── SpellFactory.java
                │   └── SpellFactoryRegistry.java
                ├── domain/
                │   ├── casting/
                │   │   ├── CastPhase.java
                │   │   ├── CastingSession.java
                │   │   ├── Channeling.java
                │   │   ├── Idle.java
                │   │   └── Preparing.java
                │   ├── combat/
                │   │   └── EffectRules.java
                │   ├── geometry/
                │   │   ├── BladeSectionVolume.java
                │   │   ├── ContactGeometry.java
                │   │   ├── SphereCrossing.java
                │   │   └── SweptVolume.java
                │   ├── model/
                │   │   ├── Box3d.java
                │   │   ├── SpellId.java
                │   │   └── Vec3d.java
                │   ├── player/
                │   │   ├── ManaPolicy.java
                │   │   └── PlayerMagicState.java
                │   ├── runtime/
                │   │   └── TickQuota.java
                │   ├── spec/
                │   │   ├── catalog/
                │   │   │   ├── DefinitionPresets.java
                │   │   │   ├── EarthSpellDefinitions.java
                │   │   │   ├── FireSpellDefinitions.java
                │   │   │   ├── HealingSpellDefinitions.java
                │   │   │   ├── HolySpellDefinitions.java
                │   │   │   ├── MagicSpellDefinitions.java
                │   │   │   ├── MixedSpellDefinitions.java
                │   │   │   ├── SpecialSpellDefinitions.java
                │   │   │   ├── SummonSpellDefinitions.java
                │   │   │   ├── WaterSpellDefinitions.java
                │   │   │   └── WindSpellDefinitions.java
                │   │   ├── CastingTiming.java
                │   │   └── SpellTraits.java
                │   └── visual/
                │       ├── mesh/
                │       │   ├── LayeredPixelMesh.java
                │       │   └── PixelFace.java
                │       ├── motion/
                │       │   ├── FireMeshDeformation.java
                │       │   ├── LifeMeshDeformation.java
                │       │   ├── MeshDeformation.java
                │       │   ├── MeshMotion.java
                │       │   ├── MeshStage.java
                │       │   ├── ReferenceMeshMotion.java
                │       │   ├── SolidMeshDeformation.java
                │       │   ├── WardMeshDeformation.java
                │       │   ├── WarmMeshDeformation.java
                │       │   ├── WaterMeshDeformation.java
                │       │   └── WindMeshDeformation.java
                │       ├── AreaAnimationClock.java
                │       ├── AreaMeshLayout.java
                │       ├── ParticleAnimation.java
                │       ├── ProjectileMeshSpec.java
                │       └── SpellAtmosphereProfile.java
                ├── feature/
                │   ├── input/
                │   │   ├── CastingInputState.java
                │   │   ├── InputSessions.java
                │   │   └── WheelGeometry.java
                │   ├── learning/
                │   │   ├── BookPolicy.java
                │   │   ├── ChantLesson.java
                │   │   ├── ClassifiedBookPolicy.java
                │   │   ├── PrimerBookPolicy.java
                │   │   ├── SegmentedLesson.java
                │   │   └── ZenithBookPolicy.java
                │   └── terrain/
                │       └── TerrainLeaseScheduler.java
                ├── mechanic/
                │   ├── barrier/
                │   │   ├── BarrierCapacity.java
                │   │   ├── BarrierDamage.java
                │   │   ├── BarrierMovement.java
                │   │   ├── BarrierProjectile.java
                │   │   └── PoweredBarrierTick.java
                │   ├── channel/
                │   │   ├── ChannelAdmission.java
                │   │   ├── ChannelCost.java
                │   │   ├── ChannelSession.java
                │   │   └── ChargeCurve.java
                │   ├── field/
                │   │   ├── AreaRegistry.java
                │   │   ├── AreaTick.java
                │   │   ├── BurstFieldContact.java
                │   │   ├── BurstFieldSchedule.java
                │   │   ├── ContactCooldowns.java
                │   │   ├── ExpiringFieldTick.java
                │   │   ├── FieldSchedule.java
                │   │   ├── FogSight.java
                │   │   ├── GroundFieldContact.java
                │   │   ├── GroundFieldPolicy.java
                │   │   ├── GroundFieldTick.java
                │   │   ├── MaintainedFieldSpec.java
                │   │   ├── MaintainedFieldTick.java
                │   │   ├── ModeSelection.java
                │   │   ├── NaturalSpawnSuppression.java
                │   │   ├── RainExposure.java
                │   │   ├── SmokeContact.java
                │   │   ├── SmokeFieldPolicy.java
                │   │   ├── SmokeFieldTick.java
                │   │   ├── WindFieldContact.java
                │   │   ├── WindFieldPolicy.java
                │   │   ├── WindFieldPulse.java
                │   │   ├── WindFieldRules.java
                │   │   ├── WindFieldState.java
                │   │   └── WindFieldTick.java
                │   ├── projectile/
                │   │   ├── BlockBurstPlan.java
                │   │   ├── BlockBurstRules.java
                │   │   ├── CarrierContactTraversal.java
                │   │   ├── CarrierFlightPolicy.java
                │   │   ├── CarrierHit.java
                │   │   ├── CarrierServerTick.java
                │   │   ├── CarrierTermination.java
                │   │   ├── ContactCandidates.java
                │   │   ├── ContactTraversal.java
                │   │   ├── DeferredAreaImpact.java
                │   │   ├── EffectShape.java
                │   │   ├── EffectShapeCatalog.java
                │   │   ├── FluidContactScan.java
                │   │   ├── HitHistoryFormat.java
                │   │   ├── ImpactHistory.java
                │   │   ├── LaunchBallistics.java
                │   │   ├── PiercingFlightRules.java
                │   │   ├── ProjectileBehavior.java
                │   │   ├── ProjectileServerStep.java
                │   │   ├── ProjectileSpawnPlan.java
                │   │   ├── ProjectileSpec.java
                │   │   ├── ProjectileSpellFactory.java
                │   │   ├── ProjectileTermination.java
                │   │   ├── SavedProjectileCounters.java
                │   │   ├── ShieldAbsorption.java
                │   │   ├── ShieldInterception.java
                │   │   ├── SolidContacts.java
                │   │   ├── SweptChunkGate.java
                │   │   ├── SweptProjectileHit.java
                │   │   ├── SweptProjectilePolicy.java
                │   │   └── WaterPassage.java
                │   ├── summon/
                │   │   └── SummonTick.java
                │   ├── targeting/
                │   │   ├── HolyAreaHit.java
                │   │   ├── HolyEligibility.java
                │   │   ├── HolyGeometry.java
                │   │   ├── HolyInstantPolicy.java
                │   │   ├── HolyNearest.java
                │   │   ├── HolyOpeningHit.java
                │   │   ├── HolyVolumePolicy.java
                │   │   └── SupportConsent.java
                │   └── terrain/
                │       └── ColumnGrowth.java
                ├── spell/
                │   ├── barrier/
                │   │   ├── confinement_barrier/
                │   │   │   └── ConfinementBarrierRules.java
                │   │   ├── demon_god_sealing_barrier/
                │   │   │   └── DemonGodSealRules.java
                │   │   ├── dual_shield/
                │   │   │   └── DualShieldRules.java
                │   │   ├── fortress_barrier/
                │   │   │   └── FortressUpkeep.java
                │   │   ├── illusion_barrier/
                │   │   │   ├── IllusionPulse.java
                │   │   │   └── IllusionRules.java
                │   │   ├── magic_shield/
                │   │   │   └── MagicShieldDamageGate.java
                │   │   ├── physical_shield/
                │   │   │   ├── PhysicalShieldAbsorb.java
                │   │   │   ├── PhysicalShieldInterception.java
                │   │   │   ├── PhysicalShieldMovement.java
                │   │   │   ├── PhysicalShieldRegistry.java
                │   │   │   ├── PhysicalShieldRelease.java
                │   │   │   ├── PhysicalShieldRules.java
                │   │   │   └── PhysicalShieldSelection.java
                │   │   └── spirit_sealing_barrier/
                │   │       └── SpiritSealingBarrierRules.java
                │   ├── detox/
                │   │   ├── antidote_ii/
                │   │   │   └── AntidoteIIEffect.java
                │   │   ├── antidote_iii/
                │   │   │   └── AntidoteIIIEffect.java
                │   │   └── detoxification/
                │   │       └── DetoxificationEffect.java
                │   ├── earth/
                │   │   ├── continental_sink/
                │   │   │   └── ContinentalSinkPolicy.java
                │   │   ├── dust_storm/
                │   │   │   └── DustStormPolicy.java
                │   │   ├── earth_fortress/
                │   │   │   └── EarthFortressLayout.java
                │   │   ├── earth_hedgehog/
                │   │   │   ├── EarthHedgehogRules.java
                │   │   │   ├── EarthHedgehogState.java
                │   │   │   └── EarthHedgehogTick.java
                │   │   ├── earth_lance/
                │   │   │   ├── EarthLanceHit.java
                │   │   │   ├── EarthLanceLift.java
                │   │   │   ├── EarthLanceSelection.java
                │   │   │   └── EarthLanceSpikes.java
                │   │   ├── earth_pillar/
                │   │   │   ├── EarthPillarFall.java
                │   │   │   ├── EarthPillarSpawn.java
                │   │   │   ├── EarthPillarStone.java
                │   │   │   └── EarthPillarStop.java
                │   │   ├── earth_wall/
                │   │   │   └── EarthWallLayout.java
                │   │   ├── earthquake/
                │   │   │   └── EarthquakePolicy.java
                │   │   ├── rock_bullet/
                │   │   │   └── RockBulletSpec.java
                │   │   ├── sand_cloud/
                │   │   │   └── SandCloudPolicy.java
                │   │   ├── sandstorm/
                │   │   │   └── SandstormPolicy.java
                │   │   ├── stone_cannon/
                │   │   │   ├── ChargeEligibility.java
                │   │   │   ├── ChargeSession.java
                │   │   │   ├── ChargeStep.java
                │   │   │   ├── StoneCannonRules.java
                │   │   │   └── StoneCannonSpec.java
                │   │   └── terra_fall/
                │   │       └── TerraFallPolicy.java
                │   ├── fire/
                │   │   ├── apocalypse_flame/
                │   │   │   └── ApocalypseFlamePolicy.java
                │   │   ├── burning_place/
                │   │   │   └── BurningPlaceContact.java
                │   │   ├── ex_flame/
                │   │   │   ├── ExFlameRules.java
                │   │   │   └── ExFlameSpec.java
                │   │   ├── exodus_flame/
                │   │   │   ├── ExodusFlameFlight.java
                │   │   │   ├── ExodusFlameHit.java
                │   │   │   ├── ExodusFlameRules.java
                │   │   │   └── ExodusFlameSpec.java
                │   │   ├── explosion/
                │   │   │   ├── ExplosionImpact.java
                │   │   │   └── ExplosionSpec.java
                │   │   ├── fireball/
                │   │   │   ├── FireballImpactPolicy.java
                │   │   │   └── FireballSpec.java
                │   │   ├── flame_pillar/
                │   │   │   ├── FlamePillarBurn.java
                │   │   │   ├── FlamePillarFirePlacement.java
                │   │   │   ├── FlamePillarIgnition.java
                │   │   │   └── FlamePillarSpec.java
                │   │   ├── flame_slice/
                │   │   │   ├── FlameSliceFlightState.java
                │   │   │   ├── FlameSliceRules.java
                │   │   │   └── FlameSliceSpec.java
                │   │   ├── flame_storm/
                │   │   │   ├── FlameStormGeometry.java
                │   │   │   └── FlameStormPolicy.java
                │   │   ├── flamethrower/
                │   │   │   ├── FlamethrowerHit.java
                │   │   │   └── FlamethrowerSpec.java
                │   │   ├── flash_over/
                │   │   │   ├── FlashOverContact.java
                │   │   │   ├── FlashOverRules.java
                │   │   │   └── FlashOverState.java
                │   │   ├── heat_hand/
                │   │   │   ├── HeatHandMelt.java
                │   │   │   └── HeatHandRules.java
                │   │   ├── heat_island/
                │   │   │   └── HeatIslandRules.java
                │   │   ├── inferno/
                │   │   │   └── InfernoRules.java
                │   │   └── magma_gush/
                │   │       ├── MagmaGushHit.java
                │   │       ├── MagmaGushRules.java
                │   │       ├── MagmaGushState.java
                │   │       └── MagmaGushTick.java
                │   ├── healing/
                │   │   ├── ex_healing/
                │   │   │   ├── ExHealingEffect.java
                │   │   │   └── ExHealingRules.java
                │   │   └── healing/
                │   │       ├── HealingEffect.java
                │   │       └── HealingRules.java
                │   ├── holy/
                │   │   ├── divine_judge/
                │   │   │   ├── DivineJudgePulse.java
                │   │   │   ├── DivineJudgeRules.java
                │   │   │   └── DivineJudgeTick.java
                │   │   ├── exorcist_burst/
                │   │   │   └── ExorcistBurstPolicy.java
                │   │   ├── exorcist_judge/
                │   │   │   └── ExorcistJudgePolicy.java
                │   │   ├── exorcist_ray/
                │   │   │   └── ExorcistRayPolicy.java
                │   │   ├── god_purge/
                │   │   │   └── GodPurgeRules.java
                │   │   ├── holy_ray/
                │   │   │   └── HolyRayPolicy.java
                │   │   └── sacred_nova/
                │   │       └── SacredNovaRules.java
                │   ├── mixed/
                │   │   ├── blast/
                │   │   │   ├── BlastHit.java
                │   │   │   └── BlastRules.java
                │   │   ├── bora_blast/
                │   │   │   ├── BoraBlastHit.java
                │   │   │   └── BoraBlastRules.java
                │   │   ├── deep_mist/
                │   │   │   └── DeepMistPolicy.java
                │   │   ├── flame_tornado/
                │   │   │   └── FlameTornadoPolicy.java
                │   │   ├── frost_nova/
                │   │   │   ├── FrostNovaHit.java
                │   │   │   └── FrostNovaRules.java
                │   │   ├── mud_rain/
                │   │   │   └── MudRainPolicy.java
                │   │   ├── nuclear_explosion/
                │   │   │   ├── NuclearExplosionRules.java
                │   │   │   └── NuclearExplosionTick.java
                │   │   └── quagmire/
                │   │       ├── QuagmireContact.java
                │   │       ├── QuagmireRules.java
                │   │       └── QuagmireTick.java
                │   ├── special/
                │   │   ├── communication_magic/
                │   │   │   ├── CommunicationDrafts.java
                │   │   │   ├── CommunicationRelease.java
                │   │   │   └── CommunicationRules.java
                │   │   ├── disturb_magic/
                │   │   │   └── DisturbEffect.java
                │   │   ├── divination_magic/
                │   │   │   └── DivinationRules.java
                │   │   ├── gravity_magic/
                │   │   │   ├── GravityContact.java
                │   │   │   └── GravityMagicRules.java
                │   │   ├── howling/
                │   │   │   ├── HowlingHit.java
                │   │   │   └── HowlingRules.java
                │   │   ├── hypnosis_magic/
                │   │   │   ├── HypnosisEffect.java
                │   │   │   └── HypnosisRules.java
                │   │   ├── necromancy/
                │   │   │   └── NecromancyRules.java
                │   │   ├── saint_dragon_battle_aura/
                │   │   │   ├── DragonAuraRules.java
                │   │   │   └── DragonAuraTick.java
                │   │   └── teleportation_magic/
                │   │       ├── TeleportInvitations.java
                │   │       ├── TeleportRelease.java
                │   │       ├── TeleportRules.java
                │   │       └── TeleportSafety.java
                │   ├── summon/
                │   │   ├── lamplight_spirits/
                │   │   │   └── LamplightUpkeep.java
                │   │   ├── monster_summon/
                │   │   │   └── MonsterSummonRules.java
                │   │   └── spirit_summon/
                │   │       └── SpiritSummonRules.java
                │   ├── water/
                │   │   ├── absolute_zero/
                │   │   │   ├── AbsoluteZeroContact.java
                │   │   │   └── AbsoluteZeroRules.java
                │   │   ├── blizzard_storm/
                │   │   │   ├── BlizzardStormRules.java
                │   │   │   └── BlizzardStormTick.java
                │   │   ├── cumulonimbus/
                │   │   │   ├── CumulonimbusRules.java
                │   │   │   ├── CumulonimbusState.java
                │   │   │   └── CumulonimbusTick.java
                │   │   ├── electric/
                │   │   │   ├── ElectricImpact.java
                │   │   │   └── ElectricRules.java
                │   │   ├── flood_flush/
                │   │   │   └── FloodFlushSpec.java
                │   │   ├── ice_blade/
                │   │   │   ├── IceBladeImpactPolicy.java
                │   │   │   └── IceBladeSpec.java
                │   │   ├── ice_fortress/
                │   │   │   ├── IceFortressPlacement.java
                │   │   │   └── IceFortressSpec.java
                │   │   ├── ice_lance/
                │   │   │   ├── IceLanceHit.java
                │   │   │   ├── IceLanceRules.java
                │   │   │   └── IceLanceSpec.java
                │   │   ├── ice_pillar/
                │   │   │   ├── IcePillarRules.java
                │   │   │   └── IcePillarSpec.java
                │   │   ├── ice_smash/
                │   │   │   ├── IceSmashImpactPolicy.java
                │   │   │   └── IceSmashSpec.java
                │   │   ├── icicle_break/
                │   │   │   └── IcicleBreakSpec.java
                │   │   ├── icicle_edge/
                │   │   │   └── IcicleEdgeSpec.java
                │   │   ├── icicle_field/
                │   │   │   └── IcicleFieldRules.java
                │   │   ├── lightning/
                │   │   │   ├── LightningImpact.java
                │   │   │   └── LightningRules.java
                │   │   ├── ocean_freeze/
                │   │   │   ├── OceanFreezeHit.java
                │   │   │   ├── OceanFreezeRules.java
                │   │   │   └── OceanFreezeState.java
                │   │   ├── squall/
                │   │   │   ├── SquallRules.java
                │   │   │   └── SquallTick.java
                │   │   ├── water_arrow/
                │   │   │   ├── WaterArrowImpactPolicy.java
                │   │   │   └── WaterArrowSpec.java
                │   │   ├── water_ball/
                │   │   │   └── WaterBallSpec.java
                │   │   ├── water_cannon/
                │   │   │   ├── WaterCannonRules.java
                │   │   │   └── WaterCannonSpec.java
                │   │   ├── water_shield/
                │   │   │   ├── WaterShieldAbsorb.java
                │   │   │   ├── WaterShieldContact.java
                │   │   │   ├── WaterShieldRules.java
                │   │   │   └── WaterShieldSelection.java
                │   │   ├── water_splash/
                │   │   │   ├── WaterSplashRules.java
                │   │   │   └── WaterSplashSpec.java
                │   │   └── waterfall/
                │   │       ├── ConnectedWater.java
                │   │       └── WaterfallSpec.java
                │   └── wind/
                │       ├── atmosphere_dominion/
                │       │   ├── AtmosphereContact.java
                │       │   └── AtmosphereDominionRules.java
                │       ├── sky_tempest/
                │       │   └── SkyTempestPolicy.java
                │       ├── sonic_blast/
                │       │   ├── SonicBlastHit.java
                │       │   ├── SonicBlastRules.java
                │       │   └── SonicBlastTerrain.java
                │       ├── sonic_boom/
                │       │   └── SonicBoomSpec.java
                │       ├── tornado/
                │       │   └── TornadoPolicy.java
                │       ├── tornado_impact/
                │       │   ├── TornadoImpactFlight.java
                │       │   ├── TornadoImpactHit.java
                │       │   ├── TornadoImpactSpec.java
                │       │   └── TornadoImpactTerrain.java
                │       ├── vacuum_storm/
                │       │   └── VacuumStormPolicy.java
                │       ├── violent_storm/
                │       │   └── ViolentStormPolicy.java
                │       ├── wind_ball/
                │       │   └── WindBallSpec.java
                │       ├── wind_bind/
                │       │   ├── WindBindEffect.java
                │       │   └── WindBindSpec.java
                │       ├── wind_blast/
                │       │   ├── WindBlastEffect.java
                │       │   └── WindBlastRules.java
                │       └── wind_slice/
                │           ├── WindSliceHit.java
                │           └── WindSliceSpec.java
                ├── CastingProfile.java
                ├── MagicCategory.java
                └── SpellDefinition.java
magic-core/src/test/java/
└── com/
    └── example/
        └── examplemod/
            └── magic/
                ├── catalog/
                │   └── SpellFactoryRegistryTest.java
                ├── domain/
                │   ├── geometry/
                │   │   └── GeometryTest.java
                │   ├── model/
                │   │   └── SpellIdTest.java
                │   ├── player/
                │   │   └── ManaPolicyTest.java
                │   └── spec/
                │       └── CastingTimingTest.java
                ├── mechanic/
                │   └── RuleBoundaryTest.java
                ├── testing/
                │   └── FrozenBladeSection.java
                ├── BladeSectionMigrationTest.java
                ├── ChannelCostTest.java
                ├── ChannelingStateTest.java
                ├── ChargeSessionTest.java
                ├── ConnectedWaterTest.java
                ├── ExFlameRulesTest.java
                ├── FlameSliceRulesTest.java
                ├── FloodFlushRulesTest.java
                ├── IceFortressPlanTest.java
                ├── IceLanceRulesTest.java
                ├── IcePillarRulesTest.java
                ├── IcicleBreakFlightTest.java
                ├── IcicleBreakRulesTest.java
                ├── SonicBoomGeometryTest.java
                ├── StoneCannonRulesTest.java
                ├── StoneChargeStepTest.java
                ├── WaterCannonRulesTest.java
                └── WaterSplashRulesTest.java
magic-core/src/moduleTest/java/
└── com/
    └── example/
        └── examplemod/
            └── magic/
                ├── casting/
                │   ├── AbsoluteZeroModuleTest.java
                │   ├── AreaRegistryModuleTest.java
                │   ├── AreaTickModuleTest.java
                │   ├── AreaVisualContractsModuleTest.java
                │   ├── BarrierCapacityModuleTest.java
                │   ├── BarrierRuntimeModuleTest.java
                │   ├── BlastCombinationModuleTest.java
                │   ├── BlastCommandModuleTest.java
                │   ├── BlastHitModuleTest.java
                │   ├── BlastRulesModuleTest.java
                │   ├── BlizzardStormModuleTest.java
                │   ├── BlockBurstPlanModuleTest.java
                │   ├── BoraBlastCombinationModuleTest.java
                │   ├── BoraBlastCommandModuleTest.java
                │   ├── BoraBlastHitModuleTest.java
                │   ├── BoraBlastRulesModuleTest.java
                │   ├── BurningPlaceCommandModuleTest.java
                │   ├── BurningPlaceModuleTest.java
                │   ├── CastingInputModuleTest.java
                │   ├── ChannelAdmissionModuleTest.java
                │   ├── ChannelPaymentModuleTest.java
                │   ├── ChargePaymentModuleTest.java
                │   ├── CircleCommittedReleaseModuleTest.java
                │   ├── ColumnGrowthModuleTest.java
                │   ├── CommunicationModuleTest.java
                │   ├── ContactCandidatesModuleTest.java
                │   ├── ContactCooldownsModuleTest.java
                │   ├── ContactGeometryModuleTest.java
                │   ├── ContactTraversalModuleTest.java
                │   ├── CumulonimbusModuleTest.java
                │   ├── DeferredAreaImpactModuleTest.java
                │   ├── DetoxificationCommandModuleTest.java
                │   ├── DivineJudgeModuleTest.java
                │   ├── DragonAuraModuleTest.java
                │   ├── EarthFortressModuleTest.java
                │   ├── EarthHedgehogModuleTest.java
                │   ├── EarthLanceHitModuleTest.java
                │   ├── EarthLanceLiftModuleTest.java
                │   ├── EarthLanceReleaseModuleTest.java
                │   ├── EarthLanceSelectionModuleTest.java
                │   ├── EarthLanceSpikesModuleTest.java
                │   ├── EarthPillarFallModuleTest.java
                │   ├── EarthPillarHistoryModuleTest.java
                │   ├── EarthPillarReleaseModuleTest.java
                │   ├── EarthPillarSpawnModuleTest.java
                │   ├── EarthPillarStoneModuleTest.java
                │   ├── EarthPillarStopModuleTest.java
                │   ├── EarthWallModuleTest.java
                │   ├── EffectShapeModuleTest.java
                │   ├── ElectricModuleTest.java
                │   ├── ExFlameModuleTest.java
                │   ├── ExHealingEffectModuleTest.java
                │   ├── ExHealingReleaseModuleTest.java
                │   ├── ExHealingRulesModuleTest.java
                │   ├── ExodusFlameFlightModuleTest.java
                │   ├── ExodusFlameHitModuleTest.java
                │   ├── ExodusFlameModuleTest.java
                │   ├── ExodusFlameRulesModuleTest.java
                │   ├── ExorcistBurstModuleTest.java
                │   ├── ExorcistJudgeModuleTest.java
                │   ├── ExorcistRayModuleTest.java
                │   ├── ExplosionImpactModuleTest.java
                │   ├── ExplosionModuleTest.java
                │   ├── FireballModuleTest.java
                │   ├── FlamePillarBurnModuleTest.java
                │   ├── FlamePillarFireModuleTest.java
                │   ├── FlamePillarModuleTest.java
                │   ├── FlameSliceModuleTest.java
                │   ├── FlameStormGeometryModuleTest.java
                │   ├── FlameTornadoModuleTest.java
                │   ├── FlamethrowerChannelModuleTest.java
                │   ├── FlamethrowerHitModuleTest.java
                │   ├── FlashOverModuleTest.java
                │   ├── FlightPoliciesModuleTest.java
                │   ├── FloodFlushModuleTest.java
                │   ├── FluidContactScanModuleTest.java
                │   ├── FogSightModuleTest.java
                │   ├── FrostNovaCommandModuleTest.java
                │   ├── FrostNovaHitModuleTest.java
                │   ├── FrostNovaRulesModuleTest.java
                │   ├── FrozenExtendedSpellMotion.java
                │   ├── FrozenReferenceMeshMotion.java
                │   ├── GroundClaimPolicyModuleTest.java
                │   ├── GroundFieldContactModuleTest.java
                │   ├── GroundFieldPolicyModuleTest.java
                │   ├── GroundFieldTickModuleTest.java
                │   ├── HealingDetoxModuleTest.java
                │   ├── HeatHandCommandModuleTest.java
                │   ├── HeatHandModuleTest.java
                │   ├── HeatIslandCommandModuleTest.java
                │   ├── HeatIslandModuleTest.java
                │   ├── HitHistoryFormatModuleTest.java
                │   ├── HolyRayModuleTest.java
                │   ├── HolyRuntimeModuleTest.java
                │   ├── HolySanctuaryModuleTest.java
                │   ├── IceBladeModuleTest.java
                │   ├── IceFortressModuleTest.java
                │   ├── IceFortressPlacementModuleTest.java
                │   ├── IceLanceHitModuleTest.java
                │   ├── IceLanceModuleTest.java
                │   ├── IcePillarModuleTest.java
                │   ├── IceSmashModuleTest.java
                │   ├── IcicleBreakHistoryModuleTest.java
                │   ├── IcicleBreakModuleTest.java
                │   ├── IcicleEdgeModuleTest.java
                │   ├── IcicleFieldModuleTest.java
                │   ├── InfernoModuleTest.java
                │   ├── InputSessionsModuleTest.java
                │   ├── IntermediateHitModuleTest.java
                │   ├── LaunchBallisticsModuleTest.java
                │   ├── LightningModuleTest.java
                │   ├── MagicShieldCommandModuleTest.java
                │   ├── MagicShieldModuleTest.java
                │   ├── MagmaGushModuleTest.java
                │   ├── MaintainedFieldModuleTest.java
                │   ├── MaintainedFieldSpecModuleTest.java
                │   ├── MeshMotionDifferentialModuleTest.java
                │   ├── ModeContactModuleTest.java
                │   ├── ModeSelectionModuleTest.java
                │   ├── OceanFreezeModuleTest.java
                │   ├── ParticleAnimationModuleTest.java
                │   ├── PhaseTwoContactTraversalModuleTest.java
                │   ├── PhaseTwoHitModuleTest.java
                │   ├── PhaseTwoServerTickModuleTest.java
                │   ├── PhaseTwoTerminationModuleTest.java
                │   ├── PhysicalShieldAbsorbModuleTest.java
                │   ├── PhysicalShieldCommandModuleTest.java
                │   ├── PhysicalShieldInterceptionModuleTest.java
                │   ├── PhysicalShieldMovementModuleTest.java
                │   ├── PhysicalShieldRegistryModuleTest.java
                │   ├── PhysicalShieldReleaseModuleTest.java
                │   ├── PhysicalShieldSelectionModuleTest.java
                │   ├── PoweredBarrierModuleTest.java
                │   ├── PreparationEligibilityModuleTest.java
                │   ├── PreparationRegistryModuleTest.java
                │   ├── ProjectileMeshSpecModuleTest.java
                │   ├── ProjectileServerStepModuleTest.java
                │   ├── ProjectileTerminationModuleTest.java
                │   ├── PureSpellCatalogModuleTest.java
                │   ├── QuagmireModuleTest.java
                │   ├── ReferenceMeshMotionModuleTest.java
                │   ├── RockBulletModuleTest.java
                │   ├── SavedProjectileCountersModuleTest.java
                │   ├── ShieldInterceptionModuleTest.java
                │   ├── SkyTempestModuleTest.java
                │   ├── SmokeRuntimeModuleTest.java
                │   ├── SmokeSpecificationsModuleTest.java
                │   ├── SolidContactsModuleTest.java
                │   ├── SonicBlastCommandModuleTest.java
                │   ├── SonicBlastHitModuleTest.java
                │   ├── SonicBlastRulesModuleTest.java
                │   ├── SonicBlastTerrainModuleTest.java
                │   ├── SonicBoomModuleTest.java
                │   ├── SoundModuleTest.java
                │   ├── SquallModuleTest.java
                │   ├── SummonLifecycleModuleTest.java
                │   ├── SummonRulesModuleTest.java
                │   ├── SupportModuleTest.java
                │   ├── SweptChunkGateModuleTest.java
                │   ├── TeleportModuleTest.java
                │   ├── TerrainLeaseSchedulerModuleTest.java
                │   ├── TornadoImpactFlightModuleTest.java
                │   ├── TornadoImpactHitModuleTest.java
                │   ├── TornadoImpactModuleTest.java
                │   ├── TornadoImpactTerrainModuleTest.java
                │   ├── TornadoModuleTest.java
                │   ├── UtilityIllusionModuleTest.java
                │   ├── VacuumStormModuleTest.java
                │   ├── ViolentStormModuleTest.java
                │   ├── WaterArrowModuleTest.java
                │   ├── WaterBallModuleTest.java
                │   ├── WaterCannonModuleTest.java
                │   ├── WaterShieldCommandModuleTest.java
                │   ├── WaterShieldModuleTest.java
                │   ├── WaterShieldSelectionModuleTest.java
                │   ├── WaterSplashModuleTest.java
                │   ├── WaterfallChannelModuleTest.java
                │   ├── WheelGeometryModuleTest.java
                │   ├── WindBallModuleTest.java
                │   ├── WindBindEffectModuleTest.java
                │   ├── WindBindModuleTest.java
                │   ├── WindBindPulseModuleTest.java
                │   ├── WindBlastEffectModuleTest.java
                │   ├── WindBlastReleaseModuleTest.java
                │   ├── WindBlastRulesModuleTest.java
                │   ├── WindFieldRuntimeModuleTest.java
                │   ├── WindSliceHitModuleTest.java
                │   └── WindSliceModuleTest.java
                ├── domain/
                │   └── visual/
                │       └── mesh/
                │           ├── FrozenLayeredMesh.java
                │           └── LayeredPixelMeshModuleTest.java
                └── learning/
                    └── LearningModuleTest.java
src/main/java/
└── com/
    └── example/
        └── examplemod/
            ├── bootstrap/
            │   ├── AreaBehaviorComposition.java
            │   ├── GroundContactComposition.java
            │   ├── GroundTerrainComposition.java
            │   ├── HolyInstantComposition.java
            │   ├── LightningComposition.java
            │   ├── LocalEffectComposition.java
            │   ├── MagicBootstrap.java
            │   ├── MagicCatalogs.java
            │   ├── MagicEntityTypes.java
            │   ├── MagicPlayerLifecycle.java
            │   ├── MagicServerNetworking.java
            │   ├── ProjectileComposition.java
            │   ├── ProjectileHitComposition.java
            │   ├── SelfEffectComposition.java
            │   ├── SpellModeComposition.java
            │   └── TargetedAreaComposition.java
            ├── infrastructure/
            │   └── fabric/
            │       ├── geometry/
            │       │   ├── BlockBurstGeometryAdapter.java
            │       │   ├── GeometryAdapter.java
            │       │   └── LaunchImpulseAdapter.java
            │       ├── magic/
            │       │   ├── AbsoluteZeroAdapter.java
            │       │   ├── AntidoteIIAdapter.java
            │       │   ├── AntidoteIIIAdapter.java
            │       │   ├── ApocalypseFlameAdapter.java
            │       │   ├── AreaRuntimeAdapter.java
            │       │   ├── AreaTickAdapter.java
            │       │   ├── AtmosphereDominionAdapter.java
            │       │   ├── AtmosphereDominionFieldEffect.java
            │       │   ├── BlastAdapter.java
            │       │   ├── BlastTerrainAdapter.java
            │       │   ├── BlizzardStormAdapter.java
            │       │   ├── BlockBurstImpactAdapter.java
            │       │   ├── BoraBlastAdapter.java
            │       │   ├── BurningPlaceAdapter.java
            │       │   ├── CarrierDamageAdapter.java
            │       │   ├── CarrierFlightAdapter.java
            │       │   ├── CarrierTickAdapter.java
            │       │   ├── ChannelDebtAdapter.java
            │       │   ├── ChannelRuntimeAdapter.java
            │       │   ├── ChargeReservationAdapter.java
            │       │   ├── ColumnPlacementAdapter.java
            │       │   ├── CombatSummonBehavior.java
            │       │   ├── CommunicationAdapter.java
            │       │   ├── ConfinementBarrierAdapter.java
            │       │   ├── ConfinementBarrierEffect.java
            │       │   ├── ContactCooldownRuntimeAdapter.java
            │       │   ├── ContinentalSinkAdapter.java
            │       │   ├── ContinentalSinkGroundContact.java
            │       │   ├── CumulonimbusAdapter.java
            │       │   ├── DeepMistAdapter.java
            │       │   ├── DemonGodSealEffect.java
            │       │   ├── DemonGodSealingBarrierAdapter.java
            │       │   ├── DetoxificationAdapter.java
            │       │   ├── DisturbMagicAdapter.java
            │       │   ├── DivinationMagicAdapter.java
            │       │   ├── DivineJudgeAdapter.java
            │       │   ├── DivineJudgmentAdapter.java
            │       │   ├── DragonAuraAdapter.java
            │       │   ├── DualShieldAdapter.java
            │       │   ├── DualShieldEffect.java
            │       │   ├── DustStormAdapter.java
            │       │   ├── EarthFortressAdapter.java
            │       │   ├── EarthHedgehogAdapter.java
            │       │   ├── EarthLanceAdapter.java
            │       │   ├── EarthLanceConstructionAdapter.java
            │       │   ├── EarthLanceLiftAdapter.java
            │       │   ├── EarthPillarAdapter.java
            │       │   ├── EarthPillarFallAdapter.java
            │       │   ├── EarthPillarStopAdapter.java
            │       │   ├── EarthWallAdapter.java
            │       │   ├── EarthquakeAdapter.java
            │       │   ├── EarthquakeGroundContact.java
            │       │   ├── EffectCarrierSpawner.java
            │       │   ├── ElectricAdapter.java
            │       │   ├── EntityImpulseAdapter.java
            │       │   ├── ExHealingAdapter.java
            │       │   ├── ExcavatingGroundTerrain.java
            │       │   ├── ExodusFlameBurstAdapter.java
            │       │   ├── ExorcistBurstAdapter.java
            │       │   ├── ExorcistJudgeAdapter.java
            │       │   ├── ExorcistRayAdapter.java
            │       │   ├── ExplosionImpactAdapter.java
            │       │   ├── FiniteBarrierEffect.java
            │       │   ├── FiniteBarrierRuntimeAdapter.java
            │       │   ├── FlamePillarAdapter.java
            │       │   ├── FlamePillarBurnRuntimeAdapter.java
            │       │   ├── FlameStormAdapter.java
            │       │   ├── FlameTornadoAdapter.java
            │       │   ├── FlamethrowerAdapter.java
            │       │   ├── FlashOverAdapter.java
            │       │   ├── FortressBarrierAdapter.java
            │       │   ├── FortressBarrierEffect.java
            │       │   ├── FrostNovaAdapter.java
            │       │   ├── GodPurgeAdapter.java
            │       │   ├── GravityMagicAdapter.java
            │       │   ├── GravityMagicFieldEffect.java
            │       │   ├── GroundContactEffect.java
            │       │   ├── GroundFieldRuntimeAdapter.java
            │       │   ├── GroundSummonBehavior.java
            │       │   ├── GroundTerrainEffect.java
            │       │   ├── GrowingColumnRuntimeAdapter.java
            │       │   ├── HealingAdapter.java
            │       │   ├── HeatHandAdapter.java
            │       │   ├── HeatIslandAdapter.java
            │       │   ├── HolyInstantAdapter.java
            │       │   ├── HolyRayAdapter.java
            │       │   ├── HolySanctuaryAdapter.java
            │       │   ├── HowlingAdapter.java
            │       │   ├── HypnosisAdapter.java
            │       │   ├── IceFortressAdapter.java
            │       │   ├── IceLanceAdapter.java
            │       │   ├── IceLanceCarrierEffect.java
            │       │   ├── IceLanceHitAdapter.java
            │       │   ├── IcePillarAdapter.java
            │       │   ├── IcicleFieldAdapter.java
            │       │   ├── IgnitingGroundContact.java
            │       │   ├── IllusionAdapter.java
            │       │   ├── IllusionBarrierAdapter.java
            │       │   ├── InfernoAdapter.java
            │       │   ├── LamplightSpiritsAdapter.java
            │       │   ├── LamplightSummonBehavior.java
            │       │   ├── LightningAdapter.java
            │       │   ├── MagicActionReceiver.java
            │       │   ├── MagicShieldAdapter.java
            │       │   ├── MagmaGushAdapter.java
            │       │   ├── MaintainedFieldEffect.java
            │       │   ├── MaintainedFieldRuntimeAdapter.java
            │       │   ├── MonsterSummonAdapter.java
            │       │   ├── MudRainAdapter.java
            │       │   ├── NecromancyAdapter.java
            │       │   ├── NuclearExplosionAdapter.java
            │       │   ├── NuclearExplosionFieldEffect.java
            │       │   ├── OceanFreezeAdapter.java
            │       │   ├── OceanFreezeRuntimeAdapter.java
            │       │   ├── PhaseProjectileAdapter.java
            │       │   ├── PhysicalShieldAdapter.java
            │       │   ├── PhysicalShieldCollisionAdapter.java
            │       │   ├── PhysicalShieldRuntimeAdapter.java
            │       │   ├── PlayerStateAdapter.java
            │       │   ├── PoweredBarrierEffect.java
            │       │   ├── PoweredBarrierRuntimeAdapter.java
            │       │   ├── PreparationRuntimeAdapter.java
            │       │   ├── ProjectileLaunch.java
            │       │   ├── QuagmireAdapter.java
            │       │   ├── SacredNovaAdapter.java
            │       │   ├── SandCloudAdapter.java
            │       │   ├── SandCloudFlightAdapter.java
            │       │   ├── SandstormAdapter.java
            │       │   ├── ServerInputSessionStore.java
            │       │   ├── SkyTempestAdapter.java
            │       │   ├── SmokeFieldRuntimeAdapter.java
            │       │   ├── SonicBlastAdapter.java
            │       │   ├── SpellModeSelectionAdapter.java
            │       │   ├── SpellRaycastAdapter.java
            │       │   ├── SpellVisibilityAdapter.java
            │       │   ├── SpiritProtectionAdapter.java
            │       │   ├── SpiritSealingBarrierAdapter.java
            │       │   ├── SpiritSealingBarrierEffect.java
            │       │   ├── SpiritSummonAdapter.java
            │       │   ├── SpiritSummonBehavior.java
            │       │   ├── SquallAdapter.java
            │       │   ├── StationaryCarrierComposition.java
            │       │   ├── StationaryCarrierEffect.java
            │       │   ├── StationaryContactEffect.java
            │       │   ├── StoneCannonProjectileAdapter.java
            │       │   ├── SummonBehavior.java
            │       │   ├── SummonCorpseRepository.java
            │       │   ├── SummonRuntimeAdapter.java
            │       │   ├── SummonSpawnAdapter.java
            │       │   ├── SummonUpkeepAdapter.java
            │       │   ├── SupportConsentAdapter.java
            │       │   ├── SupportRayAdapter.java
            │       │   ├── SweptProjectileHitAdapter.java
            │       │   ├── SweptProjectileImpactAdapter.java
            │       │   ├── SweptProjectileSpawner.java
            │       │   ├── SweptProjectileStepAdapter.java
            │       │   ├── TeleportationAdapter.java
            │       │   ├── TemporaryTerrainAdapter.java
            │       │   ├── TerraFallAdapter.java
            │       │   ├── TerraFallGroundContact.java
            │       │   ├── TerraFallGroundTerrain.java
            │       │   ├── TerrainPermissionAdapter.java
            │       │   ├── TornadoAdapter.java
            │       │   ├── TornadoImpactHitAdapter.java
            │       │   ├── VacuumStormAdapter.java
            │       │   ├── VanillaProjectileAdapter.java
            │       │   ├── ViolentStormAdapter.java
            │       │   ├── WaterShieldAdapter.java
            │       │   ├── WaterShieldCollisionAdapter.java
            │       │   ├── WaterShieldContactAdapter.java
            │       │   ├── WaterSplashAdapter.java
            │       │   ├── WaterfallCarrierEffect.java
            │       │   ├── WaterfallSourceAdapter.java
            │       │   ├── WindBindAdapter.java
            │       │   ├── WindBlastAdapter.java
            │       │   ├── WindFieldRuntimeAdapter.java
            │       │   └── WindSliceHitAdapter.java
            │       └── persistence/
            │           └── MagicStateCodec.java
            ├── item/
            │   ├── BasicAttackMagicPrimerItem.java
            │   ├── MagicTextbookItem.java
            │   ├── ManaCrystalItem.java
            │   ├── ModItems.java
            │   ├── RoxyPantsuItem.java
            │   └── ZenithGreyratMagicBookItem.java
            ├── magic/
            │   ├── AbsoluteZero.java
            │   ├── BarrierGeometry.java
            │   ├── ChantLessons.java
            │   ├── ChantPayload.java
            │   ├── ChantScore.java
            │   ├── DirectionalSpellCarrier.java
            │   ├── DivineCircle.java
            │   ├── FlameSliceEntity.java
            │   ├── FlameSliceFlight.java
            │   ├── FlameSliceVolume.java
            │   ├── FlashOver.java
            │   ├── FrostNova.java
            │   ├── Inferno.java
            │   ├── LightLeases.java
            │   ├── MagicActionPayload.java
            │   ├── MagicAim.java
            │   ├── MagicAreaEntity.java
            │   ├── MagicAttachments.java
            │   ├── MagicBooks.java
            │   ├── MagicChantEffects.java
            │   ├── MagicCommands.java
            │   ├── MagicConfig.java
            │   ├── MagicControl.java
            │   ├── MagicCores.java
            │   ├── MagicDamage.java
            │   ├── MagicEffects.java
            │   ├── MagicFieldCadence.java
            │   ├── MagicFlight.java
            │   ├── MagicGlow.java
            │   ├── MagicProjectileDamage.java
            │   ├── MagicRules.java
            │   ├── MagicSounds.java
            │   ├── MagicSpikes.java
            │   ├── MagicState.java
            │   ├── MagicSystem.java
            │   ├── MagicTerrain.java
            │   ├── MagicWaterSources.java
            │   ├── MagmaGush.java
            │   ├── ParticlePacketAdapter.java
            │   ├── PermanentTerrainAdapter.java
            │   ├── PlayerSpellRuntime.java
            │   ├── Quagmire.java
            │   ├── SegmentedChantScore.java
            │   ├── SequentialMagic.java
            │   ├── SonicBlast.java
            │   ├── SpellExecutionBindings.java
            │   ├── SpellExecutors.java
            │   ├── SpellHooks.java
            │   ├── SpellParticles.java
            │   ├── SpellSweep.java
            │   ├── SpellVisualPayload.java
            │   ├── SpellVisuals.java
            │   ├── StoneCharge.java
            │   ├── SweptSpellProjectile.java
            │   ├── TerrainLeaseRepository.java
            │   └── WaterBallEntity.java
            ├── mixin/
            │   ├── MagicArrowAccessor.java
            │   ├── MagicFreezeMixin.java
            │   ├── MagicMovementMixin.java
            │   ├── MagicProjectileMixin.java
            │   ├── MagicProtectionMixin.java
            │   ├── MagicSleepMixin.java
            │   ├── MagicSpawnMixin.java
            │   ├── MagicSummonMixin.java
            │   ├── MagicTargetingMixin.java
            │   └── MagicTerrainLeaseMixin.java
            └── ExampleMod.java
src/client/java/
└── com/
    └── example/
        └── examplemod/
            ├── client/
            │   ├── magic/
            │   │   ├── DirectionalSpellRenderer.java
            │   │   ├── FlameSliceRenderer.java
            │   │   ├── IllusionProjection.java
            │   │   ├── LayeredPixelMeshRenderer.java
            │   │   ├── LocalRainAtmosphere.java
            │   │   ├── MagicAreaRenderer.java
            │   │   ├── MagicBookScreen.java
            │   │   ├── MagicCastMotion.java
            │   │   ├── MagicClient.java
            │   │   ├── MagicClientNetworking.java
            │   │   ├── MagicHandGlow.java
            │   │   ├── MagicHud.java
            │   │   ├── MagicInputController.java
            │   │   ├── MagicKeyBindings.java
            │   │   ├── MagicKnowledgeScreen.java
            │   │   ├── MagicPoseState.java
            │   │   ├── MagicRendererRegistration.java
            │   │   ├── MagicScreenStyle.java
            │   │   ├── MagicWheelScreen.java
            │   │   ├── ReferenceMeshRepository.java
            │   │   ├── ReferenceSpellModels.java
            │   │   ├── SpellAtmosphere.java
            │   │   ├── SpellMeshModel.java
            │   │   ├── SpellParticleDensity.java
            │   │   ├── SpellSpriteParticle.java
            │   │   ├── SpellTextureAnimation.java
            │   │   ├── SpellVisualAnimations.java
            │   │   ├── SpellVisualProfile.java
            │   │   ├── SpellVolumeEffects.java
            │   │   ├── SweptSpellRenderer.java
            │   │   └── WaterBallRenderer.java
            │   ├── render/
            │   │   ├── BlockbenchArmorRenderer.java
            │   │   └── ModArmorRenderers.java
            │   └── ExampleModClient.java
            └── mixin/
                ├── MagicExtractPoseMixin.java
                ├── MagicFirstPersonMixin.java
                ├── MagicFogMixin.java
                ├── MagicHumanoidMixin.java
                ├── MagicIllusionBlockEntityMixin.java
                ├── MagicIllusionBlocksMixin.java
                ├── MagicIllusionFluidMixin.java
                ├── MagicIllusionRenderMixin.java
                ├── MagicRenderStateMixin.java
                └── MagicWindBoundRenderMixin.java
```

magic-core只用JDK；Fabric世界/Codec/Mixin与客户端渲染保持在根项目源集。测试冻结旧实现只在测试源集中，生产JAR不包含。
