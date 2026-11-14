package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionRules;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.*;
import net.minecraft.world.phys.Vec3;
import static com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter.bounds;
import static com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter.visual;
final class GravityMagicFieldEffect implements MaintainedFieldEffect {
	public com.example.examplemod.magic.mechanic.field.MaintainedFieldSpec spec(){return com.example.examplemod.magic.mechanic.field.MaintainedFieldSpec.GRAVITY;}
	public void update(ServerLevel level,MagicAreaEntity area,MaintainedFieldRuntimeAdapter.State s,ServerPlayer owner,long now,boolean pulse) {
		for(var t:level.getEntitiesOfClass(LivingEntity.class,bounds(area),t->t.isAlive()&&!t.isSpectator())){

				com.example.examplemod.magic.spell.special.gravity_magic.GravityContact.apply(new com.example.examplemod.magic.spell.special.gravity_magic.GravityContact.Target(){
					final Vec3 offset=t.getBoundingBox().getCenter().subtract(area.position());
					public double height(){return offset.y;}public double horizontalSquared(){return offset.horizontalDistanceSqr();}
					public boolean allowed(){return t==owner||MagicEffects.allowed(owner,t);}public boolean visible(){return SpellVisibilityAdapter.visible(level,area.position().add(0,Math.clamp(offset.y,.2,12),0),t,area);}
					public void heavyEffects(){t.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,25,1),owner);t.addEffect(new MobEffectInstance(MagicEffects.HEAVY_GRAVITY,25,0),owner);}
					public void hurt(){t.hurtServer(level,MagicDamage.source(area,area.spell()),2);}public void slowFalling(){t.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,40,0),owner);}
					public boolean immune(){return MagicControl.immune(t);}public void lift(){t.setDeltaMovement(t.getDeltaMovement().add(0,.03,0));t.hurtMarked=true;}
				},s.mode,pulse);
			
		}
		if(now%3==0)visual(area,s,now-s.start,false);
	}
}
