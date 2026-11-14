package com.example.examplemod.bootstrap;

import java.util.List;
import java.util.Map;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import java.util.function.Function;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.casting.CastSpellCommand;
import com.example.examplemod.magic.catalog.SpellFactoryRegistry;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.*;
import com.example.examplemod.magic.spell.fire.fireball.FireballSpec;
import com.example.examplemod.infrastructure.fabric.magic.*;

/** Explicit migrated projectile assembly. Immutable definitions only; no global runtime/player state. */
public final class ProjectileComposition {
	private record Binding(ProjectileSpec spec,Function<ServerPlayer,com.example.examplemod.magic.application.port.ProjectileSpawner> spawners,ProjectileLaunch launch) {
		static Binding vanilla(ProjectileSpec spec,Function<ServerPlayer,? extends Projectile> entities,ProjectileLaunch launch) {
			return new Binding(spec,player->new VanillaProjectileAdapter(player,()->entities.apply(player),launch),launch);
		}
	}
	private static final Map<SpellDefinition,Binding> BINDINGS=Map.ofEntries(
			Map.entry(SpellDefinition.TORNADO_IMPACT,Binding.vanilla(com.example.examplemod.magic.spell.wind.tornado_impact.TornadoImpactSpec.PROJECTILE,player->new SweptSpellProjectile(MagicSystem.TORNADO_IMPACT,player.level()),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.EXODUS_FLAME,Binding.vanilla(com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameSpec.PROJECTILE,player->new SweptSpellProjectile(MagicSystem.EXODUS_FLAME,player.level()),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.WIND_SLICE,new Binding(com.example.examplemod.magic.spell.wind.wind_slice.WindSliceSpec.PROJECTILE,player->new PhaseProjectileAdapter(player,SpellDefinition.WIND_SLICE),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.EXPLOSION,new Binding(com.example.examplemod.magic.spell.fire.explosion.ExplosionSpec.PROJECTILE,player->new PhaseProjectileAdapter(player,SpellDefinition.EXPLOSION),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.FLOOD_FLUSH,new Binding(com.example.examplemod.magic.spell.water.flood_flush.FloodFlushSpec.PROJECTILE,player->new PhaseProjectileAdapter(player,SpellDefinition.FLOOD_FLUSH),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.ICICLE_EDGE,new Binding(com.example.examplemod.magic.spell.water.icicle_edge.IcicleEdgeSpec.PROJECTILE,player->new PhaseProjectileAdapter(player,SpellDefinition.ICICLE_EDGE),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.ICICLE_BREAK,new Binding(com.example.examplemod.magic.spell.water.icicle_break.IcicleBreakSpec.PROJECTILE,player->new PhaseProjectileAdapter(player,SpellDefinition.ICICLE_BREAK),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.WATER_BALL,Binding.vanilla(com.example.examplemod.magic.spell.water.water_ball.WaterBallSpec.PROJECTILE,player->new WaterBallEntity(MagicSystem.WATER_BALL,player.level()),ProjectileLaunch.SERVER_ROTATION)),
			Map.entry(SpellDefinition.FIREBALL,Binding.vanilla(FireballSpec.PROJECTILE,player->new WaterBallEntity(MagicSystem.FIREBALL,player.level()),ProjectileLaunch.SERVER_ROTATION)),
			Map.entry(SpellDefinition.WIND_BALL,Binding.vanilla(com.example.examplemod.magic.spell.wind.wind_ball.WindBallSpec.PROJECTILE,player->new WaterBallEntity(MagicSystem.WIND_BALL,player.level()),ProjectileLaunch.SERVER_ROTATION)),
			Map.entry(SpellDefinition.ROCK_BULLET,Binding.vanilla(com.example.examplemod.magic.spell.earth.rock_bullet.RockBulletSpec.PROJECTILE,player->new WaterBallEntity(MagicSystem.ROCK_BULLET,player.level()),ProjectileLaunch.SERVER_ROTATION)),
			Map.entry(SpellDefinition.WATER_ARROW,Binding.vanilla(com.example.examplemod.magic.spell.water.water_arrow.WaterArrowSpec.PROJECTILE,player->new WaterBallEntity(MagicSystem.WATER_ARROW,player.level()),ProjectileLaunch.SERVER_ROTATION)),
			Map.entry(SpellDefinition.ICE_SMASH,Binding.vanilla(com.example.examplemod.magic.spell.water.ice_smash.IceSmashSpec.PROJECTILE,player->new WaterBallEntity(MagicSystem.ICE_SMASH,player.level()),ProjectileLaunch.SERVER_ROTATION)),
			Map.entry(SpellDefinition.FLAME_SLICE,Binding.vanilla(com.example.examplemod.magic.spell.fire.flame_slice.FlameSliceSpec.PROJECTILE,player->new FlameSliceEntity(MagicSystem.FLAME_SLICE,player.level()),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.ICE_BLADE,Binding.vanilla(com.example.examplemod.magic.spell.water.ice_blade.IceBladeSpec.PROJECTILE,player->new WaterBallEntity(MagicSystem.ICE_BLADE,player.level()),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.WATER_CANNON,Binding.vanilla(com.example.examplemod.magic.spell.water.water_cannon.WaterCannonSpec.PROJECTILE,player->new SweptSpellProjectile(MagicSystem.WATER_CANNON,player.level()),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.EX_FLAME,Binding.vanilla(com.example.examplemod.magic.spell.fire.ex_flame.ExFlameSpec.PROJECTILE,player->new SweptSpellProjectile(MagicSystem.EX_FLAME,player.level()),ProjectileLaunch.REQUESTED_AIM)),
			Map.entry(SpellDefinition.SONIC_BOOM,Binding.vanilla(com.example.examplemod.magic.spell.wind.sonic_boom.SonicBoomSpec.PROJECTILE,player->new SweptSpellProjectile(MagicSystem.SONIC_BOOM,player.level()),ProjectileLaunch.REQUESTED_AIM)));
	public static boolean handles(SpellDefinition spell) { return BINDINGS.containsKey(spell); }
	public static CastSpellCommand create(ServerPlayer player,SpellDefinition definition) {
		var binding=BINDINGS.get(definition);
		if(binding==null)throw new IllegalArgumentException("Unmigrated projectile: "+definition.id());
		var registry=new SpellFactoryRegistry(List.of(new SpellFactoryRegistry.Binding(binding.spec(),new ProjectileSpellFactory())));
		return new CastSpellCommand(binding.spec(),registry.create(binding.spec().id()),new PlayerStateAdapter(player),binding.spawners().apply(player),List.of(
			event->player.setAttached(MagicAttachments.RECOVERY_TOTAL,definition.cooldownTicks()),
			event->SequentialMagic.released(player,definition),
			event->MagicGlow.success(player,definition),
			event->MagicSounds.listener.accept(player,MagicSounds.Phase.RELEASE)),
			error->com.example.examplemod.ExampleMod.LOGGER.error("Committed projectile observer failed: "+definition.id(),error));
	}
	public static CastSpellCommand.Result release(CastSpellCommand command,ServerPlayer player,SpellDefinition definition,MagicAim aim) {
		var input=BINDINGS.get(definition).launch().input(player,aim);
		return command.execute(input.origin(),input.yaw(),input.pitch());
	}
	private ProjectileComposition() { }
}
