package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionRules;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.*;
import net.minecraft.world.phys.Vec3;
import static com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter.bounds;
import static com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter.visual;
final class NuclearExplosionFieldEffect implements MaintainedFieldEffect {
	public com.example.examplemod.magic.mechanic.field.MaintainedFieldSpec spec(){return com.example.examplemod.magic.mechanic.field.MaintainedFieldSpec.NUCLEAR;}
	public boolean ready(ServerPlayer owner){return NuclearExplosionAdapter.ready(owner);}
	public void released(ServerPlayer owner,long now){NuclearExplosionAdapter.markReleased(owner,now);}
	public void update(ServerLevel level,MagicAreaEntity area,MaintainedFieldRuntimeAdapter.State s,ServerPlayer owner,long now,boolean pulse){
		com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionTick.tick(new com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionTick.World(){
			public long age(){return now-s.start;}public boolean exploded(){return s.exploded;}public void commitExplosion(){s.exploded=true;}
			public void targets(){double r=NuclearExplosionRules.radius(s.extra);float damage=NuclearExplosionRules.damage(s.extra);
				for(var t:level.getEntitiesOfClass(LivingEntity.class,bounds(area),t->t.isAlive()&&!t.isSpectator())){
					if(t!=owner&&!MagicEffects.allowed(owner,t)||t.getBoundingBox().getCenter().distanceToSqr(area.position())>r*r||!SpellVisibilityAdapter.visible(level,area.position(),t,area))continue;
					t.hurtServer(level,MagicDamage.source(area,area.spell()),damage);
				}
			}
			public void terrain(){TerrainLeaseRepository.excavate(owner,area.position(),(int)NuclearExplosionRules.radius(s.extra),1,8192);}
			public boolean visualDue(){return now%3==0;}public void visual(){MaintainedFieldRuntimeAdapter.visual(area,s,now-s.start,true);}
		});
	}
}
