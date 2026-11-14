package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;

import java.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;
import com.example.examplemod.ExampleMod;

final class DemonGodSealEffect implements PoweredBarrierEffect {
	public int coreCount(){return 5;}public double radius(){return 3;}public boolean hemisphere(){return false;}
	public boolean prepare(ServerPlayer owner,MagicAim aim,MagicAreaEntity area,PoweredBarrierRuntimeAdapter.State s){
		var target=PoweredBarrierRuntimeAdapter.sealTarget(owner,aim);s.cores=PoweredBarrierRuntimeAdapter.sealCores(owner,aim);
		if(target==null||s.cores.size()!=5)return false;s.target=target.getUUID();s.capacity=1200;area.setPos(target.getBoundingBox().getCenter());return true;
	}
	public void committed(ServerLevel level,PoweredBarrierRuntimeAdapter.State s){for(var id:s.cores)MagicCores.take(level.getEntity(id),400);}
	public boolean maintain(ServerLevel level,MagicAreaEntity area,PoweredBarrierRuntimeAdapter.State s,ServerPlayer owner,long now){
		var target=level.getEntity(s.target);if(!(target instanceof LivingEntity living)||!living.isAlive())return false;
		target.setDeltaMovement(Vec3.ZERO);living.addEffect(new net.minecraft.world.effect.MobEffectInstance(MagicEffects.SEALED,5,0),owner);return true;
	}
}
