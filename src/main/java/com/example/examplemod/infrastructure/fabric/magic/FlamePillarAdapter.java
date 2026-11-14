package com.example.examplemod.infrastructure.fabric.magic;
import java.util.*;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.LocalEffect;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarSpec;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
/** Per-release selection, with a boolean no-effect result for rejected fire placement. */
public final class FlamePillarAdapter implements LocalEffect {
	private final ServerPlayer player;
	private LivingEntity target;
	private BlockHitResult block;
	private boolean located;
	public FlamePillarAdapter(ServerPlayer player) { this.player=player; }
	@Override public Optional<Vec3d> locate(float yaw,float pitch) {
		located=false;target=null;block=null;
		var aim=new MagicAim(yaw,pitch);
		var hit=SpellRaycastAdapter.trace(player,aim,FlamePillarSpec.RANGE);
		if(hit==null)return Optional.empty();
		target=hit.target();
		if(target==null) {
			block=SpellRaycastAdapter.aim(player,aim);
			if(block==null||block.getType()!=HitResult.Type.BLOCK)return Optional.empty();
		}
		located=true;var point=hit.point();return Optional.of(new Vec3d(point.x,point.y,point.z));
	}
	@Override public boolean place(Vec3d base,float yaw,float pitch) {
		if(!located)throw new IllegalStateException("Fire target was not located");
		located=false;var selected=target;var surface=block;target=null;block=null;
		if(selected!=null)return com.example.examplemod.infrastructure.fabric.magic.FlamePillarBurnRuntimeAdapter.igniteTarget(player,selected,new MagicAim(yaw,pitch));
		return igniteBlock(surface);
	}
	private boolean igniteBlock(BlockHitResult hit) {
		var level=(ServerLevel)player.level();
		return com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarFirePlacement.apply(
				List.of(hit.getBlockPos().relative(hit.getDirection()),hit.getBlockPos().above()),
				new com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarFirePlacement.World<net.minecraft.core.BlockPos>() {
			public boolean mayChange(net.minecraft.core.BlockPos pos) { return com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(player,pos); }
			public boolean existingFire(net.minecraft.core.BlockPos pos) { return level.getBlockState(pos).is(Blocks.FIRE)||level.getBlockState(pos).is(Blocks.SOUL_FIRE); }
			public boolean air(net.minecraft.core.BlockPos pos) { return level.getBlockState(pos).isAir(); }
			public boolean placeSurvivingFire(net.minecraft.core.BlockPos pos) {
				var fire=net.minecraft.world.level.block.BaseFireBlock.getState(level,pos);
				return fire.canSurvive(level,pos)&&level.setBlockAndUpdate(pos,fire);
			}
		});
	}
	public boolean cast(MagicAim aim) {
		var point=locate(aim.yaw(),aim.pitch());return point.isPresent()&&place(point.get(),aim.yaw(),aim.pitch());
	}
}
