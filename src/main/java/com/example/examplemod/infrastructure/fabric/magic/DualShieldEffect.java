package com.example.examplemod.infrastructure.fabric.magic;
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

final class DualShieldEffect implements FiniteBarrierEffect {
	public double radius(){return 3;}public boolean plane(){return true;}public float capacity(){return 160;}
	public Vec3 target(ServerPlayer owner,SpellDefinition spell,MagicAim aim){return owner.getEyePosition().add(aim.direction().scale(1.5));}
	public boolean acceptsDamage(int mode,boolean magic){return DualShieldRules.intercepts(mode,magic);}public boolean blocksDamage(){return true;}
	public boolean acceptsProjectile(int mode,boolean magic){return DualShieldRules.intercepts(mode,magic);}public boolean holdsBody(Entity entity){return false;}
	public boolean absorb(BarrierCapacity capacity,boolean magic,float amount){return magic?capacity.absorbMagical(amount):capacity.absorbPhysical(amount);}
}
