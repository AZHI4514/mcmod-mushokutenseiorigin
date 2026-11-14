package com.example.examplemod.infrastructure.fabric.magic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.WaterSource;
import com.example.examplemod.magic.spell.water.waterfall.WaterfallSpec;
/** Owns the latest water block in one channel; never saves or deletes vanilla water. */
public final class WaterfallSourceAdapter implements WaterSource {
	private final ServerPlayer player;
	private BlockPos water;
	public WaterfallSourceAdapter(ServerPlayer player) { this.player=player; }
	@Override public boolean begin(float yaw,float pitch) {
		if(!generate(yaw,pitch))return false;
		// Retain the legacy post-initial-generation trace used to establish renewal tracking.
		water=null;var hit=SpellRaycastAdapter.trace(player,new MagicAim(yaw,pitch),WaterfallSpec.RANGE);
		if(hit!=null)water=hit.base();
		return true;
	}
	@Override public boolean targetAvailable(float yaw,float pitch) {
		return SpellRaycastAdapter.trace(player,new MagicAim(yaw,pitch),WaterfallSpec.RANGE)!=null;
	}
	@Override public boolean generate(float yaw,float pitch) {
		var aim=new MagicAim(yaw,pitch);var hit=SpellRaycastAdapter.trace(player,aim,WaterfallSpec.RANGE);
		if(hit==null)return false;
		if(!MagicWaterSources.place(player,hit.base())&&!MagicWaterSources.renew(player,hit.base()))return false;
		SpellVisuals.burst((ServerLevel)player.level(),SpellDefinition.WATERFALL,Vec3.atBottomCenterOf(hit.base()),aim.direction());
		water=hit.base();return true;
	}
	@Override public void renew() { if(water!=null)MagicWaterSources.renew(player,water); }
}
