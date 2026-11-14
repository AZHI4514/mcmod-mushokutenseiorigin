package com.example.examplemod.magic;

import com.example.examplemod.infrastructure.fabric.magic.ChannelRuntimeAdapter;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;

/** Typed server-thread prerequisite and committed-hit Observer boundaries. */
public final class SpellHooks {
	public enum CastSource { KEYBOARD, WAND }
	@FunctionalInterface public interface Requirement { boolean available(ServerPlayer player,String id); }
	@FunctionalInterface public interface HitObserver { void afterHit(Projectile projectile,Entity target); }
	private static final Event<Requirement> REQUIREMENTS=EventFactory.createArrayBacked(Requirement.class,
		listeners -> (player,id) -> {for(var listener:listeners)if(listener.available(player,id))return true;return false;});
	private static final Event<HitObserver> HITS=EventFactory.createArrayBacked(HitObserver.class,
		listeners -> (projectile,target) -> {for(var listener:listeners)listener.afterHit(projectile,target);});
	public static void registerRequirement(Requirement requirement){REQUIREMENTS.register(java.util.Objects.requireNonNull(requirement));}
	public static void registerHitObserver(HitObserver observer){HITS.register(java.util.Objects.requireNonNull(observer));}
	public static void afterHit(Projectile projectile,Entity target){HITS.invoker().afterHit(projectile,target);}
	public static boolean canUse(ServerPlayer player,SpellDefinition spell,CastSource source){
		return source==CastSource.KEYBOARD&&(spell.castMode()==SpellDefinition.CastMode.INSTANT||ChannelRuntimeAdapter.isChannel(spell))
			&&spell.prerequisitesMet(id -> REQUIREMENTS.invoker().available(player,id));
	}
	private SpellHooks(){}
}
