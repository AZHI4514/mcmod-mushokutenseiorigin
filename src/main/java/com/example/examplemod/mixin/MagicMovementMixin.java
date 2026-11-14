package com.example.examplemod.mixin;
import com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter;

import com.example.examplemod.magic.MagicEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Both sides apply the synced effect; attacks and camera remain available; bound entities cannot translate. */
@Mixin(Entity.class)
public abstract class MagicMovementMixin {
	@ModifyVariable(method="move",at=@At("HEAD"),argsOnly=true)
	private Vec3 mushoku$bound(Vec3 movement) {
		var entity=(Entity)(Object)this;
		if(entity instanceof LivingEntity living&&(living.hasEffect(MagicEffects.WIND_BOUND)||living.hasEffect(MagicEffects.ARC_BOUND)||living.hasEffect(MagicEffects.ICE_BOUND)||living.hasEffect(MagicEffects.SEALED)||living.hasEffect(MagicEffects.HYPNOTIZED))) {living.setDeltaMovement(Vec3.ZERO);living.resetFallDistance();return com.example.examplemod.magic.MagicControl.boundMovement(movement);}
		if(entity instanceof LivingEntity living&&living.hasEffect(MagicEffects.MUD_HELD)&&movement.y>0){double y=com.example.examplemod.magic.Quagmire.jumpVelocity(movement.y);var velocity=living.getDeltaMovement();living.setDeltaMovement(velocity.x,com.example.examplemod.magic.Quagmire.jumpVelocity(velocity.y),velocity.z);movement=new Vec3(movement.x,y,movement.z);}
		if(entity instanceof LivingEntity living && living.hasEffect(MagicEffects.MUD_RAIN_HELD) && movement.y>0) {
			var v=living.getDeltaMovement();living.setDeltaMovement(v.x,Math.min(v.y,.21),v.z);movement=new Vec3(movement.x,Math.min(movement.y,.21),movement.z);
		}
		if(entity instanceof LivingEntity living && living.hasEffect(MagicEffects.HEAVY_GRAVITY) && !com.example.examplemod.magic.MagicControl.immune(living) && movement.y>0){var v=living.getDeltaMovement();living.setDeltaMovement(v.x,Math.min(.08,v.y),v.z);movement=new Vec3(movement.x,Math.min(.08,movement.y),movement.z);}
		return com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter.clipMovement(entity,com.example.examplemod.infrastructure.fabric.magic.FiniteBarrierRuntimeAdapter.clipMovement(entity,com.example.examplemod.magic.PlayerSpellRuntime.clipShieldMovement(entity,movement)));
	}
}
