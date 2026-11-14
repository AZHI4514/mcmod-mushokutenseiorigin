package com.example.examplemod.infrastructure.fabric.magic;
import java.util.Optional;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.LocalEffect;
import com.example.examplemod.magic.domain.model.Vec3d;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.HitResult;
/** Preserve the self-support branch before the living-target ray. */
public final class EarthLanceAdapter implements LocalEffect {
	private final ServerPlayer player;
	private boolean self,selected;
	private SpellRaycastAdapter.Ray hit;
	public EarthLanceAdapter(ServerPlayer player) { this.player=player; }
	@Override public Optional<Vec3d> locate(float yaw,float pitch) {
		return locate(new MagicAim(yaw,pitch),false);
	}
	private Optional<Vec3d> locate(MagicAim aim,boolean targetOnly) {
		selected=false;self=false;hit=null;
		var result=com.example.examplemod.magic.spell.earth.earth_lance.EarthLanceSelection.select(targetOnly,()->{
			var block=SpellRaycastAdapter.aim(player,aim);
			self=block!=null&&block.getType()==HitResult.Type.BLOCK&&block.getDirection()==net.minecraft.core.Direction.UP&&com.example.examplemod.infrastructure.fabric.magic.EarthLanceAdapter.underfoot(block.getBlockPos(),player.blockPosition());
			if(!self)return Optional.<Vec3d>empty();var v=block.getLocation();return Optional.of(new Vec3d(v.x,v.y,v.z));
		},()->{
			hit=SpellRaycastAdapter.trace(player,aim,SpellDefinition.EARTH_LANCE.range());
			if(hit==null||hit.target()==null)return Optional.<Vec3d>empty();var v=hit.point();return Optional.of(new Vec3d(v.x,v.y,v.z));
		});
		selected=result.isPresent();return result;
	}

	@Override public boolean place(Vec3d base,float yaw,float pitch) {
		if(!selected)throw new IllegalStateException("Earth lance target was not selected");selected=false;
		var aim=new MagicAim(yaw,pitch);
		if(self)return EarthLanceConstructionAdapter.construct(player,aim);
		var target=hit.target();var carrier=EffectCarrierSpawner.entity(player,SpellDefinition.EARTH_LANCE,hit.point(),aim.direction());hit=null;
		return com.example.examplemod.magic.spell.earth.earth_lance.EarthLanceHit.apply(new com.example.examplemod.magic.spell.earth.earth_lance.EarthLanceHit.Target() {
			public boolean raise() { return MagicSpikes.raise(player,target.blockPosition()); }
			public void damage() { CarrierDamageAdapter.damage(carrier,target); }
			public void show() { SpellVisuals.burst((ServerLevel)player.level(),SpellDefinition.EARTH_LANCE,target.position(),aim.direction()); }
		});
	}
	public boolean cast(MagicAim aim) {var base=locate(aim.yaw(),aim.pitch());return base.isPresent()&&place(base.get(),aim.yaw(),aim.pitch());}
	/** Legacy instant entry deliberately skips the self-support branch. */
	public boolean castTarget(MagicAim aim) {
		var base=locate(aim,true);return base.isPresent()&&place(base.get(),aim.yaw(),aim.pitch());
	}
	public static boolean underfoot(net.minecraft.core.BlockPos hit,net.minecraft.core.BlockPos casterFeet){return hit.equals(casterFeet.below());}
}
