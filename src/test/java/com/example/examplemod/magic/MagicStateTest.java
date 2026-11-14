package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.OceanFreezeRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.HolyInstantAdapter;
import com.example.examplemod.infrastructure.fabric.magic.EarthHedgehogAdapter;
import com.example.examplemod.infrastructure.fabric.magic.EarthFortressAdapter;
import com.example.examplemod.infrastructure.fabric.magic.DivineJudgmentAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;

import java.util.List;
import com.mojang.serialization.JsonOps;

/** 无需启动游戏的行为测试，由 verifyMagic 运行并纳入 check/build。 */
public final class MagicStateTest {
	private static int assertions;
	private static void check(boolean condition, String message) {
		assertions++;
		if (!condition) throw new AssertionError(message);
	}

	public static void main(String[] args) {
		var center=net.minecraft.world.phys.Vec3.ZERO;
		check(Math.abs(BarrierGeometry.sphere(center,3,false,new net.minecraft.world.phys.Vec3(-10,0,0),new net.minecraft.world.phys.Vec3(20,0,0))-.35)<1E-9,"Fast outside-to-outside motion hits shell");
		check(!Double.isFinite(BarrierGeometry.sphere(center,3,false,new net.minecraft.world.phys.Vec3(-10,4,0),new net.minecraft.world.phys.Vec3(20,0,0))),"Movement missing sphere is unblocked");
		check(Math.abs(BarrierGeometry.sphere(center,3,false,center,new net.minecraft.world.phys.Vec3(6,0,0))-.5)<1E-9,"Exit hits shell");
		check(!Double.isFinite(BarrierGeometry.sphere(center,3,true,new net.minecraft.world.phys.Vec3(-10,-1,0),new net.minecraft.world.phys.Vec3(20,0,0))),"Hemisphere has no buried sphere wall");
		check(Math.abs(BarrierGeometry.sphere(center,3,true,new net.minecraft.world.phys.Vec3(0,-2,0),new net.minecraft.world.phys.Vec3(0,4,0))-.5)<1E-9,"Hemisphere base catches vertical entry");
		check(MagicControl.duration(60,true,100,0)==40,"Player control cap");
		check(MagicControl.duration(60,false,100,80)==30,"Repeated control halves");
		check(MagicControl.duration(60,false,140,80)==60,"Resistance expires at three seconds");
		check(MagicControl.duration(6,false,100,80)==5,"Control minimum five ticks");
		check(MagicFieldCadence.ready(100,90,10),"Field cadence permits exact boundary");
		check(!MagicFieldCadence.ready(99,90,10),"Overlapping fields cannot double pulse");
		check(!MagicFieldCadence.ready(100,90,0),"Invalid field cadence rejected");
		check(SonicBlast.contains(new net.minecraft.world.phys.Vec3(0,0,16),new net.minecraft.world.phys.Vec3(0,0,1)),"Sonic cone reaches sixteen blocks");
		check(!SonicBlast.contains(new net.minecraft.world.phys.Vec3(0,0,-1),new net.minecraft.world.phys.Vec3(0,0,1)),"Sonic cone excludes rear");
		check(!SonicBlast.contains(new net.minecraft.world.phys.Vec3(12,0,12),new net.minecraft.world.phys.Vec3(0,0,1)),"Sonic cone excludes distant side");
		for(int i=0;i<12;i++){check(EarthHedgehogAdapter.offset(i).horizontalDistance()<=6,"Hedgehog stays in radius");check(!EarthHedgehogAdapter.active(i*2-1,i)&&EarthHedgehogAdapter.active(i*2,i),"Hedgehog stagger boundary");}
		check(EarthHedgehogAdapter.mayHit(0)&&EarthHedgehogAdapter.mayHit(1)&&!EarthHedgehogAdapter.mayHit(2),"Hedgehog two-cluster cap");
		check(Quagmire.contains(new net.minecraft.world.phys.Vec3(6,0,0))&&!Quagmire.contains(new net.minecraft.world.phys.Vec3(6.01,0,0)),"Mud radius boundary");
		check(!Quagmire.contains(new net.minecraft.world.phys.Vec3(0,2,0)),"Mud leaves airborne targets alone");
		check(Quagmire.jumpVelocity(-.5)==-.5&&Quagmire.jumpVelocity(.42)==.115,"Mud caps ascent without pulling down");
		double mudVelocity=Quagmire.jumpVelocity(.42),mudHeight=0;while(mudVelocity>0){mudHeight+=mudVelocity;mudVelocity=(mudVelocity-.08)*.98;}check(mudHeight<=.15,"Mud jump remains below fifteen centimeters under vanilla gravity");
		check(com.example.examplemod.magic.spell.mixed.bora_blast.BoraBlastRules.contains(0,0,20,0,0,1),"Cold gust range boundary");
		check(!com.example.examplemod.magic.spell.mixed.bora_blast.BoraBlastRules.contains(0,0,-1,0,0,1),"Thermal gust excludes rear");
		check(MagicBooks.lessons(MagicCategory.MIXED,2).contains("blast"),"Blast belongs to mixed textbook");
		float magmaTotal=0;for(int tick=0;tick<=180;tick++)magmaTotal+=MagmaGush.damageAt(tick);check(magmaTotal==26,"Magma total is ten plus eight times two");
		check(MagmaGush.damageAt(160)==2&&MagmaGush.damageAt(161)==0,"Magma final pulse boundary");
		check(WindFieldRuntimeAdapter.contains(new net.minecraft.world.phys.Vec3(5,20,0),5,20),"Tornado cylinder boundary");
		check(!WindFieldRuntimeAdapter.contains(new net.minecraft.world.phys.Vec3(5.1,10,0),5,20),"Tornado excludes outside radius");
		check(WindFieldRuntimeAdapter.impulse(new net.minecraft.world.phys.Vec3(1,0,0),new net.minecraft.world.phys.Vec3(0,.34,0),true,false,true).y==.35,"Tornado player ascent cap");
		float flashResidual=0;for(int age=-1;age<=180;age++)flashResidual+=FlashOver.residual(age);check(flashResidual==24,"Flash has eight residual attempts");
		check(FlashOver.contains(new net.minecraft.world.phys.Vec3(16,6,0))&&!FlashOver.contains(new net.minecraft.world.phys.Vec3(16.01,3,0)),"Flash cylinder boundary");
		check(MagicBooks.lessons(MagicCategory.MIXED,3).contains("frost_nova"),"Frost Nova mixed lesson");
		float infernoTotal=18;for(int age=0;age<=420;age++)infernoTotal+=Inferno.residual(age);check(infernoTotal==118,"Inferno initial plus twenty pulses");
		check(Inferno.residual(0)==0&&Inferno.residual(400)==5&&Inferno.residual(401)==0,"Inferno final pulse boundary");
		check(AbsoluteZero.contains(new net.minecraft.world.phys.Vec3(16,0,0))&&!AbsoluteZero.contains(new net.minecraft.world.phys.Vec3(16,1,0)),"Absolute zero spherical boundary");
		var dome=EarthFortressAdapter.shell(net.minecraft.core.BlockPos.ZERO,new net.minecraft.world.phys.Vec3(0,0,1));
		check(dome.size()<=200&&!dome.isEmpty(),"Fortress respects block cap");
		check(!dome.contains(new net.minecraft.core.BlockPos(0,0,3))&&!dome.contains(new net.minecraft.core.BlockPos(0,1,3)),"Fortress has two-high doorway");
		check(!dome.contains(net.minecraft.core.BlockPos.ZERO),"Fortress interior stays hollow");
		check(MagicRules.strikeReady(40,40,1200),"Storm strike starts at scheduled tick");
		check(!MagicRules.strikeReady(39,40,1200),"Storm cannot strike early");
		check(!MagicRules.strikeReady(1200,1200,1200),"Storm cannot strike at expiry");
		int blizzardPulses = 0;
		for (int age = -1; age <= 80; age++) if (MagicRules.fieldPulse(age, 80, 10)) blizzardPulses++;
		check(blizzardPulses == 8, "Blizzard has exactly eight pulses including release and excluding expiry");
		check(!MagicRules.fieldPulse(0,80,0), "Invalid interval cannot pulse");
		for (int rank = 5; rank <= 7; rank++) for (int seed = 0; seed < 3; seed++) {
			int count = ChantScore.noteCount(rank, seed);
			check(count >= (rank == 5 ? 8 : 10) && count <= (rank == 5 ? 10 : 12), "High rank section respects bounded packet");
		}
		// Contact aura, piercing-water policy and bounded wind propulsion regressions.
		var body = new net.minecraft.world.phys.AABB(-.3, 0, -.3, .3, 1.8, .3);
		check(MagicRules.touchesBody(body,new net.minecraft.world.phys.AABB(.35,.8,-.2,.8,1.5,.2)), "Body flame contacts the torso");
		check(MagicRules.touchesBody(body,new net.minecraft.world.phys.AABB(-.2,1.7,-.2,.2,2,.2)), "Body flame covers the head");
		check(!MagicRules.touchesBody(body,new net.minecraft.world.phys.AABB(1.8,0,-.2,2.2,1,.2)), "Old distant fire ring no longer causes contact damage");
		for(var id:List.of("sonic_boom","stone_cannon","icicle_edge","flood_flush")) {
			check(MagicRules.passesWater(SpellDefinition.find(id),true), "Piercing spell crosses water: "+id);
			check(!MagicRules.passesWater(SpellDefinition.find(id),false), "Dry wall remains solid: "+id);
		}
		for(var id:List.of("fireball","water_ball","ice_blade","rock_bullet")) check(!MagicRules.passesWater(SpellDefinition.find(id),true), "Nonpiercing fluid stop preserved: "+id);
		for(var id:List.of("flame_slice","water_cannon","ex_flame","sonic_boom","stone_cannon","icicle_break","icicle_edge","flood_flush","wind_slice","explosion")) {
			var constant=SpellDefinition.find(id);
			check(constant.piercing(),"Every constant-speed spell pierces: "+id);
			check(MagicRules.passesWater(constant,false,true),"Dry waterloggable block is passable: "+id);
			check(MagicRules.passesWater(constant,true,true),"Wet waterloggable block is passable: "+id);
			check(!MagicRules.passesWater(constant,false,false),"Dry full solid block stops flight: "+id);
		}
		var recoil=com.example.examplemod.magic.spell.wind.wind_blast.WindBlastRules.velocity(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(new net.minecraft.world.phys.Vec3(0,0,-1)));
		check(recoil.z<0 && recoil.y>0,"Slime-like recoil lifts off and pushes opposite aim");
		double travel=0,velocity=-recoil.z;for(int tick=0;tick<240;tick++){travel+=velocity;velocity*=.91;}
		check(Math.abs(travel-10)<1.0E-7,"Nominal airborne horizontal recoil is ten blocks");
		check(Math.abs(com.example.examplemod.magic.mechanic.projectile.LaunchBallistics.launchPeak(com.example.examplemod.magic.spell.wind.wind_blast.WindBlastRules.velocity(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(new net.minecraft.world.phys.Vec3(0,1,0))).y)-10)<1.0E-7,"Upward recoil peak uses ten blocks");
		check(com.example.examplemod.magic.spell.wind.wind_blast.WindBlastRules.SLOW_FALL_TICKS==100,"Slow falling lasts five seconds");

		var spell = SpellDefinition.WATER_BALL;
		var state = MagicState.initial(100);
		check(!state.canCast(spell), "Unknown spell cannot cast");
		state = state.learn(spell.id());
		check(state.canCast(spell), "First learning loads and selects spell");
		check(state.learn(spell.id()).equals(state), "Learning is idempotent");
		check(MagicState.initial(100).learn("water_shield").learned().isEmpty(), "Normal learning still requires prerequisites");
		check(MagicState.initial(100).learnWithoutRequirements("water_shield").learned().contains("water_shield"), "Legacy learning bypasses prerequisites");
		var paid = state.cast(spell);
		check(paid.mana() == 99 && paid.cooldown() == 0, "Beginner releases have no recovery after preparation");
		check(spell.castingProfile().preparationTicks() == 100, "Beginner requires five seconds preparation");
		var zero = new MagicState(0, 100, 0, 0, state.learned(), state.loaded(), state.selected());
		check(!zero.canCast(spell), "Insufficient mana denied");
		check(!state.select("").selected().isEmpty(), "Blank selection keeps previous");
		var removed = state.toggle(spell.id());
		check(removed.selected().isEmpty() && removed.loaded().isEmpty() && removed.learned().size() == 1, "Unload does not forget");
		check(removed.toggle(spell.id()).canCast(spell), "Known spell can be loaded again");
		check(paid.withMaximum(200).mana() == 99, "Increasing maximum does not refill");
		check(paid.withMaximum(50).mana() == 50, "Decreasing maximum clamps mana");
		check(paid.withMaximum(200).maximum() == 200, "Maximum editable");
		var countdown = new MagicState(3, 100, 23998, 2, state.learned(), state.loaded(), state.selected());
		check(countdown.tick().mana() == 3, "No early daily recovery");
		var recovered = countdown.tick().tick();
		check(recovered.mana() == 100 && recovered.onlineTicks() == 0 && recovered.cooldown() == 0, "Daily recovery at exactly 24000 ticks");
		check(paid.refill().learned().equals(paid.learned()) && paid.refill().loaded().equals(paid.loaded())
				&& paid.refill().selected().equals(paid.selected()), "Recovery preserves knowledge and selection");
		var dirty = new MagicState(-5, -1, -10, -2, List.of("unknown", spell.id(), spell.id()), List.of("unknown", spell.id(), spell.id()), "unknown");
		check(dirty.mana() == 0 && dirty.maximum() == 1 && dirty.onlineTicks() == 0 && dirty.cooldown() == 0, "Invalid numeric data sanitized");
		check(dirty.learned().size() == 1 && dirty.loaded().size() == 1 && dirty.selected().equals(spell.id()), "Invalid and duplicate spells sanitized");
		var json = MagicState.CODEC.encodeStart(JsonOps.INSTANCE, paid).getOrThrow();
		check(MagicState.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow().equals(paid), "Persistent codec round trip");
		var day = zero;
		for (int i = 0; i < 23999; i++) day = day.tick();
		check(day.mana() == 0, "No premature full day recovery");
		check(day.tick().mana() == 100, "Full online day refills");
		var continuous = state;
		int successes = 0;
		for (int tick = 0; tick < 200; tick++) {
			if (continuous.canCast(spell)) { continuous = continuous.cast(spell); successes++; }
			continuous = continuous.tick();
		}
		check(successes == 100 && continuous.mana() == 0, "100 casts consume 100 mana without overspending");
		check(zero.restoreMana(30).mana() == 30, "Cheat restores requested amount");
		check(paid.restoreMana(30).mana() == 100, "Cheat recovery clamps to maximum");
		check(zero.restoreMana(Integer.MAX_VALUE).mana() == 100, "Full restore cannot overflow");
		check(paid.restoreMana(1).cooldown() == paid.cooldown() && paid.restoreMana(1).onlineTicks() == paid.onlineTicks(), "Cheat preserves timers");
		check(paid.restoreMana(-1).equals(paid), "Negative recovery never removes mana");
		check(paid.restoreMana(1).learned().equals(paid.learned()) && paid.restoreMana(1).selected().equals(paid.selected()), "Cheat preserves spell state");

		var four = MagicState.initial(100);
		for (var definition : SpellDefinition.ALL) four = four.learn(definition.id());
		check(four.learned().size() == 102 && four.loaded().size() == 10, "All lessons known, wheel limited to ten");
		check(four.selected().equals(spell.id()), "Later lessons preserve selection");
		for (var definition : SpellDefinition.ALL) {
			var ready = new MagicState(10000, 10000, 0, 0, four.learned(), List.of(definition.id()), definition.id());
			var spent = ready.cast(definition);
			check(spent.mana() == 10000 - definition.manaCost(), "Exact cost: " + definition.id());
			for (int i = 1; i < definition.cooldownTicks(); i++) {
				spent = spent.tick();
				check(!spent.canCast(definition), "No early cooldown: " + definition.id());
			}
			check(spent.tick().canCast(definition), "Cooldown ends exactly: " + definition.id());
			var poor = new MagicState(definition.manaCost() - 1, 10000, 0, 0, four.learned(), List.of(definition.id()), definition.id());
			check(!poor.canCast(definition), "Reject insufficient mana: " + definition.id());
			check(ready.learn(definition.id()).equals(ready), "No duplicate lesson: " + definition.id());
			check(definition.prerequisitesMet(id -> false), "Basic spell has no external dependency");
		}
		check(Double.isInfinite(SpellDefinition.ICE_BLADE.flightRange()), "Ballistic Ice Blade has no distance cap");
		check(SpellDefinition.ICE_BLADE.speed() == 2.5F && SpellDefinition.ICE_BLADE.gravity() == 0.02, "Ice Blade uses initial speed and ice gravity");
		for(int rank=1;rank<=7;rank++) check(MagicRules.flightTicks(rank)==rank*60, "Three seconds per rank");
		for(var definition:SpellDefinition.ALL) {
			if(definition.motion()==SpellDefinition.Motion.CONSTANT) {
				check(definition.gravity()==0, "Constant flight has no gravity: "+definition.id());
				check(definition.flightRange()==definition.speed()*definition.flightTicks(), "Time determines flight range: "+definition.id());
			}
			if(definition.motion()==SpellDefinition.Motion.BALLISTIC) check(definition.flightTicks()==0&&Double.isInfinite(definition.flightRange()), "Ballistic flight has no expiry: "+definition.id());
		}
		check(SpellDefinition.FLAME_SLICE.flightTicks()==60&&SpellDefinition.FLAME_SLICE.speed()==2, "Beginner constant flight lasts three seconds");
		check(SpellDefinition.STONE_CANNON.flightTicks()==120&&SpellDefinition.STONE_CANNON.flightRange()==360, "Intermediate stone cannon flies at three blocks per tick");
		for(var id:List.of("ice_pillar","ice_lance","waterfall","wind_bind","earth_lance")) check(SpellDefinition.find(id).motion()==SpellDefinition.Motion.INSTANT, "Instant execution: "+id);
		check(!SpellDefinition.SONIC_BOOM.areaDamage()&&SpellDefinition.SONIC_BOOM.piercing(), "Piercing is distinct from AOE");
		check(SpellDefinition.WIND_SLICE.areaDamage()&&SpellDefinition.WIND_SLICE.piercing(), "Impact AOE is distinct from piercing");
		check(SpellDefinition.MAGIC_SHIELD.glowColor()==0x43BFFF&&SpellDefinition.PHYSICAL_SHIELD.glowColor()==0xBA70FF, "Shield glow colors");
		check(SpellDefinition.EARTH_PILLAR.motion()==SpellDefinition.Motion.FALLING, "Earth rock falls until terrain contact");
		check(MagicRules.shieldAfter(MagicRules.shieldAfter(16,7),9)==0, "Repeated physical hits break shield at capacity");
		var ringCenter = net.minecraft.world.phys.Vec3.ZERO;
		check(MagicRules.touchesRing(new net.minecraft.world.phys.AABB(1.6,0,-0.3,2.2,1.8,0.3),ringCenter,2), "Body overlapping fire ring is hit");
		check(MagicRules.touchesRing(new net.minecraft.world.phys.AABB(-3,0,-3,3,4,3),ringCenter,2), "Large body surrounding ring is hit");
		check(!MagicRules.touchesRing(new net.minecraft.world.phys.AABB(-0.3,0,-0.3,0.3,1.8,0.3),ringCenter,2), "Center without ring contact is safe");
		check(!MagicRules.touchesRing(new net.minecraft.world.phys.AABB(2.4,0,-0.3,3,1.8,0.3),ringCenter,2), "Outside ring is safe");
		check(!MagicRules.touchesRing(new net.minecraft.world.phys.AABB(1.8,1,-0.2,2.2,2,0.2),ringCenter,2), "Above fire ring is safe");
		check(MagicRules.statusTicks(1) == 100 && MagicRules.statusTicks(7) == 700, "Rank-based status duration");
		check(MagicRules.extend(20,80) == 100, "Maintenance adds remaining time");
		check(MagicRules.extend(Integer.MAX_VALUE-1,80) == Integer.MAX_VALUE, "Maintenance cannot overflow");
		check(MagicRules.shieldAfter(8,4) == 4 && MagicRules.shieldAfter(4,20) == 0, "Shield capacity clamps on oversized hit");
		check(SpellDefinition.WATER_SHIELD.cooldownTicks() == 0 && SpellDefinition.MAGIC_SHIELD.cooldownTicks() == 0, "Beginner utility uses shared recovery");
		check(MagicState.initial(100).learn("water_shield").learned().isEmpty(), "Water Shield requires Water Ball");
		var legacyLearning = MagicState.initial(100).learnWithoutRequirements("physical_shield");
		check(legacyLearning.learned().contains("physical_shield") && legacyLearning.loaded().contains("physical_shield"), "Legacy book directly teaches spells with prerequisites");
		check(MagicState.initial(100).learn("ice_blade").learned().isEmpty(), "Ice Blade requires Ice Smash");
		check(MagicState.initial(100).learn("heat_island").learned().isEmpty(), "Heat Island requires Heat Hand");
		check(SpellDefinition.ICE_BLADE.icy() && !SpellDefinition.WATER_ARROW.icy(), "Ice status separate from water status");
		var wallGeometry = new SpellSweep(new net.minecraft.world.phys.Vec3(0,0,1),3,2,.3);
		var arrowBox = new net.minecraft.world.phys.AABB(-0.1,0.9,2,0.1,1.1,2.2);
		check(Double.isFinite(wallGeometry.contact(arrowBox,new net.minecraft.world.phys.Vec3(0,1,0),new net.minecraft.world.phys.Vec3(0,0,1),3)), "Water wall sweeps fast projectile");
		check(!wallGeometry.intersects(new net.minecraft.world.phys.AABB(1.6,0.9,-0.1,1.8,1.1,0.1),new net.minecraft.world.phys.Vec3(0,1,0)), "Outside water wall width does not block");
		check(wallGeometry.intersects(new net.minecraft.world.phys.AABB(-0.1,1.8,-0.1,0.1,1.9,0.1),new net.minecraft.world.phys.Vec3(0,1,0)), "Upper half of two-block water wall contacts");
		for (int yaw=-180; yaw<180; yaw+=45) {
			var normal = net.minecraft.world.phys.Vec3.directionFromRotation(0,yaw);
			var rotated = new SpellSweep(normal,3,2,.3);
			check(Math.abs(rotated.side.dot(normal)) < 1.0E-6 && rotated.up.y > 0.99, "Water wall orthonormal at yaw " + yaw);
			var hitCenter = normal.scale(2).add(0,1,0);
			var hitBox = new net.minecraft.world.phys.AABB(hitCenter.x-0.1,0.9,hitCenter.z-0.1,hitCenter.x+0.1,1.1,hitCenter.z+0.1);
			check(Double.isFinite(rotated.contact(hitBox,new net.minecraft.world.phys.Vec3(0,1,0),normal,3)), "Water wall rotated sweep " + yaw);
		}
		check(SpellDefinition.WATER_CANNON.rank()==2 && SpellDefinition.EX_FLAME.statusTicks()==200,"Intermediate rank and status duration");
		check(com.example.examplemod.magic.mechanic.channel.ChargeCurve.display(10)==10 && com.example.examplemod.magic.mechanic.channel.ChargeCurve.display(19)==19 && com.example.examplemod.magic.mechanic.channel.ChargeCurve.display(20)==20,"Charge UI displays actual paid mana");
		check(SpellDefinition.WATER_SPLASH.statusTicks()==2000,"Water Splash wetness lasts 100 seconds");
		check(SpellDefinition.ICICLE_FIELD.damage()==5,"Icicle Field deals five damage");

		check(com.example.examplemod.magic.mechanic.channel.ChargeCurve.extraAfterTicks(0)==0 && com.example.examplemod.magic.mechanic.channel.ChargeCurve.extraAfterTicks(20)==15 && com.example.examplemod.magic.mechanic.channel.ChargeCurve.extraAfterTicks(40)==40 && com.example.examplemod.magic.mechanic.channel.ChargeCurve.extraAfterTicks(60)==75,"Linear rate acceleration integrates to increasing per-second costs");
		long injected=0;
		for(int t=1;t<=100;t++) {
			long debit=com.example.examplemod.magic.mechanic.channel.ChargeCurve.extraAfterTicks(t)-com.example.examplemod.magic.mechanic.channel.ChargeCurve.extraAfterTicks(t-1);
			check(debit>=0,"Injection never refunds mana during charging");
			injected+=debit;
		}
		check(injected==175,"Fractional tick amounts carry forward without lost mana");
		check(com.example.examplemod.magic.mechanic.channel.ChargeCurve.extraAfterTicks(Integer.MAX_VALUE)>Integer.MAX_VALUE,"Large charge duration uses safe long arithmetic");
		check(com.example.examplemod.magic.mechanic.channel.ChargeCurve.stoneDamage(10)==10 && com.example.examplemod.magic.mechanic.channel.ChargeCurve.stoneDamage(19)==10 && com.example.examplemod.magic.mechanic.channel.ChargeCurve.stoneDamage(20)==11,"Stone damage counts full extra tens");
		check(com.example.examplemod.magic.mechanic.channel.ChargeCurve.stoneDamage(1000000)==100009,"No arbitrary stone damage cap");
		var debited=four.spendMana(13);
		check(debited.mana()==87&&debited.cooldown()==0,"Charge prepayment does not start cooldown");
		check(debited.restoreMana(13).equals(four),"Charge refund preserves live state");
		try { four.spendMana(101); throw new AssertionError("Charge overspend"); } catch(IllegalArgumentException expected) { assertions++; }
		var anchor=new net.minecraft.core.BlockPos(5,5,5); var hitPoint=new net.minecraft.world.phys.Vec3(5,5,5);
		check(com.example.examplemod.infrastructure.fabric.geometry.BlockBurstGeometryAdapter.blastBlocks(anchor,1,hitPoint).equals(List.of(anchor)),"Water blast exactly one block");
		check(com.example.examplemod.infrastructure.fabric.geometry.BlockBurstGeometryAdapter.blastBlocks(anchor,2,hitPoint).size()==8&&com.example.examplemod.infrastructure.fabric.geometry.BlockBurstGeometryAdapter.blastBlocks(anchor,2,hitPoint).stream().distinct().count()==8,"Fire blast exactly eight distinct positions");
		check(com.example.examplemod.infrastructure.fabric.geometry.BlockBurstGeometryAdapter.impactBounds(hitPoint).getXsize()==3&&com.example.examplemod.infrastructure.fabric.geometry.BlockBurstGeometryAdapter.impactBounds(hitPoint).getYsize()==3,"Blast damage volume side length three");
		var wide=new SpellSweep(new net.minecraft.world.phys.Vec3(0,0,1),3,1,0.25);
		check(Double.isFinite(wide.contact(new net.minecraft.world.phys.AABB(1.3,0,3,1.7,2,3.5),new net.minecraft.world.phys.Vec3(0,1,0),new net.minecraft.world.phys.Vec3(0,0,1),5)),"Wide sonic sweep catches side target");
		check(!Double.isFinite(wide.contact(new net.minecraft.world.phys.AABB(2,0,3,2.5,2,3.5),new net.minecraft.world.phys.Vec3(0,1,0),new net.minecraft.world.phys.Vec3(0,0,1),5)),"Sonic sweep excludes outside width");
		var fastRock=new SpellSweep(new net.minecraft.world.phys.Vec3(0,0,1),0.25,0.25,0.25);
		check(Double.isFinite(fastRock.contact(new net.minecraft.world.phys.AABB(-0.1,0.9,0.8,0.1,1.1,1),new net.minecraft.world.phys.Vec3(0,1,0),new net.minecraft.world.phys.Vec3(0,0,1),2)),"Fast stone cannot skip a thin creature between endpoints");
		check(!Double.isFinite(fastRock.contact(new net.minecraft.world.phys.AABB(-0.1,0.9,2.2,0.1,1.1,2.3),new net.minecraft.world.phys.Vec3(0,1,0),new net.minecraft.world.phys.Vec3(0,0,1),2)),"Swept body cannot hit beyond current travel");
		var verticalSweep=new SpellSweep(new net.minecraft.world.phys.Vec3(0,1,0),3,1,0.25);
		check(Double.isFinite(verticalSweep.contact(new net.minecraft.world.phys.AABB(-0.1,3,-0.1,0.1,3.2,0.1),new net.minecraft.world.phys.Vec3(0,1,0),new net.minecraft.world.phys.Vec3(0,1,0),3)),"Vertical aim retains finite collision basis");
		check(!MagicState.initial(100).learn("icicle_field").learned().contains("icicle_field"),"Cannot forge ice field prerequisites");
		var iceStudent=MagicState.initial(100).learn("water_ball").learn("ice_smash");
		check(!iceStudent.learn("icicle_field").learned().contains("icicle_field"),"Ice field requires Heat Island too");
		check(iceStudent.learn("fireball").learn("heat_hand").learn("heat_island").learn("icicle_field").learned().contains("icicle_field"),"Both prerequisites unlock ice field");
		var firePaid = four.select("fireball").cast(SpellDefinition.FIREBALL).select("water_ball");
		check(firePaid.canCast(spell), "Beginner has no post-release cooldown; server preparation controls cadence");
		var fourJson = MagicState.CODEC.encodeStart(JsonOps.INSTANCE, firePaid).getOrThrow();
		check(MagicState.CODEC.parse(JsonOps.INSTANCE, fourJson).getOrThrow().equals(firePaid), "Four spells persist with cooldown");
		check(four.toggle("water_ball").selected().equals("fireball"), "Removing selection falls back to first remaining");
		var gated = new SpellDefinition("future", 1, 1, 2, 100, "water", 1.5F, 0.03, 0,
				SpellDefinition.CastMode.CHANT, List.of("disease_system"));
		check(!gated.prerequisitesMet(id -> false), "Missing future dependency denies availability");
		check(gated.prerequisitesMet(id -> id.equals("disease_system")), "Known dependency can be supplied later");

		var novice = MagicState.initial(100);
		check(novice.learn("water_arrow").equals(novice), "Cannot forge water-arrow learning without Water Ball");
		check(novice.learn("ice_smash").equals(novice), "Ice Smash requires Water Ball");
		check(novice.learn("flame_slice").equals(novice), "Flame Slice requires Fireball");
		check(novice.learn("healing").learned().contains("healing"), "Healing needs no offensive prerequisite");
		var waterStudent = novice.learn("water_ball");
		check(waterStudent.learn("water_arrow").learned().contains("water_arrow"), "Water Arrow unlocks after Water Ball");
		check(waterStudent.learn("ice_smash").learned().contains("ice_smash"), "Ice Smash unlocks after Water Ball");
		check(waterStudent.learn("flame_slice").equals(waterStudent), "Wrong element prerequisite cannot unlock Flame Slice");
		check(novice.learn("fireball").learn("flame_slice").learned().contains("flame_slice"), "Fireball unlocks Flame Slice");
		var heal = SpellDefinition.HEALING;
		check(heal.healingAmount(20, 20) == 0, "Full health prevents healing execution");
		check(heal.healingAmount(19, 20) == 1, "Healing clamps to missing health");
		check(heal.healingAmount(10, 20) == 4, "Healing restores four HP");
		check(heal.healingAmount(0, 20) == 0, "Healing cannot resurrect");
		check(heal.healingAmount(30, 40) == 4, "Healing respects modified maximum health");
		check(heal.healingAmount(Float.NaN, 20) == 0, "Invalid health is rejected");
		check(SpellDefinition.WATER_BALL.healingAmount(10, 20) == 0, "Attack spell cannot heal");
		var flight = new FlameSliceFlight(0).advance(2.25);
		var flightJson = FlameSliceFlight.CODEC.encodeStart(JsonOps.INSTANCE, flight).getOrThrow();
		var restored = FlameSliceFlight.CODEC.parse(JsonOps.INSTANCE, flightJson).getOrThrow();
		check(restored.equals(flight) && restored.remaining() == 117.75, "Reload preserves constant flight budget");
		check(!flight.advance(117.5).exhausted(), "Blade still flies before time-derived limit");
		check(flight.advance(117.75).exhausted(), "Blade expires at its time-derived range");
		check(new FlameSliceFlight(Double.NaN).exhausted(), "Invalid saved range fails closed");
		var origin = new net.minecraft.world.phys.Vec3(0, 1, 0);
		var south = new net.minecraft.world.phys.Vec3(0, 0, 1);
		var volume = new FlameSliceVolume(south, 0);
		var edge = new net.minecraft.world.phys.AABB(1.35, 0.2, 0.6, 1.65, 1.8, 0.9);
		check(Double.isFinite(volume.contact(edge, origin, 0.75)), "Volume edge hits a creature even when center ray misses");
		check(Math.abs(volume.contact(edge, origin, 0.75) - 0.1) < 1.0E-6, "One-block thickness participates in earliest collision");
		check(!Double.isFinite(volume.contact(new net.minecraft.world.phys.AABB(1.6, 0, 0.6, 1.8, 2, 0.9), origin, 0.75)), "Creature wholly outside width does not trigger");
		check(!Double.isFinite(volume.contact(new net.minecraft.world.phys.AABB(-0.1, 1.6, 0.6, 0.1, 1.8, 0.9), origin, 0.75)), "Creature above volume does not trigger");
		check(volume.intersects(new net.minecraft.world.phys.AABB(-0.1, 0, -0.1, 0.1, 2, 0.1), origin), "Creature initially inside volume is detected");
		var distant = new net.minecraft.world.phys.AABB(-0.2, 0, 5, 0.2, 2, 5.4);
		check(Double.isFinite(volume.contact(distant, origin, 6)), "Continuous sweep cannot skip creature between tick endpoints");
		var hit = origin.add(south.scale(volume.contact(edge, origin, 0.75) + 1.0E-5));
		check(volume.intersects(edge, hit), "Creature triggering collision remains in impact volume");
		check(volume.intersects(new net.minecraft.world.phys.AABB(-1.4, 0, 0.6, -1.1, 2, 0.9), hit), "Second creature in opposite side is included in same area hit");
		check(!volume.intersects(distant, hit), "Creature farther down flight path is not part of first hit");
		var full = new net.minecraft.world.phys.AABB(-1.5, 0.5, -0.5, 1.5, 1.5, 0.5);
		check(volume.fullyBlocked(origin, List.of(full)), "Exact three-by-one coverage fully blocks");
		check(!volume.fullyBlocked(origin, List.of()), "Air does not block");
		var singleBlock = new net.minecraft.world.phys.AABB(-0.5, 0.5, -0.5, 0.5, 1.5, 0.5);
		check(!volume.fullyBlocked(origin, List.of(singleBlock)), "One-block-wide obstacle cannot stop whole blade");
		var halfSlab = new net.minecraft.world.phys.AABB(-2, 0.5, -0.5, 2, 1, 0.5);
		check(!volume.fullyBlocked(origin, List.of(halfSlab)), "Half-height obstruction allows passage");
		var left = new net.minecraft.world.phys.AABB(-1.5, 0.5, -0.5, 0, 1.5, 0.5);
		var right = new net.minecraft.world.phys.AABB(0, 0.5, -0.5, 1.5, 1.5, 0.5);
		check(volume.fullyBlocked(origin, List.of(left, right)), "Union of neighboring blocks completely stops blade");
		var gapped = new net.minecraft.world.phys.AABB(0.01, 0.5, -0.5, 1.5, 1.5, 0.5);
		check(!volume.fullyBlocked(origin, List.of(left, gapped)), "Small real opening must not be treated as full coverage");
		check(volume.fullyBlocked(origin, List.of(full, full)), "Overlapping blockers do not create coverage artifacts");
		check(!volume.fullyBlocked(origin, List.of(new net.minecraft.world.phys.AABB(-2, 0, 1, 2, 2, 2))), "Block outside current cross-section does not block");
		var wall = new net.minecraft.world.phys.AABB(-2, 0, 0.7, 2, 2, 1.7);
		check(volume.blockingDistance(origin, 0.75, List.of(wall)) < 0.3, "Full wall stops within upcoming movement");
		check(!Double.isFinite(volume.blockingDistance(origin, 0.75, List.of(singleBlock))), "Partial block is passed across entire tick");
		var diagonalDirection = new net.minecraft.world.phys.Vec3(1, 0, 1).normalize();
		var diagonalVolume = new FlameSliceVolume(diagonalDirection, 45);
		check(!diagonalVolume.intersects(new net.minecraft.world.phys.AABB(1.2, 0, 1.2, 1.3, 2, 1.3), origin), "Rotated volume excludes conservative bounding-box corners");
		check(diagonalVolume.fullyBlocked(origin, List.of(new net.minecraft.world.phys.AABB(-3, 0, -3, 3, 2, 3))), "Diagonal volume can be completely blocked");
		var upwards = new FlameSliceVolume(new net.minecraft.world.phys.Vec3(0, 1, 0), 0);
		check(Double.isFinite(upwards.contact(new net.minecraft.world.phys.AABB(-0.2, 2, -0.2, 0.2, 3, 0.2), origin, 1)), "Looking upward uses vertical swept volume");
		check(upwards.fullyBlocked(new net.minecraft.world.phys.Vec3(0, 2, 0), List.of(new net.minecraft.world.phys.AABB(-2, 2, -1, 2, 3, 1))), "Ceiling can block full upward cross-section");
		check(new MagicAim(0, 0).direction().distanceTo(new net.minecraft.world.phys.Vec3(0, 0, 1)) < 1.0E-5, "Aim faces south at yaw zero");
		check(new MagicAim(90, 0).direction().distanceTo(new net.minecraft.world.phys.Vec3(-1, 0, 0)) < 1.0E-5, "Mouse right/left yaw controls flight");
		check(new MagicAim(0, -90).direction().y > 0.99999, "Looking up sends blade upwards");
		check(new MagicAim(0, 90).direction().y < -0.99999, "Looking down sends blade downwards");
		check(new MagicAim(45, -30).direction().x < 0 && new MagicAim(45, -30).direction().y > 0, "Diagonal upward aim preserves both axes");
		check(new MagicAim(1080, 0).direction().distanceTo(new MagicAim(0, 0).direction()) < 1.0E-5, "Repeated mouse turns normalize yaw");
		check(!new MagicAim(Float.NaN, 0).valid() && !new MagicAim(0, Float.POSITIVE_INFINITY).valid(), "Reject non-finite network angles");
		check(!new MagicAim(0, 91).valid() && !new MagicAim(0, -91).valid(), "Reject out-of-range network pitch before payment");
		check(SpellDefinition.ALL.size()==102 && MagicBooks.LESSONS.size()==39,"Introductory primer retains its thirty-nine lessons");
		check(MagicBooks.LESSONS.stream().distinct().count()==39,"No duplicated textbook lessons");
		check(MagicBooks.ZENITH_LESSONS.equals(SpellDefinition.ALL.stream().map(SpellDefinition::id).toList()),"Zenith book includes every registered spell exactly once in catalog order");
		var zenithStudent=MagicState.initial(100);
		for(var id:MagicBooks.ZENITH_LESSONS.reversed())zenithStudent=zenithStudent.learnWithoutRequirements(id);
		check(zenithStudent.learned().size()==SpellDefinition.ALL.size(),"Zenith buttons can learn all spells without prerequisites or chant lessons");
		check(zenithStudent.loaded().size()==10,"Learning every Zenith lesson still respects the ten-slot wheel limit");
		for(var definition:SpellDefinition.ALL) {
			check(MagicBooks.lessons(MagicCategory.of(definition),definition.rank()).contains(definition.id()),"Every registered spell has an authorized categorized textbook");
			if(definition.execution()==SpellDefinition.Execution.PHASE_TWO) {
				check(definition.rank()==2,"New spells retain intermediate status durations");
				check(!MagicState.initial(100).learn(definition.id()).learned().contains(definition.id()),"New spell cannot bypass its learning prerequisites");
			}
		}
		check(SpellDefinition.EX_HEALING.healingAmount(17,20)==3 && SpellDefinition.EX_HEALING.healingAmount(20,20)==0,"Intermediate healing clamps and rejects full health");
		check(SpellDefinition.EX_HEALING.healingAmount(4,20)==8,"Intermediate healing restores eight points");
		for(int rate:new int[]{4,6}) {
			int debt=0, paidChannel=0;
			for(int tick=1;tick<=1000;tick++) {
				int debit=com.example.examplemod.magic.mechanic.channel.ChannelCost.calculate(debt,rate).debit();
				paidChannel+=debit; debt=com.example.examplemod.magic.mechanic.channel.ChannelCost.remainder(debt,rate,debit);
				check(debt>=-19 && debt<=0,"Fractional channel debt stays inside persistent codec range");
				check(paidChannel*20+debt==tick*rate,"Every channel tick is paid exactly, including fractional carry");
				check(paidChannel==(tick*rate+19)/20,"Restart-safe prepayment never gives free fractional ticks");
			}
			check(paidChannel==rate*50,"Channel rate remains exact over fifty seconds");
		}
		int fractional=0, switchingCost=0, units=0;
		for(int tick=0;tick<123;tick++) {
			int rate=tick%2==0?4:6; int debit=com.example.examplemod.magic.mechanic.channel.ChannelCost.calculate(fractional,rate).debit();
			switchingCost+=debit;units+=rate;fractional=com.example.examplemod.magic.mechanic.channel.ChannelCost.remainder(fractional,rate,debit);
		}
		check(switchingCost==(units+19)/20,"Rapid channel switching preserves prepaid fractions");
		var movement=com.example.examplemod.magic.MagicControl.boundMovement(new net.minecraft.world.phys.Vec3(4,-0.7,3));
		check(movement.lengthSqr()==0,"Wind bind prevents all displacement");
		check(com.example.examplemod.magic.MagicControl.boundMovement(new net.minecraft.world.phys.Vec3(0,0.42,0)).y==0,"Wind bind prevents jumping");
		check(com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerHit.inCone(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(new net.minecraft.world.phys.Vec3(0,0,16)),com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(south),16),"Flame reaches sixteen blocks");
		check(!com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerHit.inCone(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(new net.minecraft.world.phys.Vec3(0,0,16.1)),com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(south),16),"Flame cannot exceed range");
		check(!com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerHit.inCone(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(new net.minecraft.world.phys.Vec3(3,0,1)),com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(south),16),"Flame excludes targets outside its half angle");
		check(!com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerHit.inCone(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(new net.minecraft.world.phys.Vec3(0,0,-1)),com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(south),16),"Flame cannot hit behind the player");
		int pulses=0;for(int t=100;t>0;t--)if(com.example.examplemod.magic.spell.wind.wind_bind.WindBindSpec.pulse(t))pulses++;
		check(pulses==5&&!com.example.examplemod.magic.spell.wind.wind_bind.WindBindSpec.pulse(100),"Wind bind delays damage and pulses five times");
		check(com.example.examplemod.infrastructure.fabric.geometry.LaunchImpulseAdapter.launchVelocity(new net.minecraft.world.phys.Vec3(0,1,0),6).y>0,"Launch imparts upward velocity");
		for(double height:new double[]{0.5,3,6})check(Math.abs(com.example.examplemod.magic.mechanic.projectile.LaunchBallistics.launchPeak(com.example.examplemod.infrastructure.fabric.geometry.LaunchImpulseAdapter.launchVelocity(new net.minecraft.world.phys.Vec3(0,1,0),height).y)-height)<1.0E-7,"Vertical launch reaches requested height under vanilla gravity");
		var angledShield=new SpellSweep(new net.minecraft.world.phys.Vec3(1,0,1),3,3,0.2);
		check(!angledShield.intersects(new net.minecraft.world.phys.AABB(0.8,0,0.8,0.9,0.1,0.9),net.minecraft.world.phys.Vec3.ZERO),"Angled shield does not block empty bounding-box corners");
		check(Double.isFinite(angledShield.contact(new net.minecraft.world.phys.AABB(-2,0,-2,-1.9,0.1,-1.9),net.minecraft.world.phys.Vec3.ZERO,new net.minecraft.world.phys.Vec3(-1,0,-1).normalize(),4)),"Angled shield detects incoming movement");
		var ceilingShield=new SpellSweep(new net.minecraft.world.phys.Vec3(0,1,0),3,3,0.2);
		check(Double.isFinite(ceilingShield.contact(new net.minecraft.world.phys.AABB(0,2,0,0.1,2.1,0.1),net.minecraft.world.phys.Vec3.ZERO,new net.minecraft.world.phys.Vec3(0,1,0),3)),"Vertical shield supports full pitch collision");

		var plane=new net.minecraft.world.phys.Vec3(0,0,1);
		check(com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldCollisionAdapter.shieldOverlapMotion(new net.minecraft.world.phys.Vec3(0,0,-0.5),plane,0.2).lengthSqr()==0,"Overlapping creature cannot pass through shield");
		check(com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldCollisionAdapter.shieldOverlapMotion(new net.minecraft.world.phys.Vec3(0,0,0.5),plane,-0.2).lengthSqr()==0,"Shield blocks crossing from either side");
		check(com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldCollisionAdapter.shieldOverlapMotion(new net.minecraft.world.phys.Vec3(0.2,0,0.5),plane,0.2).z==0.5,"Overlapping creature can escape away from shield");
		check(com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldCollisionAdapter.shieldOverlapMotion(new net.minecraft.world.phys.Vec3(0.2,0,-0.5),plane,0.2).x==0.2,"Shield retains tangent movement");
		check(SpellDefinition.WIND_BLAST.cooldownTicks()==10,"Intermediate recovery is 0.5 seconds");
		check(SpellDefinition.EARTH_LANCE.cooldownTicks()==10&&SpellDefinition.ICICLE_BREAK.cooldownTicks()==10,"Earth lance and ice shot cooldowns are 0.5 seconds");

		var casterFeet=new net.minecraft.core.BlockPos(5,64,-3);
		check(com.example.examplemod.infrastructure.fabric.magic.EarthLanceAdapter.underfoot(new net.minecraft.core.BlockPos(5,63,-3),casterFeet),"Earth lance builds dirt directly under caster");
		check(!com.example.examplemod.infrastructure.fabric.magic.EarthLanceAdapter.underfoot(new net.minecraft.core.BlockPos(6,63,-3),casterFeet),"Earth lance does not build dirt on nearby terrain");
		check(!com.example.examplemod.infrastructure.fabric.magic.EarthLanceAdapter.underfoot(new net.minecraft.core.BlockPos(5,60,-3),casterFeet),"Earth lance does not build dirt far below caster");
		check(com.example.examplemod.magic.spell.fire.explosion.ExplosionSpec.TNT_POWER==4F,"Explosion uses the vanilla single-TNT power");

		int taught = 0;
		for (var category : MagicCategory.values()) for (int rank = 1; rank <= 2; rank++) {
			for (String id : MagicBooks.lessons(category, rank)) {
				taught++; var definition = SpellDefinition.find(id);
				check(definition.rank() == rank && MagicCategory.of(definition) == category, "Each course has the correct category and rank");
			}
		}
		check(HolyInstantAdapter.inColumn(new net.minecraft.world.phys.Vec3(2, 0, 15), new net.minecraft.world.phys.Vec3(0, 0, 1), 15), "Holy ray includes cylinder boundary");
		check(!HolyInstantAdapter.inColumn(new net.minecraft.world.phys.Vec3(2.01, 0, 15), new net.minecraft.world.phys.Vec3(0, 0, 1), 15), "Holy ray excludes outside cylinder");
		check(!HolyInstantAdapter.inColumn(new net.minecraft.world.phys.Vec3(0, 0, 15.01), new net.minecraft.world.phys.Vec3(0, 0, 1), 15), "Holy ray stops at wall");
		check(SpellDefinition.GOD_PURGE.castingProfile().requiresCircle() && SpellDefinition.GOD_PURGE.manaCost() == 1800, "God Purge uses the divine circle and its own mana charge");
		check(DivineCircle.withinRange(new net.minecraft.world.phys.Vec3(3.5, 65, .5), new net.minecraft.core.BlockPos(0,64,0)), "Circle accepts three-block boundary");
		check(!DivineCircle.withinRange(new net.minecraft.world.phys.Vec3(3.51, 65, .5), new net.minecraft.core.BlockPos(0,64,0)), "Circle rejects movement outside boundary");
		int runes = 0; for (int x=-3;x<=3;x++) for(int z=-3;z<=3;z++) if(DivineCircle.rune(x,z)) runes++;
		check(runes == 8, "Circle has eight rune positions");
		check(com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.wallContains(new net.minecraft.world.phys.Vec3(8,8,2),new net.minecraft.world.phys.Vec3(0,0,1)) && !com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.wallContains(new net.minecraft.world.phys.Vec3(8.01,8,2),new net.minecraft.world.phys.Vec3(0,0,1)), "Fire wall is sixteen by eight by four");
		check(com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.damagePulse("apocalypse_flame",59)==-1 && com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.damagePulse("apocalypse_flame",60)==0 && com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.damagePulse("apocalypse_flame",360)==15, "Apocalypse warns three seconds then has fifteen residual pulses");
		check(com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.damagePulse("terra_fall",19)==-1 && com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.damagePulse("terra_fall",20)==0 && com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.damagePulse("terra_fall",60)==1 && com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.damagePulse("terra_fall",100)==2, "Three falling-earth waves each have a preceding warning");
		check(OceanFreezeRuntimeAdapter.radius(0)==0 && OceanFreezeRuntimeAdapter.radius(20)==8 && OceanFreezeRuntimeAdapter.radius(160)==64 && OceanFreezeRuntimeAdapter.radius(99999)==64, "Ocean freezes at eight blocks per second with a bounded radius");
		check(DivineJudgmentAdapter.pulseIndex(0) == 0 && DivineJudgmentAdapter.pulseIndex(19) == 0 && DivineJudgmentAdapter.pulseIndex(20) == 1 && DivineJudgmentAdapter.pulseIndex(40) == 2 && DivineJudgmentAdapter.pulseIndex(60) == -1, "Judgment has three one-second pulse windows");
		check(taught == 47, "Beginner and intermediate catalogs include legacy spells, earth wall and blast");
		for (int rank = 1; rank <= 7; rank++) check(CastingProfile.of(rank, CastingProfile.Mode.FULL).preparationTicks() > 0, "All full-chant tiers configured");
		check(!CastingProfile.unlocked(CastingProfile.Mode.SILENT) && !CastingProfile.unlocked(CastingProfile.Mode.SHORTENED), "Future modes remain locked");
		check(CastingProfile.of(7, CastingProfile.Mode.FULL).requiresCircle(), "Divine tier requires circle");
		for (int count = 4; count <= 12; count++) {
			int[] letters = new int[count];
			for (int i = 0; i < count; i++) letters[i] = (i * 7) % 26;
			var lesson = new ChantScore(letters);
			for (int i = 0; i < count; i++) {
				for (int j = 0; j < 26; j++) if (j != letters[i]) lesson.press(j);
				check(lesson.index() == i && !lesson.finished(), "Wrong guesses preserve progress and never end the lesson");
				lesson.press(letters[i]);
				check(lesson.index() == i + 1, "Correct next letter lights exactly one symbol");
			}
			check(lesson.passed(), "All letters in sequence complete learning");
			lesson.press(letters[0]);
			check(lesson.index() == count, "Completed lesson is stable");
		}
		for (int rank = 1; rank <= 7; rank++) {
			var profile = CastingProfile.of(rank, CastingProfile.Mode.FULL);
			var special = new SpellDefinition("timing_test", 3000, 60, 0, 1, "fire", 0, 0, 0,
					SpellDefinition.CastMode.INSTANT, List.of(), SpellDefinition.Execution.AREA, 0, 32, 1, List.of(), rank, false, profile);
			check(special.officialRank() == 0 && special.rank() == rank, "Unknown official grade retains timing");
			check(special.cooldownTicks() == profile.recoveryTicks(), "Explicit timing controls cooldown");
			var sections = new java.util.ArrayList<int[]>();
			for (int j = 0; j < SegmentedChantScore.sectionCount(rank); j++) sections.add(new int[rank >= 6 ? 10 : rank == 5 ? 8 : 2 + rank * 2]);
			var progress = new SegmentedChantScore(rank, sections, 0);
			progress.press(1); check(progress.currentIndex() == 0, "Wrong input preserves progress");
			progress.press(0); progress.restartCurrentSection();
			check(progress.currentIndex() == 0, "Incomplete section restarts");
			for (int j = 0; j < sections.getFirst().length; j++) progress.press(0);
			check(progress.completedSections() == 1, "Completed section checkpoint");
			var resumed = new SegmentedChantScore(rank, sections, progress.completedSections());
			for (int j = 1; j < sections.size(); j++) for (int key : sections.get(j)) resumed.press(key);
			check(resumed.finished(), "Resume remaining sections");
		}
		System.out.println("MagicState: " + assertions + " behavior assertions passed.");
	}
}
