package com.example.examplemod.magic.spell.wind.wind_blast;
import com.example.examplemod.magic.domain.model.Vec3d;
/** A successful impulse remains successful even when vanilla rejects an effect refresh. */
public final class WindBlastEffect {
	public interface Caster {
		void launch(Vec3d velocity);
		boolean slowFall(int ticks);
	}
	public static boolean apply(Vec3d aim,Caster caster) {
		caster.launch(WindBlastRules.velocity(aim.scale(-1)));
		caster.slowFall(WindBlastRules.SLOW_FALL_TICKS);
		return true;
	}
	private WindBlastEffect() { }
}
