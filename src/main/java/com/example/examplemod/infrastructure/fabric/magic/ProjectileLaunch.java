package com.example.examplemod.infrastructure.fabric.magic;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import com.example.examplemod.magic.MagicAim;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpawnPlan;
/** Strategy variants preserve two existing vanilla launch contracts. */
public enum ProjectileLaunch {
	SERVER_ROTATION {
		public Input input(ServerPlayer player,MagicAim requested) { return new Input(new Vec3d(player.getX(),player.getEyeY()-.1,player.getZ()),player.getYRot(),player.getXRot()); }
		public void shoot(Projectile projectile,ServerPlayer player,ProjectileSpawnPlan plan) { projectile.shootFromRotation(player,plan.pitch(),plan.yaw(),0,plan.spec().speed(),plan.spec().inaccuracy()); }
	},
	REQUESTED_AIM {
		public Input input(ServerPlayer player,MagicAim requested) { return new Input(new Vec3d(player.getX(),player.getEyeY(),player.getZ()),requested.yaw(),requested.pitch()); }
		public void shoot(Projectile projectile,ServerPlayer player,ProjectileSpawnPlan plan) { var direction=new MagicAim(plan.yaw(),plan.pitch()).direction();projectile.shoot(direction.x,direction.y,direction.z,plan.spec().speed(),plan.spec().inaccuracy()); }
	};
	public record Input(Vec3d origin,float yaw,float pitch) { }
	public abstract Input input(ServerPlayer player,MagicAim requested);
	public abstract void shoot(Projectile projectile,ServerPlayer player,ProjectileSpawnPlan plan);
}
