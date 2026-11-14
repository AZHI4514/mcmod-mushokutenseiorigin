package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.LocalHitTargets;
import com.example.examplemod.magic.domain.model.Box3d;
import com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
/** Preserves original broad-phase order, visibility origin and vanilla damage/hooks. */
public record IceLanceHitAdapter(DirectionalSpellCarrier carrier,ServerLevel level) implements LocalHitTargets<LivingEntity> {
	@Override public Iterable<LivingEntity> candidates() {
		return level.getEntities(carrier,carrier.getBoundingBox(),e->e instanceof LivingEntity).stream().map(e->(LivingEntity)e).toList();
	}
	@Override public Box3d bounds(LivingEntity target) { return GeometryAdapter.box(target.getBoundingBox()); }
	@Override public boolean visible(LivingEntity target) { return SpellVisibilityAdapter.visible(level,carrier.position().add(0,.1,0),target,carrier); }
	@Override public void damage(LivingEntity target) { CarrierDamageAdapter.damage(carrier,target); }
}
