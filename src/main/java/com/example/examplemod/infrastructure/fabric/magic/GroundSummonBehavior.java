package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.summon.spirit_summon.SpiritSummonRules;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.Vec3;
import java.util.*;
final class GroundSummonBehavior extends CombatSummonBehavior {
	GroundSummonBehavior(SpellDefinition spell){super(spell,6.25);}
	protected void move(Mob mob,Vec3 destination) {
		if(destination.distanceToSqr(mob.position())>2){
			// A path is computed by vanilla navigation and moved through normal collision; no teleport attacks.
			mob.setNoAi(true);mob.setTarget(null);var path=mob.getNavigation().createPath(destination.x,destination.y,destination.z,0);
			if(path!=null&&!path.isDone()){while(!path.isDone()&&path.getNextEntityPos(mob).distanceToSqr(mob.position())<.5)path.advance();if(!path.isDone()){var direction=path.getNextEntityPos(mob).subtract(mob.position());var velocity=mob.getDeltaMovement();mob.setDeltaMovement(direction.normalize().x*.23, direction.y>.4&&mob.onGround()?.42:velocity.y,direction.normalize().z*.23);mob.hurtMarked=true;}}
		}else mob.getNavigation().stop();
	}
}
