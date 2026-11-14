package com.example.examplemod.infrastructure.fabric.magic;

import java.util.*;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.magic.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.level.*;

/** Server-owned ongoing channels; debt remains persistent player data. */
public final class ChannelRuntimeAdapter {
	public static boolean isChannel(SpellDefinition spell){return spell!=null&&(spell.id().equals("waterfall")||spell.id().equals("flamethrower"));}
	// Negative debt is prepaid fractional mana. Persistent across stop/restart; never refunded as full mana.
	public static final AttachmentType<Integer> CHANNEL_DEBT=AttachmentRegistry.create(ExampleMod.id("channel_debt"),b->b.initializer(()->0).persistent(Codec.intRange(-19,0)).copyOnDeath());
	/** Not persistent or synced: each server owns its channel container through its overworld. */
	private static final AttachmentType<ChannelRuntime> CHANNEL_RUNTIME=AttachmentRegistry.create(
			ExampleMod.id("channel_runtime"),b->b.initializer(ChannelRuntime::new));
	private static final class ChannelRuntime { final Map<UUID,Channel> channels=new HashMap<>(); }
	private static ChannelRuntime runtime(ServerPlayer player) {
		return player.level().getServer().overworld().getAttachedOrCreate(CHANNEL_RUNTIME);
	}
	public static void clearServer(net.minecraft.server.MinecraftServer server) {
		var runtime=server.overworld().getAttached(CHANNEL_RUNTIME);
		if(runtime!=null)runtime.channels.clear();
		server.overworld().removeAttached(CHANNEL_RUNTIME);
	}
	private static final class Channel {
		final SpellDefinition spell; final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
		MagicAim aim;
		final com.example.examplemod.magic.application.casting.FlamethrowerChannel flamethrower;
		final com.example.examplemod.infrastructure.fabric.magic.WaterfallSourceAdapter waterSource;
		final com.example.examplemod.magic.application.casting.WaterfallChannel waterfall;
		Channel(ServerPlayer player,SpellDefinition spell,MagicAim aim) {
			this.spell=spell;this.aim=aim;dimension=player.level().dimension();
			boolean water=spell==SpellDefinition.WATERFALL;
			flamethrower=water?null:new com.example.examplemod.magic.application.casting.FlamethrowerChannel(
					new com.example.examplemod.infrastructure.fabric.magic.PlayerStateAdapter(player),
					new com.example.examplemod.infrastructure.fabric.magic.ChannelDebtAdapter(player),
					new com.example.examplemod.infrastructure.fabric.magic.FlamethrowerAdapter(player),()->MagicGlow.success(player,spell));
			waterSource=water?new com.example.examplemod.infrastructure.fabric.magic.WaterfallSourceAdapter(player):null;
			waterfall=water?new com.example.examplemod.magic.application.casting.WaterfallChannel(
					new com.example.examplemod.infrastructure.fabric.magic.PlayerStateAdapter(player),
					new com.example.examplemod.infrastructure.fabric.magic.ChannelDebtAdapter(player),
					waterSource,()->MagicGlow.success(player,spell)):null;
		}
	}
	private static boolean available(ServerPlayer player,Channel channel) {
		var state=MagicSystem.state(player);
		return player.isAlive()&&!player.isRemoved()&&!player.isSpectator()&&!player.isUsingItem()&&!player.isSleeping()
				&&player.containerMenu==player.inventoryMenu&&channel.dimension.equals(player.level().dimension())
				&&state.selected().equals(channel.spell.id())&&state.loaded().contains(channel.spell.id())&&state.learned().contains(channel.spell.id())
				&&SpellHooks.canUse(player,channel.spell,SpellHooks.CastSource.KEYBOARD);
	}
	public static boolean channelActive(ServerPlayer player) { return runtime(player).channels.containsKey(player.getUUID()); }
	public static void channel(ServerPlayer player,SpellDefinition spell,MagicAim aim) {
		if(!aim.valid()) return;
		var old=runtime(player).channels.get(player.getUUID());
		if(old!=null) { if(old.spell==spell) { old.aim=aim; if(old.waterfall!=null)old.waterfall.heartbeat();else old.flamethrower.heartbeat(); } return; }
		var state=MagicSystem.state(player);
		if(state.cooldown()!=0||!state.selected().equals(spell.id())||!state.loaded().contains(spell.id())||!state.learned().contains(spell.id())||state.mana()<1||!SpellHooks.canUse(player,spell,SpellHooks.CastSource.KEYBOARD)||!aim.valid()) return;
		var next=new Channel(player,spell,aim);
		com.example.examplemod.magic.mechanic.channel.ChannelAdmission.apply(new com.example.examplemod.magic.mechanic.channel.ChannelAdmission.Entry(){
			public boolean begin(){return spell==SpellDefinition.WATERFALL?next.waterfall.begin(aim.yaw(),aim.pitch()):next.flamethrower.begin(aim.yaw(),aim.pitch());}
			public void register(){runtime(player).channels.put(player.getUUID(),next);}
			public void emit(){if(next.waterfall!=null)MagicGlow.success(player,spell);else next.flamethrower.emit(aim.yaw(),aim.pitch());}
			public void releaseSound(){MagicSounds.listener.accept(player,MagicSounds.Phase.RELEASE);}
		});
	}
	/** Compatibility for callers of the old instantaneous entry, without opening a second channel. */
	public static boolean generateWater(ServerPlayer player,MagicAim aim) {
		var channel=runtime(player).channels.get(player.getUUID());
		var source=channel!=null&&channel.waterSource!=null?channel.waterSource:
				new com.example.examplemod.infrastructure.fabric.magic.WaterfallSourceAdapter(player);
		return source.generate(aim.yaw(),aim.pitch());
	}

	public static void stop(ServerPlayer player) {
		var channel=runtime(player).channels.remove(player.getUUID()); if(channel==null) return;
		// Waterfall sources remain as permanent vanilla water.
		player.setAttached(MagicAttachments.RECOVERY_TOTAL,channel.spell.cooldownTicks());
		if(channel.waterfall!=null)channel.waterfall.stop();
		else channel.flamethrower.stop();
	}
	public static boolean tick(ServerPlayer player) {
		var id=player.getUUID();
		var channel=runtime(player).channels.get(id); if(channel==null) return false;
		if(channel.waterfall!=null) {
			if(channel.waterfall.tick(available(player,channel),channel.aim.yaw(),channel.aim.pitch())) { stop(player);return true; }
			return false;
		}
		if(channel.flamethrower.tick(available(player,channel),channel.aim.yaw(),channel.aim.pitch())) { stop(player);return true; }
		return false;
	}
	private ChannelRuntimeAdapter() { }
}
