package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.MagicControl;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.wind.tornado_impact.TornadoImpactHit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
public final class TornadoImpactHitAdapter {
	public static void apply(SweptSpellProjectile projectile,LivingEntity target,SpellDefinition spell,float damage) {
		var level=(ServerLevel)projectile.level();
		TornadoImpactHit.apply(new TornadoImpactHit.Target() {
			public boolean hurt(){return target.hurtServer(level,MagicDamage.source(projectile,spell),damage);}
			public boolean impulseImmune(){return target.getType().builtInRegistryHolder().is(MagicControl.BIND_IMMUNE);}
			public void impulse(){target.setDeltaMovement(target.getDeltaMovement().add(projectile.getDeltaMovement().normalize()));target.hurtMarked=true;}
			public void knockback(){if(spell.knockback()>0){var motion=projectile.getDeltaMovement();target.knockback(spell.knockback(),-motion.x,-motion.z);}}
			public void effect(){MagicEffects.apply(projectile.getOwner(),target,spell);}
			public void afterHit(){SpellHooks.afterHit(projectile,target);}
		});
	}
	private TornadoImpactHitAdapter() { }
}
