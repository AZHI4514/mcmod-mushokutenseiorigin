package com.example.examplemod.magic;

import java.util.*;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.*;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.*;

/** Per-player channel sessions, committed burst queues, physical shields and physical shields. */
public final class PhaseTwoRuntime {
	// Negative debt is prepaid fractional mana. Persistent across stop/restart; never refunded as full mana.
	public static final AttachmentType<Integer> CHANNEL_DEBT=AttachmentRegistry.create(ExampleMod.id("channel_debt"),b->b.initializer(()->0).persistent(Codec.intRange(-19,0)).copyOnDeath());
	private static final Map<UUID,Channel> CHANNELS=new HashMap<>();

	private static final Map<UUID,PhaseTwoEntity> SHIELDS=new HashMap<>();
	private static final Map<UUID,Map<UUID,Long>> CONTACTS=new HashMap<>();
	private static final class Channel {
		final SpellDefinition spell; final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
		MagicAim aim; int heartbeat, ticks; net.minecraft.core.BlockPos water;
		Channel(ServerPlayer player,SpellDefinition spell,MagicAim aim) { this.spell=spell;this.aim=aim;dimension=player.level().dimension(); }
	}
	public static void initialize() {
		MagicWaterSources.initialize();MagicSpikes.initialize();
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((target,source,amount)->{

			if(source.getDirectEntity() instanceof Projectile projectile&&MagicAreas.isMagic(projectile)) return true;
			if(!(source.is(DamageTypeTags.IS_PROJECTILE)||source.getDirectEntity() instanceof LivingEntity)) return true;
			var origin=source.getSourcePosition(); if(origin==null) return true;
			for(var shield:SHIELDS.values()) {
				if(shield.isRemoved()||shield.level()!=target.level()) continue;
				var delta=target.getBoundingBox().getCenter().subtract(origin); double distance=delta.length(); if(distance<1.0E-6) continue;

				if(Double.isFinite(shield.volume().contact(new AABB(origin,origin),shield.center(),delta.normalize().scale(-1),distance))) { shield.absorb(amount); return false; }
			}
			return true;
		});
	}
	public static boolean contactReady(UUID area,UUID target,long now) {
		var contacts=CONTACTS.computeIfAbsent(area,id->new HashMap<>());
		contacts.values().removeIf(time->now-time>=20);
		if(contacts.containsKey(target)) return false; contacts.put(target,now); return true;
	}
	private static boolean available(ServerPlayer player,Channel channel) {
		var state=MagicSystem.state(player);
		return player.isAlive()&&!player.isRemoved()&&!player.isSpectator()&&!player.isUsingItem()&&!player.isSleeping()
				&&player.containerMenu==player.inventoryMenu&&channel.dimension.equals(player.level().dimension())
				&&state.selected().equals(channel.spell.id())&&state.loaded().contains(channel.spell.id())&&state.learned().contains(channel.spell.id())
				&&SpellHooks.canUse(player,channel.spell,SpellHooks.CastSource.KEYBOARD);
	}
	public static boolean channelActive(ServerPlayer player) { return CHANNELS.containsKey(player.getUUID()); }
	public static void channel(ServerPlayer player,SpellDefinition spell,MagicAim aim) {
		if(!aim.valid()) return;
		var old=CHANNELS.get(player.getUUID());
		if(old!=null) { if(old.spell==spell) { old.aim=aim; old.heartbeat=0; } return; }
		var state=MagicSystem.state(player);
		if(state.cooldown()!=0||!state.selected().equals(spell.id())||!state.loaded().contains(spell.id())||!state.learned().contains(spell.id())||state.mana()<1||!SpellHooks.canUse(player,spell,SpellHooks.CastSource.KEYBOARD)||!PhaseTwoSpells.canExecute(player,spell,aim)) return;
		var next=new Channel(player,spell,aim);
		int oldDebt=player.getAttachedOrElse(CHANNEL_DEBT,0);
		if(!pay(player,spell)) return;
		if(spell==SpellDefinition.WATERFALL&&!PhaseTwoSpells.instant(player,spell,aim)) {player.setAttached(MagicAttachments.STATE,state);player.setAttached(CHANNEL_DEBT,oldDebt);return;}
		CHANNELS.put(player.getUUID(),next);
		if(spell==SpellDefinition.WATERFALL){var hit=PhaseTwoSpells.trace(player,aim,12);if(hit!=null)next.water=hit.base();}
		MagicGlow.success(player,spell);
		if(spell!=SpellDefinition.WATERFALL) flame(player,next,player.level().getGameTime());
		MagicSounds.listener.accept(player,MagicSounds.Phase.RELEASE);
	}
	public static void waterContact(ServerPlayer owner,net.minecraft.core.BlockPos pos) {var channel=CHANNELS.get(owner.getUUID());if(channel!=null&&channel.spell==SpellDefinition.WATERFALL)channel.water=pos;}

	public static void stop(ServerPlayer player) {
		var channel=CHANNELS.remove(player.getUUID()); if(channel==null) return;
		// Waterfall sources remain as permanent vanilla water.
		player.setAttached(MagicAttachments.RECOVERY_TOTAL,channel.spell.cooldownTicks());
		var state=MagicSystem.state(player); player.setAttached(MagicAttachments.STATE,state.withCooldown(Math.max(state.cooldown(),channel.spell.cooldownTicks())));
	}
	public static boolean shield(ServerPlayer player,MagicAim aim) {
		var previous=SHIELDS.get(player.getUUID()); if(previous!=null&&previous.renew(player)) return true;
		// Position and orientation use the full validated crosshair direction.
		var facing=aim.direction();
		var entity=PhaseTwoSpells.entity(player,SpellDefinition.PHYSICAL_SHIELD,player.getEyePosition().add(facing),facing);
		if(!player.level().addFreshEntity(entity)) return false;
		if(previous!=null) previous.discard(); SHIELDS.put(player.getUUID(),entity); return true;
	}
	public static void breakMaintenance(ServerPlayer player) { var shield=SHIELDS.get(player.getUUID());if(shield!=null) shield.breakMaintenance(); }

	public static boolean tick(ServerPlayer player) {
		long now=player.level().getGameTime(); var id=player.getUUID();
		tickColumns(player); tickBurns(player);
		var shield=SHIELDS.get(id); if(shield!=null&&(shield.isRemoved()||shield.level()!=player.level()||!player.isAlive())) { shield.discard();SHIELDS.remove(id); }

		CONTACTS.values().forEach(map->map.values().removeIf(time->now-time>=20)); CONTACTS.values().removeIf(Map::isEmpty);
		var channel=CHANNELS.get(id); if(channel==null) return false;
		if(!available(player,channel)||++channel.heartbeat>6) { stop(player); return true; }
		if(channel.spell==SpellDefinition.WATERFALL&&PhaseTwoSpells.trace(player,channel.aim,12)==null){stop(player);return true;}
		if(!pay(player,channel.spell)) { stop(player); return true; }
		MagicGlow.success(player,channel.spell);
		if(channel.spell==SpellDefinition.WATERFALL) {
			if(channel.water!=null)MagicWaterSources.renew(player,channel.water);
			if(++channel.ticks%20==0)PhaseTwoSpells.instant(player,channel.spell,channel.aim);
		} else flame(player,channel,now);
		return false;
	}
	private static boolean pay(ServerPlayer player,SpellDefinition spell) {
		int debt=player.getAttachedOrElse(CHANNEL_DEBT,0), rate=spell.manaCost();
		int debit=PhaseTwoRules.debit(debt,rate); var state=MagicSystem.state(player);
		if(state.mana()<debit) return false;
		player.setAttached(MagicAttachments.STATE,state.spendMana(debit));
		player.setAttached(CHANNEL_DEBT,PhaseTwoRules.remainder(debt,rate,debit)); return true;
	}
	private static void flame(ServerPlayer player,Channel channel,long now) {
		var level=(ServerLevel)player.level();var origin=player.getEyePosition();var forward=channel.aim.direction();
		var carrier=PhaseTwoSpells.entity(player,channel.spell,origin,forward);
		for(var entity:level.getEntities(player,player.getBoundingBox().inflate(16),e->e instanceof LivingEntity&&MagicEffects.allowed(player,(LivingEntity)e))) {
			var target=(LivingEntity)entity; var delta=target.getBoundingBox().getCenter().subtract(origin);
			if(PhaseTwoRules.inCone(delta,forward,16)&&IntermediateSpells.visible(level,origin,target,carrier)&&contactReady(player.getUUID(),target.getUUID(),now)) PhaseTwoSpells.damage(carrier,target);
		}
		for(int i=1;i<=16;i+=2) {
			var pos=origin.add(forward.scale(i));
			if(!PhaseTwoSpells.loaded(level,new AABB(origin,pos).inflate(0.01))) break;
			if(level.clip(new net.minecraft.world.level.ClipContext(origin,pos,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,player)).getType()!=HitResult.Type.MISS) break;
			level.sendParticles(net.minecraft.core.particles.ParticleTypes.FLAME,pos.x,pos.y,pos.z,3,i*0.13,i*0.13,i*0.13,0.01);
		}
	}
	public static boolean intercept(Projectile projectile) {
		if(MagicAreas.isMagic(projectile)||projectile.isRemoved()||projectile.getDeltaMovement().lengthSqr()<1.0E-8) return false;
		var motion=projectile.getDeltaMovement(); double length=motion.length(), first=Double.POSITIVE_INFINITY; PhaseTwoEntity selected=null;
		for(var shield:SHIELDS.values()) {
			if(shield.isRemoved()||shield.level()!=projectile.level()||shield.getOwner()==projectile.getOwner()||motion.dot(shield.direction())>=0) continue;
			double contact=shield.volume().contact(projectile.getBoundingBox(),shield.center(),motion.normalize().scale(-1),length);
			if(contact<first) { first=contact;selected=shield; }
		}
		if(selected==null) return false;
		var end=projectile.position().add(motion.normalize().scale(first));
		if(projectile.level().clip(new net.minecraft.world.level.ClipContext(projectile.position(),end,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,projectile)).getType()!=HitResult.Type.MISS) return false;
		// Physical shield has no magic-area dependency; vanilla arrows/tridents use their real incoming damage.
		float damage=MagicProjectileDamage.incoming(projectile,selected);
		selected.absorb(damage);
		if(projectile instanceof net.minecraft.world.entity.projectile.arrow.ThrownTrident) { projectile.setPos(end.subtract(motion.normalize().scale(0.1)));projectile.setDeltaMovement(motion.scale(-0.1)); }
		else projectile.discard(); return true;
	}
	public static void clear(ServerPlayer player) {
		stop(player);var id=player.getUUID(); COLUMNS.remove(id);BURNS.remove(id);
		var shield=SHIELDS.remove(id);if(shield!=null)shield.discard(); CONTACTS.remove(id);
	}
	/** Shield collision uses the oriented volume, never its larger axis-aligned bounds. */
	public static Vec3 clipShieldMovement(Entity entity,Vec3 movement) {
		double distance=movement.length(); if(distance<1.0E-8||entity instanceof Projectile||entity.isSpectator()) return movement;
		double first=distance;
		for(var other:entity.level().getEntities(entity,entity.getBoundingBox().expandTowards(movement).inflate(0.1),e->e instanceof PhaseTwoEntity p&&p.shield())) {
			var shield=(PhaseTwoEntity)other;
			// Let entities already intersecting the surface escape rather than trapping them.
			if(shield.volume().intersects(entity.getBoundingBox(),shield.center())) {
				double side=entity.getBoundingBox().getCenter().subtract(shield.center()).dot(shield.direction());
				var resolved=PhaseTwoRules.shieldOverlapMotion(movement,shield.direction(),side);
				if(resolved.equals(movement))continue;return resolved;
			}
			first=Math.min(first,shield.volume().contact(entity.getBoundingBox(),shield.center(),movement.normalize().scale(-1),distance));
		}
		return first<distance?movement.scale(Math.max(0,first-0.001)/distance):movement;
	}
	private record Column(java.util.List<net.minecraft.core.BlockPos> positions,SpellDefinition spell,boolean permanent,long expiry) { }
	private static final Map<UUID,java.util.List<Column>> COLUMNS=new HashMap<>();
	public static void growingColumn(ServerPlayer owner,java.util.List<net.minecraft.core.BlockPos> positions,SpellDefinition spell,boolean permanent) {
		COLUMNS.computeIfAbsent(owner.getUUID(),k->new ArrayList<>()).add(new Column(new ArrayList<>(positions),spell,permanent,owner.level().getGameTime()+200));
	}
	private static void tickColumns(ServerPlayer owner) {
		var list=COLUMNS.get(owner.getUUID());if(list==null)return;var level=(ServerLevel)owner.level();
		list.removeIf(c->{
			if(level.getGameTime()>=c.expiry)return true;
			var it=c.positions.iterator();while(it.hasNext()) {
				var p=it.next();if(!MagicTerrain.mayChange(owner,p)||!level.getBlockState(p).isAir()){it.remove();continue;}
				if(!level.getEntities(owner,new AABB(p),e->e instanceof LivingEntity).isEmpty()||owner.getBoundingBox().intersects(new AABB(p)))break;
				if(c.permanent)level.setBlockAndUpdate(p,net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState());
				else MagicTerrain.temporary(owner,p,c.spell==SpellDefinition.EARTH_LANCE?MagicTerrain.TEMPORARY_DIRT:MagicTerrain.TEMPORARY_ICE,(int)(c.expiry-level.getGameTime()));it.remove();
			}return c.positions.isEmpty();
		});if(list.isEmpty())COLUMNS.remove(owner.getUUID());
	}
	private record Burn(LivingEntity target,PhaseTwoEntity carrier,long start) { }
	private static final Map<UUID,java.util.List<Burn>> BURNS=new HashMap<>();
	public static boolean igniteTarget(ServerPlayer owner,LivingEntity target,MagicAim aim) {
		var carrier=PhaseTwoSpells.entity(owner,SpellDefinition.FLAME_PILLAR,target.position(),aim.direction());
		target.igniteForTicks(60); PhaseTwoSpells.damage(carrier,target);placeFire(owner,target);
		BURNS.computeIfAbsent(owner.getUUID(),k->new ArrayList<>()).add(new Burn(target,carrier,owner.level().getGameTime()));return true;
	}
	private static void tickBurns(ServerPlayer owner) {
		var list=BURNS.get(owner.getUUID());if(list==null)return;long now=owner.level().getGameTime();
		list.removeIf(b->{long age=now-b.start;if(!b.target.isAlive()||b.target.level()!=owner.level()||age>=60)return true;
			if(age>0&&age%20==0)PhaseTwoSpells.damage(b.carrier,b.target);return false;});if(list.isEmpty())BURNS.remove(owner.getUUID());
	}

	private static void placeFire(ServerPlayer owner,LivingEntity target) {
		var level=(ServerLevel)owner.level();var pos=target.blockPosition();
		if(MagicTerrain.mayChange(owner,pos)&&level.getBlockState(pos).isAir()) {
			var fire=net.minecraft.world.level.block.BaseFireBlock.getState(level,pos);if(fire.canSurvive(level,pos))level.setBlockAndUpdate(pos,fire);
		}
	}

	private PhaseTwoRuntime() { }
}
