package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import java.util.*;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.*;

/** Server-owned committed work queue, independent of casting/channel state. */
public final class FlamePillarBurnRuntimeAdapter {
	private record Burn(LivingEntity target,DirectionalSpellCarrier carrier,com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarBurn task) { }
	private static final AttachmentType<BurnRuntime> BURN_RUNTIME=AttachmentRegistry.create(ExampleMod.id("burn_runtime"),b->b.initializer(BurnRuntime::new));
	private static final class BurnRuntime { final Map<UUID,List<Burn>> burns=new HashMap<>(); }
	private static Map<UUID,List<Burn>> burns(ServerPlayer owner) { return owner.level().getServer().overworld().getAttachedOrCreate(BURN_RUNTIME).burns; }
	public static boolean igniteTarget(ServerPlayer owner,LivingEntity target,MagicAim aim) {
		var carrier=EffectCarrierSpawner.entity(owner,SpellDefinition.FLAME_PILLAR,target.position(),aim.direction());
		com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarIgnition.apply(new com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarIgnition.World() {
			public void ignite(int ticks) { target.igniteForTicks(ticks); }
			public void damage() { CarrierDamageAdapter.damage(carrier,target); }
			public void placeFire() { FlamePillarBurnRuntimeAdapter.placeFire(owner,target); }
			public void burst() { SpellVisuals.burst((ServerLevel)owner.level(),SpellDefinition.FLAME_PILLAR,target.position(),aim.direction()); }
			public void schedule() { burns(owner).computeIfAbsent(owner.getUUID(),k->new ArrayList<>()).add(new Burn(target,carrier,new com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarBurn(owner.level().getGameTime()))); }
		});return true;
	}
	public static void tick(ServerPlayer owner) {
		var list=burns(owner).get(owner.getUUID());if(list==null)return;long now=owner.level().getGameTime();
		list.removeIf(b->b.task.tick(now,new com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarBurn.Target() {
			public boolean alive() { return b.target.isAlive(); }
			public boolean sameLevel() { return b.target.level()==owner.level(); }
			public void damage() { CarrierDamageAdapter.damage(b.carrier,b.target); }
		}));if(list.isEmpty())burns(owner).remove(owner.getUUID());
	}

	private static void placeFire(ServerPlayer owner,LivingEntity target) {
		var level=(ServerLevel)owner.level();var pos=target.blockPosition();
		if(com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,pos)&&level.getBlockState(pos).isAir()) {
			var fire=net.minecraft.world.level.block.BaseFireBlock.getState(level,pos);if(fire.canSurvive(level,pos))level.setBlockAndUpdate(pos,fire);
		}
	}

	public static void initialize() {}
	public static void clear(ServerPlayer owner){burns(owner).remove(owner.getUUID());}
	public static void clearServer(net.minecraft.server.MinecraftServer server){server.overworld().removeAttached(BURN_RUNTIME);}
	private FlamePillarBurnRuntimeAdapter() {}
}
