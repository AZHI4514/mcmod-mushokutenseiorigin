package com.example.examplemod.magic.spell.water.ice_lance;
import com.example.examplemod.magic.application.port.LocalHitTargets;
import com.example.examplemod.magic.domain.geometry.SweptVolume;
import com.example.examplemod.magic.domain.model.Vec3d;
/** Stateless first-age hit coordination; entity age remains the authoritative saved clock. */
public final class IceLanceHit {
	public static <T> void apply(int age,Vec3d center,Vec3d horizontalDirection,LocalHitTargets<T> targets) {
		if(!IceLanceRules.damageDue(age))return;
		var volume=new SweptVolume(horizontalDirection,IceLanceSpec.WIDTH,IceLanceSpec.HEIGHT,IceLanceSpec.DEPTH);
		for(var target:targets.candidates()) {
			if(!volume.intersects(targets.bounds(target),center)||!targets.visible(target))continue;
			targets.damage(target);
		}
	}
	private IceLanceHit() { }
}
