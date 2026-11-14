package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.DirectionalSpellCarrier;
import net.minecraft.server.level.*;
/** Per-spell world behavior of a stationary carrier. */
interface StationaryCarrierEffect {void apply(DirectionalSpellCarrier carrier,ServerPlayer owner,ServerLevel level,float yaw,int age);}
