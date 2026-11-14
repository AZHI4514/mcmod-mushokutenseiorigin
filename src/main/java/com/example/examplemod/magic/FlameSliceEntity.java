package com.example.examplemod.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import java.util.ArrayList;

/** 3×1×1火断体沿准星方向扫掠；仅完整截面遮挡时停，穿透生物，每发目标身份去重。 */
public final class FlameSliceEntity extends Projectile {
	private FlameSliceFlight flight = new FlameSliceFlight(0);
	private int age;
	private com.example.examplemod.magic.mechanic.projectile.ImpactHistory hit=new com.example.examplemod.magic.mechanic.projectile.ImpactHistory();

	public FlameSliceEntity(EntityType<? extends FlameSliceEntity> type, Level level) { super(type, level); }
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) { }

	@Override
	public void tick() {
		super.tick();
		if (isRemoved()) return;
		if (level().isClientSide()) { setPos(position().add(getDeltaMovement())); return; }
		if (com.example.examplemod.magic.spell.fire.flame_slice.FlameSliceRules.expired(age,flight.core(),getDeltaMovement().lengthSqr())) {
			discard(); return;
		}
		var level = (ServerLevel) level();
		var from = position();
		var direction = getDeltaMovement().normalize();
		double distance = Math.min(getDeltaMovement().length(), flight.remaining());
		var to = from.add(direction.scale(distance));
		var volume = new FlameSliceVolume(direction, getYRot());
		var bounds = volume.bounds(from).minmax(volume.bounds(to)).inflate(1.0E-5);
		for (int x = (int) Math.floor(bounds.minX) >> 4; x <= (int) Math.floor(bounds.maxX) >> 4; x++) {
			for (int z = (int) Math.floor(bounds.minZ) >> 4; z <= (int) Math.floor(bounds.maxZ) >> 4; z++) {
				if (!level.hasChunkAt(new BlockPos(x << 4, getBlockY(), z << 4))) { return; }
			}
		}
		++age;
		if(getY()<level.getMinY()) {discard();return;}
		var obstacles = new ArrayList<AABB>();
		for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ))) {
			if(MagicFlight.passesWaterBlock(this,pos))continue;
			for(var box:level.getBlockState(pos).getCollisionShape(level,pos).toAabbs())obstacles.add(box.move(pos));
		}
		double wall = Math.min(volume.blockingDistance(from, distance, obstacles), MagicFlight.fluidHit(this,new SpellSweep(direction,3,1,1),from,direction.scale(distance)));
		// 不用原版细射线/墙体视线判定，宽体从边缘擦过生物同样命中。
		var targets = level.getEntities(this, bounds, entity -> entity instanceof LivingEntity
				&& entity.isAlive() && !entity.isSpectator() && entity != getOwner());
		targets.sort(java.util.Comparator.comparingDouble(t->volume.contact(t.getBoundingBox(),from,distance)));
		for(var entity:targets) {
			var hitTarget=(LivingEntity)entity;
			double contact=volume.contact(hitTarget.getBoundingBox(),from,distance);
			if(!com.example.examplemod.magic.spell.fire.flame_slice.FlameSliceRules.canTrigger(contact,wall,distance,hit.contains(hitTarget.getUUID())))continue;
			var center=from.add(direction.scale(contact));
			var body=hitTarget.getBoundingBox();
			var impactArea=com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.box(com.example.examplemod.magic.spell.fire.flame_slice.FlameSliceRules.impactArea(hitTarget.getX(),body.minY,hitTarget.getZ(),body.maxY));
			for(var target:level.getEntities(this,impactArea,e->e instanceof LivingEntity living && MagicEffects.allowed(getOwner(),living))) {
				if(!hit.claim(target.getUUID()))continue;
				if(target.hurtServer(level,MagicDamage.source(this,SpellDefinition.FLAME_SLICE),SpellDefinition.FLAME_SLICE.damage())) {
					MagicEffects.apply(getOwner(),target,SpellDefinition.FLAME_SLICE);SpellHooks.afterHit(this,target);
				}
			}
			SpellVisuals.burst(level,SpellDefinition.FLAME_SLICE,center,direction);impactSound();
		}
		if(wall<=distance){SpellVisuals.burst(level,SpellDefinition.FLAME_SLICE,from.add(direction.scale(wall)),direction);discard();return;}
		setPos(to);
		flight = flight.advance(distance);
		level.sendParticles(SpellParticles.trail(SpellDefinition.FLAME_SLICE), getX(), getY(), getZ(), 2, 0.3, 0.1, 0.3, 0);
		if (flight.exhausted()) discard();
	}

	private void impactSound() {
		if (getOwner() instanceof net.minecraft.server.level.ServerPlayer player)
			MagicSounds.listener.accept(player, MagicSounds.Phase.IMPACT);
	}


	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("MagicAge", age);
		MagicFlight.saveHits(output,hit.snapshot());
		output.store("FlameFlight", FlameSliceFlight.CODEC, flight);
	}
	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		age = Math.max(0, input.getIntOr("MagicAge", 0));
		var restoredHits=new java.util.HashSet<java.util.UUID>();
		MagicFlight.loadHits(input,restoredHits);
		hit=new com.example.examplemod.magic.mechanic.projectile.ImpactHistory(restoredHits);
		flight = input.read("FlameFlight", FlameSliceFlight.CODEC)
				.orElse(new FlameSliceFlight(SpellDefinition.FLAME_SLICE.range()));
	}
}
