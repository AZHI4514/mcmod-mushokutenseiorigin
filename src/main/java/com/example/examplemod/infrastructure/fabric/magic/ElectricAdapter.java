package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.water.electric.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
public record ElectricAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return execute(player,SpellDefinition.ELECTRIC,new MagicAim(yaw,pitch));}
	public static LivingEntity target(ServerPlayer player,MagicAim aim) {
		var level=(ServerLevel)player.level();var from=player.getEyePosition();var end=from.add(aim.direction().scale(32));
		Vec3 to=from;
		for(int i=0;i<32;i++){var a=from.add(aim.direction().scale(i));var b=from.add(aim.direction().scale(i+1));if(!SpellRaycastAdapter.loaded(level,new AABB(a,b)))break;var wall=level.clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player));to=wall.getLocation();if(wall.getType()==HitResult.Type.BLOCK)break;}
		double nearest=from.distanceTo(to);LivingEntity selected=null;
		for(var e:level.getEntities(player,new AABB(from,to).inflate(.2),e->e instanceof LivingEntity&&MagicEffects.allowed(player,(LivingEntity)e))){
			var hit=e.getBoundingBox().inflate(.1).clip(from,to);if(hit.isPresent()&&ElectricRules.nearer(hit.get().distanceTo(from),nearest)){nearest=hit.get().distanceTo(from);selected=(LivingEntity)e;}
		}return selected;
	}
	public static boolean execute(ServerPlayer owner,SpellDefinition spell,MagicAim aim){
		var level=(ServerLevel)owner.level();var source=new MagicAreaEntity(MagicSystem.MAGIC_AREA,level);source.configure(owner,spell,aim);
		var target=target(owner,aim);if(target==null)return false;
		ElectricImpact.apply(new ElectricImpact.Target(){
			public boolean hurt(){return target.hurtServer(level,MagicDamage.thunder(source,owner),spell.damage());}
			public boolean immune(){return MagicControl.immune(target);}
			public void slow(){target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,60,1),owner);}
			public void control(){MagicControl.apply(target,ElectricRules.controlTicks(target instanceof Player));}
			public void afterHit(){SpellHooks.afterHit(source,target);}
			public void burst(){SpellVisuals.burst(level,spell,target.getBoundingBox().getCenter(),aim.direction());}
		});return true;
	}
}
