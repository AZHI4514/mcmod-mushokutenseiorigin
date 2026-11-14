package com.example.examplemod.bootstrap;
import java.util.Map;
import com.example.examplemod.magic.*;
import com.example.examplemod.infrastructure.fabric.magic.*;
import net.minecraft.world.entity.LivingEntity;
/** Hit side-effect strategies keep tornado impact's distinct direct-hit behavior. */
public final class ProjectileHitComposition {
	@FunctionalInterface private interface Hit { void apply(SweptSpellProjectile projectile,LivingEntity target,SpellDefinition spell,float damage); }
	private static final Hit STANDARD=SweptProjectileHitAdapter::apply;
	private static final Map<SpellDefinition,Hit> SPECIAL=Map.of(SpellDefinition.TORNADO_IMPACT,TornadoImpactHitAdapter::apply);
	public static void hurt(SweptSpellProjectile projectile,LivingEntity target,SpellDefinition spell,float damage){SPECIAL.getOrDefault(spell,STANDARD).apply(projectile,target,spell,damage);}
	private ProjectileHitComposition() {}
}
