package com.example.examplemod.infrastructure.fabric.magic;
import java.util.stream.IntStream;
import java.util.Optional;
import com.example.examplemod.magic.application.port.TargetedArea;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.water.ice_pillar.*;
import com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter;
/** Instant column placement, preserving damage/launch before deferred growth and visuals. */
public record IcePillarAdapter(ServerPlayer player) implements TargetedArea {
	private java.util.List<BlockPos> plan(BlockPos start) {
		return IntStream.range(0,IcePillarSpec.HEIGHT).mapToObj(start::above).toList();
	}
	/** Resolve and validate before payment. No world mutation occurs here. */
	@Override public Optional<Vec3d> locate(float yaw,float pitch) {
		var hit=SpellRaycastAdapter.trace(player,new MagicAim(yaw,pitch),IcePillarSpec.RANGE);
		if(hit==null||!ColumnPlacementAdapter.canPlace(player,plan(hit.base())))return Optional.empty();
		var base=hit.base();return Optional.of(new Vec3d(base.getX(),base.getY(),base.getZ()));
	}
	/** Compatibility entry for the remaining legacy direct executor. */
	public boolean raise(MagicAim aim,BlockPos start) {
		if(!ColumnPlacementAdapter.canPlace(player,plan(start)))return false;
		apply(new Vec3d(start.getX(),start.getY(),start.getZ()),aim.yaw(),aim.pitch());
		return true;
	}
	/** Called synchronously after successful locate and payment on the server thread. */
	@Override public void apply(Vec3d base,float yaw,float pitch) {
		var start=BlockPos.containing(base.x,base.y,base.z);var plan=plan(start);var aim=new MagicAim(yaw,pitch);
		var level=(ServerLevel)player.level();var spell=SpellDefinition.ICE_PILLAR;
		var carrier=EffectCarrierSpawner.entity(player,spell,Vec3.atCenterOf(start),aim.direction());
		var bounds=GeometryAdapter.box(IcePillarRules.bounds(new Vec3d(start.getX(),start.getY(),start.getZ())));
		var targets=level.getEntities(player,bounds,e->e instanceof LivingEntity);
		for(var target:targets) {
			CarrierDamageAdapter.damage(carrier,(LivingEntity)target);
			EntityImpulseAdapter.launch(target,com.example.examplemod.infrastructure.fabric.geometry.LaunchImpulseAdapter.launchVelocity(new Vec3(0,1,0),IcePillarSpec.LAUNCH_DISTANCE));
		}
		if(!targets.isEmpty())com.example.examplemod.infrastructure.fabric.magic.GrowingColumnRuntimeAdapter.growingColumn(player,plan,spell,false);
		else for(var pos:plan)com.example.examplemod.infrastructure.fabric.magic.TemporaryTerrainAdapter.temporary(player,pos,MagicTerrain.TEMPORARY_ICE,IcePillarSpec.TEMPORARY_TICKS);
		SpellVisuals.burst(level,spell,Vec3.atBottomCenterOf(start),aim.direction());
	}
	public boolean cast(MagicAim aim) {
		var hit=SpellRaycastAdapter.trace(player,aim,SpellDefinition.ICE_PILLAR.range());
		return hit!=null&&raise(aim,hit.base());
	}

}
