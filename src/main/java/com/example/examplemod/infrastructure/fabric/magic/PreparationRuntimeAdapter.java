package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.*;
import com.example.examplemod.infrastructure.fabric.magic.TeleportationAdapter;
import com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter;

import com.example.examplemod.magic.*;

import net.minecraft.server.level.ServerPlayer;

/** 服务端准备阶段不预扣魔力；完成后重新校验并原子释放。 */
public final class PreparationRuntimeAdapter {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<com.example.examplemod.magic.application.casting.PreparationRegistry<Pending>> RUNTIME =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(
					com.example.examplemod.ExampleMod.id("preparation_runtime"),
					builder -> builder.initializer(()->new com.example.examplemod.magic.application.casting.PreparationRegistry<Pending>()));
	private static com.example.examplemod.magic.application.casting.PreparationRegistry<Pending> runtime(ServerPlayer player) {
		return player.level().getServer().overworld().getAttachedOrCreate(RUNTIME);
	}
	/** Register the runtime attachment during mod initialization. */
	public static void initialize() { }
	/** Online sessions were cancelled first; drop any remaining server-owned references. */
	public static void clear(net.minecraft.server.MinecraftServer server) {
		server.overworld().removeAttached(RUNTIME);
	}
	private static final class Pending {
		final SpellDefinition spell;
		final com.example.examplemod.magic.domain.casting.CastingSession session;
		final com.example.examplemod.magic.application.casting.CastSpellCommand command;
		final com.example.examplemod.magic.application.casting.CastTargetedAreaCommand areaCommand;
		final com.example.examplemod.magic.application.casting.CastLocalEffectCommand localCommand;
		final com.example.examplemod.magic.application.casting.CastSelfEffectCommand selfCommand;
		final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
		MagicAim aim;
		java.util.List<java.util.UUID> cores;
		java.util.UUID sealTarget;
		String teleportToken;
		net.minecraft.world.phys.Vec3 origin;
		net.minecraft.core.BlockPos circle;
		net.minecraft.world.phys.Vec3 locked;
		int remaining, heartbeat;
		Pending(ServerPlayer player, SpellDefinition spell, MagicAim aim) {
			this.spell = spell; this.aim = aim; dimension = player.level().dimension();
			remaining = spell.castingProfile().preparationTicks();
			session = (com.example.examplemod.bootstrap.ProjectileComposition.handles(spell) || com.example.examplemod.bootstrap.TargetedAreaComposition.handles(spell) || com.example.examplemod.bootstrap.LocalEffectComposition.handles(spell) || com.example.examplemod.bootstrap.SelfEffectComposition.handles(spell) || spell==SpellDefinition.WATERFALL) ? new com.example.examplemod.magic.domain.casting.CastingSession(remaining) : null;
			command = com.example.examplemod.bootstrap.ProjectileComposition.handles(spell) ? com.example.examplemod.bootstrap.ProjectileComposition.create(player, spell) : null;
			localCommand = com.example.examplemod.bootstrap.LocalEffectComposition.handles(spell) ? com.example.examplemod.bootstrap.LocalEffectComposition.create(player,spell) : null;
			selfCommand = com.example.examplemod.bootstrap.SelfEffectComposition.handles(spell) ? com.example.examplemod.bootstrap.SelfEffectComposition.create(player,spell) : null;
			areaCommand = com.example.examplemod.bootstrap.TargetedAreaComposition.handles(spell) ? com.example.examplemod.bootstrap.TargetedAreaComposition.create(player,spell) : null;
		}
	}
	private static boolean available(ServerPlayer player, SpellDefinition spell) {
		var state = MagicSystem.state(player);
		return com.example.examplemod.magic.application.casting.PreparationEligibility.allows(new com.example.examplemod.magic.application.casting.PreparationEligibility.Player(){
			public boolean counterReady(){return player.level().getGameTime()>=player.getAttachedOrElse(MagicAttachments.COUNTER_UNTIL,0L);}
			public boolean silenced(){return com.example.examplemod.infrastructure.fabric.magic.FiniteBarrierRuntimeAdapter.silenced(player);}
			public boolean alive(){return player.isAlive();}
			public boolean spectator(){return player.isSpectator();}
			public boolean usingItem(){return player.isUsingItem();}
			public boolean sleeping(){return player.isSleeping();}
			public boolean inventoryOpen(){return player.containerMenu==player.inventoryMenu;}
			public boolean cooldownReady(){return state.cooldown()==0;}
			public boolean selected(){return state.selected().equals(spell.id());}
			public boolean learned(){return state.learned().contains(spell.id());}
			public boolean loaded(){return state.loaded().contains(spell.id());}
			public boolean enoughMana(){return state.mana()>=(ChannelRuntimeAdapter.isChannel(spell)?1:spell.manaCost());}
			public boolean hookAllows(){return SpellHooks.canUse(player,spell,SpellHooks.CastSource.KEYBOARD);}
		});
	}
	public static boolean counterable(ServerPlayer player){var pending=runtime(player).get(player.getUUID());return pending!=null&&!pending.spell.castingProfile().requiresCircle();}
	public static boolean active(ServerPlayer player){return runtime(player).contains(player.getUUID());}
	public static void request(ServerPlayer player, SpellDefinition spell, MagicAim aim) {
		if (spell == null || !aim.valid() || ChantLessons.active(player) || !MagicSystem.state(player).selected().equals(spell.id())) return;
		if (PlayerSpellRuntime.channelActive(player)) { PlayerSpellRuntime.channel(player, spell, aim); return; }
		if (StoneCharge.active(player)) { StoneCharge.heartbeat(player, aim); return; }
		var pending = runtime(player).get(player.getUUID());
		if (pending != null) {
			if (pending.spell == spell) { pending.aim = aim; pending.heartbeat = 0; if (pending.session != null) pending.session.heartbeat(); }
			else cancel(player);
			return;
		}
		if (!available(player, spell) || !SpellExecutors.canExecute(player, spell, aim)) return;
		var next = new Pending(player, spell, aim);
		if (spell.castingProfile().requiresCircle() && spell!=SpellDefinition.DEMON_GOD_SEALING_BARRIER) {
			next.circle = DivineCircle.locate(player);
			if (next.circle == null) return;
		}
		if(spell==SpellDefinition.DEMON_GOD_SEALING_BARRIER){next.sealTarget=PoweredBarrierRuntimeAdapter.sealTarget(player,aim).getUUID();next.cores=PoweredBarrierRuntimeAdapter.sealCores(player,aim);next.origin=player.position();}
		if(spell==SpellDefinition.TELEPORTATION_MAGIC){next.origin=player.position();next.teleportToken=TeleportationAdapter.token(player);TeleportationAdapter.preview(player);}
		runtime(player).put(player.getUUID(), next);
		player.setAttached(MagicAttachments.VISUAL_SPELL,spell.id());
		player.setAttached(MagicAttachments.PREPARE_TOTAL, next.remaining);
		player.setAttached(MagicAttachments.PREPARE_LEFT, next.remaining);
		player.setAttached(MagicAttachments.CAST_COLOR, 0);
		player.setAttached(MagicAttachments.CAST_TICKS, next.remaining + 1);
	}
	public static void tick(ServerPlayer player) {
		var pending = runtime(player).get(player.getUUID());
		if (pending == null) return;
		if (!available(player, pending.spell) || !pending.dimension.equals(player.level().dimension()) || ++pending.heartbeat > 10) { cancel(player); return; }
		if(pending.spell==SpellDefinition.TELEPORTATION_MAGIC&&(player.position().distanceToSqr(pending.origin)>4||!TeleportationAdapter.canCast(player)||!pending.teleportToken.equals(TeleportationAdapter.token(player)))){cancel(player);return;}
		if(pending.cores!=null&&(player.position().distanceToSqr(pending.origin)>9||!pending.cores.equals(PoweredBarrierRuntimeAdapter.sealCores(player,pending.aim))||PoweredBarrierRuntimeAdapter.sealTarget(player,pending.aim)==null||!pending.sealTarget.equals(PoweredBarrierRuntimeAdapter.sealTarget(player,pending.aim).getUUID()))){cancel(player);return;}
		if(pending.spell==SpellDefinition.LIGHTNING&&com.example.examplemod.magic.spell.water.lightning.LightningRules.lockNow(pending.remaining)){pending.locked=LightningAdapter.point(player,pending.aim);if(pending.locked==null){cancel(player);return;}}
		if(pending.locked!=null&&com.example.examplemod.magic.spell.water.lightning.LightningRules.warn(pending.remaining)){var level=(net.minecraft.server.level.ServerLevel)player.level();for(int i=0;i<24;i++){double a=i*Math.PI/12;level.sendParticles(SpellParticles.accent(pending.spell),pending.locked.x+Math.cos(a)*2,pending.locked.y+.08,pending.locked.z+Math.sin(a)*2,1,0,.025,0,0);}}
		if (pending.circle != null) {
			if (!DivineCircle.valid(player, pending.circle)) { cancel(player); return; }
			if (pending.remaining % 5 == 0) DivineCircle.glow(player, pending.circle, pending.remaining);
		}
		MagicChantEffects.tick(player);
		if (pending.session != null) {
			var step = pending.session.tick(true);
			if (step.signal() == com.example.examplemod.magic.domain.casting.CastingSession.Signal.CANCELLED) { cancel(player); return; }
			pending.remaining = step.remaining();
		} else --pending.remaining;
		player.setAttached(MagicAttachments.PREPARE_LEFT, pending.remaining);
		if (pending.remaining > 0) return;
		runtime(player).remove(player.getUUID());
		player.setAttached(MagicAttachments.CAST_TICKS, 0);
		var spell = pending.spell;
		if (spell==SpellDefinition.LIGHTNING ? !LightningAdapter.validPoint(player,pending.locked) : !SpellExecutors.canExecute(player, spell, pending.aim)) return;
		if(spell==SpellDefinition.LIGHTNING){com.example.examplemod.bootstrap.LightningComposition.create(player,pending.locked).execute(pending.aim.yaw(),pending.aim.pitch());return;}
		if (ChannelRuntimeAdapter.isChannel(spell)) { PlayerSpellRuntime.channel(player, spell, pending.aim); return; }
		if (spell == SpellDefinition.STONE_CANNON) { StoneCharge.begin(player, pending.aim); return; }
		if (pending.command != null) {
			com.example.examplemod.bootstrap.ProjectileComposition.release(pending.command,player,spell,pending.aim);
			return;
		}
		if(pending.selfCommand!=null) { pending.selfCommand.execute(pending.aim.yaw(),pending.aim.pitch(),pending.circle==null?java.util.List.of():java.util.List.of(()->DivineCircle.consume(player)));return; }
		if(pending.localCommand!=null) { pending.localCommand.execute(pending.aim.yaw(),pending.aim.pitch());return; }
		if(pending.areaCommand!=null) { pending.areaCommand.execute(pending.aim.yaw(),pending.aim.pitch());return; }
		var before = MagicSystem.state(player);
		if (!before.canCast(spell)) return;
		player.setAttached(MagicAttachments.STATE, before.cast(spell));
		if (!(SpellExecutors.execute(player, spell, pending.aim))) { player.setAttached(MagicAttachments.STATE, before); return; }
		if (pending.circle != null) DivineCircle.consume(player);
		player.setAttached(MagicAttachments.RECOVERY_TOTAL, spell.cooldownTicks());
		SequentialMagic.released(player,spell);
		MagicGlow.success(player, spell);
		MagicSounds.listener.accept(player, MagicSounds.Phase.RELEASE);
	}
	public static void cancel(ServerPlayer player) {
		SequentialMagic.cancel(player);
		var removed = runtime(player).remove(player.getUUID());
		if (removed != null) { if (removed.session != null) removed.session.cancel(); player.setAttached(MagicAttachments.CAST_TICKS, 0); }
		player.setAttached(MagicAttachments.PREPARE_LEFT, 0);
		player.setAttached(MagicAttachments.PREPARE_TOTAL, 0);
	}
	private PreparationRuntimeAdapter() { }
}
