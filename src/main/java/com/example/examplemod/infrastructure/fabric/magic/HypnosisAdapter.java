package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.*;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.special.hypnosis_magic.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record HypnosisAdapter(ServerPlayer player) implements SelfEffect {
	public static void initialize(){
		ServerLivingEntityEvents.AFTER_DAMAGE.register((target,source,base,taken,blocked)->{
			if(taken>0 && target.hasEffect(MagicEffects.HYPNOTIZED))wake(target);
		});
	}
	private static LivingEntity sleepTarget(ServerPlayer owner,MagicAim aim){
		return SupportRayAdapter.trace(owner,aim,16,t->HypnosisRules.eligible(new HypnosisRules.Target(){public boolean immune(){return MagicControl.immune(t);}public boolean allowed(){return MagicEffects.allowed(owner,t);}public boolean hypnotized(){return t.hasEffect(MagicEffects.HYPNOTIZED);}public boolean ready(){return owner.level().getGameTime()>=t.getAttachedOrElse(MagicAttachments.HYPNOSIS_UNTIL,0L);}}));
	}
	public static boolean canCast(ServerPlayer owner,MagicAim aim){return aim.valid()&&sleepTarget(owner,aim)!=null;}
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	public static boolean cast(ServerPlayer owner,MagicAim aim){var level=(ServerLevel)owner.level();var spell=SpellDefinition.HYPNOSIS_MAGIC;
			var target=sleepTarget(owner,aim);if(target==null)return false;
			int duration=HypnosisRules.duration(target instanceof Player);
			return HypnosisEffect.apply(new HypnosisEffect.Target(){
				public boolean addEffect(){return target.addEffect(new MobEffectInstance(MagicEffects.HYPNOTIZED,duration,0),owner);}
				public void setImmunity(){target.setAttached(MagicAttachments.HYPNOSIS_UNTIL,level.getGameTime()+duration+200);}
				public void markActive(){target.setAttached(MagicAttachments.HYPNOSIS_ACTIVE,true);}
				public void visual(){
			for(int i=0;i<30;i++){
				double angle=i*.7;
				level.sendParticles(SpellParticles.accent(spell),target.getX()+Math.cos(angle)*.6,target.getEyeY()+i*.008,target.getZ()+Math.sin(angle)*.6,1,.03,.03,.03,.018);
			}
				}
			});
	}
	public static void tickLiving(LivingEntity target){
		if(target.getAttachedOrElse(MagicAttachments.HYPNOSIS_ACTIVE,false)&&!target.hasEffect(MagicEffects.HYPNOTIZED))wake(target);
	}
	private static void wake(LivingEntity target){
		target.removeEffect(MagicEffects.HYPNOTIZED);
		target.setAttached(MagicAttachments.HYPNOSIS_ACTIVE,false);
		target.setAttached(MagicAttachments.HYPNOSIS_UNTIL,target.level().getGameTime()+200);
	}
}
