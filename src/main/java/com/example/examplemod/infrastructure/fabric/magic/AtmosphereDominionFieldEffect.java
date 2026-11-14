package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.CumulonimbusAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SquallAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SmokeFieldRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionRules;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.*;
import net.minecraft.world.phys.Vec3;
import static com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter.bounds;
import static com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter.visual;
final class AtmosphereDominionFieldEffect implements MaintainedFieldEffect {
	public com.example.examplemod.magic.mechanic.field.MaintainedFieldSpec spec(){return com.example.examplemod.magic.mechanic.field.MaintainedFieldSpec.ATMOSPHERE;}
	public Vec3 target(ServerPlayer owner,SpellDefinition spell,MagicAim aim){return owner.position();}
	public void update(ServerLevel level,MagicAreaEntity area,MaintainedFieldRuntimeAdapter.State s,ServerPlayer owner,long now,boolean pulse) {
		for(var t:level.getEntitiesOfClass(LivingEntity.class,bounds(area),t->t.isAlive()&&!t.isSpectator())){

				com.example.examplemod.magic.spell.wind.atmosphere_dominion.AtmosphereContact.apply(new com.example.examplemod.magic.spell.wind.atmosphere_dominion.AtmosphereContact.Target(){
					Vec3 offset;private Vec3 offset(){if(offset==null)offset=t.getBoundingBox().getCenter().subtract(area.position());return offset;}
					public boolean owner(){return t==owner;}public double height(){return offset().y;}public double horizontalSquared(){return offset().horizontalDistanceSqr();}
					public boolean allowed(){return MagicEffects.allowed(owner,t);}public boolean visible(){return SpellVisibilityAdapter.visible(level,area.position().add(0,Math.clamp(offset().y,.2,64),0),t,area);}
					public boolean claim(){return MagicFieldCadence.claim(t,"wind",20);}public void hurt(){t.hurtServer(level,MagicDamage.source(area,area.spell()),6);}
					public boolean immune(){return MagicControl.immune(t);}public void push(){t.setDeltaMovement(t.getDeltaMovement().add(s.direction.scale(.2)));t.hurtMarked=true;}
				},s.mode,pulse);
			
		}
		if(s.mode==0&&pulse)for(var e:level.getEntities(area,bounds(area),e->e instanceof MagicAreaEntity)){
			var other=(MagicAreaEntity)e;if(other.spell().rank()>=7)continue;
			if(other.spell()==SpellDefinition.SQUALL)SquallAdapter.shorten(other,80);
			else if(other.spell()==SpellDefinition.CUMULONIMBUS)CumulonimbusAdapter.shorten(other,80);
			else if(other.spell()==SpellDefinition.MUD_RAIN)SmokeFieldRuntimeAdapter.shorten(other,80);
		}
		if(now%3==0)visual(area,s,now-s.start,false);
	}
}
