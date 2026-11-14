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

interface FiniteBarrierEffect {
	double radius();boolean plane();float capacity();
	Vec3 target(ServerPlayer owner,SpellDefinition spell,MagicAim aim);
	default LivingEntity victim(ServerPlayer owner,MagicAim aim){return null;}
	boolean acceptsDamage(int mode,boolean magic);boolean blocksDamage();
	boolean acceptsProjectile(int mode,boolean magic);boolean holdsBody(Entity entity);
	default boolean silences(){return false;}
	boolean absorb(BarrierCapacity capacity,boolean magic,float amount);
	default void update(ServerLevel level,MagicAreaEntity area,FiniteBarrierRuntimeAdapter.State state){}
}
