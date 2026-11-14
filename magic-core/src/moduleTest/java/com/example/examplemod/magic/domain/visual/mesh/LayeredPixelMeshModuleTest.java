package com.example.examplemod.magic.domain.visual.mesh;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class LayeredPixelMeshModuleTest {
	@Test void geometryStrategiesExactlyMatchFrozenVerticesAndColors(){
		for(String id:List.of("magic_core","squall","ocean_freeze","flame_storm","apocalypse_flame","earthquake","terra_fall","continental_sink","sand_cloud","dust_storm","sandstorm","deep_mist","mud_rain","antidote_iii","exorcist_ray","exorcist_burst","exorcist_judge","holy_ray","sacred_nova","divine_judge","god_purge","atmosphere_dominion","gravity_magic","nuclear_explosion","dual_shield","spirit_sealing_barrier","confinement_barrier","fortress_barrier","demon_god_sealing_barrier","disturb_magic","divination_magic","howling","hypnosis_magic","monster_summon","spirit_summon","lamplight_spirits","necromancy","saint_dragon_battle_aura","communication_magic","teleportation_magic","unknown"))for(float age:new float[]{0,1,2,9,10,20,99,100,110,140})for(boolean impact:new boolean[]{false,true}){
			var expected=FrozenLayeredMesh.build(id,age,.73F,impact);var actual=LayeredPixelMesh.build(id,age,.73F,impact);
			assertEquals(expected.size(),actual.size(),id+" "+age);
			for(int i=0;i<expected.size();i++){assertEquals(expected.get(i).color(),actual.get(i).color());for(int j=0;j<4;j++)assertArrayEquals(expected.get(i).points()[j],actual.get(i).points()[j]);}
		}
	}
	@Test void unknownAndInvisibleGeometryDoesNotAllocateFaces(){assertTrue(LayeredPixelMesh.build("unknown",10,1,false).isEmpty());assertTrue(LayeredPixelMesh.build("magic_core",10,0,false).isEmpty());}
}
