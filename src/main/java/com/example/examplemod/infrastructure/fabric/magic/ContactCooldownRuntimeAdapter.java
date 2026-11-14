package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import java.util.*;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.*;

/** Contact cadence index scoped to the server, shared by all applicable fields. */
public final class ContactCooldownRuntimeAdapter {
	private static final AttachmentType<com.example.examplemod.magic.mechanic.field.ContactCooldowns> CONTACT_RUNTIME=AttachmentRegistry.create(
			ExampleMod.id("contact_runtime"),b->b.initializer(()->new com.example.examplemod.magic.mechanic.field.ContactCooldowns(20)));
	private static com.example.examplemod.magic.mechanic.field.ContactCooldowns contacts(ServerLevel level) {
		return level.getServer().overworld().getAttachedOrCreate(CONTACT_RUNTIME);
	}
	public static void initialize() {}
	public static boolean claim(ServerLevel level,UUID area,UUID target,long now){return contacts(level).claim(area,target,now);}
	public static void prune(ServerLevel level,long now){contacts(level).prune(now);}
	public static void clear(ServerPlayer player){contacts((ServerLevel)player.level()).clear(player.getUUID());}
	public static void clearServer(net.minecraft.server.MinecraftServer server){server.overworld().removeAttached(CONTACT_RUNTIME);}
	private ContactCooldownRuntimeAdapter() {}
}
