package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import net.minecraft.core.BlockPos;
import java.util.function.*;

final class WaterfallCarrierEffect implements StationaryCarrierEffect {
	public void apply(DirectionalSpellCarrier carrier,ServerPlayer owner,ServerLevel level,float yaw,int age){
			for(var target:level.getEntities(carrier,carrier.getBoundingBox(),e->e!=owner&&!e.isSpectator())) {
				if(!carrier.volume().intersects(target.getBoundingBox(),carrier.center())) continue;
				if(target instanceof LivingEntity living&&!MagicEffects.allowed(owner,living)) continue;
				target.clearFire(); target.setDeltaMovement(target.getDeltaMovement().multiply(0.8,0.8,0.8)); target.hurtMarked=true;
				if(target instanceof LivingEntity living) living.addEffect(new net.minecraft.world.effect.MobEffectInstance(MagicEffects.WET,carrier.spell().statusTicks(),0),owner);
			}
	}
}
