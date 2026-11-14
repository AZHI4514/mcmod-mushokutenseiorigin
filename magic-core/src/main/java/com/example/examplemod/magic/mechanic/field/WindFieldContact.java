package com.example.examplemod.magic.mechanic.field;
public final class WindFieldContact {
	public interface Target {boolean excluded();boolean inside();boolean owner();boolean allowed();boolean visible();void pulse();boolean immune();boolean moves();void move();}
	public static void apply(boolean pulse,Target t){if(t.excluded()||!t.inside()||!t.owner()&&!t.allowed()||!t.visible())return;if(pulse)t.pulse();if(!t.immune()&&t.moves())t.move();}
	private WindFieldContact() { }
}
