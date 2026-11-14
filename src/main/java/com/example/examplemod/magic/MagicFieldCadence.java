package com.example.examplemod.magic;

import com.example.examplemod.infrastructure.fabric.magic.SpellVisibilityAdapter;

import java.util.HashMap;
import net.minecraft.world.entity.LivingEntity;

/** Persisted per-target field cadence. Instant spells and the original 39 are unaffected. */
public final class MagicFieldCadence {
	public static boolean ready(long now,long previous,int interval){return com.example.examplemod.magic.mechanic.field.FieldSchedule.cadenceReady(now,previous,interval);}
	public static boolean claim(LivingEntity target,String element,int interval){
		if(element.equals("water")&&interval>10){for(var entity:target.level().getEntities(target,target.getBoundingBox().inflate(9),e->e instanceof MagicAreaEntity area&&area.spell()==SpellDefinition.BLIZZARD_STORM)){
			var area=(MagicAreaEntity)entity;if(area.getOwner() instanceof net.minecraft.server.level.ServerPlayer owner&&MagicEffects.allowed(owner,target)&&target.getBoundingBox().getCenter().distanceToSqr(owner.position().add(0,1,0))<=64&&SpellVisibilityAdapter.visible((net.minecraft.server.level.ServerLevel)target.level(),owner.position().add(0,1,0),target,area)){interval=10;break;}
		}}
		long now=target.level().getGameTime();var prior=target.getAttachedOrCreate(MagicAttachments.FIELD_HITS);
		if(!ready(now,prior.getOrDefault(element,Long.MIN_VALUE/2),interval))return false;
		var next=new HashMap<>(prior);next.put(element,now);target.setAttached(MagicAttachments.FIELD_HITS,java.util.Map.copyOf(next));return true;
	}
	private MagicFieldCadence(){}
}
