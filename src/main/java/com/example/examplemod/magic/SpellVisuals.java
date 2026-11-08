package com.example.examplemod.magic;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

/** Nearby clients animate successful outcomes. Does not load chunks or create gameplay entities. */
public final class SpellVisuals {
	public static void burst(ServerLevel level, SpellDefinition spell, Vec3 position, Vec3 direction) {
		var packet=new SpellVisualPayload(spell.id(),position,direction);
		for(var viewer:level.players()) {
			if(viewer.position().distanceToSqr(position)<=48*48 && ServerPlayNetworking.canSend(viewer,SpellVisualPayload.TYPE)) ServerPlayNetworking.send(viewer,packet);
		}
	}
	private SpellVisuals() { }
}
