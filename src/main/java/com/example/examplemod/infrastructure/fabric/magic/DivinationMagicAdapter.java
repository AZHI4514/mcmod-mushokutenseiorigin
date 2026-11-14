package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.*;
import com.example.examplemod.infrastructure.fabric.magic.PreparationRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.special.divination_magic.DivinationRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record DivinationMagicAdapter(ServerPlayer player) implements SelfEffect {
	private static ServerPlayer subject(ServerPlayer owner,MagicAim aim){
		boolean crystal=owner.getMainHandItem().is(com.example.examplemod.item.ModItems.MANA_CRYSTAL)
				||owner.getMainHandItem().is(com.example.examplemod.item.ModItems.CHARGED_MANA_CRYSTAL)
				||owner.getOffhandItem().is(com.example.examplemod.item.ModItems.MANA_CRYSTAL)
				||owner.getOffhandItem().is(com.example.examplemod.item.ModItems.CHARGED_MANA_CRYSTAL);
		if(!crystal)return null;
		if(owner.isShiftKeyDown())return owner;
		var target=SupportRayAdapter.trace(owner,aim,8,t->t instanceof ServerPlayer&&SupportConsentAdapter.consented(owner,t));
		return target instanceof ServerPlayer p?p:null;
	}
	public static boolean canCast(ServerPlayer owner,MagicAim aim){return aim.valid()&&subject(owner,aim)!=null;}
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	public static boolean cast(ServerPlayer owner,MagicAim aim){var level=(ServerLevel)owner.level();var spell=SpellDefinition.DIVINATION_MAGIC;
		var target=subject(owner,aim);if(target==null)return false;
		int projectiles=0;
		double earliest=Double.POSITIVE_INFINITY;
		for(var entity:level.getEntities(target,target.getBoundingBox().inflate(48),e->e instanceof Projectile)){
			var projectile=(Projectile)entity;
			if(projectile.getOwner()==target)continue;
			var velocity=entity.getDeltaMovement();
			int hit=DivinationRules.firstIntersection(ticks->{var at=entity.position().add(velocity.scale(ticks));var predicted=target.getBoundingBox().move(target.getDeltaMovement().scale(ticks)).inflate(.6);return predicted.contains(at);});
			if(hit!=0){projectiles++;earliest=Math.min(earliest,hit/20.0);}
		}
		boolean fall=target.getDeltaMovement().y<-.2 && !target.onGround() && !target.hasEffect(net.minecraft.world.effect.MobEffects.SLOW_FALLING)
				&& target.fallDistance>3;
		boolean casting=level.players().stream().anyMatch(p->p!=target && p.distanceToSqr(target)<=64*64 && MagicEffects.allowed(p,target)&&PreparationRuntimeAdapter.active(p));
		String projectileMessage=projectiles==0?"magic.mushokutenseiorigin.forecast_unknown":"magic.mushokutenseiorigin.forecast_projectile";
		double time=Double.isFinite(earliest)?DivinationRules.reportedTime(earliest,level.getRandom().nextDouble()):0;
		owner.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.forecast_header",target.getDisplayName()));
		owner.sendSystemMessage(Component.translatable(projectileMessage,Math.round(time)));
		owner.sendSystemMessage(Component.translatable(fall?"magic.mushokutenseiorigin.forecast_fall":"magic.mushokutenseiorigin.forecast_unknown"));
		owner.sendSystemMessage(Component.translatable(casting?"magic.mushokutenseiorigin.forecast_cast":"magic.mushokutenseiorigin.forecast_unknown"));
		level.sendParticles(SpellParticles.accent(spell),target.getX(),target.getEyeY(),target.getZ(),36,.5,.25,.5,.03);
		return true;
	}
}
