package com.example.examplemod.magic;

import com.example.examplemod.magic.domain.combat.EffectRules;
import com.example.examplemod.magic.mechanic.field.FieldSchedule;

/** 独立可测的等级时长、续期和护盾数值规则；均以服务端tick为单位。 */
public final class MagicRules {
	public static boolean passesWater(SpellDefinition spell, boolean water) { return passesWater(spell, water, false); }
	public static boolean passesWater(SpellDefinition spell, boolean water, boolean waterloggable) { return (water || waterloggable) && spell != null && spell.piercing(); }
	public static boolean touchesBody(net.minecraft.world.phys.AABB owner, net.minecraft.world.phys.AABB target) { return com.example.examplemod.magic.domain.geometry.ContactGeometry.body(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.box(owner),com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.box(target)); }

	public static boolean strikeReady(long now,long next,long expiry) { return FieldSchedule.strikeReady(now, next, expiry); }
	public static boolean fieldPulse(int age, int duration, int interval) { return FieldSchedule.pulse(age, duration, interval); }
	public static int flightTicks(int rank) { return EffectRules.flightTicks(rank); }
	public static int statusTicks(int rank) { return EffectRules.statusTicks(rank); }
	public static int extend(int remaining, int duration) {
		return EffectRules.extend(remaining, duration);
	}
	public static float shieldAfter(float remaining, float incoming) { return EffectRules.shieldAfter(remaining, incoming); }
	/** 包围盒到环心的最近/最远水平距离区间必须与火环相交。 */
	public static boolean touchesRing(net.minecraft.world.phys.AABB box,net.minecraft.world.phys.Vec3 center,double radius){
		return com.example.examplemod.magic.domain.geometry.ContactGeometry.ring(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.box(box),com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(center),radius);
	}
	private MagicRules() { }
}
