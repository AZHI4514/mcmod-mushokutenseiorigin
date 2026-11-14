package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.PreparationRuntimeAdapter;

import java.util.*;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/** A shortcut queues two normal preparations, never a free second spell or a new learned ID. */
public final class SequentialMagic {
	private static final class Runtime {
		private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<Map<UUID,Integer>> SESSIONS =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("sequential_magic_runtime"),
				builder -> builder.initializer(() -> new HashMap<UUID,Integer>()));
	}
	private static Map<UUID,Integer> sessions(ServerPlayer player) {
		return player.level().getServer().overworld().getAttachedOrCreate(Runtime.SESSIONS);
	}
	public static void initialize(){CommandRegistrationCallback.EVENT.register((d,r,e)->d.register(Commands.literal("mtcombo")
		.then(Commands.literal("ice_lance_blizzard").executes(c->{var p=c.getSource().getPlayerOrException();var state=MagicSystem.state(p);
			if(!state.learned().contains("ice_lance")||!state.learned().contains("blizzard_storm")||!state.loaded().contains("ice_lance")||!state.loaded().contains("blizzard_storm")||PreparationRuntimeAdapter.active(p))return 0;
			p.setAttached(MagicAttachments.STATE,state.select("ice_lance"));sessions(p).put(p.getUUID(),0);p.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.combo_ready"));return 1;
		})).then(Commands.literal("cancel").executes(c->{cancel(c.getSource().getPlayerOrException());return 1;}))));}
	public static void request(ServerPlayer player,SpellDefinition selected,MagicAim aim){
		var stage=sessions(player).get(player.getUUID());if(stage==null){PreparationRuntimeAdapter.request(player,selected,aim);return;}
		var spell=stage==0?SpellDefinition.ICE_LANCE:SpellDefinition.BLIZZARD_STORM;
		if(!MagicSystem.state(player).selected().equals(spell.id())){cancel(player);return;}
		PreparationRuntimeAdapter.request(player,spell,aim);
	}
	public static void released(ServerPlayer player,SpellDefinition spell){
		var stage=sessions(player).get(player.getUUID());if(stage==null)return;
		if(stage==0&&spell==SpellDefinition.ICE_LANCE){sessions(player).put(player.getUUID(),1);player.setAttached(MagicAttachments.STATE,MagicSystem.state(player).select("blizzard_storm"));}
		else if(stage==1&&spell==SpellDefinition.BLIZZARD_STORM)sessions(player).remove(player.getUUID());
	}
	public static void cancel(ServerPlayer player){sessions(player).remove(player.getUUID());}
	private SequentialMagic(){}
}
