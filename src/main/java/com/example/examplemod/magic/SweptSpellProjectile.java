package com.example.examplemod.magic;

import java.util.HashSet;
import java.util.UUID;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/** 中级飞行体统一连续扫掠，穿透每生物每发一次；卸载保存命中集合、注魔和已飞行时间。 */
public final class SweptSpellProjectile extends Projectile {
	private final HashSet<UUID> hit=new HashSet<>();
	private double traveled;
	private int age;
	private int expansionBroken;
	private int paid=10;
	public SweptSpellProjectile(EntityType<? extends SweptSpellProjectile> type,Level level) { super(type,level); }
	public SpellDefinition spell(){return com.example.examplemod.bootstrap.MagicEntityTypes.sweptSpell(getType());}
	public void paidMana(int value) { paid=com.example.examplemod.magic.spell.earth.stone_cannon.StoneCannonRules.paidMana(value); }
	public float damage() { return spell()==SpellDefinition.STONE_CANNON ? com.example.examplemod.magic.mechanic.channel.ChargeCurve.stoneDamage(paid) : spell().damage(); }
	@Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }
	@Override public boolean shouldBeSaved() { return true; }
	@Override protected void addAdditionalSaveData(ValueOutput output) { super.addAdditionalSaveData(output); output.putInt("MagicAge",age);output.putInt("ExpansionBroken",expansionBroken);output.putInt("PaidMana",paid);MagicFlight.saveHits(output,hit); }
	@Override protected void readAdditionalSaveData(ValueInput input) { super.readAdditionalSaveData(input); age=com.example.examplemod.magic.mechanic.projectile.SavedProjectileCounters.age(input.getIntOr("MagicAge",0));expansionBroken=com.example.examplemod.magic.mechanic.projectile.SavedProjectileCounters.brokenBlocks(input.getIntOr("ExpansionBroken",0));paid=com.example.examplemod.magic.spell.earth.stone_cannon.StoneCannonRules.paidMana(input.getIntOr("PaidMana",10));MagicFlight.loadHits(input,hit); }
	@Override public void tick() {
		super.tick(); if(isRemoved()) return;
		if(!(level() instanceof ServerLevel level)) {
			var motion=getDeltaMovement(); var point=position().subtract(motion.scale(0.35));
			var trail=SpellParticles.trail(spell());
			level().addParticle(trail,point.x,point.y,point.z,-motion.x*0.025,-motion.y*0.025,-motion.z*0.025);
			setPos(position().add(motion)); return;
		}
		com.example.examplemod.infrastructure.fabric.magic.SweptProjectileStepAdapter.prepareAndApply(this,level,age,hit,()->++age,()->expansionBroken,()->expansionBroken++,value->traveled+=value);
	}
}
