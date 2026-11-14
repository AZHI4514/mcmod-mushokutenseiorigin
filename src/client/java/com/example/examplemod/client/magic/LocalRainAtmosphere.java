package com.example.examplemod.client.magic;

import com.example.examplemod.magic.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.Heightmap;

/** Distributed rain columns use only loaded terrain and the shared particle budget. */
final class LocalRainAtmosphere {
	static int emit(Minecraft client,Entity field,int budget,int interval){
		var level=client.level;var random=level.getRandom();boolean storm=field instanceof MagicAreaEntity area && area.spell()==SpellDefinition.CUMULONIMBUS;
		for(int i=0;i<72/interval&&budget>1;i++){
			double angle=random.nextDouble()*Math.PI*2,radius=(storm?24:16)*Math.sqrt(random.nextDouble());
			double x=field.getX()+Math.cos(angle)*radius,z=field.getZ()+Math.sin(angle)*radius;
			var column=BlockPos.containing(x,field.getY(),z);if(!level.hasChunkAt(column))continue;
			int roof=level.getHeight(Heightmap.Types.MOTION_BLOCKING,column.getX(),column.getZ());
			double bottom=Math.max(field.getY(),roof),top=field.getY()+(storm?48:32);if(bottom>=top)continue;
			double y=bottom+random.nextDouble()*(top-bottom);
			level.addParticle(SpellParticles.trail(SpellDefinition.SQUALL),x,y,z,-.025,-.85,0);budget--;
			if(random.nextInt(5)==0&&roof>=field.getY()){level.addParticle(SpellParticles.burst(SpellDefinition.SQUALL),x,roof+.04,z,(random.nextDouble()-.5)*.08,.025,(random.nextDouble()-.5)*.08);budget--;}
		}
		return budget;
	}
	private LocalRainAtmosphere(){}
}
