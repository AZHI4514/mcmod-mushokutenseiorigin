package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
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

final class SpiritSealingBarrierEffect implements FiniteBarrierEffect {
	public double radius(){return 8;}public boolean plane(){return false;}public float capacity(){return 100;}
	public Vec3 target(ServerPlayer owner,SpellDefinition spell,MagicAim aim){return WindFieldRuntimeAdapter.target(owner,spell,aim);}
	public boolean acceptsDamage(int mode,boolean magic){return true;}public boolean blocksDamage(){return false;}
	public boolean acceptsProjectile(int mode,boolean magic){return false;}public boolean holdsBody(Entity entity){return entity instanceof LivingEntity&&entity.getType().builtInRegistryHolder().is(HolyInstantAdapter.TARGETS);}
	public boolean absorb(BarrierCapacity capacity,boolean magic,float amount){return capacity.absorbShell(amount);}
	public void update(ServerLevel level,MagicAreaEntity area,FiniteBarrierRuntimeAdapter.State s){
		// Enclosed undead can spend the shell capacity by striking it, at most once per second each.
		if(level.getGameTime()%20==0)
			for(var t:level.getEntitiesOfClass(LivingEntity.class,FiniteBarrierRuntimeAdapter.bounds(area).inflate(.7),e->e.getType().builtInRegistryHolder().is(HolyInstantAdapter.TARGETS))){
				double distance=t.getBoundingBox().getCenter().distanceTo(area.position());
				if(SpiritSealingBarrierRules.strikesShell(distance) && t instanceof net.minecraft.world.entity.Mob mob && mob.getTarget()!=null){
					if(s.capacity.absorbShell(SpiritSealingBarrierRules.strikeCost(t.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)))){area.discard();break;}
				}
			}
	}
}
