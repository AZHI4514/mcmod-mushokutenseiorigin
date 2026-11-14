package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.barrier.magic_shield.MagicShieldDamageGate;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.damagesource.DamageSource;
public record MagicShieldAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return AreaRuntimeAdapter.cast(player,SpellDefinition.MAGIC_SHIELD,new MagicAim(yaw,pitch));}
	public static boolean allowDamage(LivingEntity target,DamageSource source,float amount,Supplier<MagicAreaEntity> find){
		return MagicShieldDamageGate.allow(new MagicShieldDamageGate.Hit(){
			private MagicAreaEntity shield;
			public boolean magicProjectile(){return source.getDirectEntity() instanceof Projectile projectile&&AreaRuntimeAdapter.isMagic(projectile);}
			public boolean shieldAvailable(){shield=find.get();return shield!=null&&!shield.isRemoved()&&shield.level()==target.level();}
			public void absorb(){shield.absorb(amount);}
		});
	}
}
