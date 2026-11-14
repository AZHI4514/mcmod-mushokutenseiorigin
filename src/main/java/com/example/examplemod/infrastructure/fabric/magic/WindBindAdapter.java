package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.MagicControl;
import java.util.Optional;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.LocalEffect;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.wind.wind_bind.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
/** Single-release selection; vanilla tag immunity and addEffect semantics remain authoritative. */
public final class WindBindAdapter implements LocalEffect {
	private final ServerPlayer player;
	private LivingEntity selected;
	private SpellRaycastAdapter.Ray selection;
	public WindBindAdapter(ServerPlayer player) { this.player=player; }
	@Override public Optional<Vec3d> locate(float yaw,float pitch) {
		selected=null;selection=null;
		var hit=SpellRaycastAdapter.trace(player,new MagicAim(yaw,pitch),WindBindSpec.RANGE);
		if(hit==null||hit.target()==null||hit.target().getType().builtInRegistryHolder().is(MagicControl.BIND_IMMUNE))return Optional.empty();
		selected=hit.target();selection=hit;var point=hit.point();return Optional.of(new Vec3d(point.x,point.y,point.z));
	}
	@Override public boolean place(Vec3d base,float yaw,float pitch) {
		if(selected==null)throw new IllegalStateException("Wind bind target was not selected");
		var target=selected;var hit=selection;selected=null;selection=null;
		// Preserve the old configured, unregistered carrier construction before applying the effect.
		EffectCarrierSpawner.entity(player,SpellDefinition.WIND_BIND,hit.point(),new MagicAim(yaw,pitch).direction());
		return WindBindEffect.apply(new WindBindEffect.Target(){
			public boolean apply(int duration) { return target.addEffect(new MobEffectInstance(MagicEffects.WIND_BOUND,duration,0),player); }
			public void showBound() { target.setAttached(MagicAttachments.WIND_BOUND_VISUAL,true); }
		});
	}
	public boolean cast(MagicAim aim) { var point=locate(aim.yaw(),aim.pitch());return point.isPresent()&&place(point.get(),aim.yaw(),aim.pitch()); }
}
