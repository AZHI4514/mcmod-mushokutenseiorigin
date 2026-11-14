package com.example.examplemod.magic.spell.holy.divine_judge;
public final class DivineJudgeTick {
	public interface World {
		DivineJudgePulse state();long now();boolean ownerPresent();boolean alive();boolean sameLevel();boolean adopt();
		void removeState();void discard();void commit(DivineJudgePulse pulse);void strike(int index);
	}
	public static void tick(World w){
		var pulse=w.state();int index=pulse==null?-1:DivineJudgeRules.pulseIndex(w.now()-pulse.start());
		if(index<0||!w.ownerPresent()||!w.alive()||!w.sameLevel()||!w.adopt()){w.removeState();w.discard();return;}
		if(index<pulse.count())return;
		w.commit(new DivineJudgePulse(pulse.start(),index+1));w.strike(index);
	}
	private DivineJudgeTick() { }
}
