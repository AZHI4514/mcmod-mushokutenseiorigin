package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import java.util.HashSet;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;

public final class SupportConsentAdapter {
	private static final AttachmentType<java.util.List<String>> TRUSTED = AttachmentRegistry.create(
			ExampleMod.id("support_consent"), b -> b.initializer(() -> java.util.List.<String>of()).persistent(Codec.STRING.listOf()).copyOnDeath());
	public static void initialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
			dispatcher.register(Commands.literal("mtconsent")
				.then(Commands.argument("caster",EntityArgument.player())
					.then(Commands.literal("allow").executes(c -> consent(c.getSource().getPlayerOrException(),EntityArgument.getPlayer(c,"caster"),true)))
					.then(Commands.literal("revoke").executes(c -> consent(c.getSource().getPlayerOrException(),EntityArgument.getPlayer(c,"caster"),false)))));
		});
	}
	private static int consent(ServerPlayer recipient,ServerPlayer caster,boolean allow){
		var trusted=new HashSet<>(recipient.getAttachedOrCreate(TRUSTED));
		if(allow)trusted.add(caster.getUUID().toString());else trusted.remove(caster.getUUID().toString());
		recipient.setAttached(TRUSTED,java.util.List.copyOf(trusted));
		recipient.sendSystemMessage(Component.translatable(allow?"magic.mushokutenseiorigin.consent_allowed":"magic.mushokutenseiorigin.consent_revoked",caster.getDisplayName()));
		return 1;
	}
	public static boolean consented(ServerPlayer caster,LivingEntity recipient){
		return com.example.examplemod.magic.mechanic.targeting.SupportConsent.allows(new com.example.examplemod.magic.mechanic.targeting.SupportConsent.Recipient(){
			public boolean self(){return recipient==caster;}public boolean player(){return recipient instanceof ServerPlayer;}
			public boolean trusted(){return ((ServerPlayer)recipient).getAttachedOrCreate(TRUSTED).contains(caster.getUUID().toString());}
			public boolean pet(){return recipient instanceof TamableAnimal;}public boolean owned(){return ((TamableAnimal)recipient).getOwner()==caster;}
			public boolean allied(){return recipient.isAlliedTo(caster);}
		});
	}
	private SupportConsentAdapter() { }
}
