package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.*;
/** World strategy for one core-powered barrier; lifecycle and serialization stay shared. */
interface PoweredBarrierEffect {
	int coreCount();double radius();boolean hemisphere();
	boolean prepare(ServerPlayer owner,MagicAim aim,MagicAreaEntity area,PoweredBarrierRuntimeAdapter.State state);
	default void committed(ServerLevel level,PoweredBarrierRuntimeAdapter.State state){}
	boolean maintain(ServerLevel level,MagicAreaEntity area,PoweredBarrierRuntimeAdapter.State state,ServerPlayer owner,long now);
}
