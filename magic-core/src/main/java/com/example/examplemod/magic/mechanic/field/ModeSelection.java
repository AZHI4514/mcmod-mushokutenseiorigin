package com.example.examplemod.magic.mechanic.field;
public final class ModeSelection {
	public static boolean selectable(com.example.examplemod.magic.SpellDefinition spell){return spell!=null&&(spell.execution()==com.example.examplemod.magic.SpellDefinition.Execution.MODE_FIELD||spell.id().equals("dual_shield"));}
	public static int normalized(int saved,int count){return Math.clamp(saved,0,count-1);}
	public static int next(int current,int count){return (current+1)%count;}
	private ModeSelection() { }
}
