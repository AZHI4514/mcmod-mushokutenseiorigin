package com.example.examplemod.magic;

import com.example.examplemod.magic.domain.runtime.TickQuota;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;

/** Shared cosmetic packet budget; gameplay never depends on whether a particle was emitted. */
public final class ParticlePacketAdapter {
	private static final class Runtime {
		private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<TickQuota> BUDGET =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("particle_budget_runtime"),
				builder -> builder.initializer(() -> new TickQuota(180)));
	}
	public static void send(ServerLevel level,ParticleOptions type,double x,double y,double z,int count,double dx,double dy,double dz,double speed){
		if(level.players().stream().noneMatch(p->p.distanceToSqr(new Vec3(x,y,z))<96*96))return;
		var budget=level.getAttachedOrCreate(Runtime.BUDGET);if(!budget.reserve(level.getGameTime(),1))return;level.sendParticles(type,x,y,z,Math.clamp(count,0,80),dx,dy,dz,speed);
	}
	private ParticlePacketAdapter(){}
}
