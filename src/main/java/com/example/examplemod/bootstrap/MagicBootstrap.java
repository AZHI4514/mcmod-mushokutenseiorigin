package com.example.examplemod.bootstrap;
import com.example.examplemod.infrastructure.fabric.magic.*;
import com.example.examplemod.infrastructure.fabric.magic.TeleportationAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SummonRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.PreparationRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.CommunicationAdapter;
import com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter;
import com.example.examplemod.magic.*;

import com.example.examplemod.magic.feature.input.InputSessions;
import com.example.examplemod.magic.feature.input.InputSessions.Session;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.example.examplemod.ExampleMod;

/** Explicit initialization order. No runtime input or entity behavior is implemented here. */
public final class MagicBootstrap {
	public static void initialize() {
		SpellExecutionBindings.validate();
		SpellParticles.initialize();
		MagicConfig.load();
		MagicTerrain.initialize();
		TerrainLeaseRepository.initialize();
		MagicEffects.initialize();
		AreaRuntimeAdapter.initialize();
		PlayerSpellRuntime.initialize();
		MagicAttachments.initialize();
		StoneCharge.initialize(); PreparationRuntimeAdapter.initialize();
		MagicCommands.initialize();
		SupportConsentAdapter.initialize();
		HypnosisAdapter.initialize();
		com.example.examplemod.infrastructure.fabric.magic.FiniteBarrierRuntimeAdapter.initialize();
		SummonRuntimeAdapter.initialize();LightLeases.initialize();MagicCores.initialize();CommunicationAdapter.initialize();TeleportationAdapter.initialize();SequentialMagic.initialize();MaintainedFieldRuntimeAdapter.initialize();PoweredBarrierRuntimeAdapter.initialize();
		com.example.examplemod.bootstrap.MagicServerNetworking.initialize();
		com.example.examplemod.bootstrap.MagicPlayerLifecycle.initialize();
	}

	private MagicBootstrap(){}
}
