package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.water.lightning.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
public final class LightningAdapter {
	public static Vec3 point(ServerPlayer player,MagicAim aim) {
		var level=(ServerLevel)player.level();var from=player.getEyePosition();var direction=aim.direction();
		for(int i=0;i<64;i++){
			var a=from.add(direction.scale(i));var b=from.add(direction.scale(i+1));if(!SpellRaycastAdapter.loaded(level,new AABB(a,b)))return null;
			var hit=level.clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player));
			if(hit.getType()==HitResult.Type.BLOCK){var at=hit.getLocation().subtract(direction.scale(.02));return level.canSeeSky(BlockPos.containing(at))?at:null;}
		}
		var at=from.add(direction.scale(64));return level.canSeeSky(BlockPos.containing(at))?at:null;
	}
	public static boolean validPoint(ServerPlayer owner,Vec3 at){
		var level=(ServerLevel)owner.level();if(at==null)return false;var from=owner.getEyePosition();var direction=at.subtract(from);int steps=LightningRules.steps(direction.length());for(int i=0;i<=steps;i++)if(!level.hasChunkAt(BlockPos.containing(from.add(direction.scale((double)i/steps)))))return false;return LightningRules.inRange(owner.getEyePosition().distanceToSqr(at))&&level.hasChunkAt(BlockPos.containing(at))&&level.canSeeSky(BlockPos.containing(at))&&level.clip(new ClipContext(owner.getEyePosition(),at,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner)).getType()==HitResult.Type.MISS;
	}
	public static boolean execute(ServerPlayer owner,SpellDefinition spell,MagicAim aim,Vec3 at){
		var level=(ServerLevel)owner.level();var source=new MagicAreaEntity(MagicSystem.MAGIC_AREA,level);source.configure(owner,spell,aim);
		if(!validPoint(owner,at))return false;
		for(var e:level.getEntities(source,new AABB(at.x-2,at.y,at.z-2,at.x+2,at.y+32,at.z+2),e->e instanceof LivingEntity&&e.isAlive()&&!e.isSpectator())){
			var target=(LivingEntity)e;double dx=e.getX()-at.x,dz=e.getZ()-at.z;
			LightningImpact.apply(new LightningImpact.Target(){
				public boolean inside(){return LightningRules.inside(dx,dz);}
				public boolean skyVisible(){return level.canSeeSky(BlockPos.containing(target.getEyePosition()));}
				public boolean owner(){return target==owner;}
				public boolean allowed(){return MagicEffects.allowed(owner,target);}
				public boolean visible(){return SpellVisibilityAdapter.visible(level,at.add(0,.2,0),target,source);}
				public boolean hurt(){return target.hurtServer(level,MagicDamage.thunder(source,owner),spell.damage());}
				public void control(){MagicControl.apply(target,target instanceof Player?20:40);}
				public void afterHit(){SpellHooks.afterHit(source,target);}
			});
		}SpellVisuals.burst(level,spell,at,new Vec3(0,1,0));return true;
	}
	private LightningAdapter() { }
}
