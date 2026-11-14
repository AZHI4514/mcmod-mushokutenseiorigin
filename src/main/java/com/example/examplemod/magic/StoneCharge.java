package com.example.examplemod.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/** 预扣存入持久化附件；异常退出后加入时退款，正常释放才设冷却。 */
public final class StoneCharge {
	/** The overworld is a stable, server-owned lifetime anchor; never saved or synced. */
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<StoneCharge> RUNTIME =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(
					com.example.examplemod.ExampleMod.id("stone_charge_runtime"),
					builder -> builder.initializer(StoneCharge::new));
	private final Map<UUID, Charge> active = new HashMap<>();
	private static StoneCharge runtime(ServerPlayer player) {
		return player.level().getServer().overworld().getAttachedOrCreate(RUNTIME);
	}
	/** Register attachment during mod initialization, before any player joins. */
	public static void initialize() { }
	private static final class Charge {
		final ResourceKey<Level> dimension;
		MagicAim aim;
		final com.example.examplemod.magic.spell.earth.stone_cannon.ChargeSession session=new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeSession();
		Charge(ServerPlayer player,MagicAim aim) { this.dimension=player.level().dimension(); this.aim=aim; }
	}
	private static com.example.examplemod.magic.application.casting.ChargePayment payment(ServerPlayer player) {
		return new com.example.examplemod.magic.application.casting.ChargePayment(new com.example.examplemod.infrastructure.fabric.magic.PlayerStateAdapter(player),new com.example.examplemod.infrastructure.fabric.magic.ChargeReservationAdapter(player));
	}
	public static boolean active(ServerPlayer player) { return runtime(player).active.containsKey(player.getUUID()); }
	public static void heartbeat(ServerPlayer player,MagicAim aim) {
		var charge=runtime(player).active.get(player.getUUID()); if(charge!=null&&aim.valid()) { charge.aim=aim; charge.session.heartbeat(); }
	}
	public static boolean begin(ServerPlayer player,MagicAim aim) {
		if(active(player)) { heartbeat(player,aim); return true; }
		var state=MagicSystem.state(player); var spell=SpellDefinition.STONE_CANNON;
		if(!aim.valid()||!state.canCast(spell)||!SpellHooks.canUse(player,spell,SpellHooks.CastSource.KEYBOARD)) return false;
		if(!payment(player).begin())return false;
		runtime(player).active.put(player.getUUID(),new Charge(player,aim)); return true;
	}
	public static boolean tick(ServerPlayer player) {
		var charge=runtime(player).active.get(player.getUUID()); if(charge==null) return false;
		if(charge.session.expired(valid(player)&&charge.dimension.equals(player.level().dimension()))) { cancel(player); return true; }
		var state=MagicSystem.state(player);
		int reserved=player.getAttachedOrElse(MagicAttachments.CHARGE_MANA,0);
		var step=charge.session.advance(state.mana(),reserved);
		payment(player).debit(step,player.getAttachedOrElse(MagicAttachments.CHARGE_BUDGET,0));
		if(MagicSystem.state(player).mana()==0) { release(player,charge.aim); return true; }
		return false;
	}
	private static boolean valid(ServerPlayer player) {
		var state=MagicSystem.state(player);
		return new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeEligibility(
				player.isAlive(),player.isSpectator(),player.isUsingItem(),player.isSleeping(),
				player.containerMenu==player.inventoryMenu,state.selected().equals("stone_cannon"),
				state.loaded().contains("stone_cannon"),state.learned().contains("stone_cannon")).eligible();
	}
	public static void release(ServerPlayer player,MagicAim aim) {
		if(!active(player)) return;
		if(!valid(player)||!aim.valid()||!runtime(player).active.get(player.getUUID()).dimension.equals(player.level().dimension())) { cancel(player); return; }
		if(!payment(player).release(new com.example.examplemod.infrastructure.fabric.magic.StoneCannonProjectileAdapter(player,aim),SpellDefinition.STONE_CANNON.cooldownTicks())) { runtime(player).active.remove(player.getUUID());return; }
		runtime(player).active.remove(player.getUUID());
		player.setAttached(MagicAttachments.RECOVERY_TOTAL,SpellDefinition.STONE_CANNON.cooldownTicks());
		MagicGlow.success(player,SpellDefinition.STONE_CANNON); MagicSounds.listener.accept(player,MagicSounds.Phase.RELEASE);
	}
	public static void cancel(ServerPlayer player) {
		runtime(player).active.remove(player.getUUID());payment(player).cancel();
	}
	/** Call after refunding online players; does not affect another server instance. */
	public static void clear(net.minecraft.server.MinecraftServer server) {
		var runtime = server.overworld().getAttached(RUNTIME);
		if (runtime != null) runtime.active.clear();
		server.overworld().removeAttached(RUNTIME);
	}
	private StoneCharge() { }
}
