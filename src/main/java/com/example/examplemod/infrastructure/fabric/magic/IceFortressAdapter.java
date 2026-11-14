package com.example.examplemod.infrastructure.fabric.magic;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.TargetedArea;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.water.ice_fortress.IceFortressSpec;
/** Per-release validated wall plan. Application is irreversible once target launch starts. */
public final class IceFortressAdapter implements TargetedArea {
	private final ServerPlayer player;
	private List<BlockPos> plan=List.of();
	public IceFortressAdapter(ServerPlayer player) { this.player=player; }
	@Override public Optional<Vec3d> locate(float yaw,float pitch) {
		plan=List.of();var aim=new MagicAim(yaw,pitch);
		// Preserve the legacy read-only aim call before the player-relative base calculation.
		SpellRaycastAdapter.aim(player,aim);
		var start=BlockPos.containing(player.position().add(Vec3.directionFromRotation(0,yaw).scale(2)));
		var direction=aim.direction();
		var candidate=IceFortressSpec.plan(start.getX(),start.getY(),start.getZ(),direction.x,direction.z).stream().map(c->new BlockPos(c.x(),c.y(),c.z())).toList();
		if(!ColumnPlacementAdapter.canPlace(player,candidate))return Optional.empty();
		plan=candidate;return Optional.of(new Vec3d(start.getX(),start.getY(),start.getZ()));
	}
	@Override public void apply(Vec3d base,float yaw,float pitch) {
		if(plan.isEmpty())throw new IllegalStateException("Wall was not preflighted");
		var cells=plan;plan=List.of();var level=(ServerLevel)player.level();var aim=new MagicAim(yaw,pitch);
		com.example.examplemod.magic.spell.water.ice_fortress.IceFortressPlacement.apply(cells,new com.example.examplemod.magic.spell.water.ice_fortress.IceFortressPlacement.World<net.minecraft.world.entity.Entity,BlockPos>() {
			public List<net.minecraft.world.entity.Entity> targets() {
				var bounds=new AABB(cells.getFirst()).minmax(new AABB(cells.getLast()));
				var targets=new ArrayList<net.minecraft.world.entity.Entity>(level.getEntities(player,bounds,e->e instanceof LivingEntity));
				if(player.getBoundingBox().intersects(bounds))targets.add(player);return targets;
			}
			public void launch(net.minecraft.world.entity.Entity target) { EntityImpulseAdapter.launch(target,com.example.examplemod.infrastructure.fabric.geometry.LaunchImpulseAdapter.launchVelocity(new Vec3(0,1,0),3)); }
			public void grow(List<BlockPos> plan) { com.example.examplemod.infrastructure.fabric.magic.GrowingColumnRuntimeAdapter.growingColumn(player,plan,SpellDefinition.ICE_FORTRESS,false); }
			public void place(BlockPos pos,int ticks) { com.example.examplemod.infrastructure.fabric.magic.TemporaryTerrainAdapter.temporary(player,pos,MagicTerrain.TEMPORARY_ICE,ticks); }
			public void burst() { SpellVisuals.burst(level,SpellDefinition.ICE_FORTRESS,Vec3.atBottomCenterOf(cells.getFirst()),aim.direction()); }
		});
	}
	public boolean cast(MagicAim aim) {
		var base=locate(aim.yaw(),aim.pitch());if(base.isEmpty())return false;
		apply(base.get(),aim.yaw(),aim.pitch());return true;
	}
	public static boolean canCast(ServerPlayer player,MagicAim aim) {
		var plan=candidate(player,aim);
		return !plan.isEmpty()&&ColumnPlacementAdapter.canPlace(player,plan);
	}

	private static List<BlockPos> candidate(ServerPlayer player,MagicAim direction){
		SpellRaycastAdapter.aim(player,direction);
		var start=BlockPos.containing(player.position().add(Vec3.directionFromRotation(0,direction.yaw()).scale(2)));
		return IceFortressSpec.plan(start.getX(),start.getY(),start.getZ(),direction.direction().x,direction.direction().z).stream().map(cell->new BlockPos(cell.x(),cell.y(),cell.z())).toList();
	}
}
