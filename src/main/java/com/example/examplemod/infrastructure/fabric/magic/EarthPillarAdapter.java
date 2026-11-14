package com.example.examplemod.infrastructure.fabric.magic;
import java.util.ArrayList;
import net.minecraft.world.level.block.Blocks;
import java.util.Optional;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.LocalEffect;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarSpawn;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.phys.*;
public final class EarthPillarAdapter implements LocalEffect {
 private final ServerPlayer player;
 public EarthPillarAdapter(ServerPlayer player){this.player=player;}
 public Optional<Vec3d> locate(float yaw,float pitch){
  var aim=new MagicAim(yaw,pitch);var hit=SpellRaycastAdapter.trace(player,aim,12);var block=SpellRaycastAdapter.aim(player,aim);if(block==null)return Optional.empty();
  var point=hit!=null?Vec3.atBottomCenterOf(hit.base()):block.getLocation();
  return Optional.of(EarthPillarSpawn.top(new Vec3d(point.x,point.y,point.z)));
 }
 public boolean place(Vec3d top,float yaw,float pitch){
  var level=(ServerLevel)player.level();var b=EarthPillarSpawn.bounds(top);var bounds=new AABB(b.minX,b.minY,b.minZ,b.maxX,b.maxY,b.maxZ);
  if(!SpellRaycastAdapter.loaded(level,bounds))return false;
  for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX-1.0E-6,bounds.maxY-1.0E-6,bounds.maxZ-1.0E-6)))if(!level.getBlockState(pos).isAir())return false;
  return level.addFreshEntity(EffectCarrierSpawner.entity(player,SpellDefinition.EARTH_PILLAR,new Vec3(top.x,top.y,top.z),new Vec3(0,-1,0)));
 }
 public boolean cast(MagicAim aim){var top=locate(aim.yaw(),aim.pitch());return top.isPresent()&&place(top.get(),aim.yaw(),aim.pitch());}
	public static boolean permanentStone(ServerPlayer owner,BlockPos center) {
		var level=(ServerLevel)owner.level();var positions=new ArrayList<BlockPos>();
		for(var pos:BlockPos.betweenClosed(center.offset(-1,0,-1),center.offset(1,2,1)))positions.add(pos.immutable());
		return com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarStone.place(positions,new com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarStone.Placement<BlockPos>() {
			public boolean available(BlockPos pos) { return com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,pos)&&level.getBlockState(pos).isAir(); }
			public void place(BlockPos pos) { level.setBlockAndUpdate(pos,Blocks.STONE.defaultBlockState()); }
		});
	}
}
