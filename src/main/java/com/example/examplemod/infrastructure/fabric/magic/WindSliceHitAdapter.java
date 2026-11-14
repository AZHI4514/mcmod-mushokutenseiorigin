package com.example.examplemod.infrastructure.fabric.magic;
import java.util.UUID;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.wind.wind_slice.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
/** Original broad phase and center sphere, without additional per-target visibility checks. */
public record WindSliceHitAdapter(DirectionalSpellCarrier carrier,ServerLevel level,LivingEntity direct) implements WindSliceHit.Targets<Entity> {
	@Override public Iterable<Entity> candidates() { return level.getEntities(carrier,direct.getBoundingBox().inflate(WindSliceSpec.RADIUS),e->e instanceof LivingEntity&&WindSliceHit.inRadius(e.getBoundingBox().getCenter().distanceToSqr(direct.getBoundingBox().getCenter()))&&MagicEffects.allowed(carrier.getOwner(),(LivingEntity)e)); }
	@Override public UUID identity(Entity target) { return target.getUUID(); }
	@Override public boolean direct(Entity target) { return target==direct; }
	@Override public void damage(Entity target) { CarrierDamageAdapter.damage(carrier,(LivingEntity)target); }
}
