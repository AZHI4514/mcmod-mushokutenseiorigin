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
		check(four.learned().size() == 15 && four.loaded().size() == 10, "All lessons known, wheel limited to ten");
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
		System.out.println("MagicState: " + assertions + " behavior assertions passed.");
	}
}
