package com.example.examplemod.bootstrap;
import java.util.List;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.casting.CastSelfEffectCommand;
import com.example.examplemod.infrastructure.fabric.magic.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
public final class LightningComposition {
	public static CastSelfEffectCommand create(ServerPlayer player,Vec3 locked){
		var spell=SpellDefinition.LIGHTNING;int recovery=spell.cooldownTicks();
		return new CastSelfEffectCommand(spell.id(),spell.manaCost(),recovery,new PlayerStateAdapter(player),(yaw,pitch)->LightningAdapter.execute(player,spell,new MagicAim(yaw,pitch),locked),List.of(
			()->player.setAttached(MagicAttachments.RECOVERY_TOTAL,recovery),()->SequentialMagic.released(player,spell),()->MagicGlow.success(player,spell),()->MagicSounds.listener.accept(player,MagicSounds.Phase.RELEASE)),error->com.example.examplemod.ExampleMod.LOGGER.error("Committed lightning observer failed",error));
	}
	private LightningComposition() { }
}
