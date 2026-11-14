package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.domain.visual.motion.ReferenceMeshMotion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ReferenceMeshMotionModuleTest {
	@Test void explicitStrategiesPreserveFrozenFirstMatchSampling(){
		String[] ids={"blast","bora_blast","magma_gush","flame_tornado","violent_storm","tornado","vacuum_storm","sky_tempest","absolute_zero","inferno","flash_over","quagmire","earth_hedgehog","sonic_blast","frost_nova","lightning","electric","cumulonimbus","tornado_impact","exodus_flame","blizzard_storm","water_ball","fireball","rock_bullet","ice_smash","water_shield","waterfall","wind_bind","unknown"};
		String[] roles={"crust","droplet","front","smoke","rubble","coldstream","bolt","combustion","icegravel","fracture","splash","liquid","heart","icecore","rock","drop","tongue","spark","crystal","chip","spray","current","other"};
		var random=new java.util.Random(29814);
		for(String id:ids)for(String role:roles)for(int stage=0;stage<3;stage++)for(float age:new float[]{-1,0,.5F,5,12,20,80,160,240,401}){
			int index=random.nextInt(20);float cx=random.nextFloat()-.5F,cy=random.nextFloat()-.5F,cz=random.nextFloat()-.5F;float[] v={random.nextFloat()-.5F,random.nextFloat()-.5F,random.nextFloat()-.5F};
			assertEquals(FrozenReferenceMeshMotion.alpha(id,role,index,age,stage),ReferenceMeshMotion.alpha(id,role,index,age,stage));
			assertArrayEquals(FrozenReferenceMeshMotion.deform(id,role,index,cx,cy,cz,v,age,stage),ReferenceMeshMotion.deform(id,role,index,cx,cy,cz,v,age,stage));
		}
	}
}
