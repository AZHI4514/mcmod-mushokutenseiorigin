package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.summon.spirit_summon.SpiritSummonRules;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.Vec3;
import java.util.*;
/** Shared combat sequence; ground pathing and spirit flight implement distinct movement hooks. */
abstract class CombatSummonBehavior extends SummonBehavior {
	private final SpellDefinition spell;
	private final double reachSquared;
	CombatSummonBehavior(SpellDefinition spell,double reachSquared){this.spell=spell;this.reachSquared=reachSquared;}
	protected final void perform(ServerLevel level,Mob mob,ServerPlayer owner,boolean pulse,int command,LivingEntity target) {
		Vec3 destination=target!=null?target.position():command==1?mob.position():owner.position().add(1,0,1);
		move(mob,destination);
		if(target!=null&&pulse&&attackDue(level.getGameTime())&&mob.distanceToSqr(target)<reachSquared
			&&SpellVisibilityAdapter.visible(level,mob.getEyePosition(),target,mob)){
			target.hurtServer(level,damageSource(mob,owner),4);
			ParticlePacketAdapter.send(level,SpellParticles.burst(spell),target.getX(),target.getY()+.7,target.getZ(),12,.25,.3,.25,.05);
		}
	}
	protected boolean attackDue(long now){return true;}
	protected net.minecraft.world.damagesource.DamageSource damageSource(Mob mob,ServerPlayer owner){return mob.damageSources().mobAttack(mob);}
	protected abstract void move(Mob mob,Vec3 destination);
}
