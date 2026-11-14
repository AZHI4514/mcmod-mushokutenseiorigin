package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.ClipContext;

public final class CarrierDamageAdapter {
	public static void damage(DirectionalSpellCarrier carrier,LivingEntity target) {
		var spell=carrier.spell();var owner=carrier.getOwner();var level=(ServerLevel)carrier.level();
		com.example.examplemod.magic.mechanic.projectile.CarrierHit.apply(new com.example.examplemod.magic.mechanic.projectile.CarrierHit.Target(){
			public boolean allowed(){return MagicEffects.allowed(owner,target);}
			public boolean hurt(){return target.hurtServer(level,MagicDamage.source(carrier,spell),spell.damage());}
			public void effects(){MagicEffects.apply(owner,target,spell);}
			public void afterHit(){SpellHooks.afterHit(carrier,target);}
			public void postHit(){if(spell==SpellDefinition.FLOOD_FLUSH){var v=carrier.direction();target.knockback(com.example.examplemod.magic.spell.water.flood_flush.FloodFlushSpec.KNOCKBACK,-v.x,-v.z);}}
		});
	}
	public static void areaDamage(DirectionalSpellCarrier carrier,Vec3 center,double half) {
		var box=new AABB(center.x-half,center.y-half,center.z-half,center.x+half,center.y+half,center.z+half);
		for(var entity:carrier.level().getEntities(carrier,box,e->e instanceof LivingEntity))
			if(SpellVisibilityAdapter.visible((ServerLevel)carrier.level(),center,(LivingEntity)entity,carrier)) damage(carrier,(LivingEntity)entity);
		((ServerLevel)carrier.level()).sendParticles(SpellParticles.burst(carrier.spell()),center.x,center.y,center.z,1,0,0,0,0);
	}
	private CarrierDamageAdapter() { }
}
