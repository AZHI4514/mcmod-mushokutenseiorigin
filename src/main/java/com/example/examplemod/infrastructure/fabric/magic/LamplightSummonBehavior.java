package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.summon.spirit_summon.SpiritSummonRules;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.Vec3;
import java.util.*;
final class LamplightSummonBehavior extends SummonBehavior {
	protected void perform(ServerLevel level,Mob mob,ServerPlayer owner,boolean pulse,int command,LivingEntity target) {
			mob.setPos(owner.position().add(0,owner.getBbHeight()*.8,0));mob.setInvisible(true);
			if(pulse)LightLeases.update(owner);
			for(int i=0;i<3;i++){double angle=level.getGameTime()*.04+i*Math.PI*2/3;var p=owner.position().add(Math.cos(angle)*1.3,1.3+Math.sin(angle*1.7)*.2,Math.sin(angle)*1.3);
				if(level.getGameTime()%3==0)ParticlePacketAdapter.send(level,SpellParticles.accent(SpellDefinition.LAMPLIGHT_SPIRITS),p.x,p.y,p.z,2,.03,.03,.03,.005);}
	}
}
