package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.*;
import com.example.examplemod.infrastructure.fabric.magic.PreparationRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.special.disturb_magic.DisturbEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record DisturbMagicAdapter(ServerPlayer player) implements SelfEffect {
	private static ServerPlayer counterTarget(ServerPlayer owner,MagicAim aim){
		var target=SupportRayAdapter.trace(owner,aim,24,t->t instanceof ServerPlayer other
			&& MagicEffects.allowed(owner,other) && PreparationRuntimeAdapter.counterable(other));
		return target instanceof ServerPlayer p?p:null;
	}
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	public static boolean cast(ServerPlayer owner,MagicAim aim){
		var level=(ServerLevel)owner.level();var target=counterTarget(owner,aim);
		return DisturbEffect.apply(target==null?null:new DisturbEffect.Target(){
			public void cancel(){PreparationRuntimeAdapter.cancel(target);}public void preventCasting(){target.setAttached(MagicAttachments.COUNTER_UNTIL,level.getGameTime()+30);}
			public void visual(){level.sendParticles(SpellParticles.burst(SpellDefinition.DISTURB_MAGIC),target.getX(),target.getEyeY(),target.getZ(),56,.4,.5,.4,.12);}
		});
	}
}
