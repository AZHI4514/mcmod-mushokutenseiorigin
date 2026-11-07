package com.example.examplemod.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.example.examplemod.ExampleMod;

/** 所有状态变化发生在服务端线程；一次请求最多执行一次成功施法。 */
public final class MagicSystem {
	private static final Map<UUID, Session> SESSIONS = new HashMap<>();
	public static final EntityType<WaterBallEntity> WATER_BALL = registerProjectile("water_ball");
	public static final EntityType<WaterBallEntity> FIREBALL = registerProjectile("fireball");
	public static final EntityType<WaterBallEntity> WIND_BALL = registerProjectile("wind_ball");
	public static final EntityType<WaterBallEntity> ROCK_BULLET = registerProjectile("rock_bullet");
	public static final EntityType<WaterBallEntity> WATER_ARROW = registerProjectile("water_arrow");
	public static final EntityType<WaterBallEntity> ICE_SMASH = registerProjectile("ice_smash");
	public static final EntityType<WaterBallEntity> ICE_BLADE = registerProjectile("ice_blade");
	public static final EntityType<MagicAreaEntity> MAGIC_AREA = Registry.register(BuiltInRegistries.ENTITY_TYPE,
			ExampleMod.id("magic_area"), EntityType.Builder.<MagicAreaEntity>of(MagicAreaEntity::new, MobCategory.MISC)
					.sized(0.1F, 0.1F).clientTrackingRange(6).updateInterval(1)
					.build(ResourceKey.create(Registries.ENTITY_TYPE, ExampleMod.id("magic_area"))));
	public static final EntityType<FlameSliceEntity> FLAME_SLICE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
			ExampleMod.id("flame_slice"), EntityType.Builder.<FlameSliceEntity>of(FlameSliceEntity::new, MobCategory.MISC)
					.sized(1.5F, 0.25F).clientTrackingRange(4).updateInterval(1)
					.build(ResourceKey.create(Registries.ENTITY_TYPE, ExampleMod.id("flame_slice"))));

	public static final EntityType<IntermediateProjectile> WATER_CANNON=registerIntermediate("water_cannon",0.25F);
	public static final EntityType<IntermediateProjectile> WATER_SPLASH=registerIntermediate("water_splash",0.1F);
	public static final EntityType<IntermediateProjectile> ICICLE_FIELD=registerIntermediate("icicle_field",0.1F);
	public static final EntityType<IntermediateProjectile> EX_FLAME=registerIntermediate("ex_flame",2F);
	public static final EntityType<IntermediateProjectile> SONIC_BOOM=registerIntermediate("sonic_boom",1F);
	public static final EntityType<IntermediateProjectile> STONE_CANNON=registerIntermediate("stone_cannon",0.25F);
	public static final EntityType<PhaseTwoEntity> PHASE_TWO=Registry.register(BuiltInRegistries.ENTITY_TYPE,ExampleMod.id("phase_two_spell"),EntityType.Builder.<PhaseTwoEntity>of(PhaseTwoEntity::new,MobCategory.MISC).sized(0.25F,0.25F).clientTrackingRange(8).updateInterval(1).build(ResourceKey.create(Registries.ENTITY_TYPE,ExampleMod.id("phase_two_spell"))));
	private static EntityType<IntermediateProjectile> registerIntermediate(String id,float size) {
		return Registry.register(BuiltInRegistries.ENTITY_TYPE,ExampleMod.id(id),EntityType.Builder.<IntermediateProjectile>of(IntermediateProjectile::new,MobCategory.MISC)
				.sized(size,size).clientTrackingRange(8).updateInterval(1).build(ResourceKey.create(Registries.ENTITY_TYPE,ExampleMod.id(id))));
	}
	static EntityType<IntermediateProjectile> intermediateType(SpellDefinition spell) {
		return switch(spell.id()) {
			case "water_cannon"->WATER_CANNON; case "water_splash"->WATER_SPLASH; case "icicle_field"->ICICLE_FIELD;
			case "ex_flame"->EX_FLAME; case "sonic_boom"->SONIC_BOOM; case "stone_cannon"->STONE_CANNON;
			default->throw new IllegalArgumentException("Unknown intermediate spell");
		};
	}
	private static EntityType<WaterBallEntity> registerProjectile(String id) {
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, ExampleMod.id(id),
				EntityType.Builder.<WaterBallEntity>of(WaterBallEntity::new, MobCategory.MISC)
						.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(1)
						.build(ResourceKey.create(Registries.ENTITY_TYPE, ExampleMod.id(id))));
	}

	static EntityType<WaterBallEntity> projectileType(SpellDefinition spell) {
		return switch (spell.id()) {
			case "water_ball" -> WATER_BALL;
			case "fireball" -> FIREBALL;
			case "wind_ball" -> WIND_BALL;
			case "rock_bullet" -> ROCK_BULLET;
			case "water_arrow" -> WATER_ARROW;
			case "ice_smash" -> ICE_SMASH;
			case "ice_blade" -> ICE_BLADE;
			default -> throw new IllegalArgumentException("No spell executor: " + spell.id());
		};
	}

	public static void initialize() {
		MagicConfig.load();
		MagicTerrain.initialize();
		MagicEffects.initialize();
		MagicAreas.initialize();
		PhaseTwoRuntime.initialize();
		MagicAttachments.initialize();
		MagicCommands.initialize();
		PayloadTypeRegistry.serverboundPlay().register(MagicActionPayload.TYPE, MagicActionPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(MagicActionPayload.TYPE,
				(packet, context) -> handle(context.player(), packet));
		ServerPlayerEvents.JOIN.register(player -> {
			SESSIONS.put(player.getUUID(), new Session());
			StoneCharge.cancel(player);
			player.setAttached(MagicAttachments.STATE, state(player));
		});
		ServerPlayerEvents.LEAVE.register(player -> { StoneCharge.cancel(player); PhaseTwoRuntime.clear(player); SESSIONS.remove(player.getUUID()); MagicAreas.clear(player.getUUID()); });
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> { server.getPlayerList().getPlayers().forEach(player -> { StoneCharge.cancel(player); PhaseTwoRuntime.clear(player); }); StoneCharge.clear(); SESSIONS.clear(); MagicAreas.clearAll(); });
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, player, alive) -> {
			SESSIONS.put(player.getUUID(), new Session());
			MagicAreas.clear(player.getUUID());
			PhaseTwoRuntime.clear(oldPlayer);
			StoneCharge.cancel(player);
			if (!alive) player.setAttached(MagicAttachments.STATE, state(player).refill());
			player.setAttached(MagicAttachments.CAST_TICKS, 0);
		});
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				Session session = SESSIONS.computeIfAbsent(player.getUUID(), id -> new Session());
				session.age++;
				if (session.dimension != null && !session.dimension.equals(player.level().dimension())) { MagicAreas.clear(player.getUUID()); PhaseTwoRuntime.clear(player); session.blocked=true; }
				session.dimension = player.level().dimension();
				if (player.isAlive()) player.setAttached(MagicAttachments.STATE, state(player).tick());
				if (player.isUsingItem() || !player.isAlive()) { session.blocked = true; MagicAreas.breakMaintenance(player); PhaseTwoRuntime.breakMaintenance(player); }
				if (!player.isAlive()) MagicAreas.clear(player.getUUID());
				if (StoneCharge.tick(player)) session.blocked=true;
				if (PhaseTwoRuntime.tick(player)) session.blocked=true;
				int pose = player.getAttachedOrElse(MagicAttachments.CAST_TICKS, 0);
				if (pose > 0) player.setAttached(MagicAttachments.CAST_TICKS,
						player.isUsingItem() ? 0 : pose - 1);
			}
		});
	}

	public static MagicState state(ServerPlayer player) {
		return player.getAttachedOrCreate(MagicAttachments.STATE);
	}

	/** 配置重载或后续成长系统可调用；增加上限不自动回满。 */
	public static void setMaximum(ServerPlayer player, int maximum) {
		player.setAttached(MagicAttachments.STATE, state(player).withMaximum(maximum));
	}

	public static void restoreMana(ServerPlayer player, int amount) {
		player.setAttached(MagicAttachments.STATE, state(player).restoreMana(amount));
	}

	public static void successfulSleep(ServerPlayer player) {
		player.setAttached(MagicAttachments.STATE, state(player).refill());
	}

	private static void handle(ServerPlayer player, MagicActionPayload packet) {
		Session session = SESSIONS.computeIfAbsent(player.getUUID(), id -> new Session());
		if (packet.sequence() <= session.lastSequence) return;
		session.lastSequence = packet.sequence();
		// Bound UI/network actions per tick, independently of the spell cooldown.
		if (session.requestTick != session.age) { session.requestTick = session.age; session.requests = 0; }
		if (++session.requests > 8) return;
		if (packet.action().equals("release")) { StoneCharge.release(player,new MagicAim(packet.yaw(),packet.pitch())); PhaseTwoRuntime.stop(player); PhaseTwoRuntime.breakMaintenance(player); MagicAreas.breakMaintenance(player); session.blocked = true; return; }
		if (packet.action().equals("stop")) { StoneCharge.cancel(player); PhaseTwoRuntime.stop(player); PhaseTwoRuntime.breakMaintenance(player); MagicAreas.breakMaintenance(player); session.blocked = true; player.setAttached(MagicAttachments.CAST_TICKS, 0); return; }
		if (packet.action().equals("arm")) {
			if (!player.isUsingItem() && player.isAlive() && !player.isSpectator()) session.blocked = false;
			return;
		}
		if (!player.isAlive() || player.isSpectator()) return;
		MagicState before = state(player);
		switch (packet.action()) {
			case "learn" -> {
				if (MagicBooks.teaches(player.getMainHandItem(), packet.spell())
						|| MagicBooks.teaches(player.getOffhandItem(), packet.spell()))
					player.setAttached(MagicAttachments.STATE, before.learn(packet.spell()));
			}
			case "toggle" -> { PhaseTwoRuntime.stop(player); PhaseTwoRuntime.breakMaintenance(player); StoneCharge.cancel(player); player.setAttached(MagicAttachments.STATE, state(player).toggle(packet.spell())); }
			case "select" -> { PhaseTwoRuntime.stop(player); PhaseTwoRuntime.breakMaintenance(player); StoneCharge.cancel(player); player.setAttached(MagicAttachments.STATE, state(player).select(packet.spell())); }
			case "cast" -> {
				if (player.isUsingItem() || player.containerMenu != player.inventoryMenu || player.isSleeping()) {
					StoneCharge.cancel(player);
					PhaseTwoRuntime.stop(player);
					session.blocked = true;
					player.setAttached(MagicAttachments.CAST_TICKS, 0);
					return;
				}
				if (session.blocked) return;
				SpellDefinition spell = SpellDefinition.find(packet.spell());
				if (PhaseTwoSpells.isChannel(spell)) { PhaseTwoRuntime.channel(player,spell,new MagicAim(packet.yaw(),packet.pitch())); return; }
				if (spell == SpellDefinition.STONE_CANNON) {
					StoneCharge.begin(player,new MagicAim(packet.yaw(),packet.pitch())); return;
				}
				if (!before.canCast(spell) || !SpellHooks.canUse(player, spell, SpellHooks.CastSource.KEYBOARD)) {
					if (before.cooldown() == 0 && session.age - session.lastHint >= 20) {
						player.sendOverlayMessage(Component.translatable("magic.mushokutenseiorigin.cannot_cast"));
						session.lastHint = session.age;
					}
					return;
				}
				var aim = new MagicAim(packet.yaw(), packet.pitch());
				if (!aim.valid()) return;
				if (!SpellExecutors.canExecute(player, spell, aim)) return;
				MagicState paid = before.cast(spell);
				player.setAttached(MagicAttachments.STATE, paid);
				if (!SpellExecutors.execute(player, spell, aim)) {
					player.setAttached(MagicAttachments.STATE, before);
					return;
				}
				MagicGlow.success(player,spell);
				MagicSounds.listener.accept(player, MagicSounds.Phase.RELEASE);
			}
			default -> { }
		}
	}

	private static final class Session {
		net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
		int lastSequence = -1;
		int age;
		int lastHint = -20;
		int requestTick = -1;
		int requests;
		boolean blocked = true;
	}

	private MagicSystem() {
	}
}
