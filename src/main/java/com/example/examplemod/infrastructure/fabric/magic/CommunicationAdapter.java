package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.special.communication_magic.*;
import com.example.examplemod.magic.spell.special.communication_magic.CommunicationDrafts.Draft;
import com.example.examplemod.magic.application.port.SelfEffect;

import java.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.*;
import com.example.examplemod.ExampleMod;

/** Messages retain vanilla signing/filtering; both players must explicitly bind each other. */
public record CommunicationAdapter(ServerPlayer player) implements SelfEffect {
	private static final AttachmentType<List<String>> BONDS=AttachmentRegistry.create(ExampleMod.id("communication_bonds"),b->b.initializer(()->List.<String>of()).persistent(Codec.STRING.listOf()).copyOnDeath());
	private static final AttachmentType<Boolean> MUTED=AttachmentRegistry.create(ExampleMod.id("communication_muted"),b->b.initializer(()->false).persistent(Codec.BOOL).copyOnDeath());
	private static final AttachmentType<CommunicationDrafts<PlayerChatMessage>> RUNTIME=AttachmentRegistry.create(ExampleMod.id("communication_runtime"),b->b.initializer(()->new CommunicationDrafts<PlayerChatMessage>()));
	private static CommunicationDrafts<PlayerChatMessage> drafts(ServerPlayer p){return p.level().getServer().overworld().getAttachedOrCreate(RUNTIME);}
	public static void initialize(){
		CommandRegistrationCallback.EVENT.register((d,r,e)->d.register(Commands.literal("mtcommunication")
			.then(Commands.literal("bind").then(Commands.argument("player",EntityArgument.player()).executes(c->bond(c.getSource().getPlayerOrException(),EntityArgument.getPlayer(c,"player"),true))))
			.then(Commands.literal("unbind").then(Commands.argument("player",EntityArgument.player()).executes(c->bond(c.getSource().getPlayerOrException(),EntityArgument.getPlayer(c,"player"),false))))
			.then(Commands.literal("mute").executes(c->{c.getSource().getPlayerOrException().setAttached(MUTED,true);return 1;}))
			.then(Commands.literal("unmute").executes(c->{c.getSource().getPlayerOrException().setAttached(MUTED,false);return 1;}))
			.then(Commands.literal("draft").then(Commands.argument("player",EntityArgument.player()).then(Commands.argument("message",MessageArgument.message()).executes(c->{
				var sender=c.getSource().getPlayerOrException();var recipient=EntityArgument.getPlayer(c,"player");
				MessageArgument.resolveChatMessage(c,"message",message->{
					if(!CommunicationRules.validLength(message.signedContent())||message.isFullyFiltered()||!ready(sender,recipient))return;
					drafts(sender).put(sender.getUUID(),new Draft<>(recipient.getUUID(),message,sender.level().getGameTime()+1200));
					sender.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.communication_ready"));
				});return 1;
			}))))));
	}
	private static int bond(ServerPlayer player,ServerPlayer other,boolean allow){
		if(player==other)return 0;var set=new HashSet<>(player.getAttachedOrCreate(BONDS));
		if(allow&&set.size()>=64)return 0;
		if(allow)set.add(other.getUUID().toString());else set.remove(other.getUUID().toString());
		player.setAttached(BONDS,List.copyOf(set));return 1;
	}
	private static boolean ready(ServerPlayer sender,ServerPlayer recipient){
		return CommunicationRules.ready(new CommunicationRules.Peer(){
			public boolean present(){return recipient!=null;}public boolean self(){return sender==recipient;}public boolean receiverDisconnected(){return recipient.hasDisconnected();}public boolean senderDisconnected(){return sender.hasDisconnected();}
			public boolean senderChat(){return sender.getChatVisibility()==net.minecraft.world.entity.player.ChatVisiblity.FULL;}public boolean receiverChat(){return recipient.getChatVisibility()==net.minecraft.world.entity.player.ChatVisiblity.FULL;}
			public boolean muted(){return recipient.getAttachedOrCreate(MUTED);}public boolean senderBound(){return sender.getAttachedOrCreate(BONDS).contains(recipient.getUUID().toString());}public boolean receiverBound(){return recipient.getAttachedOrCreate(BONDS).contains(sender.getUUID().toString());}
		});
	}
	public static boolean canCast(ServerPlayer sender){
		var draft=drafts(sender).get(sender.getUUID());return draft!=null&&draft.expires()>sender.level().getGameTime()&&!draft.message().hasExpiredServer(java.time.Instant.now())&&ready(sender,sender.level().getServer().getPlayerList().getPlayer(draft.recipient()));
	}
	public static boolean cast(ServerPlayer sender){
		return CommunicationRelease.cast(new CommunicationRelease.World(){
			Draft<PlayerChatMessage> draft;ServerPlayer recipient;OutgoingChatMessage outgoing;
			public boolean ready(){return canCast(sender);}public void takeDraft(){draft=drafts(sender).remove(sender.getUUID());recipient=sender.level().getServer().getPlayerList().getPlayer(draft.recipient());outgoing=OutgoingChatMessage.create(draft.message());}
			public void sendRecipient(){recipient.sendChatMessage(outgoing,sender.shouldFilterMessageTo(recipient),ChatType.bind(ChatType.MSG_COMMAND_INCOMING,sender));}
			public void sendSender(){sender.sendChatMessage(outgoing,false,ChatType.bind(ChatType.MSG_COMMAND_OUTGOING,sender).withTargetName(recipient.getDisplayName()));}
			public void visual(){SpellVisuals.burst(sender.level(),SpellDefinition.COMMUNICATION_MAGIC,sender.getEyePosition(),sender.getLookAngle());}
		});
	}
	public static void clear(ServerPlayer player){drafts(player).remove(player.getUUID());}
	public boolean apply(float yaw,float pitch){return cast(player);}
}
