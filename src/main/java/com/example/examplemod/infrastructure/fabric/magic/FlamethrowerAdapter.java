package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.FlameStream;
import com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerHit;
import com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;
/** Real candidate queries, vanilla damage and unchanged eight-segment particle emission. */
public record FlamethrowerAdapter(ServerPlayer player) implements FlameStream {
	@Override public void emit(float yaw,float pitch) {
		var level=(ServerLevel)player.level();var origin=player.getEyePosition();var forward=new MagicAim(yaw,pitch).direction();
		var spell=SpellDefinition.FLAMETHROWER;long now=level.getGameTime();
		var carrier=EffectCarrierSpawner.entity(player,spell,origin,forward);
		FlamethrowerHit.apply(GeometryAdapter.vector(forward),new FlamethrowerHit.Targets<LivingEntity>() {
			public Iterable<LivingEntity> candidates() { return level.getEntities(player,player.getBoundingBox().inflate(16),e->e instanceof LivingEntity&&MagicEffects.allowed(player,(LivingEntity)e)).stream().map(e->(LivingEntity)e).toList(); }
			public com.example.examplemod.magic.domain.model.Vec3d delta(LivingEntity target) { return GeometryAdapter.vector(target.getBoundingBox().getCenter().subtract(origin)); }
			public boolean visible(LivingEntity target) { return SpellVisibilityAdapter.visible(level,origin,target,carrier); }
			public boolean claim(LivingEntity target) { return PlayerSpellRuntime.contactReady(level,player.getUUID(),target.getUUID(),now); }
			public void damage(LivingEntity target) { CarrierDamageAdapter.damage(carrier,target); }
		});
		for(int i=1;i<=16;i+=2) {
			var pos=origin.add(forward.scale(i));
			if(!SpellRaycastAdapter.loaded(level,new AABB(origin,pos).inflate(0.01))) break;
			if(level.clip(new net.minecraft.world.level.ClipContext(origin,pos,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,player)).getType()!=HitResult.Type.MISS) break;
			level.sendParticles(SpellParticles.trail(spell),pos.x,pos.y,pos.z,3,i*0.13,i*0.13,i*0.13,0.01);
		}
	}
}
