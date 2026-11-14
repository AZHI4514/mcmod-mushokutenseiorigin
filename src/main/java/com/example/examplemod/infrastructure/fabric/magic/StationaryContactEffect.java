package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import net.minecraft.core.BlockPos;
import java.util.function.*;

record StationaryContactEffect(boolean cadence) implements StationaryCarrierEffect {
	public void apply(DirectionalSpellCarrier carrier,ServerPlayer owner,ServerLevel level,float yaw,int age){
		for(var entity:level.getEntities(carrier,carrier.getBoundingBox(),e->e instanceof LivingEntity)) {
			var living=(LivingEntity)entity;
			if(!carrier.volume().intersects(living.getBoundingBox(),carrier.center())||!SpellVisibilityAdapter.visible(level,carrier.position().add(0,0.1,0),living,carrier)) continue;
			// Fire pillar tracks individual contacts rather than a global one-tick pulse.
			if(cadence) {
				if(!PlayerSpellRuntime.contactReady(level,carrier.getUUID(),living.getUUID(),level.getGameTime())) continue;
			}
			CarrierDamageAdapter.damage(carrier,living);
		}
	}
}
