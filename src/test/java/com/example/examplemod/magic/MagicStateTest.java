package com.example.examplemod.magic;

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
		var spell = SpellDefinition.WATER_BALL;
		var state = MagicState.initial(100);
		check(!state.canCast(spell), "Unknown spell cannot cast");
		state = state.learn(spell.id());
		check(state.canCast(spell), "First learning loads and selects spell");
		check(state.learn(spell.id()).equals(state), "Learning is idempotent");
		var paid = state.cast(spell);
		check(paid.mana() == 99 && paid.cooldown() == 2, "One successful cast debits exactly once");
		check(!paid.canCast(spell), "Immediate second cast denied");
		check(!paid.tick().canCast(spell), "One tick is insufficient");
		check(paid.tick().tick().canCast(spell), "Two tick cooldown");
		try { paid.cast(spell); throw new AssertionError("Cooldown bypass"); }
		catch (IllegalStateException expected) { assertions++; }
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
		check(four.learned().size() == 39 && four.loaded().size() == 10, "All lessons known, wheel limited to ten");
		check(four.selected().equals(spell.id()), "Later lessons preserve selection");
		for (var definition : SpellDefinition.ALL) {
			var ready = new MagicState(100, 100, 0, 0, four.learned(), List.of(definition.id()), definition.id());
			var spent = ready.cast(definition);
			check(spent.mana() == 100 - definition.manaCost(), "Exact cost: " + definition.id());
			for (int i = 1; i < definition.cooldownTicks(); i++) {
				spent = spent.tick();
				check(!spent.canCast(definition), "No early cooldown: " + definition.id());
			}
			check(spent.tick().canCast(definition), "Cooldown ends exactly: " + definition.id());
			var poor = new MagicState(definition.manaCost() - 1, 100, 0, 0, four.learned(), List.of(definition.id()), definition.id());
			check(!poor.canCast(definition), "Reject insufficient mana: " + definition.id());
			check(ready.learn(definition.id()).equals(ready), "No duplicate lesson: " + definition.id());
			check(definition.prerequisitesMet(id -> false), "Basic spell has no external dependency");
		}
		check(SpellDefinition.ICE_BLADE.range() == 15, "Ice Blade travels straight for fifteen blocks");
		check(SpellDefinition.ICE_BLADE.speed() == 1.125F, "Ice blade speed multiplied by 1.5");
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
		check(SpellDefinition.WATER_SHIELD.cooldownTicks() == 60 && SpellDefinition.BURNING_PLACE.cooldownTicks() == 60
				&& SpellDefinition.MAGIC_SHIELD.cooldownTicks() == 80, "Confirmed cooldowns leave no maintenance gaps");
		check(MagicState.initial(100).learn("water_shield").learned().isEmpty(), "Water Shield requires Water Ball");
		check(MagicState.initial(100).learn("ice_blade").learned().isEmpty(), "Ice Blade requires Ice Smash");
		check(MagicState.initial(100).learn("heat_island").learned().isEmpty(), "Heat Island requires Heat Hand");
		check(SpellDefinition.ICE_BLADE.icy() && !SpellDefinition.WATER_ARROW.icy(), "Ice status separate from water status");
		var wallGeometry = new MagicWallVolume(new net.minecraft.world.phys.Vec3(0,0,1),0);
		var arrowBox = new net.minecraft.world.phys.AABB(-0.1,0.9,2,0.1,1.1,2.2);
		check(Double.isFinite(wallGeometry.contact(arrowBox,new net.minecraft.world.phys.Vec3(0,1,0),new net.minecraft.world.phys.Vec3(0,0,1),3)), "Water wall sweeps fast projectile");
		check(!wallGeometry.intersects(new net.minecraft.world.phys.AABB(1.6,0.9,-0.1,1.8,1.1,0.1),new net.minecraft.world.phys.Vec3(0,1,0)), "Outside water wall width does not block");
		check(wallGeometry.intersects(new net.minecraft.world.phys.AABB(-0.1,1.8,-0.1,0.1,1.9,0.1),new net.minecraft.world.phys.Vec3(0,1,0)), "Upper half of two-block water wall contacts");
		for (int yaw=-180; yaw<180; yaw+=45) {
			var normal = net.minecraft.world.phys.Vec3.directionFromRotation(0,yaw);
			var rotated = new MagicWallVolume(normal,yaw);
			check(Math.abs(rotated.side.dot(normal)) < 1.0E-6 && rotated.up.y > 0.99, "Water wall orthonormal at yaw " + yaw);
			var hitCenter = normal.scale(2).add(0,1,0);
			var hitBox = new net.minecraft.world.phys.AABB(hitCenter.x-0.1,0.9,hitCenter.z-0.1,hitCenter.x+0.1,1.1,hitCenter.z+0.1);
			check(Double.isFinite(rotated.contact(hitBox,new net.minecraft.world.phys.Vec3(0,1,0),normal,3)), "Water wall rotated sweep " + yaw);
		}
		check(SpellDefinition.WATER_CANNON.rank()==2 && SpellDefinition.EX_FLAME.statusTicks()==200,"Intermediate rank and status duration");
		check(IntermediateRules.chargeDisplay(10)==10 && IntermediateRules.chargeDisplay(19)==19 && IntermediateRules.chargeDisplay(20)==20,"Charge UI displays actual paid mana");
		check(SpellDefinition.WATER_SPLASH.statusTicks()==2000,"Water Splash wetness lasts 100 seconds");
		check(SpellDefinition.ICICLE_FIELD.damage()==5,"Icicle Field deals five damage");
		check(IntermediateRules.chargeExtraAfterTicks(0)==0 && IntermediateRules.chargeExtraAfterTicks(20)==15 && IntermediateRules.chargeExtraAfterTicks(40)==40 && IntermediateRules.chargeExtraAfterTicks(60)==75,"Linear rate acceleration integrates to increasing per-second costs");
		long injected=0;
		for(int t=1;t<=100;t++) {
			long debit=IntermediateRules.chargeExtraAfterTicks(t)-IntermediateRules.chargeExtraAfterTicks(t-1);
			check(debit>=0,"Injection never refunds mana during charging");
			injected+=debit;
		}
		check(injected==175,"Fractional tick amounts carry forward without lost mana");
		check(IntermediateRules.chargeExtraAfterTicks(Integer.MAX_VALUE)>Integer.MAX_VALUE,"Large charge duration uses safe long arithmetic");
		check(IntermediateRules.stoneDamage(10)==10 && IntermediateRules.stoneDamage(19)==10 && IntermediateRules.stoneDamage(20)==11,"Stone damage counts full extra tens");
		check(IntermediateRules.stoneDamage(1000000)==100009,"No arbitrary stone damage cap");
		var debited=four.spendMana(13);
		check(debited.mana()==87&&debited.cooldown()==0,"Charge prepayment does not start cooldown");
		check(debited.restoreMana(13).equals(four),"Charge refund preserves live state");
		try { four.spendMana(101); throw new AssertionError("Charge overspend"); } catch(IllegalArgumentException expected) { assertions++; }
		var anchor=new net.minecraft.core.BlockPos(5,5,5); var hitPoint=new net.minecraft.world.phys.Vec3(5,5,5);
		check(IntermediateRules.blastBlocks(anchor,1,hitPoint).equals(List.of(anchor)),"Water blast exactly one block");
		check(IntermediateRules.blastBlocks(anchor,2,hitPoint).size()==8&&IntermediateRules.blastBlocks(anchor,2,hitPoint).stream().distinct().count()==8,"Fire blast exactly eight distinct positions");
		check(IntermediateRules.impactBounds(hitPoint).getXsize()==3&&IntermediateRules.impactBounds(hitPoint).getYsize()==3,"Blast damage volume side length three");
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
		check(!firePaid.canCast(spell), "Switching cannot bypass global cooldown");
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
		check(restored.equals(flight) && restored.remaining() == 7.75, "Reload preserves ten-block range");
		check(!flight.advance(7.5).exhausted(), "Blade still flies before ten-block limit");
		check(flight.advance(7.75).exhausted(), "Blade expires at exactly ten blocks");
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
		check(SpellDefinition.ALL.size()==39 && MagicBooks.LESSONS.size()==39,"Thirty-nine spells share both textbooks");
		check(MagicBooks.LESSONS.stream().distinct().count()==39,"No duplicated textbook lessons");
		for(var definition:SpellDefinition.ALL) {
			check(MagicBooks.LESSONS.contains(definition.id()),"Every registered spell is teachable");
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
				int debit=PhaseTwoRules.debit(debt,rate);
				paidChannel+=debit; debt=PhaseTwoRules.remainder(debt,rate,debit);
				check(debt>=-19 && debt<=0,"Fractional channel debt stays inside persistent codec range");
				check(paidChannel*20+debt==tick*rate,"Every channel tick is paid exactly, including fractional carry");
				check(paidChannel==(tick*rate+19)/20,"Restart-safe prepayment never gives free fractional ticks");
			}
			check(paidChannel==rate*50,"Channel rate remains exact over fifty seconds");
		}
		int fractional=0, switchingCost=0, units=0;
		for(int tick=0;tick<123;tick++) {
			int rate=tick%2==0?4:6; int debit=PhaseTwoRules.debit(fractional,rate);
			switchingCost+=debit;units+=rate;fractional=PhaseTwoRules.remainder(fractional,rate,debit);
		}
		check(switchingCost==(units+19)/20,"Rapid channel switching preserves prepaid fractions");
		var movement=PhaseTwoRules.boundMovement(new net.minecraft.world.phys.Vec3(4,-0.7,3));
		check(movement.lengthSqr()==0,"Wind bind prevents all displacement");
		check(PhaseTwoRules.boundMovement(new net.minecraft.world.phys.Vec3(0,0.42,0)).y==0,"Wind bind prevents jumping");
		check(PhaseTwoRules.inCone(new net.minecraft.world.phys.Vec3(0,0,16),south,16),"Flame reaches sixteen blocks");
		check(!PhaseTwoRules.inCone(new net.minecraft.world.phys.Vec3(0,0,16.1),south,16),"Flame cannot exceed range");
		check(!PhaseTwoRules.inCone(new net.minecraft.world.phys.Vec3(3,0,1),south,16),"Flame excludes targets outside its half angle");
		check(!PhaseTwoRules.inCone(new net.minecraft.world.phys.Vec3(0,0,-1),south,16),"Flame cannot hit behind the player");
		int pulses=0;for(int t=100;t>0;t--)if(PhaseTwoRules.bindPulse(t))pulses++;
		check(pulses==5&&!PhaseTwoRules.bindPulse(100),"Wind bind delays damage and pulses five times");
		check(PhaseTwoRules.launchVelocity(new net.minecraft.world.phys.Vec3(0,1,0),6).y>0,"Launch imparts upward velocity");
		for(double height:new double[]{0.5,3,6})check(Math.abs(PhaseTwoRules.launchPeak(PhaseTwoRules.launchVelocity(new net.minecraft.world.phys.Vec3(0,1,0),height).y)-height)<1.0E-7,"Vertical launch reaches requested height under vanilla gravity");
		var angledShield=new SpellSweep(new net.minecraft.world.phys.Vec3(1,0,1),3,3,0.2);
		check(!angledShield.intersects(new net.minecraft.world.phys.AABB(0.8,0,0.8,0.9,0.1,0.9),net.minecraft.world.phys.Vec3.ZERO),"Angled shield does not block empty bounding-box corners");
		check(Double.isFinite(angledShield.contact(new net.minecraft.world.phys.AABB(-2,0,-2,-1.9,0.1,-1.9),net.minecraft.world.phys.Vec3.ZERO,new net.minecraft.world.phys.Vec3(-1,0,-1).normalize(),4)),"Angled shield detects incoming movement");
		var ceilingShield=new SpellSweep(new net.minecraft.world.phys.Vec3(0,1,0),3,3,0.2);
		check(Double.isFinite(ceilingShield.contact(new net.minecraft.world.phys.AABB(0,2,0,0.1,2.1,0.1),net.minecraft.world.phys.Vec3.ZERO,new net.minecraft.world.phys.Vec3(0,1,0),3)),"Vertical shield supports full pitch collision");

		var plane=new net.minecraft.world.phys.Vec3(0,0,1);
		check(PhaseTwoRules.shieldOverlapMotion(new net.minecraft.world.phys.Vec3(0,0,-0.5),plane,0.2).lengthSqr()==0,"Overlapping creature cannot pass through shield");
		check(PhaseTwoRules.shieldOverlapMotion(new net.minecraft.world.phys.Vec3(0,0,0.5),plane,-0.2).lengthSqr()==0,"Shield blocks crossing from either side");
		check(PhaseTwoRules.shieldOverlapMotion(new net.minecraft.world.phys.Vec3(0.2,0,0.5),plane,0.2).z==0.5,"Overlapping creature can escape away from shield");
		check(PhaseTwoRules.shieldOverlapMotion(new net.minecraft.world.phys.Vec3(0.2,0,-0.5),plane,0.2).x==0.2,"Shield retains tangent movement");
		check(SpellDefinition.WIND_BLAST.cooldownTicks()==4,"Wind blast cooldown is 0.2 seconds");
		check(SpellDefinition.EARTH_LANCE.cooldownTicks()==10&&SpellDefinition.ICICLE_BREAK.cooldownTicks()==10,"Earth lance and ice shot cooldowns are 0.5 seconds");

		var casterFeet=new net.minecraft.core.BlockPos(5,64,-3);
		check(PhaseTwoRules.underfoot(new net.minecraft.core.BlockPos(5,63,-3),casterFeet),"Earth lance builds dirt directly under caster");
		check(!PhaseTwoRules.underfoot(new net.minecraft.core.BlockPos(6,63,-3),casterFeet),"Earth lance does not build dirt on nearby terrain");
		check(!PhaseTwoRules.underfoot(new net.minecraft.core.BlockPos(5,60,-3),casterFeet),"Earth lance does not build dirt far below caster");
		check(PhaseTwoRules.TNT_POWER==4F,"Explosion uses the vanilla single-TNT power");
		System.out.println("MagicState: " + assertions + " behavior assertions passed.");
	}
}
