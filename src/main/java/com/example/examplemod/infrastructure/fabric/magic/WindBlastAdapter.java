package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
/** Vanilla motion packet and effect application remain at the world boundary. */
public final class WindBlastAdapter implements SelfEffect {
	private final ServerPlayer player;
	public WindBlastAdapter(ServerPlayer player) { this.player=player; }
	@Override public boolean apply(float yaw,float pitch) {
		var aim=new MagicAim(yaw,pitch);
		return com.example.examplemod.magic.spell.wind.wind_blast.WindBlastEffect.apply(
			com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(aim.direction()),
			new com.example.examplemod.magic.spell.wind.wind_blast.WindBlastEffect.Caster() {
				public void launch(com.example.examplemod.magic.domain.model.Vec3d v) { EntityImpulseAdapter.launch(player,new net.minecraft.world.phys.Vec3(v.x,v.y,v.z)); }
				public boolean slowFall(int ticks) { return player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,ticks,0)); }
			});
	}
}
