package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
/** Creates the existing registered carrier without changing its registration or saved identity. */
public final class EffectCarrierSpawner {
	public static DirectionalSpellCarrier entity(ServerPlayer owner,SpellDefinition spell,Vec3 pos,Vec3 direction) {
		var entity=new DirectionalSpellCarrier(MagicSystem.PHASE_TWO,owner.level());entity.configure(owner,spell,pos,direction);return entity;
	}
	public static boolean launch(ServerPlayer player,SpellDefinition spell,MagicAim aim) {
		return player.level().addFreshEntity(entity(player,spell,player.getEyePosition(),aim.direction()));
	}
	private EffectCarrierSpawner() {}
}
