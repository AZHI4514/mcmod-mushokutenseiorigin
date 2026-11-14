# 102术唯一迁移账本

保持原ALL顺序；每术只分配一个阶段。水弹先由R05试点，其余按依赖顺序执行。代码迁移状态与游戏验收分开；全部游戏验收当前NOT_RUN。

| SpellId | 类别 | 唯一阶段任务 | 状态 | 当前入口/证据 |
|---|---|---|---|---|
| `water_ball` | water | R05-water_ball | DONE（准备/释放试点） | tasks/R05-water_ball.md；evidence/r05-build.log；实体载体未改，D-004已澄清 |
| `fireball` | fire | R06-fireball | DONE（准备/释放切片） | tasks/R06-fireball.md；evidence/r06-water-ice-build.log；原版世界适配未验收 |
| `wind_ball` | wind | R06-wind_ball | DONE（准备/释放切片） | tasks/R06-wind_ball.md；evidence/r06-water-ice-build.log；原版世界适配未验收 |
| `rock_bullet` | earth | R06-rock_bullet | DONE（准备/释放切片） | tasks/R06-rock_bullet.md；evidence/r06-water-ice-build.log；原版世界适配未验收 |
| `water_arrow` | water | R06-water_arrow | DONE（准备/释放切片） | tasks/R06-water_arrow.md；evidence/r06-water-ice-build.log；原版世界适配未验收 |
| `ice_smash` | water | R06-ice_smash | DONE（准备/释放切片） | tasks/R06-ice_smash.md；evidence/r06-water-ice-build.log；原版世界适配未验收 |
| `flame_slice` | fire | R06-flame_slice | DONE（核心/准备/释放切片） | tasks/R06-flame_slice.md；evidence/r06-flame-ice-build.log；原版世界验收NOT_RUN |
| `healing` | healing | R08-healing | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `HealingAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `water_shield` | water | R07-water_shield | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-water_shield.md；evidence/r07-water-shield-final-audit.md |
| `ice_blade` | water | R06-ice_blade | DONE（核心/准备/释放切片） | tasks/R06-ice_blade.md；evidence/r06-flame-ice-build.log；原版世界验收NOT_RUN |
| `heat_hand` | fire | R07-heat_hand | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-heat_hand.md；evidence/r07-heat-hand-audit.md |
| `heat_island` | fire | R07-heat_island | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-heat_island.md；evidence/r07-heat-island-audit.md |
| `burning_place` | fire | R07-burning_place | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-burning_place.md；evidence/r07-burning-place-audit.md |
| `detoxification` | detox | R08-detoxification | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `DetoxificationAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `magic_shield` | barrier | R07-magic_shield | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-magic_shield.md；evidence/r07-magic-shield-audit.md |
| `water_cannon` | water | R06-water_cannon | DONE（准备/释放与纯规则切片） | tasks/R06-water_cannon.md；evidence/r06-water-cannon-build.log；D-005及世界验收待续 |
| `water_splash` | water | R06-water_splash | DONE（即时范围施法切片） | tasks/R06-water_splash.md；evidence/r06-water-splash-build.log；世界验收NOT_RUN |
| `icicle_field` | water | R07-icicle_field | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-icicle_field.md；evidence/r07-icicle-field-audit.md |
| `ex_flame` | fire | R06-ex_flame | DONE（准备/释放与纯规则切片） | tasks/R06-ex_flame.md；evidence/r06-ex-flame-build.log；世界验收NOT_RUN |
| `sonic_boom` | wind | R06-sonic_boom | DONE（规格/准备/释放切片） | tasks/R06-sonic_boom.md；evidence/r06-sonic-build.log；世界载体尚未全部迁移 |
| `stone_cannon` | earth | R06-stone_cannon | 切片离线PASS（注魔/费用/规则/服务器会话） | tasks/R06-stone_cannon.md；evidence/r06-stone-final-build.log；实体世界与存档验收NOT_RUN |
| `ice_pillar` | water | R06-ice_pillar | 切片离线PASS（准备/释放/柱体规则） | tasks/R06-ice_pillar.md；evidence/r06-ice-pillar-build.log；世界服务/逐格生长与租约未全迁 |
| `waterfall` | water | R06-waterfall | SLICE_DONE（核心/协调；非全术） | tasks/R06-waterfall.md；evidence/r06-waterfall-lifecycle-audit.md |
| `ice_lance` | water | R06-ice_lance | SLICE_DONE（核心/协调；非全术） | tasks/R06-ice_lance.md；evidence/r06-ice-lance-final-build.log |
| `icicle_break` | water | R06-icicle_break | SLICE_DONE（核心/协调；非全术） | tasks/R06-icicle_break.md；evidence/r06-icicle-edge-start-build.log |
| `icicle_edge` | water | R06-icicle_edge | SLICE_DONE（核心/协调；非全术） | tasks/R06-icicle_edge.md；evidence/r06-icicle-edge-exit-audit.md |
| `flood_flush` | water | R06-flood_flush | SLICE_DONE（核心/协调；非全术） | tasks/R06-flood_flush.md；evidence/r06-flood-flight-audit.md |
| `ice_fortress` | water | R06-ice_fortress | SLICE_DONE（核心/协调；世界边界待验收） | tasks/R06-ice_fortress.md；evidence/r06-fortress-ownership-build.log |
| `explosion` | fire | R06-explosion | SLICE_DONE（核心/协调；世界边界待验收） | tasks/R06-explosion.md；evidence/r06-explosion-entry-audit.md |
| `flame_pillar` | fire | R06-flame_pillar | SLICE_DONE（核心/协调；世界边界待验收） | tasks/R06-flame_pillar.md；evidence/r06-flame-pillar-build.log |
| `flamethrower` | fire | R06-flamethrower | SLICE_DONE（核心/协调；世界边界待验收） | tasks/R06-flamethrower.md；evidence/r06-flamethrower-build.log |
| `wind_slice` | wind | R06-wind_slice | SLICE_DONE（结构等价；D-006未决） | tasks/R06-wind_slice.md |
| `wind_bind` | wind | R06-wind_bind | SLICE_DONE（结构切片；世界未验收） | tasks/R06-wind_bind.md；evidence/r06-wind-bind-pulse-build.log |
| `wind_blast` | wind | R06-wind_blast | SLICE_DONE（结构切片；世界未验收） | tasks/R06-wind_blast.md |
| `earth_lance` | earth | R06-earth_lance | SLICE_DONE（核心/协调结构切片；世界未验收） | tasks/R06-earth_lance.md |
| `earth_pillar` | earth | R06-earth_pillar | SLICE_DONE（核心/协调结构切片；世界未验收） | tasks/R06-earth_pillar.md |
| `ex_healing` | healing | R08-ex_healing | SLICE_DONE（核心/协调；世界未验收） | tasks/R06-ex_healing.md（历史命名）；evidence/r06-heal-effect-build.log |
| `antidote_ii` | detox | R08-antidote_ii | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `AntidoteIIAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `physical_shield` | barrier | R07-physical_shield | SLICE_DONE（结构切片；共享实体及世界验收仍待续） | tasks/R06-physical_shield.md |
| `blizzard_storm` | water | R07-blizzard_storm | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-blizzard_storm.md；evidence/r07-blizzard-audit.md |
| `squall` | water | R07-squall | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-squall.md；evidence/r07-squall-audit.md |
| `exodus_flame` | fire | R06-exodus_flame | SLICE_DONE（核心/协调/适配；非全术） | tasks/R06-exodus_flame.md |
| `tornado_impact` | wind | R06-tornado_impact | SLICE_DONE（核心/协调；非全术） | tasks/R06-tornado_impact.md |
| `cumulonimbus` | water | R07-cumulonimbus | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-cumulonimbus.md；evidence/r07-storm-audit.md |
| `lightning` | water | R07-lightning | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-lightning.md；evidence/r07-lightning-audit.md |
| `electric` | water | R07-electric | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-electric.md；evidence/r07-electric-audit.md |
| `frost_nova` | mixed | R06-frost_nova | SLICE_DONE（核心/协调/适配；世界待验收） | tasks/R06-frost_nova.md |
| `sonic_blast` | wind | R06-sonic_blast | SLICE_DONE（核心/协调/适配；世界待验收） | tasks/R06-sonic_blast.md |
| `earth_wall` | earth | R07-earth_wall | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-earth_wall.md；evidence/r07-earth-wall-audit.md |
| `earth_fortress` | earth | R07-earth_fortress | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-earth_fortress.md；evidence/r07-earth-fortress-audit.md |
| `earth_hedgehog` | earth | R07-earth_hedgehog | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-earth_hedgehog.md；evidence/r07-hedgehog-audit.md |
| `quagmire` | mixed | R07-quagmire | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-quagmire.md；evidence/r07-quagmire-audit.md |
| `blast` | mixed | R06-blast | SLICE_DONE（核心/协调/适配；世界待验收） | tasks/R06-blast.md |
| `bora_blast` | mixed | R06-bora_blast | SLICE_DONE（核心/协调/适配；世界待验收） | tasks/R06-bora_blast.md |
| `magma_gush` | fire | R07-magma_gush | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-magma_gush.md；evidence/r07-magma-audit.md |
| `violent_storm` | wind | R07-violent_storm | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-violent_storm.md；evidence/r07-violent-storm-audit.md |
| `tornado` | wind | R07-tornado | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-tornado.md；evidence/r07-tornado-audit.md |
| `vacuum_storm` | wind | R07-vacuum_storm | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-vacuum_storm.md；evidence/r07-vacuum-audit.md |
| `sky_tempest` | wind | R07-sky_tempest | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-sky_tempest.md；evidence/r07-sky-tempest-audit.md |
| `flash_over` | fire | R07-flash_over | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-flash_over.md；evidence/r07-flash-audit.md |
| `flame_tornado` | mixed | R07-flame_tornado | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-flame_tornado.md；evidence/r07-flame-tornado-audit.md |
| `inferno` | fire | R07-inferno | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-inferno.md；evidence/r07-inferno-audit.md |
| `absolute_zero` | water | R07-absolute_zero | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-absolute_zero.md；evidence/r07-zero-audit.md |
| `exorcist_ray` | holy | R07-exorcist_ray | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-exorcist_ray.md；evidence/r07-exorcist-ray-audit.md |
| `exorcist_burst` | holy | R07-exorcist_burst | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-exorcist_burst.md；evidence/r07-exorcist-burst-audit.md |
| `exorcist_judge` | holy | R07-exorcist_judge | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-exorcist_judge.md |
| `holy_ray` | holy | R07-holy_ray | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-holy_ray.md |
| `sacred_nova` | holy | R07-sacred_nova | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-sacred_nova.md |
| `divine_judge` | holy | R07-divine_judge | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-divine_judge.md |
| `god_purge` | holy | R07-god_purge | SLICE_DONE（核心/协调/适配；世界未验收） | tasks/R07-sacred_nova.md；共享HolySanctuaryAdapter |
| `flame_storm` | fire | R07-flame_storm | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `FlameStormAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `apocalypse_flame` | fire | R07-apocalypse_flame | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `ApocalypseFlameAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `earthquake` | earth | R07-earthquake | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `EarthquakeAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `terra_fall` | earth | R07-terra_fall | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `TerraFallAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `continental_sink` | earth | R07-continental_sink | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `ContinentalSinkAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `sand_cloud` | earth | R07-sand_cloud | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `SandCloudAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `dust_storm` | earth | R07-dust_storm | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `DustStormAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `sandstorm` | earth | R07-sandstorm | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `SandstormAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `deep_mist` | mixed | R07-deep_mist | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `DeepMistAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `mud_rain` | mixed | R07-mud_rain | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `MudRainAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `antidote_iii` | detox | R08-antidote_iii | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `AntidoteIIIAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `howling` | special | R08-howling | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `HowlingAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `hypnosis_magic` | special | R08-hypnosis_magic | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `HypnosisAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `atmosphere_dominion` | wind | R07-atmosphere_dominion | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `AtmosphereDominionAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `gravity_magic` | special | R08-gravity_magic | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `GravityMagicAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `nuclear_explosion` | mixed | R07-nuclear_explosion | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `NuclearExplosionAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `dual_shield` | barrier | R07-dual_shield | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `DualShieldAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `spirit_sealing_barrier` | barrier | R07-spirit_sealing_barrier | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `SpiritSealingBarrierAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `confinement_barrier` | barrier | R07-confinement_barrier | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `ConfinementBarrierAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `disturb_magic` | special | R08-disturb_magic | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `DisturbMagicAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `divination_magic` | special | R08-divination_magic | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `DivinationMagicAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `monster_summon` | summon | R08-monster_summon | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `MonsterSummonAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `spirit_summon` | summon | R08-spirit_summon | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `SpiritSummonAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `lamplight_spirits` | summon | R08-lamplight_spirits | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `LamplightSpiritsAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `necromancy` | special | R08-necromancy | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `NecromancyAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `fortress_barrier` | barrier | R07-fortress_barrier | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `FortressBarrierAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `demon_god_sealing_barrier` | barrier | R07-demon_god_sealing_barrier | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `DemonGodSealingBarrierAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `saint_dragon_battle_aura` | special | R08-saint_dragon_battle_aura | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `DragonAuraAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `communication_magic` | special | R08-communication_magic | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `CommunicationAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `teleportation_magic` | special | R08-teleportation_magic | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `TeleportationAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `illusion_barrier` | barrier | R08-illusion_barrier | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `IllusionAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |
| `ocean_freeze` | water | R07-ocean_freeze | IN_PROGRESS（独立释放已接；结构/世界收尾待验） | `OceanFreezeAdapter`；evidence/r10-execution-binding-source-audit.json；evidence/r10-smoke-modes-shapes-build.log |

爆音冲击波：SonicBlastRules纯几何规则已接兼容入口；无新增模式层、无空Factory；世界命中/地形/付费仍待迁，保持IN_PROGRESS。证据r06-sonic-rules-build.log（63/352）。

## 爆音冲击波结构切片收尾（2026-11-13）

SonicBlastHit/Terrain与SonicBlastAdapter已接真实SelfEffectComposition，保留40MP/240准备/20恢复、伤害成功与免疫顺序、近零冲量、完整可见快照后最多128成功破坏。旧兼容cast仅委托，不重复扣费；无新增实体tick或保存状态。结构SLICE_DONE，残留兼容入口/共享准备容器与实际世界验收边界见architecture/evidence/r06-sonic-structure-exit.md。

新增18项合同，完整离线build通过：63单测/370模块测试、旧15,658断言、102定义/教材和资源/JAR检查通过。证据r06-sonic-exit-build.log及JUnit汇总/XML。首轮测试事件记录变量遮蔽已修正，保留失败日志。合同不是Fabric/世界/事件/视觉/联机/性能验收，以上NOT_RUN。

下一R06-blast已建卡，仅源码审计、尚未迁代码；先核对有效准备/恢复，再抽30度热风锥规则与命中协调。整体架构重构仍未完成。不启动游戏、不Git操作。

## 突风结构切片收尾（2026-11-13）

BlastRules/Hit/Adapter已接SelfEffectComposition与既有准备/单次付款，16MP/160准备/10恢复保持；热风无标准点燃/破坏/反冲，寒冷突风旧分支未迁。结构SLICE_DONE，兼容与生命周期边界见architecture/evidence/r06-blast-structure-exit.md。新增17项模块合同及10,000锥形差分，完整离线build通过：63单测/387模块测试、15,658旧断言、102定义/教材、资源/JAR通过。证据r06-blast-build.log及JUnit汇总/XML。

下一任务R06-bora_blast，仅源码审计尚未迁代码；关键保持伤害后才读取温暖/湿身，与冰霜新星的伤害前快照不同。实际世界/Fabric/Mixin/视觉/联机/性能NOT_RUN，整体架构未完成。不启动游戏、不Git操作。

## 寒冷突风结构切片收尾（2026-11-13）

BoraBlastRules/Hit/Adapter已接SelfEffectComposition，38MP/240准备/20恢复及伤害后温暖/湿身读取顺序保持。新增21项模块合同与10,000随机几何差分通过；完整离线build退出0：63单测/408模块测试，15,658旧断言、102有效定义/教材/资源/JAR通过。证据r06-bora-build.log及JUnit汇总/XML，结构退出边界见architecture/evidence/r06-bora-structure-exit.md。

下一唯一任务R06-exit-audit：核对Intermediate/PhaseTwo/共享准备等残留业务，先形成逐项可执行任务再决定进入R07，不能将单术切片全部通过等同整阶段完成。ThermalGust仍有兼容contains及其他输入fallback，未删除。世界/Fabric/Mixin/事件/Codec/视觉/联机/性能NOT_RUN；不启动游戏、不Git操作。

## 准备会话服务器归属切片（2026-11-13）

MagicPreparation进程全局ACTIVE已删除，PreparationRegistry<Pending>由服务器主世界preparation_runtime附件拥有，仅initializer、无persistent/sync/copyOnDeath。initialize在MagicSystem初始化注册附件；原逐玩家cancel顺序保持，停服逐玩家清理后removeAttached，回收剩余引用。所有get/contains/put/remove从当前玩家所属服务器读取。Pending、准备计时、心跳、付款、取消及特殊术分支未改；这不是整个MagicPreparation业务拆分完成。

4项新增模块合同覆盖同UUID两实例隔离、替换/删除身份、清理/重建、取消一个实例不影响另一个。首次测试误认为显式cancel后Idle.tick还应发布CANCELLED；修正为不能READY，保留现有State生产语义，失败日志r06-preparation-initial-failure.log。最终完整build退出0：63单测/412模块测试、15,658旧断言、102有效定义/教材、资源/JAR及架构边界通过，证据r06-preparation-build.log和JUnit汇总/XML。测试只验证核心实例容器及CastingSession组合，真实Fabric附件/停服/跨维/游戏/Mixin/视觉/联机/性能NOT_RUN。

残余矩阵evidence/r06-residual-matrix.md已记录共享Intermediate/PhaseTwo/准备协调等未迁职责，R06仍未整体退出。下一R06-intermediate-runtime先审计共享扫掠/墙流体/未加载/历史/Codec的旧顺序，建立窄协调切片，不改变TNT、水穿透或费用例外。不游戏、不Git。

模式实审：Registry为实例容器，不冒称新GoF模式；服务器附件负责生命周期归属，核心无Fabric依赖。既有Command/State角色未改，未造空Factory。

## 共享弹体终止协调检查点（2026-11-13）

ProjectileTermination已接IntermediateProjectile最后终止分支：流体<=distance且<=wall优先，position→particles→discard；否则wall<=distance且有block时position→wallEffect→particles→discard。核心无SpellId，匿名World保留狱炎碰墙爆发、随后重新读取owner及水炮/大火球blockImpact；异常保留已发生副作用，不自动discard或落入后续分支。无终止才原setPos(to)/traveled累加。命中历史、Codec、区块/年龄、风枪地形先于流体、候选排序等未改，仍待迁。

新增8项模块合同（3边界/含10,000差分、5动作顺序/异常），完整离线build退出0：63单测/420模块测试；旧15,658断言、102定义/教材、资源/JAR及架构边界通过。证据r06-terminal-build.log、JUnit汇总/XML。合同不是世界/Fabric/Mixin/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS，下一精确动作提取最终生物接触循环协调：保持contact重算、狱炎skipContact与历史短路、非狱炎先claim再setPos/hurt、piercing与非piercing各自particles/非piercing重复定位和discard顺序；先扩任务卡允许路径再实现。不启动游戏、不Git。

设计审查：窄World端口为真实世界动作的Adapter边界，纯协调只决定顺序，不添加新模式、通用解释器或空Factory；旧载体剩余职责未清。

## 共享弹体生物接触协调（2026-11-13）

ContactTraversal已接IntermediateProjectile排序后的目标循环，逐目标重算contact，再由skip窄端口保留狱炎历史短路；非狱炎原Set先claim后定位/伤害，狱炎仍由爆发使用原Set。piercing循环前快照；穿透每目标effect后particles，非穿透effect后重复定位→particles→discard→return。核心不分派SpellId，异常保留已发生副作用且不补执行后续动作。候选/排序、世界伤害和爆发、终止、年龄/区块、存档/注册/资源保持。

新增8项核心合同验证顺序、非穿透停止、跳过、后续接触重算、爆发对后续历史的影响、伤害/粒子异常和空列表。完整离线build退出0：63单测/428模块测试，旧15,658断言、102有效定义/教材、资源/JAR及架构边界通过。证据r06-contact-build.log与JUnit汇总/XML。测试桩只验证端口/核心组合，不是实际Minecraft/Fabric/Mixin/伤害/Codec/视觉/联机/性能验收，上述NOT_RUN。

R06-intermediate-runtime仍IN_PROGRESS。下一精确动作审计并提取候选收集/排序协调，保持各术contact与history求值次序、strict小于墙流体、稳定同距顺序；先更新允许路径和合同再实现。共享载体扫掠/区块/Codec等仍待迁，不能称R06完成。不游戏、不Git。

实审：ContactTraversal负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

候选筛选排序已迁ContactCandidates，保留原筛选表达式及求值次序、实时contact稳定排序，不缓存或新增同距排序键。新增5合同、1000组冻结排序差分；完整离线build通过63单测/433模块测试、15,658旧断言、102定义/教材/资源/JAR。证据r06-candidates-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计命中历史保存恢复及MagicVictims UUID容错、PaidMana/ExpansionBroken与年龄边界，先定义合同再迁移。不游戏、不Git。

实审：ContactCandidates负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

命中历史字符串格式已迁HitHistoryFormat并由MagicFlight委托。保留MagicVictims及Codec.STRING.listOf、先clear再Supplier读取、UUID.fromString容错、原Set身份与部分异常语义。新增5项合同；完整离线build通过63单测/438模块测试、15,658旧断言、102定义/教材/资源/JAR。证据r06-history-build.log及JUnit汇总/XML。真实Codec重载/世界/Fabric/Mixin/视觉/联机/性能NOT_RUN。PaidMana、ExpansionBroken及MagicAge仍由原载体保存，未改默认值或clamp；下一核对并提取这些读取归一化的纯规则及旧默认值合同。R06-intermediate-runtime仍IN_PROGRESS，不游戏、不Git。

实审：HitHistoryFormat负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

共享弹体存档计数边界已迁SavedProjectileCounters，IntermediateProjectile保持原读取默认值及逐字段立即赋值顺序；MagicAge非负无新上限，ExpansionBroken仍clamp0..64，PaidMana仍由StoneCannonRules处理且默认10。未改Codec/保存键/注册/资源。新增4项模块合同及10000组冻结计数差分；完整离线build通过63单测/442模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-saved-counters-build.log及JUnit汇总/XML。实际世界/Codec重载/Fabric/Mixin/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS。下一审计并迁扫掠区块可用性检查，保持枚举顺序、首次未加载退出、狱炎和风枪discard而其他暂停、age仅通过预检后递增；先扩任务卡再实现。不游戏、不Git。

实审：SavedProjectileCounters负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

扫掠区块可用性检查已迁SweptChunkGate，IntermediateProjectile使用窄Loaded回调读取原hasChunkAt，保持x外z内、floor转int再右移、首次未加载退出。失败后才判断狱炎/风枪销毁，其他暂停，成功后才++age；不加载新区块，不改法术/资源/存档。新增4项核心合同及1000组冻结枚举差分；完整离线build通过63单测/446模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-chunk-gate-build.log及JUnit汇总/XML。测试只验证核心枚举与短路，真实Fabric/区块卸载/世界/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计固体碰撞候选收集与首墙/impact选点协调，保留水穿透跳过、接触<=步长才记contacts但contact<wall仍选墙的原分支、同距首个shape和永久地形先于流体；先定义端口及边界合同再迁移。不游戏、不Git。

实审：SweptChunkGate负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

固体接触合并与首墙选择已迁SolidContacts，保持contact<=步长才合并HashMap、contact<wall才更新墙和impact、同距取首个形状、含水过滤和世界枚举不变，地形仍先于流体。新增6项核心合同及1000组冻结差分；完整离线build通过63单测/452模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-solid-contacts-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一审计流体候选扫描及固体终止交接，保持读取时点、未加载跳过及流体同距优先，先定义端口再迁移。不游戏、不Git。

实审：SolidContacts负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

流体扫描已迁FluidContactScan并由MagicFlight窄World适配。保持loaded→fluid→empty→ignoresWater→water短路、同一FluidState高度读取、逐项Math.min且零距离不提前停止；近零运动和方块枚举仍原世界层。新增5项模块合同；完整离线build通过63单测/457模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-fluid-scan-build.log及JUnit汇总/XML。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一核对已迁核心协调与载体剩余职责，建立完整服务端单步协调及窄世界适配合同，保持年龄、地形、流体、生物、终止顺序；不游戏、不Git。

实审：FluidContactScan负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

ProjectileServerStep已协调寿命和几何准备后的完整服务端扫掠：区块检查→年龄递增→固体和永久地形→流体→生物→边界终止→移动。世界匿名适配仍负责原API和单术差异，不分派到通用解释器；未加载不推进年龄，目标终止或边界终止均跳过后续移动，异常原样传播且不回滚。新增6项核心合同；完整离线build通过63单测/463模块测试、15658旧断言、102定义/教材/资源/JAR。证据r06-server-step-build.log及JUnit汇总/XML。首次提取多余括号导致编译失败，已修正并重跑全构建，保留初次日志。实际世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS：寿命、几何选择、世界匿名适配和保存载体仍未全迁。下一明确世界Adapter归属与载体窄状态端口，先审计避免扩大实体公共可变接口；不游戏、不Git。

实审：ProjectileServerStep负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

IntermediateStepAdapter已承接服务端世界单步操作，原命中Set直接传入，年龄和破坏计数及距离通过窄回调注入，实体字段保持私有，无新增公共setter。原区块/年龄/地形/流体/目标/终止顺序、原注册和保存键不变。完整离线build通过63单测/463模块测试、15658旧断言、102有效定义/教材/资源/JAR。证据r06-step-adapter-build.log及JUnit汇总/XML。未新增测试，以原核心合同和编译/基线回归验证适配迁移，不声称世界集成。用户决定全部重构完成后统一游戏实测；世界/Fabric/Mixin/Codec/视觉/联机/性能仍NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，寿命和几何选择/保存载体尚待收尾，下一审计这些剩余职责并形成明确退出清单，不游戏、不Git。

实审：ProjectileServerStep负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

IntermediateStepAdapter.prepareAndApply已承接原寿命检查与扫掠几何准备，载体tick保留super/removed、客户端轨迹，服务端只注入原命中Set和私有计数回调。保留原寿命表达式短路、体积尺寸、速度、区块/年龄/地形/流体/目标/终止顺序，未改变注册和保存格式。完整离线build通过63单测/463模块测试、15658旧断言、102定义/教材/资源/JAR；未新增测试，本批为世界适配迁移并运行原合同回归。证据r06-step-preparation-build.log及JUnit汇总/XML。用户明确全部重构完再统一实机测试；世界/Fabric/Mixin/Codec/视觉/联机/性能NOT_RUN。R06-intermediate-runtime仍IN_PROGRESS，下一核对载体保存及适配器单术分支归属，补结构退出清单后推进IntermediateSpells命中适配，不能称R06全部完成。不游戏、不Git。

实审：ProjectileServerStep负责目标循环顺序，匿名World适配实际游戏动作；不是通用魔法解释器或新模式，未添加空Factory。

## 中级普通命中协调（2026-11-13）

IntermediateHit 纯协调与 IntermediateHitAdapter 已接 IntermediateSpells.hurt；风枪龙卷仍使用独立 TornadoImpactHitAdapter。保持伤害成功后才判断击退并读取 motion、附效时才读取 owner，按 hurt→条件 knockback→effect→afterHit 原顺序执行。失败立即停止，异常原样传播、不回滚、不补调用。8项模块合同验证成功/拒绝、零击退、伤害后状态读取与三阶段异常截断。

完整离线 build 退出0：63单测/471模块测试，旧15,658断言、102有效定义、39/102教材、旧玩家存档fixture、资源与JAR/架构边界通过。证据 r06-intermediate-hit-build.log 及对应JUnit汇总/XML。端口测试不等于真实Fabric伤害/事件集成；游戏/Codec重载/视觉/联机/性能 NOT_RUN，全部重构完成后统一游戏测试，不启动游戏、不Git。

共享弹体运行时 STRUCTURE_SLICE_DONE，退出归属见 evidence/r06-intermediate-runtime-structure-exit.md；整体R06未完成。当前唯一任务 R06-intermediate-effects IN_PROGRESS。下一精确动作：审计 IntermediateSpells.execute 的剩余 TARGET_AREA fallback 与 blockImpact 其他输入fallback的真实调用及兼容合同，再拆范围命中/可见性世界适配；禁止仅凭注册路径无调用删除兼容行为。

## 中级范围结算和世界效果结构切片（2026-11-13）

DeferredAreaImpact纯核心负责按查询顺序筛选、wet快照、伤害及效果/事件，全部命中完成后才依次encase，最后burst。IntermediateAreaAdapter承接原12格加载检查/选点/半径6/效果及地形；保持兼容其他输入和water分支，不改数值。9项模块合同覆盖多目标先命中后造冰、筛选/伤害失败、湿身快照、效果决定入队、空目标和事件/效果/地形异常截断。

SpellVisibilityAdapter承接原加载采样和COLLIDER/NONE射线；9个已迁Adapter直接调用它，未迁世界类保留IntermediateSpells.visible委托。IntermediateImpactAdapter保留水炮/大火球/其他输入碰块分流及原读取时点，视觉burst后才读取owner播放音效。未扩大对其地形/视觉的测试声称。

完整离线build退出0：63单测/480模块测试、15,658旧行为断言、102定义和39/102教材、旧存档fixture、架构/资源/JAR检查通过，证据r06-intermediate-area-build.log及JUnit汇总/XML。游戏/Fabric/Codec重载/视觉/联机/性能NOT_RUN；按用户要求全部重构完成后统一实测。不启动游戏、不Git。

R06-intermediate-effects结构切片STRUCTURE_SLICE_DONE，不等于R06整体退出。IntermediateSpells保留弹体构造/执行分流及hurt分流；发布装配统一归R06-release-routing，实体核心/注册ID/资源未变，icicle_field不因此记为整术迁移完成。下一唯一任务R06-phase-two-runtime：先审计PhaseTwoEntity服务端tick、扫掠/停留/保存及已迁单术端口，建立职责清单后拆分；R07领域/障壁的未迁业务仍随单术处理。

## PhaseTwo服务端预检和分派协调（2026-11-13）

PhaseTwoServerTick纯核心已接PhaseTwoTickAdapter与原实体tick，保留super/updateBounds先行、低于世界discard、当前区块检查、owner读取、stationary无owner暂停、移动ahead检查、寿命检查、age递增及shield→stationary→fall→fly顺序。stationary重复读取保持原次序；不新增isRemoved短路。适配器注入age/remaining/anchor快照及窄回调，不增公共setter。落石内部第二次loaded失败仍发生在age递增之后；客户端/存档/注册/资源不改。

新增12项模块合同，完整离线build退出0：63单测/492模块测试、15,658旧行为断言、102有效定义与39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-tick-build.log和JUnit汇总/XML。核心端口合同不是Fabric/真实世界/Codec重载/事件/视觉/联机/性能验收，以上NOT_RUN。用户要求全部重构完成后统一游戏测试，不启动游戏、不Git。

R06-phase-two-runtime仍IN_PROGRESS：stationaryTick、fall、fly的内部世界业务和共享候选/终止协调、保存归一化尚待逐项迁移。下一精确动作审计fly候选过滤/排序及接触循环，用已有ContactCandidates等核心时保持PiercingFlightRules与其他分支求值次序，尤其爆炸contact后的动态piercing重读与风裂独立AOE历史。先扩任务卡再实施，不因本分派切片关闭整个R06。

## PhaseTwo飞行/驻留/落石结构切片（2026-11-13）

PhaseTwoFlightAdapter/StationaryAdapter/FallAdapter已承接原世界操作；载体只委托并注入原ImpactHistory、私有计数回调与方向/age快照。候选复用ContactCandidates筛完后实时contact稳定排序，保留历史求值次序；PhaseTwoContactTraversal先claim，爆炸定位后读取piercing→explode→重读piercing，不额外burst/discard；普通命中effect→burst→piercing→条件discard。风裂AOE继续使用同一ImpactHistory。PhaseTwoTermination保留流体同距优先、爆炸墙前0.02定位后explode、普通墙burst后discard。存档age/remaining复用原等价非负规则，默认值及逐字段赋值、历史最后替换保持。

18项新增模块合同（接触10、终止8），完整离线build通过：63单测/510模块测试、15,658旧断言、102有效定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-runtime-build.log及JUnit汇总/XML。结构切片不代表真实世界/Codec重载/视觉/联机/性能验收，以上NOT_RUN。用户授权持续执行全部重构，全部完成后统一游戏测试；不启动游戏、不Git。

R06-phase-two-runtime结构切片STRUCTURE_SLICE_DONE：实体同步/Codec/客户端与体积身份仍是载体，新增Adapter的世界API/单术分支继续依赖各自已有核心规则。几何身份配置与零引用兼容最终归R10，不能称整个R06完成。下一R06-phase-two-effects：迁damage/areaDamage的权限→伤害→附效→hook→洪流击退协调及共享加载/射线适配；准备/发布装配仍后续专卡。未把R07/R08未迁法术计为已完成。

## PhaseTwo共享伤害与目标查询结构切片（2026-11-13）

PhaseTwoHit和PhaseTwoDamageAdapter承接权限短路→伤害→附效→hook→洪流条件击退，读取spell/owner/level的时点保持。6项模块合同验证顺序、拒绝及异常；areaDamage仍逐目标可见后伤害，末尾一次粒子。PhaseTwoTargetingAdapter承接aim/aimedEntity/trace/traceFrom，loaded复用SweptChunkGate.available（传入回调的是块坐标）。trace的同距后者胜与aimedEntity的同距首者胜保持独立。首轮编译接口名误写loaded已修复，失败日志留在工作目录。构造/预检/释放、plan/construct/permanentStone/launch兼容世界入口归后续release-routing及R10审计，未标整阶段完成。

完整离线build退出0：63单测/516模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-phase-two-effects-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R06-channel-runtime，拆服务端通道容器/准入/初次提交与tick适配，保留水源首次成功和火焰先登记再发射的顺序。

## 持续通道运行时归属与首次提交（2026-11-13）

ChannelRuntimeAdapter承接原服务器附件容器、资格预检、heartbeat、begin/stop/tick及兼容generateWater，channel_debt/channel_runtime ID与持久属性不变。ChannelAdmission纯核心保证begin成功→register→emit→sound，6项合同覆盖拒绝/异常及提交后副作用保留。PhaseTwoRuntime保持columns/burns/shield/prune在通道前运行，停服移除其他附件顺序不变。WaterfallChannel/FlamethrowerChannel已有费用/心跳/停止合同继续通过。只是共享通道结构切片，真实附件重载/多服务器集成未验收。

完整离线build退出0：63单测/522模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-channel-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R06-release-routing：清理PhaseTwoSpells剩余构造/计划/释放世界业务，审计已迁R06全部装配和兼容路径，再处理准备资格与通知协调。

## 准备资格和释放适配结构收尾（2026-11-13）

PreparationEligibility纯核心保留counter/silence/玩家状态/GUI/冷却/选择/知识/装槽/MP/事件的原短路顺序，4项合同含13个拒绝位置验证。PreparationRuntimeAdapter持服务器准备附件及原特殊仪式协调，MagicPreparation仅兼容委托；未改变剩余R07/R08特殊分支。PhaseTwoConstructionAdapter/ReleaseAdapter和IntermediateReleaseAdapter承接原构造/兼容释放世界业务，PhaseTwoSpells/IntermediateSpells现仅签名委托/选择。六个无状态查询/仪式服务开放跨包调用，不增加实体公共setter；首次访问编译失败已修复，原失败日志保留。R06的33术装配源码逐项核对见r06-release-bindings.json：这是接线证据，非实际世界验收。已完成R06共享核心/协调结构切片；R10仍须清理兼容批次命名和实例几何身份，所有世界边界NOT_RUN。

完整离线build退出0：63单测/526模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r06-preparation-routing-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-water_shield：按账本顺序建立逐术卡，审计水幕体积/续期/容量/接触湿身/弹体拦截及全局实例归属后迁独立核心和适配。

## 水盾结构切片及领域注册表服务器归属（2026-11-13）

WaterShieldRules/Contact/Absorb/Selection与WaterShieldAdapter/ContactAdapter/CollisionAdapter已接实际准备付款、实体接触及服务器弹体拦截；保持6MP/100准备/0恢复/80持续、锚点strict<1E-6、容量8、每接触drag后mark/首次claim后湿身、每20tick原Map清理。最近strict<同距首项，流体/地形不改；共享ShieldInterception已由物理盾与水盾双调用，先地形→incoming/absorb→三叉戟类型→反弹/销毁。原资源/ID/数值不变。独立R07-area-runtime把全局ACTIVE改成AreaRegistry服务器附件，仅initializer，五个清理调用传入player/server；6项核心隔离/异常合同不是Fabric附件集成。新添29项合同（水盾命令6、规则/接触/吸收8、选择4、共享拦截5、注册表6），本切片结构离线PASS；实际世界/同服多人/生命周期仍未验收。

完整离线build退出0：63单测/555模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-water-shield-final-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-heat_hand：审计交互权限、工具/方块处理、持续资格与付款/准备，提纯规则及独立世界Adapter；其后按账本heat_island/burning_place推进。

## 灼热手核心规则与世界适配（2026-11-13）

HeatHandRules保存命中面2×2 a外b内轴映射及冰/蒸发转化，HeatHandMelt保存所有格非短路setBlock后读取warmth、changed短路frozen再温暖及末尾逐格粒子顺序；HeatHandAdapter保存4格OUTLINE及权限，兼容入口委托。SelfEffectComposition接独立2MP/100准备/0恢复命令。新增14项模块合同（8规则/协调、6付款/取消/部分异常），原200温暖/方块资源数值不改，无Recipe/工具分支凭空新增。世界方块权限/环境属性仍未实机验收。

完整离线build退出0：63单测/569模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-heat-hand-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-heat_island：拆半径3区域温暖、清湿身及固定锚点续期，保持200持续和现有效statusTicks；随后燃烧领域。

## 地热区域规则与独立释放（2026-11-13）

HeatIslandRules保持目标getX/Z到领域中心的半径3含边界判断，10000随机冻结算式差分通过；HeatIslandAdapter先removeWet后MagicEffects.warm，非攻击不新增PVP过滤。6MP/100准备/0恢复和独立SelfEffect命令已接，200领域及原statusTicks保持。新增10项模块合同（规则/顺序4，准备/付款6）。共享Area实体owner/剩余/加载和原地续期职责仍后续公共生命周期任务，不把领域世界行为称已实测。

完整离线build退出0：63单测/579模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-heat-island-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-burning_place：抽每目标接触/权限/可见/claim/伤害/点燃事件顺序，保留原全身接触体和20tick尝试冷却。

## 燃烧领域贴身接触协调（2026-11-13）

BurningPlaceContact/Adapter保留history→全身接触→权限→原clip可见→claim→hurt→effects→hook，伤害拒绝仍消耗本目标本次20tick尝试，异常不回滚claim。贴身owner包围盒inflate(.12)、自身排除与PVP、原无chunk采样的可见射线不变。10MP/100准备/0恢复独立命令已接，80持续/2伤害/原随机点燃不变；删除原实体零调用的局部几何/可见帮助函数。新增14项模块合同（8命中协调、6付款），共享Area生命周期仍单独收尾。

完整离线build退出0：63单测/593模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-burning-place-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-magic_shield：抽魔法直伤允许事件/容量吸收及独立准备付款，随后icicle_field；共享Area生命周期另卡处理。

## 魔法盾直伤边界与共享容量吸收（2026-11-13）

魔法盾独立SelfEffect/准备付款，原伤害事件位置委托懒查询适配。仅已知魔法Projectile直接伤害进入盾检查；有同level有效盾则吸收并拒绝伤害。三盾共享容量扣除→显示→重新读取容量→移除协调，保留异常副作用和8点容量，不共用几何资格。8MP/100准备/0恢复/100持续不变。

完整离线build退出0：63单测/603模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-magic-shield-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R07-icicle_field独立领域选点、湿身快照和延迟冰封；随后共享领域生命周期。

## 冰霜领域独立选点与延迟冰封（2026-11-13）

IcicleFieldRules/Adapter接入TargetedArea Command及Preparing State；保持14MP/5伤害/12选点/6半径和旧paidMana，未生成飞行实体。复用DeferredAreaImpact，湿身先快照，hurt成功才附效及移湿并hook，全部命中完成再冰封，最后burst。新增6项边界/异常合同及10000几何差分。首次测试错误使用禁止NaN的Vec3d已改成标量比较合同，失败日志保留。

完整离线build退出0：63单测/609模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-icicle-field-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-area-lifecycle拆短期领域服务端时序，随后blizzard_storm。

## 短期领域生命周期与世界接触分离（2026-11-13）

AreaTick抽纯短路生命周期，AreaTickAdapter拥有查询和接触副作用，实体保留私有计数/anchor/接触容器并提供窄State回调。无owner/removed/dead/跨维不减寿命，寿命先减再查chunk；到期不查chunk。位移阈值及NaN比较、follow→pulse→prune→contacts均原序。8项合同覆盖短路/边界/异常；扩展领域仍各自tick和存档，不误套短期规则。

完整离线build退出0：63单测/617模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-area-lifecycle-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-blizzard_storm独立节拍、跟随/adopt和旧保存到期规则，随后squall。

## 吹雪独立节拍与旧到期保存合同（2026-11-13）

BlizzardStormTick/Rules与世界Adapter接入独立SelfEffect和准备；释放首帧立即tick、跟随后adopt，拒绝和异常不增pulse，80tick内8次伤害脉冲。保留water cadence claim→hurt→附效→hook及原可见射线，36MP/2伤害不变。旧ExpansionSpell/Pulse/Expiry键和默认80/0、绝对时间恢复不变。8项模块合同覆盖短路、节拍、异常和整数/long边界。

完整离线build退出0：63单测/625模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-blizzard-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-squall局部降雨生命周期/暴露/时长规则，随后cumulonimbus。

## 局部降雨独立生命周期与暴露规则（2026-11-13）

SquallRules/Tick与独立Adapter接真实SelfEffect/Preparing，LocalMagicRain为兼容API。900/450时长、48格逐段碰撞、16半径、10tick节拍、清火→湿身300不变。RainExposure抽懒半径→chunk→天空→清火→湿身协调。RainExpiry旧键与shorten保留，7项模块合同覆盖短路、时长、暴露、异常。弱实体索引仍为适配器私有，不声称完成真实世界生命周期测试。

完整离线build退出0：63单测/632模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-squall-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-cumulonimbus纯排程/受害冷却/生命周期与世界查询分离，随后lightning/electric。

## 积雨云独立排程与受害冷却状态（2026-11-13）

CumulonimbusTick/Rules/State与世界Adapter、SelfEffect准备接入。保留旧短路和未检查removed的原边界，40tick落雷间隔先写next再清victims，选点失败仍消耗本次落雷；80tick受害尝试先claim再cadence/hurt。1200时长/24半径/48高/16随机候选、天空/伤害/视觉与旧保存键默认不变。9项模块合同覆盖排程、短路、状态实例隔离和异常。

完整离线build退出0：63单测/641模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-storm-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-lightning保持remaining15锁点和重复validPoint，抽命中控制与独立付款，随后electric。

## 雷光锁定落点与命中控制分离（2026-11-13）

LightningRules/Impact与独立Adapter/Command装配，保留remaining15锁点、每3tick警示与释放前及命中前二次validPoint；不重新按最终准星定位。220MP/32伤害/64距离/2水平半径32高与自身误伤特例不变。天空→非自身权限→可见→hurt→control→hook原序；纯规则7项合同覆盖短路和边界。实际Fabric伤害及锁点预警视觉未运行。

完整离线build退出0：63单测/648模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-lightning-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-electric抽单目标选择/免疫分支/命中协调，随后earth_wall与earth_fortress。

## 电击独立目标选择与免疫命中分支（2026-11-13）

ElectricRules/Impact与ElectricAdapter接入SelfEffect/Preparing，ThunderMagic仅双术兼容API。32格原逐段碰撞/卸载停止、严格最近<保留首个同距。无目标false可退款；有目标hurt拒绝仍burst，成功immune则缓慢60/1否则玩家40生物60控制，再hook。260MP/10伤害及世界查询原序不变。6项模块合同覆盖命中、拒绝、免疫、异常和距离比较。

完整离线build退出0：63单测/654模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-electric-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_wall有序布局纯化与原租约入口，随后earth_fortress。

## 土墙独立有序布局与租约适配（2026-11-13）

EarthWallLayout纯5×3有序布局接独立Adapter/SelfEffect；保留24格逐段选点、面法向.02偏移、主轴>=同距决策与y外x内顺序。原1200/true租约预检/排队和首格burst不变。4项模块合同包含10000随机方向逐格有序差分，世界权限/租约实际执行尚未游戏验收。

完整离线build退出0：63单测/658模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-earth-wall-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_fortress纯半球壳/门洞/稳定排序与独立适配，随后earth_hedgehog。

## 土堡纯半球壳与门洞有序布局（2026-11-13）

EarthFortressLayout接独立Adapter/SelfEffect，保留y/x/z遍历、9..16平方半径、两格高方向门洞、稳定按层/平方距排序，最多200计划与原2400/false租约。4项模块合同含1000随机方向及9轴向完整有序差分、门洞/空心/不可变性。实际世界租约权限和恢复仍NOT_RUN。

完整离线build退出0：63单测/662模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-earth-fortress-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-earth_hedgehog纯布局/processed状态/命中计数与世界tick分离，随后quagmire。

## 土刺独立时序与命中尝试状态（2026-11-13）

EarthHedgehogRules/State/Tick与世界Adapter/SelfEffect接入，12刺黄金角偏移原精度，400寿命/2tick递进、严格age==i*2不补漏帧。processed先写再世界处理、命中计数先写再hurt，受伤拒绝仍占两次预算。旧Spikes保存键和附件不变，原基地搜索及三层非短路碰撞保留。8项合同覆盖时序、状态隔离、计数与异常。

完整离线build退出0：63单测/670模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-hedgehog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-quagmire表面体积/跳跃cap/免疫附效和领域生命周期，随后magma_gush。

## 泥沼体积、运动限制与免疫接触分离（2026-11-13）

QuagmireRules/Contact/Tick与Adapter/SelfEffect接入；保留半径6与y[-.2,.65]、向上.115限制不下拉、400到期、owner允许受影响/他人权限与可见、首领仅弱慢和普通先慢后束缚。表面.025偏移/MudExpiry与原运动Mixin API保留。8项模块合同覆盖几何、运动、免疫、短路和异常。

完整离线build退出0：63单测/678模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-quagmire-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-magma_gush独立首帧/余焰排程、processed与自伤火焰分支，随后violent_storm。

## 熔岩喷涌首帧与余焰排程分离（2026-11-13）

MagmaGushRules/State/Tick/Hit与Adapter/SelfEffect接入。首帧10伤、20..160八次2伤、>160到期，processed在damage判断前写。保留火cadence先claim再hurt，owner随机采样/抗火检查/300点燃和其他目标附效分支、scorch→tick→burst、旧保存默认及附件。9项合同覆盖排程、几何、命中原序及异常。

完整离线build退出0：63单测/687模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-magma-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：先R07-wind-runtime共享依赖，再按violent_storm/tornado/vacuum_storm/sky_tempest逐术策略和合同，flame_tornado按账本位置。

## 风领域共享生命周期与冲量规则（2026-11-13）

WindFieldTick/State/Rules抽共享生命周期、20tick排程、圆柱和归一化冲量，World Adapter保留原世界查询和领域相消。移动失败只discard不立即删弱状态；pulse更新next在重叠抑制前；接触完再相消。旧WindStart/Expiry/Next/Direction和原注册ID不变。9项模块合同通过，未将五术标完成，逐术Strategy和命中继续。

完整离线build退出0：63单测/696模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-wind-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-violent_storm独立策略/命中协调，然后tornado/vacuum_storm/sky_tempest，其他术按账本。

## 暴风独立策略与领域接触协调（2026-11-13）

ViolentStormPolicy作为真实Strategy持有16×16、owner排除、pulse外推.2及相消资格，独立SelfEffect绑定。WindFieldContact抽excluded→geometry→owner/permission→visible→pulse→immune→move原序，免疫每tick读时点不改。6项模块合同验证自身排除、免疫不挡伤害、异常阻止运动及策略；其余风术仍LegacyPolicy临时路径待逐卡替换，不声明全部完成。

完整离线build退出0：63单测/702模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-violent-storm-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-tornado独立移动/拉拽策略和脉冲协调，随后vacuum_storm/sky_tempest。

## 龙卷独立移动拉拽策略与脉冲命中（2026-11-13）

TornadoPolicy独立5×20移动/每pulse拉拽策略及SelfEffect接入，原首帧不移动、后续.1移动/loaded检查和同owner火龙卷重叠抑制留在世界适配。WindFieldPulse抽claim→hurt→附效→hook→真空慢原序，6项模块合同覆盖拉拽方向、玩家上升cap和伤害拒绝/异常。原数值/资源/保存键不变。

完整离线build退出0：63单测/708模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-tornado-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-vacuum_storm独立策略与拒绝伤害仍慢合同，随后sky_tempest。

## 真空暴风独立策略与拒伤减速合同（2026-11-13）

VacuumStormPolicy独立12×12固定圆柱，无移动/相消，SelfEffect/Preparing绑定。WindFieldPulse保持cadence或hurt拒绝仍加500tick慢，成功hook后慢，异常不继续附效。5项合同验证拒伤、拒cadence、hook异常与策略；260MP/6伤害/300持续及旧Wind保存键不变。

完整离线build退出0：63单测/713模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-vacuum-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-sky_tempest独立中心伤害和每tick切向冲量，随后flash_over。

## 天空风暴中心伤害与切向冲量策略（2026-11-13）

SkyTempestPolicy独立24×40、中心半径8伤7/外4、每tick切向.08上升.04、玩家y<=.35与自身可伤规则。独立SelfEffect，pulse仅相消雨/积雨云，暴风烟/火特例不扩大。4项合同覆盖中心包端点/NaN、每tick移动与360方向切向及玩家限速。620MP/600持续和旧Wind保存键不变。

完整离线build退出0：63单测/717模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-sky-tempest-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-flash_over首帧双伤与余焰独立排程，随后flame_tornado/inferno/absolute_zero。

## 闪燃独立首帧双伤与余焰状态（2026-11-13）

FlashOverRules/State/Contact与Adapter/SelfEffect接入，首帧16爆发和3余焰均执行，余焰八次，flash不claim、pulse先claim火20。flashed和next在世界命中前写，原owner随机/抗火/400点燃和保存键默认不变。共享ExpiringFieldTick仅state/expiry优先的相同生命周期族；8合同覆盖短路、首帧双伤、默认旧状态与异常。

完整离线build退出0：63单测/725模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-flash-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-flame_tornado独立火焰/上升策略并移除临时LegacyPolicy，随后inferno。

## 火龙卷独立火焰与上升策略（2026-11-13）

FlameTornadoPolicy独立5×18移动/火脉冲策略及SelfEffect绑定，上升最终仍用原速度+.06，玩家.35上限。成功hurt后火附效再hook，自身原随机/抗火/500点燃不变。五风术策略全部显式绑定并删除临时LegacyPolicy，公共几何帮助对非风术保留旧默认16。5项模块合同通过，原300MP/7伤害/240持续不变。

完整离线build退出0：63单测/730模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-flame-tornado-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-inferno独立规则、401到期与20次余焰，复用实际相同爆发排程后absolute_zero。

## 炼狱独立规则与真实共享爆发排程（2026-11-13）

InfernoRules/Adapter/SelfEffect接入，首帧18，20..400二十次5伤、401结束，半径20高8和scorch20/2048/401保留。提炼实际相同BurstFieldSchedule/Contact共享时序，闪燃保留兼容核心API并重跑合同。旧Inferno保存键默认及owner随机/抗火/600点燃不变；5新增合同验证独立规则、时序和跳帧不补发。

完整离线build退出0：63单测/735模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-inferno-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-absolute_zero湿身增伤/暖身快照/首次冻结控制与后续冷雾，随后holy逐术。

## 绝对零度首次冻结与冷雾顺序分离（2026-11-13）

AbsoluteZeroRules/Contact与Adapter/SelfEffect接入，首次28+湿6，warm先wet快照；hurt成功才移湿，warm只移暖，否则canFreeze后冻结并控制。后续冷雾无伤，仅无warm/可冻才随机.8；初次拒伤不落入冷雾。160/半径16及freezeSurface16/4096/1200和旧Zero保存默认不变。7合同覆盖资格、快照、控制和异常。

完整离线build退出0：63单测/742模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-zero-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-holy-runtime共享几何/世界适配，随后exorcist_ray/exorcist_burst/exorcist_judge/holy_ray逐术。

## 圣系共享几何与资格适配（2026-11-13）

HolyGeometry与HolyEligibility抽纯柱体/懒资格，HolyInstantAdapter持原plan/instant/visual，HolyMagic兼容API保留TARGETS/DAMAGE身份。SacredNova/DivineJudge仅公开原无状态服务以便跨包调用，无字段setter。3项合同含10000方向投影差分、边界及玩家/死亡/owner/tag读取短路；四即时术尚待逐术策略，不标整体迁移完成。

完整离线build退出0：63单测/745模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-exorcist_ray独立单体策略，随后exorcist_burst/exorcist_judge/holy_ray。

## 驱魔射线独立最近单体策略（2026-11-13）

ExorcistRayPolicy独立单体/窄光束策略和SelfEffect准备绑定，严格最近<同距首个，资格/墙/卸载原序、hurt失败仍成功与原visual，无新增hook。2项合同覆盖最近比较及窄光束参数，四术策略剩余临时分支继续逐卡清理。

完整离线build退出0：63单测/747模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-ray-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：R07-exorcist_burst终点球形/可见伤害协调，随后exorcist_judge/holy_ray。

## 驱魔爆破独立终点球形命中（2026-11-13）

ExorcistBurstPolicy与HolyAreaHit接入终点4半径球、标签资格、形状、可见、hurt顺序；不画beam且hurt拒绝仍成功。3项模块合同覆盖懒读取和异常。

完整离线build退出0：63单测/750模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-burst-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续R07-exorcist_judge与holy_ray逐术策略。

## 驱魔审判独立单体策略与接口隔离（2026-11-13）

单体审判策略已绑定；HolyVolumePolicy仅范围术实现形状，HolyNearest严格比较保留同距首个。两项合同及完整构建通过。

完整离线build退出0：63单测/752模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-exorcist-judge-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成holy_ray独立策略后继续sacred_nova。

## 圣光射线独立柱形策略（2026-11-13）

HolyRayPolicy、HolyRayAdapter接入2半径柱形命中和宽光束视觉；移除HolyInstantComposition最后LegacyPolicy，未支持法术显式拒绝。

完整离线build退出0：63单测/754模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-ray-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成圣域及神之净化共用生命周期适配，继续divine_judge。

## 圣域与神之净化纯规则及适配迁移（2026-11-13）

HolySanctuaryAdapter保留WeakHashMap、NovaExpiry和初始伤害/击退顺序；独立16/48范围规则、自然生成抑制短路、环形节拍已提纯，SacredNova/GodPurge均有SelfEffect绑定。首次编译发现旧包内HolyMagic不可见，改依赖同包HolyInstantAdapter后完整构建通过。

完整离线build退出0：63单测/760模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-holy-fields-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成divine_judge脉冲模块，审计god_purge后继续剩余R07。

## 神之审判三脉冲状态与协调（2026-11-13）

DivineJudgeTick按原次序完成状态/时间/归属检查，提交count后命中；独立规则保留60tick三段、柱体和加载clamp。6项合同覆盖跳帧不补发、异常不重放及短路。

完整离线build退出0：63单测/766模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-divine-judge-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：迁移火土领域五种Strategy，再抽取实际命中与生命周期模块。

## 五种火土领域规则与实际生命周期命中协调（2026-11-13）

五术独立GroundFieldPolicy与SelfEffect已绑定；GroundFieldTick/GroundFieldContact提纯生命周期和命中顺序，原世界/地形/视觉保留于GroundFieldRuntimeAdapter，旧ExpansionFields仅兼容门面。17项领域合同及10,000组火墙差分通过；各术副作用Strategy和地形仍待后续收尾，不能标整项完成。

完整离线build退出0：63单测/783模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-ground-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：烟雾视距状态和五术模块拆分，随后审计火土各术副作用Strategy与地形。

## 烟雾五术生命周期与视距协调（2026-11-13）

FogSight纯化强弱叠加和目标获取；SmokeFieldTick/SmokeContact接入实际五术生命周期，飞行撞击帧不再进入雾分支（原行为保留）。五术各自Strategy和SelfEffect适配，旧SmokeFields保留存档与Mixin入口；12项合同通过。各术效果策略/服务器归属仍待后续结构收尾。

完整离线build退出0：63单测/795模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-smoke-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成ocean_freeze独立扩散/扫描/命中模块后处理模式与障壁。

## 海洋冻结纯状态与命中模块（2026-11-13）

独立OceanFreezeState/Rules/Hit与Fabric适配已接入；半径/扫描/配额/加载clamp、先claim后hurt、UUID容错均保留。6项模块合同通过，真实水面/租约恢复仍未实测。

完整离线build退出0：63单测/801模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-ocean-freeze-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：提纯模式选择及核爆额外费用，再迁移大气/重力/核爆独立运行模块。

## 三种模式术独立规则与持续费用协调（2026-11-13）

ModeSelection及三术规则、MaintainedFieldTick、NuclearExplosionTick、AtmosphereContact、GravityContact均接实际路径。保留核爆额外支付、先schedule后upkeep、费用失败只discard、核爆先标记后targets/terrain及原命中副作用。18项合同通过；world状态/模式attachment隔离与实际费用模块接线仍待后续审计。

完整离线build退出0：63单测/819模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-mode-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：迁移障壁几何与独立模块；随后R08辅助功能及R09/R10收尾。

## 有限障壁容量与移动弹体协调（2026-11-13）

双重盾/灵体封印/拘束各自规则与SelfEffect适配接入；BarrierCapacity、BarrierDamage/Movement/Projectile纯协调保持旧顺序。三叉戟先反弹后击破、保留容量独立与旧存档默认；13项合同通过。供能障壁另拆，真实Mixin和事件未启动验收。

完整离线build退出0：63单测/832模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-barrier-runtime-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：龙圣斗气独立后继续城塞/魔神封印供能模块，然后R08其他功能。

## 龙圣斗气独立与核心障壁供能协调（2026-11-13）

龙圣斗气独立adapter和tick规则接入；城塞FortressUpkeep保留真实takeEnergy及100tick断供宽限；魔神封印目标资格与PoweredBarrierTick已抽离。13项合同通过，核心能量和保存键不变。

完整离线build退出0：63单测/845模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r07-powered-barrier-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：完成治疗/解毒/同意与声音模块；已发现高阶阵面成功消费接线遗漏，立即修复并补合同后统一归档。

## 治疗解毒支持同意独立模块及阵面回归（2026-11-13）

治疗/初中高级解毒、SupportConsent、SupportRay独立；移除批次执行器内相应业务，保留最近目标后资格判定和非短路移除。补修成功SelfEffect释放后阵面消费，顺序为效果→阵面→通知，失败不消费；13项合同通过。

完整离线build退出0：63单测/858模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-healing-support-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆howling/hypnosis，再处理效用、召唤、通信、转移、幻影。

## 声音效用幻影独立模块及召唤规则（2026-11-13）

Howling/Hypnosis、Disturb/Divination各有独立实际适配与纯协调；幻影保留先付费后schedule的历史差异并接入独立SelfEffect。召唤slot/灯灵信用/尸体expiry/精灵防护规则已提纯；19项合同通过。

完整离线build退出0：63单测/877模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-sound-utility-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续召唤生命周期/归属/付费，随后通信和转移。R09/R10及先前切片结构收尾仍未完成。

## 召唤归属支付生命周期与四术释放端口（2026-11-13）

SummonTick接实际owner/level/alive/expiry/slot校验和schedule→pay→act；四术独立SelfEffect已接入，原UUID/attachment保留。首次跨包编译发现LightLeases窄操作不可见，开放update/release后通过完整构建，未暴露状态。5项合同通过，四术专属AI与世界共享适配仍待结构收尾。

完整离线build退出0：63单测/882模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-summon-lifecycle-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：通信草稿服务器隔离与发送协调，再迁移转移邀请/安全预检/原子移动。

## 通信转移与71项释放绑定真实接线复核（2026-11-13）

通信草稿与转移邀请已由服务器实例持有，纯资格/安全/发送/移动回滚合同接入。审计发现此前部分SelfEffect接线替换静默失败，已以71项显式绑定修复；冰结领域paidMana恢复历史10；高阶成功释放补回阵面消费。结构切片通过，不代表R07/R08共享世界分支或R09/R10全部完成。

完整离线build退出0：63单测/896模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r08-routing-reconciliation-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：将102术释放绑定覆盖纳入构建，移除局部效果装配条件链，隔离输入会话服务器归属；随后继续逐术世界职责与客户端拆分。

## 102术绑定构建门禁、服务器输入归属与客户端职责拆分（2026-11-13）

构建检查102术唯一释放绑定与完整覆盖，检查实际SelfEffect适配器文件存在；局部5术与选点4术装配改为显式Map。输入会话由overworld运行时附件持有，纯InputSessions保留先提交sequence再限流语义，3项隔离/生命周期合同通过。HUD、知识、轮盘、共享样式、渲染注册、网络传输拆为独立职责，MagicScreens无引用后删除；轮盘几何提核，2项合同覆盖1—10槽中心及边界。实体注册唯一归属MagicEntityTypes，MagicSystem保留18个公开兼容别名及原注册顺序/参数。首次客户端安装因教材样式引用未迁而中止，补全后构建通过；Loom一次缓存锁重建失败，离线重试成功。R09和R10仅部分职责完成，SpellExecutors条件链、共享世界适配、客户端视觉和旧批次类仍需继续。

完整离线build退出0：63单测/901模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-routing-client-ownership-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续102术资格/已付费释放模块注册，替代SpellExecutors条件链；随后逐术世界策略、运行状态归属、视觉采样和旧批次职责清理。

## 执行注册、领域世界归属、纯视觉采样与火土地形策略（2026-11-13）

SpellExecutors改为兼容门面，102项显式资格/已付费释放绑定在启动核查覆盖与重复，保留付费调用边界；源码表达式审计见r10-execution-binding-source-audit.json。18处领域索引改为level附件，障壁/圣域查询显式使用目标world；学习与组合会话由server持有，粒子预算由level持有。附件延迟注册，避免纯函数测试初始化Fabric Mixin；首次verifyMagic发现此问题已修复并全测。无业务依赖的ExampleMixin及配置项删除，其他Mixin保留。70项既有氛围配置移入纯核显式Map，粒子渐隐/成长/漂移/帧和4tick字母采样已接；新增4项合同含40004组角色采样。火土5术的命中/次数/节拍/伤害策略和燃烧/震动/塌陷/落石副作用分离，3项合同；地形开挖/落石填土独立适配，原权限/租约/配额不变。仍有烟雾/模式/障壁/召唤共享分支、旧批次载体/视觉职责、定义规格及完整逐术账本收尾，未完成全部重构。

完整离线build退出0：63单测/908模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-ownership-visual-ground-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分烟雾五术、模式三术及召唤专属世界副作用；继续视觉profile/采样、旧批次职责与逐术账本审计，不启动游戏、不Git。

## 烟雾逐术规格、三模式世界更新与18种载体形状（2026-11-13）

烟雾5术视距/残留/颜色/减速/泥雨束缚/伤害/天空暴露/粒子数量/发射属性由逐术纯规格提供，原class中的术ID分支移除；砂云扫掠与转雾世界算法独立，保持接触当帧return。大气/重力/核爆三术世界更新独立Strategy，生命周期仍共享，模式选择/费用和形状后续继续拆。18种旧批次载体形状移入纯核EffectShapeCatalog，实体不再维护四串形状分支。增加5项模块合同；执行注册表在verifySpellBaseline中实际初始化，资格/已付费释放与视觉绑定覆盖检查纳入architectureTest。已运行完整离线build成功，仍不是实际世界/碰撞/视觉验收。

完整离线build退出0：63单测/913模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-smoke-modes-shapes-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分模式选择/供能费用与边界、召唤AI/建造职责；继续旧批次运行类与客户端视觉模块收尾及102术账本核对。

## 旧批次运行职责及空门面清理、召唤Template Method与模式归属（2026-11-13）

柱体生长队列、火柱锁定灼烧队列、接触节拍索引拆为三个独立运行适配，原PhaseTwoRuntime仅剩明确生命周期协调后改为PlayerSpellRuntime；原附件key与取消/停服入口保留。免疫tag归属MagicControl、Ray归属射线适配、通道类型归属通道模块，调用者直连既有窄适配器后删除PhaseTwoSpells/IntermediateSpells空门面。首次编译补齐建造适配的entity/damage静态依赖后全测通过。召唤用Template Method固定读取指令→指定目标→精灵自动目标→清理无效目标→行为顺序；独立地面导航/精灵飞行/光精灵行为，保留原导航和物理移动，怪物与死灵共享地面机制但绑定各自视觉。模式选择持久化与指令迁至SpellModeSelectionAdapter，核爆冷却归属NuclearExplosionAdapter，原key/copyOnDeath保持。仍有批次实体/渲染/规则/部分适配器及定义/供能/召唤生成等职责未清，整体IN_PROGRESS。

完整离线build退出0：63单测/913模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-runtime-facades-summons-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续形状/模式费用与边界策略、召唤生成及供能职责；移除残留批次规则分支，客户端视觉与完整102术账本最终审计。

## 批次规则与释放分支移除、模式领域规格收尾（2026-11-13）

IntermediateRules与PhaseTwoRules已删除，块爆体积和抛射求解迁入纯核，原断言改为调用真实职责所有者。PhaseTwoReleaseAdapter已移除，其资格与已付款释放直接进入102术显式绑定；实体创建独立EffectCarrierSpawner，冰柱/冰枪保留原trace和放置顺序。持续通道仅保留原有有效角度检查。大气/重力/核爆通过各自世界策略负责选点、就绪及释放通知；供能、跟随、尺寸和核爆球形上下界提为纯规格，生命周期不再判断术ID。新增6项模块合同，当前63/919；全部旧数值、资源、保存键及原版世界副作用未修改。整体重构仍IN_PROGRESS，尚有召唤、障壁、定义及客户端视觉等职责收尾。

完整离线build退出0：63单测/919模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-release-rules-maintained-policy-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分召唤生成/供能/尸体存储与精灵保护，清理IntermediateReleaseAdapter残留分派；随后客户端视觉、定义规格及102术最终账本审计。

## 批次规则与释放分支移除、模式领域规格收尾（2026-11-13）

IntermediateRules与PhaseTwoRules已删除，块爆体积和抛射求解迁入纯核，原断言改为调用真实职责所有者。PhaseTwoReleaseAdapter已移除，其资格与已付款释放直接进入102术显式绑定；实体创建独立EffectCarrierSpawner，冰柱/冰枪保留原trace和放置顺序。持续通道仅保留原有有效角度检查。大气/重力/核爆通过各自世界策略负责选点、就绪及释放通知；供能、跟随、尺寸和核爆球形上下界提为纯规格，生命周期不再判断术ID。新增6项模块合同，当前63/919；全部旧数值、资源、保存键及原版世界副作用未修改。整体重构仍IN_PROGRESS，尚有召唤、障壁、定义及客户端视觉等职责收尾。

完整离线build退出0：63单测/919模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-release-rules-maintained-policy-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分召唤生成/供能/尸体存储与精灵保护，清理IntermediateReleaseAdapter残留分派；随后客户端视觉、定义规格及102术最终账本审计。

## 召唤职责、纯材质动画策略与客户端输入策略（2026-11-13）

召唤尸体记录及选点、实体生成、三类供能策略与精灵保护独立，持久键和实际调用顺序保持。IntermediateReleaseAdapter及零调用IntermediateAreaAdapter删除，原有效逐术入口直连窄适配，生成仅由SweptProjectileSpawner负责。ExtendedSpellMotion已删除；原34术变形按七类材质Strategy抽入纯核MeshMotion及各MeshDeformation，35个ID含unknown×26角色×3阶段×10时间点，共27300组变形/透明度冻结差分精确通过。MagicClient只保留初始化与兼容委托，键位注册、物理输入/UI边界独立；CastingInputState纯状态负责初始松键、打断、两tick连发、模式锁，3项实际接线合同通过。完整离线build通过，整体仍IN_PROGRESS；未声称世界/视觉实际验收。

完整离线build退出0：63单测/923模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-summon-motion-input-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续实体类型身份映射和渲染尺寸/姿态纯规格；拆ReferenceSpellModels资源加载与纯采样，随后障壁、定义规格及逐术账本最终核对。

## 参考模型纯采样、渲染规格与注册身份归属（2026-11-13）

ReferenceSpellModels的透明度和变形已抽到纯核ReferenceMeshMotion，特殊术显式不可变采样策略保留原首个匹配，未擅改原flame_tornado不可达第二分支；20,010组变形/透明度冻结差分通过。渲染尺寸与载体姿态抽为ProjectileMeshSpec，薄片/箭/岩炮/未知默认与竖直落石/静止盾姿态合同通过。基础和扫掠实体的类型→法术映射统一归属MagicEntityTypes，未知类型默认保持，实际注册顺序/ID/尺寸不变。当前63单测/926模块测试，整个重构仍IN_PROGRESS。

完整离线build退出0：63单测/926模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r09-reference-render-identity-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：抽取法术定义隐式分类为纯规格，清理长位置参数及目录职责；随后继续有限障壁和供能障壁逐术策略、残留批次命名与102术账本。

## 纯法术定义、Builder与障壁/建造/静止载体职责（2026-11-13）

SpellDefinition、CastingProfile、MagicCategory的实际类型迁入magic-core，公共全名与record组件不变；SpellTraits独立纯分类规则，102术快照通过。90处长位置构造改为经典Builder具名构建，兼容构造器保留，rank及recovery实际值保持。有限障壁的三种目标/伤害/弹体/生物拦截和亡灵击壳分离为独立Strategy；城塞与魔神封印的生成/付核心能量/维持拆为策略。PhaseTwoConstructionAdapter删除，土枪建造/土柱永久石块/冰城预检/原版物理冲量分别归属专属适配。PhaseTwoStationaryAdapter删除，水瀑/冰枪/火柱静止世界行为显式装配；未知默认接触保持。迁移时两个缺失导入和三个receiver错误由编译发现并修正，最终完整build通过；没有游戏实测或Git操作，整体仍IN_PROGRESS。

完整离线build退出0：63单测/926模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-definition-barrier-stationary-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：拆分扫掠弹体的逐术过期/体积/碰块策略与定向载体飞行专属策略，再移除已清空批次命名；继续视觉目录/定义目录和102术逐项完成证据。

## 逐术飞行策略、纯目录完整合同与机制类型清理（2026-11-13）

SweptProjectilePolicy明确绑定六种扫掠术的寿命/体积/接触/卸载/地形/爆发/碰块能力，CarrierFlightPolicy维护严格接触/截面/爆炸/风切特例；共享扫掠和飞行适配不再持有对应术ID长条件。ExpansionFields/ExpansionBarriers无职责转发已删除。新增纯JDK102术逐字段冻结目录合同及Builder不可变/独立/非法输入合同、飞行边界测试，63/930通过。既有职责拆分后将17个PhaseTwo/Intermediate类型改为SweptSpellProjectile/DirectionalSpellCarrier等真实机制名，完整清单r10-mechanism-class-names.json；原phase_two_spell实体ID、PHASE_TWO兼容枚举、所有附件/存档/网络ID保留。完整离线build通过；仍未完成整体重构，不启动游戏、不Git。

完整离线build退出0：63单测/930模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-flight-mechanism-catalog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：继续Reference模型资产加载/渲染分离与视觉目录，拆地形租约纯计划/预算和服务端MagicSystem输入网络职责；最后逐术账本、模式审查及R00—R10完成门禁审计。

## 地形纯调度、视觉网格策略、元素目录与领域生命周期（2026-11-13）

MagicSystem服务端网络/动作/会话/生命周期及参考模型资源加载已分责。地形租约采用JDK-only TerrainLeaseScheduler与实例TickQuota，4项合同覆盖恢复优先、失败耗额、过期/延期、不足配额和12次成功组装上限；持久ID/Codec及玩家替换保护保留。40术分层网格生成迁入纯核11类材质Strategy，820组旧新网格逐顶点/颜色精确差分，2项模块合同；39项领域视觉布局纯规格，客户端不再依赖服务端世界适配器。102定义拆10个元素目录，公共常量/ALL顺序和逐字段基线保留。领域实体38项tick/范围/save/load显式绑定，Illusion/CoreSpell/ExpansionSpell及早晚加载顺序保持。20个已无职责领域及单术转发门面删除，真实调用直连；地形注册与权限/临时放置/爆破掉落拆分，expiry通过窄方法封装。全部完整离线build通过；整体最终审计尚未完成，实际游戏NOT_RUN。

完整离线build退出0：63单测/936模块测试，15,658旧行为断言、102定义和39/102教材、旧玩家存档fixture、架构/资源/JAR检查通过。证据r10-terrain-visual-catalog-build.log及JUnit汇总/XML。实际游戏/Fabric/世界/事件/Codec重载/视觉/联机/性能NOT_RUN；用户授权持续执行全部重构，无需反复确认继续，全部完成后统一实机测试；不启动游戏、不Git。

下一精确动作：核对领域绑定兼容语义和视觉布局合同，处理SpellHooks可写静态扩展点及残余职责；完成102术追踪、模式审查、真实文件树与最终门禁。
