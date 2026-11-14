package com.example.examplemod.infrastructure.fabric.magic;

import java.util.Set;
import java.util.UUID;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

/** Uses the carrier's live history; direct contacts and all bursts share the same claims. */
public final class ExodusFlameBurstAdapter {
	public static void apply(SweptSpellProjectile projectile,ServerLevel level,Set<UUID> hit) {
		var center=projectile.position().subtract(projectile.getDeltaMovement().normalize().scale(.025));
		for(var e:level.getEntities(projectile,new net.minecraft.world.phys.AABB(center,center).inflate(com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameRules.BURST_RADIUS),e->e instanceof LivingEntity&&e.isAlive()&&!e.isSpectator())) {
			var target=(LivingEntity)e;
			com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameHit.apply(new com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameHit.Target() {
				public boolean previouslyHit(){return hit.contains(target.getUUID());}
				public double distanceSquared(){return target.getBoundingBox().getCenter().distanceToSqr(center);}
				public boolean visible(){return SpellVisibilityAdapter.visible(level,center,target,projectile);}
				public boolean owner(){return target==projectile.getOwner();}
				public boolean allowed(){return MagicEffects.allowed(projectile.getOwner(),target);}
				public void claim(){hit.add(target.getUUID());}
				public boolean hurt(){return target.hurtServer(level,MagicDamage.source(projectile,projectile.spell()),projectile.spell().damage());}
				public void selfEffect(){if(target.getRandom().nextFloat()<.8F&&!target.fireImmune()&&!target.hasEffect(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE))target.igniteForTicks(projectile.spell().statusTicks());}
				public void otherEffect(){MagicEffects.apply(projectile.getOwner(),target,projectile.spell());}
				public void afterHit(){SpellHooks.afterHit(projectile,target);}
			});
		}
	}
	private ExodusFlameBurstAdapter() { }
}
