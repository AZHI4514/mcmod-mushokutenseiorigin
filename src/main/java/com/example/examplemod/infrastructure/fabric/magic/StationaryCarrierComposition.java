package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import net.minecraft.core.BlockPos;
import java.util.function.*;

public final class StationaryCarrierComposition {
	private static final StationaryCarrierEffect CONTACT=new StationaryContactEffect(false);
	private static final java.util.Map<SpellDefinition,StationaryCarrierEffect> EFFECTS=java.util.Map.of(
		SpellDefinition.WATERFALL,new WaterfallCarrierEffect(),SpellDefinition.ICE_LANCE,new IceLanceCarrierEffect(),SpellDefinition.FLAME_PILLAR,new StationaryContactEffect(true));
	public static void apply(DirectionalSpellCarrier carrier,ServerPlayer owner,ServerLevel level,float yaw,int age){EFFECTS.getOrDefault(carrier.spell(),CONTACT).apply(carrier,owner,level,yaw,age);}
	private StationaryCarrierComposition(){}
}
