package com.example.examplemod.magic;

import java.util.HashSet;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/** 中级飞行体统一连续扫掠，穿透每生物每发一次；短命实体不跨卸载复活。 */
public final class IntermediateProjectile extends Projectile {
	private final HashSet<UUID> hit=new HashSet<>();
	private double traveled;
	private int age;
	private int paid=10;
	public IntermediateProjectile(EntityType<? extends IntermediateProjectile> type,Level level) { super(type,level); }
	public SpellDefinition spell() {
		if(getType()==MagicSystem.EX_FLAME) return SpellDefinition.EX_FLAME;
		if(getType()==MagicSystem.SONIC_BOOM) return SpellDefinition.SONIC_BOOM;
		if(getType()==MagicSystem.STONE_CANNON) return SpellDefinition.STONE_CANNON;
		if(getType()==MagicSystem.WATER_SPLASH) return SpellDefinition.WATER_SPLASH;
		if(getType()==MagicSystem.ICICLE_FIELD) return SpellDefinition.ICICLE_FIELD;
		return SpellDefinition.WATER_CANNON;
	}
	public void paidMana(int value) { paid=Math.max(10,value); }
	public float damage() { return spell()==SpellDefinition.STONE_CANNON ? IntermediateRules.stoneDamage(paid) : spell().damage(); }
	@Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }
	@Override public boolean shouldBeSaved() { return false; }
	@Override protected void addAdditionalSaveData(ValueOutput output) { super.addAdditionalSaveData(output); }
	@Override protected void readAdditionalSaveData(ValueInput input) { super.readAdditionalSaveData(input); age=100; }
	@Override public void tick() {
		super.tick(); if(isRemoved()) return;
		if(!(level() instanceof ServerLevel level)) { setPos(position().add(getDeltaMovement())); return; }
		if(++age>spell().lifetimeTicks()||traveled>=spell().range()||getDeltaMovement().lengthSqr()<1.0E-8) { discard(); return; }
		var from=position(); var direction=getDeltaMovement().normalize();
		double distance=Math.min(spell().speed(),spell().range()-traveled);
		var body=spell()==SpellDefinition.EX_FLAME?new SpellSweep(direction,2,2,2)
				:spell()==SpellDefinition.SONIC_BOOM?new SpellSweep(direction,3,1,0.25):new SpellSweep(direction,0.25,0.25,0.25);
		var to=from.add(direction.scale(distance)); var bounds=body.bounds(from).minmax(body.bounds(to)).inflate(1.0E-5);
		for(int x=(int)Math.floor(bounds.minX)>>4;x<=((int)Math.floor(bounds.maxX)>>4);x++)
			for(int z=(int)Math.floor(bounds.minZ)>>4;z<=((int)Math.floor(bounds.maxZ)>>4);z++)
				if(!level.hasChunkAt(new BlockPos(x<<4,getBlockY(),z<<4))) { discard(); return; }
		double wall=Double.POSITIVE_INFINITY; BlockPos block=null; Vec3 impact=null;
		for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ))) {
			for(var local:level.getBlockState(pos).getCollisionShape(level,pos).toAabbs()) {
				double contact=body.contact(local.move(pos),from,direction,distance);
				if(contact<wall) {
					wall=contact; block=pos.immutable();
					var box=local.move(pos); var center=from.add(direction.scale(contact));
					impact=new Vec3(Math.clamp(center.x,box.minX,box.maxX),Math.clamp(center.y,box.minY,box.maxY),Math.clamp(center.z,box.minZ,box.maxZ));
				}
			}
		}
		var targets=new ArrayList<LivingEntity>();
		for(var entity:level.getEntities(this,bounds,e->e instanceof LivingEntity && MagicEffects.allowed(getOwner(),(LivingEntity)e))) {
			if(!hit.contains(entity.getUUID())&&body.contact(entity.getBoundingBox(),from,direction,distance)<wall) targets.add((LivingEntity)entity);
		}
		targets.sort(Comparator.comparingDouble(t->body.contact(t.getBoundingBox(),from,direction,distance)));
		boolean piercing=spell()==SpellDefinition.SONIC_BOOM||spell()==SpellDefinition.STONE_CANNON;
		for(var target:targets) {
			double contact=body.contact(target.getBoundingBox(),from,direction,distance); if(contact>distance) continue;
			hit.add(target.getUUID());
			setPos(from.add(direction.scale(contact)));
			IntermediateSpells.hurt(this,target,spell(),damage());
			if(!piercing) { setPos(from.add(direction.scale(contact))); IntermediateSpells.impactParticles(this); discard(); return; }
		}
		if(wall<=distance&&block!=null) {
			setPos(from.add(direction.scale(wall)));
			if(getOwner() instanceof ServerPlayer owner&&(spell()==SpellDefinition.WATER_CANNON||spell()==SpellDefinition.EX_FLAME))
				IntermediateSpells.blockImpact(this,owner,block,impact);
			IntermediateSpells.impactParticles(this); discard(); return;
		}
		setPos(to); traveled+=distance; if(traveled>=spell().range()) discard();
	}
}
