package com.example.examplemod.magic.spell.water.ice_pillar;
import com.example.examplemod.magic.domain.model.Box3d;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.domain.combat.EffectRules;
public final class IcePillarRules {
	/** Base is the selected block's minimum corner, not its center. */
	public static Box3d bounds(Vec3d base) {
		return new Box3d(base.x,base.y,base.z,base.x+1,base.y+IcePillarSpec.HEIGHT,base.z+1);
	}
	public static int freezeTicks(float roll,boolean canFreeze,boolean warm) {
		return EffectRules.freezeTicks(roll,IcePillarSpec.FREEZE_CHANCE,IcePillarSpec.FREEZE_TICKS,canFreeze,warm);
	}
	private IcePillarRules() { }
}
