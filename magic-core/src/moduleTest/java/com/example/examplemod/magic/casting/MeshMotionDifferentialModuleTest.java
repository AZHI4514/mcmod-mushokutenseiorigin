package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.domain.visual.motion.MeshMotion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MeshMotionDifferentialModuleTest {
	@Test void materialStrategiesExactlyMatchFrozenAuthoredMotion(){
		String[] ids={"wind_ball","water_arrow","flame_slice","healing","ice_blade","heat_hand","heat_island","burning_place","detoxification","magic_shield","water_cannon","water_splash","icicle_field","ex_flame","sonic_boom","stone_cannon","ice_pillar","waterfall","ice_lance","icicle_break","icicle_edge","flood_flush","ice_fortress","explosion","flame_pillar","flamethrower","wind_slice","wind_bind","wind_blast","earth_lance","earth_pillar","ex_healing","antidote_ii","physical_shield","unknown"};
		String[] roles={"droplet","crown","ripple","liquidshaft","breaker","ember","firebloom","blastwave","hotcore","emberbed","firetongue","firewake","gust","lifemote","lightseed","lifelight","thermal","wardmote","wardface","barrierveil","growth","groundspike","wallice","rubble","fracture","other"};
		var random=new java.util.Random(287141);
		for(String id:ids){
			assertEquals(FrozenExtendedSpellMotion.handles(id),MeshMotion.handles(id));assertEquals(FrozenExtendedSpellMotion.persistent(id),MeshMotion.persistent(id));assertEquals(FrozenExtendedSpellMotion.shaded(id),MeshMotion.shaded(id));assertEquals(FrozenExtendedSpellMotion.lifetime(id),MeshMotion.lifetime(id));
			for(String role:roles)for(int stage=0;stage<3;stage++)for(float age:new float[]{-1,0,.5F,2,5,12,20,28,32,80}){
				int index=random.nextInt(16);float cx=random.nextFloat()-.5F,cy=random.nextFloat()-.5F,cz=random.nextFloat()-.5F;
				float[] v={random.nextFloat()-.5F,random.nextFloat()-.5F,random.nextFloat()-.5F};
				assertEquals(FrozenExtendedSpellMotion.alpha(id,role,index,age,stage),MeshMotion.alpha(id,role,index,age,stage));
				assertArrayEquals(FrozenExtendedSpellMotion.deform(id,role,index,cx,cy,cz,v,age,stage),MeshMotion.deform(id,role,index,cx,cy,cz,v,age,stage));
			}
		}
	}
}
