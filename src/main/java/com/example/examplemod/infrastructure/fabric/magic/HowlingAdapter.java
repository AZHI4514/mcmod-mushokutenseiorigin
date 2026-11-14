package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.special.howling.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record HowlingAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	public static boolean cast(ServerPlayer owner,MagicAim aim){var spell=SpellDefinition.HOWLING;var level=(ServerLevel)owner.level();var from=owner.getEyePosition();

		var carrier=new MagicAreaEntity(MagicSystem.MAGIC_AREA,level);carrier.configure(owner,spell,aim);carrier.setPos(from);
		for(var target:level.getEntitiesOfClass(LivingEntity.class,new AABB(from,from).inflate(12),t->MagicEffects.allowed(owner,t))){
			var d=target.getBoundingBox().getCenter().subtract(from);double length=d.length();
			HowlingHit.apply(new HowlingHit.Target(){public boolean inside(){return HowlingRules.inside(length,d.dot(aim.direction()));}public boolean visible(){return SpellVisibilityAdapter.visible(level,from,target,carrier);}public boolean hurt(){return target.hurtServer(level,MagicDamage.source(carrier,spell),4);}public void control(){MagicControl.apply(target,HowlingRules.controlTicks(target instanceof Player));}});
		}
		var nearest=level.getEntitiesOfClass(LivingEntity.class,new AABB(from,from).inflate(16),t->t!=owner&&t.isAlive()&&!t.isSpectator())
				.stream().min(java.util.Comparator.comparingDouble(owner::distanceToSqr)).orElse(null);
		if(nearest!=null){
			var direction=nearest.position().subtract(owner.position()).normalize();
			// Exactly one broad bearing marker; it is never placed at the detected entity.
			for(int i=0;i<12;i++){
				var point=from.add(direction.scale(2)).add((level.getRandom().nextDouble()-.5)*1.2,(level.getRandom().nextDouble()-.5)*.6,(level.getRandom().nextDouble()-.5)*1.2);
				net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(owner,new SpellVisualPayload(spell.id(),point,direction));
			}
		}
		for(int ring=1;ring<=6;ring++)for(int i=0;i<24;i++){
			double angle=i*Math.PI/12,spread=ring*.4;
			var right=aim.direction().cross(new Vec3(0,1,0));if(right.lengthSqr()<.01)right=new Vec3(1,0,0);right=right.normalize();
			var up=right.cross(aim.direction()).normalize();
			var p=from.add(aim.direction().scale(ring*1.7)).add(right.scale(Math.cos(angle)*spread)).add(up.scale(Math.sin(angle)*spread));
			level.sendParticles(SpellParticles.accent(spell),p.x,p.y,p.z,1,.025,.025,.025,.01);
		}
		return true;
	}
}
