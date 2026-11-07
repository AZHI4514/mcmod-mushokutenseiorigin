package com.example.examplemod.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/** 预扣存入持久化附件；异常退出后加入时退款，正常释放才设冷却。 */
public final class StoneCharge {
	private static final Map<UUID, Charge> ACTIVE=new HashMap<>();
	private static final class Charge {
		final ResourceKey<Level> dimension;
		MagicAim aim; int ticks; int heartbeat;
		Charge(ServerPlayer player,MagicAim aim) { this.dimension=player.level().dimension(); this.aim=aim; }
	}
	public static boolean active(ServerPlayer player) { return ACTIVE.containsKey(player.getUUID()); }
	public static void heartbeat(ServerPlayer player,MagicAim aim) {
		var charge=ACTIVE.get(player.getUUID()); if(charge!=null&&aim.valid()) { charge.aim=aim; charge.heartbeat=0; }
	}
	public static boolean begin(ServerPlayer player,MagicAim aim) {
		if(active(player)) { heartbeat(player,aim); return true; }
		var state=MagicSystem.state(player); var spell=SpellDefinition.STONE_CANNON;
		if(!aim.valid()||!state.canCast(spell)||!SpellHooks.canUse(player,spell,SpellHooks.CastSource.KEYBOARD)) return false;
		player.setAttached(MagicAttachments.CHARGE_BUDGET,state.mana());
		player.setAttached(MagicAttachments.STATE,state.spendMana(10));
		player.setAttached(MagicAttachments.CHARGE_MANA,10);
		ACTIVE.put(player.getUUID(),new Charge(player,aim)); return true;
	}
	public static boolean tick(ServerPlayer player) {
		var charge=ACTIVE.get(player.getUUID()); if(charge==null) return false;
		if(!valid(player)||!charge.dimension.equals(player.level().dimension())||++charge.heartbeat>10) { cancel(player); return true; }
		var state=MagicSystem.state(player);
		int reserved=player.getAttachedOrElse(MagicAttachments.CHARGE_MANA,0);
		long previous=IntermediateRules.chargeExtraAfterTicks(charge.ticks);
		if(charge.ticks<Integer.MAX_VALUE) charge.ticks++;
		long due=IntermediateRules.chargeExtraAfterTicks(charge.ticks)-previous;
		int debit=(int)Math.min(due,Math.min(state.mana(),Integer.MAX_VALUE-reserved));
		if(debit>0) {
			player.setAttached(MagicAttachments.STATE,state.spendMana(debit));
			player.setAttached(MagicAttachments.CHARGE_MANA,reserved+debit);
		}
		if(MagicSystem.state(player).mana()==0) { release(player,charge.aim); return true; }
		return false;
	}
	private static boolean valid(ServerPlayer player) {
		var state=MagicSystem.state(player);
		return player.isAlive()&&!player.isSpectator()&&!player.isUsingItem()&&!player.isSleeping()
				&&player.containerMenu==player.inventoryMenu&&state.selected().equals("stone_cannon")&&state.loaded().contains("stone_cannon")&&state.learned().contains("stone_cannon");
	}
	public static void release(ServerPlayer player,MagicAim aim) {
		if(!active(player)) return;
		if(!valid(player)||!aim.valid()||!ACTIVE.get(player.getUUID()).dimension.equals(player.level().dimension())) { cancel(player); return; }
		int paid=player.getAttachedOrElse(MagicAttachments.CHARGE_MANA,0);
		if(!IntermediateSpells.execute(player,SpellDefinition.STONE_CANNON,aim,paid)) { cancel(player); return; }
		ACTIVE.remove(player.getUUID()); player.setAttached(MagicAttachments.CHARGE_MANA,0); player.setAttached(MagicAttachments.CHARGE_BUDGET,0);
		player.setAttached(MagicAttachments.STATE,MagicSystem.state(player).withCooldown(SpellDefinition.STONE_CANNON.cooldownTicks()));
		player.setAttached(MagicAttachments.CAST_TICKS,2); MagicSounds.listener.accept(player,MagicSounds.Phase.RELEASE);
	}
	public static void cancel(ServerPlayer player) {
		ACTIVE.remove(player.getUUID()); int paid=player.getAttachedOrElse(MagicAttachments.CHARGE_MANA,0);
		player.setAttached(MagicAttachments.CHARGE_MANA,0); player.setAttached(MagicAttachments.CHARGE_BUDGET,0);
		if(paid>0) player.setAttached(MagicAttachments.STATE,MagicSystem.state(player).restoreMana(paid));
	}
	public static void clear() { ACTIVE.clear(); }
	private StoneCharge() { }
}
