package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.summon.spirit_summon.SpiritSummonRules;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.Vec3;
import java.util.*;
/** Template Method fixes command/target validation order; specialized behavior owns movement and effects. */
abstract class SummonBehavior {
	final void act(ServerLevel level,Mob mob,ServerPlayer owner,boolean pulse) {
		int command=mob.getAttachedOrCreate(SummonRuntimeAdapter.COMMAND);LivingEntity target=null;
		String id=mob.getAttachedOrCreate(SummonRuntimeAdapter.TARGET);
		if(!id.isEmpty()){try{var e=level.getEntity(UUID.fromString(id));if(e instanceof LivingEntity living&&MagicEffects.allowed(owner,living))target=living;}catch(IllegalArgumentException ignored){}}
		target=acquireTarget(level,mob,owner,command,target);
		if(command==3||command==1)target=null;
		if(target!=null&&(target.isRemoved()||!target.isAlive()))target=null;
		perform(level,mob,owner,pulse,command,target);
	}
	protected LivingEntity acquireTarget(ServerLevel level,Mob mob,ServerPlayer owner,int command,LivingEntity configured){return configured;}
	protected abstract void perform(ServerLevel level,Mob mob,ServerPlayer owner,boolean pulse,int command,LivingEntity target);
}
