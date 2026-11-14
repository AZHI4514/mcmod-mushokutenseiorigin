package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.magic.*;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Persistent fire walls, delayed bombardment, and finite ground shock waves. */
public final class GroundFieldRuntimeAdapter {
	private static final class State {
		long start, expiry;
		int pulse = -1;
		Vec3 origin = Vec3.ZERO, direction = Vec3.ZERO;
		final Map<String,Integer> contacts = new HashMap<>();
	}
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,State>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("ground_field_runtime_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,State>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,State> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static boolean handles(SpellDefinition spell) { return spell != null && spell.execution() == SpellDefinition.Execution.EXPANSION_FIELD; }
	public static double radius(SpellDefinition spell) {
		return handles(spell)?com.example.examplemod.bootstrap.GroundFieldComposition.policy(spell.id()).radius():20;
	}
	public static double height(SpellDefinition spell) {
		return handles(spell)?com.example.examplemod.bootstrap.GroundFieldComposition.policy(spell.id()).height():12;
	}
	public static Vec3 target(ServerPlayer owner,SpellDefinition spell,MagicAim aim) {
		if (!aim.valid()) return null;
		if (com.example.examplemod.bootstrap.GroundFieldComposition.policy(spell.id()).wall()) return owner.position().add(horizontal(aim).scale(3));
		var at=WindFieldRuntimeAdapter.target(owner,spell,aim);
		if (at == null || !owner.level().hasChunkAt(BlockPos.containing(at))) return null;
		if (!spell.element().equals("earth")) return at;
		var level=(ServerLevel)owner.level();
		var from=at.add(0,2,0);var to=at.add(0,-5,0);
		if(!SpellRaycastAdapter.loaded(level,new AABB(from,to)))return null;
		var hit=level.clip(new ClipContext(from,to,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));
		return hit.getType()==HitResult.Type.BLOCK && hit.getDirection()==Direction.UP ? hit.getLocation() : null;
	}
	private static Vec3 horizontal(MagicAim aim) {
		var v=new Vec3(aim.direction().x,0,aim.direction().z);
		return v.lengthSqr()<.00001 ? Vec3.directionFromRotation(0,aim.yaw()) : v.normalize();
	}
	public static boolean cast(ServerPlayer owner,SpellDefinition spell,MagicAim aim) {
		var at=target(owner,spell,aim);if(at==null)return false;
		var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());area.configure(owner,spell,aim);area.setPos(at);
		var s=new State();s.start=owner.level().getGameTime();s.expiry=s.start+spell.lifetimeTicks();s.origin=at;s.direction=horizontal(aim);
		if(!AreaRuntimeAdapter.replaceExpansion(owner,area))return false;
		states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start);
		tick(area);return true;
	}
	public static boolean wallContains(Vec3 offset,Vec3 forward) {
		return com.example.examplemod.magic.spell.fire.flame_storm.FlameStormGeometry.wall(offset.x,offset.y,offset.z,forward.x,forward.y,forward.z);
	}
	public static int damagePulse(String id,long age) {
		var policy=com.example.examplemod.bootstrap.GroundFieldComposition.find(id);return policy==null?-1:policy.damagePulse(age);
	}
	private static boolean unobstructedWall(ServerLevel level,MagicAreaEntity area,State s,Vec3 point) {
		var d=point.subtract(s.origin);double travel=Math.max(0,d.dot(s.direction));
		var from=point.subtract(s.direction.scale(travel));
		if(!SpellRaycastAdapter.loaded(level,new AABB(from,point)))return false;
		return level.clip(new ClipContext(from,point,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,area)).getType()==HitResult.Type.MISS;
	}
	public static void tick(MagicAreaEntity area) {
		if(!(area.level() instanceof ServerLevel level))return;
		var s=states(area).get(area);long now=level.getGameTime();
		com.example.examplemod.magic.mechanic.field.GroundFieldTick.tick(new com.example.examplemod.magic.mechanic.field.GroundFieldTick.World(){
			public boolean statePresent(){return s!=null;}public boolean expired(){return now>=s.expiry;}
			public boolean ownerPresent(){return area.getOwner() instanceof ServerPlayer;}
			private ServerPlayer owner(){return (ServerPlayer)area.getOwner();}
			public boolean alive(){return owner().isAlive();}public boolean sameLevel(){return owner().level()==level;}public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner(),area);}
			public void removeState(){states(area).remove(area);}public void discard(){area.discard();}
			private long age(){return now-s.start;}
			public void move(){if(com.example.examplemod.bootstrap.GroundFieldComposition.policy(area.spell().id()).wall())area.setPos(s.origin.add(s.direction.scale(com.example.examplemod.magic.spell.fire.flame_storm.FlameStormGeometry.travel(age()))));}
			public int pulse(){return damagePulse(area.spell().id(),age());}public int lastPulse(){return s.pulse;}
			public void commit(int pulse){s.pulse=pulse;}public void terrain(int pulse){com.example.examplemod.bootstrap.GroundTerrainComposition.effect(area.spell()).apply(owner(),area,pulse);}
			public boolean continuousContacts(){return com.example.examplemod.bootstrap.GroundFieldComposition.policy(area.spell().id()).continuousContacts();}
			public void contacts(int pulse,boolean attempt){GroundFieldRuntimeAdapter.contacts(level,area,s,owner(),now,pulse,attempt);}
			public boolean visualDue(){return age()%2==0;}public void visual(){GroundFieldRuntimeAdapter.visual(level,area,s,age());}
		});
	}
	private static void contacts(ServerLevel level,MagicAreaEntity area,State s,ServerPlayer owner,long now,int pulse,boolean attempt){
		var spell=area.spell();var policy=com.example.examplemod.bootstrap.GroundFieldComposition.policy(spell.id());var effect=com.example.examplemod.bootstrap.GroundContactComposition.effect(spell);
			for(var target:level.getEntitiesOfClass(LivingEntity.class,area.areaBounds(),t->t.isAlive()&&!t.isSpectator())) {
				com.example.examplemod.magic.mechanic.field.GroundFieldContact.apply(new com.example.examplemod.magic.mechanic.field.GroundFieldContact.Target(){
					Vec3 point,offset;
					public boolean eligible(){return target==owner||MagicEffects.allowed(owner,target);}
					public boolean inside(){
						point=target.getBoundingBox().getCenter();offset=point.subtract(area.position());
						if(policy.wall())return wallContains(offset,s.direction)&&unobstructedWall(level,area,s,point);
						return !(offset.horizontalDistanceSqr()>radius(spell)*radius(spell)||offset.y<-.1||offset.y>height(spell));
					}
					public boolean surface(){return !spell.element().equals("earth")||nearSurface(level,target);}
					public boolean visible(){var view=policy.wall()?point.subtract(s.direction.scale(.2)):area.position().add(0,Math.clamp(offset.y,.25,height(spell)),0);return SpellVisibilityAdapter.visible(level,view,target,area);}
					public void continuous(){effect.continuous(owner,target);}
					public boolean attackDue(){return attempt||policy.perTickAttack();}
					public boolean claim(){
						String key=target.getUUID().toString();Integer previous=s.contacts.get(key);
						if(!policy.mayClaim(previous,pulse))return false;
						if(policy.cadenceRequired(pulse)&&!MagicFieldCadence.claim(target,"fire",20))return false;
						s.contacts.put(key,policy.claimedValue(previous,pulse));return true;
					}
					public boolean hurt(){return target.hurtServer(level,MagicDamage.source(area,spell),policy.damage(pulse,spell.damage()));}
					public void afterDamage(){effect.afterDamage(area,owner,target,s.expiry,now);}
					public void hook(){SpellHooks.afterHit(area,target);}
				});
			}
	}
	private static boolean nearSurface(ServerLevel level,LivingEntity target) {
		var feet=target.position();var bottom=BlockPos.containing(feet);
		for(int i=0;i<=2;i++){var p=bottom.below(i);if(level.hasChunkAt(p) && level.getBlockState(p).isFaceSturdy(level,p,Direction.UP))return true;}
		return false;
	}
	private static void visual(ServerLevel level,MagicAreaEntity area,State s,long age) {
		if(level.players().stream().noneMatch(p->p.distanceToSqr(area.position())<128*128))return;
		var spell=area.spell();boolean fire=spell.element().equals("fire");
		boolean warning=com.example.examplemod.bootstrap.GroundFieldComposition.policy(spell.id()).warning(age);
		int count=warning?36:spell.rank()>=7?80:56;
		var random=level.getRandom();
		for(int i=0;i<count;i++) {
			double a=random.nextDouble()*Math.PI*2, r=radius(spell)*Math.sqrt(random.nextDouble());
			var p=area.position().add(Math.cos(a)*r,warning?.12:random.nextDouble()*height(spell),Math.sin(a)*r);
			if(com.example.examplemod.bootstrap.GroundFieldComposition.policy(spell.id()).wall()) {
				var right=new Vec3(-s.direction.z,0,s.direction.x);
				double lateral=(random.nextDouble()-.5)*16, y=random.nextDouble()*8;
				p=area.position().add(right.scale(lateral)).add(s.direction.scale((random.nextDouble()-.5)*4)).add(0,y,0);
				if(!unobstructedWall(level,area,s,p))continue;
			}
			if(!level.hasChunkAt(BlockPos.containing(p)))continue;
			if(warning){
				ParticlePacketAdapter.send(level,ParticleTypes.ENCHANT,p.x,area.getY()+.16,p.z,1,0,.03,0,0);
			} else if(fire) {
				var type=i%5==0?SpellParticles.haze(spell):i%3==0?SpellParticles.accent(spell):SpellParticles.burst(spell);
				ParticlePacketAdapter.send(level,type,p.x,p.y,p.z,1,.15,.28,.15,.055);
			} else {
				double lift=com.example.examplemod.bootstrap.GroundFieldComposition.policy(spell.id()).visualLift();if(lift!=0)p=p.add(0,lift,0);
				ParticlePacketAdapter.send(level,new BlockParticleOption(ParticleTypes.BLOCK,Blocks.COARSE_DIRT.defaultBlockState()),p.x,p.y,p.z,2,.25,.12,.25,.08);
				if(i%3==0)ParticlePacketAdapter.send(level,SpellParticles.haze(spell),p.x,area.getY()+.1,p.z,1,.35,.1,.35,.025);
			}
		}
	}
	public static void shorten(MagicAreaEntity area,int ticks) {var s=states(area).get(area);if(s!=null)s.expiry=Math.max(area.level().getGameTime(),s.expiry-ticks);}
	public static void save(MagicAreaEntity area,ValueOutput out) {
		var s=states(area).get(area);if(s==null)return;
		out.putLong("FieldStart",s.start);out.putLong("FieldExpiry",s.expiry);out.putInt("FieldPulse",s.pulse);
		out.store("FieldOrigin",Vec3.CODEC,s.origin);out.store("FieldDirection",Vec3.CODEC,s.direction);
		out.store("FieldContacts",com.mojang.serialization.Codec.unboundedMap(com.mojang.serialization.Codec.STRING,com.mojang.serialization.Codec.INT),s.contacts);
	}
	public static void load(MagicAreaEntity area,ValueInput in) {
		var s=new State();s.start=in.getLongOr("FieldStart",0);s.expiry=in.getLongOr("FieldExpiry",0);s.pulse=in.getIntOr("FieldPulse",999);
		s.origin=in.read("FieldOrigin",Vec3.CODEC).orElse(area.position());s.direction=in.read("FieldDirection",Vec3.CODEC).orElse(new Vec3(0,0,1));
		s.contacts.putAll(in.read("FieldContacts",com.mojang.serialization.Codec.unboundedMap(com.mojang.serialization.Codec.STRING,com.mojang.serialization.Codec.INT)).orElse(Map.of()));
		states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start);
	}
	private GroundFieldRuntimeAdapter(){}
}
