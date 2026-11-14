package com.example.examplemod.infrastructure.fabric.magic;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.TargetedArea;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.water.water_splash.*;
import com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter;
/** Instantaneous water burst. Damage-source entity is never added as a flying projectile. */
public record WaterSplashAdapter(ServerPlayer player) implements TargetedArea {
	public Optional<Vec3d> locate(float yaw,float pitch) {
		var level=(ServerLevel)player.level();var from=player.getEyePosition();var direction=new MagicAim(yaw,pitch).direction();
		for(int step=0;step<=12;step++)if(!level.hasChunkAt(BlockPos.containing(from.add(direction.scale(step)))))return Optional.empty();
		var hit=level.clip(new ClipContext(from,from.add(direction.scale(12)),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player));
		return Optional.of(WaterSplashRules.center(GeometryAdapter.vector(from),GeometryAdapter.vector(direction),hit.getType()==HitResult.Type.MISS?null:GeometryAdapter.vector(hit.getLocation())));
	}
	public void apply(Vec3d point,float yaw,float pitch) {
		var level=(ServerLevel)player.level();var center=GeometryAdapter.vector(point);var aim=new MagicAim(yaw,pitch);
		var source=SweptProjectileSpawner.projectile(player,SpellDefinition.WATER_SPLASH,aim,10);source.setPos(center);
		for(var entity:level.getEntities(player,GeometryAdapter.box(WaterSplashRules.bounds(point)),e->e instanceof LivingEntity && MagicEffects.allowed(player,(LivingEntity)e))) {
			var target=(LivingEntity)entity;
			if(!WaterSplashRules.affects(point,GeometryAdapter.vector(target.getBoundingBox().getCenter()),true)||!SpellVisibilityAdapter.visible(level,center,target,player))continue;
			if(target.hurtServer(level,MagicDamage.source(source,SpellDefinition.WATER_SPLASH),WaterSplashSpec.DAMAGE)) {
				target.addEffect(new MobEffectInstance(MagicEffects.WET,WaterSplashSpec.WET_TICKS,0),player);SpellHooks.afterHit(source,target);
			}
		}
		SpellVisuals.burst(level,SpellDefinition.WATER_SPLASH,center,aim.direction());
	}
	public boolean cast(MagicAim aim){var point=locate(aim.yaw(),aim.pitch());if(point.isEmpty())return false;apply(point.get(),aim.yaw(),aim.pitch());return true;}
}
