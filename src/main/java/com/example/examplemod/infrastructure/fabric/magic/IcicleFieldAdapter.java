package com.example.examplemod.infrastructure.fabric.magic;
import java.util.Optional;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.TargetedArea;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.water.icicle_field.IcicleFieldRules;
import com.example.examplemod.magic.mechanic.projectile.DeferredAreaImpact;
import com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
/** Field damage is resolved before encasing terrain can obstruct subsequent targets. */
public record IcicleFieldAdapter(ServerPlayer owner) implements TargetedArea {
	public Optional<Vec3d> locate(float yaw,float pitch) {
		var level=(ServerLevel)owner.level();var from=owner.getEyePosition();var aim=new MagicAim(yaw,pitch);
		for(int step=0;step<=12;step++)if(!level.hasChunkAt(BlockPos.containing(from.add(aim.direction().scale(step)))))return Optional.empty();
		var hit=level.clip(new ClipContext(from,from.add(aim.direction().scale(12)),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));
		return Optional.of(IcicleFieldRules.center(GeometryAdapter.vector(from),GeometryAdapter.vector(aim.direction()),hit.getType()==HitResult.Type.MISS?null:GeometryAdapter.vector(hit.getLocation())));
	}
	public void apply(Vec3d point,float yaw,float pitch) { apply(point,yaw,pitch,10); }
	public void apply(Vec3d point,float yaw,float pitch,int paid) {
		var level=(ServerLevel)owner.level();var spell=SpellDefinition.ICICLE_FIELD;var aim=new MagicAim(yaw,pitch);var center=GeometryAdapter.vector(point);
		var projectile=SweptProjectileSpawner.projectile(owner,spell,aim,paid);projectile.setPos(center);
		var range=GeometryAdapter.box(IcicleFieldRules.bounds(point));
		DeferredAreaImpact.apply(new DeferredAreaImpact.World<LivingEntity>() {
			public Iterable<LivingEntity> targets() {
				// The original query returns only permitted living entities, in world query order.
				var targets=level.getEntities(owner,range,e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e));
				return () -> targets.stream().map(e->(LivingEntity)e).iterator();
			}
			public boolean affects(LivingEntity target) {
				return !(!IcicleFieldRules.within(point,GeometryAdapter.vector(target.getBoundingBox().getCenter()))||!SpellVisibilityAdapter.visible(level,center,target,owner));
			}
			public boolean wet(LivingEntity target) { return target.hasEffect(MagicEffects.WET); }
			public boolean hurt(LivingEntity target) { return target.hurtServer(level,MagicDamage.source(projectile,spell),spell.damage()); }
			public boolean applyEffects(LivingEntity target, boolean wet) {
				MagicEffects.apply(owner,target,spell);
				if(wet) { target.removeEffect(MagicEffects.WET); return true; }
				return false;
			}
			public void afterHit(LivingEntity target) { SpellHooks.afterHit(projectile,target); }
			public void encase(LivingEntity target) { com.example.examplemod.infrastructure.fabric.magic.TemporaryTerrainAdapter.encase(owner,target); }
			public void burst() { SpellVisuals.burst(level,spell,center,aim.direction()); }
		});
	}
	public boolean cast(MagicAim aim,int paid){var point=locate(aim.yaw(),aim.pitch());if(point.isEmpty())return false;apply(point.get(),aim.yaw(),aim.pitch(),paid);return true;}
}
