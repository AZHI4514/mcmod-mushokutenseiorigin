package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.magic.*;

import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Local smoke/fog volumes; a sand projectile becomes one persistent smoke field on contact. */
public final class SmokeFieldRuntimeAdapter {
	private static final class State {long expiry,next; boolean flight; Vec3 velocity=Vec3.ZERO;}
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,State>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("smoke_field_runtime_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,State>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,State> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static boolean handles(SpellDefinition s){return s!=null && s.execution()==SpellDefinition.Execution.SMOKE_FIELD;}
	public static double radius(SpellDefinition s){return handles(s)?com.example.examplemod.bootstrap.SmokeFieldComposition.policy(s.id()).radius():14;}
	public static double height(SpellDefinition s){return handles(s)?com.example.examplemod.bootstrap.SmokeFieldComposition.policy(s.id()).height():8;}
	public static boolean flight(MagicAreaEntity area){var s=states(area).get(area);return s!=null&&s.flight;}
	public static boolean canCast(ServerPlayer owner,SpellDefinition s,MagicAim aim){return aim.valid()&&(com.example.examplemod.bootstrap.SmokeFieldComposition.policy(s.id()).launched()||WindFieldRuntimeAdapter.target(owner,s,aim)!=null);}
	public static boolean cast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){
		var at=com.example.examplemod.bootstrap.SmokeFieldComposition.policy(spell.id()).launched()?owner.getEyePosition():WindFieldRuntimeAdapter.target(owner,spell,aim);if(at==null)return false;
		var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());area.configure(owner,spell,aim);area.setPos(at);
		var s=new State();s.flight=com.example.examplemod.bootstrap.SmokeFieldComposition.policy(spell.id()).launched();s.velocity=aim.direction().scale(1.5);s.expiry=s.flight?Long.MAX_VALUE:owner.level().getGameTime()+spell.lifetimeTicks();
		if(!AreaRuntimeAdapter.replaceExpansion(owner,area))return false;states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,owner.level().getGameTime());tick(area);return true;
	}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;var s=states(area).get(area);long now=level.getGameTime();
		com.example.examplemod.magic.mechanic.field.SmokeFieldTick.tick(new com.example.examplemod.magic.mechanic.field.SmokeFieldTick.World(){
			public boolean statePresent(){return s!=null;}public boolean expired(){return now>=s.expiry;}
			public boolean ownerPresent(){return area.getOwner() instanceof ServerPlayer;}private ServerPlayer owner(){return (ServerPlayer)area.getOwner();}
			public boolean alive(){return owner().isAlive();}public boolean sameLevel(){return owner().level()==level;}public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner(),area);}
			public void removeState(){states(area).remove(area);}public void discard(){area.discard();}
			public boolean flying(){return s.flight;}public void projectile(){var step=SandCloudFlightAdapter.advance(area,s.velocity,s.expiry,s.next,level);s.flight=step.flying();s.velocity=step.velocity();s.expiry=step.expiry();s.next=step.next();}
			public boolean pulseDue(){return now>=s.next;}public void schedule(){s.next=now+20;}
			public void contacts(boolean pulse){SmokeFieldRuntimeAdapter.contacts(level,area,owner(),pulse);}
			public void visual(){SmokeFieldRuntimeAdapter.visual(level,area,now);}
		});
	}
	private static void contacts(ServerLevel level,MagicAreaEntity area,ServerPlayer owner,boolean pulse){
		var spell=area.spell();var policy=com.example.examplemod.bootstrap.SmokeFieldComposition.policy(spell.id());boolean mud=policy.requiresSky();
		var center=area.position();
		for(var t:level.getEntitiesOfClass(LivingEntity.class,area.areaBounds(),t->t.isAlive()&&!t.isSpectator())){
			com.example.examplemod.magic.mechanic.field.SmokeContact.apply(new com.example.examplemod.magic.mechanic.field.SmokeContact.Target(){
				Vec3 offset;
				public boolean inside(){offset=t.getEyePosition().subtract(center);return !(offset.y<0||offset.y>height(spell)||offset.horizontalDistanceSqr()>radius(spell)*radius(spell));}
				public boolean exposed(){return !mud||level.canSeeSky(t.blockPosition());}
				public boolean visible(){var source=center.add(0,Math.clamp(offset.y,.15,height(spell)),0);return SpellVisibilityAdapter.visible(level,source,t,area);}
				public void sight(){var sight=policy.sight();if(sight!=null)affectSight(t,sight.range(),sight.linger(),sight.color());}
				public void slowing(){
					var slowing=policy.slowing();if(slowing==null)return;
					if(!MagicControl.immune(t))t.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,slowing.duration(),slowing.amplifier()),owner);
					if(slowing.held())t.addEffect(new MobEffectInstance(MagicEffects.MUD_RAIN_HELD,60,0),owner);
				}
				public void damage(){if(policy.damaging()&&pulse&&(t==owner||MagicEffects.allowed(owner,t))&&MagicFieldCadence.claim(t,"earth",20))t.hurtServer(level,MagicDamage.source(area,spell),2);}
			});
		}
	}
	private static void visual(ServerLevel level,MagicAreaEntity area,long now){
		var spell=area.spell();var policy=com.example.examplemod.bootstrap.SmokeFieldComposition.policy(spell.id());boolean mud=policy.requiresSky();var center=area.position();
		if(now%3==0&&level.players().stream().anyMatch(p->p.distanceToSqr(center)<96*96)){
			var random=level.getRandom();int count=policy.particleCount();
			for(int i=0;i<count;i++){
				double angle=random.nextDouble()*Math.PI*2,r=radius(spell)*Math.sqrt(random.nextDouble());
				var p=center.add(Math.cos(angle)*r,random.nextDouble()*height(spell),Math.sin(angle)*r);
				var pos=BlockPos.containing(p);if(!level.hasChunkAt(pos))continue;
				if(mud){int roof=level.getHeight(Heightmap.Types.MOTION_BLOCKING,pos.getX(),pos.getZ());if(p.y<roof)continue;
					ParticlePacketAdapter.send(level,SpellParticles.trail(spell),p.x,p.y,p.z,1,.18,.1,.18,.06);
				} else {
					if(level.clip(new ClipContext(center.add(0,p.y-center.y,0),p,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,area)).getType()!=HitResult.Type.MISS)continue;
					ParticlePacketAdapter.send(level,i%4==0?SpellParticles.accent(spell):SpellParticles.haze(spell),p.x,p.y,p.z,1,.3,.18,.3,.022);
				}
			}
		}
	}
	private static void affectSight(LivingEntity target,int range,int linger,int color){
		com.example.examplemod.magic.mechanic.field.FogSight.apply(new com.example.examplemod.magic.mechanic.field.FogSight.State(){
			public int remaining(){return target.getAttachedOrElse(MagicAttachments.FOG_LEFT,0);}
			public int range(){return target.getAttachedOrElse(MagicAttachments.FOG_RANGE,256);}
			public void range(int v){target.setAttached(MagicAttachments.FOG_RANGE,v);}
			public void color(int v){target.setAttached(MagicAttachments.FOG_COLOR,v);}
			public void linger(int v){target.setAttached(MagicAttachments.FOG_LINGER,v);}
			public void remaining(int v){target.setAttached(MagicAttachments.FOG_LEFT,v);}
		},range,linger,color);
	}
	public static void tickLiving(LivingEntity target){
		int left=target.getAttachedOrElse(MagicAttachments.FOG_LEFT,0);
		if(left>0)target.setAttached(MagicAttachments.FOG_LEFT,left-1);
	}
	public static boolean mayAcquire(ServerLevel level,LivingEntity attacker,LivingEntity target){
		return com.example.examplemod.magic.mechanic.field.FogSight.mayAcquire(new com.example.examplemod.magic.mechanic.field.FogSight.Target(){
			public boolean attackerPresent(){return attacker!=null;}public boolean targetPresent(){return target!=null;}
			public boolean retainedTarget(){return attacker instanceof Mob mob&&mob.getTarget()==target;}
			public int remaining(){return attacker.getAttachedOrElse(MagicAttachments.FOG_LEFT,0);}
			public int range(){return attacker.getAttachedOrElse(MagicAttachments.FOG_RANGE,256);}
			public double squaredDistance(){return attacker.distanceToSqr(target);}
		});
	}
	public static void shorten(MagicAreaEntity area,int ticks){var s=states(area).get(area);if(s!=null&&!s.flight)s.expiry=Math.max(area.level().getGameTime(),s.expiry-ticks);}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area).get(area);if(s==null)return;out.putLong("SmokeStart",area.getAttachedOrCreate(MagicAttachments.SPIKES_STARTED));out.putLong("SmokeExpiry",s.expiry);out.putLong("SmokeNext",s.next);out.putBoolean("SandFlight",s.flight);out.store("SandVelocity",Vec3.CODEC,s.velocity);}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new State();s.expiry=in.getLongOr("SmokeExpiry",0);s.next=in.getLongOr("SmokeNext",0);s.flight=in.getBooleanOr("SandFlight",false);s.velocity=in.read("SandVelocity",Vec3.CODEC).orElse(Vec3.ZERO);states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,in.getLongOr("SmokeStart",area.level().getGameTime()));}
	private SmokeFieldRuntimeAdapter(){}
}
