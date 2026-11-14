package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.field.ModeSelection;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionRules;
import com.example.examplemod.bootstrap.SpellModeComposition;

import java.util.HashMap;
import java.util.WeakHashMap;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.ExampleMod;

/** Pre-cast mode selections and real per-second mana upkeep. */
public final class MaintainedFieldRuntimeAdapter {
	private static final java.util.Map<SpellDefinition,MaintainedFieldEffect> EFFECTS=java.util.Map.of(
		SpellDefinition.ATMOSPHERE_DOMINION,new AtmosphereDominionFieldEffect(),
		SpellDefinition.GRAVITY_MAGIC,new GravityMagicFieldEffect(),
		SpellDefinition.NUCLEAR_EXPLOSION,new NuclearExplosionFieldEffect());

	static final class State {long start,next,expiry;int mode;boolean exploded;int extra;Vec3 direction=Vec3.ZERO;}
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,State>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("maintained_field_runtime_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,State>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,State> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static boolean handles(SpellDefinition s){return s!=null&&s.execution()==SpellDefinition.Execution.MODE_FIELD;}
	public static boolean selectable(SpellDefinition spell){return SpellModeSelectionAdapter.selectable(spell);}
	public static int mode(ServerPlayer player,SpellDefinition spell){return SpellModeSelectionAdapter.mode(player,spell);}
	public static void cycle(ServerPlayer player,SpellDefinition spell){SpellModeSelectionAdapter.cycle(player,spell);}
	public static int cost(ServerPlayer player,SpellDefinition spell){return SpellModeSelectionAdapter.cost(player,spell);}
	public static boolean canCast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){
		if(!aim.valid()||MagicSystem.state(owner).mana()<cost(owner,spell))return false;
		var effect=EFFECTS.get(spell);
		return effect.ready(owner)&&effect.target(owner,spell,aim)!=null;
	}
	public static boolean cast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){
		var effect=EFFECTS.get(spell);var at=effect.target(owner,spell,aim);if(at==null)return false;
		var state=MagicSystem.state(owner);int extra=cost(owner,spell)-spell.manaCost();if(state.mana()<extra)return false;
		var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());area.configure(owner,spell,aim);area.setPos(at);
		var s=new State();s.start=owner.level().getGameTime();s.next=s.start+20;s.expiry=s.start+spell.lifetimeTicks();s.extra=extra;s.mode=mode(owner,spell);s.direction=aim.direction();
		if(!AreaRuntimeAdapter.replaceExpansion(owner,area))return false;
		int upkeep=effect.spec().upkeep();
		if(state.mana()<extra+upkeep){area.discard();return false;}
		owner.setAttached(MagicAttachments.STATE,state.spendMana(extra+upkeep));
		effect.released(owner,s.start);
		states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start);tick(area);return true;
	}
	public static AABB bounds(MagicAreaEntity area){
		var state=states(area).get(area);int extra=state==null?0:state.extra;
		var spec=EFFECTS.get(area.spell()).spec();double radius=spec.radius(extra);
		return new AABB(area.getX()-radius,area.getY()-spec.below(extra),area.getZ()-radius,area.getX()+radius,area.getY()+spec.above(extra),area.getZ()+radius);
	}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;
		var s=states(area).get(area);long now=level.getGameTime();
		com.example.examplemod.magic.mechanic.field.MaintainedFieldTick.tick(new com.example.examplemod.magic.mechanic.field.MaintainedFieldTick.World(){
			public boolean statePresent(){return s!=null;}public boolean expired(){return now>=s.expiry;}
			public boolean ownerPresent(){return area.getOwner() instanceof ServerPlayer;}private ServerPlayer owner(){return (ServerPlayer)area.getOwner();}
			public boolean alive(){return owner().isAlive();}public boolean sameLevel(){return owner().level()==level;}public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner(),area);}
			public void removeState(){states(area).remove(area);}public void discard(){area.discard();}
			public void follow(){if(EFFECTS.get(area.spell()).spec().followsOwner())area.setPos(owner().position());}
			public boolean pulseDue(){return now>=s.next;}public void schedule(){s.next=now+20;}
			public boolean payUpkeep(){int fee=EFFECTS.get(area.spell()).spec().upkeep();if(fee==0)return true;var mana=MagicSystem.state(owner());if(mana.mana()<fee)return false;owner().setAttached(MagicAttachments.STATE,mana.spendMana(fee));return true;}
			public void update(boolean pulse){EFFECTS.get(area.spell()).update(level,area,s,owner(),now,pulse);}
		});
	}


	static void visual(MagicAreaEntity area,State s,long age,boolean nuclear){
		var level=(ServerLevel)area.level();if(level.players().stream().noneMatch(p->p.distanceToSqr(area.position())<160*160))return;
		var random=level.getRandom();var spec=EFFECTS.get(area.spell()).spec();double r=spec.radius(s.extra);
		for(int i=0;i<(nuclear?90:60);i++){
			double a=random.nextDouble()*Math.PI*2,rad=r*Math.sqrt(random.nextDouble());
			double y=nuclear?(age<100?.1:random.nextDouble()*r):random.nextDouble()*spec.height();
			var point=area.position().add(Math.cos(a)*rad,y,Math.sin(a)*rad);
			if(!level.hasChunkAt(net.minecraft.core.BlockPos.containing(point)))continue;
			var particle=nuclear&&age<100?net.minecraft.core.particles.ParticleTypes.ENCHANT:i%4==0?SpellParticles.accent(area.spell()):SpellParticles.haze(area.spell());
			ParticlePacketAdapter.send(level,particle,point.x,point.y,point.z,1,.2,.2,.2,.035);
		}
	}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area).get(area);if(s==null)return;out.putLong("ModeStart",s.start);out.putLong("ModeNext",s.next);out.putLong("ModeExpiry",s.expiry);out.putInt("ModeChoice",s.mode);out.putInt("NuclearExtra",s.extra);out.putBoolean("ModeExploded",s.exploded);out.store("ModeDirection",Vec3.CODEC,s.direction);}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new State();s.start=in.getLongOr("ModeStart",0);s.next=in.getLongOr("ModeNext",Long.MAX_VALUE);s.expiry=in.getLongOr("ModeExpiry",0);s.mode=Math.clamp(in.getIntOr("ModeChoice",0),0,3);s.extra=NuclearExplosionRules.extra(in.getIntOr("NuclearExtra",s.mode*1000));s.exploded=in.getBooleanOr("ModeExploded",true);s.direction=in.read("ModeDirection",Vec3.CODEC).orElse(Vec3.ZERO);states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start);}
	public static void initialize(){SpellModeSelectionAdapter.initialize();}

	private MaintainedFieldRuntimeAdapter(){}
}
