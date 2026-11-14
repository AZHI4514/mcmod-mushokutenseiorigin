package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.*;
import net.minecraft.core.*;
public final class TerraFallGroundTerrain implements GroundTerrainEffect {
	public void apply(ServerPlayer owner,MagicAreaEntity area,int pulse) {
		if(pulse<0)return;
			var rubble=new java.util.LinkedHashMap<BlockPos,net.minecraft.world.level.block.state.BlockState>();
			var level=(ServerLevel)owner.level();
			for(int i=0;i<682;i++){
				double a=i*2.3999632297+pulse*.7, r=19.5*Math.sqrt((i+.5)/682);
				var p=BlockPos.containing(area.position().add(Math.cos(a)*r,0,Math.sin(a)*r));
				for(int y=3;y>=-3;y--) {
					var q=p.offset(0,y,0);
					if(!level.hasChunkAt(q))continue;
					if(level.getBlockState(q).isAir() && level.getBlockState(q.below()).isFaceSturdy(level,q.below(),Direction.UP)) {rubble.put(q,MagicTerrain.EXPANSION_DIRT.defaultBlockState());break;}
				}
			}
			TerrainLeaseRepository.enqueue(owner,rubble,1800,false);
	}
}
