package com.example.examplemod.magic.spell.fire.flame_storm;
import com.example.examplemod.magic.mechanic.field.GroundFieldPolicy;
public final class FlameStormPolicy implements GroundFieldPolicy {
	public double radius(){return 9;}public double height(){return 8;}
	public int damagePulse(long age){return age>=0&&age<240?(int)(age/20):-1;}
	public boolean warning(long age){return false;}
	public boolean continuousContacts(){return true;}
	public boolean wall(){return true;}
	public boolean perTickAttack(){return true;}
	public boolean mayClaim(Integer previous,int pulse){return (previous==null?0:previous)<6;}
	public boolean cadenceRequired(int pulse){return true;}
	public int claimedValue(Integer previous,int pulse){return (previous==null?0:previous)+1;}
}
