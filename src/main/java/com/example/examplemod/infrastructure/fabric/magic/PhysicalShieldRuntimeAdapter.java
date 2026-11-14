package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldRegistry;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

/** Owns the sole per-server, nonpersistent physical-shield attachment. */
public final class PhysicalShieldRuntimeAdapter {
	private static final AttachmentType<PhysicalShieldRegistry<DirectionalSpellCarrier>> SHIELD_RUNTIME=AttachmentRegistry.create(
			ExampleMod.id("physical_shield_runtime"),b->b.initializer(()->new PhysicalShieldRegistry<DirectionalSpellCarrier>()));
	public static PhysicalShieldRegistry<DirectionalSpellCarrier> shields(ServerLevel level) {
		return level.getServer().overworld().getAttachedOrCreate(SHIELD_RUNTIME);
	}
	public static void clearServer(MinecraftServer server) {
		server.overworld().removeAttached(SHIELD_RUNTIME);
	}
	public static boolean release(ServerPlayer player,MagicAim aim) {
		var previous=shields((ServerLevel)player.level()).get(player.getUUID());
		return com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldRelease.apply(new com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldRelease.World(){
			private DirectionalSpellCarrier entity;
			public boolean renewPrevious(){return previous!=null&&previous.renew(player);}
			public boolean spawn(){var facing=aim.direction();entity=EffectCarrierSpawner.entity(player,SpellDefinition.PHYSICAL_SHIELD,player.getEyePosition().add(facing),facing);return player.level().addFreshEntity(entity);}
			public void discardPrevious(){if(previous!=null)previous.discard();}
			public void remember(){shields((ServerLevel)player.level()).put(player.getUUID(),entity);}
		});
	}
	public static void breakMaintenance(ServerPlayer player) { var shield=shields((ServerLevel)player.level()).get(player.getUUID());if(shield!=null) shield.breakMaintenance(); }

	public static void tick(ServerPlayer player) {
		var id=player.getUUID();
		var shield=shields((ServerLevel)player.level()).get(id);
		if(shield!=null&&(shield.isRemoved()||shield.level()!=player.level()||!player.isAlive())) {
			shield.discard();shields((ServerLevel)player.level()).remove(id);
		}
	}
	public static void clear(ServerPlayer player) {
		var shield=shields((ServerLevel)player.level()).remove(player.getUUID());
		if(shield!=null)shield.discard();
	}
	private PhysicalShieldRuntimeAdapter() { }
}
