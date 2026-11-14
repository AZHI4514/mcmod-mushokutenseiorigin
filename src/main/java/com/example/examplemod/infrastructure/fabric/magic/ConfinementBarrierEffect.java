package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.*;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.barrier.*;
import com.example.examplemod.magic.spell.barrier.dual_shield.DualShieldRules;
import com.example.examplemod.magic.spell.barrier.spirit_sealing_barrier.SpiritSealingBarrierRules;
import com.example.examplemod.magic.spell.barrier.confinement_barrier.ConfinementBarrierRules;

import java.util.WeakHashMap;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

final class ConfinementBarrierEffect implements FiniteBarrierEffect {
	public double radius(){return 3;}public boolean plane(){return false;}public float capacity(){return 160;}
	public LivingEntity victim(ServerPlayer owner,MagicAim aim){return SupportRayAdapter.trace(owner,aim,24,t->MagicEffects.allowed(owner,t));}
	public Vec3 target(ServerPlayer owner,SpellDefinition spell,MagicAim aim){
		var target=victim(owner,aim);if(target==null)return null;if(MagicControl.immune(target))return target.position();
		var center=target.getBoundingBox().getCenter();var bounds=new AABB(center,center).inflate(3);
		if(!SpellRaycastAdapter.loaded((ServerLevel)owner.level(),bounds))return null;
		if(!owner.level().getWorldBorder().isWithinBounds(bounds))return null;return center;
	}
	public boolean acceptsDamage(int mode,boolean magic){return true;}public boolean blocksDamage(){return true;}
	public boolean acceptsProjectile(int mode,boolean magic){return true;}public boolean holdsBody(Entity entity){return true;}
	public boolean silences(){return true;}
	public boolean absorb(BarrierCapacity capacity,boolean magic,float amount){return capacity.absorbShell(amount);}
}
