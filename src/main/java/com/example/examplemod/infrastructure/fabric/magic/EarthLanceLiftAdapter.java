package com.example.examplemod.infrastructure.fabric.magic;
import java.util.List;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.earth.earth_lance.EarthLanceLift;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
/** World mutations for the already-preflighted self-support branch. */
public final class EarthLanceLiftAdapter {
	public static void apply(ServerPlayer player,List<BlockPos> plan,List<Entity> targets,MagicAim aim) {
		EarthLanceLift.apply(targets,new EarthLanceLift.World<Entity>() {
			public void launch(Entity target) { EntityImpulseAdapter.launch(target,com.example.examplemod.infrastructure.fabric.geometry.LaunchImpulseAdapter.launchVelocity(new Vec3(0,1,0),3)); }
			public void growPermanent() { com.example.examplemod.infrastructure.fabric.magic.GrowingColumnRuntimeAdapter.growingColumn(player,plan,SpellDefinition.EARTH_LANCE,true); }
			public void placePermanent() { for(var pos:plan)player.level().setBlockAndUpdate(pos,Blocks.DIRT.defaultBlockState()); }
			public void show() { SpellVisuals.burst((ServerLevel)player.level(),SpellDefinition.EARTH_LANCE,Vec3.atBottomCenterOf(plan.getFirst()),aim.direction()); }
		});
	}
	private EarthLanceLiftAdapter() { }
}
