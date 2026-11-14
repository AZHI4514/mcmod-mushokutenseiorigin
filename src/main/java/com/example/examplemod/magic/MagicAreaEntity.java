package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SmokeFieldRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** 短期法术实体不跨退出/区块重载复活；剩余时间与盾值只由服务端维护。 */
public final class MagicAreaEntity extends Projectile {
	private static final EntityDataAccessor<String> SPELL = SynchedEntityData.defineId(MagicAreaEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Float> PITCH=SynchedEntityData.defineId(MagicAreaEntity.class,EntityDataSerializers.FLOAT);
	private int remaining;
	private float capacity = 8;
	private Vec3 anchor = Vec3.ZERO;
	private int pulse;
	private final java.util.Map<java.util.UUID, Integer> fireContacts = new java.util.HashMap<>();
	private final java.util.Map<java.util.UUID, Integer> wetContacts = new java.util.HashMap<>();
	private boolean renewable = true;
	public MagicAreaEntity(EntityType<? extends MagicAreaEntity> type, Level level) { super(type, level); }
	@Override protected void defineSynchedData(SynchedEntityData.Builder builder) { builder.define(PITCH,0F);builder.define(SPELL, "water_shield"); }
	public SpellDefinition spell() { return SpellDefinition.find(entityData.get(SPELL)); }
	public void configure(ServerPlayer owner, SpellDefinition spell, MagicAim aim) {
		setOwner(owner); entityData.set(SPELL, spell.id()); anchor = owner.position(); remaining = spell.lifetimeTicks();
		setYRot(aim.yaw());entityData.set(PITCH,aim.pitch());
		Vec3 offset = Vec3.directionFromRotation(0, aim.yaw()).scale(1.5);
		setPos(spell == SpellDefinition.WATER_SHIELD ? owner.getEyePosition().add(aim.direction()) : anchor);
	}
	public void breakMaintenance() { renewable = false; }
	public boolean canRenew(ServerPlayer owner) {
		if(spell()==SpellDefinition.WATER_SHIELD)return renewable&&com.example.examplemod.magic.spell.water.water_shield.WaterShieldRules.canRenew(true,owner.position().distanceToSqr(anchor));
		return renewable && (!stationary() || owner.position().distanceToSqr(anchor) < 1.0E-6);
	}
	private boolean stationary() { return spell() == SpellDefinition.WATER_SHIELD || spell() == SpellDefinition.HEAT_ISLAND; }
	public void renew() { remaining = spell()==SpellDefinition.WATER_SHIELD?com.example.examplemod.magic.spell.water.water_shield.WaterShieldRules.extend(remaining):MagicRules.extend(remaining, spell().lifetimeTicks()); capacity = 8; }
	public void absorb(float damage) {
		if(spell()==SpellDefinition.WATER_SHIELD){
			com.example.examplemod.magic.spell.water.water_shield.WaterShieldAbsorb.apply(damage,new com.example.examplemod.magic.spell.water.water_shield.WaterShieldAbsorb.Shield(){
				public float capacity(){return capacity;}
				public void capacity(float value){capacity=value;}
				public void show(){if(level() instanceof ServerLevel server)SpellVisuals.burst(server,spell(),position(),wallDirection());}
				public void discard(){MagicAreaEntity.this.discard();}
			});return;
		}
		com.example.examplemod.magic.mechanic.projectile.ShieldAbsorption.apply(damage,new com.example.examplemod.magic.mechanic.projectile.ShieldAbsorption.Shield(){
			public float capacity(){return capacity;}
			public void capacity(float value){capacity=value;}
			public void show(){if(level() instanceof ServerLevel server)SpellVisuals.burst(server,spell(),position(),wallDirection());}
			public void discard(){MagicAreaEntity.this.discard();}
		});
	}
	public AABB areaBounds() {
		var behavior=com.example.examplemod.bootstrap.AreaBehaviorComposition.find(spell());
		if(behavior!=null)return behavior.bounds().apply(this);
		if (spell() == SpellDefinition.WATER_SHIELD) {
			return wallVolume().bounds(position());
		}
		if (spell() == SpellDefinition.BURNING_PLACE && getOwner() instanceof LivingEntity owner) return owner.getBoundingBox().inflate(.12);
		double r = spell() == SpellDefinition.HEAT_ISLAND ? 3 : spell() == SpellDefinition.BURNING_PLACE ? spell().range()+0.5 : 2;
		return new AABB(getX()-r,getY()-0.5,getZ()-r,getX()+r,getY()+2.5,getZ()+r);
	}
	public float wallPitch(){return entityData.get(PITCH);}
	public Vec3 wallDirection(){return Vec3.directionFromRotation(wallPitch(),getYRot());}
	public SpellSweep wallVolume(){return new SpellSweep(wallDirection(),3,2,0.3);}
	public boolean touchesWall(AABB box) {
		return wallVolume().intersects(box,position());
	}

	@Override public void tick() {
		super.tick();
		var behavior=com.example.examplemod.bootstrap.AreaBehaviorComposition.find(spell());
		if(behavior!=null){behavior.tick().accept(this);return;}
		if (!(level() instanceof ServerLevel level)) {
			if(tickCount%4==0) {
				boolean warm=spell()==SpellDefinition.HEAT_ISLAND;
				double radius=warm?3:spell()==SpellDefinition.BURNING_PLACE?.45:0.85;
				if(spell()!=SpellDefinition.WATER_SHIELD) for(int i=0;i<2;i++) {
					double angle=tickCount*0.08+i*Math.PI;
					var particle=SpellParticles.trail(spell());
					level().addParticle(particle,getX()+Math.cos(angle)*radius,getY()+(warm?0.08:spell()==SpellDefinition.BURNING_PLACE?.2+(tickCount%20)/12.0:0.2),getZ()+Math.sin(angle)*radius,0,0.015,0);
				}
			}
			return;
		}
		com.example.examplemod.magic.mechanic.field.AreaTick.tick(new com.example.examplemod.infrastructure.fabric.magic.AreaTickAdapter(this,level,new com.example.examplemod.infrastructure.fabric.magic.AreaTickAdapter.State(){
			public int decrementRemaining(){return --remaining;}
			public boolean stationary(){return MagicAreaEntity.this.stationary();}
			public double displacementSquared(){return getOwner().position().distanceToSqr(anchor);}
			public void advancePulse(){++pulse;}
			public int pulse(){return pulse;}
		},fireContacts,wetContacts));
	}
	public void startExpansion() { if (spell() == SpellDefinition.BLIZZARD_STORM) tickBlizzard(); }
	public void tickBlizzard() {
		if (!(level() instanceof ServerLevel level)) { if (getOwner() instanceof LivingEntity owner) setPos(owner.position()); return; }
		com.example.examplemod.infrastructure.fabric.magic.BlizzardStormAdapter.tick(this,level,()->pulse,()->pulse++);
	}
	public void restoreSpellId(String id){entityData.set(SPELL,id);}
	public void saveBlizzard(ValueOutput output){
		output.putInt("ExpansionPulse",pulse);
		output.putLong("ExpansionExpiry",com.example.examplemod.magic.spell.water.blizzard_storm.BlizzardStormRules.expiry(level().getGameTime(),pulse));
	}
	public void loadBlizzard(ValueInput input){
		pulse=com.example.examplemod.magic.spell.water.blizzard_storm.BlizzardStormRules.restore(input.getIntOr("ExpansionPulse",80),input.getLongOr("ExpansionExpiry",0),level().getGameTime());
	}
	@Override protected void addAdditionalSaveData(ValueOutput output){
		super.addAdditionalSaveData(output);
		com.example.examplemod.bootstrap.AreaBehaviorComposition.save(this,output);
	}
	@Override protected void readAdditionalSaveData(ValueInput input){
		super.readAdditionalSaveData(input);
		com.example.examplemod.bootstrap.AreaBehaviorComposition.load(this,input,()->remaining=0);
	}
}
