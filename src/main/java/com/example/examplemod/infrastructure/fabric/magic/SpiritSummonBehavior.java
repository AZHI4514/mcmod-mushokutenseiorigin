package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.summon.spirit_summon.SpiritSummonRules;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.Vec3;
import java.util.*;
final class SpiritSummonBehavior extends CombatSummonBehavior {
	SpiritSummonBehavior(){super(SpellDefinition.SPIRIT_SUMMON,256);}
	protected LivingEntity acquireTarget(ServerLevel level,Mob mob,ServerPlayer owner,int command,LivingEntity configured) {
		if(command!=3&&configured==null)return level.getEntitiesOfClass(Mob.class,mob.getBoundingBox().inflate(16),t->t.getTarget()==owner&&MagicEffects.allowed(owner,t)).stream().min(Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
		return configured;
	}
	protected void move(Mob mob,Vec3 destination) {
		var movement=destination.add(0,1,0).subtract(mob.position());if(movement.lengthSqr()>1)mob.setDeltaMovement(movement.normalize().scale(.17));else mob.setDeltaMovement(Vec3.ZERO);
	}
	protected boolean attackDue(long now){return SpiritSummonRules.attacksThisPulse(now);}
	protected net.minecraft.world.damagesource.DamageSource damageSource(Mob mob,ServerPlayer owner){return mob.damageSources().indirectMagic(mob,owner);}
}
