package com.example.examplemod.infrastructure.fabric.magic;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.LocalEffect;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.water.ice_lance.IceLanceSpec;
/** Owns target resolution and registration of the existing stationary visual/hit carrier. */
public record IceLanceAdapter(ServerPlayer player) implements LocalEffect {
	@Override public Optional<Vec3d> locate(float yaw,float pitch) {
		var hit=SpellRaycastAdapter.trace(player,new MagicAim(yaw,pitch),IceLanceSpec.RANGE);
		if(hit==null)return Optional.empty();
		return Optional.of(new Vec3d(hit.base().getX()+.5,hit.base().getY(),hit.base().getZ()+.5));
	}
	@Override public boolean place(Vec3d base,float yaw,float pitch) {
		return player.level().addFreshEntity(EffectCarrierSpawner.entity(player,SpellDefinition.ICE_LANCE,new Vec3(base.x,base.y,base.z),new MagicAim(yaw,pitch).direction()));
	}
	public boolean cast(MagicAim aim) {
		var base=locate(aim.yaw(),aim.pitch());
		return base.isPresent()&&place(base.get(),aim.yaw(),aim.pitch());
	}

}
