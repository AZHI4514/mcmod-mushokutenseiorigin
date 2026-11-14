package com.example.examplemod.infrastructure.fabric.magic;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
public final class EntityImpulseAdapter {
	public static void launch(net.minecraft.world.entity.Entity target,Vec3 motion) {
		target.setOnGround(false); target.setDeltaMovement(motion); target.hurtMarked=true;
		if(target instanceof ServerPlayer player) player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(player));
	}
	private EntityImpulseAdapter(){}
}
